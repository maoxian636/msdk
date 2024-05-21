package com.msdk.xsdk.bean;


import java.util.List;
import java.util.Map;

public class XJsonData {


    private Boolean monitorType;
    private String monitorName;
    private String monitorKey;
    private String monitorPath;
    private List<String> adjust;

    @Override
    public String toString() {
        return "XJsonData{" +
                "monitorType=" + monitorType +
                ", monitorName='" + monitorName + '\'' +
                ", monitorKey='" + monitorKey + '\'' +
                ", monitorPath='" + monitorPath + '\'' +
                ", adjust=" + adjust +
                '}';
    }

    public Boolean getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(Boolean monitorType) {
        this.monitorType = monitorType;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public String getMonitorKey() {
        return monitorKey;
    }

    public void setMonitorKey(String monitorKey) {
        this.monitorKey = monitorKey;
    }

    public String getMonitorPath() {
        return monitorPath;
    }

    public void setMonitorPath(String monitorPath) {
        this.monitorPath = monitorPath;
    }

    public List<String> getAdjust() {
        return adjust;
    }

    public void setAdjust(List<String> adjust) {
        this.adjust = adjust;
    }

    public XJsonData(Boolean monitorType, String monitorName, String monitorKey, String monitorPath, List<String> adjust) {
        this.monitorType = monitorType;
        this.monitorName = monitorName;
        this.monitorKey = monitorKey;
        this.monitorPath = monitorPath;
        this.adjust = adjust;
    }
}
