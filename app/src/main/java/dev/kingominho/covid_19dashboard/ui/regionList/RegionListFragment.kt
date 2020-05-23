package dev.kingominho.covid_19dashboard.ui.regionList


import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import dev.kingominho.covid_19dashboard.R
import dev.kingominho.covid_19dashboard.database.getDatabase
import dev.kingominho.covid_19dashboard.databinding.FragmentRegionListBinding
import dev.kingominho.covid_19dashboard.repository.Covid19DataRepository
import dev.kingominho.covid_19dashboard.util.hideKeyboard

/**
 * A simple [Fragment] subclass.
 */
class RegionListFragment : Fragment() {

    private val viewModel: RegionListViewModel by lazy {
        ViewModelProvider(
            this, RegionListViewModelFactory(
                Covid19DataRepository(
                    getDatabase(requireNotNull(this.activity).application)
                ),
                requireNotNull(this.activity).application
            )
        ).get(RegionListViewModel::class.java)
    }

    private val adapter =
        CountryAdapter(
            CountryClickListener { countrySlug ->
                viewModel.onCountryClicked(countrySlug)
            })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentRegionListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_region_list,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val layoutManager = LinearLayoutManager(activity)
        viewModel.countries.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitData(it)
            }
        })
        binding.countryList.layoutManager = layoutManager
        binding.countryList.adapter = adapter


        viewModel.eventCountrySelected.observe(viewLifecycleOwner, Observer { countryClicked ->
            if (countryClicked == true) {
                hideKeyboard()
                this.findNavController().navigate(
                    RegionListFragmentDirections.actionRegionListToDashboard()
                        .setSlug(viewModel.selectedCountrySlug)
                )
                viewModel.countrySelectEventComplete()
            }
        })

        binding.executePendingBindings()

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_bar_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        searchView.setOnCloseListener {
            hideKeyboard()
            true
        }

        val expandListener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                hideKeyboard()
                return true
            }
        }

        searchItem.setOnActionExpandListener(expandListener)

        super.onCreateOptionsMenu(menu, inflater)
    }

}
