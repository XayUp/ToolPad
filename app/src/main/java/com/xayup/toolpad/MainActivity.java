package com.xayup.toolpad;

import android.app.Activity;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.*;
import android.widget.Toast;
import com.itsaky.androidide.logsender.LogSender;
import com.xayup.multipad.MakePads;
import com.xayup.multipad.SkinTheme;
import com.xayup.multipad.VariaveisStaticas;
import com.xayup.multipad.XayUpFunctions;
import com.xayup.toolpad.FrameData;
import com.xayup.toolpad.LedPreview;
import com.xayup.toolpad.databinding.ActivityMainBinding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final Activity context = this;
    private ActivityMainBinding binding;
    private GridLayout grid, colors_list;
    private GridLayout led_frames;
    private RelativeLayout led_frames_background;
    private HorizontalScrollView led_frames_horizontal, colors_list_background;
    private ScrollView led_frames_vertical;
    private int root_h, step_width = 0;
    private SeekBar led_frames_progress;
    private View led_frames_vertical_bar;
    private Button led_frames_prev, led_frames_pause_play, led_frames_next;
    private LedPreview mLedPreview;
    private EditText bpm_edit;

    /*
     * Soma ou subtraia com a id para obter um certo valor
     * util para obter uma view a partir da ID que iniciaria com uma
     * ID que seria usando em outra view (x e y da Grid, codigo de cor) ou
     * identificar o tipo de retorno de um metodo.
     */
    private final int ID_COLOR = 0;
    private final int ID_FRAME = 1;

    public static int selected_pad = -1;
    public static View selected_frame, selected_color;
    public static int color = 0;
    public static FrameData mFrameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Remove this line if you don't want AndroidIDE to show this app's logs
        LogSender.startLogging(this);
        super.onCreate(savedInstanceState);
        // Inflate and get instance of binding
        String errLog = TopExceptionHandler.getErrorLog(this);
        if (errLog == null) {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            // set content view to binding's root
            setContentView(binding.getRoot());
            mainActivity();
        } else {
            setContentView(R.layout.crash);
            ((TextView) findViewById(R.id.logText)).setText(errLog);
        }
    }

    public void mainActivity() {
        getSupportActionBar().hide();
        grid = findViewById(R.id.pads);
        led_frames = findViewById(R.id.led_frames);
        led_frames_background = findViewById(R.id.led_frames_background);
        led_frames_horizontal = findViewById(R.id.led_frames_horizontal);
        led_frames_vertical = findViewById(R.id.led_frames_vertical);
        led_frames_progress = findViewById(R.id.led_frames_progress);
        led_frames_vertical_bar = findViewById(R.id.led_frames_verticalbar);
        colors_list = findViewById(R.id.colors_list);
        colors_list_background = findViewById(R.id.colors_list_background);
        led_frames_prev = findViewById(R.id.led_frames_prev);
        led_frames_pause_play = findViewById(R.id.led_frames_pause_play);
        led_frames_next = findViewById(R.id.led_frames_next);
        bpm_edit = findViewById(R.id.bpm_edit);
        led_frames_progress.setPadding(0, 0, 0, 0);

        View root = grid.getRootView();
        SkinTheme.defaultSkin(this);
        XayUpFunctions.hideSystemBars(getWindow());
        root.post(
                new Runnable() {
                    @Override
                    public void run() {
                        root_h = root.getMeasuredHeight();
                        new MakePads(context, grid, 10, 10, root_h, "").makePadInLayout();
                    }
                });

        led_frames_progress.post(
                () -> {
                    led_frames_progress.setOnSeekBarChangeListener(
                            new SeekBar.OnSeekBarChangeListener() {

                                @Override
                                public void onProgressChanged(
                                        SeekBar arg0, int arg1, boolean arg2) {
                                    ((RelativeLayout.LayoutParams)
                                                    led_frames_vertical_bar.getLayoutParams())
                                            .setMarginStart(step_width * arg0.getProgress());
                                    led_frames_vertical_bar.setVisibility(View.GONE);
                                    led_frames_vertical_bar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar arg0) {
                                    ((RelativeLayout.LayoutParams)
                                                    led_frames_vertical_bar.getLayoutParams())
                                            .setMarginStart(step_width * arg0.getProgress());
                                    led_frames_vertical_bar.setVisibility(View.GONE);
                                    led_frames_vertical_bar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar arg0) {
                                    ((RelativeLayout.LayoutParams)
                                                    led_frames_vertical_bar.getLayoutParams())
                                            .setMarginStart(step_width * arg0.getProgress());
                                    led_frames_vertical_bar.setVisibility(View.GONE);
                                    led_frames_vertical_bar.setVisibility(View.VISIBLE);
                                }
                            });
                    led_frames_horizontal.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    int w = led_frames_horizontal.getMeasuredWidth();
                                    step_width = Math.round(w / 12); // largura  minimo de colunas
                                    led_frames_progress.setMax(12);
                                    led_frames.setLayoutParams(
                                            new LinearLayout.LayoutParams(
                                                    step_width * 12,
                                                    led_frames_horizontal.getMeasuredHeight()
                                                            - led_frames_progress
                                                                    .getMeasuredHeight()));
                                    for (int c = 0; c < 12; c++) {
                                        for (int l = 0; l < 6; l++) {
                                            GridLayout.LayoutParams grid_params =
                                                    new GridLayout.LayoutParams(
                                                            GridLayout.spec(
                                                                    l, GridLayout.FILL, 1.0f),
                                                            GridLayout.spec(
                                                                    c, GridLayout.FILL, 1.0f));
                                            grid_params.height = 0;
                                            grid_params.width = 0;
                                            View frame_block = new View(context);
                                            frame_block.setId(ID_FRAME + c + l);
                                            frame_block.setTag(0);
                                            led_frames.addView(frame_block, grid_params);
                                            frame_block.setOnClickListener(onClick(ID_FRAME));
                                            frame_block.setOnLongClickListener(
                                                    onLongClick(ID_FRAME));
                                        }
                                    }
                                }
                            });
                });

        colors_list_background.post(
                () -> {
                    // Toast.makeText(context, VariaveisStaticas.newColorInt.length +"",
                    // Toast.LENGTH_SHORT).show();
                    int h = colors_list_background.getMeasuredHeight();
                    int items_size = h / 3;
                    int grid_w = 0;
                    for (int c = 0; c < VariaveisStaticas.newColorInt.length; c += 3) {
                        grid_w += items_size;
                        for (int l = 0;
                                (l < 3 & (l + c) < VariaveisStaticas.newColorInt.length);
                                l++) {
                            LinearLayout background = new LinearLayout(context);
                            GridLayout.LayoutParams params =
                                    new GridLayout.LayoutParams(
                                            GridLayout.spec(l, GridLayout.FILL, 1.0f),
                                            GridLayout.spec(c, GridLayout.FILL, 1.0f));
                            View color_view = new View(context);
                            params.height = 0;
                            params.width = 0;
                            params.setMargins(1, 1, 1, 1);
                            color_view.setBackgroundColor(VariaveisStaticas.newColorInt[l + c]);
                            background.setId(ID_COLOR + l + c);
                            background.addView(
                                    color_view,
                                    new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT));
                            background.setOnClickListener(onClick(ID_COLOR));
                            colors_list.addView(background, params);
                            selected_color = background;
                        }
                    }
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(grid_w, h);
                    colors_list.setLayoutParams(params);
                });
        colors_list_background.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View arg0, MotionEvent arg1) {
                        return false;
                    }
                });

        led_frames_horizontal.setOnTouchListener(bidirecionalScroll(led_frames_horizontal));
        led_frames_vertical.setOnTouchListener(bidirecionalScroll(led_frames_vertical));
        led_frames_pause_play.setOnClickListener(onClick(led_frames_pause_play.getId()));

        led_frames.setOnTouchListener(null);
    }

    // Metodos de interacao

    public View.OnTouchListener onTouch(int type) {
        switch (type) {
            case ID_FRAME:
                {
                    return new View.OnTouchListener() {
                        boolean moved = false;
                        float pivot_x = 0;
                        float pivot_y = 0;

                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    pivot_x = event.getX();
                                    pivot_y = event.getY();
                                    return true;
                                case MotionEvent.ACTION_UP:
                                    // Toast.makeText(context, moved + "", 300).show();
                                    /*
                                     * Se foi apenas tocado mas nao rolado/movido
                                     */
                                    if (selected_pad != -1) {
                                        // Toast.makeText(context, "touched",
                                        // Toast.LENGTH_SHORT).show();
                                        if (selected_frame != null) {
                                            selected_frame.setPadding(0, 0, 0, 0);
                                        }
                                        final float x = event.getX();
                                        View frame_block = new View(context);
                                        LinearLayout frame_block_background =
                                                new LinearLayout(context);
                                        RelativeLayout.LayoutParams params =
                                                new RelativeLayout.LayoutParams(40, 40);
                                        Map<Integer, Object> frame_info = new HashMap<>();
                                        List<Map<Integer, Object>> frame = new ArrayList<>();
                                        frame.add(frame_info);
                                        params.setMargins((int) x, (int) event.getY(), 0, 0);
                                        frame_block.setBackgroundColor(Color.RED);
                                        frame_block_background.setBackgroundColor(Color.WHITE);
                                        frame_block_background.setId((int) x);
                                        frame_block_background.setLayoutParams(params);
                                        frame_block_background.addView(
                                                frame_block,
                                                new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.MATCH_PARENT));
                                        frame_block_background.setOnClickListener(
                                                onClick(ID_COLOR));
                                        led_frames.addView(frame_block_background);
                                        mFrameData.addNewFrame((int) x, selected_pad);
                                        selected_frame = frame_block_background;
                                        selected_frame.setPadding(2, 2, 2, 2);
                                    } else {
                                        Toast.makeText(
                                                        context,
                                                        "Selecione um botão",
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                    return true;
                            }
                            return false;
                        }
                    };
                }
        }
        return null;
    }

    public View.OnTouchListener bidirecionalScroll(final View v) {
        if (v.getId() == led_frames_horizontal.getId()) {
            return new View.OnTouchListener() {
                float pivot_y = 0;
                int old_scroll_y = 0;
                boolean down = true;

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            {
                                down = true;
                                return false;
                            }
                        case MotionEvent.ACTION_MOVE:
                            {
                                if (down == true) { // ACTION_DOWN nao funciona entao vamos
                                    // improvisar
                                    pivot_y = event.getY();
                                    old_scroll_y = led_frames_vertical.getScrollY();
                                    down = false;
                                }
                                float get_y = event.getY();
                                int to_y = (int) (old_scroll_y + (pivot_y - get_y));
                                led_frames_vertical.setScrollY(to_y);
                                return false;
                            }
                    }
                    return false;
                }
            };
        } else {
            return new View.OnTouchListener() {
                float pivot_x = 0;
                int old_scroll_x = 0;
                boolean down = true;

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            {
                                down = true;
                                return false;
                            }
                        case MotionEvent.ACTION_MOVE:
                            {
                                if (down == true) { // ACTION_DOWN nao funciona entao vamos
                                    // improvisar
                                    pivot_x = event.getX();
                                    old_scroll_x = led_frames_horizontal.getScrollX();
                                    //  Toast.makeText(context, "X: " + old_scroll_x + "Y: " +
                                    // led_frames_horizontal.getScrollY(), 50).show();
                                    down = false;
                                }
                                float get_x = event.getX();
                                int to_x =
                                        (int)
                                                (led_frames_horizontal.getScrollX()
                                                        + (pivot_x - get_x));
                                led_frames_horizontal.setScrollX(to_x);
                                return false;
                            }
                    }
                    return false;
                }
            };
        }
    }

    public View.OnClickListener onClick(final int type) {
        switch (type) {
            case ID_COLOR:
                {
                    return new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            color = view.getId() - ID_COLOR;
                            if (selected_color != null) {
                                selected_color.setPadding(0, 0, 0, 0);
                                view.setPadding(3, 3, 3, 3);
                            }
                            selected_color = view;
                        }
                    };
                }
            case ID_FRAME:
                {
                    return new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (selected_pad == -1) {
                                Toast.makeText(
                                                context,
                                                "Nenhuma pad selecionada",
                                                Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            if (selected_frame != null) {
                                if (mFrameData.containsFrame(view.getLeft(), selected_pad)
                                        && (int) view.getTag() == 0) {
                                    Toast.makeText(context, "Coluna já ocupada", 0).show();
                                    return;
                                }
                                selected_frame.setBackgroundColor(Color.CYAN);
                            }
                            // tente adicionar uma frame. Caso ela exista retornara falso
                            if (!mFrameData.addFrame(view.getLeft(), selected_pad)) {
                                mFrameData.stateFrame(context, grid, selected_pad, selected_frame, view);
                            }
                            view.setTag(1);
                            selected_frame = view;
                            Toast.makeText(context, view.getLeft() + "", Toast.LENGTH_SHORT).show();
                            view.setBackgroundColor(Color.GREEN);
                        }
                    };
                }
            case R.id.led_frames_pause_play:
                {
                    return new Button.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            if (selected_pad == -1) {
                                return;
                            }
                            try {
                                int bpm = Integer.parseInt(bpm_edit.getText().toString());
                                if (mLedPreview == null) {
                                    mLedPreview =
                                            new LedPreview(
                                                    context,
                                                    grid,
                                                    mFrameData,
                                                    led_frames_progress,
                                                    selected_pad,
                                                    step_width);
                                    mLedPreview.run(bpm);
                                } else {
                                    mLedPreview.update(bpm, step_width);
                                    if (!mLedPreview.isRunning()) {
                                        mLedPreview.run(bpm);
                                    } else if (mLedPreview.isPaused()) {
                                        mLedPreview.unpausePreview();
                                    } else {
                                        mLedPreview.pausePreview();
                                    }
                                }
                            } catch (NumberFormatException n) {
                                Toast.makeText(context, "Defina a BPM", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                }
        }

        return null;
    }

    public View.OnLongClickListener onLongClick(int type) {
        switch (type) {
            case ID_FRAME:
                {
                    return new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            if (mFrameData.removeFrame(
                                    grid, view.getLeft(), selected_pad, (selected_frame == view))) {
                                view.setBackgroundColor(0);
                                view.setTag(0);
                            }
                            selected_frame = (selected_frame == view) ? null : selected_frame;
                            return true;
                        }
                    };
                }
        }
        return null;
    }
}
