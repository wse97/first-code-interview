package com.mrwaseem.firstcodeinterview.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mr.waseem.firstcode.Data.Model.PostsModel
import com.mrwaseem.firstcodeinterview.R
import com.mrwaseem.firstcodeinterview.ui.activity.AddPost.AddPostActivity
import com.mrwaseem.firstcodeinterview.ui.activity.details.DetailsActivity
import kotlinx.android.synthetic.main.item_posts.view.*

class PostsAdapter(
    private val context: Context, private var list: List<PostsModel>,
    var onItemClick: (pos: Int)
    -> Unit
) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {
    inner class ViewHolder  // Todo Butterknife bindings
        (itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun bind(model: PostsModel, pos: Int) {
            itemView.textTitle.text = model.title
            itemView.imageDelete.setOnClickListener { onItemClick.invoke(pos) }

            itemView.setOnClickListener {
                context.startActivity(
                    Intent(context, DetailsActivity::class.java).putExtra("title", model.title)
                        .putExtra("image", model.image)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        var view = inflater.inflate(R.layout.item_posts, parent, false)
        return ViewHolder(view)
    }

    fun setData(list: List<PostsModel>) {
        this.list = list
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }


}
