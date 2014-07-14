package kr.kdev.dg1s.biowiki;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


public class Constants {

    //잎차례와 겹잎
    //어긋나기, 마주나기, 돌려나기
    public static String[] simpleAlternateLeaves = {};
    public static String[] simpleOppositeLeaves = {
            "한삼덩굴", "쥐송이풀", "큰땅빈대", "향유(노야기)", "돼지풀",
            "큰금계국", "코스모스", "만수국아재비", "털별꽃아재비", "뚱딴지(돼지감자)",
            "석류풀", "쇠비름", "벼룩이자리", "점나도나물", "벼룩나물",
            "잣나물", "좀부처꽃", "마디꽃" };
    public static String[] verticillate = {};

    //장상복엽
    //3출엽, 5출엽, 2회3출엽, 3회3출엽
    public static String[] trifoliolateLeaf = {
            "돌콩", "둥근매듭풀", "매듭풀", "전동싸리", "붉은토끼풀"
            , "토끼풀", "새팥", "괭이밥", "탱자나무" };
    public static String[] pentafoliolateLeaf = {};
    public static String[] viternateLeaf = {};
    public static String[] triternateLeaf = {};

    //우상복엽
    //기수1쌍우상복엽, 기수1회우상복엽, 우수1회우상복엽, 기수2회우상복엽, 우수2회우상복엽(단어 끝 'Leaf' 생략)
    public static String[] oddPinnateUnijugate = {};
    public static String[] oddPinnate = {
            "벌노랑이", "유럽전호", "만수국아재비", "황새냉이" };
    public static String[] evenPinnate = {
            "자귀풀", "얼치기완두", "가는살갈퀴"};
    public static String[] oddBipinnate = {
            "벌사상자", "돼지풀", "코스모스"};
    public static String[] evenBipinnate = {};

    //엽형
    //침형, 선형, 피침형, 도피침형, 심장형, 신장형, 원형, 타원형, 장타원형,
    public static String[] acicular = {"전동싸리" };
    public static String[] linear = {
            "재쑥", "여우주머니", "망초", "큰금계국", "코스모스",
            "만수국아재비", "가는쑥부쟁이", "쑥부쟁이", "족제비쑥", "쇠서나물(모련채)",
            "개밀", "메귀리", "참새귀리", "오리새", "바랭이",
            "왕바랭이", "그령", "비노리", "쥐보리", "미국개기장",
            "새포아풀", "금강아지풀", "강아지풀(가라지)", "수강아지풀(가라지조)", "쥐꼬리새풀",
            "길뚝사초", "괭이사초", "다닥냉이", "좀부처꽃"};
    public static String[] lanceolate = {
            "미국쑥부쟁이", "개망초", "큰금계국", "만수국아재비", "왕고들빼기",
            "가시상치(가시상추)", "쇠서나물(모련채)", "뽀리뱅이", "닭의장풀", "개밀",
            "메귀리", "참새귀리", "기생여뀌", "봄여뀌", "갓",
            "유채(평지)", "냉이", "황새냉이", "개갓냉이", "깨풀",
            "여뀌바늘"};
    public static String[] oblancolate = {
            "마디풀", "떡쑥(꽃다대)", "지칭개", "가는쑥부쟁이", "석류풀",
            "다닥냉이" };
    public static String[] cordate = {"어저귀"};
    public static String[] reniform = {"며느리밑씻개(사광이아재비)"};
    public static String[] orbicular = {"토끼풀", "괭이밥", "마디꽃"};
    public static String[] elliptical = {
            "붉은토끼풀", "수까치개", "향유(노야기)", "질경이", "쑥",
            "고들빼기"};
    public static String[] oblong = {
            "소리쟁이", "돌콩", "매듭풀", "전동싸리", "가는살갈퀴",
            "얼치기완두", "큰땅빈대", "겹달맞이꽃", "구기자나무", "조뱅이",
            "쇠비름", "벼룩나물", "꽃다지", "자귀풀"};
    // 광타원형, 난형, 도란형, 삼각형, 극형, 전형, 민들레형, 주걱형
    public static String[] oval = {"까마중(가마중)", "벼룩이자리"};
    public static String[] ovate = {
            "명아주(도토라지)", "개비름", "털비름", "뱀딸기", "새팥",
            "개망초", "털별꽃아재비", "큰방가지똥", "큰도꼬마리", "고들빼기",
            "뽕모시풀", "벼룩이자리", "점나도나물", "잣나물", "황새냉이",
            "깨풀"};
    public static String[] obovate = {"둥근매듭풀", "벌노랑이", "쇠비름", "말냉이", "마디꽃"};
    public static String[] deltoid = {};
    public static String[] hastate = {"애기메꽃"};
    public static String[] sagittate = {"며느리배꼽(사광이풀)", "흰제비꽃"};
    public static String[] runcinate = {"서양민들레"};
    public static String[] spatulate = {"망초", "떡쑥(꽃다대)", "이고들빼기", "꽃다지"};

    //엽선
    //점첨두, 예두, 급첨두, 예철두, 둔두, 원두, 요두, 평두, 미두
    public static String[] acuminate = {
            "개여뀌", "미국자리공", "새팥", "수까치개", "어저귀",
            "겹달맞이꽃", "애기메꽃", "향유(노야기)", "까마중(가마중)", "뚱딴지(돼지감자)",
            "닭의장풀", "흰여뀌", "잣나물(쇠별꽃)" };
    public static String[] acute = {
            "마디풀", "소리쟁이", "털비름", "벌노랑이", "붉은토끼풀",
            "큰땅빈대", "여우주머니", "흰제비꽃", "질경이(배짱이)", "조뱅이",
            "개망초", "망초", "떡쑥", "큰금계국", "코스모스",
            "만수국아재비", "가는쑥부쟁이", "쑥부쟁이", "족제비쑥", "쇠서나물(모련채)",
            "큰방가지똥", "서양민들레", "큰도꼬마리", "이고들빼기", "고들빼기",
            "뽀리뱅이", "개밀", "메귀리", "참새귀리", "오리새",
            "바랭이", "왕바랭이", "그령", "비노리", "쥐보리",
            "미국개기장", "금강아지풀", "강아지풀(가라지)", "수강아지풀(가라지조)", "쥐꼬리새풀",
            "길뚝사초", "괭이사초", "뽕모시풀", "기생여뀌", "봄여뀌",
            "석류풀", "점나도나물", "벼룩나물", "갓", "유채(평지)",
            "꽃다지", "다닥냉이", "개갓냉이", "속속이풀", "말냉이",
            "깨풀", "좀부처꽃", "마디꽃", "여뀌바늘"  };
    public static String[] mucronate = {"얼치기완두"};
    public static String[] cuspidate = {};
    public static String[] obtuse = {"좀명아주", "애기똥풀", "가는쑥부쟁이", "새포아풀"};
    public static String[] rounded = {"구기자나무", "쇠비름", "자귀풀"};
    public static String[] emarginate = {"개비름", "둥근매듭풀", "토끼풀", "가는살갈퀴", "괭이밥"};
    public static String[] truncate = {};
    public static String[] caudate = {"돼지풀", "쑥", "털별꽃아재비", "왕고들빼기", "가시상치(가시상추)"};

    //잎바닥, 엽저(정보가 거의 없음, 삭제 요망)
    //유저, 설저, 둔저, 왜저, 예저, 순저, 심장저, 원저, 관천저, 평저, 이저, 극저
    public static String[] atteuate = {"향유(노야기)", "까마중(가마중)", "다닥냉이"};
    public static String[] cuneate = {};
    public static String[] obtuse2 = {};
    public static String[] oblique = {};
    public static String[] acute2 = {"겹달맞이꽃", "냉이", "황새냉이"};
    public static String[] peltate = {};
    public static String[] cordate2 = {"어저귀", "애기메꽃", "나팔꽃", "잣나물(쇠별꽃)"};
    public static String[] rounded2 = {};
    public static String[] perpoliate = {"며느리배꼽(사광이풀)"};
    public static String[] truncate2 = {};
    public static String[] auriculate = {"가시상치(가시상추)", "이고들빼기", "고들빼기", "유채(평지)"};
    public static String[] hastate2 = {};

    //엽연
    //전연, 둔거치, 소둔거치, 예거치, 소예거치, 복거치, 치아상거치, 소치아상거치, 파형
    public static String[] entire = {
            "개여뀌", "마디풀", "족제비싸리", "둥근매듭풀", "매듭풀",
            "벌노랑이", "가는살갈퀴", "얼치기완두", "새팥", "괭이밥",
            "여우주머니", "애기메꽃", "구기자나무", "개망초", "망초",
            "떡쑥(꽃다대)", "큰금계국", "코스모스", "가는쑥부쟁이", "서양민들레",
            "닭의장풀", "개밀", "메귀리", "참새귀리", "오리새",
            "바랭이", "왕바랭이", "그령", "비노리", "쥐보리",
            "미국개기장", "새포아풀", "금강아지풀", "강아지풀(가라지)", "수강아지풀(가라지조)",
            "쥐꼬리새풀", "길뚝사초", "괭이사초", "흰여뀌", "기생여뀌",
            "봄여뀌", "석류풀", "쇠비름", "점나도나물", "벼룩나물",
            "유채(평지)", "자귀풀", "좀부처꽃", "마디꽃", "여뀌바늘" };
    public static String[] crenate = {
            "탱자나무", "수까치개", "어저귀", "개망초", "망초",
            "뽕모시풀", "유채(평지)", "냉이", "꽃다지", "개갓냉이",
            "속속이풀", "깨풀" };
    public static String[] crenulate = {"한삼덩굴", "흰제비꽃", "향유(노야기)", "뚱딴지(돼지감자)"};
    public static String[] serrate = {
            "명아주(도토라지)", "뱀딸기", "조뱅이", "털별꽃아재비", "큰방가지똥",
            "큰도꼬마리", "갓", "다닥냉이" };
    public static String[] serrulate = {"전동싸리", "토끼풀", "큰땅빈대", "만수국아재비", "쇠서나물(모련채)"};
    public static String[] biserrate = {};
    public static String[] dentate = {"이고들빼기", "고들빼기"};
    public static String[] denticulate = {"벼룩이자리"};
    public static String[] undulate = {
            "겹달맞이꽃", "까마중(가마중)", "질경이(배짱이)", "개쑥갓", "잣나물(쇠별꽃)"
            , "속속이풀"};
    //반곡, 역반곡, 편형(이 3개 삭제 요망), 천열, 중열, 전열, 우열, 장상열
    public static String[] revolute = {};
    public static String[] involute = {};
    public static String[] plane = {};
    public static String[] lobed = {"좀명아주"};
    public static String[] cleft = {"쑥", "쑥부쟁이", "개쑥갓", "뽀리뱅이", "황새냉이"};
    public static String[] parted = {
            "애기똥풀", "돼지풀", "지칭개", "왕고들빼기", "가시상치(가시상추)",
            "족제비쑥"};
    public static String[] pinnatifid = {"속속이풀"};
    public static String[] palmatifid = {"쥐손이풀", "미국쥐손이", "유럽전호", "나팔꽃", "쑥"};

    //자방위치(삭제 요망)
    //상위자방, 중위자방, 하위자방
    public static String[] superiorOvary = {"구기자나무", "질경이(배짱이)", "닭의장풀", "좀부처꽃", "마디꽃"};
    public static String[] halfInferiorOvery = {"쇠비름"};
    public static String[] inferiorOvery = {};

    //화형(자료가 빈약함)
    //방사상칭형, 폭상, 순형, 십자화형, 왕관형, 종형, 두형, 투구형, 칠면상
    public static String[] actinomorphic = {
            "괭이밥", "쥐손이풀", "흰제비꽃", "겹달맞이꽃", "쇠비름",
            "점나도나물", "벼룩나물", "잣나물(쇠별꽃)", "좀부처꽃", "마디꽃"};
    public static String[] rotate = {"벼룩이자리", "여뀌바늘"};
    public static String[] bilabiate = {"향유(노야기)"};
    public static String[] cruciform = {
            "갓", "유채(평지)", "냉이", "황새냉이", "꽃다지",
            "다닥냉이", "개갓냉이", "속속이풀", "말냉이"};
    public static String[] coronate = {"까마중(가마중)"};
    public static String[] campanulate = {"애기메꽃", "나팔꽃"};
    public static String[] funnel = {};
    public static String[] galeate = {};
    public static String[] gibbous = {};
    // 구개형, 가면상, 접형, 낭형, 분형, 호형, 관상, 설상
    public static String[] palate = {};
    public static String[] personate = {};
    public static String[] papillionaceous = {
            "돌콩", "둥근매듭풀", "벌노랑이", "전동싸리", "가는살갈퀴",
            "얼치기완두", "새팥", "자귀풀"};
    public static String[] saccate = {};
    public static String[] salver = {};
    public static String[] urceolate = {};
    public static String[] tubular = {"개쑥갓", "족제비쑥"};
    public static String[] ligulate = {
            "미국쑥부쟁이", "개망초", "망초", "큰금계국", "코스모스",
            "만수국아재비", "털별꽃아재비", "뚱딴지(돼지감자)", "가는쑥부쟁이", "쑥부쟁이",
            "왕고들빼기", "가시상치(가시상추)", "쇠서나물(모련채)", "큰방가지똥", "서양민들레",
            "이고들빼기", "고들빼기", "뽀리뱅이"};

    //꽃차례, 무한화서
   //수상화서, 총상화서, 산방화서, 산형화서, 원추화서, 선형화서, 취산화서, 두상화서, 육수화서, 미상화서, 배상화서
    public static String[] spike = {
            "개비름", "털비름", "향유(노야기)", "질경이(배짱이)", "돼지풀",
            "개밀", "바랭이", "왕바랭이", "길뚝사초", "괭이사초",
            "흰여뀌", "기생여뀌", "봄여뀌", "말냉이", "깨풀",
            "좀부처꽃", "마디꽃", "쥐보리"};
    public static String[] raceme = {
            "재쑥", "족제비싸리", "돌콩", "전동싸리", "새팥",
            "갓", "유채(평지)", "냉이", "황새냉이", "꽃다지",
            "다닥냉이", "개갓냉이", "속속이풀", "자귀풀"};
    public static String[] corymb = {
            "까마중(가마중)", "떡쑥(꽃다대)", "쇠서나물(모련채)", "개쑥갓", "이고들빼기",
            "고들빼기", "냉이", "냉이"};
    public static String[] umbel = {"애기똥풀", "유럽전호", "뽀리뱅이"};
    public static String[] panicle = {
            "명아주(도토라지)", "좀명아주", "쑥", "개망초", "망초",
            "왕고들빼기", "가시상치(가시상추)", "족제비쑥", "큰도꼬마리", "메귀리",
            "참새귀리", "오리새", "그령", "비노리", "미국개기장",
            "새포아풀", "금강아지풀", "강아지풀(가라지)", "수강아지풀(가라지조)", "쥐꼬리새풀"};
    public static String[] rhipidium = {};
    public static String[] cyme = {
            "뱀딸기", "벌사상자", "닭의장풀", "뽕모시풀", "석류풀",
            "벼룩이자리", "점나도나물", "벼룩나물", "잣나물(쇠별꽃)"};
    public static String[] cptitulum = {
            "애기메꽃", "나팔꽃", "큰금계국", "코스모스", "만수국아재비",
            "털별꽃아재비", "뚱딴지(돼지감자)", "지칭개", "가는쑥부쟁이", "쑥부쟁이",
            "서양민들레", "쇠비름", "여뀌바늘"};
    public static String[] spedix = {};
    public static String[] catkin = {};
    public static String[] cyathium = {"큰땅빈대"};

    //과실 모양
    //덩어리, 물, 알갱이, 여윈, 캡슐, 배, 뚜껑, 갈래, 분리, 꼬투리, 견과류, 날개
    // (한글이름으로, 뒤의 글자'열매' 뺐음)
    public static String[] aggregate = {"뱀딸기"};
    public static String[] berry = {"미국자리공", "탱자나무", "구기자나무", "까마중(가마중)"};
    public static String[] drupe = {};
    public static String[] achene = {
            "한삼덩굴", "개여뀌", "며느리배꼽(사광이풀)", "며느리밑씻개(사광이아재비)", "마디풀",
            "소리쟁이", "재쑥", "돼지풀", "쑥", "미국쑥부쟁이",
            "조뱅이", "개망초", "망초", "떡쑥(꽃다대)", "큰금계국",
            "코스모스", "뚱딴지(돼지감자)", "지칭개", "가는쑥부쟁이", "쑥부쟁이",
            "왕고들빼기", "가시상치(가시상추)", "족제비쑥", "쇠서나물(모련채)", "개쑥갓",
            "큰방가지똥", "서양민들레", "큰도꼬마리", "이고들빼기", "고들빼기",
            "뽀리뱅이", "길뚝사초", "괭이사초", "뽕모시풀", "흰여뀌",
            "기생여뀌", "봄여뀌", "만수국아재비", "털별꽃아재비"};
    public static String[] capsule = {
            "개미자리", "명아주(도토라지)", "좀명아주", "개비름", "털비름",
            "애기똥풀", "괭이밥", "쥐손이풀", "미국쥐손이", "큰땅빈대",
            "여우주머니", "수까치개", "흰제비꽃", "겹달맞이꽃", "나팔꽃",
            "질경이(배짱이)", "닭의장풀", "석류풀", "쇠비름", "벼룩이자리",
            "점나도나물", "벼룩나물", "잣나물(쇠별꽃)", "깨풀", "좀부처꽃",
            "마디꽃", "여뀌바늘"};
    public static String[] pome = {};
    public static String[] pyxis = {};
    public static String[] schizocarp = {};
    public static String[] loment = {};
    public static String[] legume = {
            "새콩", "돌콩", "둥근매듭풀", "매듭풀", "벌노랑이",
            "전동싸리", "붉은토끼풀", "토끼풀", "가는살갈퀴", "얼치기완두",
            "새팥", "꽃다지", "다닥냉이", "자귀풀",};
    public static String[] nut = {"족제비싸리"};
    public static String[] samara = {};
    //영과(벼 처럼 생긴 열매), 그림 없는데 식물사전에는 있네ㄷㄷ이를 어쩌나 명규보고 다시 그리라 할까;;
    public static String[] caryopsis = {
            "개밀", "메귀리", "참새귀리", "오리새", "바랭이",
            "왕바랭이", "그령", "비노리", "쥐보리", "미국개기장",
            "새포아풀", "금강아지풀", "강아지풀(가라지)", "수강아지풀(가라지조)", "쥐꼬리새풀"};

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

    public static String FILE_XML_CATEGORY = "categories.xml";

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
  //  public static int[] flowerIds3 = {

    //}
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
