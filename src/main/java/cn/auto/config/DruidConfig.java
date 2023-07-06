package cn.auto.config;


import cn.auto.core.dynamic.DynamicDataSourceHandler;
import cn.auto.enums.DbType;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@Configuration
public class DruidConfig {

    /**
     * 加载主数据源
     *
     * @return 主数据源
     */
    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 加载从数据源(按条件加载)
     *
     * @return 从数据源
     */
    @Bean("slaveDataSource")
    @ConfigurationProperties("spring.datasource.druid.slave")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
    public DataSource slaveDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 配置动态数据源
     *
     * @param masterDataSource 主数据源
     * @return 动态数据源切换配置类
     */
    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSourceHandler dataSource(DataSource masterDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DbType.PRIMARY_DB, masterDataSource);
        try {
            Object slaveDataSource = SpringUtil.getBean("slaveDataSource");
            targetDataSources.put(DbType.SECOND_DB, slaveDataSource);
        } catch (Exception e) {
            //未开启从库则会找不到该对象，非异常，这里进行空捕获
        }
        return new DynamicDataSourceHandler(masterDataSource, targetDataSources);
    }
}
