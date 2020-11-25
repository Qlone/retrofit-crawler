package test;

import com.qlone.craw.Call;
import com.qlone.craw.http.POST;
import com.qlone.craw.http.RelativeUrl;
import test.bean.Accept;
import test.bean.GuoneiBean;

/**
 * @author heweinan
 * @date 2020-10-29 16:32
        */
public interface Baidu {

    @POST("/")
    Call<Accept> html();

    @POST()
    Call<GuoneiBean> guonei(@RelativeUrl String relative);
}
