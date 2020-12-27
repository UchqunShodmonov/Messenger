package uz.uchqun.telegramclone.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.ui.message_recycler_view.views.MessageView
import uz.uchqun.telegramclone.utils.AppVoicePlayer
import uz.uchqun.telegramclone.database.CURRENT_UID
import uz.uchqun.telegramclone.utils.asTime

class HolderVoiceMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {


    private val mAppVoicePlayer = AppVoicePlayer()
    private val blocReceivedVoiceMessage: ConstraintLayout =
        view.findViewById(R.id.bloc_received_voice_message)
    private val blocUserVoiceMessage: ConstraintLayout =
        view.findViewById(R.id.bloc_user_voice_message)

    private val chatReceivedVoiceTime: TextView =
        view.findViewById(R.id.chat_received_voice_message_time)
    private val chatUserVoiceTime: TextView = view.findViewById(R.id.chat_user_voice_message_time)

    private val chatReceivedBtnPlay: ImageView = view.findViewById(R.id.chat_received_btn_play)
    private val chatReceivedBtnStop: ImageView = view.findViewById(R.id.chat_received_btn_stop)
    private val chatUserBtnPlay: ImageView = view.findViewById(R.id.chat_user_btn_play)
    private val chatUserBtnStop: ImageView = view.findViewById(R.id.chat_user_btn_stop)


    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blocUserVoiceMessage.visibility = View.VISIBLE
            blocReceivedVoiceMessage.visibility = View.GONE
            chatUserVoiceTime.text = view.timeStamp.asTime()
        } else {
            blocReceivedVoiceMessage.visibility = View.VISIBLE
            blocUserVoiceMessage.visibility = View.GONE
            chatReceivedVoiceTime.text = view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {
        mAppVoicePlayer.init()
        if (view.from == CURRENT_UID) {
            chatUserBtnPlay.setOnClickListener {
                chatUserBtnPlay.visibility = View.GONE
                chatUserBtnStop.visibility = View.VISIBLE
                chatUserBtnStop.setOnClickListener {
                    stop {
                        chatUserBtnStop.setOnClickListener(null)
                        chatUserBtnPlay.visibility = View.VISIBLE
                        chatUserBtnStop.visibility = View.GONE
                    }
                }
                play(view) {
                    chatUserBtnPlay.visibility = View.VISIBLE
                    chatUserBtnStop.visibility = View.GONE
                }

            }
        } else {
            chatReceivedBtnPlay.setOnClickListener {
                chatReceivedBtnPlay.visibility = View.GONE
                chatReceivedBtnStop.visibility = View.VISIBLE
                chatReceivedBtnStop.setOnClickListener {
                    stop {
                        chatReceivedBtnStop.setOnClickListener(null)
                        chatReceivedBtnPlay.visibility = View.VISIBLE
                        chatReceivedBtnStop.visibility = View.GONE
                    }
                }
                play(view) {
                    chatReceivedBtnPlay.visibility = View.VISIBLE
                    chatReceivedBtnStop.visibility = View.GONE
                }
            }
        }
    }

    private fun play(view: MessageView, function: () -> Unit) {
        mAppVoicePlayer.play(view.id, view.fileUrl) {
            function()
        }
    }

    fun stop(function: () -> Unit) {
        mAppVoicePlayer.stop {
            function()
        }
    }

    override fun onDetached() {
        chatUserBtnPlay.setOnClickListener(null)
        chatReceivedBtnPlay.setOnClickListener(null)
        mAppVoicePlayer.release()
    }

}