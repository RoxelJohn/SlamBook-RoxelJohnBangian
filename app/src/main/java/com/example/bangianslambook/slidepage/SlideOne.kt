package com.example.bangianslambook.slidepage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bangianslambook.ActivityHome
import com.example.bangianslambook.databinding.FragmentSlide1Binding



class SlideOne : Fragment() {


    private var _binding: FragmentSlide1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSlide1Binding.inflate(inflater, container, false)

        // Set click listener on the button to navigate to the ActivityHome
        binding.btnNext.setOnClickListener {
            // Create an Intent to navigate to the ActivityHome
            val intent = Intent(requireContext(), ActivityHome::class.java)
            startActivity(intent)
            requireActivity().finish()  // Optionally close the current activity (if necessary)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}