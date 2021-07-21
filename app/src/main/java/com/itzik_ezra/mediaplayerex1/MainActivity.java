package com.itzik_ezra.mediaplayerex1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageButton m_PlayBtn,m_PauseBtn,m_NextBtn,m_PrevBtn,m_AddSongBtn;

    private List<Song> m_songsList= new ArrayList<Song>();
    private SharedPreferences m_sharedPreferences;
    private boolean isPlaying=false,firstClick=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        m_sharedPreferences = getSharedPreferences("details",MODE_PRIVATE);
        setPlayBtnClick();
        setPauseBtnClick();
        setNextBtnClick();
        setPrevBtnClick();
        setAddSongBtn();
        setRecycleView();

    }

    private void setRecycleView() {
        RecyclerView songs = findViewById(R.id.songs_view);
        songs.setHasFixedSize(true);
        songs.setLayoutManager(new LinearLayoutManager(this));

        getSongList();


    }

    private void getSongList() {
        if(m_sharedPreferences.getBoolean("first",true))
        {

            m_songsList = SingletonSongsHandler.getInstance().LoadSongsList(true,this);

        }
        else{

            m_songsList = SingletonSongsHandler.getInstance().LoadSongsList(false,this);
            SharedPreferences.Editor editor = m_sharedPreferences.edit();
            editor.putBoolean("first",false).commit();
        }
    }


    private void setAddSongBtn() {
        m_AddSongBtn=findViewById(R.id.addNewSong_btn);
        m_AddSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddNewSongActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setPrevBtnClick() {
        m_PrevBtn=findViewById(R.id.prev_btn);
        m_PrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrevBtn();
            }
        });
    }

    private void playPrevBtn() {
        Intent intent = new Intent(this,MediaPlayerService.class);
        intent.putExtra("command", "prev");
        startService(intent);
    }

    private void setNextBtnClick() {
        m_NextBtn= findViewById(R.id.next_btn);
        m_NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
            }
        });
    }

    private void playNextSong() {
        Intent intent = new Intent(this,MediaPlayerService.class);
        intent.putExtra("command", "next");
        startService(intent);

    }

    private void setPauseBtnClick() {
        m_PauseBtn=findViewById(R.id.pause_btn);
        m_PauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMusic();
            }
        });
    }

    private void stopMusic() {
        Intent intent = new Intent(this,MediaPlayerService.class);
        intent.putExtra("command", "pause");
        startService(intent);


    }

    private void setPlayBtnClick() {
        m_PlayBtn=findViewById(R.id.play_btn);
        m_PlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlay();
            }
        });
    }

    private void startPlay() {
        if(SingletonSongsHandler.getInstance().getM_songsList().size()>0){
            Intent intent = new Intent(this,MediaPlayerService.class);
            if(firstClick){
                intent.putExtra("command", "new instance");
                firstClick=false;
            }
            else {
                intent.putExtra("command", "play");

            }

            startService(intent);
        }
    }
}