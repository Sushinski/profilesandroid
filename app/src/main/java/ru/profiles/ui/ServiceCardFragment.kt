package ru.profiles.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.service_card_fragment.view.*
import ru.profiles.di.ViewModelFactory
import ru.profiles.model.ServiceWithRelations
import ru.profiles.viewmodel.ServiceCardViewModel
import javax.inject.Inject
import android.graphics.drawable.ColorDrawable
import ru.profiles.utils.ScreenUtils
import android.R
import android.graphics.Color


class ServiceCardFragment : DaggerFragment(){


    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: ServiceCardViewModel

    companion object {
        fun newInstance(serviceModel: ServiceWithRelations) = ServiceCardFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ServiceCardViewModel::class.java)
        val rootView = inflater.inflate(ru.profiles.profiles.R.layout.service_card_fragment, container, false)
        val toolbarHeight = ScreenUtils.calculateBackdropHeight(ScreenUtils.getScreenWidth(requireContext()))/2

        // we want the title to be responsible for the background colour, not the toolbar
        val toolbarBackground = rootView.toolbar.background as ColorDrawable
        val colorInt = toolbarBackground.color
        val toolbarColours = intArrayOf(Color.red(colorInt), Color.green(colorInt), Color.blue(colorInt))
        rootView.backdrop_toolbar.background = toolbarBackground
        rootView.toolbar.background = null
        rootView.backdrop_toolbar.setTitle("Карточка")


        // ensure the title only gets as big as the required backdrop size
        val layout = rootView.backdrop_toolbar.layoutParams
        layout.height = toolbarHeight
        rootView.backdrop_toolbar.layoutParams = layout

        // ensure the scroll view starts at the required point in space
        rootView.scrollView.setPadding(rootView.scrollView.paddingLeft, toolbarHeight, rootView.scrollView.paddingRight,
            rootView.scrollView.paddingBottom)
        rootView.scrollView.clipToPadding = false // this will allow the scroll view to draw *above* its padding limit (so it fills the gap between the title bar and the start of the padding)

        rootView.scrollView.viewTreeObserver.addOnScrollChangedListener {

            // calculate the new size of the collapsing title
            val scrollY = rootView.scrollView.scrollY
            val titleHeight = rootView.backdrop_toolbar.height
            val toolbarHeight = rootView.toolbar.height
            val heightRemaining = titleHeight - scrollY
            var percent: Float = 0.0f
            percent = if (heightRemaining > toolbarHeight) {
                scrollY / (titleHeight - toolbarHeight).toFloat()
            } else {
                1.0f
            }
            // if the user flicks it can cause a negative percent, which causes the colour filter to flash black
            percent = Math.max(0.0f, percent)

            // set the size of the title bar
            rootView.backdrop_toolbar.setScrollOffset(percent)
            // tint the image on collapse cos it looks neat
            rootView.backdrop_toolbar_image.setColorFilter(Color.argb((170f * percent).toInt(), toolbarColours[0], toolbarColours[1], toolbarColours[2]))
        }
        return rootView
    }


}