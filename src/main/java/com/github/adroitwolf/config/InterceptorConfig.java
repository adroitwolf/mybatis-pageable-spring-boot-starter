package com.github.adroitwolf.config;

import com.github.adroitwolf.Interceptor.MybatisInterceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

/**
 * @author adroitwolf
 * @version 1.0.0
 * @ClassName InterceptorConfig.java
 * @Description TODO
 * @createTime 2021年03月08日 09:34:00
 */
@Configuration
@ConditionalOnBean(SqlSession.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class InterceptorConfig {


    @Autowired
    List<SqlSessionFactory> sqlSessionFactoryList;

    @PostConstruct
    public void addPageInterceptor() {
        MybatisInterceptor interceptor = new MybatisInterceptor();
        Properties properties = new Properties();
        //先把一般方式配置的属性放进去
//        properties.putAll(pageHelperProperties());
        //在把特殊配置放进去，由于close-conn 利用上面方式时，属性名就是 close-conn 而不是 closeConn，所以需要额外的一步
//        properties.putAll(this.properties.getProperties());
        interceptor.setProperties(properties);
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            configuration.addInterceptor(interceptor);

        }
    }
}
