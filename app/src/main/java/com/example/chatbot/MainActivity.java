package com.example.chatbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatbot.Adapter.MessageAdapter;
import com.example.chatbot.Model.Chat;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {



    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;


    private String uuid = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        btn_send = findViewById(R.id.button_send);
        text_send = findViewById(R.id.text_send);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = text_send.getText().toString();
                if(! message.equals("")) {
                    sendMessage("user", message);
                }
                text_send.setText("");
            }
        });


    }




    private void sendMessage(String sender, String message){

        mChat = new ArrayList<>();
        Chat chat = new Chat(sender,message);
        mChat.add(chat);
        messageAdapter = new MessageAdapter( MainActivity.this,mChat);
        recyclerView.setAdapter(messageAdapter);

        sendPostRequest(sender, message);

    }

    private void sendPostRequest(String sender, String message) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://us-central1-school-chatbot-5552a.cloudfunctions.net/connectChat?text=%22"+message+"%22&session=%22"+sender +"%22";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {


                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray  = obj.getJSONArray("fulfillmentMessages");

                            JSONObject jsonObject  = jsonArray.getJSONObject(0);
                            JSONObject value1 = jsonObject.getJSONObject("text");



                            Log.d("MyActivity", value1.getString("text"));

                            mChat = new ArrayList<>();
                            Chat chat = new Chat("Bot",value1.getString("text"));
                            mChat.add(chat);
                            messageAdapter = new MessageAdapter( MainActivity.this,mChat);
                            recyclerView.setAdapter(messageAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MyActivity", "error");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


}
