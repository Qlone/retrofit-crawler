package com.qlone.craw.parse.element;

import java.util.List;
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

    @Override
    public List<RestultT> action(Elements elements) {
        List<RestultT> array = new ArrayList<>(elements.size());
        for(int i = skip; i < size && i < elements.size();i++){
            array.add(doAction(elements.get(i)));
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
            return element.text();
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

    public static class SelectAction extends AbstractElementAction<Elements> {

        private String childQuery;

        public SelectAction(int skip, int size,String childQuery) {
            super(skip, size);
            this.childQuery = childQuery;
        }

        @Override
        protected Elements doAction(Element element) {
            return element.select(this.childQuery);
        }
    }
}
