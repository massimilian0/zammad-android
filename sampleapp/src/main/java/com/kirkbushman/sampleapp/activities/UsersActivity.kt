package com.kirkbushman.sampleapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kirkbushman.sampleapp.R
import com.kirkbushman.sampleapp.SampleApplication
import com.kirkbushman.sampleapp.controllers.OnClickCallback
import com.kirkbushman.sampleapp.controllers.UsersController
import com.kirkbushman.sampleapp.doAsync
import com.kirkbushman.zammad.models.User
import kotlinx.android.synthetic.main.activity_users.*

class UsersActivity : AppCompatActivity() {

    private val client by lazy { SampleApplication.instance.getClient() }

    private val users = ArrayList<User>()
    private val controller by lazy {
        UsersController(object : OnClickCallback {

            override fun onClick(position: Int) {

                val user = users[position]
                UserActivity.start(this@UsersActivity, user)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        list.setHasFixedSize(true)
        list.setController(controller)

        doAsync(doWork = {

            users.addAll(client?.users() ?: listOf())
        }, onPost = {

            controller.setUsers(users)
        })
    }
}