package com.nebula;

/**
 * brief:Integrate an系列nebula框架中的常量统一管理，完成AR20180115666
 * 该类参考：Mainfest.class实现。
 * <br> author：晴雨【qy】
 * <br> email：staryumou@163.com
 * <br> Created by sun on 2017/12/26.
 * <br> update date information：2018/01/15
 * <br> website：https://qydq.github.io
 * <br> Copyrigth(c),2018,孙顺涛,inasst.com
 * <br> version 2.0
 */

public class AnConstants {
    public static String AnDIR = "an";//主目录文件名

    public static String AnDataBase = "an.db";//数据库文件名
    public static String AnMovie = "movie";//视频文件目录名
    public static String AnPicture = "picture";//存放图片文件名
    public static String AnMusic = "melody";//存放音乐文件名
    public static String AnDownload = "download";//下载文件目录名
    public static String AnShps = "shps";//SharedPreferences's name
    public static String AnPublic = "public";//公共文件夹名
    public static String AnApk = "apk";//存放apk文件的目录名
    public static String AnLog = "log-";//公共log文件目录名
    public static String AnLog_ALL = "--qydq--";//过滤所有日志的标志

    /**
     * 这些应该叫AnConfigData吧。
     */
    public static String UTF8_ENCODE = "UTF-8";//UTF8编码
    public static int time = 5;//请求超时时间
    public static int time_ad = 10;//下载广告请求超时时间
    public static int time_download = 10;//下载文件请求超时时间
    public static int delay_time = 10;//通用请求超时超时时间

    /**
     * retrofit使用参考下面博客。
     * http://blog.csdn.net/carson_ho/article/details/73732076
     * Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
     * 如果接口里的url是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
     * Exg : http://fy.iciba.com/ajax.php?a=fy&f=auto&t=auto&w=hello%20world
     * 注解上的url为path = ajax.php?a=fy&f=auto&t=auto&w=hello%20world;
     * baseUrl=http://fy.iciba.com/
     * 如果path是一个完整网址，则retofit的baseUrl可以忽略
     */
    private static String retrofitPath = "";
    private static String retrofitBaseUrl = URL.BASE_URL;

    public static String getRetrofitPath() {
        return retrofitPath;
    }

    public static void setRetrofitPath(String retrofitPath) {
        AnConstants.retrofitPath = retrofitPath;
    }

    public static String getRetrofitBaseUrl() {
        return retrofitBaseUrl;
    }

    public static void setRetrofitBaseUrl(String retrofitBaseUrl) {
        AnConstants.retrofitBaseUrl = retrofitBaseUrl;
    }

    public static final String getAnnotationPath = getRetrofitPath();

    /*
            * 网络请求地址*/
    public static final class URL {
        public static String BASE_URL = "https://github.com/qydq/Integrate/";
        public static String Extra_requestURl = "requestURl";
        public static String Extra_requestfileName = "fileName";
        public static String Extra_requestdirpath = "apkDirPath";
        public static final String MICROPHONE = "android.permission-group.MICROPHONE";
        public static final String PHONE = "android.permission-group.PHONE";
        public static final String getAnnotationPath = getRetrofitPath();

    }

    /*
*
* 统一管理存储键值对的键*/
    public static final class KEY {
        public static String BASE_KEY = "https://github.com/qydq/Integrate/";
        public static String Extra_daynight = "day_night_mode";
        public static String Extra_intoAppFirst = "intoAppFirst";
        public static String Extra_splashClickBox = "checkbox";
        public static String Extra_apkIntallPath = "apkintallpath";

    }

    public static String suffixPNG = ".PNG";
    public static String suffixJPEG = ".JPEG";
    public static String suffixJPG = ".jpg";


}
