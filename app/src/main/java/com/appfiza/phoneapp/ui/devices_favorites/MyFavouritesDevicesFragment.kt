package com.appfiza.phoneapp.ui.devices_favorites

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.appfiza.phoneapp.databinding.FragmentFavouritesDevicesListBinding
import com.appfiza.phoneapp.model.Device
import com.appfiza.phoneapp.ui.devices_list.DeviceAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * Created by Fayçal KADDOURI 🐈
 */
class MyFavouritesDevicesFragment : Fragment(), DeviceAdapter.ClickDeviceListener {

    private lateinit var binding: FragmentFavouritesDevicesListBinding
    private lateinit var searchView: SearchView
    private val viewModel: MyFavouritesDevicesViewModel by viewModel()
    private lateinit var deviceAdapter: DeviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        deviceAdapter = DeviceAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesDevicesListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.adapter = deviceAdapter
        binding.viewModel = viewModel
        return binding.root
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
        val action = MyFavouritesDevicesFragmentDirections.actionMyFavouritesDevicesFragmentToDeviceDetailsFragment(deviceID = device.id)
        view?.findNavController()?.navigate(action)
    }
}