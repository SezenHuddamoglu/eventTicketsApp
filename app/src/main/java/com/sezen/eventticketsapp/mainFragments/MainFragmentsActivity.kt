package com.sezen.eventticketsapp.mainFragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sezen.eventticketsapp.R



class MainFragmentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fragments)

        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val userFragment = UserFragment()

        //When the user redicted to the MainFragmentActivities home fragment will be
        //the first showed fragment to the user
        setCurrentFragment(homeFragment)
        val bottomNavigationView: BottomNavigationView =findViewById(R.id.bottomNavigationView)

        //I set which fragment it redirects to when the user clicks on the icons in the bottom nav bar
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> setCurrentFragment(homeFragment)
                R.id.ic_search -> setCurrentFragment(searchFragment)
                R.id.ic_user -> setCurrentFragment(userFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.lFragment, fragment)
            commit()
        }
    }
}