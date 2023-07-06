package cn.auto.core.dynamic;

import cn.auto.enums.DbType;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
public class DbThreadLocal {

    /**
     * 本地数据源线程，每个线程单独拥有自己的数据源配置（默认为主数据源）
     */
    private static ThreadLocal<DbType> THREAD_LOCAL = new ThreadLocal<>();

    public static void setType(DbType dbType){
        THREAD_LOCAL.set(dbType);
    }

    public static DbType getType(){
        return THREAD_LOCAL.get() == null ? DbType.PRIMARY_DB: THREAD_LOCAL.get();
    }

    public static void cleanType(){
        THREAD_LOCAL.remove();
    }

}
