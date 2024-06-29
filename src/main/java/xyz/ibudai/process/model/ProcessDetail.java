package xyz.ibudai.process.model;

public class ProcessDetail {

    private String protocol;

    private String innerHost;

    private String outerHost;

    private String status;

    private String pid;

    public ProcessDetail() {
    }

    public ProcessDetail(String protocol, String innerHost, String outerHost, String status, String pid) {
        this.protocol = protocol;
        this.innerHost = innerHost;
        this.outerHost = outerHost;
        this.status = status;
        this.pid = pid;
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
}
