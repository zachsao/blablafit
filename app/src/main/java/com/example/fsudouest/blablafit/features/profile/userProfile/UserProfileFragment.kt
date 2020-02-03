package com.example.fsudouest.blablafit.features.profile.userProfile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentUserProfileBinding
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.conversation.ConversationActivity
import com.example.fsudouest.blablafit.features.profile.ProfileViewModel
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import org.jetbrains.anko.startActivity
import javax.inject.Inject


class UserProfileFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory<ProfileViewModel>

    private val viewModel by lazy { ViewModelProvider(this, factory)[ProfileViewModel::class.java] }

    private lateinit var binding: FragmentUserProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        val args: UserProfileFragmentArgs by navArgs()
        val userId = args.userId

        viewModel.stateLiveData().observe(viewLifecycleOwner, Observer { profileState ->
            binding.user = profileState.data.user

            binding.contactButton.setOnClickListener {
                requireContext().startActivity<ConversationActivity>(
                        "contactName" to profileState.data.user?.nomComplet,
                        "userId" to userId
                )
            }
        })
        viewModel.getUser(userId)


        return binding.root
    }


}
