package com.example.bangianslambook

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.bangianslambook.databinding.ActivityUserDetailBinding
import java.util.Calendar

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the data passed from the previous activity
        val name = intent.getStringExtra("Name") ?: "No name available"
        val dob = intent.getStringExtra("DateOfBirth") ?: "No date of birth available"
        val gender = intent.getStringExtra("Gender") ?: "No gender available"
        val address = intent.getStringExtra("Address") ?: "No address available"
        val contactNumber = intent.getStringExtra("ContactNumber") ?: "No contact number available"
        val hobbies = intent.getStringArrayListExtra("Hobbies")?.joinToString(", ") ?: "No hobbies listed"

        // Set the data for other fields, defaulting to "None" if empty
        val nickname = intent.getStringExtra("Nickname")?.takeIf { it.isNotEmpty() } ?: "None"
        val childhoodHero = intent.getStringExtra("ChildhoodHero")?.takeIf { it.isNotEmpty() } ?: "None"
        val quoteSaying = intent.getStringExtra("QuoteSaying")?.takeIf { it.isNotEmpty() } ?: "None"
        val favoriteSong = intent.getStringExtra("FavoriteSong")?.takeIf { it.isNotEmpty() } ?: "None"
        val schoolMemory = intent.getStringExtra("SchoolMemory")?.takeIf { it.isNotEmpty() } ?: "None"

        // Set the data to the TextViews
        binding.nameTextView.text = name
        binding.dobTextView.text = dob
        binding.genderTextView.text = gender
        binding.addressTextView.text = address
        binding.contactTextView.text = contactNumber
        binding.hobbiesTextView.text = hobbies
        binding.nicknameTextView.text = nickname
        binding.childhoodHeroTextView.text = childhoodHero
        binding.quoteTextView.text = quoteSaying
        binding.songTextView.text = favoriteSong
        binding.schoolMemoryTextView.text = schoolMemory

        // Calculate and display age
        val age = calculateAge(dob)
        binding.ageTextView.text = "Age: $age"

        // Get the avatar resource passed from the intent and set it to the ImageView
        val avatarResource = intent.getIntExtra("AvatarResource", R.drawable.default_avatar)
        binding.avatarImageView.setImageResource(avatarResource)

        // Log the avatar resource to check if it's passed correctly
        Log.d("UserDetailActivity", "Avatar Resource: $avatarResource")
        
        // Set onClick listener to navigate back to ActivityHome
        binding.btnBack.setOnClickListener {
            finish()  // Go back to the previous activity
        }
    }

    // Helper method to calculate age from DOB
    private fun calculateAge(dob: String): Int {
        val parts = dob.split("-")
        if (parts.size != 3) return 0 // Return 0 if the DOB format is invalid
        val year = parts[0].toInt()
        val month = parts[1].toInt()
        val day = parts[2].toInt()

        val dobCalendar = Calendar.getInstance()
        dobCalendar.set(year, month - 1, day)

        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
            age-- // Subtract one year if the birthday hasn't occurred yet this year
        }
        return age
    }
}

