import android.content.Context
import android.telecom.CallScreeningService
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.newstest.MainActivity
import com.example.newstest.retrofit.*
import kotlinx.coroutines.CoroutineScope

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import com.example.newstest.retrofit.Article




import androidx.lifecycle.LiveData
import com.example.newstest.Database.RoomDatabases

import kotlinx.coroutines.Dispatchers.IO

import kotlinx.coroutines.launch
import java.net.SocketTimeoutException


class NewsRepository(val db:RoomDatabases ) {

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    fun getNewsCall(country: String, Category: String?): MutableLiveData<MutableList<Article>> {
    //You can get a New API key from NewsApi.Org
        val call = RetrofitHelper.NewsApiCall.api.getNews(
            country,
            Category,
            "3af34c4172814610afe0a3e59dafafe4"
        )
        var Newlist = MutableLiveData<MutableList<Article>>()

        call.enqueue(object : Callback<NewsDataFromJson> {
            override fun onResponse(
                call: Call<NewsDataFromJson>,
                response: Response<NewsDataFromJson>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Newlist.value = body.articles
                    }
                } else {
                    val jsonObj: JSONObject?
                        jsonObj = response.errorBody()?.string().let { JSONObject(it) }
                        if (jsonObj != null) {
                            MainActivity.apiRequestError = true
                            MainActivity.errorMessage = jsonObj.getString("message")
                            Newlist.value = mutableListOf<Article>()//
                        }
                }
            }

            override fun onFailure(call: Call<NewsDataFromJson>, t: Throwable) {
                MainActivity.apiRequestError = true
                MainActivity.errorMessage = t.localizedMessage as String
                Log.d("err_msg", "msg" + t.localizedMessage)
            }

        })
        return Newlist
    }
}


