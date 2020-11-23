package test;

import com.qlone.craw.parse.annotation.Data;
import com.qlone.craw.parse.annotation.Text;
import java.util.List;

/**
 * @author heweinan
 * @date 2020-11-23 11:06
 */

public class Accept {
    @Text(value = "div#pane-news li ")
    private List<String> text;

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }
}
