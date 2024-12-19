package com.example.bangianslambook

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bangianslambook.slidepage.SlideOne
import com.example.bangianslambook.slidepage.SlideTwo
import com.example.bangianslambook.slidepage.SlideThree

class SlideAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SlideOne()
            1 -> SlideTwo()
            2 -> SlideThree()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}
