package com.github.adroitwolf.model;

import lombok.Data;
import org.apache.ibatis.session.RowBounds;

/**
 * @author adroitwolf
 * @version 1.0.0
 * @ClassName IRowBounds.java
 * @Description 自定义的Rowbonds，虽然这么说，但是不会用里面的函数
 * @createTime 2021年03月05日 14:34:00
 */
@Data
public class IRowBounds  extends RowBounds {
    private int pageSize;

    private int pageNum; // 从1开始


    private  int offset;

    private  int limit;


    public IRowBounds(int pageNum, int pageSize) {
        super(-1,-1);  //这里是防止RowBounds为Default
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        calcPageInfo();
    }

    private void calcPageInfo(){
        this.offset = this.pageNum >1 ? (this.pageNum -1) * this.pageSize : 0;
        this.limit = this.pageNum >0? this.pageSize : 0;
    }


}
