package uz.uchqun.telegramclone.ui.screens.groups

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_add_contacts.*
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.database.*
import uz.uchqun.telegramclone.model.CommonModel
import uz.uchqun.telegramclone.ui.screens.base.BaseFragment
import uz.uchqun.telegramclone.utils.AppValueEventListener
import uz.uchqun.telegramclone.utils.getCommonModel
import uz.uchqun.telegramclone.utils.replaceFragment
import uz.uchqun.telegramclone.utils.showToast

class AddContactsFragment : BaseFragment(R.layout.fragment_add_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private val mRefContactsList = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
    private val mRefUser = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES)
    private var mListItems = listOf<CommonModel>()


    override fun onResume() {
        listContacts.clear()
        super.onResume()
        initRecyclerView()
        add_contacts_btn_next.setOnClickListener {
            if (listContacts.isEmpty()) {
                showToast("Add Users")
            } else replaceFragment(CreateGroupFragment(listContacts))
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = add_contacts_recycler_view
        mAdapter = AddContactsAdapter()
        mRefContactsList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach { model ->


                mRefUser.child(model.id)
                    .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                        val newModel = dataSnapshot1.getCommonModel()

                        mRefMessages.child(model.id).limitToLast(1)
                            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                                val tempList = dataSnapshot2.children.map { it.getCommonModel() }

                                if (tempList.isEmpty()) {
                                    newModel.lastMessage = "Cleared chat"
                                } else newModel.lastMessage = tempList[0].text

                                if (newModel.fullname.isEmpty()) {
                                    newModel.fullname = newModel.phone
                                }
                                mAdapter.updateListItems(newModel)
                            })
                    })

            }
        })

        mRecyclerView.adapter = mAdapter
    }


    companion object {
        val listContacts = mutableListOf<CommonModel>()

    }


}