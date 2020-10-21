package com.example.lyricsfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText edtArtistName, edtSongName;
    private Button btnGetLyrics, btnYoutube;
    private TextView txtLyrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtArtistName = findViewById(R.id.edtArtistName);
        edtSongName = findViewById(R.id.edtSongName);
        btnGetLyrics = findViewById(R.id.btnGetLyrics);
        btnYoutube = findViewById(R.id.btnYoutube);
        txtLyrics = findViewById(R.id.txtLyrics);

        btnYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String artist = edtArtistName.getText().toString();
                final String song = edtSongName.getText().toString();
                song.replace(" ", "+");

                Intent intent = new Intent(MainActivity.this, BrowseYoutubeActivity.class);
                intent.putExtra("Artist", artist);
                intent.putExtra("Song", song);
                startActivity(intent);
            }
        });

        btnGetLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String url = "https://api.lyrics.ovh/v1/" + edtArtistName.getText().toString() + "/" +
                        edtSongName.getText().toString();
                url.replace(" ","%20");

                final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("lyrics").length() != 0) {
                                txtLyrics.setText(response.getString("lyrics"));
                            } else {
                                txtLyrics.setText("Song not found");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse.statusCode==404) {
                                    txtLyrics.setText("Song not found");
                                }
                            }
                        });

                requestQueue.add(jsonObjectRequest);
            }
        });
    }
}
