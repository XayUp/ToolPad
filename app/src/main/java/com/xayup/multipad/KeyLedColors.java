package com.xayup.multipad;

import android.os.SystemClock;
import android.util.ArrayMap;
import android.view.View;
import android.widget.*;
import android.app.*;
import com.xayup.multipad.VariaveisStaticas;
import java.util.ArrayList;
import java.lang.*;

import com.xayup.toolpad.R;

public class KeyLedColors {
    //private final int[] colorInt = {0, -8355712, -1644050, -394759, -607281, -1158322, -1739918, -1140070, -134434, -22491, -83872, -144257, -8015, -70313, -68198, -1342, -2232890, -7617974, -5450110, -4202594, -10624593, -16724163, -16663998, -15623105, -11080767, -16717981, -16720038, -16722876, -8716835, -16718653, -16654157, -16716092, -11931665, -16718122, -16718384, -16715812, -9839619, -16721155, -16656388, -16274212, -9318916, -15819785, -15495709, -13994039, -7957508, -14575885, -12228107, -12495907, -5335555, -8170244, -10401076, -11122748, -18433, -1547268, -2730515, -3125272, -288031, -1689662, -1822788, -1952333, -1297348, -22491, -795127, -10044566, -10759936, -16723299, -14448386, -13210884, -16730931, -12166182, -985867, -1183246, -645578, -3020421, -3743731, -8395486, -16660174, -16722523, -16721412, -16016388, -10986508, -5482001, -2522916, -4685495, -26880, -5513438, -6364844, -10044566, -12854457, -9380167, -14165030, -6436611, -8865546, -5263377, -2719509, -569399, -1340897, -2368761, -6498547, -732362, -3690687, -16659862, -14363985, -9077057, -11237157, -1653338, -1492164, -351583, -1205147, -1914254, -4662663, -6826708, -10392388, -3487583, -7285310, -3219970, -4207368, -2959395, -2433053, -1644050, -125404, -3397853, -6501274, -16736996, -256, -4279790, -667619, -1869784};
    //private final int[] chainCode = {0, 1, 2, 3, 4, 5, 6, 7, 8, 19, 29, 39, 49, 59, 69, 79, 89, 98, 97, 96, 95, 94, 93, 92, 91, 80, 70, 60, 50, 40, 30, 20, 10};
    private String cpled;
    //private String currentProjDir;
    //private Activity context;
    private int rpt;
    private int corcode;
    //private int padId;

    public KeyLedColors(){
		VariaveisStaticas.threadMap = new ArrayMap<String, ThreadLed>();
    }

    public void readKeyLed(int rpt, String cpled, Activity context, View root){
		if((VariaveisStaticas.threadMap.get(cpled) != null) && !VariaveisStaticas.spamLeds && VariaveisStaticas.threadMap.get(cpled).isRunning()){
			VariaveisStaticas.threadMap.get(cpled).stop();
		}
		VariaveisStaticas.threadMap.put(cpled, null);
		VariaveisStaticas.threadMap.put(cpled, new ThreadLed(context, cpled, rpt, root));
		VariaveisStaticas.threadMap.get(cpled).start();
    }
}
