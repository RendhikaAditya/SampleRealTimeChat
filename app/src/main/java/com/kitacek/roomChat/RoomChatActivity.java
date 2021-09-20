package com.kitacek.roomChat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kitacek.R;
import com.kitacek.databinding.ActivityRoomChatBinding;

import java.util.ArrayList;

public class RoomChatActivity extends AppCompatActivity {
    private ActivityRoomChatBinding binding;
    private ImageView btn_send_msg;
    private EditText input_msg;
    private TextView chat_conversation;
    private String user_name, idAdmin,room_name, sender;
    private RecyclerView rvPesan;
    private ArrayList<Message> messages;
    private ProgressBar progressBar;
    private MessageAdapter messageAdapter;
    private DatabaseReference mDatabase;
// ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        btn_send_msg = findViewById(R.id.imgSenderMessage);
        input_msg = findViewById(R.id.edtText);
        rvPesan = findViewById(R.id.recyclerMessage);
        progressBar = findViewById(R.id.progressMessage);
        user_name = getIntent().getExtras().get("user_name").toString();
        idAdmin = getIntent().getExtras().get("admin_id").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        sender = getIntent().getExtras().get("sender").toString();
        binding.namaChat.setText(user_name);
        messages = new ArrayList<>();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RoomChat", "room :: "+room_name+":: idU :: 1 :: msg :: "+input_msg.getText().toString());
                    FirebaseDatabase
                            .getInstance()
                            .getReference("message/" + room_name)
                            .push()
                            .setValue(
                                    new Message(
                                            sender+"1",
                                            room_name,
                                            input_msg.getText().toString()
                                    )
                            );
                    input_msg.setText("");
                    Log.d("RoomChat", " :: sukses :: ");
            }
        });
        messageAdapter = new MessageAdapter(messages, sender+"1", room_name,RoomChatActivity.this);
        rvPesan.setLayoutManager(new LinearLayoutManager(this));
        rvPesan.setAdapter(messageAdapter);
        setupRoomChat();
    }


    private void setupRoomChat() {
        Log.d("RoomChat", "setup :: "+ FirebaseAuth.getInstance().getUid());
        FirebaseDatabase
                .getInstance()
                .getReference("user/" + FirebaseAuth
                        .getInstance()
                        .getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attachMessageListener(room_name);
                Log.d("RoomChat", "run");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("RoomChat", "eror :: "+error);
            }
        });
    }

    private void attachMessageListener(String chatRoomateId) {
        Log.d("RoomChat", "chatRomate :: "+chatRoomateId);
        FirebaseDatabase
                .getInstance()
                .getReference("message/" + chatRoomateId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    messages.add(dataSnapshot.getValue(Message.class));
                }
                Log.d("RoomChat", "data Chat :" + snapshot.getValue());
                Log.d("RoomChat", "model Chat :" + messages);
                messageAdapter.notifyDataSetChanged();
                rvPesan.scrollToPosition(messages.size() - 1);
                rvPesan.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
