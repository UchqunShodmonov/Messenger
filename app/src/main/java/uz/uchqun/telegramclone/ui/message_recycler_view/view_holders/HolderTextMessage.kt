package uz.uchqun.telegramclone.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.ui.message_recycler_view.views.MessageView
import uz.uchqun.telegramclone.database.CURRENT_UID
import uz.uchqun.telegramclone.utils.asTime

class HolderTextMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val blocUserMessage: ConstraintLayout = view.findViewById(R.id.bloc_user)
    private val chatUserMessage: TextView = view.findViewById(R.id.chat_user_message)
    private val chatUserMessageTime: TextView = view.findViewById(R.id.chat_user_message_time)

    private val blocReceivedMessage: ConstraintLayout = view.findViewById(R.id.bloc_received)
    private val chatReceivedMessage: TextView = view.findViewById(R.id.chat_received_message)
    private val chatReceivedMessageTime: TextView = view.findViewById(R.id.chat_user_received_time)


    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blocUserMessage.visibility = View.VISIBLE
            blocReceivedMessage.visibility = View.GONE

            chatUserMessage.text = view.text
            chatUserMessageTime.text = view.timeStamp.asTime()
        } else {
            blocReceivedMessage.visibility = View.VISIBLE
            blocUserMessage.visibility = View.GONE

            chatReceivedMessage.text = view.text
            chatReceivedMessageTime.text = view.timeStamp.toString().asTime()
        }
    }

    override fun onAttach(view: MessageView) {

    }

    override fun onDetached() {

    }
}