package com.faizalfakh.consumerapp.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.faizalfakh.consumerapp.R
import com.faizalfakh.consumerapp.model.UserFavorite
import kotlinx.android.synthetic.main.user_list.view.*

class UserFavoriteAdapter : RecyclerView.Adapter<UserFavoriteAdapter.UserFavoriteViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    var dataFavorite = ArrayList<UserFavorite>()
        set(dataFavorite) {
            if (dataFavorite.size > 0) {
                this.dataFavorite.clear()
            }
            this.dataFavorite.addAll(dataFavorite)
            notifyDataSetChanged()
        }


    inner class UserFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(user: UserFavorite) {
            with(itemView){
                Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into(ivAvatar)
                tvUsername.text = user.username
                tvType.text = user.type

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFavoriteViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.user_list, parent, false)
        return UserFavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserFavoriteViewHolder, position: Int) {
        holder.bindData(dataFavorite[position])
    }

    override fun getItemCount(): Int = dataFavorite.size


    interface OnItemClickCallback {
        fun onItemClicked(data: UserFavorite)
    }

}