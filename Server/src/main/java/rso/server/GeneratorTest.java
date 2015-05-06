package rso.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rso.generator.Generator;

/**
 * Created by marcin on 06/05/15.
 */
@Component
public class GeneratorTest implements Runnable {

    @Autowired
    Generator generator;

    public void run() {
        while (true){
            try {
                Thread.sleep(5000);
                generator.generate(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
