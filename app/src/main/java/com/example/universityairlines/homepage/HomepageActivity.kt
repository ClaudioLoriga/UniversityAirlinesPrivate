package com.example.universityairlines.homepage

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.universityairlines.R
import com.example.universityairlines.booking.BookingFragment
import com.example.universityairlines.check_in.CheckInFragment
import com.example.universityairlines.databinding.ActivityHomepageBinding
import com.example.universityairlines.databinding.FragmentContainerHomepageBinding
import kotlin.reflect.jvm.internal.impl.util.Check

class HomepageActivity : AppCompatActivity() {

    private lateinit var binding: FragmentContainerHomepageBinding

    lateinit var page1: BookingFragment
    lateinit var page2: HomepageFragment
    lateinit var page3: CheckInFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentContainerHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        page1 = BookingFragment()
        page2 = HomepageFragment()
        page3 = CheckInFragment()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, page1).commit()
                    true
                }
                R.id.page_2 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, page2).commit()
                    true
                }
                R.id.page_3 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, page3).commit()
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId = R.id.page_2
    }

    companion object {
        const val EXTRAKEY = "username"
    }

}