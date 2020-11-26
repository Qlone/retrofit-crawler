# retrofit-crawer
help you get a html like json

----

####Make a interface

First,we will create a interface like retrofit,and add some rules like json

```java
public interface Google {

    @POST("/topstories")
    Call<TopStoyResp> topstories();
    
}

public class TopStoyResp {

    @Select("div[jscontroller = d0DtYd]")
    private List<News> news;

    public static class News{

        @Text("article h3 a")
        private String titile;

        @Attr(value = "div[jsname = gKDw6b] a",attr = "href")
        private String url;
    }
}
```
####Crawling web data
create a retrofitCrawler,in order to init our interface
```java
RetrofitCrawler retrofitCrawler = new RetrofitCrawler.Builder()
                .baseUrl("https://news.google.com")
                .build();
Google google = retrofitCrawler.create(Google.class);
```

now, you can use google.class to get some data.you can execute sync or async.
```java
 Call<TopStoyResp> topstories = google.topstories();
 Response<TopStoyResp> execute = topstories.execute();
 TopStoyResp resp = execute.body();
```


####Issuses
Report issues to hewinana@gmail.com or [github issues](https://github.com/Qlone/retrofit-crawler/issues)