package com.yikang.app.yikangserver.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Created by 郝晓东 on 2016/5/28.
 */
public class TimeCastUtils {
    /**
     * 将指定字符串转换成日期
     *
     * @param date        String 日期字符串
     * @param datePattern String 日期格式
     * @return Date
     */
    public static java.util.Date getFormatDate(String date, String datePattern) {
        SimpleDateFormat sd = new SimpleDateFormat(datePattern);
        return sd.parse(date, new java.text.ParsePosition(0));
    }


    /**
     * 将指定日期对象转换成格式化字符串
     *
     * @param date        Date XML日期对象
     * @param datePattern String 日期格式
     * @return String
     */
    public static String getFormattedString(Date date, String datePattern) {
        SimpleDateFormat sd = new SimpleDateFormat(datePattern);

        return sd.format(date);
    }

    /**
     * 将指定XML日期对象转换成格式化字符串
     *
     * @param xmlDate     Date XML日期对象
     * @param datePattern String 日期格式
     * @return String
     */
    public static String getFormattedString(XMLGregorianCalendar xmlDate,
                                            String datePattern) {
        SimpleDateFormat sd = new SimpleDateFormat(datePattern);

        Calendar calendar = xmlDate.toGregorianCalendar();

        return sd.format(calendar.getTime());
    }

    /**
     * 将指定XML日期对象转换成日期对象
     *
     * @param xmlDate     Date XML日期对象
     *
     * @return Date
     */
    public static Date xmlGregorianCalendar2Date(XMLGregorianCalendar xmlDate) {
        return xmlDate.toGregorianCalendar().getTime();
    }

    public static String getThisYear() {
        // 获得当前日期
        Calendar cldCurrent = Calendar.getInstance();
        // 获得年月日
        String strYear = String.valueOf(cldCurrent.get(Calendar.YEAR));
        return strYear;
    }

    public static XMLGregorianCalendar convert2XMLCalendar(Calendar calendar) {
        try {
            DatatypeFactory dtf = DatatypeFactory.newInstance();
            return dtf.newXMLGregorianCalendar(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND),
                    calendar.get(Calendar.MILLISECOND),
                    calendar.get(Calendar.ZONE_OFFSET) / (1000 * 60));

        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 获取当天时间
    public static java.sql.Timestamp getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
        String dateString = dateFormat.format(now);
        SimpleDateFormat sd = new SimpleDateFormat(dateformat);
        Date dateFormt = sd.parse(dateString, new java.text.ParsePosition(0));
        java.sql.Timestamp dateTime = new java.sql.Timestamp(dateFormt
                .getTime());

        return dateTime;
        // return hehe;
    }

    // 获取指定时间
    public static java.sql.Timestamp getNowNewTime(String date, String dateformat) {
        //Date   now   =   new   Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);//可以方便地修改日期格式
        dateFormat.parse(date, new java.text.ParsePosition(0));

        //  String  dateString= dateFormat.format(date);
        Date dateFormt = dateFormat.parse(date, new java.text.ParsePosition(0));
        java.sql.Timestamp dateTime = new java.sql.Timestamp(dateFormt.getTime());


        return dateTime;
        // return hehe;
    }

    /**
     *
     * @return
     */
    public static String getTFormatString(String tdate) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String str = "";
        try {
            java.util.Date date = format1.parse(tdate);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str = format2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void main(String[] args) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        String date = "20110202";
        System.out.println(sd.parse(date, new java.text.ParsePosition(0)));
        System.out.println(getBefore2HourDate());
    }

    //获取当前时间前2个小时的时间。
    public static String getBefore2HourDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, -2); // 目前時間加3小時
        return df.format(c.getTime());

    }


    /**
     * @param time1 当前时间
     * @param time2 比较时间
     * @return 如果time1比time2大gap分钟，则返回true;
     */
    public static String compareDateTime(Long time1, Long time2) {
        if ((time1 - time2) <= 60 * 1000) {
            return "刚刚";
        } else if ((time1 - time2) <= 59 * 60 * 1000 && (time1 - time2) > 58 *60 * 1000) {
            return "59分钟前";
        }else if ((time1 - time2) <= 58 * 60 * 1000 && (time1 - time2) > 57 *60 * 1000) {
            return "58钟前";
        }else if ((time1 - time2) <= 57 * 60 * 1000 && (time1 - time2) > 56 *60 * 1000) {
            return "57分钟前";
        }else if ((time1 - time2) <= 56 * 60 * 1000 && (time1 - time2) > 55 *60 * 1000) {
            return "56分钟前";
        }else if ((time1 - time2) <= 55 * 60 * 1000 && (time1 - time2) > 54 *60 * 1000) {
            return "55分钟前";
        }else if ((time1 - time2) <= 54 * 60 * 1000 && (time1 - time2) > 53 *60 * 1000) {
            return "54分钟前";
        }else if ((time1 - time2) <= 53 * 60 * 1000 && (time1 - time2) > 52 *60 * 1000) {
            return "53分钟前";
        }else if ((time1 - time2) <= 52 * 60 * 1000 && (time1 - time2) > 51 *60 * 1000) {
            return "52分钟前";
        }else if ((time1 - time2) <= 51 * 60 * 1000 && (time1 - time2) > 50 *60 * 1000) {
            return "51分钟前";
        }else if ((time1 - time2) <= 50 * 60 * 1000 && (time1 - time2) > 49 * 60 * 1000) {
            return "50分钟前";
        }else if ((time1 - time2) <= 49 * 60 * 1000 && (time1 - time2) > 48 *60 * 1000) {
            return "49分钟前";
        }else if ((time1 - time2) <= 48 * 60 * 1000 && (time1 - time2) >  47*60 * 1000) {
            return "48分钟前";
        }else if ((time1 - time2) <= 47* 60 * 1000 && (time1 - time2) > 46 *60 * 1000) {
            return "47分钟前";
        }else if ((time1 - time2) <= 46 * 60 * 1000 && (time1 - time2) > 45 *60 * 1000) {
            return "46分钟前";
        }else if ((time1 - time2) <= 45 * 60 * 1000 && (time1 - time2) >44 * 60 * 1000) {
            return "45分钟前";
        }else if ((time1 - time2) <= 44 * 60 * 1000 && (time1 - time2) > 43 *60 * 1000) {
            return "44分钟前";
        }else if ((time1 - time2) <= 43 * 60 * 1000 && (time1 - time2) > 42 *60 * 1000) {
            return "43分钟前";
        }else if ((time1 - time2) <= 42 * 60 * 1000 && (time1 - time2) > 41 *60 * 1000) {
            return "42分钟前";
        }else if ((time1 - time2) <=41 * 60 * 1000 && (time1 - time2) > 40 *60 * 1000) {
            return "41分钟前";
        }else if ((time1 - time2) <= 40 * 60 * 1000 && (time1 - time2) > 39 *60 * 1000) {
            return "40分钟前";
        }else if ((time1 - time2) <= 39 * 60 * 1000 && (time1 - time2) >38 * 60 * 1000) {
            return "39分钟前";
        }else if ((time1 - time2) <= 38 * 60 * 1000 && (time1 - time2) > 37 *60 * 1000) {
            return "38分钟前";
        }else if ((time1 - time2) <= 37 * 60 * 1000 && (time1 - time2) > 36 *60 * 1000) {
            return "37分钟前";
        }else if ((time1 - time2) <= 36 * 60 * 1000 && (time1 - time2) > 35 * 60 * 1000) {
            return "36分钟前";
        }else if ((time1 - time2) <= 35 * 60 * 1000 && (time1 - time2) > 34 *60 * 1000) {
            return "35分钟前";
        }else if ((time1 - time2) <= 34 * 60 * 1000 && (time1 - time2) > 33 *60 * 1000) {
            return "34分钟前";
        }else if ((time1 - time2) <= 33 * 60 * 1000 && (time1 - time2) > 32 *60 * 1000) {
            return "33分钟前";
        }else if ((time1 - time2) <= 32 * 60 * 1000 && (time1 - time2) >31 * 60 * 1000) {
            return "32分钟前";
        }else if ((time1 - time2) <= 31 * 60 * 1000 && (time1 - time2) > 30 *60 * 1000) {
            return "31分钟前";
        }else if ((time1 - time2) <= 30 * 60 * 1000 && (time1 - time2) > 29 *60 * 1000) {
            return "30分钟前";
        }else if ((time1 - time2) <= 29 * 60 * 1000 && (time1 - time2) > 28 *60 * 1000) {
            return "29分钟前";
        }else if ((time1 - time2) <= 28 * 60 * 1000 && (time1 - time2) >27 * 60 * 1000) {
            return "28分钟前";
        }else if ((time1 - time2) <= 27 * 60 * 1000 && (time1 - time2) > 26 * 60 * 1000) {
            return "27分钟前";
        }else if ((time1 - time2) <= 26 * 60 * 1000 && (time1 - time2) >25 * 60 * 1000) {
            return "26分钟前";
        }else if ((time1 - time2) <= 25 * 60 * 1000 && (time1 - time2) > 24 *60 * 1000) {
            return "25分钟前";
        }else if ((time1 - time2) <= 24 * 60 * 1000 && (time1 - time2) > 23 *60 * 1000) {
            return "24分钟前";
        }else if ((time1 - time2) <= 23 * 60 * 1000 && (time1 - time2) > 22 *60 * 1000) {
            return "23分钟前";
        }else if ((time1 - time2) <= 22 * 60 * 1000 && (time1 - time2) >21* 60 * 1000) {
            return "22分钟前";
        }else if ((time1 - time2) <= 21* 60 * 1000 && (time1 - time2) >  20 *60 * 1000) {
            return "21分钟前";
        }else if ((time1 - time2) <= 20 * 60 * 1000 && (time1 - time2) > 19 *60 * 1000) {
            return "20分钟前";
        }else if ((time1 - time2) <= 19 * 60 * 1000 && (time1 - time2) >18 * 60 * 1000) {
            return "19分钟前";
        }else if ((time1 - time2) <= 18 * 60 * 1000 && (time1 - time2) >17 * 60 * 1000) {
            return "18分钟前";
        }else if ((time1 - time2) <= 17 * 60 * 1000 && (time1 - time2) > 16 *60 * 1000) {
            return "17分钟前";
        }else if ((time1 - time2) <= 16 * 60 * 1000 && (time1 - time2) > 15*60 * 1000) {
            return "16分钟前";
        }else if ((time1 - time2) <= 15* 60 * 1000 && (time1 - time2) > 14 *60 * 1000) {
            return "15分钟前";
        }else if ((time1 - time2) <= 14 * 60 * 1000 && (time1 - time2) >13 * 60 * 1000) {
            return "14分钟前";
        }else if ((time1 - time2) <= 13 * 60 * 1000 && (time1 - time2) >12 * 60 * 1000) {
            return "13分钟前";
        }else if ((time1 - time2) <= 12 * 60 * 1000 && (time1 - time2) > 11 *60 * 1000) {
            return "12分钟前";
        }else if ((time1 - time2) <= 11 * 60 * 1000 && (time1 - time2) >10 * 60 * 1000) {
            return "11分钟前";
        }else if ((time1 - time2) <= 10 * 60 * 1000 && (time1 - time2) >9 * 60 * 1000) {
            return "10分钟前";
        }else if ((time1 - time2) <= 9 * 60 * 1000 && (time1 - time2) >8 * 60 * 1000) {
            return "9分钟前";
        }else if ((time1 - time2) <= 8 * 60 * 1000 && (time1 - time2) > 7 *60 * 1000) {
            return "8分钟前";
        }else if ((time1 - time2) <= 7 * 60 * 1000 && (time1 - time2) >6 * 60 * 1000) {
            return "7分钟前";
        }else if ((time1 - time2) <= 6 * 60 * 1000 && (time1 - time2) >5 * 60 * 1000) {
            return "6分钟前";
        }else if ((time1 - time2) <= 5 * 60 * 1000 && (time1 - time2) >4 * 60 * 1000) {
            return "5分钟前";
        }else if ((time1 - time2) <=4 * 60 * 1000 && (time1 - time2) > 3 *60 * 1000) {
            return "4分钟前";
        }else if ((time1 - time2) <= 3 * 60 * 1000 && (time1 - time2) > 2 *60 * 1000) {
            return "3分钟前";
        }else if ((time1 - time2) <= 2 * 60 * 1000 && (time1 - time2) > 1 *60 * 1000) {
            return "2分钟前";
        }else if ((time1 - time2) <= 1 * 60 * 1000 && (time1 - time2) > 59* 1000) {
            return "1分钟前";
        }
        else if ((time1 - time2) <= 23 * 60 * 60 * 1000 && (time1 - time2) > 22 *60 * 60 * 1000) {
            return "23小时前";
        }else if ((time1 - time2) <= 22 * 60 * 60 * 1000 && (time1 - time2) > 21 *60 * 60 * 1000) {
            return "22小时前";
        }else if ((time1 - time2) <= 21 * 60 * 60 * 1000 && (time1 - time2) > 20 * 60 * 60 * 1000) {
            return "21小时前";
        }else if ((time1 - time2) <= 20 * 60 * 60 * 1000 && (time1 - time2) > 19 *60 * 60 * 1000) {
            return "20小时前";
        }else if ((time1 - time2) <= 19 * 60 * 60 * 1000 && (time1 - time2) >18 * 60 * 60 * 1000) {
            return "19小时前";
        }else if ((time1 - time2) <= 18 * 60 * 60 * 1000 && (time1 - time2) >17 * 60 * 60 * 1000) {
            return "18小时前";
        }else if ((time1 - time2) <= 17 * 60 * 60 * 1000 && (time1 - time2) > 16 *60 * 60 * 1000) {
            return "17小时前";
        }else if ((time1 - time2) <= 16 * 60 * 60 * 1000 && (time1 - time2) >15 * 60 * 60 * 1000) {
            return "16小时前";
        }else if ((time1 - time2) <= 15 * 60 * 60 * 1000 && (time1 - time2) >14 *60 * 60 * 1000) {
            return "15小时前";
        }else if ((time1 - time2) <= 14 * 60 * 60 * 1000 && (time1 - time2) > 13 *60 * 60 * 1000) {
            return "14小时前";
        }else if ((time1 - time2) <= 13 * 60 * 60 * 1000 && (time1 - time2) > 12 *60 * 60 * 1000) {
            return "13小时前";
        }else if ((time1 - time2) <= 12 * 60 * 60 * 1000 && (time1 - time2) > 11 *60 * 60 * 1000) {
            return "12小时前";
        }else if ((time1 - time2) <= 11 * 60 * 60 * 1000 && (time1 - time2) > 10 *60 * 60 * 1000) {
            return "11小时前";
        }else if ((time1 - time2) <= 10 * 60 * 60 * 1000 && (time1 - time2) > 9 * 60 * 60 * 1000) {
            return "10小时前";
        }else if ((time1 - time2) <= 9 * 60 * 60 * 1000 && (time1 - time2) >8 * 60 * 60 * 1000) {
            return "9小时前";
        }else if ((time1 - time2) <= 8 * 60 * 60 * 1000 && (time1 - time2) > 7 *60 * 60 * 1000) {
            return "8小时前";
        }else if ((time1 - time2) <= 7 * 60 * 60 * 1000 && (time1 - time2) >6 * 60 * 60 * 1000) {
            return "7小时前";
        }else if ((time1 - time2) <= 6 * 60 * 60 * 1000 && (time1 - time2) > 5 *60 * 60 * 1000) {
            return "6小时前";
        }else if ((time1 - time2) <= 5 * 60 * 60 * 1000 && (time1 - time2) > 4 *60 * 60 * 1000) {
            return "5小时前";
        }else if ((time1 - time2) <= 4 * 60 * 60 * 1000 && (time1 - time2) >3 *60 * 60 * 1000) {
            return "4小时前";
        }else if ((time1 - time2) <= 3 * 60 * 60 * 1000 && (time1 - time2) >2 * 60 * 60 * 1000) {
            return "3小时前";
        }else if ((time1 - time2) <= 2 * 60 * 60 * 1000 && (time1 - time2) > 1 *60 * 60 * 1000) {
            return "2小时前";
        } else if ((time1 - time2) <= 10 * 24 * 60 * 60 * 1000 && (time1 - time2) > 9 *24 * 60 * 60 * 1000) {
            return "10天前";
        }
        else if ((time1 - time2) <= 9 * 24 * 60 * 60 * 1000 && (time1 - time2) > 8 *24 * 60 * 60 * 1000) {
            return "9天前";
        }
        else if ((time1 - time2) <= 8 * 24 * 60 * 60 * 1000 && (time1 - time2) > 7 *24 * 60 * 60 * 1000) {
            return "8天前";
        }
        else if ((time1 - time2) <= 7 * 24 * 60 * 60 * 1000 && (time1 - time2) >6 *  24 * 60 * 60 * 1000) {
            return "7天前";
        }
        else if ((time1 - time2) <= 6 * 24 * 60 * 60 * 1000 && (time1 - time2) > 5*24 * 60 * 60 * 1000) {
            return "6天前";
        }
        else if ((time1 - time2) <= 5* 24 * 60 * 60 * 1000 && (time1 - time2) > 4 *24 * 60 * 60 * 1000) {
            return "5天前";
        }
        else if ((time1 - time2) <= 4 * 24 * 60 * 60 * 1000 && (time1 - time2) > 3 *24 * 60 * 60 * 1000) {
            return "4天前";
        }
        else if ((time1 - time2) <= 3 * 24 * 60 * 60 * 1000 && (time1 - time2) >  2 *24 * 60 * 60 * 1000) {
            return "3天前";
        }
        else if ((time1 - time2) <= 2 * 24 * 60 * 60 * 1000 && (time1 - time2) >1 * 24 * 60 * 60 * 1000) {
            return "2天前";
        }
        else if ((time1 - time2) <= 1 * 24 * 60 * 60 * 1000 && (time1 - time2) > 24 * 60 * 60 * 1000) {
            return "1天前";
        }else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(time2);
            return dateString;
        }
    }

}
