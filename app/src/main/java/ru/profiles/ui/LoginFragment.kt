package ru.profiles.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding3.view.clicks
import dagger.android.support.DaggerFragment
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import ru.profiles.interfaces.LoginFragmentOps
import ru.profiles.profiles.R
import ru.profiles.viewmodel.LoginViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject



class LoginFragment : DaggerFragment(), LoginFragmentOps {


    @Inject
    lateinit var viewModel: LoginViewModel


    override fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setNegativeButton("OK") { dialog, _->dialog.dismiss()}
            .create()
            .show()
    }

    companion object {
        fun newInstance() = LoginFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.login_fragment, container, false).also { it -> run{
            // todo check latest logged user & refresh auth
            it.login_button.clicks()
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe{
                    if(ensureFields())
                        viewModel.loginUser(identityText.text.toString(), passwordText.text.toString())
                }
            }

            viewModel.getLoggedUser().observe(this, Observer{
                user->
                if(user != null)
                    Toast.makeText(context, user.toString() + " logged successfully", Toast.LENGTH_LONG).show()
            })

            viewModel.getErrorStatus().observe(this, Observer {
                error->
                    showAlertDialog(getString(R.string.error_input_msg),
                        getString(R.string.wrong_login_msg)
                    )
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // todo if none, subscribe to auth events
        /*viewModel.mAuth.observe(this, auth->{

        })*/
    }

    private fun ensureFields() : Boolean {
        arrayOf(identityText, passwordText)
            .firstOrNull{it.text.isBlank()}
            .also { if(it != null ) shakeField(it) }
            .let{ return it == null }
    }

    private fun shakeField(view: EditText){
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shaking))
    }

}
