package com.qlone.craw.parse;

import org.jsoup.nodes.Element;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author heweinan
 * @date 2020-10-29 17:32
 */
public class DocumentObject {
    private final static JsoupParser jsoupParser = new JsoupParser();

    public static <T> T parseObject(Element element, Type clazz) throws InstantiationException, IllegalAccessException {
        return jsoupParser.parse(element,clazz);
    }
}
