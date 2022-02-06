package com.appfiza.phoneapp.ui.devices_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appfiza.phoneapp.R
import com.appfiza.phoneapp.databinding.DeviceItemBinding
import com.appfiza.phoneapp.databinding.HeaderRecyclerviewBinding
import com.appfiza.phoneapp.model.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

/**
 * Created by Fayçal KADDOURI 🐈
 */
class DeviceAdapter(
    private val clickDeviceListener: ClickDeviceListener
) :
    ListAdapter<DeviceAdapter.DataItem, RecyclerView.ViewHolder>(DeviceDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Device>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.DeviceItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> DeviceViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    class HeaderViewHolder(binding: HeaderRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = HeaderRecyclerviewBinding.inflate(inflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }

    class DeviceViewHolder constructor(private val deviceItemBinding: DeviceItemBinding) :
        RecyclerView.ViewHolder(deviceItemBinding.root) {
        companion object {
            fun from(parent: ViewGroup): DeviceViewHolder {
                val binding: DeviceItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.device_item, parent, false
                )
                return DeviceViewHolder(binding)
            }
        }

        fun bind(
            device: Device,
            clickDeviceListener: ClickDeviceListener,
        ) {
            deviceItemBinding.device = device
            // executePendingBindings() is important in order to execute the data binding immediately.
            // Otherwise it can populate incorrect view.
            deviceItemBinding.executePendingBindings()
            deviceItemBinding.cardDeviceContainer.setOnClickListener {
                clickDeviceListener.onClickDevice(device)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DeviceViewHolder -> {
                val device = getItem(position) as DataItem.DeviceItem
                holder.bind(device.device, clickDeviceListener)
            }
            is HeaderViewHolder -> {}
        }
    }

    private class DeviceDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.DeviceItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    interface ClickDeviceListener {
        fun onClickDevice(device: Device)
    }

    sealed class DataItem {
        data class DeviceItem(val device: Device) : DataItem() {
            override val id: Int = device.id
        }

        object Header : DataItem() {
            override val id: Int = Int.MAX_VALUE
        }

        abstract val id: Int
    }


}



