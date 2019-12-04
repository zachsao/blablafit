package com.example.fsudouest.blablafit.features.category


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentCategoryBinding
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItem
import com.example.fsudouest.blablafit.features.workoutDetails.DetailsSeanceActivity
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import org.jetbrains.anko.support.v4.startActivity
import javax.inject.Inject

class CategoryFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentCategoryBinding

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel by lazy { ViewModelProviders.of(this, factory).get(CategoryViewModel::class.java) }
    private val args by navArgs<CategoryFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        initList()
        viewModel.getWorkouts(args.title)

        return binding.root
    }

    private fun initList() {
        binding.recyclerView.adapter = GroupAdapter<GroupieViewHolder>().apply {
            setOnItemClickListener { item, _ ->
                item as WorkoutViewItem
                findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToDetailsSeanceActivity(item.seance.id))
            }
        }
    }


}
