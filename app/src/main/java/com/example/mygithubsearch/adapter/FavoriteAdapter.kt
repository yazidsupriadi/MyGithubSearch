package com.example.mygithubsearch.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubsearch.data.Favorite
import com.example.mygithubsearch.R
import com.example.mygithubsearch.databinding.ItemRowGithubBinding
import com.example.mygithubsearch.CustomOnItemClickListener
import com.example.mygithubsearch.ui.detail.DetailActivity


class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    var listFavorites = ArrayList<Favorite>()
        set(listFavorites) {
            if (listFavorites.size > 0) {
                this.listFavorites.clear()
            }
            this.listFavorites.addAll(listFavorites)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_github, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorites[position])
    }

    override fun getItemCount(): Int = this.listFavorites.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowGithubBinding.bind(itemView)
        fun bind(favorite: Favorite) {
            Glide.with(itemView.context)
                    .load(favorite.avatar)
                    .into(binding.imgItemPhoto)
            binding.tvItemName.text = favorite.username
            binding.tvItemLocation.text = favorite.location
            itemView.setOnClickListener(
                    CustomOnItemClickListener(adapterPosition,object : CustomOnItemClickListener.OnItemClickCallback{
                        override fun onItemClicked(view: View, position: Int) {
                            val intent = Intent(activity,DetailActivity::class.java)
                            intent.putExtra(DetailActivity.EXTRA_DATA,position)
                            intent.putExtra(DetailActivity.EXTRA_FAVORITE,favorite)
                            activity.startActivity(intent)
                        }
                    })
            )

        }
    }
}