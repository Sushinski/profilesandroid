package ru.profiles.ui.registration

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.registration_fragment_1.*
import ru.profiles.extensions.*
import ru.profiles.profiles.R
import ru.profiles.viewmodel.RegistrationViewModel
import javax.inject.Inject


class RegistrationFragment1 : DaggerFragment() {

    private lateinit var mCheckingFields: Array<EditText>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var regViewModel: RegistrationViewModel

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
        mCheckingFields = arrayOf(email_text, password_text, name_text, surname_text)
        regViewModel.clearRegisteredUser()
        regViewModel.getRegisteredUser().observe(this, Observer { user ->
            user?.also {
                if (!user.isBlank()) {
                    val action = RegistrationFragment1Directions.actionRegFrag1ToRegFrag12()
                    NavHostFragment
                        .findNavController(this)
                        .navigate(action)
                }
            }
        })
        regViewModel.getRegistrationError().observe(this, Observer { error ->
            error.mFields?.let {
                Toast.makeText(context, "${it[it.keys.first()]?.first()}", Toast.LENGTH_SHORT).show()
            } ?: let{ Toast.makeText(context, "Ошибка, попробуйте позже", Toast.LENGTH_SHORT).show() }
        })
        reg_proceed_btn.setOnClickListener {
            if (ensureFields(mCheckingFields, EditText::shakeField, "Заполните все поля!") &&
                radioGroup_gender.isSomeoneChecked(RadioGroup::shakeField, "Выберите пол!") &&
                approval_checkBox.hasChecked(CheckBox::shakeField, "Подтвердите согласие!") &&
                email_text.isEmailValid(EditText::shakeField, "Исправьте Email!")
            ) {
                regViewModel.regUser(email_text.text.toString(),
                    password_text.text.toString(),
                    name_text.toString(),
                    surname_text.toString(),
                    when(radioGroup_gender.checkedRadioButtonId){
                        R.id.men_radio_button->0
                        R.id.women_radio_button->1
                        else->-1
                    })
            }
        }
        show_hidden_button.setOnClickListener {
            if(password_text.transformationMethod == null) {
                password_text.transformationMethod = PasswordTransformationMethod()
                (it as ImageButton).setImageResource(R.drawable.round_visibility_black_48dp)
            }else {
                password_text.transformationMethod = null
                (it as ImageButton).setImageResource(R.drawable.round_visibility_off_black_48dp)
            }
        }
    }

}