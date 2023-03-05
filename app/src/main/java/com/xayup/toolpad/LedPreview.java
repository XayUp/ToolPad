package com.xayup.toolpad;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.widget.SeekBar;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.exoplayer2.text.ttml.TtmlDecoder;
import com.xayup.multipad.VariaveisStaticas;
import com.xayup.toolpad.BpmTimer;
import com.xayup.toolpad.FrameData;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class LedPreview implements Runnable {
    Activity context;
    AtomicBoolean running;
    BpmTimer mBpmTimer;
    SeekBar progress;
    FrameData mFrameData;
    View grid;
    boolean pause;
    long paused_time, pointer_velocity;
    int bpm, pad_id, step_width;
    

    public LedPreview(
            Activity context, View grid, FrameData mFrameData, SeekBar progress, int pad_id, int step_width) {
        this.context = context;
        running = new AtomicBoolean();
        pause = false;
        this.progress = progress;
        this.pad_id = pad_id;
        this.step_width = step_width;
        this.mFrameData = mFrameData;
        this.grid = grid;
        this.mBpmTimer = new BpmTimer(context);
    }

    public void stopPreview() {
        pause = false;
        running.set(false);
    }

    public boolean isRunning() {
        return running.get();
    }

    public void pausePreview() {
        paused_time = SystemClock.uptimeMillis();
        pause = true;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
        pointer_velocity = mBpmTimer.getDelayMiliseconds(bpm);
    }

    public void update(int bpm, int step_width) {
        this.step_width = step_width;
        setBpm(bpm);
    }

    public void unpausePreview() {
        pause = false;
    }

    public boolean isPaused() {
        return pause;
    }

    public void run(int bpm) {
        unpausePreview();
        running.set(true);
        update(bpm, step_width);
        new Thread(this).start();
    }

    private void showLed(Activity context, final View grid, final int padid, final int corCode) {
        context.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) grid.findViewById(padid).findViewById(R.id.led))
                                .setBackgroundColor(VariaveisStaticas.newColorInt[corCode]);
                        /*
                        if(MidiStaticVars.midiInput != null){
                        	try{
                        	int offset = 0;
                        	int channel = 1;
                        	int numBytes = 0;
                        	byte[] bytes = new byte[32];
                        	bytes[numBytes++] = (byte) (NOTE + (channel - 1));
                        	bytes[numBytes++] = (byte) (UsbDeviceActivity.rowProgramMode(padid));
                        	bytes[numBytes++] = (byte) corCode;
                        	MidiStaticVars.midiInput.send(bytes, offset, numBytes);
                        	} catch (IOException e){
                        		Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        	}
                        } */
                    }
                });
    }

    @Override
    public void run() {
        int color, frame_id;
        long time;
        Set<Integer> frame_led_on_pad;
        Map<Integer, Object> frame_led_value;
        while (running.get() && progress.getProgress() < progress.getMax()) {
            if (!running.get()) {
                break;
            }
            time = SystemClock.uptimeMillis();
            time += pointer_velocity;
            frame_id = progress.getProgress() * step_width;
            if (mFrameData.containsFrame(frame_id, pad_id)) {
                final int step_a = frame_id;
                context.runOnUiThread(
                    () -> {
                        Toast.makeText(context, step_a + "", Toast.LENGTH_SHORT).show();
                    });
                frame_led_value = mFrameData.getFrameValues(pad_id, frame_id);
                frame_led_on_pad = frame_led_value.keySet();
                for (int led_on_pad : frame_led_on_pad) {
                    if (!running.get()) {
                        break;
                    }
                    color = (int) frame_led_value.get(led_on_pad);
                    showLed(context, grid, led_on_pad, color);
                }
            }
            progress.setProgress(progress.getProgress() + 1);
            while (running.get() && SystemClock.uptimeMillis() < time || isPaused()) {
                if (isPaused() && SystemClock.uptimeMillis() > paused_time + (7 * 1000)) {
                    running.set(false);
                    unpausePreview();
                }
            }
        }
        stopPreview();
    }
}
