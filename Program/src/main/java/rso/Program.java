package rso;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import rso.generator.Generator;

/**
 * Created by kometa on 27.04.2015.
 */
public class Program {


    public static void  main(String[] Args){
//        ApplicationContext ctx =
//                new AnnotationConfigApplicationContext("rso");
        DriverManagerDataSource d = new DriverManagerDataSource();
        Generator generator = new Generator();
        generator.generate();
        //ctx.getBean("dataSource");
        System.out.println("Hello world");
    }
}
