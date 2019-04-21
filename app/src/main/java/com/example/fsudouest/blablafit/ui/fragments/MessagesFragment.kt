package com.example.fsudouest.blablafit.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentMessagesBinding
import com.example.fsudouest.blablafit.di.Injectable


/**
 * A Fragment that displays the list of the user's messages with other blablafit users
 */
class MessagesFragment : Fragment(), Injectable {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMessagesBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_messages,container,false)
        return binding.root
    }

}
