package test.bean;

import com.qlone.craw.parse.annotation.*;

import java.util.List;

/**
 * @author heweinan
 * @date 2020-11-23 11:06
 */

public class Accept {
//    @Attr(value = "div#pane-news li a",attr = "href")

    @Select("div#pane-news li")
    private List<News> news;

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

}
