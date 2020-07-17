package cn.yylm.scw.util;

/**
 * 统一处理Ajax请求
 * @param <T>
 */
public class ResultEntity<T> {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    //封装当前请求处理结果成功或失败
    private String result;

    //请求处理失败返回的消息
    private String message;

    //返回的数据
    private T data;

    /**
     * 请求成功不需要返回数据
     *
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> successWithoutData() {
        return new ResultEntity<Type>(SUCCESS, null, null);
    }

    /**
     * 请求成功需要返回数据
     * @param data
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> successWithData(Type data) {
        return new ResultEntity<>(SUCCESS, null, data);
    }

    /**
     * 请求处理失败
     * @param msg
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> failed(String msg) {
        return new ResultEntity<>(FAILED, msg, null);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
