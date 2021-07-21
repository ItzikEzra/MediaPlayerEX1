package com.itzik_ezra.mediaplayerex1;

import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SingletonSongsHandler {

    private static SingletonSongsHandler m_singleInstance;

    private List<Song> m_songsList;

    public void setM_songsList(List<Song> m_songsList) {
        this.m_songsList = m_songsList;
    }

    public List<Song> getM_songsList() {
        return m_songsList;
    }

    private SingletonSongsHandler() {
        this.m_songsList = new ArrayList<>();
    }

    public static SingletonSongsHandler getInstance()
    {
        if (m_singleInstance == null)
        {
            m_singleInstance = new SingletonSongsHandler();
        }
        return m_singleInstance;

    }
    public void addSongToList(Song song){
        m_songsList.add(song);
    }

    public  void removeSongFormList(Song song){
        m_songsList.remove(song);
    }

    public List<Song> LoadSongsList(boolean firstLoad,AppCompatActivity appCompatActivity)
    {
        if(firstLoad)
        {//uri להכניס תמונות
            Uri uri;


            uri = Uri.parse("src/main/res/drawable-v24/sara_song_photo.jpg");
            m_songsList.add(new Song("Sara","Bob Dylan",uri.toString(),"http://www.syntax.org.il/xtra/bob1.m4a"));
            m_songsList.add(new Song("Sara","Bob Dylan",uri.toString(),"http://www.syntax.org.il/xtra/bob1.m4a"));
            m_songsList.add(new Song("Sara","Bob Dylan",uri.toString(),"http://www.syntax.org.il/xtra/bob1.m4a"));
            m_songsList.add(new Song("Sara","Bob Dylan",uri.toString(),"http://www.syntax.org.il/xtra/bob1.m4a"));


            return m_songsList;
        }
        try
        {
            FileInputStream fileInputStream = appCompatActivity.openFileInput("songs list");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            m_songsList=(ArrayList<Song>)objectInputStream.readObject();


        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return m_songsList;

    }
    public void SaveSongsList(AppCompatActivity appCompatActivity) throws IOException {
        FileOutputStream fileOutputStream=appCompatActivity.openFileOutput("songs list",appCompatActivity.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(m_songsList);
        objectOutputStream.close();
    }
}
