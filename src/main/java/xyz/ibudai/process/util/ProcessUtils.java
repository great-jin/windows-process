package xyz.ibudai.process.util;

import xyz.ibudai.process.model.ProcessDetail;
import xyz.ibudai.process.model.ServiceDetail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProcessUtils {

    private static final Pattern PATTERN;
    private static final String CHARSET_GBK = "GBK";

    static {
        String REGEX = "^(.*?)\\s+(\\d+)\\s+(\\S+)\\s+(\\d+)\\s+([\\d,]+\\s+K)$";
        PATTERN = Pattern.compile(REGEX);
    }

    /**
     * 查询系统中当前进程
     */
    public static List<ProcessDetail> getTaskDetail() {
        List<ProcessDetail> detailList = new ArrayList<>();

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("netstat", "-ano");
        try {
            Process process = processBuilder.start();
            try (
                    InputStreamReader in = new InputStreamReader(process.getInputStream(), CHARSET_GBK);
                    BufferedReader reader = new BufferedReader(in)
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    List<String> list = Arrays.stream(line.split(" "))
                            .filter(it -> !Objects.equals("", it))
                            .collect(Collectors.toList());
                    if (list.size() != 5) {
                        continue;
                    }

                    String protocol = list.get(0);
                    String innerHost = list.get(1);
                    String outerHost = list.get(2);
                    String status = list.get(3);
                    String pid = list.get(4);
                    detailList.add(new ProcessDetail(pid, protocol, innerHost, outerHost, status));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 查询进程服务名与内存占用
        List<ProcessDetail> data = detailList.stream().skip(1).collect(Collectors.toList());
        List<ServiceDetail> serviceList = getServiceDetail();
        Map<String, ServiceDetail> serviceMap = new HashMap<>();
        serviceList.forEach(it -> serviceMap.put(it.getPid(), it));
        for (ProcessDetail item : data) {
            if (!serviceMap.containsKey(item.getPid())) {
                continue;
            }
            ServiceDetail service = serviceMap.get(item.getPid());
            if (Objects.isNull(service)) {
                continue;
            }

            item.setServiceName(service.getServiceName());
            item.setMemoryUse(convertMb(service.getMemoryUse()));
        }
        return data;
    }

    /**
     * 查询系统服务信息
     */
    public static List<ServiceDetail> getServiceDetail() {
        List<ServiceDetail> detailList = new ArrayList<>();

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("tasklist");
        try {
            Process process = processBuilder.start();
            try (
                    InputStreamReader in = new InputStreamReader(process.getInputStream(), CHARSET_GBK);
                    BufferedReader reader = new BufferedReader(in)
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = PATTERN.matcher(line.trim());
                    if (matcher.matches()) {
                        // 提取匹配到的各列
                        String imageName = matcher.group(1);
                        String pid = matcher.group(2);
                        String memUsage = matcher.group(5);
                        detailList.add(new ServiceDetail(pid, imageName, memUsage));
                    }

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return detailList.stream().skip(1).collect(Collectors.toList());
    }

    private static String convertMb(String usage) {
        usage = usage.substring(0, usage.indexOf("K"));
        usage = usage.replace(",", "");
        return Integer.parseInt(usage.trim()) / 1024 + " MB";
    }
}
