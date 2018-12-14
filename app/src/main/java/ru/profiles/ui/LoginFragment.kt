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
        return inflater.inflate(R.layout.login_fragment, container, false).also { it ->
            it.login_button.setOnClickListener { _ ->
                viewModel.loginUser(identityText.text.toString(), passwordText.text.toString()).subscribe { auth ->
                    if (auth.mRefreshToken.isNotEmpty() && auth.mToken.isNotEmpty()) {
                        Toast.makeText(context, "Auth successful", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // todo if none, subscribe to auth events
        /*viewModel.mAuth.observe(this, auth->{

        })*/
    }

}
