package com.example.bangianslambook

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bangianslambook.databinding.ActivityFormBinding
import com.google.gson.Gson
import java.util.Calendar

class ActivityForm : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the DatePicker click listener for DOB field
        binding.dob.setOnClickListener {
            openDatePicker() // Open date picker when the DOB field is clicked
        }

        binding.btnBackItem.setOnClickListener {
            val backIntent = Intent(this, ActivityHome::class.java)
            startActivity(backIntent)
            finish()
        }

        // Gender change listener to update the avatar image based on selected gender
        binding.male.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateAvatar("Male")
            }
        }

        binding.female.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateAvatar("Female")
            }
        }

        binding.other.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                updateAvatar("Other")
            }
        }

        // Submit button click listener
        binding.submitButton.setOnClickListener {
            // Collect form data
            val name = binding.name.text.toString()
            val dob = binding.dob.text.toString()
            val gender = when {
                binding.male.isChecked -> "Male"
                binding.female.isChecked -> "Female"
                binding.other.isChecked -> "Other"
                else -> ""
            }
            val address = binding.address.text.toString()
            val contactNumber = binding.contactNumber.text.toString()
            val hobbies = mutableListOf<String>()
            if (binding.cooking.isChecked) hobbies.add("Cooking")
            if (binding.singing.isChecked) hobbies.add("Singing")
            if (binding.dancing.isChecked) hobbies.add("Dancing")
            if (binding.baking.isChecked) hobbies.add("Baking")
            if (binding.sleeping.isChecked) hobbies.add("Sleeping")
            if (binding.eating.isChecked) hobbies.add("Eating")
            val nickname = binding.nickname.text.toString()
            val childhoodHero = binding.childhoodHero.text.toString()
            val quoteSaying = binding.quoteSaying.text.toString()
            val schoolMemory = binding.schoolMemory.text.toString()
            val favoriteSong = binding.favoriteSong.text.toString()

            // Validate required fields (excluding optional ones)
            if (name.isEmpty() || dob.isEmpty() || gender.isEmpty() || address.isEmpty() ||
                contactNumber.isEmpty() || hobbies.isEmpty()
            ) {
                // Highlight the empty fields and show a message for required fields
                if (name.isEmpty()) binding.name.error = "Name is required"
                if (dob.isEmpty()) binding.dob.error = "Date of Birth is required"
                if (gender.isEmpty()) Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
                if (address.isEmpty()) binding.address.error = "Address is required"
                if (contactNumber.isEmpty()) binding.contactNumber.error = "Contact number is required"
                if (hobbies.isEmpty()) Toast.makeText(this, "Please select at least one hobby", Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }

            // Validate contact number (should start with "09" and have exactly 11 digits)
            if (!contactNumber.matches("^09\\d{9}$".toRegex())) {
                binding.contactNumber.error = "Contact number must start with '09' and be 11 digits long"
                return@setOnClickListener
            }

            // Create UserData object with the selected photo URI (if available)
            val userData = UserData(
                name = name,
                dob = dob,
                gender = gender,
                address = address,
                contactNumber = contactNumber,
                hobbies = hobbies,
                nickname = nickname, // Optional
                childhoodHero = childhoodHero, // Optional
                quoteSaying = quoteSaying, // Optional
                schoolMemory = schoolMemory, // Optional
                favoriteSong = favoriteSong, // Optional
            )

            // Store user data in SharedPreferences
            val sharedPreferences = getSharedPreferences("UserFormData", Context.MODE_PRIVATE)
            val slamsJson = sharedPreferences.getString("Slams", "[]")
            val slamsList = Gson().fromJson(slamsJson, Array<UserData>::class.java).toMutableList()

            // Check if the entry already exists based on name or contact number
            if (slamsList.any { it.name == name }) {
                Toast.makeText(this, "Name was already exists!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Add the new entry to the list
            slamsList.add(userData)

            // Save updated list to SharedPreferences
            val editor = sharedPreferences.edit()
            val updatedSlamsJson = Gson().toJson(slamsList)
            editor.putString("Slams", updatedSlamsJson)
            editor.apply()

            // Display success message
            Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()

            // Get avatar resource based on selected gender
            val avatarResource = when (gender) {
                "Male" -> R.drawable.male
                "Female" -> R.drawable.female
                "Other" -> R.drawable.other
                else -> R.drawable.default_avatar // Default avatar in case of invalid gender
            }

            // Pass data to UserDetailActivity
            val gotodisplay = Intent(this, UserDetailActivity::class.java).apply {
                putExtra("AvatarResource", avatarResource)
                putExtra("Name", name)
                putExtra("DateOfBirth", dob)
                putExtra("Gender", gender)
                // Pass other data as needed...
            }
            startActivity(gotodisplay)

            // Navigate back to ActivityHome
            val intent = Intent(this, ActivityHome::class.java)
            startActivity(intent)
            finish()
        }
        val avatarResource = ""
        Log.d("UserDetailActivity", "Avatar Resource: $avatarResource")


    }

    // Function to open DatePickerDialog
    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Ensure that the year does not exceed 2020
        val maxYear = 2020
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Check if the selected year is greater than 2020
                if (selectedYear > maxYear) {
                    Toast.makeText(this, "Year cannot exceed $maxYear", Toast.LENGTH_SHORT).show()
                    return@DatePickerDialog
                }

                // Format the date in "yyyy-MM-dd"
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                binding.dob.setText(formattedDate) // Set the selected date in the DOB field

                // Calculate the age based on DOB
                calculateAge(selectedYear, selectedMonth, selectedDay)
            },
            year, month, day
        )

        // Optionally, set a max date for the DatePicker (not strictly necessary if manually checking year)
        calendar.set(maxYear, 11, 31) // Set the maximum date to December 31st, 2020
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    // Function to calculate and display the age
    private fun calculateAge(year: Int, month: Int, day: Int) {
        val dob = Calendar.getInstance().apply {
            set(year, month, day)
        }

        val currentDate = Calendar.getInstance()

        var age = currentDate.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (currentDate.get(Calendar.MONTH) < dob.get(Calendar.MONTH) ||
            (currentDate.get(Calendar.MONTH) == dob.get(Calendar.MONTH) && currentDate.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH))) {
            age--
        }

        // Display the calculated age
        binding.ageTextView.text = "Age: $age"
    }

    // Update avatar based on gender
    private fun updateAvatar(gender: String) {
        when (gender) {
            "Male" -> binding.avatarImageView.setImageResource(R.drawable.male)
            "Female" -> binding.avatarImageView.setImageResource(R.drawable.female)
            "Other" -> binding.avatarImageView.setImageResource(R.drawable.other)
        }
    }
}
