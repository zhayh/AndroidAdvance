package edu.niit.network.model;

import java.io.Serializable;
import java.util.List;

public class Weather implements Serializable {

    /**
     * code : 200
     * msg : 成功!
     * data : {"yesterday":{"date":"30日星期二","high":"高温 23℃","fx":"东风","low":"低温 12℃","fl":"<![CDATA[3-4级]]>","type":"晴"},"city":"南京","aqi":"46","forecast":[{"date":"31日星期三","high":"高温 21℃","fengli":"<![CDATA[3-4级]]>","low":"低温 10℃","fengxiang":"东北风","type":"多云"},{"date":"1日星期四","high":"高温 20℃","fengli":"<![CDATA[3-4级]]>","low":"低温 9℃","fengxiang":"东风","type":"多云"},{"date":"2日星期五","high":"高温 20℃","fengli":"<![CDATA[3-4级]]>","low":"低温 11℃","fengxiang":"东风","type":"多云"},{"date":"3日星期六","high":"高温 20℃","fengli":"<![CDATA[3-4级]]>","low":"低温 15℃","fengxiang":"东风","type":"多云"},{"date":"4日星期天","high":"高温 19℃","fengli":"<![CDATA[3-4级]]>","low":"低温 16℃","fengxiang":"东风","type":"小雨"}],"ganmao":"天凉，昼夜温差较大，较易发生感冒，请适当增减衣服，体质较弱的朋友请注意适当防护。","wendu":"18"}
     */

    private int code;
    private String msg;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        /**
         * yesterday : {"date":"30日星期二","high":"高温 23℃","fx":"东风","low":"低温 12℃","fl":"<![CDATA[3-4级]]>","type":"晴"}
         * city : 南京
         * aqi : 46
         * forecast : [{"date":"31日星期三","high":"高温 21℃","fengli":"<![CDATA[3-4级]]>","low":"低温 10℃","fengxiang":"东北风","type":"多云"},{"date":"1日星期四","high":"高温 20℃","fengli":"<![CDATA[3-4级]]>","low":"低温 9℃","fengxiang":"东风","type":"多云"},{"date":"2日星期五","high":"高温 20℃","fengli":"<![CDATA[3-4级]]>","low":"低温 11℃","fengxiang":"东风","type":"多云"},{"date":"3日星期六","high":"高温 20℃","fengli":"<![CDATA[3-4级]]>","low":"低温 15℃","fengxiang":"东风","type":"多云"},{"date":"4日星期天","high":"高温 19℃","fengli":"<![CDATA[3-4级]]>","low":"低温 16℃","fengxiang":"东风","type":"小雨"}]
         * ganmao : 天凉，昼夜温差较大，较易发生感冒，请适当增减衣服，体质较弱的朋友请注意适当防护。
         * wendu : 18
         */

        private Forecast yesterday;
        private String city;
        private String aqi;
        private String ganmao;
        private String wendu;
        private List<Forecast> forecast;

        public Forecast getYesterday() {
            return yesterday;
        }

        public void setYesterday(Forecast yesterday) {
            this.yesterday = yesterday;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getGanmao() {
            return ganmao;
        }

        public void setGanmao(String ganmao) {
            this.ganmao = ganmao;
        }

        public String getWendu() {
            return wendu;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public List<Forecast> getForecast() {
            return forecast;
        }

        public void setForecast(List<Forecast> forecast) {
            this.forecast = forecast;
        }

        public static class Forecast {
            /**
             * date : 30日星期二
             * high : 高温 23℃
             * fx : 东风
             * low : 低温 12℃
             * fl : <![CDATA[3-4级]]>
             * type : 晴
             */

            private String date;
            private String high;
            private String fx;
            private String low;
            private String fl;
            private String type;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getFx() {
                return fx;
            }

            public void setFx(String fx) {
                this.fx = fx;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return "Yesterday{" +
                        "date='" + date + '\'' +
                        ", high='" + high + '\'' +
                        ", fx='" + fx + '\'' +
                        ", low='" + low + '\'' +
                        ", fl='" + fl + '\'' +
                        ", type='" + type + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "Weather{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

