package com.example.storeroom3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.storeroom3.databinding.ItemStoreBinding

class StoreAdapter(
    private var stores: MutableList<StoreEntity>,
    private var listener: OnClickListener): RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_store,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = stores[position]

        with(holder){
            setlistener(store)

            binding.tvName.text = store.name
            binding.chbFavorite.isChecked = store.isFavorite
        }
    }

    override fun getItemCount(): Int = stores.size

    fun setStore(stores: MutableList<StoreEntity>) {
        this.stores = stores
        notifyDataSetChanged()
    }

    fun add(storeEntity: StoreEntity) {
        stores.add(storeEntity)
        notifyDataSetChanged()
    }

    fun update(storeEntity: StoreEntity) {
        val index = stores.indexOf(storeEntity)
        if (index != -1) {
            stores[index] = storeEntity
            notifyItemChanged(index)
        }
    }

    fun delete(storeEntity: StoreEntity) {
        val index = stores.indexOf(storeEntity)
        if (index != -1){
            stores.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemStoreBinding.bind(view)

        fun setlistener (storeEntity : StoreEntity){
            with(binding.root) {
                setOnClickListener { listener.onClick(storeEntity) }
                setOnLongClickListener {
                    listener.onDeleteStore(storeEntity)
                    true
                }
            }

            binding.chbFavorite.setOnClickListener {
                listener.onFavoriteStore(storeEntity)
            }
        }
    }
}

