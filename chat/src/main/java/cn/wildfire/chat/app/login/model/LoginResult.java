package cn.wildfire.chat.app.login.model;


/**
 * 真-model，code啊，message之类的，放到了status里面去了
 */
public class LoginResult {
    /**
     * code : 0
     * message : success
     * result : {"deviceId":"160b646130f54cdfa428986d3ee36aca","token":"r7cqz1tfBc6zwDib0J6oeGDRbEXIEfDS6Ce+mc5Cv7lV1+aI976sm/BX7iAaiJk6CYE4A9uWGUb2Bo/FaXHxsOTWr6/LaYX3eFpEBaSh++ZAMVow8aUT6oFBXUFdfdCBMeSAgl70q9f9rY+zksf48BqGvCVUKxsC9C60qWgEmOz3p0aCqNPR8rz5CJSOtI7FF2RTDav/Sd+9sf4fE4vPjg==","register":false}
     */

    private int code;
    private String message;
    private ResultBean result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * deviceId : 160b646130f54cdfa428986d3ee36aca
         * token : r7cqz1tfBc6zwDib0J6oeGDRbEXIEfDS6Ce+mc5Cv7lV1+aI976sm/BX7iAaiJk6CYE4A9uWGUb2Bo/FaXHxsOTWr6/LaYX3eFpEBaSh++ZAMVow8aUT6oFBXUFdfdCBMeSAgl70q9f9rY+zksf48BqGvCVUKxsC9C60qWgEmOz3p0aCqNPR8rz5CJSOtI7FF2RTDav/Sd+9sf4fE4vPjg==
         * register : false
         */

        private String deviceId;
        private String token;
        private boolean register;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public boolean isRegister() {
            return register;
        }

        public void setRegister(boolean register) {
            this.register = register;
        }
    }


    @Override
    public String toString() {
        return "LoginResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
