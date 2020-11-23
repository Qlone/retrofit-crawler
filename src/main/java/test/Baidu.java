package test;

import com.qlone.craw.Call;
import com.qlone.craw.http.POST;

/**
 * @author heweinan
 * @date 2020-10-29 16:32
        */
public interface Baidu {

    @POST("/")
    Call<Accept> html();
}
