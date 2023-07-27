package com.example.chatgptinkotlin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatgptinkotlin.databinding.ActivityMainBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val client= OkHttpClient()
    val JSON = "application/json; charset=utf-8".toMediaType()
    var chatList : MutableList<ChatModel> = ArrayList()
    var adapter : ChatListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate( layoutInflater)
        setContentView(binding.root)
        showTypingText(false)
        binding.recyclerId.layoutManager = LinearLayoutManager(this)
        adapter = ChatListAdapter(chatList)
        binding.recyclerId.adapter = adapter
        binding.sendBtnCard.setOnClickListener {
           val queryText = binding.messageEdittext.text.trim().toString()
            if(!queryText.isNullOrBlank()){
             //   Toast.makeText(this , queryText , Toast.LENGTH_LONG).show()
                binding.chatgptIcon.visibility = View.GONE
                binding.tvWelcomeText.visibility = View.GONE
                addToChat(queryText , true)
                showTypingText(true)
                callChatGPT(queryText)
                binding.messageEdittext.text.clear()
            }
        }
    }

    private fun callChatGPT(queryText: String) {
        val jsonBody = JSONObject()
        jsonBody.put("model", "text-davinci-003")
        jsonBody.put("prompt", queryText)
        jsonBody.put("max_tokens", 4000)
        jsonBody.put("temperature", 0)
        val body: RequestBody = jsonBody.toString().toRequestBody(JSON)
        val request: Request = Request.Builder()
            .url("https://api.openai.com/v1/completions")
            .header("Authorization", "Bearer Please_fill_your_ApiKey_here")
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
             //   showTypingText(false)
                addResponse("Failed to load response due to " + e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
              //  showTypingText(false)
                if (response.isSuccessful) {
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(response.body!!.string())
                        val jsonArray = jsonObject.getJSONArray("choices")
                        val result = jsonArray.getJSONObject(0).getString("text")
                        addResponse(result.toString().trim())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body.toString())
                }
            }
        })

    }


    fun addToChat(message: String, sentBy: Boolean) {
        runOnUiThread {
            chatList.add(ChatModel(message , sentBy))
            adapter!!.notifyDataSetChanged()
            binding.recyclerId.smoothScrollToPosition(adapter!!.getItemCount())
            if (!sentBy){
                showTypingText(false)
            }
        }

    }

    fun addResponse(response: String) {
        addToChat(response,false)
    }

    fun showTypingText(flag : Boolean ){

        if(flag){
            binding.typingText.visibility = View.VISIBLE
        }else{
            binding.typingText.visibility = View.GONE
        }
    }
}