package rso;

import generator.Generator;

/**
 * Created by kometa on 27.04.2015.
 */
public class Program {


    public static void  main(String[] Args){

        Generator generator = new Generator();
        generator.generate();
        System.out.println("Hello world");
    }
}
