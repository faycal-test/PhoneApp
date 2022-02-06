package com.appfiza.phoneapp.ui.device_details

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.appfiza.phoneapp.R
import com.appfiza.phoneapp.databinding.FragmentDeviceDetailsBinding
import com.appfiza.phoneapp.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


/**
 * Created by Fayçal KADDOURI 🐈
 */
class DeviceDetailsFragment : Fragment() {

    private lateinit var binding: FragmentDeviceDetailsBinding

    private val args: DeviceDetailsFragmentArgs by navArgs()

    private val viewModel by viewModel<DeviceDetailsViewModel> {
        parametersOf(args.deviceID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favourite_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val starItem = menu.findItem(R.id.action_favorite)
        val device = viewModel.device.value
        device?.let {
            if (it.isFavorite) {
                starItem.setIcon(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.outline_star_24
                    )
                )
            } else {
                starItem.setIcon(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.outline_star_outline_24
                    )
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_favorite -> {
                viewModel.device.value?.let { viewModel.onClickOnFavorite(it) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_device_details,
            container,
            false
        )
        binding = FragmentDeviceDetailsBinding.inflate(inflater)
        binding.viewModel = viewModel
        return binding.root
    }

    private fun initObservers() {
        viewModel.device.observe(viewLifecycleOwner) {
            (activity as AppCompatActivity).supportActionBar?.title = it.model
            requireActivity().invalidateOptionsMenu()
        }
        viewModel.messageLiveData.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        initObservers()
    }


}