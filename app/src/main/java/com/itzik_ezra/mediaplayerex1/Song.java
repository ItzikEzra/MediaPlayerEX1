package com.itzik_ezra.mediaplayerex1;

import java.io.Serializable;

public class Song  implements Serializable {

    String m_SongName;
    String m_SongPerformer;
    String m_PhotoPath;
    String m_SongPath;


    public void setM_SongName(String m_SongName) {
        this.m_SongName = m_SongName;
    }

    public void setM_SongPerformer(String m_SongPerformer) {
        this.m_SongPerformer = m_SongPerformer;
    }

    public void setM_PhotoPath(String m_PhotoPath) {
        this.m_PhotoPath = m_PhotoPath;
    }

    public void setM_SongPath(String m_SongPath) {
        this.m_SongPath = m_SongPath;
    }


    public String getM_SongName() {
        return m_SongName;
    }

    public String getM_SongPerformer() {
        return m_SongPerformer;
    }

    public String getM_PhotoPath() {
        return m_PhotoPath;
    }

    public String getM_SongPath() {
        return m_SongPath;
    }

    public Song(String m_SongName, String m_SongPerformer, String m_PhotoPath, String m_SongPath) {
        this.m_SongName = m_SongName;
        this.m_SongPerformer = m_SongPerformer;
        this.m_PhotoPath = m_PhotoPath;
        this.m_SongPath = m_SongPath;
    }




}
