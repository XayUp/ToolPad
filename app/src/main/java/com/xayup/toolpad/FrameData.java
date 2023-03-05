package com.xayup.toolpad;

import android.app.Activity;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;
import com.xayup.multipad.VariaveisStaticas;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FrameData {
    /*
     * Para facilitar
     * Isso e as informacoes de cada frame por ID
     * A partir do ID se obtém o tipo, ID da pad e cor
     */
    protected Activity context;

    public Map<Integer, Map<Integer, Map<Integer, Object>>> pad_frames;
    //                pad,     frame_id      led_on_pad     colo
    // protected Map<Integer, Object> frame;

    protected boolean runner;

    public FrameData(Activity context, int pad_id) {
        pad_frames = new HashMap<>();
        pad_frames.put(pad_id, new HashMap<>());
        this.context = context;
    }

    public boolean addToFrame(int frame_id, int pad_id, int led_on_pad, int color) {
        Map<Integer, Object> frame = pad_frames.get(pad_id).get(frame_id);
        if (frame == null) {
            frame = new HashMap<>();
        }
        frame.put(led_on_pad, color);
        Toast.makeText(context, led_on_pad + "", 0).show();
        pad_frames.get(pad_id).put(frame_id, frame);
        return true;
    }

    public boolean addNewFrame(int frame_id, int pad_id) {
        Map<Integer, Object> frame_values = new HashMap<>();
        pad_frames.get(pad_id).put(frame_id, frame_values);
        return true;
    }

    public boolean addFrame(int frame_id, int pad_id) {
        if (containsFrame(frame_id, pad_id)) {
            return false;
        } else {
            addNewFrame(frame_id, pad_id);
            return true;
        }
    }

    public void ledOff(int frame_id, int pad_id, int led_on_pad) {
        Map<Integer, Object> frame = pad_frames.get(pad_id).get(frame_id);
        if (frame.containsKey(led_on_pad)) {
            if ((int) frame.get(led_on_pad) != 0) {
                frame.remove(led_on_pad);
                context.findViewById(pad_id).findViewById(R.id.led).setBackgroundColor(0);
            }
        } else {
            frame.put(led_on_pad, 0);
        }
    }

    public boolean removeFrame(final View grid, int frame_id, int pad_id, boolean selected_frame) {
        try {
            if (selected_frame) {
                ligthLeds(grid, frame_id, pad_id, 0);
            }
            pad_frames.get(pad_id).remove(frame_id);
            return true;
        } catch (NullPointerException n) {
            return false;
        }
    }

    public boolean containsFrame(int frame_id, int pad_id) {
        return pad_frames.get(pad_id).containsKey(frame_id);
    }

    public Map<Integer, Object> getFrameValues(int pad_id, int pixel) {
        return pad_frames.get(pad_id).get(pixel);
    }

    protected Object getChain(int led_on_pad) {
        if (VariaveisStaticas.chainsIDlist.contains("" + led_on_pad)) {
            return VariaveisStaticas.chainsIDlist.indexOf(led_on_pad);
        }
        return null;
    }

    public Map<Integer, Object> getFrame(int frame_id, int pad_id) {
        return pad_frames.get(pad_id).get(frame_id);
    }

    public void ligthLeds(
            final View grid, int frame_id, int pad_id, final int state /*0 - off, 1 - on*/) {
        Map<Integer, Object> frame = getFrame(frame_id, pad_id);
        if (frame != null) {
            for (int key : frame.keySet()) {

                context.runOnUiThread(
                        () -> {
                            try {
                                grid.findViewById(key)
                                        .findViewById(R.id.led)
                                        .setBackgroundColor(
                                                state
                                                        * VariaveisStaticas.newColorInt[
                                                                (int) frame.get(key)]);
                            } catch (NullPointerException n) {
                            }
                        });
            }
        }
    }

    public boolean stateFrame(
            Activity context,
            View grid,
            final int pad_id,
            final View old_frame,
            final View new_frame) {
        if (new_frame == null) {
            return false;
        }
        new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                int start = 0;
                                int[] frame_ids = new int[2];
                                frame_ids[1] = new_frame.getLeft();
                                try {
                                    frame_ids[0] = old_frame.getLeft();
                                } catch (NullPointerException n) {
                                    start++;
                                }
                                for (int i = start; i < 2; i++) {
                                    ligthLeds(grid, frame_ids[i], pad_id, i);
                                    /*
                                    if (frames.get(i) != null) {
                                        final int led_on = i;
                                        for (int key : frames.get(i).keySet()) {
                                            context.runOnUiThread(
                                                    () -> {
                                                        context.findViewById(key)
                                                                .findViewById(R.id.led)
                                                                .setBackgroundColor(
                                                                        led_on
                                                                                * VariaveisStaticas
                                                                                        .newColorInt[
                                                                                        (int)
                                                                                                frames.get(
                                                                                                                led_on)
                                                                                                        .get(
                                                                                                                key)]);
                                                    });
                                        }
                                    }*/
                                }
                            }
                        })
                .start();
        return true;
    }
}
