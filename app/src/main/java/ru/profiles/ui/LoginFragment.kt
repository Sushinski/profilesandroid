package ru.profiles.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import ru.profiles.interfaces.LoginFragmentOps
import ru.profiles.profiles.R
import ru.profiles.viewmodel.LoginViewModel
import javax.inject.Inject

class LoginFragment : DaggerFragment(), LoginFragmentOps {

    companion object {
        fun newInstance() = LoginFragment()
    }

    @Inject
    lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.login_fragment, container, false).also { it -> run{
            // todo check latest logged user & refresh auth
                it.login_button.setOnClickListener { _ ->
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
                    Toast.makeText(context, error.mUserMessage ?: "Login Error", Toast.LENGTH_LONG).show()
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // todo if none, subscribe to auth events
        /*viewModel.mAuth.observe(this, auth->{

        })*/
    }

}
