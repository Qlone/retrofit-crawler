package test;

import com.alibaba.fastjson.JSONObject;
import com.github.qlone.Call;
import com.github.qlone.Response;
import com.github.qlone.RetrofitCrawler;
import test.bean.Accept;
import test.bean.GuoneiBean;

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

        Call<Accept> index = baidu.html();
        Call<GuoneiBean> guonei = baidu.guonei("/guonei");

        Response<Accept> indexResp = index.execute();
        Response<GuoneiBean> guoneiResp = guonei.execute();
        System.out.println(JSONObject.toJSONString(indexResp.body()));
        System.out.println(JSONObject.toJSONString(guoneiResp.body()));
    }
}
