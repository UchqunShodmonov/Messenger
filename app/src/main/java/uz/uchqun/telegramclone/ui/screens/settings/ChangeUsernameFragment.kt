package uz.uchqun.telegramclone.ui.screens.settings

import kotlinx.android.synthetic.main.fragment_change_username.*
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.database.CURRENT_UID
import uz.uchqun.telegramclone.database.NODE_USERNAME
import uz.uchqun.telegramclone.database.REF_DATABASE_ROOT
import uz.uchqun.telegramclone.database.USER
import uz.uchqun.telegramclone.ui.screens.base.BaseChangeFragment
import uz.uchqun.telegramclone.utils.*
import java.util.*

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    lateinit var mNewUsername: String

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Edit username"
        settings_input_username.setText(USER.username)
    }

    override fun change() {
        super.change()
        mNewUsername = settings_input_username.text.toString().toLowerCase(Locale.getDefault())
        if (mNewUsername.isEmpty()) {
            showToast("Please input username")
        } else {
            REF_DATABASE_ROOT.child(
                NODE_USERNAME
            ).addListenerForSingleValueEvent(AppValueEventListener {
                if (it.hasChild(mNewUsername)) {
                    settings_change_username_choose_text.text =
                        getString(R.string.settings_default_change_username_unable)
                } else {
                    settings_change_username_choose_text.text =
                        getString(R.string.settings_default_change_username_able)
                    changeUsername()
                }
            })

        }
    }

    private fun changeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAME).child(mNewUsername).setValue(
            CURRENT_UID
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername(mNewUsername)
                }
            }
    }


}