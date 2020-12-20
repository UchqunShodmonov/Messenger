package uz.uchqun.telegramclone.ui.fragments.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import uz.uchqun.telegramclone.MainActivity
import uz.uchqun.telegramclone.activities.RegisterActivity
import uz.uchqun.telegramclone.databinding.FragmentEnterCodeBinding
import uz.uchqun.telegramclone.utils.*


class EnterCodeFragment(val mPhoneNumber: String, val id: String) : Fragment() {

    private lateinit var mBinding: FragmentEnterCodeBinding
    private lateinit var code: String


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
        (activity as RegisterActivity).title = mPhoneNumber
        mBinding.registerPhoneText.text = mPhoneNumber
        mBinding.registerInputCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 6) {
                    code = s.toString()
                    enterCode()
                }
            }

        })
    }

    private fun enterCode() {
        var credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                val dataMap = mutableMapOf<String, Any>()
                dataMap[CHILD_ID] = uid
                dataMap[CHILD_PHONE] = mPhoneNumber
                dataMap[CHILD_USERNAME] = uid

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dataMap)
                    .addOnCompleteListener { task2->
                        if (task2.isSuccessful){
                            showToast("Welcome")
                            (activity as RegisterActivity).replaceActivity(MainActivity())
                        }else showToast(task2.exception?.message.toString())
                    }

            } else showToast(task.exception?.message.toString())
        }
    }


}
