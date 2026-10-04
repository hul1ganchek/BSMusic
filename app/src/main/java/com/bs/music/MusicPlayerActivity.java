package com.bs.music;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MusicPlayerActivity extends AppCompatActivity {
    AudioModel currentSong;
    TextView currentTimeTv;
    ImageView musicIcon;
    ImageView nextBtn;
    ImageView pausePlay;
    ImageView previousBtn;
    SeekBar seekBar;
    ArrayList<AudioModel> songsList;
    TextView titleTv;
    TextView totalTimeTv;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        this.titleTv = (TextView) findViewById(R.id.song_title);
        this.currentTimeTv = (TextView) findViewById(R.id.current_time);
        this.totalTimeTv = (TextView) findViewById(R.id.total_time);
        this.seekBar = (SeekBar) findViewById(R.id.seek_bar);
        this.pausePlay = (ImageView) findViewById(R.id.pause_play);
        this.nextBtn = (ImageView) findViewById(R.id.next);
        this.previousBtn = (ImageView) findViewById(R.id.previous);
        this.musicIcon = (ImageView) findViewById(R.id.music_icon_big);
        this.titleTv.setSelected(true);
        this.songsList = (ArrayList) getIntent().getSerializableExtra("LIST");
        setResourcesWithMusic();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MusicPlayerActivity.this.mediaPlayer != null) {
                    MusicPlayerActivity.this.seekBar.setProgress(MusicPlayerActivity.this.mediaPlayer.getCurrentPosition());
                    MusicPlayerActivity.this.currentTimeTv.setText(MusicPlayerActivity.convertToMMSS(MusicPlayerActivity.this.mediaPlayer.getCurrentPosition() + ""));
                    if (MusicPlayerActivity.this.mediaPlayer.isPlaying()) {
                        MusicPlayerActivity.this.pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                        ImageView imageView = MusicPlayerActivity.this.musicIcon;
                        MusicPlayerActivity musicPlayerActivity = MusicPlayerActivity.this;
                        int i = musicPlayerActivity.x;
                        musicPlayerActivity.x = i + 1;
                        imageView.setRotation(i);
                    } else {
                        MusicPlayerActivity.this.pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        MusicPlayerActivity.this.musicIcon.setRotation(0.0f);
                    }
                }
                new Handler().postDelayed(this, 100L);
            }
        });
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.bs.music.MusicPlayerActivity.2
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (MusicPlayerActivity.this.mediaPlayer != null && fromUser) {
                    MusicPlayerActivity.this.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    void setResourcesWithMusic() {
        AudioModel audioModel = this.songsList.get(MyMediaPlayer.currentIndex);
        this.currentSong = audioModel;
        this.titleTv.setText(audioModel.getTitle());
        this.totalTimeTv.setText(convertToMMSS(this.currentSong.getDuration()));
        this.pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausePlay();
            }
        });
        this.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextSong();
            }
        });
        this.previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPreviousSong();
            }
        });
        playMusic();
    }


    private void playMusic() {
        this.mediaPlayer.reset();
        try {
            this.mediaPlayer.setDataSource(this.currentSong.getPath());
            this.mediaPlayer.prepare();
            this.mediaPlayer.start();
            this.seekBar.setProgress(0);
            this.seekBar.setMax(this.mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playNextSong() {
        if (MyMediaPlayer.currentIndex == this.songsList.size() - 1) {
            return;
        }
        MyMediaPlayer.currentIndex++;
        this.mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void playPreviousSong() {
        if (MyMediaPlayer.currentIndex == 0) {
            return;
        }
        MyMediaPlayer.currentIndex--;
        this.mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void pausePlay() {
        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.pause();
        } else {
            this.mediaPlayer.start();
        }
    }

    public static String convertToMMSS(String duration) {
        long millis = Long.parseLong(duration);
        return String.format("%02d:%02d", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1L)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1L)));
    }
}
