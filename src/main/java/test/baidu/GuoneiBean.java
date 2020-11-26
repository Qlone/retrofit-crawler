package test.baidu;

import com.github.qlone.parse.annotation.Text;
import java.util.List;

/**
 * @author heweinan
 * @date 2020-11-25 15:33
 */
public class GuoneiBean {

    @Text("div.b-left li")
    private List<String> title;

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }
}
