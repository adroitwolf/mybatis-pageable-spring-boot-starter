package com.github.adroitwolf.Interceptor;

import com.github.adroitwolf.model.IPage;
import com.github.adroitwolf.model.IRowBounds;
import com.github.adroitwolf.util.ExecutorUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;

/**
 * @author adroitwolf
 * @version 1.0.0
 * @ClassName MybatisInterceptor.java
 * @Description TODO
 * @createTime 2021年03月05日 08:26:00
 */
@Intercepts({@Signature(
        type = Executor.class, method = "query",args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
public class MybatisInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Executor executor = (Executor) invocation.getTarget();
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object param =  args[1]; // 这个是一个map参数
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];


        if(rowBounds instanceof  IRowBounds){

            // 我改成我自己的rowBounds;
            IRowBounds iRowBounds = (IRowBounds) rowBounds;

            // 这里直接查询出来，不转下面的拦截器了
            List<Object> queryPageResult = ExecutorUtils.getQueryPageResult(executor, ms, param, iRowBounds, resultHandler);

            IPage<Object> queryRs = new IPage<>(queryPageResult);


            int total = ExecutorUtils.getCountResult(ms,param);

            queryRs.setTotal(total);

            return queryRs;
        }

        // 直接放行
        return invocation.proceed();

    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }









}
