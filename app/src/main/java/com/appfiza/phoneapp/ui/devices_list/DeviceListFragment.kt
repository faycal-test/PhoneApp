package com.appfiza.phoneapp.ui.devices_list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.appfiza.phoneapp.R
import com.appfiza.phoneapp.databinding.FragmentDevicesListBinding
import com.appfiza.phoneapp.model.Device
import com.appfiza.phoneapp.ui.device_details.DeviceDetailsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * Created by Fayçal KADDOURI 🐈
 */
class DeviceListFragment : Fragment(), DeviceAdapter.ClickDeviceListener {

    private lateinit var binding: FragmentDevicesListBinding
    private lateinit var searchView: SearchView
    private val viewModel: DeviceListViewModel by viewModel()
    private lateinit var deviceAdapter: DeviceAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        deviceAdapter = DeviceAdapter(this)
        viewModel.loadDevices()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val mSearchMenuItem: MenuItem = menu.findItem(R.id.action_search)
        searchView = mSearchMenuItem.actionView as SearchView
        setupSearchViewListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDevicesListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.adapter = deviceAdapter
        binding.viewModel = viewModel

        return binding.root
    }

    private fun setupSearchViewListener() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                searchByModel(query)
                return true
            }
        })
    }

    private fun searchByModel(query: String) {
        viewModel.observeSearchDevices(query).observe(viewLifecycleOwner) {
            deviceAdapter.addHeaderAndSubmitList(it)
        }
    }

    private fun initObservers() {
        viewModel.observeDevices().observe(viewLifecycleOwner) {
            deviceAdapter.addHeaderAndSubmitList(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    override fun onClickDevice(device: Device) {
        val action = DeviceListFragmentDirections.actionDevicesListFragmentToDeviceDetailsFragment(deviceID = device.id)
        view?.findNavController()?.navigate(action)
    }
}