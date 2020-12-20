package uz.uchqun.telegramclone.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.databinding.ActivityRegisterBinding
import uz.uchqun.telegramclone.ui.fragments.register.EnterPhoneNumberFragment
import uz.uchqun.telegramclone.utils.initFireBase
import uz.uchqun.telegramclone.utils.replaceFragment

class RegisterActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mToolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initFireBase()
    }


    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
    }

    private fun initFunc() {
        setSupportActionBar(mToolbar)
        title = getString(R.string.register_title_your_phone)
        replaceFragment(EnterPhoneNumberFragment())
    }

    private fun initFields() {
        mToolbar = mBinding.registerToolbar
    }
}