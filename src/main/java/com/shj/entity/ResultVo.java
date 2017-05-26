package com.shj.entity;

/**
 * Created by xiaoyaolan on 2017/5/3.
 */
public class ResultVo {
    private boolean flag;
    private Object data;
    private String msg;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResultVo{" +
                "flag=" + flag +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
