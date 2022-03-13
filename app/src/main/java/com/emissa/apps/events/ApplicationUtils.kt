package com.emissa.apps.events

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun fragmentNavigation(supportFragmentManager: FragmentManager, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.main_frag_container, fragment)
        .addToBackStack(fragment.id.toString())
        .commit()
}