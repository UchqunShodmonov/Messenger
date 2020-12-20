package uz.uchqun.telegramclone.ui.fragments.chats

import androidx.fragment.app.Fragment
import uz.uchqun.telegramclone.MainActivity
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.utils.APP_ACTIVITY

class ChatsFragment : Fragment(R.layout.fragment_chats) {
    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = "Telegram"
    }

}