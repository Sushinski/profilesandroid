package ru.profiles.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import dagger.android.support.DaggerFragment
import ru.profiles.di.ViewModelFactory
import ru.profiles.profiles.R
import ru.profiles.viewmodel.SplashViewModel
import javax.inject.Inject

class SplashFragment : DaggerFragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: SplashViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(SplashViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        mViewModel.getLoggedUser().observe(this, Observer{
                u->
            NavHostFragment
                .findNavController(this)
                .navigate(
                    if(u == null) R.id.action_splash_to_login else R.id.action_splash_to_search
                )
        })
    }
}