package ru.profiles.ui


import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.jakewharton.rxbinding3.view.clicks
import dagger.android.support.DaggerFragment
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import ru.profiles.extensions.ensureFields
import ru.profiles.extensions.shakeField
import ru.profiles.interfaces.LoginFragmentOps
import ru.profiles.profiles.R
import ru.profiles.viewmodel.LoginViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject



class LoginFragment : DaggerFragment(), LoginFragmentOps {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: LoginViewModel

    private lateinit var mCheckingFields: Array<EditText>


    override fun showAlertDialog(title: String, message: String) {
        val alertIcon = ImageView(activity).also { it.setImageResource(R.drawable.baseline_error_white) }
        Toast.makeText(activity, message, Toast.LENGTH_SHORT)
            .also { it.setGravity(Gravity.CENTER, 0, 0) }
            .also { (it.view as LinearLayout).addView(alertIcon) }
            .show()
    }

    companion object {
        fun newInstance() = LoginFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[LoginViewModel::class.java]
        (activity as AppCompatActivity).supportActionBar?.hide()
        return inflater.inflate(R.layout.login_fragment, container, false).also { it -> run{
            // todo check latest logged user & refresh auth
                it.login_button.clicks()
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe{
                    if(ensureFields(mCheckingFields, EditText::shakeField, "Заполните все поля!"))
                        viewModel.loginUser(identityText.text.toString(), passwordText.text.toString())
                }

                it.login_reg_button.setOnClickListener{
                    _ -> NavHostFragment
                        .findNavController(this)
                        .navigate(R.id.action_login_to_reg_frag_1)
                }
            }


            viewModel.getLoggedUser().observe(this, Observer{
                user->
                if(user != null) {
                    Toast.makeText(context, user.toString() + " logged successfully", Toast.LENGTH_SHORT).show()
                    NavHostFragment.findNavController(this).navigate(R.id.action_login_to_search)
                }
            })

            viewModel.getErrorStatus().observe(this, Observer {
                showAlertDialog(getString(R.string.error_input_msg),
                        getString(R.string.wrong_login_msg)
                    )
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCheckingFields = arrayOf(identityText, passwordText)
    }


}
