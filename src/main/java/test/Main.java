package test;

import com.alibaba.fastjson.JSONObject;
import com.github.qlone.Call;
import com.github.qlone.Response;
import com.github.qlone.RetrofitCrawler;
import test.baidu.Accept;
import test.baidu.Baidu;
import test.baidu.GuoneiBean;
import test.google.Google;
import test.google.TopStoyResp;

import java.io.IOException;

/**
 * @author heweinan
 * @date 2020-10-29 16:32
 */
public class Main {

    //div.tags-box > a
    public static void main(String[] args) throws IOException {
        RetrofitCrawler retrofitCrawler = new RetrofitCrawler.Builder()
                .baseUrl("https://news.google.com")
                .build();

        Google google = retrofitCrawler.create(Google.class);

        Call<TopStoyResp> topstories = google.topstories();
        Response<TopStoyResp> execute = topstories.execute();
        System.out.println(JSONObject.toJSONString(execute.body()));
    }
}
