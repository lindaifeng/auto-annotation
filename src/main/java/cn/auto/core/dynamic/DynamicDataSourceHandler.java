package cn.auto.core.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/** 动态数据源切换配置
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 * 原理：
 * 1、获取数据库连接getConnection()方法时，调用的是determineTargetDataSource()方法，来创建连接，而determineTargetDataSource()方法是决定spring容器连接那个数据源。
 * 2、哪个数据源又是由determineCurrentLookupKey()方法来决定的，此方法是抽象方法，需要我们继承AbstractRoutingDataSource抽象类来重写此方法。
 * 3、该方法返回一个key，该key是数据源对象中的beanName，并赋值给lookupKey，由此key可以通过resolvedDataSources属性的键来获取对应的DataSource值，从而达到数据源切换的功能
 */
public class DynamicDataSourceHandler extends AbstractRoutingDataSource {

    public DynamicDataSourceHandler(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources)
    {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    /**
     * 操作数据库前会先获取本地线程的主从库枚举，再根据主从库枚举获取指定数据源
     * @return 主从库枚举
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DbThreadLocal.getType();
    }
}
