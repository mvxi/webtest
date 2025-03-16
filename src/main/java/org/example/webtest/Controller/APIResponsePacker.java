package org.example.webtest.Controller;

public class APIResponsePacker<T> {
    private T data;
    private int errCode;
    private String errMsg;

    public APIResponsePacker(int errCode, String errMsg,T data) {
        this.data = data;
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

}
