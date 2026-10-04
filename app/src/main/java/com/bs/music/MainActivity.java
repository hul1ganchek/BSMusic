package com.bs.music;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView noMusicTextView;
    RecyclerView recyclerView;
    ArrayList<AudioModel> songsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.noMusicTextView = (TextView) findViewById(R.id.no_songs_text);
        if (!checkPermission()) {
            requestPermission();
            return;
        }
        String[] projection = {"title", "_data", TypedValues.TransitionType.S_DURATION};
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, "is_music != 0", null, null);
        while (cursor.moveToNext()) {
            AudioModel songData = new AudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2));
            if (new File(songData.getPath()).exists()) {
                this.songsList.add(songData);
            }
        }
        if (this.songsList.size() == 0) {
            this.noMusicTextView.setVisibility(View.INVISIBLE);
        } else {
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            this.recyclerView.setAdapter(new MusicListAdapter(this.songsList, getApplicationContext()));
        }
    }

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
        return result == 0;
    }

    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {
            Toast.makeText(this, "READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 123);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = this.recyclerView;
        if (recyclerView != null) {
            recyclerView.setAdapter(new MusicListAdapter(this.songsList, getApplicationContext()));
        }
    }
}
