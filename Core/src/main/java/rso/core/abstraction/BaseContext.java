package rso.core.abstraction;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by marcin on 07/05/15.
 */
public class BaseContext implements ApplicationContextAware {

    private static BaseContext instance;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }



    public static BaseContext getInstance(){
        if(instance == null ){
            instance = new BaseContext();
        }
        return instance;
    }
}
