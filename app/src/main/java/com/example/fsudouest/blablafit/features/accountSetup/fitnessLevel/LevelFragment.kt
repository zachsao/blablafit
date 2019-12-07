package com.example.fsudouest.blablafit.features.accountSetup.fitnessLevel


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupState
import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupViewModel
import com.example.fsudouest.blablafit.features.home.MainActivity
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_level.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor
import javax.inject.Inject

class LevelFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory<AccountSetupViewModel>

    private lateinit var viewModel: AccountSetupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_level, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this, factory)[AccountSetupViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        viewModel.stateLiveData().observe(this, Observer{ state ->
            when (state){
                is AccountSetupState.LevelUpdated -> selectLevel(state.data.level)
                is AccountSetupState.Success -> {
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    startActivity(intentFor<MainActivity>().newTask().clearTask())
                }
                is AccountSetupState.Loading -> {
                    showBlockingProgress()
                }
                is AccountSetupState.Failure -> {
                    hideBlockingProgress()
                }
            }
        })

        setLevelListeners()
        back.setOnClickListener { findNavController().navigate(R.id.action_levelFragment_to_genderSelectionFragment) }
        next.setOnClickListener { viewModel.updateUser() }
    }

    private fun hideBlockingProgress() {
        progressBar.visibility = View.GONE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun showBlockingProgress() {
        progressBar.visibility = View.VISIBLE
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun selectLevel(level: FitnessLevel) {
        when (level) {
            FitnessLevel.Beginner -> {
                select(beginner)
                unSelect(intermediate)
                unSelect(advanced)
            }
            FitnessLevel.Intermediate -> {
                unSelect(beginner)
                select(intermediate)
                unSelect(advanced)
            }
            FitnessLevel.Advanced -> {
                unSelect(beginner)
                unSelect(intermediate)
                select(advanced)
            }
        }
    }

    private fun setLevelListeners() {
        beginner.setOnClickListener{ viewModel.updateLevel(FitnessLevel.Beginner)}
        intermediate.setOnClickListener{ viewModel.updateLevel(FitnessLevel.Intermediate)}
        advanced.setOnClickListener{ viewModel.updateLevel(FitnessLevel.Advanced)}
    }

    private fun select(view: View){
        view.background = activity?.getDrawable(R.drawable.card_selected)
    }

    private fun unSelect(view: View){
        view.background = activity?.getDrawable(R.drawable.card_unselected)
    }
}
