package uz.uchqun.telegramclone.ui.message_recycler_view.view_holders

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item_file.view.*
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.database.CURRENT_UID
import uz.uchqun.telegramclone.ui.message_recycler_view.views.MessageView
import uz.uchqun.telegramclone.utils.*
import java.io.File

class HolderFileMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {


    private val blocReceivedFileMessage: ConstraintLayout =
        view.findViewById(R.id.bloc_received_file_message)
    private val blocUserFileMessage: ConstraintLayout =
        view.findViewById(R.id.bloc_user_file_message)
    private val chatReceivedFileTime: TextView =
        view.findViewById(R.id.chat_received_file_message_time)
    private val chatUserFileTime: TextView = view.findViewById(R.id.chat_user_file_message_time)

    private val chatUserFileName: TextView = view.chat_user_filename
    private val chatReceivedFileName: TextView = view.chat_received_filename
    private val chatUserBtnDownload: ImageView = view.chat_user_btn_download
    private val chatReceivedBtnDownload: ImageView = view.chat_received_btn_download
    private val chatUserProgressBar: ProgressBar = view.chat_user_progress_bar
    private val chatReceivedProgressBar: ProgressBar = view.chat_received_progress_bar


    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blocUserFileMessage.visibility = View.VISIBLE
            blocReceivedFileMessage.visibility = View.GONE
            chatUserFileTime.text = view.timeStamp.asTime()
            chatUserFileName.text = view.text
        } else {
            blocReceivedFileMessage.visibility = View.VISIBLE
            blocUserFileMessage.visibility = View.GONE
            chatReceivedFileTime.text = view.timeStamp.asTime()
            chatReceivedFileName.text = view.text
        }
    }

    override fun onAttach(view: MessageView) {
        if (view.from == CURRENT_UID) chatUserBtnDownload.setOnClickListener { clickToBtnFile(view) }
        else chatReceivedBtnDownload.setOnClickListener { clickToBtnFile(view) }
    }

    private fun clickToBtnFile(view: MessageView) {
        if (view.from == CURRENT_UID) {
            chatUserBtnDownload.visibility = View.INVISIBLE
            chatUserProgressBar.visibility = View.VISIBLE
        } else {
            chatReceivedBtnDownload.visibility = View.INVISIBLE
            chatReceivedProgressBar.visibility = View.VISIBLE
        }

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            view.text
        )

        try {
            if (checkPermision(WRITE_FILES)) {
                file.createNewFile()
                getFileFromStorage(file, view.fileUrl) {
                    if (view.from == CURRENT_UID) {
                        chatUserBtnDownload.visibility = View.VISIBLE
                        chatUserProgressBar.visibility = View.INVISIBLE
                    } else {
                        chatReceivedBtnDownload.visibility = View.VISIBLE
                        chatReceivedProgressBar.visibility = View.INVISIBLE
                    }
                }
            }
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }


    override fun onDetached() {
        chatUserBtnDownload.setOnClickListener(null)
        chatReceivedBtnDownload.setOnClickListener(null)
    }

}