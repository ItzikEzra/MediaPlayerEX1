package com.itzik_ezra.mediaplayerex1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.util.List;

public class MediaPlayerService  extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener  {
    final int NOTIF_ID=1;
    private List<Song> m_songsList;
    private Integer m_currentSong=0;
    private RemoteViews m_remoteViews;
    private NotificationCompat.Builder m_builder;
    private MediaPlayer m_mediaPlayer= new MediaPlayer();

    @Override
    public void onCreate() {
        super.onCreate();
        m_mediaPlayer.setOnCompletionListener(this);
        m_mediaPlayer.setOnPreparedListener(this);
        m_mediaPlayer.reset();

        m_songsList=SingletonSongsHandler.getInstance().getM_songsList();

        startNotification();
        
        

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String command = intent.getStringExtra("command");
        switch (command)
        {
            case "play":
                playSong();
                break;
            case "pause":
                pauseSong();
                break;
            case "next":
                nextSong();
                break;
            case "prev":
                prevSong();
                break;
            case "new instance":
                playFirstSong();
                break;
            case "close":
                closeNotification();
                break;


        }
        return super.onStartCommand(intent, flags, startId);

    }

    private void closeNotification() {
        stopSelf();
    }

    private void playFirstSong() {

        m_currentSong = 0;
        updateNotifiction();
        playCurrentSong();
    }

    private void prevSong() {
        m_songsList=SingletonSongsHandler.getInstance().getM_songsList();
        if(m_currentSong==0){
            m_currentSong=m_songsList.size()-1;
        }
        else {
            m_currentSong--;
        }
        playCurrentSong();

    }

    private void nextSong() {
        m_songsList=SingletonSongsHandler.getInstance().getM_songsList();
        m_currentSong++;
        //if we arrived to the last song
        if(m_currentSong==m_songsList.size()){
            m_currentSong=0;
        }
        playCurrentSong();
    }

    private void pauseSong() {
        if(m_mediaPlayer.isPlaying())
            m_mediaPlayer.pause();
    }

    private void playSong() {
        m_currentSong=0;
        updateNotifiction();
        playCurrentSong();


    }

    private void playCurrentSong()  {
        m_mediaPlayer.reset();

        if(m_mediaPlayer.isPlaying()){
            m_mediaPlayer.stop();
            m_mediaPlayer.reset();

        }
        if(SingletonSongsHandler.getInstance().getM_songsList().size()>0)
        {
            try {
                m_mediaPlayer.setDataSource((m_songsList.get(m_currentSong).getM_SongPath()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        else {

            stopSelf();
            Toast.makeText(this,"Can't play,empty list, please add a song", Toast.LENGTH_LONG).show();
        }
    }

    private void updateNotifiction() {
        m_remoteViews.setTextViewText(R.id.notificatiion_title_song_name,m_songsList.get(m_currentSong).getM_SongName());
        m_remoteViews.setTextViewText(R.id.notificatiion_performer_song_name,m_songsList.get(m_currentSong).getM_SongPerformer());
        m_remoteViews.setImageViewUri(R.id.notif_song_photo, Uri.parse(m_songsList.get(m_currentSong).getM_PhotoPath()));

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIF_ID,m_builder.build());
    }

    private void startNotification() {
        NotificationManager manager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId =null;
        if(Build.VERSION.SDK_INT>=26) {
            channelId = "Music";
            CharSequence channelName = "Music";
            int importance;
            importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            manager.createNotificationChannel(notificationChannel);
        }


        m_builder = new NotificationCompat.Builder(this,channelId);
        m_builder.setSmallIcon(android.R.drawable.ic_media_play);

        m_remoteViews= new RemoteViews(getPackageName(),R.layout.notification_layout);

        Intent playIntent= new Intent(this,MediaPlayerService.class);
        playIntent.putExtra("command","play");
        PendingIntent PlayPendingIntent= PendingIntent.getActivity(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        m_remoteViews.setOnClickPendingIntent(R.id.play_btn,PlayPendingIntent);

        Intent prevIntent= new Intent(this,MediaPlayerService.class);
        prevIntent.putExtra("command","prev");
        PendingIntent prevPendingIntent= PendingIntent.getActivity(this,1,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        m_remoteViews.setOnClickPendingIntent(R.id.prev_btn,prevPendingIntent);

        Intent pauseIntent= new Intent(this,MediaPlayerService.class);
        pauseIntent.putExtra("command","pause");
        PendingIntent pausePendingIntent= PendingIntent.getActivity(this,2,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        m_remoteViews.setOnClickPendingIntent(R.id.pause_btn,pausePendingIntent);

        Intent nextIntent= new Intent(this,MediaPlayerService.class);
        nextIntent.putExtra("command","next");
        PendingIntent nextPendingIntent= PendingIntent.getActivity(this,3,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        m_remoteViews.setOnClickPendingIntent(R.id.next_btn,nextPendingIntent);

        m_builder.setContent(m_remoteViews);
        manager.notify(NOTIF_ID,m_builder.build());


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        m_currentSong++;
        m_currentSong = m_currentSong % m_songsList.size();
        playCurrentSong();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

    }
}
