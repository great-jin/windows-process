package xyz.ibudai.process;

import xyz.ibudai.process.util.ExceptionUtils;

public class ExceptionTest {

    public static void main(String[] args) {
        try {
            Integer.parseInt("abc");
        } catch (Exception e) {
            System.out.println(ExceptionUtils.buildMsg(e));
        }
    }
}
