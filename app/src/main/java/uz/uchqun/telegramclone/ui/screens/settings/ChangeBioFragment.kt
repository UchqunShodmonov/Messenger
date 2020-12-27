package uz.uchqun.telegramclone.ui.screens.settings

import kotlinx.android.synthetic.main.fragment_change_bio.*
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.ui.screens.base.BaseChangeFragment
import uz.uchqun.telegramclone.utils.APP_ACTIVITY
import uz.uchqun.telegramclone.database.USER
import uz.uchqun.telegramclone.utils.setBioToDatabase

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {


    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Edit Bio"
        settings_input_bio.setText(USER.bio)


    }


    override fun change() {
        val newBio = settings_input_bio.text.toString()
        setBioToDatabase(newBio)
    }


}