package com.nongshanie.minio.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 响应VO基类
 * Created by erhu on 2017/1/23.
 *
 * @param <T> the type parameter
 */
@SuppressWarnings("unused")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> implements Serializable {
    @Getter
    private String code;

    @Getter
    private String msg;

    @Getter
    private T info;

    /**
     * 成功并且无响应数据的success response
     * 单例，减少对象创建
     */
    private static Response successEmptyResponse = new SuccessEmptyResponse(ErrorCodeDefine.MessageCode.OK.getCode(), ErrorCodeDefine.MessageCode.OK.getMsg());


    /**
     * Instantiates a new Response.
     * 外部不允许调用此构造函数
     *
     * @param code the code
     * @param msg  the msg
     */
    protected Response(String code, String msg) {
        // code 为空 长度不是6位(错误编码长度6为)，也不是1位（成功编码固定为0）
        if (StringUtils.isEmpty(code) || (code.length() != 6 && !code.equals(ErrorCodeDefine.MessageCode.OK.getCode()))) {
            throw new IllegalArgumentException("响应编码错误！");
        }
        this.code = code;
        this.msg = msg;
    }

    /**
     * Instantiates a new Response.
     * 外部不允许调用此构造函数
     *
     * @param serviceCode the service code
     * @param codeDefine  the code define
     */
    protected Response(String serviceCode, ErrorCodeDefine codeDefine) {
        if (StringUtils.isEmpty(serviceCode) || serviceCode.length() != 2) {
            throw new IllegalArgumentException("服务编码错误！");
        }
        this.code = serviceCode + codeDefine.getCode();
        this.msg = codeDefine.getMsg();
    }


    /**
     * 使用code msg构建 response
     * 目前不对外开放
     *
     * @param serviceCode 服务编号
     * @param code        响应编码
     * @param msg         消息
     * @return response
     */
    private static Response buildErrorResponse(String serviceCode, String code, String msg) {
        if (StringUtils.isEmpty(serviceCode) || serviceCode.length() != 2) {
            throw new IllegalArgumentException("服务编码错误！");
        }
        return new Response(serviceCode + code, msg);
    }


    /**
     * 使用ErrorCodeDefine构建 response
     * 目前不对外开放
     *
     * @param serviceCode 服务编号
     * @param codeDefine  错误定义
     * @return response
     */
    private static Response buildErrorResponse(String serviceCode, ErrorCodeDefine codeDefine) {
        return new Response(serviceCode, codeDefine);
    }


    /**
     * Build success empty response response.
     *
     * @return the response
     */
    public static Response buildSuccessEmptyResponse() {
        return Response.successEmptyResponse;
    }


    /**
     * 快速创建成功的response， info = null
     *
     * @param <T> info type
     * @return success response, code=0,msg=OK
     */
    public static <T> Response<T> buildSuccessResponse() {
        return new Response<>(ErrorCodeDefine.MessageCode.OK.getCode(), ErrorCodeDefine.MessageCode.OK.getMsg());
    }

    /**
     * 快速创建成功的response， info = T info
     *
     * @param <T> info type
     * @return success response, code=0,msg=OK
     */
    public static <T> Response<T> buildSuccessResponseWithInfo(T info) {
        return new Response<>(ErrorCodeDefine.MessageCode.OK.getCode(), ErrorCodeDefine.MessageCode.OK.getMsg()).setInfo(info);
    }

    /**
     * 快速构建成功的result reponse，储存列表数据 info = Result<T>
     *
     * @param <T>        data type
     * @param data       list<E>
     * @param totalCount 总数 meta.count
     * @return Response<Result<T>>
     */
    public static <T> Response<Result<T>> buildSuccessResponseWithResult(List<T> data, int totalCount) {
        return new Response<Result<T>>(ErrorCodeDefine.MessageCode.OK.getCode(), ErrorCodeDefine.MessageCode.OK.getMsg())
                .setInfo(Result.buildResult(data, Meta.buildMeta(totalCount)));
    }

    /**
     * 快速构建成功的result reponse，储存列表数据 info = Result<T> , metaData
     *
     * @param <T>      data type
     * @param data     list<E>
     * @param metaData meta.data
     * @return Response<Result<T>>
     */
    public static <T> Response<Result<T>> buildSuccessResponseWithResult(List<T> data, Meta metaData) {
        return new Response<Result<T>>(ErrorCodeDefine.MessageCode.OK.getCode(), ErrorCodeDefine.MessageCode.OK.getMsg())
                .setInfo(Result.buildResult(data, metaData));
    }

    /**
     * 判断response是否是success的
     *
     * @return code ==0  返回true，否则返回false
     */
    @JsonIgnore
    public boolean isSuccessResponse() {
        return ErrorCodeDefine.MessageCode.OK.getCode().equals(this.code);
    }


    /**
     * Sets code.
     *
     * @param code the code
     * @return the code
     */
    public Response setCode(String code) {
        this.code = code;
        return this;
    }


    /**
     * Sets msg.
     *
     * @param msg the msg
     * @return the msg
     */
    public Response setMsg(String msg) {
        this.msg = msg;
        return this;
    }


    /**
     * Sets info.
     *
     * @param info the info
     * @return the info
     */
    public Response setInfo(T info) {
        this.info = info;
        return this;
    }

    /**
     * 定义返回体为空的success response。
     * 对此response的set操作会抛出UnsupportedOperationException异常！
     */
    private static class SuccessEmptyResponse extends Response {
        private static final long serialVersionUID = -7722537724801482736L;

        /**
         * Instantiates a new Success empty response.
         *
         * @param code the code
         * @param msg  the msg
         */
        SuccessEmptyResponse(String code, String msg) {
            super.code = code;
            super.msg = msg;
        }

        /**
         * 判断response是否是success的
         *
         * @return always be true
         */
        @JsonIgnore
        public boolean isSuccessResponse() {
            return true;
        }

        /**
         * 不支持此操作
         *
         * @param code the info
         * @return Response
         */
        @Override
        public Response setCode(String code) {
            throw new UnsupportedOperationException();
        }

        /**
         * 不支持此操作
         *
         * @param msg the info
         * @return the info
         */
        @Override
        public Response setMsg(String msg) {
            throw new UnsupportedOperationException();

        }

        /**
         * 不支持此操作
         *
         * @param info the info
         * @return the info
         */

        @Override
        public Response setInfo(Object info) {
            throw new UnsupportedOperationException();
        }


    }

}
