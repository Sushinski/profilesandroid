package ru.profiles.ui.registration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import kotlinx.android.synthetic.main.registration_fragment_1.*
import kotlinx.android.synthetic.main.registration_fragment_1.view.*
import ru.profiles.extensions.ensureFields
import ru.profiles.extensions.shakeField
import ru.profiles.profiles.R


class RegistrationFragment1 : DaggerFragment() {

    private val mCheckingFields = arrayOf(email_text, password_text, repeat_password_text)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.registration_fragment_1, container, false)
        val a = (activity as AppCompatActivity)
        a.supportActionBar?.show()
        a.supportActionBar?.title = ""
        v.reg_proceed_btn.setOnClickListener {
            if(ensureFields(mCheckingFields, this::shakeField)) { // todo check email & passwd
                // save temporary user
                val action = RegistrationFragment1Directions.actionRegFrag1ToRegFrag2()
                action.email = email_text.toString()
                action.password = repeat_password_text.toString()
                NavHostFragment
                    .findNavController(this)
                    .navigate(action)
            }
        }
        return v
    }

}