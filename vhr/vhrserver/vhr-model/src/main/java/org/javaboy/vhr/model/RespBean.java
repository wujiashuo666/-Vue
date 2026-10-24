package org.javaboy.vhr.model;

public class RespBean {
    /**
     * 业务处理成功状态码
     */
    private static final int STATUS_SUCCESS = 200;
    /**
     * 业务处理失败状态码
     */
    private static final int STATUS_ERROR = 500;

    private Integer status;
    private String msg;
    private Object obj;

    public static RespBean build() {
        return new RespBean();
    }

    public static RespBean ok(String msg) {
        return new RespBean(STATUS_SUCCESS, msg, null);
    }

    public static RespBean ok(String msg, Object obj) {
        return new RespBean(STATUS_SUCCESS, msg, obj);
    }

    public static RespBean error(String msg) {
        return new RespBean(STATUS_ERROR, msg, null);
    }

    public static RespBean error(String msg, Object obj) {
        return new RespBean(STATUS_ERROR, msg, obj);
    }

    private RespBean() {
    }

    private RespBean(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public Integer getStatus() {
        return status;
    }

    public RespBean setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RespBean setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public RespBean setObj(Object obj) {
        this.obj = obj;
        return this;
    }

    @Override
    public String toString() {
        return "RespBean{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", obj=" + obj +
                '}';
    }
}
