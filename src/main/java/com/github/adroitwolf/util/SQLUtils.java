package com.github.adroitwolf.util;

import com.github.adroitwolf.model.IRowBounds;

/**
 * @author adroitwolf
 * @version 1.0.0
 * @ClassName SQLUtils.java
 * @Description TODO
 * @createTime 2021年03月08日 13:59:00
 */
public class SQLUtils {

    public  static String unionPageSql(String osql, IRowBounds iRowBounds){
        StringBuilder builder = new StringBuilder();
        builder.append(osql);
        builder.append(" limit ");
        builder.append(iRowBounds.getOffset());
        builder.append(",");
        builder.append(iRowBounds.getLimit());
        return builder.toString();
    }
}
