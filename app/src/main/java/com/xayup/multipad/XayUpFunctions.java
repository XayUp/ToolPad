package com.xayup.multipad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.xayup.multipad.VariaveisStaticas;
import java.io.IOException;

import com.xayup.toolpad.R;

public class XayUpFunctions {
    
    public static final int RELEASE = 0;
    public static final int TOUCH = 1;
    public static final int TOUCH_AND_RELEASE = 2;
    
	private static int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

	//Fullscreen AlertDialog
	protected static void showDiagInFullscreen(AlertDialog theDialog) {
		theDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		theDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
		theDialog.show();
		theDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
	}

	protected static void showDiagInFullscreen(ProgressDialog theDialog) {
		theDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		theDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
		theDialog.show();
		theDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
	}

	protected static void showDiagInFullscreen(Dialog theDialog) {
		theDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		theDialog.getWindow().getDecorView().setSystemUiVisibility(flags);
		theDialog.show();
		theDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
	}

	//Fullscreen current window
	public static void hideSystemBars(Window getWindow) {
		getWindow.getDecorView().setSystemUiVisibility(flags);
		final View decorView = getWindow.getDecorView();
		decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
			@Override
			public void onSystemUiVisibilityChange(int arg0) {
				if (arg0 == 0) {
					decorView.setSystemUiVisibility(flags);
				}
			}
		});
	}

	//stop leds
	public static void clearLeds(Activity context, View root) {
		for (int i = 1; (i <= 98); i++) {
			if (i != 9 & i != 90) {
				int fi = i;
				context.runOnUiThread(new Runnable() {
					@Override
					public void run() {
					/*	if (VariaveisStaticas.glows != null)
							VariaveisStaticas.glows.offGlows();*/
						root.getRootView().findViewById(fi).findViewById(R.id.led).setBackgroundColor(0);
//						try {
//							if (MidiStaticVars.midiInput != null) {
//								int offset = 0;
//								int numBytes = 0;
//								byte[] bytes = new byte[32];
//								bytes[numBytes++] = (byte) ((MidiStaticVars.NOTE_OFF & 0xFF)
//										+ (MidiStaticVars.CHANNEL - 1));
//								bytes[numBytes++] = (byte) (UsbDeviceActivity.rowProgramMode(fi));
//								bytes[numBytes++] = (byte) 0;
//								MidiStaticVars.midiInput.send(bytes, numBytes, offset);
//							}
//						} catch (IOException e) {
//						}
					}
				});
			}
		}
	}

	//Toque na visualizacao
	public static void touchAndRelease(final Activity context, final int ViewId, final int type) {
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
                    View v = context.findViewById(ViewId);
                    switch(type){
                        case TOUCH:
                            v.dispatchTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0, 0, 0));
                        break;
                        case RELEASE:
                            v.dispatchTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0, 0, 0));
                        break;
                        case TOUCH_AND_RELEASE:
                            v.dispatchTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0, 0, 0));
			            	v.dispatchTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0, 0, 0));
			
                        break;
                    }
				}
		});
	}

	//hide pad/chains overlay
	protected static void hidePC(int visibility) {
		for (int i = 0; i < SkinTheme.pads.size(); i++) {
			SkinTheme.pads.get(i).setVisibility(visibility);
		}
		for (int i = 0; i < SkinTheme.padsCenter.size(); i++) {
			SkinTheme.padsCenter.get(i).setVisibility(visibility);
		}
		for (int i = 0; i < SkinTheme.chainsled.size(); i++) {
			SkinTheme.chainsled.get(i).setVisibility(visibility);
		}
	}
}