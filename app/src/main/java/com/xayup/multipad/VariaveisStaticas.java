package com.xayup.multipad;

import android.os.Environment;
import android.widget.Button;
import com.xayup.multipad.AutoPlayFunc;
import com.xayup.multipad.SoundLoader;
import com.xayup.multipad.ThreadLed;
import com.xayup.multipad.KeyLedColors;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariaveisStaticas {
	//	"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99"}
	//	protected static final int[] colorInt = {0, -8355712, -1644050, -394759, -607281, -1158322, -1739918, -1140070, -134434, -22491, -83872, -144257, -8015, -70313, -68198, -1342, -2232890, -7617974, -5450110, -4202594, -10624593, -16724163, -16663998, -15623105, -11080767, -16717981, -16720038, -16722876, -8716835, -16718653, -16654157, -16716092, -11931665, -16718122, -16718384, -16715812, -9839619, -16721155, -16656388, -16274212, -9318916, -15819785, -15495709, -13994039, -7957508, -14575885, -12228107, -12495907, -5335555, -8170244, -10401076, -11122748, -18433, -1547268, -2730515, -3125272, -288031, -1689662, -1822788, -1952333, -1297348, -22491, -795127, -10044566, -10759936, -16723299, -14448386, -13210884, -16730931, -12166182, -985867, -1183246, -645578, -3020421, -3743731, -8395486, -16660174, -16722523, -16721412, -16016388, -10986508, -5482001, -2522916, -4685495, -26880, -5513438, -6364844, -10044566, -12854457, -9380167, -14165030, -6436611, -8865546, -5263377, -2719509, -569399, -1340897, -2368761, -6498547, -732362, -3690687, -16659862, -14363985, -9077057, -11237157, -1653338, -1492164, -351583, -1205147, -1914254, -4662663, -6826708, -10392388, -3487583, -7285310, -3219970, -4207368, -2959395, -2433053, -1644050, -125404, -3397853, -6501274, -16736996, -256, -4279790, -667619, -1869784};
	protected static final int[] chainCode = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 19, 29, 39, 49, 59, 69, 79, 89, 98, 97, 96,
			95, 94, 93, 92, 91, 80, 70, 60, 50, 40, 30, 20, 10 };
	// -6111237
	/*Old color*/ protected static final int[] colorInt = { 0, -8355712, -1644050, -394759, -607281, -1158322, -1739918,
			-1140070, -134434, -22491, -83872, -144257, -8015, -70313, -68198, -1342, -2232890, -7617974, -5450110,
			-4202594, -10624593, -16724163, -16663998, -15623105, -11080767, -16717981, -16720038, -16722876, -8716835,
			-16718653, -16654157, -16716092, -11931665, -16718122, -16718384, -16715812, -9839619, -16721155, -16656388,
			-16274212, -9318916, -15819785, -15495709, -13994039, -7957508, -14575885, -12228107, -12495907, -5335555,
			-8170244, -10401076, -11122748, -18433, -1547268, -2730515, -3125272, -288031, -1689662, -1822788, -1952333,
			-1297348, -22491, -795127, -10044566, -10759936, -16723299, -14448386, -13210884, -16730931, -12166182,
			-985867, -1183246, -645578, -3020421, -3743731, -8395486, -16660174, -16722523, -16721412, -16016388,
			-10986508, -5482001, -2522916, -4685495, -26880, -5513438, -6364844, -10044566, -12854457, -9380167,
			-14165030, -6436611, -8865546, -5263377, -2719509, -569399, -1340897, -2368761, -6498547, -732362, -3690687,
			-16659862, -14363985, -6111237, -11237157, -1653338, -1492164, -351583, -1205147, -1914254, -4662663,
			-6826708, -10392388, -3487583, -7285310, -3219970, -4207368, -2959395, -2433053, -1644050, -125404,
			-3397853, -6501274, -16736996, -256, -4279790, -667619, -1869784 };
	/*New*/ public static final int[] newColorInt = { 0, -9803419, -5592668, -65538, -34442, -65536, -1572864,
			-5570560, -11638, -32512, -3512320, -4627968, -8314, -67584, -3751168, -6118656, -11141274, -16711936,
			-16594944, -12879104, -16711802, -16713728, -16723456, -16744448, -12386416, -16711834, -16725940,
			-16736699, -13697121, -16711784, -16727945, -16736917, -13303867, -16711738, -16727918, -16738441,
			-13238274, -16715777, -16727872, -16734043, -8263169, -16730625, -16741694, -16752000, -6638593, -15007490,
			-15400765, -15924088, -4610561, -7732993, -9436978, -10747741, -24610, -65311, -2424651, -6553489, -30550,
			-65374, -3014560, -5373875, -40960, -24832, -2575872, -9725696, -13668608, -16754113, -16755561, -15334710,
			-16746903, -9895736, -4344156, -12828871, -65536, -3473920, -6242048, -10229760, -14044416, -16725379,
			-16721690, -16736027, -9633562, -6553369, -34605, -11059968, -38656, -4588032, -4980900, -16712704,
			-14090404, -14745703, -13697069, -9313541, -11759457, -8222517, -824066, -65367, -32768, -3423488, -7865600,
			-6647552, -8689664, -16749767, -16752572, -12566202, -13220535, -9806529, -9437184, -4951201, -3043757,
			-8114, -5243061, -8868352, -12631731, -131159, -5505112, -5254928, -3029778, -10000284, -5590103, -3677239,
			-3670016, -8585216, -16730112, -16740864, -4212224, -7044608, -5078528, -8436736 };
			
	final static int[] launchpad_pads_map_to_multipad = {
		/*Chain Top*/
		1, 2, 3, 4, 5, 6, 7, 8,
		
		/*Pads*/
		81, 82, 83, 84, 71, 72, 73, 74, 61, 62, 63, 64, 51, 52, 53, 54, 41, 42, 43, 44, 31, 32, 33, 34, 21, 22, 23,
		24, 11, 12, 13, 14,
		
		85, 87, 88, 84, 75, 77, 73, 74, 65, 67, 63, 64, 55, 57, 53, 54, 45, 46, 47, 48, 35, 36, 37, 38, 25, 26, 27,
		28, 15, 16, 17, 18,
		
		/*Chain Right*/
		19, 29, 39, 49, 59, 69, 79, 89,
		
		/*Chain Left*/
		10, 20, 30, 40, 50, 60, 70, 80,
		
		/*Chain Bottom*/
	91, 92, 93, 94, 95, 96, 97, 98 };
			
	public static Map<Integer, Integer> customColorInt = new HashMap<Integer, Integer>();

	public static int colorInt(int color, boolean CUSTOM, boolean UNIPAD_COLOR) {
		if (CUSTOM) {
			return customColorInt.get(color);
		} else {
			if (UNIPAD_COLOR) {
				return colorInt[color];
			} else {
				return newColorInt[color];
			}
		}
	}

	public static final String[] chainsID = { "0"/*para balancear*/, "19", "29", "39", "49", "59", "69", "79", "89",
			"98", "97", "96", "95", "94", "93", "92", "91", "80", "70", "60", "50", "40", "30", "20", "10" };
	public static final List<String> btnsIDList = Arrays.asList(new String[] { "11", "12", "13", "14", "15", "16", "17",
			"18", "21", "22", "23", "24", "25", "26", "27", "28", "31", "32", "33", "34", "35", "36", "37", "38", "41",
			"42", "43", "44", "45", "46", "47", "48", "51", "52", "53", "54", "55", "56", "57", "58", "61", "62", "63",
			"64", "65", "66", "67", "68", "71", "72", "73", "74", "75", "76", "77", "78", "81", "82", "83", "84", "85",
			"86", "87", "88" });
	public final String[] chainsF = { "11", "12", "13", "14", "15", "16", "17", "18" };
	public final static List<String> chainsIDlist = Arrays.asList(chainsID);

	//Diretorios
	public static final String MULTIPAD_PATCH = Environment.getExternalStorageDirectory() + "/MultiPad";
	public static final String COLOR_TABLE_PATCH = MULTIPAD_PATCH + "/LCT";
    
    //Congigurações
    
    public static String chainSl = "1";
	public static String getCurrentPath;

    public static Map<Integer, String> toChainPool;
    public static Map<String, Integer> streamsPool;
	public static Map<Integer, Integer> soundrpt;
	public static Map<String, Integer> ledrpt;
	public static Map<String, File> fileProj;
	public static Map<String, List<List<String>>> ledFiles;
	public static Map<String, ThreadLed> threadMap;
	public static Map<String, Integer> chainClickable;
    
    public static int glowPadRadius, glowChainRadius;
    
	public static int otherChain;
	public static int oldPad;
	public static int perAutoPlay;
	public static int chainId;
	public static int padWH;
	public static int display_height; //MainActivity.height;//Default MainActivity.height, ou ponha um valro estatico, quanto menor que a altura de sua tela, menor os botoes

	public static float watermark, padPressAlpha, glowIntensity, glowChainIntensity;

	public static boolean mk2, autoPlayCheck, spamSounds, spamLeds, slideMode, oldColors, changeChainGlows, pressLed, custom_color_table;

	public static List<String> autoPlay, invalid_formats;

	public static AutoPlayFunc autoPlayThread;
	public static KeyLedColors ledFunc;
	public static SoundLoader mSoundLoader;

	public static Button stopRecAutoplay;
	
    public static boolean stopAll, glowEf, recAutoplay, useSoundPool;
    
//	public static VerticalSeekBar progressAutoplay;
    
    
}