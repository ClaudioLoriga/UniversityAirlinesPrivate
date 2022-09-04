package com.example.universityairlines.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.universityairlines.R
import com.example.universityairlines.homepage.HomepageActivity
import com.example.universityairlines.registration.RegistrationActivity
import com.example.universityairlines.repository.UserRepository
import com.example.universityairlines.databinding.NewLoginBinding
import com.example.universityairlines.model.ApiResult
import com.example.universityairlines.validation.setupValidation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: NewLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = "Accedi"

        binding.loginbutton.setOnClickListener { checkUser() }

        binding.loginbutton.setOnClickListener {
            checkUser()
            it.isEnabled = false
        }

        //Gestione comportamento bottone registrazione
        binding.registerbutton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        with(binding) { setupValidation(loginbutton, edittextpassword, edittextemail) }
    }

    private fun checkUser() {
        lifecycleScope.launch {
            val mail = binding.edittextemail.text.toString()
            val pwd = binding.edittextpassword.text.toString()
            binding.loadingAnimation.isVisible = true
            binding.frameLayout.isVisible = true


            when (val result = UserRepository.getUser(mail, pwd)) {
                is ApiResult.Success -> {
                    val intent = Intent(this@LoginActivity, HomepageActivity::class.java)
                    val sharedPref = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)
                    sharedPref.edit(commit = true) { putString(getString(R.string.username_shared_preferences), result.value.firstName) }
                    binding.loadingAnimation.isVisible = false
                    binding.frameLayout.isVisible = false
                    startActivity(intent)
                    finish()
                }
                is ApiResult.Failure -> {
                    MaterialAlertDialogBuilder(this@LoginActivity)
                        .setTitle("404 NOT FOUND")
                        .setPositiveButton(
                            "Ok"
                        ) { dialog, which -> dialog?.dismiss() }
                        .setMessage(
                            result.errorResponse?.message ?: "Utente non trovato"
                        ).show()
                    binding.loadingAnimation.isVisible = false
                    binding.frameLayout.isVisible = false
                    binding.loginbutton.isEnabled = true
                }
            }
        }
    }
}