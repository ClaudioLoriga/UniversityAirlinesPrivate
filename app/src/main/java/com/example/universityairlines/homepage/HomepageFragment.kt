package com.example.universityairlines.homepage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.universityairlines.R
import com.example.universityairlines.booking.BookingFragment
import com.example.universityairlines.databinding.ActivityHomepageBinding

class HomepageFragment : Fragment() {

    private lateinit var binding: ActivityHomepageBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE) ?: return
        val stringName = sharedPref.getString(getString(R.string.username_shared_preferences), "utente")
        val stringHomePage = resources.getString(R.string.welcome_user, stringName)
        binding.textView.text = stringHomePage


    }

    companion object {
        const val EXTRAKEY = "username"
    }

}