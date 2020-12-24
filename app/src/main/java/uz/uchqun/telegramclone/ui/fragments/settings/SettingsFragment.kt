package uz.uchqun.telegramclone.ui.fragments.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.activities.RegisterActivity
import uz.uchqun.telegramclone.databinding.FragmentSettingsBinding
import uz.uchqun.telegramclone.utils.*


class SettingsFragment : Fragment() {


    private lateinit var mBinding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
        APP_ACTIVITY.title = "Settings"
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY.mAppDrawer.enableDrawer()
    }


    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        if (USERModel.bio.equals("")) {
            mBinding.settingsBio.text = "Bio"
        } else mBinding.settingsBio.text = USERModel.bio
        mBinding.settingsUserFullName.text = USERModel.fullname
        mBinding.settingsPhoneNumber.text = USERModel.phone
        mBinding.settingsUserStatus.text = USERModel.status
        mBinding.settingsUsername.text = USERModel.username
        mBinding.settingsUserStatus.text = USERModel.status

        mBinding.settingsBtnChangeUsername.setOnClickListener {
            replaceFragment(ChangeUsernameFragment())
        }
        mBinding.settingsBtnChangeBio.setOnClickListener {
            replaceFragment(ChangeBioFragment())
        }
        mBinding.settingsChangePhoto.setOnClickListener {
            changePhotoUser()
        }
        mBinding.settingsUserImage.downloadAndSetImage(USERModel.photoUrl)

    }


    //Change Image
    private fun changePhotoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)

            putImageToStorage(uri, path) {
                getUrlFromStorage(path) {
                    putUrlToDatabase(it) {
                        mBinding.settingsUserImage.downloadAndSetImage(it)
                        showToast(getString(R.string.toast_data_update))
                        USERModel.photoUrl = it
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity!!.menuInflater.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AppStates.updateState(AppStates.OFFLINE)
                AUTH.signOut()
                APP_ACTIVITY.replaceActivity(RegisterActivity())
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeNamFragment())
        }

        return true
    }


}