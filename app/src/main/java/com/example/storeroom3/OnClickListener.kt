package com.example.storeroom3

interface OnClickListener {
    fun onClick (storeEntity: StoreEntity)
    fun onFavoriteStore(storeEntity: StoreEntity)
    fun onDeleteStore(storeEntity: StoreEntity)
}
