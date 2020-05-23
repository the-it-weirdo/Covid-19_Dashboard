package dev.kingominho.covid_19dashboard.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.kingominho.covid_19dashboard.R
import dev.kingominho.covid_19dashboard.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private val viewModel: AboutViewModel by lazy {
        ViewModelProvider(this).get(AboutViewModel::class.java)
    }

    private val adapter = AboutAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAboutBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_about,
            container,
            false
        )

        binding.madeBy.text = getString(R.string.made_by_format, kingoMinho)

        binding.lifecycleOwner = viewLifecycleOwner

        val layoutManager = LinearLayoutManager(activity)
        adapter.submitData(viewModel.data)
        binding.aboutList.layoutManager = layoutManager
        binding.aboutList.adapter = adapter

        return binding.root
    }
}