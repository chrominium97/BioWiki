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
    public static String[] flowerNames1 = {"머리모양꽃차례\n(두상화서)", "꼬리모양꽃차례\n(미상화서)", "잔모양꽃차례\n(배상화서)", "고른꽃차례(산방화서)",
            "우산모양꽃차례\n(산형화서)", "부채모양꽃차례\n(선형화서)", "이삭꽃차례\n(수상화서)", "고깔꽃차례\n(원추화서)", "살찐대꽃차례\n(육수화서)", "송이꽃차례\n(층상화서)",
            "고른우산살송이모양꽃차례\n(취산화서)", "고른우산살송이모양꽃차례2\n(취산화서)"
    };
    public static int[] flowerDrawable2 = {R.drawable.d1201, R.drawable.d1202, R.drawable.d1203};
    public static String[] flowerNames2 = {"상위자방", "중위자방", "하위자방"
    };
    public static int[] flowerDrawable3 = {R.drawable.d1301, R.drawable.d1302, R.drawable.d1303, R.drawable.d1304,
            R.drawable.d1305, R.drawable.d1306, R.drawable.d1307, R.drawable.d1308,
            R.drawable.d1309, R.drawable.d1310, R.drawable.d1311, R.drawable.d1312,
            R.drawable.d1313, R.drawable.d1314, R.drawable.d1315, R.drawable.d1316,
            R.drawable.d1317};
    public static String[] flowerNames3 = {"탈모양\n(가면상)", "대롱모양\n(관상)", "덮개모양\n(구개형)", "복주머니모양\n(낭형)", "깔대기모양\n(두형)", "방사상칭형",
            "화분모양\n(분형)", "혀모양\n(설상)", "입술모양\n(순형)", "십자모양\n(십자화형)",
            "왕관모양\n(왕관형)", "나비모양\n(접형)", "종모양\n(종형)", "돌출모양\n(철면상)",
            "헬멧모양\n(투구형)", "바퀴모양\n(폭상)", "단지모양\n(호형)"
    };
    public static int[] leafDrawable1 = {R.drawable.d2101, R.drawable.d2102, R.drawable.d2103, R.drawable.d2104,
            R.drawable.d2105, R.drawable.d2106, R.drawable.d2107, R.drawable.d2108,
            R.drawable.d2109};
    public static String[] leafNames1 = {"급하게뾰족한잎끝\n(급첨두)", "무딘잎끝\n(둔두)", "꼬리모양잎끝\n(미두)",
            "날카로운잎끝\n(예두)", "날카롭게뾰족한잎끝\n(예첨두)", "오목한잎끝\n(요두)",
            "둥근잎끝\n(원두)", "점점뾰족한잎끝\n(점첨두)", "편평한잎끝\n(평두)"
    };
    public static int[] leafDrawable2 = {R.drawable.d2201, R.drawable.d2202, R.drawable.d2203, R.drawable.d2204,
            R.drawable.d2205, R.drawable.d2206, R.drawable.d2207, R.drawable.d2208,
            R.drawable.d2209, R.drawable.d2210, R.drawable.d2211, R.drawable.d2212,
            R.drawable.d2213, R.drawable.d2214, R.drawable.d2215, R.drawable.d2216,
            R.drawable.d2217};
    public static String[] leafNames2 = {"매끈한모양\n(전연)", "둔한톱니\n(둔거치)", "작은둔한톱니\n(소둔거치)", "뾰족한톱니\n(예거치)", "작은뾰족한톱니\n(소예거치)",
            "뾰족한겹톱니\n(복거치)", "이빨형톱니\n(치아상거치)", "작은이빨형톱니\n(소치아상거치)", "물결형\n(파형)", "반곡", "역반곡", "편형",
            "얕게패인모양\n(천열)", "중간정도패인모양\n(중열)", "깊게패인모양\n(전열)", "날개모양\n(우열)", "손바닥모양\n(장상열)"
    };
    public static int[] leafDrawable3 = {R.drawable.d2301, R.drawable.d2302, R.drawable.d2303, R.drawable.d2304,
            R.drawable.d2305, R.drawable.d2306, R.drawable.d2307, R.drawable.d2308,
            R.drawable.d2309, R.drawable.d2310, R.drawable.d2311, R.drawable.d2312};
    public static String[] leafNames3 = {"통과형잎바닥\n(관천저)", "창날모양잎바닥\n(극저)", "무딘잎바닥\n(둔저)", "쐐기모양잎바닥\n(설저)",
            "방패모양잎바닥\n(순저)", "심장형잎바닥\n(심장저)", "예리한잎바닥\n(예저)", "비뚠잎바닥\n(왜저)", "둥근잎바닥\n(원저)", "흐르는잎바닥\n(유저)", "귀모양잎바닥\n(이저)", "편평한잎바닥\n(평저)"
    };
    public static int[] leafDrawable4 = {R.drawable.d2401, R.drawable.d2402, R.drawable.d2403, R.drawable.d2404,
            R.drawable.d2405, R.drawable.d2406, R.drawable.d2407, R.drawable.d2408,
            R.drawable.d2409, R.drawable.d2410, R.drawable.d2411, R.drawable.d2412,
            R.drawable.d2413, R.drawable.d2414, R.drawable.d2415, R.drawable.d2416,
            R.drawable.d2417};
    public static String[] leafNames4 = {"넓은타원모양\n(광타원형)", "창날모양\n(극형)", "계란모양\n(난형)", "거꿀계란모양\n(도란형)",
            "거꿀창끝모양\n(도피침형)", "민들레잎모양\n(민들레형)", "삼각모양\n(삼각형)", "줄모양\n(선형)",
            "콩팥모양\n(신장형)", "심장모양\n(심장형)", "둥근모양\n(원형)", "긴타원모양\n(장타원형)",
            "활촉모양\n(전형)", "주걱모양\n(주걱형)", "바늘모양\n(침형)", "타원모양\n(타원형)", "창끝모양\n(피침형)"
    };
    public static int[] leafDrawable5 = {R.drawable.d2501, R.drawable.d2502, R.drawable.d2503, R.drawable.d2504,
            R.drawable.d2505};
    public static String[] leafNames5 = {"홀수한쌍깃모양겹잎\n(기수1쌍우상복엽)", "홀수한번깃모양겹잎\n(기수1회우상복엽)", "홀수두번깃모양겹잎\n(기수2회우상복엽)",
            "짝수한번깃모양겹잎\n(우수1회우상복엽)", "짝수두번깃모양겹잎\n(우수2회우상복엽)"
    };
    public static int[] leafDrawable6 = {R.drawable.d2601, R.drawable.d2602, R.drawable.d2603};
    public static String[] leafNames6 = {"돌려나기\n(윤생)", "마주나기\n(대생)", "어긋나기\n(호생)"
    };
    public static int[] leafDrawable7 = {R.drawable.d2701, R.drawable.d2702, R.drawable.d2703, R.drawable.d2704};
    public static String[] leafNames7 = {"두번세장잎\n(2회3출엽)", "세장잎\n(3출엽)", "세번세장잎\n(3회3출엽)", "다섯장잎\n(5출엽)"
    };
    public static int[] fruitDrawable = {R.drawable.d3001, R.drawable.d3002, R.drawable.d3003, R.drawable.d3004,
            R.drawable.d3005, R.drawable.d3006, R.drawable.d3007, R.drawable.d3008,
            R.drawable.d3009, R.drawable.d3010, R.drawable.d3011, R.drawable.d3012};
    public static String[] fruitNames = {"뚜껑열매\n(개과)", "견과류열매\n(견과)", "분리열매\n(분과)", "갈래열매\n(분열과)",
            "캡슐열매\n(삭과)", "여윈열매\n(수과)", "배열매\n(이과)", "날개열매\n(익과)",
            "물열매\n(장과/액과)", "덩어리열매\n(취과)", "알갱이열매\n(핵과)", "꼬투리열매\n(협과)"
    };

}
