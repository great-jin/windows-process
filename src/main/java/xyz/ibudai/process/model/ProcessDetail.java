package xyz.ibudai.process.model;

public class ProcessDetail {

    private String pid;

    private String serviceName;

    private String protocol;

    private String innerHost;

    private String outerHost;

    private String status;

    private String memoryUse;


    public ProcessDetail() {
    }

    public ProcessDetail(String pid, String protocol, String innerHost, String outerHost, String status) {
        this.pid = pid;
        this.protocol = protocol;
        this.innerHost = innerHost;
        this.outerHost = outerHost;
        this.status = status;
    }

    public static Object[] convert(ProcessDetail detail) {
        return new Object[]{
                detail.getPid(),
                detail.getServiceName(),
                detail.getProtocol(),
                detail.getInnerHost(),
                detail.getOuterHost(),
                detail.getMemoryUse(),
                detail.getStatus()
        };
    }


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getInnerHost() {
        return innerHost;
    }

    public void setInnerHost(String innerHost) {
        this.innerHost = innerHost;
    }

    public String getOuterHost() {
        return outerHost;
    }

    public void setOuterHost(String outerHost) {
        this.outerHost = outerHost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
