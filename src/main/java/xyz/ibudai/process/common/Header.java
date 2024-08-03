package xyz.ibudai.process.common;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public enum Header {

    PID(0, "PID", "进程号"),
    SERVICE_NAME(1, "Service name", "服务名"),
    PROTOCOL(2, "Protocol", "协议"),
    INNER_HOST(3, "Inner Host", "内网端口"),
    OUTER_HOST(4, "Outer Host", "外网端口"),
    MEMORY_SE(6, "Memory use", "内存占用"),
    STATUS(5, "Status", "状态");

    private final int index;

    private final String nameEn;

    private final String nameZh;

    Header(int index, String nameEn, String nameZh) {
        this.index = index;
        this.nameEn = nameEn;
        this.nameZh = nameZh;
    }

    public static String[] getHeaders(Locale locale) {
        if (Objects.equals(locale.getLanguage(), Language.EN.getLanguage())) {
            return Arrays.stream(Header.values())
                    .map(Header::getNameEn)
                    .toArray(String[]::new);
        } else {
            return Arrays.stream(Header.values())
                    .map(Header::getNameZh)
                    .toArray(String[]::new);
        }
    }

    public int getIndex() {
        return index;
    }

    public String getNameZh() {
        return nameZh;
    }

    public String getNameEn() {
        return nameEn;
    }
}
