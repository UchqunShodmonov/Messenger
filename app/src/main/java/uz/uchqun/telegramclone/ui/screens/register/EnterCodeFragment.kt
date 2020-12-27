package uz.uchqun.telegramclone.ui.screens.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import uz.uchqun.telegramclone.database.*
import uz.uchqun.telegramclone.databinding.FragmentEnterCodeBinding
import uz.uchqun.telegramclone.utils.*


class EnterCodeFragment(val phoneNumber: String, val id: String) : Fragment() {

    private lateinit var mBinding: FragmentEnterCodeBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = phoneNumber
        mBinding.registerPhoneText.text = phoneNumber
        mBinding.registerInputCode.addTextChangedListener(AppTextWatcher{
            if (it.toString().length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        val code = mBinding.registerInputCode.text.toString()
        var credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                val dataMap = mutableMapOf<String, Any>()
                dataMap[CHILD_ID] = uid
                dataMap[CHILD_PHONE] = phoneNumber

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                    .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
                        if (!dataSnapshot.hasChild(CHILD_USERNAME)) {
                            dataMap[CHILD_USERNAME] = uid
                        }
                        REF_DATABASE_ROOT.child(NODE_PHONES)
                            .child(phoneNumber).setValue(uid)
                            .addOnFailureListener { task2 -> showToast(task2.message.toString()) }
                            .addOnSuccessListener {
                                REF_DATABASE_ROOT.child(NODE_USERS)
                                    .child(uid).updateChildren(dataMap)
                                    .addOnSuccessListener {
                                        restartActivity()
                                    }
                                    .addOnFailureListener { task2 -> showToast(task2.message.toString()) }
                            }
                    })
            } else showToast(task.exception?.message.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = phoneNumber
    }


}
