package com.example.fsudouest.blablafit.features.profile.myProfile


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentMyProfileBinding
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.profile.ProfileViewModel
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.firebase.ui.auth.AuthUI
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.IllegalStateException
import javax.inject.Inject

private const val RC_PHOTO_PICKER = 2

class MyProfileFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory<ProfileViewModel>

    private lateinit var viewModel: ProfileViewModel

    private lateinit var binding: FragmentMyProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_profile, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)
        setHasOptionsMenu(true)

        binding.pager.adapter = ProfilePagerAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.personal_info)
                1 -> getString(R.string.workout_buddies)
                else -> throw IllegalStateException()
            }
        }.attach()

        viewModel.stateLiveData().observe(viewLifecycleOwner, Observer {
            binding.user = it.data.currentUser
        })
        viewModel.getUser()

        binding.profilePicture.setOnClickListener { createPhotoUpdateDialog() }
        return binding.root
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER)
    }

    private fun createPhotoUpdateDialog() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle(getString(R.string.update_profile_dialog_title))
        builder.setMessage(getString(R.string.update_photo_dialog_message))
        builder.setPositiveButton("OK") { _, _ ->
            chooseImageFromGallery()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { _, _ -> }
        // Create the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            Glide.with(activity!!)
                    .load(selectedImageUri.toString())
                    .into(binding.profilePicture)

            Glide.with(activity!!)
                    .load(selectedImageUri.toString())
                    .into(binding.profilePicBackground)

            selectedImageUri?.let {
                viewModel.uploadProfilePictureToStorage(it)
            }
        }
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(activity!!)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sign_out,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_signout -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
