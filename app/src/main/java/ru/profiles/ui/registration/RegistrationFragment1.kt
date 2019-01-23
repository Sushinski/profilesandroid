package ru.profiles.ui.registration

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import kotlinx.android.synthetic.main.registration_fragment_1.*
import kotlinx.android.synthetic.main.registration_fragment_1.view.*
import ru.profiles.extensions.ensureFields
import ru.profiles.extensions.equalTextTo
import ru.profiles.extensions.isEmailValid
import ru.profiles.extensions.shakeField
import ru.profiles.profiles.R
import ru.profiles.viewmodel.RegistrationViewModel
import javax.inject.Inject


class RegistrationFragment1 : DaggerFragment() {

    private lateinit var mCheckingFields: Array<EditText>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var regViewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.registration_fragment_1, container, false)
        val a = (activity as AppCompatActivity)
        a.supportActionBar?.show()
        a.supportActionBar?.title = ""
        regViewModel = ViewModelProviders.of(this, viewModelFactory)[RegistrationViewModel::class.java]
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reg_approval_text.movementMethod = LinkMovementMethod.getInstance()
        mCheckingFields = arrayOf(email_text, password_text, repeat_password_text)
        regViewModel.getRegisteredUser().observe(this, Observer { user ->
            user?.also {
                if (!user.isBlank()) {
                    val action = RegistrationFragment1Directions.actionRegFrag1ToRegFrag2()
                    action.email = email_text.toString()
                    action.password = repeat_password_text.toString()
                    NavHostFragment
                        .findNavController(this)
                        .navigate(action)
                }
            }
        })
        reg_proceed_btn.setOnClickListener {
            if (ensureFields(mCheckingFields, EditText::shakeField, "Заполните все поля!") &&
                email_text.isEmailValid(EditText::shakeField, "Исправьте Email!") &&
                password_text.equalTextTo(repeat_password_text, EditText::shakeField, "Пароли не совпадают!")
            ) {
                regViewModel.regUser(email_text.text.toString(), repeat_password_text.text.toString())
            }
        }
    }

}