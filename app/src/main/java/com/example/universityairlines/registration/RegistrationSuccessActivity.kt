package com.example.universityairlines.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.universityairlines.homepage.HomepageActivity
import com.example.universityairlines.databinding.ActivityRegistrationSuccessBinding
import com.example.universityairlines.login.LoginActivity

class RegistrationSuccessActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegistrationSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottoneHome.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        const val EXTRAKEY = "username"
    }
}