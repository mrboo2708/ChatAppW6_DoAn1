package com.example.chatappw6_doan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappw6_doan.R
import com.example.chatappw6_doan.databinding.ContactItemLayoutBinding
import com.example.chatappw6_doan.model.UserModel
import java.util.Locale

class ContactAdapter(private val context: Context, arrayList: ArrayList<UserModel>?) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>(), Filterable {
    private var arrayList: ArrayList<UserModel>? =null
    private var filterArrayList: ArrayList<UserModel>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ContactItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.contact_item_layout, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userModel: UserModel = arrayList!![position]
        holder.layoutBinding.userModel = userModel
//        holder.layoutBinding.imgContact.setOnClickListener(View.OnClickListener {
//            val intent = Intent(context, UserInfo::class.java)
//            intent.putExtra("userID", userModel!!.uID)
//            context.startActivity(intent)
//        })
//        holder.itemView.setOnClickListener {
//            val intent = Intent(context, MessageActivity::class.java)
//            intent.putExtra("hisID", userModel!!.uID)
//            intent.putExtra("hisImage", userModel.image)
//            context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun getFilter(): Filter {
        return contactFilter
    }

    private val contactFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<UserModel?> = mutableListOf()
            filterArrayList = arrayListOf()
            if (constraint.isEmpty()) filteredList.addAll(filterArrayList!!) else {
                val filter = constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (userModel in filterArrayList!!) {
                    if (userModel.name!!.lowercase(Locale.getDefault()).contains(filter)) filteredList.add(
                        userModel
                    )
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            arrayList!!.clear()
            arrayList.addAll((results.values as Collection<UserModel>))
            notifyDataSetChanged()
        }
    }

    init {
        this.arrayList = arrayList
        filterArrayList = arrayListOf()
        filterArrayList!!.addAll(arrayList!!)
    }

    inner class ViewHolder(layoutBinding: ContactItemLayoutBinding) :
        RecyclerView.ViewHolder(layoutBinding.root) {
        var layoutBinding: ContactItemLayoutBinding

        init {
            this.layoutBinding = layoutBinding
        }
    }
}