package com.letter.days.repository

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.letter.days.data.bean.NotifyBean
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.*

class NotifyRepo {

    suspend fun requestNotify(url: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        var response: Response? = null
        try {
            response = client.newCall(request).execute()
            return if (response.code == 200) {
                response.body.toString()
            } else {
                null
            }
        } finally {
            response?.close()
        }
    }

    suspend fun saveNotifyBean(context: Context, content: String) {
        var out: FileOutputStream? = null
        var writer: BufferedWriter? = null
        try {
            out = context.openFileOutput(NOTIFY_BEAN_FILE, Context.MODE_PRIVATE)
            writer = BufferedWriter(OutputStreamWriter(out))
            writer.write(content)
        } catch (e: IOException) {

        } finally {
            writer?.flush()
            out?.flush()
            writer?.close()
            out?.close()
        }
    }

    suspend fun readNotifyBean(context: Context): String {
        var input: FileInputStream? = null
        var reader: BufferedReader? = null
        val content = StringBuilder()
        try {
            input = context.openFileInput(NOTIFY_BEAN_FILE)
            reader = BufferedReader(InputStreamReader(input))
            var line: String? = reader.readLine()
            while (line != null) {
                content.append(line)
                line = reader.readLine()
            }
        } catch (e: IOException) {

        } finally {
            reader?.close()
            input?.close()
        }
        return content.toString()
    }

    suspend fun getNotifyBean(context: Context, url: String): NotifyBean? {
        var content = requestNotify(url)
        if (content != null) {
            saveNotifyBean(context, content)
        } else {
            content = readNotifyBean(context)
        }
        if (TextUtils.isEmpty(content)) {
            return null
        }
        return Gson().fromJson(content, NotifyBean::class.java)
    }

    companion object {
        private const val NOTIFY_BEAN_FILE = "notify_bean.json"
    }
}