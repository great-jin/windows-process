package xyz.ibudai.process;

import xyz.ibudai.process.model.ProcessDetail;
import xyz.ibudai.process.model.ServiceDetail;
import xyz.ibudai.process.util.ProcessUtils;

import java.util.List;

public class ProcessTest {

    public static void main(String[] args) {
        List<ProcessDetail> taskDetail = ProcessUtils.getTaskDetail();
        System.out.println(taskDetail);

        List<ServiceDetail> serviceDetail = ProcessUtils.getServiceDetail();
        System.out.println(serviceDetail);
    }
}
