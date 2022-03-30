package com.example.storeroom3

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.storeroom3.databinding.FragmentEditStoreBinding
import com.google.android.material.snackbar.Snackbar

class EditStoreFragment : Fragment() {

    private lateinit var mBinding: FragmentEditStoreBinding
    private var mActivity: MainActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        mBinding = FragmentEditStoreBinding.inflate(inflater, container, false)
        return mBinding.root
    }
//esta funcion se hace una vez que ya se realizo el fregment para desde aca poder manipular todos los elementos del fregment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    mActivity = activity as? MainActivity
    mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true) //para que aparezca el boton de regresar
    mActivity?.supportActionBar?.title = getString(R.string.edit_stor_title_add) //para que aparezca el titulo de la ActionBar

    setHasOptionsMenu(true) //para que aparezca el menu de opciones
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu) //para que aparezca el menu de opciones
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
           android.R.id.home -> {
               mActivity?.onBackPressed()
               true
           }
            R.id.action_save -> {
                Snackbar.make(
                    mBinding.root,
                    getString(R.string.edit_store_message_save_success),
                    Snackbar.LENGTH_SHORT
                ).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }   //return super.onOptionsItemSelected(item)
    }
}