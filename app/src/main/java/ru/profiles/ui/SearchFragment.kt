package ru.profiles.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.search_fragment.*
import ru.profiles.di.ViewModelFactory
import ru.profiles.profiles.R
import ru.profiles.viewmodel.SearchViewModel
import javax.inject.Inject

class SearchFragment : DaggerFragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: SearchViewModel

    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(SearchViewModel::class.java)
        mViewModel.getLoggedUser().observe(this, Observer{
                user-> if(user == null) NavHostFragment.findNavController(this).navigate(R.id.action_search_to_login)
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
