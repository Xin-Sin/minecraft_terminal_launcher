package top.xinsin.http;

import cn.hutool.http.HttpUtil;

import java.util.Map;

/**
 * Created On 8/3/22 9:08 PM
 *
 * @author xinsin
 * @version 1.0.0
 */
public class HttpRequest {
    public static String get(String url) {

        return null;
    }
    public static String post(String url, Map params) {
        cn.hutool.http.HttpRequest post = cn.hutool.http.HttpRequest.post(url);
        post.form(params);
        post.header("Content-Type","application/x-www-form-urlencoded");
        return post.execute().body();
    }
}