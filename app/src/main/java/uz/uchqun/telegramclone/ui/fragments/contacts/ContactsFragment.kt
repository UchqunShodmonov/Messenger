package uz.uchqun.telegramclone.ui.fragments.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView
import uz.uchqun.telegramclone.R
import uz.uchqun.telegramclone.databinding.FragmentContactsBinding
import uz.uchqun.telegramclone.model.CommanModel
import uz.uchqun.telegramclone.ui.fragments.chats.SingleChatFragment
import uz.uchqun.telegramclone.utils.*

class ContactsFragment : Fragment() {

    private lateinit var mBinding: FragmentContactsBinding
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommanModel, ContactsHolder>
    private lateinit var mRefContacts:DatabaseReference
    private lateinit var mUsers:DatabaseReference
    private lateinit var mRefUserListener: AppValueEventListener
    private var mapListener = hashMapOf<DatabaseReference,AppValueEventListener>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentContactsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
        APP_ACTIVITY.title = "Contacts"
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY.mAppDrawer.enableDrawer()
    }


    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommanModel>()
            .setQuery(mRefContacts,CommanModel::class.java)
            .build()
        mAdapter = object:FirebaseRecyclerAdapter<CommanModel,ContactsHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.contacts_item,parent,false)
                return ContactsHolder(view)
            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommanModel
            ) {
                mUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)
                mRefUserListener = AppValueEventListener {
                    val contact = it.getCommonModel()

                    if(contact.fullname.isEmpty()){
                        holder.name.text = model.fullname
                    } else holder.name.text = contact.fullname

                    holder.status.text = contact.status
                    holder.photo.downloadAndSetImage(contact.photoUrl)
                    holder.itemView.setOnClickListener {
                        replaceFragment(SingleChatFragment(model))
                    }
                }

                mUsers.addValueEventListener(mRefUserListener)
                mapListener[mUsers] =mRefUserListener


            }
        }
        mBinding.contactRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }


    class ContactsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.contact_fullname)
        val status: TextView = view.findViewById(R.id.contact_status)
        val photo: CircleImageView = view.findViewById(R.id.contact_photo)
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
        mapListener.forEach{
            it.key.removeEventListener(it.value)
        }
    }
}


