package uz.uchqun.telegramclone.utils

import android.media.MediaRecorder
import java.io.File

class AppVoiceRecord {


    private val mMediaRecord = MediaRecorder()
    private lateinit var mFile: File
    private lateinit var mMessageKey: String

    fun startRecord(messageKey: String) {
        try {
            mMessageKey = messageKey
            createFileForRecord()
            prepareMediaRecorder()
            mMediaRecord.start()
        } catch (e: Exception) {
            showToast(e.message.toString())
        }

    }

    private fun prepareMediaRecorder() {

        mMediaRecord.apply {
            reset()
            setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            setOutputFile(mFile.absolutePath)
            prepare()
        }


    }

    fun stopRecord(onSuccess: (file: File, messageKey: String) -> Unit) {
        try {
            mMediaRecord.stop()
            onSuccess(mFile, mMessageKey)
        } catch (e: Exception) {
            showToast(e.message.toString())
            mFile.delete()
        }
    }

    fun releaseRecord() {
        try {
            mMediaRecord.release()
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    private fun createFileForRecord() {
        mFile = File(APP_ACTIVITY.filesDir, mMessageKey)
        mFile.createNewFile()
    }

}