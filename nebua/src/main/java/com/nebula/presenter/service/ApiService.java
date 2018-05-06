package com.nebula.presenter.service;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by stary on 2018/3/18.
 * <p>
 * ApiService说明：接口写完，但是还没有测试。
 * 注意1：retrofit2.0后：BaseUrl要以/结尾；@GET 等请求不要以/开头；@Url: 可以定义完整url，不要以 / 开头。
 * 注意2：请求的URL可以根据函数参数动态更新。一个可替换的区块为用 {  和  } 包围的字符串，
 * 而函数参数必需用  @Path 注解表明，并且注解的参数为同样的字符串
 */

public interface ApiService {
//    /*retrofit请求注解的path*/
////    String RetPath = "/uploads/allimg/160726/7730-160H6114H5.jpg";
//    public static final String path = "{path}";//请求的路径。
//    public static final String method = "{method}";//请求网络的方法。
//
//    // 访问的API是：https://api.github.com/users/{user}/repos
//    // 在发起请求时， {user} 会被替换为方法的第一个参数 user（被@Path注解作用）
//

    public static String Base_URL = "https://github.com/qydq";
//    /**
//     * method 表示请的方法，不区分大小写
//     * path表示路径 ,如users/{user}。
//     * hasBody表示是否有请求体
//     */
//    @HTTP(method = method, path = path, hasBody = false)
//    Call<ResponseBody> noBadyhttp(
//            @Path("method") String method,
//            @Path("path") String path);
//
//    @HTTP(method = method, path = path, hasBody = true)
//    Call<ResponseBody> bodyhttp(
//            @Path("method") String method,
//            @Path("path") String path);
//
//    @HTTP(method = "get", path = path, hasBody = true)
//    Call<ResponseBody> getBodyHttp(
//            @Path("path") String path);
//
//    @HTTP(method = "get", path = path, hasBody = false)
//    Call<ResponseBody> getHttp(
//            @Path("path") String path);
//
//    @HTTP(method = "get", path = path, hasBody = false)
//    Call<ResponseBody> postHttp(
//            @Path("path") String path);
//
//    /**
//     * 1.Retrofit查询
//     * 1.baseUrl+path?uid=uid
//     */
//    @GET(path)
//    <T> Observable<ResponseBody> queryGet(
//            @Path("path") String path,
//            @Query("uid") String uid);
//
//    @POST(path)
//    <T> Observable<ResponseBody> queryPost(
//            @Path("path") String path,
//            @Query("uid") String uid);
//
//    @GET(path)
//    <T> Observable<ResponseBody> queryGet(
//            @Path("path") String path,
//            @QueryMap Map<String, String> maps);
//
//    @POST(path)
//    <T> Observable<ResponseBody> queryPost(
//            @Path("path") String path,
//            @QueryMap Map<String, String> maps);
//
//    /**
//     * 2.Retrofit对一个简单
//     * 的html页面进行网络请求
//     */
//    @GET
//    <T> Call<ResponseBody> htmlGet(@Url String requestURL);
//
//    @POST
//    <T> Call<ResponseBody> htmlPost(@Url String requestURL);
//
//    /**
//     * 3.正常的网络请求
//     */
//    @GET
//    <T> Call<ResponseBody> get(@Url String requestURL);
//
//    @GET(path)
//    <T> Call<ResponseBody> pathGet(@Path("path") String path);
//
//    @POST
//    <T> Call<ResponseBody> post(@Url String requestURL);
//
//    @POST(path)
//    <T> Call<ResponseBody> pathPost(@Path("path") String path);
//
//    //
////
////    @Multipart
////    @POST("{url}")
////    Observable<ResponseBody> upLoadFile(
////            @Path("url") String url,
////            @Part("image\\\\"; filename=\\"image.jpg") RequestBody avatar);
//
//    /**
//     * 4.上传文件。
//     */
//    @POST(path)
//    <T> Observable<ResponseBody> uploadFiles(
//            @Path("path") String path,
//            @Path("headers") Map<String, String> headers,
//            @Part("filename") String filename,
//            @PartMap() Map<String, RequestBody> maps);
//
//    <T> Observable<ResponseBody> uploadFiles(
//            @Path("headers") Map<String, String> headers,
//            @Part("filename") String filename,
//            @PartMap() Map<String, RequestBody> maps);
//
//    /**
//     * 5.下载文件。
//     */
//    @Streaming
//    @GET
//    <T> Observable<ResponseBody> downloadFile(@Url String fileUrl);
//
//    //option 1: a resource relative to your base URL
//    @Streaming
//    @GET(path)
//    <T> Call<ResponseBody> downloadFileWithFixedRepos();
//
//    //option 2:using a dynamic URL
//    @Streaming
//    @GET
//    <T> Call<ResponseBody> downloadFileDynamicRepos(@Url String IMAGE_URL);
//
//    @Streaming
//    @GET
//    <T> Call<ResponseBody> downloadFileDynamicRepos(@Url String[] IMAGE_URLS);
}
