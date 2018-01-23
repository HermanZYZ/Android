package com.example.administrator.threekingdomsdictionary;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by renardbebe on 2017/11/20.
 */

public class SaxPeopleParser implements PeopleParser {
    @Override
    public List<People> parse(InputStream is) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();  //取得SAXParserFactory实例
        SAXParser parser = factory.newSAXParser();                  //从factory获取SAXParser实例
        MyHandler handler = new MyHandler();                        //实例化自定义Handler
        parser.parse(is, handler);                                  //根据自定义Handler规则解析输入流
        return handler.getPeoples();
    }

    @Override
    public String serialize(List<People> peoples) throws Exception {
        SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();//取得SAXTransformerFactory实例
        TransformerHandler handler = factory.newTransformerHandler();           //从factory获取TransformerHandler实例
        Transformer transformer = handler.getTransformer();                     //从handler获取Transformer实例
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");            // 设置输出采用的编码方式
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");                // 是否自动添加额外的空白
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");   // 是否忽略XML声明

        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);
        handler.setResult(result);

        String uri = "";    //代表命名空间的URI 当URI无值时 须置为空字符串
        String localName = "";  //命名空间的本地名称(不包含前缀) 当没有进行命名空间处理时 须置为空字符串

        handler.startDocument();
        handler.startElement(uri, localName, "peoples", null);

        AttributesImpl attrs = new AttributesImpl();    //负责存放元素的属性信息
        char[] ch = null;
        for (People people : peoples) {
            attrs.clear();  //清空属性列表
            attrs.addAttribute(uri, localName, "id", "string", String.valueOf(people.getId()));//添加一个名为id的属性(type影响不大,这里设为string)
            handler.startElement(uri, localName, "people", attrs);    //开始一个people元素 关联上面设定的id属性

            handler.startElement(uri, localName, "name", null); //开始一个name元素 没有属性
            ch = String.valueOf(people.getName()).toCharArray();
            handler.characters(ch, 0, ch.length);   //设置name元素的文本节点
            handler.endElement(uri, localName, "name");

            handler.startElement(uri, localName, "gender", null);
            ch = String.valueOf(people.getGender()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "gender");

            handler.startElement(uri, localName, "subname", null);
            ch = String.valueOf(people.getSubname()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "subname");

            handler.startElement(uri, localName, "bornPlace", null);
            ch = String.valueOf(people.getBornPlace()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "bornPlace");

            handler.startElement(uri, localName, "deadDate", null);
            ch = String.valueOf(people.getDeadDate()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "deadDate");

            handler.startElement(uri, localName, "info", null);
            ch = String.valueOf(people.getInfo()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "info");

            handler.startElement(uri, localName, "bornDate", null);
            ch = String.valueOf(people.getBornDate()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "bornDate");

            handler.startElement(uri, localName, "camp", null);
            ch = String.valueOf(people.getCamp()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "camp");

            handler.startElement(uri, localName, "image", null);
            ch = String.valueOf(people.getImage()).toCharArray();
            handler.characters(ch, 0, ch.length);
            handler.endElement(uri, localName, "image");

            handler.endElement(uri, localName, "people");
        }
        handler.endElement(uri, localName, "peoples");
        handler.endDocument();

        return writer.toString();
    }

    //需要重写DefaultHandler的方法
    private class MyHandler extends DefaultHandler {

        private List<People> peoples;
        private People people;
        private StringBuilder builder;

        //返回解析后得到的People对象集合
        public List<People> getPeoples() {
            return peoples;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            peoples = new ArrayList<People>();
            builder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (localName.equals("people")) {
                people = new People();
            }
            builder.setLength(0);   //将字符长度设置为0 以便重新开始读取元素内的字符节点
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            builder.append(ch, start, length);  //将读取的字符数组追加到builder中
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (localName.equals("id")) {
                people.setId(Integer.parseInt(builder.toString()));
            } else if (localName.equals("name")) {
                people.setName(builder.toString());
            } else if (localName.equals("gender")) {
                people.setGender(builder.toString());
            } else if (localName.equals("subname")) {
                people.setSubname(builder.toString());
            } else if (localName.equals("bornPlace")) {
                people.setBornPlace(builder.toString());
            } else if (localName.equals("deadDate")) {
                people.setDeadDate(builder.toString());
            } else if (localName.equals("info")) {
                people.setInfo(builder.toString());
            } else if (localName.equals("bornDate")) {
                people.setBornDate(builder.toString());
            } else if (localName.equals("camp")) {
                people.setCamp(builder.toString());
            } else if (localName.equals("image")) {
                people.setImage(builder.toString());
            } else if (localName.equals("people")) {
                peoples.add(people);
            }
        }
    }
}
