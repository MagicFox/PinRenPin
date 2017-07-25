package com.fox.pinrenpin;

/**
 * Created by MagicFox on 2016/8/19.
 */
public class LabaResultBean {
    private String uuId;
    private String memberId;
    private String responseTime;
    private String timeoutFlag;
    private String pattern;

    public String getuuId() {
        return uuId;
    }

    public void setuuId(String uId) {
        this.uuId = uId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getResTime() {
        return responseTime;
    }

    public void setResTime(String serverResTime) {
        this.responseTime = serverResTime;
    }

    public String getTimeoutFlag() {
        return timeoutFlag;
    }

    public void setTimeoutFlag(String timeoutFalg) {
        this.timeoutFlag = timeoutFalg;
    }

    public String getImageCode() {
        return pattern;
    }

    public void setImageCode(String resultImage) {
        this.pattern = resultImage;
    }
}
