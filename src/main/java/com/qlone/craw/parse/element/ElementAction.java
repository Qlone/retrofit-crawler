package com.qlone.craw.parse.element;


import java.util.List;
import org.jsoup.select.Elements;

/**
 * @author heweinan
 * @date 2020-11-12 11:52
 */
public interface ElementAction<RestultT> {
    List<RestultT> action(Elements elements);

}
