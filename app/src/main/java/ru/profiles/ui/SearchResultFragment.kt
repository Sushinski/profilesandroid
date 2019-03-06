package ru.profiles.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.main_search_tab_layout.*
import kotlinx.android.synthetic.main.search_toolbar_view.*
import ru.profiles.data.ServicesAdapter
import ru.profiles.model.ServiceModel
import ru.profiles.utils.MarginItemDecorator
import ru.profiles.viewmodel.SearchViewModel
import ru.profiles.profiles.R

class SearchResultFragment: Fragment() {

    private lateinit var mViewModel: SearchViewModel

    private val mDisposables = CompositeDisposable()

    private lateinit var mPopularAdapter: ServicesAdapter

    private val mPopularObserver =  Observer<PagedList<ServiceModel>> {
        mPopularAdapter.submitList(it)
    }

    private lateinit var mPopularAllLiveData: LiveData<PagedList<ServiceModel>>

    companion object {
        fun newInstance(viewModel: SearchViewModel): SearchResultFragment{
            val f = SearchResultFragment()
            f.mViewModel = viewModel
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mPopularAllLiveData = mViewModel.getServices(mapOf())
        return inflater.inflate(R.layout.main_search_tab_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPopularAdapter = ServicesAdapter()
        popular_recycler_view.adapter = mPopularAdapter
        popular_recycler_view.addItemDecoration(MarginItemDecorator(8))
        popular_online_recycler_view.adapter = mPopularAdapter
        popular_online_recycler_view.addItemDecoration(MarginItemDecorator(8))
        popular_goods_recycler_view.adapter = mPopularAdapter
        popular_goods_recycler_view.addItemDecoration(MarginItemDecorator(8))
        popular_articles_recycler_view.adapter = mPopularAdapter
        popular_articles_recycler_view.addItemDecoration(MarginItemDecorator(8))
        mViewModel.getActualSearch().observe(this, Observer {
            it?.searchString?.let{ s->observeNewSearch(s)}
        })
    }

    private fun observeNewSearch(searchString: String){
        mPopularAllLiveData.removeObserver(mPopularObserver)
        mPopularAllLiveData = mViewModel.getServices(
            if(searchString.isNotEmpty()) mapOf("search" to searchString) else mapOf()
        )
        //mPopularAdapter.submitList(null)
        mPopularAllLiveData.observe(this, mPopularObserver)
    }
}