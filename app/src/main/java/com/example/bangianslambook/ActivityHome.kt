package com.example.bangianslambook

import UserAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bangianslambook.databinding.ActivityHomeBinding
import com.google.gson.Gson

class ActivityHome : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var userAdapter: UserAdapter
    private var slamsList: MutableList<UserData> = mutableListOf()  // Changed to MutableList
    private var filteredList: MutableList<UserData> = mutableListOf()  // To store filtered results

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize View Binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve and display slams list from SharedPreferences
        loadSlamsData()

        // Initialize the adapter with the filtered list and the delete listener
        userAdapter = UserAdapter(filteredList, object : UserAdapter.OnDeleteListener {
            override fun onDelete(userToDelete: UserData) {
                deleteUser(userToDelete)
            }
        })

        // Set the adapter to the RecyclerView
        recyclerView.adapter = userAdapter

        // Set an OnClickListener for the 'addSlam' button to navigate to ActivityForm
        binding.addSlam.setOnClickListener {
            val intent = Intent(this, ActivityForm::class.java)
            startActivity(intent)
        }

        // Set up Search functionality
        val searchView = binding.btnSearch
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Not necessary for this use case
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadSlamsData()
        userAdapter.notifyDataSetChanged()
        checkIfNoSlams()
    }

    private fun loadSlamsData() {
        // Retrieve slams list from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserFormData", Context.MODE_PRIVATE)
        val slamsJson = sharedPreferences.getString("Slams", "[]")
        slamsList = Gson().fromJson(slamsJson, Array<UserData>::class.java).toMutableList()
        filteredList.clear()
        filteredList.addAll(slamsList)
    }

    private fun checkIfNoSlams() {
        if (filteredList.isEmpty()) {
            binding.noSlamTextView.visibility = android.view.View.VISIBLE
            binding.recyclerView.visibility = android.view.View.GONE
        } else {
            binding.noSlamTextView.visibility = android.view.View.GONE
            binding.recyclerView.visibility = android.view.View.VISIBLE
        }
    }

    private fun filterList(query: String?) {
        val filteredResults = mutableListOf<UserData>()

        if (!query.isNullOrEmpty()) {
            for (slam in slamsList) {
                if (slam.name.contains(query, ignoreCase = true)) {
                    filteredResults.add(slam)
                }
            }
        } else {
            filteredResults.addAll(slamsList)
        }

        filteredList.clear()
        filteredList.addAll(filteredResults)
        userAdapter.notifyDataSetChanged()

        checkIfNoSlams()
    }

    // Method to delete a user
    private fun deleteUser(userToDelete: UserData) {
        // Create an AlertDialog to confirm deletion
        val dialogBuilder = android.app.AlertDialog.Builder(this)
        dialogBuilder.setMessage("Are you sure you want to delete this slam?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                // Proceed with deletion
                slamsList.remove(userToDelete)
                filteredList.remove(userToDelete)  // Ensure it is removed from the filtered list
                userAdapter.notifyDataSetChanged()

                // Update shared preferences with the new list
                val sharedPreferences = getSharedPreferences("UserFormData", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val updatedSlamsJson = Gson().toJson(slamsList)
                editor.putString("Slams", updatedSlamsJson)
                editor.apply()

                // Reapply the search filter if there's any active query
                val currentQuery = binding.btnSearch.query.toString()
                if (currentQuery.isNotEmpty()) {
                    filterList(currentQuery)
                } else {
                    filterList(null)  // If no search query, show all
                }

                Toast.makeText(this, "Slam deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel() // Do nothing, just close the dialog
            }

        // Show the confirmation dialog
        val alert = dialogBuilder.create()
        alert.show()
    }
}


