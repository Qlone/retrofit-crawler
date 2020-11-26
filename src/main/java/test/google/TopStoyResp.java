package test.google;

import com.github.qlone.parse.annotation.Attr;
import com.github.qlone.parse.annotation.Select;
import com.github.qlone.parse.annotation.Text;

import java.util.List;

/**
 * @author heweinan
 * @date 2020-11-26 14:51
 */
public class TopStoyResp {

    @Select("div[jscontroller = d0DtYd]")
    private List<News> news;

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    public static class News{

        @Text("article h3 a")
        private String titile;

        @Attr(value = "div[jsname = gKDw6b] a",attr = "href")
        private String url;

        public String getTitile() {
            return titile;
        }

        public void setTitile(String titile) {
            this.titile = titile;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
