package app.zhengbang.teme;

/**
 * Created by victor on 2014/11/6 0006.
 */
public class AppConstants {
    /**
     * EventBus 的请求码, 用一次++
     */
    public static int requestCode = 100029;
    public static int _requestCode = 20061;

    public static final String ImagePagerPage = "ImagePagerPage";
    public static final int PromptRequestCode = 1001;
    public static String FILE_SCHEME = "file:///";
    public static final int photo_code = 11101;
    public static final int carmer_code = 11102;
    public static String CacheName= "imgCache";
    public static String cityName = "北京";
    public static double distance_per_half_m = 150;
    public static int SelectLocationByMapPage_requestCode = 13;
    public static String YouKuKey="b45e15a061d7e614";

    public static String TemplateEngine = "TemplateEngine";
    public static String HOST = "http://app.twotiger.com";
    public static String ENCODE = "UTF-8";
    public static String PSIZE = "10";
    public static String token = "token";
    public static String pageIndex = "pageIndex";
    public static String URL_BASE = "http://192.168.120.123:8091/";
    public static String login_state = "login_state";
    public static String Prompt = "Prompt";
    public static String VideoURL = "https://openapi.youku.com/v2/searches/video/by_keyword.json";
    public static int db_version = 1;
    public static int current_page_take_pic = 0;
//    public static String TeMeHost="http://123.57.253.189/teme/index.php";
    public static String TeMeHost="http://yitobe.com/teme/index.php";
//    public static String TeMeImageURL="http://123.57.253.189/teme/post_photo/";
//    http://7xip32.com1.z0.glb.clouddn.com/icon1429842184390.png?imageView2/1/w/300/h/300
    public static String TeMeImageURL="http://yitobe.com/teme/post_photo/";
    public static String QiNiuImageURL="http://7xip32.com1.z0.glb.clouddn.com/";
    public static String QiNiuThumbMethod="?imageView2/1/w/300/h/300";
    public static String TeMeRoleName_default="暂无个人签名";
    /**
     * app相关
     */
    public static int LOCATION_START = 1;
    public static int LOCATION_END = 2;
    public static int USER_ON_BUS = 12;
    public static int MODIFY_RemindS_STATION = 11;
    
    /**
     * requestCode
     */
    public static int DEAL_PublishActivity_Back = 1;
    public static int MAX_PUBLISH_SIZE = 6;
    public static int imagePagerPage_AutoScroll_requestCode = 3;
    public static String OtherEngine = "OtherEngine";
    public static final int video_code = 4;
    public static final String   LOGIN_ACCOUNT_KEY= "phone";
    public static final String   LOGIN_PSD_KEY= "password";
}
