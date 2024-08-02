package xyz.ibudai.process.model;

public class ServiceDetail {

    private String pid;

    private String serviceName;

    private String memoryUse;

    public ServiceDetail() {
    }

    public ServiceDetail(String pid, String serviceName, String memoryUse) {
        this.pid = pid;
        this.serviceName = serviceName;
        this.memoryUse = memoryUse;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMemoryUse() {
        return memoryUse;
    }

    public void setMemoryUse(String memoryUse) {
        this.memoryUse = memoryUse;
    }
}
