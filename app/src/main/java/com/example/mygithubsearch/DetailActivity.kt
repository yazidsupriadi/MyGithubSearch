package com.example.mygithubsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra data"
        private val TAB_TITLES = intArrayOf(
                R.string.follower,
                R.string.following
        )
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        viewPager()


        val tvName: TextView = findViewById(R.id.name)
        val tvUsername: TextView = findViewById(R.id.username)
        val tvCompany: TextView = findViewById(R.id.company)
        val tvLocation: TextView = findViewById(R.id.location)
        val ivAvatar: ImageView = findViewById(R.id.img_detail_profile)
        val tvFollowing: TextView = findViewById(R.id.following_count)
        val tvFollowers: TextView = findViewById(R.id.followers_count)
        val tvRepository: TextView = findViewById(R.id.repository_count)

        val detailUser = intent.getParcelableExtra<Github>(EXTRA_DATA) as Github

        supportActionBar!!.title = detailUser.name.toString()
        tvName.text = detailUser.name.toString()
        tvUsername.text =  detailUser.username.toString()
        Glide.with(this)
                .load(detailUser.avatar.toString())
                .fitCenter()
                .into(ivAvatar)
        tvCompany.text = detailUser.company.toString()
        tvLocation.text = detailUser.location.toString()
        tvFollowers.text = detailUser.followers.toString()
        tvFollowing.text = detailUser.following.toString()
        tvRepository.text = detailUser.repository.toString()


    }


    private fun viewPager() {
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

}