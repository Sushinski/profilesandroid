package ru.profiles.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.main_search_tab_layout.*
import ru.profiles.adapters.ServicesAdapter
import ru.profiles.di.ViewModelFactory
import ru.profiles.model.ServiceModel
import ru.profiles.model.ServiceWithRelations
import ru.profiles.utils.MarginItemDecorator
import ru.profiles.viewmodel.SearchViewModel
import ru.profiles.profiles.R
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchResultFragment: DaggerFragment() {

    companion object {
        const val CARD_FRAG_TAG = "card_fragment"
        fun newInstance(): SearchResultFragment{
            return SearchResultFragment()
        }
    }

    private lateinit var mViewModel: SearchViewModel

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mPopularAdapter: ServicesAdapter

    private var mClickDisposable: Disposable? = null

    private val mPopularObserver =  Observer<PagedList<ServiceWithRelations>> {
        mPopularAdapter.submitList(it)
    }

    private var mPopularAllLiveData: LiveData<PagedList<ServiceWithRelations>>? = null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // todo move scope to parent fragment
        mViewModel = ViewModelProviders.of(requireActivity(), mViewModelFactory).get(SearchViewModel::class.java)
        return inflater.inflate(R.layout.main_search_tab_layout, container, false)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mClickDisposable = mPopularAdapter.getPositionClicks()
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                viewServiceCard(it)
            }
    }

    override fun onPause() {
        super.onPause()
        mClickDisposable?.let{ if(!it.isDisposed) it.dispose() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPopularAdapter = ServicesAdapter()

        popular_recycler_view.adapter = mPopularAdapter
        popular_recycler_view.addItemDecoration(MarginItemDecorator(8))
       /* popular_online_recycler_view.adapter = mPopularAdapter
        popular_online_recycler_view.addItemDecoration(MarginItemDecorator(8))
        popular_goods_recycler_view.adapter = mPopularAdapter
        popular_goods_recycler_view.addItemDecoration(MarginItemDecorator(8))
        popular_articles_recycler_view.adapter = mPopularAdapter
        popular_articles_recycler_view.addItemDecoration(MarginItemDecorator(8))*/
        mViewModel.getActualSearch().observe(this, Observer {
            it?.let{
                observeNewSearch()
            }
        })
    }

    private fun viewServiceCard(serviceModel: ServiceWithRelations){
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.supportFragmentManager?.beginTransaction()?.let{
            it.replace(R.id.card_container, ServiceCardFragment.newInstance(serviceModel), CARD_FRAG_TAG)
            it.addToBackStack(null)
            it.commit()
        }
    }

    private fun observeNewSearch(){
        mPopularAllLiveData?.removeObserver(mPopularObserver)
        mPopularAllLiveData = mViewModel.getServices().also { it.observe(this, mPopularObserver) }
    }
}