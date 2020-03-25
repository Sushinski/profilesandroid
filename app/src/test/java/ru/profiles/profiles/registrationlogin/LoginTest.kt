package ru.profiles.profiles.registrationlogin

import com.google.gson.GsonBuilder
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import ru.profiles.api.interfaces.AuthApi
import ru.profiles.interfaces.AuthRepositoryOps
import ru.profiles.interfaces.UserRepositoryOps
import ru.profiles.viewmodel.LoginViewModel

class LoginTest {

    companion object {
        const val LOGIN =  "test"
        const val PASS = "test"
    }

    @Mock
    lateinit var mUserRepo: UserRepositoryOps

    @Mock
    lateinit var mAuthRepository: AuthRepositoryOps

    @Mock
    lateinit var mAuthApi: AuthApi


    private val mGson  = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    private lateinit var mViewModel: LoginViewModel

    @Before
    fun setupLoginTest(){
        MockitoAnnotations.initMocks(this)
        mViewModel = LoginViewModel(mUserRepo, mAuthRepository, mGson)
    }

    @Test
    fun verifyLoginSuccess(){
        mViewModel.loginUser(LOGIN, PASS)
        Assert.assertFalse(mViewModel.getLoggedUser().value?.let { false } ?: true)
    }
}