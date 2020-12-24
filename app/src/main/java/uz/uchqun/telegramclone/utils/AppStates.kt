package uz.uchqun.telegramclone.utils

enum class AppStates(val state: String) {
    ONLINE("online"),
    OFFLINE("offline"),
    TYPING("typing");

    companion object {
        fun updateState(appStates: AppStates) {
            if (AUTH.currentUser!=null) {
                REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
                    .child(CHILD_STATE).setValue(
                        appStates.state
                    )
                    .addOnSuccessListener { USERModel.status = appStates.state }
                    .addOnFailureListener {
                        showToast(it.message.toString())
                    }
            }
        }
    }
}