package com.example.administrator.threekingdomsdictionary;

import java.io.InputStream;
import java.util.List;

/**
 * Created by renardbebe on 2017/11/20.
 */

public interface PeopleParser {
    /**
     * 解析输入流 得到Book对象集合
     * @param is
     * @return
     * @throws Exception
     */
    public List<People> parse(InputStream is) throws Exception;

    /**
     * 序列化Book对象集合 得到XML形式的字符串
     * @param peoples
     * @return
     * @throws Exception
     */
    public String serialize(List<People> peoples) throws Exception;
}
