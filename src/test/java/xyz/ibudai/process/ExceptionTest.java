package xyz.ibudai.process;

import xyz.ibudai.process.util.ExceptionConvert;

public class ExceptionTest {

    public static void main(String[] args) {
        try {
            Integer.parseInt("abc");
        } catch (Exception e) {
            System.out.println(ExceptionConvert.buildMsg(e));
        }
    }
}
