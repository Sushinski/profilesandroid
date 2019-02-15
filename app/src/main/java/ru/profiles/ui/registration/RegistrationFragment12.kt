package ru.profiles.ui.registration


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.registration_fragment12.*
import ru.profiles.profiles.R
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import ru.profiles.extensions.disableBackButton


class RegistrationFragment12 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.registration_fragment12, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =  RegistrationFragment12()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val a = (activity as AppCompatActivity)
        a.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        a.supportActionBar?.show()
        this.disableBackButton()
        proceed_to_email_button.setOnClickListener {
            val intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_EMAIL)
            activity?.let {
                a -> a.packageManager
            }?.let {
                pm->intent.resolveActivity(pm)
            }.also{
                ra->if(ra != null)
                    activity?.startActivity(intent)
                else
                    Toast.makeText(context, "Почтовое приложение не найдено!", Toast.LENGTH_SHORT).show()
            }
        }

        reg_proceed_btn.setOnClickListener {
            val action = RegistrationFragment12Directions.actionRegFrag12ToRegFrag2()
            NavHostFragment
                .findNavController(this)
                .navigate(action)
        }
    }


}
