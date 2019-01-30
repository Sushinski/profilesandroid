package ru.profiles.ui

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import ru.profiles.di.ViewModelFactory
import ru.profiles.interfaces.AppBarSetter
import ru.profiles.viewmodel.CalendarViewModel
import ru.profiles.profiles.R
import javax.inject.Inject


class FeedFragment : DaggerFragment(), AppBarSetter {

    override fun getBarTitle(ctx: Context): String? {
        return ctx.resources.getString(R.string.feed_fragment_title)
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: CalendarViewModel

    companion object {
        fun newInstance() = FeedFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CalendarViewModel::class.java)
        return  inflater.inflate(R.layout.calendar_fragment, container, false)
    }

}
