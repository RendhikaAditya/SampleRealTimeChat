package com.kitacek

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kitacek.databinding.ActivityMainBinding
import com.kitacek.roomChat.RoomChatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.move.setOnClickListener {
            startActivity(Intent(this, RoomChatActivity::class.java)
                .putExtra("admin_id", "1")
                .putExtra("room_name", "101")
                .putExtra("user_name", "Aditya")
                .putExtra("sender", "admin-")
            )
        }
    }
}