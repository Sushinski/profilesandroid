package ru.profiles

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import ru.profiles.interfaces.MainActivityOps
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*
import ru.profiles.profiles.R
import ru.profiles.viewmodel.MainActivityViewModel
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), MainActivityOps {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainActivityViewModel::class.java]
    }

    override fun onSupportNavigateUp()
            = findNavController(this, R.id.my_nav_host_fragment).navigateUp()
}
