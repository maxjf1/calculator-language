package com.compiler;

import java.util.HashMap;

public class Main {

    private static HashMap<String, String> variables;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Run: \n" +
                    "java -jar compile.jar path/to/your_program.txt");
            System.exit(0);
        }
        Runner program = new Runner();
        program.run(args[0]);
    }

}
