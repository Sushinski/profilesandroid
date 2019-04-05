package ru.profiles.ui

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
import android.graphics.Color
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.service_card_fragment.*
import android.view.MenuItem
import ru.profiles.profiles.BuildConfig


class ServiceCardFragment : DaggerFragment(){


    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mViewModel: ServiceCardViewModel

    private lateinit var mServiceModel: ServiceWithRelations

    companion object {
        fun newInstance(serviceModel: ServiceWithRelations) : ServiceCardFragment{
            return ServiceCardFragment().also { it.mServiceModel = serviceModel }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ServiceCardViewModel::class.java)
        val rootView = inflater.inflate(ru.profiles.profiles.R.layout.service_card_fragment, container, false)
        val toolbarHeight = ScreenUtils.calculateBackdropHeight(ScreenUtils.getScreenWidth(requireContext()))

        // we want the title to be responsible for the background colour, not the toolbar
        val toolbarBackground = rootView.toolbar.background as ColorDrawable
        val colorInt = toolbarBackground.color
        val toolbarColours = intArrayOf(Color.red(colorInt), Color.green(colorInt), Color.blue(colorInt))
        rootView.backdrop_toolbar.background = toolbarBackground
        rootView.toolbar.background = null
        rootView.toolbar.navigationIcon = resources.getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        rootView.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        rootView.backdrop_toolbar.setTitle("Услуга")


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
            var percent = if (heightRemaining > toolbarHeight) {
                scrollY / (titleHeight - toolbarHeight).toFloat()
            } else {
                1.0f
            }
            // if the user flicks it can cause a negative percent, which causes the colour filter to flash black
            percent = Math.max(0.0f, percent)

            // set the size of the title bar
            rootView.backdrop_toolbar.setScrollOffset(percent)
            if(percent < 1.0f) rootView.backdrop_toolbar_image.translationY = -scrollY.toFloat()
            // tint the image on collapse cos it looks neat
            rootView.backdrop_toolbar_image.setColorFilter(Color.argb((255f * percent).toInt(), toolbarColours[0], toolbarColours[1], toolbarColours[2]))
        }

        rootView.foldingButton.setOnClickListener {
            val t = AutoTransition()
            t.duration = 200
            TransitionManager.beginDelayedTransition(container as ViewGroup, t)
            if (rootView.textServiceDescr.maxLines == 10) {
                rootView.textServiceDescr.maxLines = 100
                rootView.foldingButton.text = "Свернуть"
            }else{
                rootView.textServiceDescr.maxLines = 10
                rootView.foldingButton.text = "Показать всё"
            }
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillFields()
    }

    private fun fillFields(){
        serviceTitle.text = mServiceModel.service?.title
        serviceCost.text = mServiceModel.service?.cost
        ratingBar.numStars = mServiceModel.service?.ratings?.common ?: 0
        numRepliesText.text = "• ${mServiceModel.service?.ratings?.common ?: 0}"
        addressText.text = mServiceModel.locationModels?.getOrNull(0)
            ?.serviceLocations?.getOrNull(0)?.let {
                "${it.city?.getOrNull(0)?.name}" +
                        "${it.location?.street?.let{ s->if(s.isNotBlank()) ", ул.$s" else "" }}" +
                        "${it.location?.block?.let{ b->if(b.isNotBlank()) ", д.$b" else "" }}" +
                        "${it.location?.office?.let{ o->if(o.isNotBlank()) ", офис $o" else ""}}"
            } ?: "Не указан"
        metroText.text = mServiceModel.locationModels?.getOrNull(0)
            ?.serviceLocations?.getOrNull(0)
            ?.metroStations?.getOrNull(0)
            ?.stations?.getOrNull(0)
            ?.name ?: "Не указана"

        textServiceDescr.text = mServiceModel.service?.description
        profileName.text = mServiceModel.profileModels?.getOrNull(0)?.profile?.displayName
        mServiceModel.photoModels?.getOrNull(0)?.photos?.getOrNull(0)?.fileName?.let{
            val uri = "${BuildConfig.MINIO_URL}/profiles/large/$it"
            if(it.isNotEmpty()) backdrop_toolbar_image.setImageURI(uri)
        }

        mServiceModel.profileModels?.getOrNull(0)?.photoModels?.getOrNull(0)?.fileName?.let {
            val uri = "${BuildConfig.MINIO_URL}/profiles/small/$it"
            if(it.isNotEmpty()) profileImage.setImageURI(uri)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->{
                requireActivity().onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}