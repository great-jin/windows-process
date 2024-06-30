package xyz.ibudai.process.util;

import xyz.ibudai.process.model.ProcessDetail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProcessUtil {

    /**
     * 查询系统中当前进程
     *
     * @return
     */
    public static List<ProcessDetail> buildTableData() {
        List<ProcessDetail> detailList = new ArrayList<>();

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("netstat", "-ano");
        try {
            Process process = processBuilder.start();
            try (
                    InputStreamReader in = new InputStreamReader(process.getInputStream(), "GBK");
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

                    detailList.add(
                            new ProcessDetail(
                                    list.get(0),
                                    list.get(1),
                                    list.get(2),
                                    list.get(3),
                                    list.get(4)
                            )
                    );
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return detailList.stream().skip(1).collect(Collectors.toList());
    }
}
