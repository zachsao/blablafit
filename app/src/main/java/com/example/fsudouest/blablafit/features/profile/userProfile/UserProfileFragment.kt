package com.example.fsudouest.blablafit.features.profile.userProfile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.fsudouest.blablafit.databinding.FragmentUserProfileBinding
import com.example.fsudouest.blablafit.features.conversation.ConversationActivity
import com.example.fsudouest.blablafit.features.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.startActivity

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var binding: FragmentUserProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        val args: UserProfileFragmentArgs by navArgs()
        val userId = args.userId

        viewModel.stateLiveData().observe(viewLifecycleOwner, { profileState ->
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
