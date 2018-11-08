package edu.niit.network;

import java.io.Serializable;

public class Ip implements Serializable {

    /**
     * resultcode : 200
     * reason : Return Successd!
     * result : {"area":"中国","location":"方正宽带骨干网"}
     * error_code : 0
     */

    private String resultcode;
    private String reason;
    private Result result;
    private int error_code;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class Result {
        /**
         * area : 中国
         * location : 方正宽带骨干网
         */

        private String area;
        private String location;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        @Override
        public String toString() {
            return "{area=" + area + ", location=" + location + "}";
        }
    }

    @Override
    public String toString() {
        return "Ip: " + result;
    }
}
