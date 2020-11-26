package test.bean;

import com.github.qlone.parse.annotation.Attr;
import com.github.qlone.parse.annotation.Text;
import java.util.List;

/**
 * @author heweinan
 * @date 2020-11-24 15:00
 */
public class News {

    @Text("a")
    private List<String> title;

    @Attr(value = "a",attr = "href")
    private List<String> url;

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }
}
