package dev.kingominho.covid_19dashboard.ui.dashboard


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import dev.kingominho.covid_19dashboard.R
import dev.kingominho.covid_19dashboard.database.getDatabase
import dev.kingominho.covid_19dashboard.databinding.FragmentDashboardBinding
import dev.kingominho.covid_19dashboard.repository.Covid19DataRepository

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    private val viewModel: DashboardViewModel by lazy {
        ViewModelProvider(
            this, DashboardViewModelFactory(
                Covid19DataRepository(
                    getDatabase(requireNotNull(this.activity).application)
                ),
                requireNotNull(this.activity).application
            )
        ).get(DashboardViewModel::class.java)
    }

    private val args: DashboardFragmentArgs by navArgs()
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("Tag", "Dashboard: Inside onCreateView()")
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard,
            container,
            false
        )

        viewModel.eventButtonClicked.observe(viewLifecycleOwner, Observer { isClicked ->
            if (isClicked) {
                //this.findNavController().navigate()
                this.findNavController().navigate(
                    DashboardFragmentDirections.actionDashboardToRegionList()
                )
                viewModel.buttonClickEventComplete()
            }
        })


        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.swipeRefreshLayout?.setOnRefreshListener {
            loadData()
        }
        binding.executePendingBindings()

        return binding.root
    }

    private fun loadData() {
        viewModel.refreshData()
        binding.swipeRefreshLayout?.isRefreshing = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.slug != null) {
            viewModel.changeSelectedCountry(args.slug.toString())
        }
    }


}
