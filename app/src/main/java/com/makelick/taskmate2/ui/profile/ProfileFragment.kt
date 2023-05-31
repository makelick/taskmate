package com.makelick.taskmate2.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.makelick.taskmate2.databinding.FragmentProfileBinding
import com.makelick.taskmate2.model.User
import com.makelick.taskmate2.ui.SharedViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

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

        sharedViewModel.getUser()
        sharedViewModel.user.observe(viewLifecycleOwner) { user ->
            bindView(user)
        }

        binding.logoutButton.setOnClickListener {
            sharedViewModel.logout(requireContext())
            navigateToLogin()
        }
    }

    private fun bindView(user: User) {
        val fullName = user.firstName + " " + user.lastName
        binding.fullName.text = fullName
        binding.email.text = user.email
        binding.avatar.load(user.profileImageUrl)
    }

    private fun navigateToLogin() {
        val action = ProfileFragmentDirections.actionProfileFragmentToSignInFragment2()
        findNavController().navigate(action)
    }
}