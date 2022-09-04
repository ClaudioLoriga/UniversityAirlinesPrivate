package com.example.universityairlines.registration

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.universityairlines.R
import com.example.universityairlines.repository.UserRepository
import com.example.universityairlines.databinding.ActivityRegistrationBinding
import com.example.universityairlines.databinding.NewRegistrationBinding
import com.example.universityairlines.login.LoginActivity
import com.example.universityairlines.model.ApiRegistrationResult
import com.example.universityairlines.validation.setupValidation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: NewRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = "Registrati"

        binding.bottoneIscrizione.setOnClickListener {
            registrationUser()
            it.isEnabled = false
        }

        //Torna indietro nell'activity del login
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.terminiServizio.setFormatLinkText(
            resources.getString(R.string.terminiecondizioni_base),
            resources.getString(R.string.terminiecondizioni) to ::openWebsite,
            showUnderLine = true
        )

        binding.accedi.setOnClickListener {
            finish()
        }

        with(binding) {
            setupValidation(bottoneIscrizione, nomeInputRegistrazione, cognomeInputRegistrazione, passwordInputRegistrazione, emailInputRegistrazione)
        }
    }

    private fun registrationUser() {
        lifecycleScope.launch {
            binding.loadingAnimation.isVisible = true
            binding.frameLayout.isVisible = true
            val mail = binding.emailInputRegistrazione.text.toString()
            val password = binding.passwordInputRegistrazione.text.toString()
            val nome = binding.nomeInputRegistrazione.text.toString()
            val cognome = binding.cognomeInputRegistrazione.text.toString()

            when (val result = UserRepository.setUser(mail, password, nome, cognome)) {
                is ApiRegistrationResult.Success -> {
                    val intent =
                        Intent(this@RegistrationActivity, RegistrationSuccessActivity::class.java)
                    intent.putExtra(RegistrationSuccessActivity.EXTRAKEY, nome)
                    startActivity(intent)
                }
                is ApiRegistrationResult.Failure<*> -> {
                    // show error
                    MaterialAlertDialogBuilder(this@RegistrationActivity)
                        .setTitle(resources.getString(R.string.attenzione))
                        .setPositiveButton(
                            "Ok"
                        ) { dialog, which -> dialog?.dismiss() }
                        .setMessage(
                            resources.getString(R.string.problema_riprovare)
                        ).show()
                    binding.loadingAnimation.isVisible = false
                    binding.frameLayout.isVisible = false
                }
            }
        }
    }

    // Hyperlink
    @Suppress("SpreadOperator")
    fun TextView.setFormatLinkText(message: String, vararg links: Pair<String, () -> Unit>, showUnderLine: Boolean = false) {
        val linkTexts = links.map { it.first }.toTypedArray()
        val formattedMessage = String.format(message, *linkTexts)
        val spannableString = SpannableString(formattedMessage)

        links.forEach { pair ->
            val linkText = pair.first
            val linkListener = pair.second

            val linkStartIndex = spannableString.indexOf(linkText)
            val linkEndIndex = linkStartIndex + linkText.length

            if (linkStartIndex < 0 || linkEndIndex > spannableString.length) return@forEach

            spannableString.setSpan(
                getClickableSpan(linkListener),
                linkStartIndex,
                linkEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (showUnderLine) {
                spannableString.setSpan(UnderlineSpan(), linkStartIndex, linkEndIndex, 0)
            }
        }

        movementMethod = LinkMovementMethod.getInstance()
        text = spannableString
    }

    private fun getClickableSpan(onClickListener: () -> Unit) = object : ClickableSpan() {
        override fun onClick(widget: View) {
            widget.cancelPendingInputEvents()
            onClickListener()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = ds.linkColor
            ds.typeface = Typeface.DEFAULT_BOLD
        }
    }

    private fun openWebsite() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.ita-airways.com")
        startActivity(intent)
    }
}