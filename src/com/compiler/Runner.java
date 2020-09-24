package com.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Runner {
    private HashMap<String, Integer> variables = new HashMap<String, Integer>();

    /**
     * Execute a program
     *
     * @param file program file
     */
    public void run(String file) {
        try {
            File program = new File(file);
            Scanner reader = new Scanner(program);
            int i = 0;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                try {
                    this.handleLine(line.trim(), i);
                } catch (Exception e) {
                    System.out.println("Error at line " + (i + 1) + ": " + e.getMessage());
                    System.exit(1);
                }
                i++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File reading error: " + e.getMessage());
        }
    }

    /**
     * Execute a program line
     *
     * @param line
     * @param i    line index
     * @throws Exception
     */
    public void handleLine(String line, int i) throws Exception {
        line = line.trim();
        if (line.isEmpty()) return; // empty line

        if (line.matches("[a-z]+[ ]*=[a-z 0-9\\+\\*]+;")) { // Variable set instruction
            line = line.replace(";", "");
            String[] values = line.split("=");
            String variable = values[0].trim(), expression = values[1].trim();
            this.variables.put(variable, handleExpression(expression, i));

        } else if (line.matches("[a-z 0-9\\+\\*]+;")) { // Print instruction
            line = line.replace(";", "");
            this.print(handleExpression(line.trim(), i));

        } else { // Unknown instruction
            throw new Exception("Bad instruction.");
        }
    }

    /**
     * Prints an value
     *
     * @param value
     */
    public void print(int value) {
        System.out.println(value);
    }

    /**
     * Resolves an expression
     *
     * @param expression
     * @param i          line
     * @return expression result
     * @throws Exception
     */
    private int handleExpression(String expression, int i) throws Exception {
        int result = 0;
        // System.out.println("Expression: " + expression);
        String[] sums = expression.split("\\+");
        for (String sum : sums) {
            sum = sum.trim();
            String[] mults = sum.split("\\*", -1);
            int parcial = 1;
            for (String mult : mults) {
                mult = mult.trim();
                // System.out.println("Mult: " + mult);
                parcial *= resolveValue(mult);
            }
            result += parcial;
        }
        return result;
    }

    /**
     * Resolves an value, or variable
     *
     * @param value
     * @return
     */
    private int resolveValue(String value) throws Exception {
        value = value.trim();
        // System.out.println("resolving: \"" + value + "\"");
        if (value.matches("-?\\d+"))
            return Integer.parseInt(value);
        else if (!value.matches("[a-z]"))
            throw new Exception("Invalid Variable name \"" + value + "\"");
        else if (variables.containsKey(value))
            return variables.get(value);
        else throw new Exception("Variable \"" + value + "\" not found");
    }
}
