package com.nongshanie.minio.util;

/**
 * 通用错误编码枚举
 * code 格式 AABBCC AA-服务编码| BB-模块编号（00通用）| CC-异常编号
 * Created by erhu on 2017/1/26.
 * 增加异常级别,0  正常  1  info   2 notice  3 warn  4  error, 默认 info , erhu 2017/3/3
 */
public interface ErrorCodeDefine {
    String getMsg();
    String getCode();
    String getLevel();

    enum MessageCode implements ErrorCodeDefine {
        /**
         * 通用错误编码枚举
         */
        OK("0","OK", ErrorLevel.NORMAL.getCode()),
        BADREQUEST("0001","bad reqeust",ErrorLevel.NOTICE.getCode()),
        UNKNOWERROR("0002","internal server error",ErrorLevel.WARN.getCode()),
        VALIDERROR("0003","request params valid error",ErrorLevel.NOTICE.getCode()),
        TOKENEMPTY("0004","token empty",ErrorLevel.NOTICE.getCode()),
        TOKENEERR("0005","token error",ErrorLevel.NOTICE.getCode()),
        DOWNSTREAMERROR("0006","down stream error",ErrorLevel.NOTICE.getCode()),
        ;
        private String code;
        private String msg;
        private String level;

        MessageCode(String code, String msg, String level) {
            this.code = code;
            this.msg = msg;
            this.level = level;
        }

        @Override
        public String getCode() {
            return code;
        }

        public void setCode(String code,String msg) {
            this.code = code;
        }

        @Override
        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public static MessageCode getMessageCode(String code) {
            for (MessageCode it : MessageCode.values()) {
                if (it.getCode() == code) {
                    return it;
                }
            }
            return UNKNOWERROR;
        }
    }

    enum ErrorLevel{
        NORMAL("0"),
        INFO("1"),
        NOTICE("2"),
        WARN("3"),
        ERROR("4");

        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        ErrorLevel(String code) {
            this.code = code;
        }
    }
}


