package ru.profiles

import android.os.Bundle
import ru.profiles.interfaces.MainActivityOps
import ru.profiles.ui.LoginFragment
import dagger.android.support.DaggerAppCompatActivity
import ru.profiles.profiles.R


class MainActivity : DaggerAppCompatActivity(), MainActivityOps {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        //todo forward check login to skip auth
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }
}
