package uz.uchqun.telegramclone.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.ui.message_recycler_view.views.MessageView
import uz.uchqun.telegramclone.database.CURRENT_UID
import uz.uchqun.telegramclone.utils.asTime
import uz.uchqun.telegramclone.utils.downloadAndSetImage

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val blocReceivedImageMessage: ConstraintLayout = view.findViewById(R.id.bloc_received_image)
    private val blocUserImageMessage: ConstraintLayout = view.findViewById(R.id.bloc_user_image)
    private val chatUserImage: ImageView = view.findViewById(R.id.chat_user_image)
    private val chatReceivedImage: ImageView = view.findViewById(R.id.chat_received_image)
    private val chatReceivedImageTime: TextView = view.findViewById(R.id.chat_received_image_message_time)
    private val chatUserImageTime: TextView = view.findViewById(R.id.chat_user_image_message_time)


    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blocUserImageMessage.visibility = View.VISIBLE
            blocReceivedImageMessage.visibility = View.GONE

            chatUserImage.downloadAndSetImage(view.fileUrl)
            chatUserImageTime.text = view.timeStamp.asTime()

        } else {
            blocReceivedImageMessage.visibility = View.VISIBLE
            blocUserImageMessage.visibility = View.GONE

            chatReceivedImage.downloadAndSetImage(view.fileUrl)
            chatReceivedImageTime.text = view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {

    }

    override fun onDetached() {

    }
}