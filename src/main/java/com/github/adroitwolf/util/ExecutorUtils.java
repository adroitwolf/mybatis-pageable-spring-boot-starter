package com.github.adroitwolf.util;

import com.github.adroitwolf.model.IRowBounds;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author adroitwolf
 * @version 1.0.0
 * @ClassName ExecutorUtils.java
 * @Description TODO
 * @createTime 2021年03月08日 08:39:00
 */
public class ExecutorUtils {

    public  static  List<Object> getQueryPageResult(Executor executor, MappedStatement ms, Object param, IRowBounds iRowBounds, ResultHandler resultHandler) throws SQLException {
        //我直接重写sql
        BoundSql boundSql = ms.getSqlSource().getBoundSql(ms);
        // 分页查询
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), SQLUtils.unionPageSql(boundSql.getSql(),iRowBounds), boundSql.getParameterMappings(), boundSql.getParameterObject());


        System.out.println(newBoundSql.getSql());

        //重新创建 ms
        MappedStatement newMs = builderNewMappedStatement(ms, newBoundSql);

        return  executor.query(newMs, param, RowBounds.DEFAULT, resultHandler);

    }


    public static  int getCountResult(MappedStatement ms,Object parm) throws SQLException {

        Configuration config = ms.getConfiguration();


        Connection connection = config.getEnvironment().getDataSource().getConnection();
        BoundSql boundSql = ms.getSqlSource().getBoundSql(ms);
        String countSql = getCountSql(boundSql.getSql());
        PreparedStatement preparedStatement = connection.prepareStatement(countSql);
        // 查询Count
        BoundSql countBoundSql = new BoundSql(config, countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        ParameterHandler parameterHandler = new DefaultParameterHandler(ms, parm, countBoundSql);

        parameterHandler.setParameters(preparedStatement);

        //执行获得总条数
        ResultSet rs = preparedStatement.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }

        return count;
    }




    // 创建新的ms
    private static MappedStatement builderNewMappedStatement(MappedStatement ms, BoundSql newBoundSql){


        MappedStatement.Builder msBuilder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(),new MyBoundSource(newBoundSql) , ms.getSqlCommandType());

        msBuilder.resource(ms.getResource());

        msBuilder.fetchSize(ms.getFetchSize());

        msBuilder.statementType(ms.getStatementType());
        msBuilder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            msBuilder.keyProperty(ms.getKeyProperties()[0]);
        }
        msBuilder.timeout(ms.getTimeout());
        msBuilder.parameterMap(ms.getParameterMap());
        msBuilder.resultMaps(ms.getResultMaps());
        msBuilder.resultSetType(ms.getResultSetType());
        msBuilder.cache(ms.getCache());
        msBuilder.flushCacheRequired(ms.isFlushCacheRequired());
        msBuilder.useCache(ms.isUseCache());

        return msBuilder.build();
    }



    // 内部类,用于创建新的ms
    static class MyBoundSource implements SqlSource {


        private BoundSql boundSql;

        public MyBoundSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }


    private static  String getCountSql(String sql){
        StringBuilder builder=new StringBuilder("select count(*) from (");  //拼接后面的

        builder.append(sql);
        builder.append(" ) as t ");
        return builder.toString();
    }
}
