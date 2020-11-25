package com.qlone.craw.parse.element;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.qlone.craw.parse.annotation.Select;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * @author heweinan
 * @date 2020-11-13 10:59
 */
public abstract class AbstractElementAction<RestultT> implements ElementAction<RestultT> {

    private int skip;
    private int size;

    public AbstractElementAction(int skip, int size) {
        this.skip = skip;
        this.size = size;
    }

    /**
     *
     * @param elements
     * @return
     */
    @Override
    public List<RestultT> action(Elements elements) {
        List<RestultT> array = new ArrayList<>(elements.size());

        for (int i = skip; i < size && i < elements.size(); i++) {
            RestultT restultT = doAction(elements.get(i));
            array.add(restultT);
        }

        return array;
    }

    protected abstract RestultT doAction(Element element);


    public static class TextAction extends AbstractElementAction<String> {

        public TextAction(int skip, int size) {
            super(skip, size);
        }

        @Override
        protected String doAction(Element element) {
            return element.text();
        }
    }

    public static class HtmlAction extends AbstractElementAction<String> {

        public HtmlAction(int skip, int size) {
            super(skip, size);
        }

        @Override
        protected String doAction(Element element) {
            return element.html();
        }
    }

    public static class AttrAction extends AbstractElementAction<String> {

        private String attr;

        public AttrAction(int skip, int size,String attr) {
            super(skip, size);
            this.attr = attr;
        }

        @Override
        protected String doAction(Element element) {
            return element.attr(this.attr);
        }
    }

    public static class OuterHtmlAction extends AbstractElementAction<String> {

        public OuterHtmlAction(int skip, int size) {
            super(skip, size);
        }

        @Override
        protected String doAction(Element element) {
            return element.outerHtml();
        }
    }

    public static class DataAction extends AbstractElementAction<String> {

        public DataAction(int skip, int size) {
            super(skip, size);
        }

        @Override
        protected String doAction(Element element) {
            return element.data();
        }
    }

    public static class SelectAction extends AbstractElementAction<Element> {


        public SelectAction(int skip, int size) {
            super(skip, size);
        }

        @Override
        protected Element doAction(Element element) {
            return element;
        }
    }
}
