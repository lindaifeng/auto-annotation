package cn.auto.pojo;


import java.io.Serializable;

/**
 * @author ldf
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private T data;

    private static final int SUCCESS = 200;
    private static final int FAIL = 500;

    private Result(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    private Result(int code) {
        this(code, null, "操作成功");
    }

    private Result(int code, String msg) {
        this(code, null, msg);
    }

    private Result(int code, T data) {
        this(code, data, null);
    }


    public boolean isSuccess() {
        return this.code == SUCCESS;
    }

    public static <T> Result<T> success(int code) {
        return new Result(code);
    }

    public static <T> Result<T> success(String msg) {
        return new Result(SUCCESS, msg);
    }

    public static <T> Result<T> success(int code, String msg) {
        return new Result(code, msg);
    }

    public static <T> Result<T> data(T data) {
        return data(data, "操作成功");
    }

    public static <T> Result<T> data(T data, String msg) {
        return data(SUCCESS, data, msg);
    }

    public static <T> Result<T> data(int code, T data, String msg) {
        return new Result(code, data, data == null ? "承载数据为空" : msg);
    }

    public boolean isFail() {
        return this.code == FAIL;
    }

    public static <T> Result<T> fail() {
        return new Result(FAIL, "操作失败");
    }

    public static <T> Result<T> fail(String msg) {
        return new Result(FAIL, msg);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return new Result(code, null, msg);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}