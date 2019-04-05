package ru.profiles.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding3.widget.dataChanges
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.card_result_tab_fragment.*
import ru.profiles.adapters.ResultCardsAdapter
import ru.profiles.di.ViewModelFactory
import ru.profiles.model.CategoryModel
import ru.profiles.profiles.R
import ru.profiles.viewmodel.SearchViewModel
import javax.inject.Inject

class CardResultFragment : DaggerFragment()  {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: SearchViewModel

    private val mSearchLiveData = MutableLiveData<String>()

    private var mCardLiveData: LiveData<Array<CategoryModel>>? = null

    private val mCardObserver = Observer<Array<CategoryModel>> {
        gridView.adapter = it?.let { a -> ResultCardsAdapter(requireContext(), R.layout.category_card_item, a) }
        gridView.adapter.dataChanges()
    }

    companion object {
        fun newInstance() = CardResultFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(requireActivity(), mViewModelFactory).get(SearchViewModel::class.java)
        return inflater.inflate(R.layout.card_result_tab_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSearchLiveData.observe(this, Observer {
            mCardLiveData?.removeObserver(mCardObserver)
            mCardLiveData = mViewModel.searchCategories(it).also { ld->ld.observe(this, mCardObserver) }
        })
        mViewModel.searchCategories("").observe(this, mCardObserver)
    }
}