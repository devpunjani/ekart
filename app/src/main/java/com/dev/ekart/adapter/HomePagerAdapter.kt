package com.dev.ekart.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dev.ekart.HomeFragment
import com.dev.ekart.ProfileFragment
import com.dev.ekart.WishlistFragment

class HomePagerAdapter(manager: FragmentManager):FragmentStatePagerAdapter(manager) {
    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        when(position){
            0-> return HomeFragment()
            1-> return WishlistFragment()
            else-> return ProfileFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0-> return "Home"
            1-> return "Wishlist"
            else-> return "Profile"
        }
        return null
    }
}