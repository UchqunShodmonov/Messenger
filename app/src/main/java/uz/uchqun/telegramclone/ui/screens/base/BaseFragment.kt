package uz.uchqun.telegramclone.ui.screens.base

import androidx.fragment.app.Fragment
import uz.uchqun.telegramclone.utils.APP_ACTIVITY


open class BaseFragment(layout: Int) : Fragment(layout) {



    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }

}