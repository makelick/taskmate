package com.makelick.taskmate2.ui.profile

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.auth0.android.jwt.JWT
import com.makelick.taskmate2.databinding.FragmentProfileBinding
import com.makelick.taskmate2.network.TaskmateApi
import com.makelick.taskmate2.ui.MainActivity
import com.makelick.taskmate2.ui.signin.AuthConstants
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var id: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {

            val token = (activity as MainActivity).token
            restoreState(requireActivity().application)
            val user = TaskmateApi.retrofitService.getUser("Bearer_$token", id)

            val fullName = user.firstName + " " + user.lastName
            binding.fullName.text = fullName
            binding.email.text = user.email
            binding.avatar.load(user.profileImageUrl)
        }
    }
    private fun restoreState(application: Application) {
        val jsonString = application
            .getSharedPreferences(AuthConstants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .getString(AuthConstants.AUTH_STATE, null)

        id = JWT(AuthState.jsonDeserialize(jsonString!!).idToken!!).getClaim("sub").asString()!!
    }
}