package ru.profiles.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.search_toolbar_view.*
import ru.profiles.adapters.FragmentTabsAdapter
import ru.profiles.di.ViewModelFactory
import ru.profiles.profiles.R
import ru.profiles.viewmodel.MainViewModel
import javax.inject.Inject


class MainFragment : DaggerFragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: MainViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_text_view.clearFocus()
        search_text_view.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            search_button.visibility = if(b || !search_text_view.text.isEmpty()) View.INVISIBLE else View.VISIBLE
            voice_search_button.visibility = search_button.visibility
        }
        initFragments()
    }

    private fun initFragments(){
        activity?.supportFragmentManager?.let {
            val a = FragmentTabsAdapter(it)
            for (f in arrayOf(
                CalendarFragment(),
                SearchFragment(),
                ChatFragment(),
                NotificationFragment(),
                ProfileFragment()
            )) {
                a.addTab(f)
            }
            a
        }?.also{
            content_viewpager.adapter = it
            content_tablayout.setupWithViewPager(content_viewpager)

        }


    }

}
