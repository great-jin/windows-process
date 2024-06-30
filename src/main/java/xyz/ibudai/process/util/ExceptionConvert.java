package xyz.ibudai.process.util;

import java.util.Objects;

public class ExceptionConvert {

    public static String buildMsg(Throwable e) {
        if (Objects.isNull(e)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        String message = e.getMessage();
        builder.append(message).append("\n");
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            builder.append(element).append("\n");
        }
        return builder.toString();
    }
}
