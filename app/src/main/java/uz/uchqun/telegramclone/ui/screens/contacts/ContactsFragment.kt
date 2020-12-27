package uz.uchqun.telegramclone.ui.screens.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_contacts.*
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.database.CURRENT_UID
import uz.uchqun.telegramclone.database.NODE_PHONES_CONTACTS
import uz.uchqun.telegramclone.database.NODE_USERS
import uz.uchqun.telegramclone.database.REF_DATABASE_ROOT
import uz.uchqun.telegramclone.model.CommonModel
import uz.uchqun.telegramclone.ui.screens.base.BaseFragment
import uz.uchqun.telegramclone.ui.screens.single_chats.SingleChatFragment
import uz.uchqun.telegramclone.utils.*

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var mRefContacts: DatabaseReference
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mRefUserListener: AppValueEventListener
    private var mapListener = hashMapOf<DatabaseReference, AppValueEventListener>()


    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Contacts"
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = contact_recycler_view
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        // Setting for the adapter, where we specify what data and where to get it from
        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java)
            .build()

        // The adapter accepts data, displays it in the holder
        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.contacts_item, parent, false)
                return ContactsHolder(view)
            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)
                mRefUserListener = AppValueEventListener {
                    val contact = it.getCommonModel()

                    if (contact.fullname.isEmpty()) {
                        holder.name.text = model.fullname
                    } else holder.name.text = contact.fullname

                    holder.status.text = contact.state
                    holder.photo.downloadAndSetImage(contact.photoUrl)
                    holder.itemView.setOnClickListener {
                        replaceFragment(SingleChatFragment(model))
                    }
                }

                mRefUsers.addValueEventListener(mRefUserListener)
                mapListener[mRefUsers] = mRefUserListener


            }
        }
        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }


    //Holder for ViewGroup
    class ContactsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.contact_fullname)
        val status: TextView = view.findViewById(R.id.contact_status)
        val photo: CircleImageView = view.findViewById(R.id.contact_photo)
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
        println()
        mapListener.forEach {
            it.key.removeEventListener(it.value)
        }
        println()
    }
}


