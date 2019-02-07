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
import ru.profiles.viewmodel.NotificationViewModel
import ru.profiles.profiles.R
import javax.inject.Inject


class NotificationFragment : DaggerFragment(), AppBarSetter {

    override fun getBarTitle(ctx: Context): String? {
        return ctx.resources.getString(R.string.notification_fragment_title)
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: NotificationViewModel

    companion object {
        fun newInstance() = NotificationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(NotificationViewModel::class.java)
        return inflater.inflate(R.layout.notification_fragment, container, false)
    }

}
