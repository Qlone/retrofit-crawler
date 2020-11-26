package test.google;

import com.github.qlone.Call;
import com.github.qlone.http.GET;

/**
 * @author heweinan
 * @date 2020-11-26 14:51
 */
public interface Google {
    @GET("/topstories")
    Call<TopStoyResp> topstories();
}
