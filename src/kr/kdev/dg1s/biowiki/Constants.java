package kr.kdev.dg1s.biowiki;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class Constants {

    public static final DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageOnLoading(R.drawable.remote_image)
            .showImageOnFail(R.drawable.remote_failed)
            .build();

    public static final String URL_TOS = "http://en.wordpress.com/tos";
    /*
     * Reader constants
     */
    public static final int READER_MAX_POSTS_TO_REQUEST = 20;                          // max #posts to request when updating posts (should be an even # to avoid "hanging post" in 2-column grid mode)
    public static final int READER_MAX_COMMENTS_TO_REQUEST = READER_MAX_POSTS_TO_REQUEST; // max #comments to request when updating comments
    public static final int READER_MAX_POSTS_TO_DISPLAY = 200;                         // max #posts to display in ReaderPostListFragment
    public static final int READER_MAX_USERS_TO_DISPLAY = 500;                         // max #users to show in ReaderUserListActivity
    public static final long READER_AUTO_UPDATE_DELAY_MINUTES = 15;                       // 15 minute delay between automatic updates
    // intent IDs
    public static final int INTENT_READER_TAGS = 1000;
    public static final int INTENT_READER_REBLOG = 1001;
    public static final int INTENT_COMMENT_EDITOR = 1010;
    public static String readerURL = "https://en.wordpress.com/reader/mobile/v2";
    public static String readerLoginURL = "https://wordpress.com/wp-login.php";
    public static String readerURL_v3 = "https://en.wordpress.com/reader/mobile/v2/?chrome=no";
    public static String authorizedHybridHost = "en.wordpress.com";
    public static String readerTopicsURL = "http://en.wordpress.com/reader/mobile/v2/?template=topics";
    public static String wpcomXMLRPCURL = "https://wordpress.com/xmlrpc.php";
    public static String videoPressURL = "http://videopress.com";
    public static int QUICK_POST_PHOTO_CAMERA = 0;
    public static int QUICK_POST_PHOTO_LIBRARY = 1;
    public static int QUICK_POST_VIDEO_CAMERA = 2;
    public static int QUICK_POST_VIDEO_LIBRARY = 3;

    public static int[] flowerDrawable1 = {R.drawable.d1101, R.drawable.d1102, R.drawable.d1103, R.drawable.d1104,
            R.drawable.d1105, R.drawable.d1106, R.drawable.d1107, R.drawable.d1108,
            R.drawable.d1109, R.drawable.d1110, R.drawable.d1111_1, R.drawable.d1111_2};
    public static int[] flowerDrawable2 = {R.drawable.d1201, R.drawable.d1202, R.drawable.d1203};
    public static int[] flowerDrawable3 = {R.drawable.d1301, R.drawable.d1302, R.drawable.d1303, R.drawable.d1304,
            R.drawable.d1305, R.drawable.d1306, R.drawable.d1307, R.drawable.d1308,
            R.drawable.d1309, R.drawable.d1310, R.drawable.d1311, R.drawable.d1312,
            R.drawable.d1313, R.drawable.d1314, R.drawable.d1315, R.drawable.d1316,
            R.drawable.d1317};
    public static int[] leafDrawable1 = {R.drawable.d2101, R.drawable.d2102, R.drawable.d2103, R.drawable.d2104,
            R.drawable.d2105, R.drawable.d2106, R.drawable.d2107, R.drawable.d2108,
            R.drawable.d2109};
    public static int[] leafDrawable2 = {R.drawable.d2201, R.drawable.d2202, R.drawable.d2203, R.drawable.d2204,
            R.drawable.d2205, R.drawable.d2206, R.drawable.d2207, R.drawable.d2208,
            R.drawable.d2209, R.drawable.d2210, R.drawable.d2211, R.drawable.d2212,
            R.drawable.d2213, R.drawable.d2214, R.drawable.d2215, R.drawable.d2216,
            R.drawable.d2217};
    public static int[] leafDrawable3 = {R.drawable.d2301, R.drawable.d2302, R.drawable.d2303, R.drawable.d2304,
            R.drawable.d2305, R.drawable.d2306, R.drawable.d2307, R.drawable.d2308,
            R.drawable.d2309, R.drawable.d2310, R.drawable.d2311, R.drawable.d2312};
    public static int[] leafDrawable4 = {R.drawable.d2401, R.drawable.d2402, R.drawable.d2403, R.drawable.d2404,
            R.drawable.d2405, R.drawable.d2406, R.drawable.d2407, R.drawable.d2408,
            R.drawable.d2409, R.drawable.d2410, R.drawable.d2411, R.drawable.d2412,
            R.drawable.d2413, R.drawable.d2414, R.drawable.d2415, R.drawable.d2416,
            R.drawable.d2417};
    public static int[] leafDrawable5 = {R.drawable.d2501, R.drawable.d2502, R.drawable.d2503, R.drawable.d2504,
            R.drawable.d2505};
    public static int[] leafDrawable6 = {R.drawable.d2601, R.drawable.d2602, R.drawable.d2603};
    public static int[] leafDrawable7 = {R.drawable.d2701, R.drawable.d2702, R.drawable.d2703, R.drawable.d2704};
    public static int[] fruitDrawable = {R.drawable.d3001, R.drawable.d3002, R.drawable.d3003, R.drawable.d3004,
            R.drawable.d3005, R.drawable.d3006, R.drawable.d3007, R.drawable.d3008,
            R.drawable.d3009, R.drawable.d3010, R.drawable.d3011, R.drawable.d3012};

}
