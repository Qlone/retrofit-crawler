# retrofit-crawer
help you get a html like json

----
### Maven
```
<dependency>
  <groupId>com.github.qlone</groupId>
  <artifactId>retrofit-crawler</artifactId>
  <version>1.0.0</version>
</dependency>
```
### Gradle
```
implementation 'com.github.qlone:retrofit-crawler:1.0.0'
```

### Make a interface

First, you can create a interface like retrofit and add some rules like json.

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
### Crawling web data
Create a retrofitCrawler in order to init our interface
```java
RetrofitCrawler retrofitCrawler = new RetrofitCrawler.Builder()
                .baseUrl("https://news.google.com")
                .build();
Google google = retrofitCrawler.create(Google.class);
```

Now, you can use google.class to get some data. You can execute sync or async.
```java
 Call<TopStoyResp> topstories = google.topstories();
 Response<TopStoyResp> execute = topstories.execute();
 TopStoyResp resp = execute.body();
```


### Issuses
Report issues to hewinana@gmail.com or [github issues](https://github.com/Qlone/retrofit-crawler/issues)
