package ru.profiles.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import ru.profiles.di.ViewModelFactory
import ru.profiles.profiles.R
import ru.profiles.viewmodel.SearchViewModel
import javax.inject.Inject
import android.view.LayoutInflater
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_toolbar_view.*
import ru.profiles.adapters.FragmentTabsAdapter
import ru.profiles.adapters.SearchListAdapter
import ru.profiles.extensions.hideKeyBoard
import ru.profiles.interfaces.AppBarSetter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class SearchFragment : DaggerFragment(), AppBarSetter {

    override fun getBarTitle(ctx: Context): String? {
        return null
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: SearchViewModel


    companion object {
        fun newInstance() = SearchFragment()
    }

    private val mTabs = mapOf(
        "Топ" to SearchResultFragment.newInstance(),
        "Услуги" to CardResultFragment.newInstance()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(requireActivity(), mViewModelFactory).get(SearchViewModel::class.java)
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { initPager() }
        search_text_view.setAdapter(SearchListAdapter(this.requireContext(), mViewModel ))
        search_text_view.onFocusChangeListener = View.OnFocusChangeListener { v, b ->
            if(!b) this.activity?.hideKeyBoard(v)
            val t = ChangeBounds()
            t.duration = 150
            TransitionManager.beginDelayedTransition(search_fragment_layout as ViewGroup, t)
            applySearchBarState(b)
        }
        cancel_button.setOnClickListener {
            search_text_view.setText("")
            search_text_view.clearFocus()
            mViewModel.applySearch("%")
        }

        search_text_view.setOnItemClickListener{
            a,v,i,l->mViewModel.applySearch(a.getItemAtPosition(i).toString())
        }
    }

    override fun onStart() {
        super.onStart()
        val s = search_text_view.text.toString()
        if(!s.isEmpty()) applySearchBarState(true)
    }

    private fun applySearchBarState(activated: Boolean){
        search_button.visibility = if(!activated && search_text_view.text.toString().isEmpty()) View.VISIBLE else View.INVISIBLE
        voice_search_button.visibility = search_button.visibility
        cancel_button.visibility = if(activated) View.VISIBLE else View.GONE
        search_menu_button.visibility = if(activated) View.VISIBLE else View.GONE
    }

    private fun initPager() {
        activity?.supportFragmentManager?.let {
            FragmentTabsAdapter(childFragmentManager, mTabs)
        }?.also{
            search_content_viewpager.adapter = it
            search_content_tablayout.setupWithViewPager(search_content_viewpager)
            //for (t in 0 until content_tablayout.tabCount) search_content_tablayout.getTabAt(t)?.text = mTabs
        }


    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        search_text_view?.clearFocus()
    }

    inner class ExamplePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        // tab titles
        private val tabTitles = arrayOf("Tab1", "Tab2", "Tab3")

        // overriding getPageTitle()
        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitles[position]
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> SearchFragment.newInstance()
                else -> CardResultFragment.newInstance()
                // shouldn't happen
            }
        }

        override fun getCount(): Int {
            return tabTitles.size
        }
    }

}
