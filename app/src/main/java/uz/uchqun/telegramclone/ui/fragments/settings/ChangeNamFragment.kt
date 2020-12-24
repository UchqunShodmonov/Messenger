package uz.uchqun.telegramclone.ui.fragments.settings

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.databinding.FragmentChangeNamBinding
import uz.uchqun.telegramclone.utils.*


class ChangeNamFragment : Fragment() {


    private lateinit var mBinding: FragmentChangeNamBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentChangeNamBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        val fullnameList: List<String> = USERModel.fullname.split(" ")
        setHasOptionsMenu(true)
        if (fullnameList.size > 1) {
            mBinding.settingsInputFirstName.setText(fullnameList[0])
            mBinding.settingsInputLastName.setText(fullnameList[1])
        } else {
            mBinding.settingsInputFirstName.setText(fullnameList[0])
        }

    }


    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = "Edit name"
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity!!.menuInflater.inflate(R.menu.settings_confirm_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> {
                changeName()
            }
        }

        return true
    }

    private fun changeName() {
        val first_name = mBinding.settingsInputFirstName.text.toString()
        val last_name = mBinding.settingsInputLastName.text.toString()
        if (first_name.isEmpty()) {
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullName = "$first_name $last_name"
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULL_NAME)
                .setValue(fullName).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast(getString(R.string.toast_data_update))
                        USERModel.fullname = fullName
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                        fragmentManager?.popBackStack()
                    }
                }

        }
    }


}