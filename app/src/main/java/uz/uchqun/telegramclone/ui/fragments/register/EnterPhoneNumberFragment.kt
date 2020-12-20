package uz.uchqun.telegramclone.ui.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import uz.uchqun.telegramclone.MainActivity
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.activities.RegisterActivity
import uz.uchqun.telegramclone.databinding.FragmentEnterPhoneNumberBinding
import uz.uchqun.telegramclone.utils.AUTH
import uz.uchqun.telegramclone.utils.replaceActivity
import uz.uchqun.telegramclone.utils.replaceFragment
import uz.uchqun.telegramclone.utils.showToast
import java.util.concurrent.TimeUnit


class EnterPhoneNumberFragment : Fragment() {


    private lateinit var mBinding: FragmentEnterPhoneNumberBinding
    private lateinit var mPhoneNumber: String
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mresendToken:PhoneAuthProvider.ForceResendingToken



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentEnterPhoneNumberBinding.inflate(inflater, container, false)

        return mBinding.root
    }


    override fun onStart() {
        super.onStart()

        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Welcome")
                        (activity as RegisterActivity).replaceActivity(MainActivity())
                    } else showToast(task.exception?.message.toString())
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                showToast(e.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber, id))
            }
        }

        mBinding.registerBtnNext.setOnClickListener {
            sendCode()
        }
    }


    private fun sendCode() {
        mPhoneNumber = mBinding.registerInputPhoneNumber.text.toString()
        if (mPhoneNumber.isEmpty()) {
            showToast(getString(R.string.register_default_enter_code))
        } else {
            authUser()
        }
    }

    private fun authUser() {
        val options = PhoneAuthOptions.newBuilder(AUTH)
            .setPhoneNumber(mPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity as RegisterActivity)
            .setCallbacks(mCallBack)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

}