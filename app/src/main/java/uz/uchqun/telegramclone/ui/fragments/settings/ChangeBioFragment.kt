package uz.uchqun.telegramclone.ui.fragments.settings

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.databinding.FragmentChangeBioBinding
import uz.uchqun.telegramclone.utils.*

class ChangeBioFragment : Fragment() {


    private lateinit var mBinding: FragmentChangeBioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentChangeBioBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        val bio = USER.bio
        if (bio.equals("")) {

        } else {
            mBinding.settingsInputBio.setText(bio)
        }

    }


    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = "Edit Bio"
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
                changeBio()
            }
        }

        return true
    }

    private fun changeBio() {
        val bio = mBinding.settingsInputBio.text.toString()
        if (bio.isNotEmpty()) {
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_BIO)
                .setValue(bio).addOnCompleteListener {
                    if (it.isSuccessful) {
                        USER.bio = bio
                        fragmentManager?.popBackStack()
                    }
                }
        } else fragmentManager?.popBackStack()
    }


}