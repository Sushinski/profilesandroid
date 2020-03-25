package ru.profiles.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.profiles.profiles.R
import ru.profiles.viewmodel.OrderDetailViewModel

class OrderDetailFragment : Fragment() {

    companion object {
        fun newInstance() = OrderDetailFragment()
    }

    private lateinit var viewModel: OrderDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OrderDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
