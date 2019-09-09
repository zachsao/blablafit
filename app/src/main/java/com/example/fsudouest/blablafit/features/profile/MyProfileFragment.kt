package com.example.fsudouest.blablafit.features.profile


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentMyProfileBinding
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.firebase.ui.auth.AuthUI
import javax.inject.Inject

class MyProfileFragment : Fragment(), Injectable {

    private lateinit var profile_pic: ImageView

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMyProfileBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_profile, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)
        profile_pic = binding.profilePicImageView


        viewModel.user().observe(this, Observer {
            binding.user = it
            if (it.photoUrl.isNotEmpty()) {
                Glide.with(activity!!)
                        .load(it.photoUrl)
                        .into(profile_pic)
            }
        })

        profile_pic.setOnClickListener { createPhotoUpdateDialog() }
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
        builder.setPositiveButton("OK") { dialog, id ->
            chooseImageFromGallery()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> }
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
                    .into(profile_pic)

            selectedImageUri?.let {
                viewModel.uploadProfilePictureToStorage(it)
            }
        }
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(activity!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sign_out,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_signout -> {
                Log.i("My Profile Fragment", " Signing out !")
                signOut()
                return true
            }
            else ->
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val RC_PHOTO_PICKER = 2
    }

}
