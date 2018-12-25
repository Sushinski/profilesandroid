package ru.profiles

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import ru.profiles.interfaces.MainActivityOps
import dagger.android.support.DaggerAppCompatActivity
import ru.profiles.profiles.R



class MainActivity : DaggerAppCompatActivity(), MainActivityOps {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return
    }
}
