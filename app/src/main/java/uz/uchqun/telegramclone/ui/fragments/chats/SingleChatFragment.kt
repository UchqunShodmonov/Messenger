package uz.uchqun.telegramclone.ui.fragments.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import uz.uchqun.telegramclone.databinding.FragmentSingleChatBinding
import uz.uchqun.telegramclone.model.CommanModel
import uz.uchqun.telegramclone.model.UserModel
import uz.uchqun.telegramclone.utils.*


class SingleChatFragment(private val contact: CommanModel) : Fragment() {

    private lateinit var mBinding: FragmentSingleChatBinding
    private lateinit var mListenerInfoToolbar:AppValueEventListener
    private lateinit var mReceivingUserModel: UserModel
    private lateinit var mRefUser:DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.mBinding.toolbarInfo.toolbarInfos.visibility = View.VISIBLE
        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUserModel =it.getUserModel()
            initInfoToolBar()
        }

        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
            mRefUser.addValueEventListener(mListenerInfoToolbar)

        mBinding.chatBtnSendMessage.setOnClickListener {
            val message = mBinding.chatInputMessage.text.toString()
            if (message.isEmpty()){
                showToast("write messega")
            } else sendMessage(message,contact.id, TYPE_TEXT){
                mBinding.chatInputMessage.setText("")
            }
        }
    }



    private fun initInfoToolBar() {
        if (mReceivingUserModel.fullname.isEmpty()){
            APP_ACTIVITY.mBinding.toolbarInfo.toolbarChatFullname.text =contact.fullname
        } else APP_ACTIVITY.mBinding.toolbarInfo.toolbarChatFullname.text = mReceivingUserModel.fullname
        APP_ACTIVITY.mBinding.toolbarInfo.toolbarChatImage.downloadAndSetImage(mReceivingUserModel.photoUrl)

        APP_ACTIVITY.mBinding.toolbarInfo.toolbarChatStatus.text = mReceivingUserModel.status


    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.mBinding.toolbarInfo.toolbarInfos.visibility = View.GONE
        mRefUser.removeEventListener(mListenerInfoToolbar)
    }


}