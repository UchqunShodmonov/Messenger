package uz.uchqun.telegramclone.ui.screens.base

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.utils.APP_ACTIVITY
import uz.uchqun.telegramclone.utils.hideKeyboard


open class BaseChangeFragment(layout: Int) : Fragment(layout) {


    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        APP_ACTIVITY.mAppDrawer.disableDrawer()
        hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        APP_ACTIVITY.menuInflater.inflate(R.menu.settings_confirm_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> {
                change()
            }
        }

        return true
    }

    open fun change() {

    }

}