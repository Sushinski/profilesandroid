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
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_search_tab_layout.*
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_toolbar_view.*
import ru.profiles.adapters.FragmentTabsAdapter
import ru.profiles.data.ServicesAdapter
import ru.profiles.extensions.hideKeyBoard
import ru.profiles.interfaces.AppBarSetter
import ru.profiles.model.ServiceModel
import ru.profiles.utils.MarginItemDecorator
import java.util.concurrent.TimeUnit


class SearchFragment : DaggerFragment(), AppBarSetter {

    override fun getBarTitle(ctx: Context): String? {
        return null
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: SearchViewModel

    private val mDisposables = CompositeDisposable()

    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(SearchViewModel::class.java)
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.applySearch( "" ) //reset search
        activity?.let { initPager(it.applicationContext) }
        search_text_view.onFocusChangeListener = View.OnFocusChangeListener { v, b ->
            if(!b) this.activity?.hideKeyBoard(v)
            val t = ChangeBounds()
            t.duration = 150
            TransitionManager.beginDelayedTransition(search_fragment_layout as ViewGroup, t)
            search_button.visibility = if(b || !search_text_view.text.isEmpty()) View.INVISIBLE else View.VISIBLE
            voice_search_button.visibility = search_button.visibility
            cancel_button.visibility = if(b) View.VISIBLE else View.GONE
            search_menu_button.visibility = if(b) View.VISIBLE else View.GONE
        }
        cancel_button.setOnClickListener {
            search_text_view.setText("")
            search_text_view.clearFocus()
        }

        mDisposables.add(
            search_text_view.textChanges()
                .skipInitialValue()
                .filter { chs->chs.length > 2 }
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinct()
                .map { chrs->chrs.toString() }
                .subscribe {
                        t->mViewModel.applySearch(t)
                }
        )

    }

    private fun initPager(ctx: Context) {
        activity?.supportFragmentManager?.let {
            val a = FragmentTabsAdapter(childFragmentManager)
            for (f in arrayOf(SearchResultFragment.newInstance(mViewModel)).withIndex()) {
                a.addTab(f.value)
            }
            a
        }?.also{
            search_content_viewpager.adapter = it
            search_content_tablayout.setupWithViewPager(search_content_viewpager)
            for (t in 0 until search_content_tablayout.tabCount)
                search_content_tablayout.getTabAt(t)?.text = "${t + 1}"
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        search_text_view?.clearFocus()
    }

}
