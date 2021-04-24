package com.example.mygithubsearch.ui.follower


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubsearch.adapter.FollowerAdapter
import com.example.mygithubsearch.ui.detail.DetailActivity
import com.example.mygithubsearch.data.Follower
import com.example.mygithubsearch.data.Github
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import com.example.mygithubsearch.databinding.FragmentFollowerBinding

class FollowerFragment : Fragment() {

    companion object{

        private  val TAG = FollowerFragment::class.java.simpleName
    }


    private lateinit var binding: FragmentFollowerBinding

    private val listGithubFollower: ArrayList<Follower> = arrayListOf()
    private lateinit var adapter : FollowerAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowerAdapter(listGithubFollower)
        val dataUser = activity?.intent?.getParcelableExtra<Github>(DetailActivity.EXTRA_DATA) as Github
        listGithubFollower.clear()
        getGithubFollower(dataUser.username.toString())
    }

    private fun getGithubFollower(id: String) {
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token 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")
        val url = "https://api.github.com/users/$id/followers"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {

                binding.progressBarFollower.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        val avatar = jsonObject.getString("avatar_url")
                        val follower = Follower()
                        follower.username = username
                        follower.avatar = avatar
                        listGithubFollower.add(follower)

                    }
                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                binding.progressBarFollower.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
    private fun showRecyclerList() {
        binding.rvGithubFollower.layoutManager = LinearLayoutManager(activity)
        binding.rvGithubFollower.adapter = adapter


    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentFollowerBinding.inflate(inflater,container,false)
        binding.rvGithubFollower.layoutManager = LinearLayoutManager(activity)
        binding.rvGithubFollower.adapter = FollowerAdapter(listGithubFollower)
        return binding.root
    }



}