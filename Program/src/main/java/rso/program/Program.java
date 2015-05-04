package rso.program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import rso.core.abstraction.Node;

/**
 * Created by kometa on 04.05.2015.
 */
@Component
public class Program {


    @Autowired
    ApplicationContext context;

    @Value("${rso.type}")
    private String nodeType;

    public void start(){

        Node node = (Node) context.getBean(nodeType);
        node.run();
    }
}
