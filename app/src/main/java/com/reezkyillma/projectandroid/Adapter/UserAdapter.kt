package com.reezkyillma.projectandroid.Adapter


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reezkyillma.projectandroid.Fragments.ProfileFragment
import com.reezkyillma.projectandroid.MainActivity
import com.reezkyillma.projectandroid.Model.User
import com.reezkyillma.projectandroid.R

import java.util.HashMap

import de.hdodenhof.circleimageview.CircleImageView

import android.content.Context.MODE_PRIVATE

class UserAdapter(private val mContext: Context, private val mUsers: List<User>, private val isFragment: Boolean) : RecyclerView.Adapter<UserAdapter.ImageViewHolder>() {

    private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ImageViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.ImageViewHolder, position: Int) {

        firebaseUser = FirebaseAuth.getInstance().currentUser

        val user = mUsers[position]

        holder.btn_follow.visibility = View.VISIBLE
        isFollowing(user.id, holder.btn_follow)

        holder.username.text = user.username
        holder.fullname.text = user.fullname
        Glide.with(mContext).load(user.imageurl).into(holder.image_profile)

        if (user.id == firebaseUser!!.uid) {
            holder.btn_follow.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (isFragment) {
                val editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit()
                editor.putString("profileid", user.id)
                editor.apply()

                (mContext as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
                        ProfileFragment()).commit()
            } else {
                val intent = Intent(mContext, MainActivity::class.java)
                intent.putExtra("publisherid", user.id)
                mContext.startActivity(intent)
            }
        }

        holder.btn_follow.setOnClickListener {
            if (holder.btn_follow.text.toString() == "follow") {
                FirebaseDatabase.getInstance().reference.child("Follow").child(firebaseUser!!.uid)
                        .child("following").child(user.id!!).setValue(true)
                FirebaseDatabase.getInstance().reference.child("Follow").child(user.id!!)
                        .child("followers").child(firebaseUser!!.uid).setValue(true)

                addNotification(user.id)
            } else {
                FirebaseDatabase.getInstance().reference.child("Follow").child(firebaseUser!!.uid)
                        .child("following").child(user.id!!).removeValue()
                FirebaseDatabase.getInstance().reference.child("Follow").child(user.id!!)
                        .child("followers").child(firebaseUser!!.uid).removeValue()
            }
        }
    }

    private fun addNotification(userid: String?) {
        val reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid!!)

        val hashMap = HashMap<String, Any>()
        hashMap["userid"] = firebaseUser!!.uid
        hashMap["text"] = "started following you"
        hashMap["postid"] = ""
        hashMap["ispost"] = false

        reference.push().setValue(hashMap)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var username: TextView
        var fullname: TextView
        var image_profile: CircleImageView
        var btn_follow: Button

        init {

            username = itemView.findViewById(R.id.username)
            fullname = itemView.findViewById(R.id.fullname)
            image_profile = itemView.findViewById(R.id.image_profile)
            btn_follow = itemView.findViewById(R.id.btn_follow)
        }
    }

    private fun isFollowing(userid: String?, button: Button) {

        val firebaseUser = FirebaseAuth.getInstance().currentUser

        val reference = FirebaseDatabase.getInstance().reference
                .child("Follow").child(firebaseUser!!.uid).child("following")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(userid!!).exists()) {
                    button.text = "following"
                } else {
                    button.text = "follow"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}