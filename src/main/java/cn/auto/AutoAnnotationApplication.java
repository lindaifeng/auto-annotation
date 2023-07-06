package cn.auto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Author： 清峰
 * @Description： May there be no bug in the world！
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class AutoAnnotationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutoAnnotationApplication.class,args);
    }
}
