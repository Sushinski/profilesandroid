package ru.profiles.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.service_card_fragment.*
import kotlinx.android.synthetic.main.service_card_fragment.view.*
import ru.profiles.di.ViewModelFactory
import ru.profiles.model.ServiceWithRelations
import ru.profiles.profiles.R
import ru.profiles.viewmodel.ServiceCardViewModel
import javax.inject.Inject
import kotlinx.android.synthetic.main.collapsed_header.*



class ServiceCardFragment : DaggerFragment(), ViewTreeObserver.OnScrollChangedListener{

    // The height of your fully expanded header view (same than in the xml layout)
    var headerHeight: Int = 0
    // The height of your fully collapsed header view. Actually the Toolbar height (56dp)
    var minHeaderHeight: Int = 0
    // The left margin of the Toolbar title (according to specs, 72dp)
    var toolbarTitleLeftMargin: Int = 0
    // Added after edit
    var minHeaderTranslation: Int = 0

    private lateinit var headerView: View

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: ServiceCardViewModel

    companion object {
        fun newInstance(serviceModel: ServiceWithRelations) = ServiceCardFragment()
    }

    override fun onScrollChanged() {

        val scrollY = getScrollY()

        // This will collapse the header when scrolling, until its height reaches
        // the toolbar height
        collapsed_header.translationY = Math.max(-scrollY, minHeaderTranslation).toFloat()//0.0f//Math.max(0, scrollY + minHeaderTranslation).toFloat()

        // Scroll ratio (0 <= ratio <= 1).
        // The ratio value is 0 when the header is completely expanded,
        // 1 when it is completely collapsed
        val offset = 1 - Math.max(
            (-minHeaderTranslation - scrollY).toFloat() / -minHeaderTranslation, 0f
        )


        // Now that we have this ratio, we only have to apply translations, scales,
        // alpha, etc. to the header views

        // For instance, this will move the toolbar title & subtitle on the X axis
        // from its original position when the ListView will be completely scrolled
        // down, to the Toolbar title position when it will be scrolled up.
        header_title.translationX = toolbarTitleLeftMargin * offset
        header_subtitle.translationX = toolbarTitleLeftMargin * offset
        /*val t = headerHeight * offset
        collapsed_header.translationY = if(t > minHeaderHeight) collapsed_header.translationY else -t*/
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ServiceCardViewModel::class.java)
        val rootView = inflater.inflate(R.layout.service_card_fragment, container, false)
        headerHeight = resources.getDimensionPixelSize(R.dimen.header_height)
        minHeaderTranslation = -headerHeight +
                resources.getDimensionPixelOffset(R.dimen.action_bar_height)

        // Inflate your header view
        //headerView = inflater.inflate(R.layout.collapsed_header, rootView., false)

        // Add the headerView to your listView
        rootView.scrollView.viewTreeObserver.addOnScrollChangedListener(this)

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        headerHeight = resources.getDimensionPixelSize(R.dimen.header_height)
        minHeaderHeight = resources.getDimensionPixelSize(R.dimen.action_bar_height)
        toolbarTitleLeftMargin = resources.getDimensionPixelSize(R.dimen.toolbar_left_margin)
    }

    // Method that allows us to get the scroll Y position of the ListView
    private fun getScrollY(): Int {
        return scrollView.scrollY
    }

}