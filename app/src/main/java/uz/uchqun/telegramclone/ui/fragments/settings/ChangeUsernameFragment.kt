package uz.uchqun.telegramclone.ui.fragments.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.databinding.FragmentChangeUsernameBinding
import uz.uchqun.telegramclone.utils.*

class ChangeUsernameFragment : Fragment() {

    private lateinit var mBinding: FragmentChangeUsernameBinding
    private lateinit var mNewUsername: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentChangeUsernameBinding.inflate(inflater, container, false)
        mBinding.settingsInputUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                REF_DATABASE_ROOT.child(NODE_USERNAME)
                    .addListenerForSingleValueEvent(AppValueEventListener {
                        if (it.hasChild(s.toString())) {
                            mBinding.settingsChangeUsernameChooseText.text =
                                getText(R.string.settings_default_change_username_unable)

                        } else {
                            mBinding.settingsChangeUsernameChooseText.text =
                                getText(R.string.settings_default_change_username_able)
                        }

                    })
            }

            override fun afterTextChanged(s: Editable?) {
                mNewUsername = s.toString()
            }

        })
        return mBinding.root
    }


    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        mBinding.settingsInputUsername.setText(USERModel.username)
        mBinding.settingsChangeUsernameChooseText.text =
            getString(R.string.settings_default_change_username_able)

    }


    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = "Edit Username"
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
                change()
            }
        }

        return true
    }

    private fun change() {
        if (mNewUsername.isNotEmpty()) {
            mBinding.settingsChangeUsernameChooseText.text =
                getString(R.string.settings_default_change_username_choose)

            REF_DATABASE_ROOT.child(NODE_USERNAME)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(mNewUsername) || mNewUsername.equals(USERModel.username)) {
                        if (mNewUsername.equals(USERModel.username)) fragmentManager?.popBackStack()
                        else mBinding.settingsChangeUsernameChooseText.text =
                            getText(R.string.settings_default_change_username_unable)
                    } else changeUserName()
                })

        }
    }

    private fun changeUserName() {
        mBinding.settingsChangeUsernameChooseText.text =
            getText(R.string.settings_default_change_username_able)

        REF_DATABASE_ROOT.child(NODE_USERNAME).child(mNewUsername).setValue(CURRENT_UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    upDateCurrentUserName()
                }
            }
    }

    private fun upDateCurrentUserName() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USERNAME)
            .setValue(mNewUsername)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    deleteOldUserName()
                }
            }
    }

    private fun deleteOldUserName() {
        REF_DATABASE_ROOT.child(NODE_USERNAME).child(USERModel.username).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    fragmentManager?.popBackStack()
                    USERModel.username = mNewUsername
                }
            }
    }


}