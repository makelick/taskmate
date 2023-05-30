package com.makelick.taskmate2.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.makelick.taskmate2.databinding.FragmentSignInBinding
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val signInViewModel: SignInViewModel by viewModels()

    private val getAuthResponse =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val dataIntent = it.data ?: return@registerForActivityResult
            handleAuthorizationResponse(dataIntent)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInViewModel.apply {
            restoreState(requireActivity().application)
            initAuthServiceConfig()
            initAuthService(requireContext())
        }

        binding.btn.setOnClickListener {
            attemptAuthorization()
        }

        signInViewModel.jwtLiveData.observe(viewLifecycleOwner) {
            bindInfo()
            signInViewModel.persistState(requireActivity())
            navigateIfSuccessful(signInViewModel.authStateLiveData.value)
        }
    }

    private fun attemptAuthorization() {
        val authIntent = signInViewModel.attemptAuthorization()
        getAuthResponse.launch(authIntent)
    }

    private fun handleAuthorizationResponse(intent: Intent) {
        val authorizationResponse: AuthorizationResponse? = AuthorizationResponse.fromIntent(intent)
        val error = AuthorizationException.fromIntent(intent)

        signInViewModel.setAuthState(authorizationResponse, error)

//        val authCode = authorizationResponse?.authorizationCode // for request to backend
        if (authorizationResponse != null) {
            signInViewModel.exchangeAuthorizationCode(authorizationResponse)
        }

    }

    private fun bindInfo() {
        val jwt = signInViewModel.jwtLiveData.value

        val firstName = jwt?.getClaim(AuthConstants.DATA_FIRST_NAME)?.asString() ?: ""
        val lastName = jwt?.getClaim(AuthConstants.DATA_LAST_NAME)?.asString() ?: ""
        val fullName = "$firstName $lastName"

        binding.name.text = fullName
        binding.email.text = jwt?.getClaim(AuthConstants.DATA_EMAIL)?.asString()
        binding.avatar.load(jwt?.getClaim(AuthConstants.DATA_PICTURE)?.asString()) {
            transformations(CircleCropTransformation())
        }
    }

    private fun navigateIfSuccessful(authState: AuthState?) {
        if (authState != null) {
            if (authState.isAuthorized) {
                val action = SignInFragmentDirections.actionSignInFragmentToMainActivity()
                findNavController().navigate(action)
            }
        }
    }
}