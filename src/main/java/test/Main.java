package test;

import com.alibaba.fastjson.JSONObject;
import com.qlone.craw.Call;
import com.qlone.craw.Response;
import com.qlone.craw.RetrofitCrawler;

import java.io.IOException;

/**
 * @author heweinan
 * @date 2020-10-29 16:32
 */
public class Main {

    //div.tags-box > a
    public static void main(String[] args) throws IOException {
        RetrofitCrawler build = new RetrofitCrawler.Builder()
                .baseUrl("https://news.baidu.com")
                .build();
        Baidu baidu = build.create(Baidu.class);
        Call<Accept> html = baidu.html();
        Response<Accept> execute = html.execute();
        System.out.println(JSONObject.toJSONString(execute.body()));
    }
}
