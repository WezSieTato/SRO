package rso;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import rso.core.abstraction.BaseContext;
import rso.program.Program;
import rso.program.configuration.RootConfig;
import rso.generator.Generator;

/**
 * Created by kometa on 27.04.2015.
 */
public class Main {


    public static void  main(String[] Args){

        BaseContext baseContext = BaseContext.getInstance();
        ApplicationContext ctx = new AnnotationConfigApplicationContext(RootConfig.class);

        baseContext.setApplicationContext(ctx);
        Program program = ctx.getBean(Program.class);
        program.start();

        System.out.println("Hello world");
    }
}
