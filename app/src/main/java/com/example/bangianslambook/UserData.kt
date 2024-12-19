package com.example.bangianslambook

data class UserData(
    val name: String,
    val dob: String,
    val gender: String,
    val address: String,
    val contactNumber: String,
    val hobbies: List<String>,
    val nickname: String,
    val childhoodHero: String,
    val quoteSaying: String,
    val schoolMemory: String,
    val favoriteSong: String,
    val avatarResource: Int = R.drawable.default_avatar
)



