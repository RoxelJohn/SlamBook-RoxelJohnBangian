package com.example.bangianslambook

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.bangianslambook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var slideAdapter: SlideAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the ViewPager2 and set the adapter
        viewPager = binding.viewPager
        slideAdapter = SlideAdapter(this)
        viewPager.adapter = slideAdapter

        // Set up the next button to navigate through slides
        binding.btnSlide.setOnClickListener {
            val currentItem = viewPager.currentItem

            // If it's the last slide (SlideThree), go to ActivityHome
            if (currentItem == slideAdapter.itemCount - 1) {
                val intent = Intent(this, ActivityHome::class.java)
                startActivity(intent)
                finish()  // Close the current activity to avoid going back to it
            } else {
                // Otherwise, move to the next slide
                viewPager.currentItem = currentItem + 1
            }
        }

        // Optionally, you can listen to page changes directly (if you want to add any specific logic)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)


                if (position == slideAdapter.itemCount - 1) {
                }
            }
        })
    }
}
