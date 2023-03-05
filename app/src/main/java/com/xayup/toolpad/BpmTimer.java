package com.xayup.toolpad;

import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class BpmTimer {
    Context context;
    int hit_per_seconds;
    long hit_per_miliseconds;
    

    public BpmTimer(Context context) {
        this.context = context;
    }

    public int pixelPerSecondsByBpm(int bpm) {
        return bpm / 60; // 60 sao 1 minuto em segundos
    }

    public int getDelayByPixelDistance(int distance, int bpm) {
        return (distance / pixelPerSecondsByBpm(bpm)) * 1000; // 1000 e 1 segundo em milisegundos
    }
    
    public long getDelayMiliseconds(int bpm){
        return Math.round(1000/(bpm/60)); //a divisao da bpm com 60 (1 minuto, em segundos) resultara em quantas batidas tera 1 segundo
    }

    public void framesToFile(String save_local_path, Map<Integer, String> frames, int bpm) {
        try {
            File file = new File(save_local_path + File.separator + "keyleds");
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new FileNotFoundException("Storage access: false");
                }
            }

            FileWriter file_write = new FileWriter(file);
            SortedSet<Integer> keys = new TreeSet<>(frames.keySet());
            int old_pixel = 0; //Inicialmente zero pois o primeiro freme nao tem atencesor a nao ser o inicio que e 0
            for (int i : keys) {
                file_write.write(
                        getDelayByPixelDistance((i - old_pixel), bpm)
                                + "\n"
                                + frames.get(i)
                                + "\n");
                old_pixel = i;
            }
            file_write.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        } catch (IOException io) {
            Toast.makeText(context, io.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
