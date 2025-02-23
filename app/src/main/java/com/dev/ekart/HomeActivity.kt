package com.dev.ekart

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.dev.ekart.adapter.HomePagerAdapter
import com.dev.ekart.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(){
    private lateinit var adapter: HomePagerAdapter
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = HomePagerAdapter(supportFragmentManager)
        binding.vpHome.adapter=adapter
        binding.tlBottom.setupWithViewPager(binding.vpHome)
        binding.vpHome.currentItem=0
    }
}
