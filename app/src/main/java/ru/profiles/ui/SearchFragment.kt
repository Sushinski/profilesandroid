package ru.profiles.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import ru.profiles.di.ViewModelFactory
import ru.profiles.profiles.R
import ru.profiles.viewmodel.SearchViewModel
import javax.inject.Inject
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.transition.AutoTransition
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_toolbar_view.*
import ru.profiles.extensions.hideKeyBoard
import ru.profiles.interfaces.AppBarSetter


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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(SearchViewModel::class.java)
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_text_view.onFocusChangeListener = View.OnFocusChangeListener { v, b ->
            if(!b) this.activity?.hideKeyBoard(v)
            val t = ChangeBounds()
            t.duration = 150
            TransitionManager.beginDelayedTransition(search_fragment_layout as ViewGroup, t)
            search_button.visibility = if(b || !search_text_view.text.isEmpty()) View.INVISIBLE else View.VISIBLE
            voice_search_button.visibility = search_button.visibility
            cancel_button.visibility = if(b) View.VISIBLE else View.GONE
            search_areas_layout.visibility = if(b) View.VISIBLE else View.GONE

        }
        cancel_button.setOnClickListener {
                search_text_view.setText("")
                search_text_view.clearFocus()
        }
        mViewModel.getPopularServices(false).observe(this, Observer {
            list->
            Log.i("ProfilesInfo", "Popular services list size ${list.size}")
        })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        search_text_view?.clearFocus()
    }

}
