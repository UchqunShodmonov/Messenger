package uz.uchqun.telegramclone.ui.screens.settings

import kotlinx.android.synthetic.main.fragment_change_nam.*
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.ui.screens.base.BaseChangeFragment
import uz.uchqun.telegramclone.utils.APP_ACTIVITY
import uz.uchqun.telegramclone.database.USER
import uz.uchqun.telegramclone.utils.setNameToDatabase
import uz.uchqun.telegramclone.utils.showToast


class ChangeNamFragment : BaseChangeFragment(R.layout.fragment_change_nam) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Edit name"
        initFullName()
    }

    private fun initFullName() {
        val fullnameList: List<String> = USER.fullname.split(" ")

        if (fullnameList.size > 1) {
            settings_input_first_name.setText(fullnameList[0])
            settings_input_last_name.setText(fullnameList[1])
        } else settings_input_first_name.setText(fullnameList[0])

    }


    override fun change() {
        super.change()
        val name = settings_input_first_name.text.toString()
        val surname = settings_input_last_name.text.toString()

        if (name.isEmpty()) showToast(getString(R.string.settings_toast_name_is_empty))
        else {
            val fullName = "$name $surname"
            setNameToDatabase(fullName)
        }
    }


}