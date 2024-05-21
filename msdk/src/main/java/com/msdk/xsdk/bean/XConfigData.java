package com.msdk.xsdk.bean;

public class XConfigData {
    private String requestPath, username, language, naturalQuantity;

    /**
     *
     * @param requestPath       MSDK 请求路径  必须
     * @param username          MSDK 账号  必须
     */
    public XConfigData(String requestPath,  String username) {
        this.requestPath = requestPath;
        this.username = username;
    }

    /**
     *
     * @param requestPath       MSDK 请求路径  必须
     * @param language          MSDK 语言设置  可选
     */
    public XConfigData(String requestPath, String username, String language) {
        this(requestPath,username);
        this.language = language;
    }
    /**
     *
     * @param requestPath       MSDK 请求路径  必须
     * @param language          MSDK 语言设置  可选
     * @param naturalQuantity   MSDK 自然量  可选
     */
    public XConfigData(String requestPath, String username, String language, String naturalQuantity) {
        this(requestPath,username, language);
        this.naturalQuantity=naturalQuantity;
    }

    @Override
    public String toString() {
        return "XConfigData{" +
                "requestPath='" + requestPath + '\'' +
                ", username='" + username + '\'' +
                ", language='" + language + '\'' +
                ", naturalQuantity='" + naturalQuantity + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNaturalQuantity() {
        return naturalQuantity;
    }

    public void setNaturalQuantity(String naturalQuantity) {
        this.naturalQuantity = naturalQuantity;
    }
}
