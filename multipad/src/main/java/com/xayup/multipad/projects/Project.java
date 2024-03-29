package com.xayup.multipad.projects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Project implements ProjectIndexes {
    public static final byte STATUS_UNLOADED = 0;
    public static final byte STATUS_LOADED = 1;
    public static final byte STATUS_LOADING = 2;
    public static final byte STATUS_BROKEN = 3;

    //Identifier
    protected int project_id;
    protected byte status;

    public String title, producerName, difficulty;
    public File path, info_path, sample_path, autoplay_path, keysound_path;
    public int keyled_count, sample_count = 0;
    public byte state = 0;
    public List<File> keyleds_paths;

    public Project(){
        this.keyleds_paths = new ArrayList<>();
        this.status = STATUS_UNLOADED;
    }

    protected void setTitle(String title){ this.title = title; }
    protected void setProducerName(String producerName){ this.producerName = producerName; }
    protected void setDifficulty(String difficulty){ this.difficulty = difficulty; }
    protected void setPath(String path){ this.path = (path == null) ? null : new File(path); }
    protected void setInfoPath(String info_path){ this.info_path = (info_path == null) ? null : new File(info_path); }
    protected void setSamplePath(String sample_path){ this.sample_path = (sample_path == null) ? null : new File(sample_path); }
    protected void setAutoplayPath(String autoplay_path){ this.autoplay_path = (autoplay_path == null) ? null : new File(autoplay_path); }
    protected void setKeySoundPath(String keysound_path){ this.keysound_path = (keysound_path == null) ? null : new File(keysound_path); }
    protected void addKeyLedPath(File keyled_path){ this.keyleds_paths.add(keyled_path); }
    protected void setKeyLedCount(int keyled_count){ this.keyled_count = keyled_count; }
    protected void setSampleCount(int sample_count){ this.sample_count = sample_count; }
    protected void setState(byte state){ this.state = state; }

    public void setStatus(byte STATUS){
        this.status = STATUS;
    }

    public String getTitle(){ return title; }
    public String getProducerName(){ return producerName; }
    public String getDifficulty(){ return difficulty; }
    public File getPath(){ return path; }
    public File getInfoPath(){ return info_path; }
    public File getAutoplayPath(){ return autoplay_path; }
    public File getKeySoundPath(){ return keysound_path; }
    public File getSamplePath(){ return sample_path; }
    public File getKeyLedPath(int index){return (keyleds_paths.size() == 0 || index >= keyleds_paths.size()) ? null : keyleds_paths.get(index); }
    public int getKeyLedCount(){ return keyled_count; }
    public int getSampleCount(){ return sample_count; }
    public byte getState(){ return state; }

    public byte getStatus(){
        return status;
    }

    public void setProjectId(int id){
        this.project_id = id;
    }

    public int getProjectId(){
        return this.project_id;
    }

    //Interfaces
    public interface AutoPlayInterface {
        public boolean isRunning();
        public boolean startAutoPlay();
        public boolean stopAutoPlay();

        public boolean pauseAutoPlay();
        public boolean isPaused();

        public boolean resumeAutoPlay();

        public float advanceAutoPlay();

        public float regressAutoPlay();
    }

    public interface KeyLEDInterface {
        public boolean showLed(int chain, int x, int y, int id);

        public boolean breakLed(int chain, int x, int y, int id);

        public boolean breakAll();
    }

    public interface SoundInterface {
        public boolean playSound(int chain, int x, int y);

        public boolean stopSound(int chain, int x, int y);
    }
}
