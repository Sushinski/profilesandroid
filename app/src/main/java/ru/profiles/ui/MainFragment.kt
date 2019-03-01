package ru.profiles.ui

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.main_fragment.*
import ru.profiles.adapters.FragmentTabsAdapter
import ru.profiles.di.ViewModelFactory
import ru.profiles.interfaces.AppBarSetter
import ru.profiles.profiles.R
import ru.profiles.viewmodel.MainViewModel
import javax.inject.Inject


class MainFragment : DaggerFragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: MainViewModel

    private val mFragments: Array<Fragment> = arrayOf(
        FeedFragment(),
        SearchFragment(),
        ChatFragment(),
        NotificationFragment(),
        ProfileFragment()
    )

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
        activity?.let { initPager(it.applicationContext) }
    }

    private fun initPager(ctx: Context){
        activity?.supportFragmentManager?.let {
            val a = FragmentTabsAdapter(childFragmentManager)
            for (f in mFragments.withIndex()) {
                a.addTab(f.value)
            }
            a
        }?.also{
            content_viewpager.adapter = it
            content_tablayout.setupWithViewPager(content_viewpager)
            val imageResIds = intArrayOf(
                R.drawable.round_calendar_today_black_48dp,
                R.drawable.baseline_search_black_48dp,
                R.drawable.round_message_black_48dp,
                R.drawable.round_notifications_black_48dp,
                R.drawable.round_menu_black_48dp)
            for (t in 0 until content_tablayout.tabCount) content_tablayout.getTabAt(t)?.setIcon(imageResIds[t])
            //content_viewpager.setOnTouchListener { _, _ -> true }
            content_viewpager.offscreenPageLimit = 4 // all pages keeped
        }

        content_viewpager.addOnPageChangeListener(object: ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                (mFragments[position] as AppBarSetter).getBarTitle(ctx).let{
                    val a = (activity as AppCompatActivity)
                    if(it != null) {
                        a.supportActionBar?.show()
                        a.supportActionBar?.title = it
                    }else{
                        a.supportActionBar?.hide()
                    }
                }
            }
        }.also { it.onPageSelected(0) })

    }

}
