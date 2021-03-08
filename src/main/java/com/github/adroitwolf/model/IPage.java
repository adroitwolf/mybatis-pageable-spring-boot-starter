package com.github.adroitwolf.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author adroitwolf
 * @version 1.0.0
 * @ClassName IPage.java
 * @Description 分页格式
 * @createTime 2021年03月05日 10:44:00
 */
@Data
public class IPage<T> extends ArrayList<T>  {

    public IPage(Collection<? extends T> c) {
        super(c);
    }

    private int total;

    public List<T> getList() {
        return this;
    }


}
