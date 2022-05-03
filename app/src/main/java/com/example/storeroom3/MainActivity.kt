package com.example.storeroom3

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.storeroom3.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), OnClickListener, MainAux {

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

//        mBinding.btnSave.setOnClickListener{
//            val store = StoreEntity(name = mBinding.etName.text.toString().trim())
//
//            Thread {StoreApplication.database.storeDao().addStore(store)}.start()
//            mAdapter.add(store)
//        }

        mBinding.fab.setOnClickListener { launchEditFragment() }

        setupRecyclerView()

    }

    private fun launchEditFragment(args: Bundle? = null) {
        val fragment = EditStoreFragment()
        if (args != null) fragment.arguments = args


        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.containerMain, fragment)
        fragmentTransaction.addToBackStack(null) //esto hace retroceder el fragmen (OnDestroid) y regresa a la activitiMain
        fragmentTransaction.commit()

        // mBinding.fab.hide() //oculta el fab para que una vez se presione se oculte
        hideFab()
    }

    //configuracion basica para lanzar un fragment en kotlin
    private fun setupRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, 2)
        getStore()

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    private fun getStore() {
        doAsync {
            val store = StoreApplication.database.storeDao().getAllStore()
            uiThread {
                mAdapter.setStore(store)
            }
        }
    }

    /*
        * OnClickListener
        * */
    //cuando se presiona un item del recyclerView se lanza el fragment de edicion de store en el containerMain
    override fun onClick(storeId: Long) {
        val args = Bundle()
        args.putLong(getString(R.string.arg_id), storeId)

        launchEditFragment(args)
    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {
        storeEntity.isFavorite = !storeEntity.isFavorite
        doAsync {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                updateStore(storeEntity)
            }
        }
    }

    override fun onDeleteStore(storeEntity: StoreEntity) {
        val item = arrayOf("Eliminar", "Llamar", "Ir al sitio web")

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_options_title)
            .setItems(item) { _, i ->
                when (i) {
                    0 -> confirmDelete(storeEntity)
                    1 -> dial(storeEntity.phone)
                    2 -> openWeb(storeEntity.website)
                }
            }
            .show()
    }


    //funsion que se ejecuta cuando se presiona el boton eliminar
    private fun confirmDelete(storeEntity: StoreEntity) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setPositiveButton(R.string.dialog_delete_confirm) { _, _ ->
                doAsync {
                    StoreApplication.database.storeDao().deleteStore(storeEntity)
                    uiThread {
                        mAdapter.delete(storeEntity)
                    }
                }

            }
            .setNegativeButton(R.string.dialog_delete_cancel) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }//end confirmDelete

    //funsion que se ejecuta cuando se presiona el boton llamar
    private fun dial(phone: String) {
        val callintent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(callintent)
    }

    private fun openWeb(website: String) {
        if (website.isEmpty()) {
            Toast.makeText(this, R.string.main_error_no_sitioweb, Toast.LENGTH_SHORT).show()
        } else {
            val websiteintent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(website)
        }
        startActivity(websiteintent)
    }
}
       /*
    * MainAux
    * */
    override fun hideFab(isVisible: Boolean) {
        if (isVisible) mBinding.fab.show() else mBinding.fab.hide()
    }

    override fun addStore(storeEntity: StoreEntity) {
        mAdapter.add(storeEntity)
    }

    override fun updateStore(storeEntity: StoreEntity) {
        mAdapter.update(storeEntity)
    }//end updateStore
}//end class
