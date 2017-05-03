package com.choudoufu.solr.common.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;



public class DateUtil extends DateUtils{
	
	private static transient int gregorianCutoverYear = 1582;
    /** 闰年中每月天数 */
    private static final int[] DAYS_P_MONTH_LY= {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    /** 非闰年中每月天数 */
    private static final int[] DAYS_P_MONTH_CY= {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    /** 代表数组里的年、月、日 */
    private static final int Y = 0, M = 1, D = 2,H=3,m=4,s=5;
    
    public final static String PATTERN_DATE_MD = "MM-dd";
    
    /** 
	 * yyyy-MM-dd
	 */
	private final static SimpleDateFormat dateLineFormatter;
	/** yyyy-MM-dd */
	public final static String PATTERN_DATE_LINE = "yyyy-MM-dd";

	/** 
	 * yyyy-MM-dd HH:mm:ss
	 */
	private final static SimpleDateFormat dateTimeFormatter;
	/** yyyy-MM-dd HH:mm:ss */
	public final static String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	
	/** 
	 * yyyyMMddHHmmss
	 */
	private final static SimpleDateFormat dateTimeRetractFormatter;
	/** yyyyMMddHHmmss */
	public final static String PATTERN_RETRACT_DATE_TIME = "yyyyMMddHHmmss";
	
	/** yyMMddHHmmss */
	public final static String PATTERN_RETRACT_YEAR_DATE_TIME = "yyMMddHHmmss";
	
	
	/** 
	 * yyyy-MM-dd'T'HH:mm:ss'Z'
	 */
	private final static SimpleDateFormat solrDateFmt;
	/** yyyy-MM-ddTHH:mm:ssZ */
	public final static String PATTERN_SOLR_DATE = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	/**
	 * HH-mm
	 */
	private final static SimpleDateFormat timeLineFormatter;
	/** HH-mm */
	public final static String PATTERN_TIME_LINE = "HH-mm";
	
	/** HH:mm */
	public final static String PATTERN_TIME_POINT = "HH:mm";
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
		"yyyyMMddHHmmss"};
	static {
		dateTimeFormatter = new SimpleDateFormat(PATTERN_DATE_TIME);
		dateTimeRetractFormatter = new SimpleDateFormat(PATTERN_RETRACT_DATE_TIME);
		dateLineFormatter = new SimpleDateFormat(PATTERN_DATE_LINE);
		timeLineFormatter = new SimpleDateFormat(PATTERN_TIME_LINE);
		solrDateFmt = new SimpleDateFormat(PATTERN_SOLR_DATE);
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static final String now() {
		return dateTimeFormatter.format(new Date());
	}
	
	/**
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static final String now(String pattern) {
		return new SimpleDateFormat(pattern).format(new Date());
	}
	
	/**
	 * @return yyyyMMddHHmmss
	 */
	public static final String nowRetract() {
		return dateTimeRetractFormatter.format(new Date());
	}
	
	/**
	 * @return yyyy-MM-dd
	 */
	public static final String nowDateLine() {
		return dateLineFormatter.format(new Date());
	}

	/**
	 * @return HH-mm 
	 */
	public static final String nowTimeLine() {
		return timeLineFormatter.format(new Date());
	}

	@Deprecated
	public static Date toDate(String strDate) throws ParseException {
		if (StringUtils.isEmpty(strDate)) {
			return null;
		}
		Date date = null;
		try {
			if (strDate.length() > 11) {
				date = dateTimeFormatter.parse(strDate);
			} else {
				date = dateLineFormatter.parse(strDate);
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());

		}
		return date;
	}

	public static final Date toDate(String strDate, String pattern)
			throws ParseException {
		if (StringUtils.isEmpty(strDate)) {
			return null;
		}
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(pattern);

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			pe.printStackTrace();
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	public static final String toGMT(Date date) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"EEE, dd-MMM-yyyy HH:mm:ss zzz");
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			format.setCalendar(cal);
			return format.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 日期转成字符串
	 * @param aDate
	 * @return yyyy-MM-dd HH:mm:ss 
	 */
	public static final String toString(Date aDate) {
		return dateTimeFormatter.format(aDate);
	}
	
	
	/**
	 * 日期转成字符串
	 * @param aDate
	 * @return yyyy-MM-dd
	 */
	public static final String toShortString(Date aDate) {
		return dateLineFormatter.format(aDate);
	}
	
	/**
	 * 日期 转成字符串 (缩进的)
	 * @param aDate
	 * @return yyyyMMddHHmmss
	 */
	public static final String toStringIndent(Date aDate) {
		return dateTimeRetractFormatter.format(aDate);
	}
	
	/**
	 * 返回 solr格式日期
	 * @param aDate
	 * @return
	 */
	public static final String toSolrStrDate(Date date) {
		return solrDateFmt.format(date);
	}
	
	public static final String toString(Date aDate, String pattern) {
		if ((pattern == null) || (aDate == null)) {
			return "";
		}
		SimpleDateFormat df = null;
		String returnValue = "";
		df = new SimpleDateFormat(pattern);
		returnValue = df.format(aDate);

		return (returnValue);
	}

	public static final String toString(long time) {
		return dateTimeFormatter.format(new Date(time));
	}
	
	public static final String toWeekStr(int week) {
		 String[] dayOfWeek = {"", "日", "一", "二", "三", "四", "五", "六"}; 
	     return dayOfWeek[week]; 
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str ) {
		if (str == null){
			return null;
		}
		try {
			return DateUtil.parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String parseSolrDate(Object str) {
		Date date = parseDate(str);
		if(date == null)
			return null;
		return toString(date, PATTERN_SOLR_DATE);
	}
    
    /**
    * 将代表日期的字符串分割为代表年月日的整形数组
    * @param date yyyy-MM-dd HH:mm:ss
    * @return
    */
    public static int[] splitYMDhms(String date){
        date = date.replace("-", "");
        int[] splitYMDhms = {0, 0, 0, 0, 0, 0};
        splitYMDhms[Y] = Integer.parseInt(date.substring(0, 4));
        splitYMDhms[M] = Integer.parseInt(date.substring(4, 6));
        splitYMDhms[D] = Integer.parseInt(date.substring(6, 8));
        try
        {
            String[] Hms=date.split(" ")[1].split(":");
            splitYMDhms[H]=Integer.parseInt(Hms[0]);
            splitYMDhms[m]=Integer.parseInt(Hms[1]);
            splitYMDhms[s]=Integer.parseInt(Hms[2]);
        } catch (Exception e)
        {
            splitYMDhms[H]=0;
            splitYMDhms[m]=0;
            splitYMDhms[s]=0;
        }
        return splitYMDhms;
    }
	/**
    * 检查传入的参数代表的年份是否为闰年
    * @param year
    * @return
    */
    public static boolean isLeapYear(int year) {
        return year >= gregorianCutoverYear ?
            ((year%4 == 0) && ((year%100 != 0) || (year%400 == 0))) : (year%4 == 0); 
    }    
    /**
    * 日期加1天
    * @param year
    * @param month
    * @param day
    * @return
    */
    private static int[] addOneDay(int year, int month, int day){
        if(isLeapYear( year )){
            day++;
            if( day > DAYS_P_MONTH_LY[month -1 ] ){
                month++;
                if(month > 12){
                    year++;
                    month = 1;
                }
                day = 1;
            }
        }else{
            day++;
            if( day > DAYS_P_MONTH_CY[month -1 ] ){
                month++;
                if(month > 12){
                    year++;
                    month = 1;
                }
                day = 1;
            }
        }
        int[] ymd = {year, month, day};
        return ymd;
    }
    
    protected static int[] add1Day(int[] YMDhms)
    {
    	int year=YMDhms[Y],month=YMDhms[M],day=YMDhms[D];
    	if(isLeapYear( year )){
            day++;
            if( day > DAYS_P_MONTH_LY[month -1 ] ){
                month++;
                if(month > 12){
                    year++;
                    month = 1;
                }
                day = 1;
            }
        }else{
            day++;
            if( day > DAYS_P_MONTH_CY[month -1 ] ){
                month++;
                if(month > 12){
                    year++;
                    month = 1;
                }
                day = 1;
            }
        }
    	YMDhms[Y]=year;
    	YMDhms[M]=month;
    	YMDhms[D]=day;
		return YMDhms;
    	
    }
    
    /**
    * 将不足两位的月份或日期补足为两位
    * @param decimal
    * @return
    */
    public static String formatMonthDay(int decimal){
        DecimalFormat df = new DecimalFormat("00");
        return df.format( decimal );
    }
    public static String format2Length(int decimal){
    	DecimalFormat df = new DecimalFormat("00");
    	return df.format( decimal );
    }
    
    /**
    * 将不足四位的年份补足为四位
    * @param decimal
    * @return
    */
    public static String formatYear(int decimal){
        DecimalFormat df = new DecimalFormat("0000");
        return df.format( decimal );
    }

    /**
    * 计算两个日期之间相隔的天数
    * @param begin
    * @param end
    * @return
    * @throws ParseException
    */
    public static long countDay(String begin,String end){
           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
           Date beginDate , endDate;
           long day = 0;
           try {
            beginDate= format.parse(begin);
            endDate=  format.parse(end);
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);  
        } catch (ParseException e) {
            e.printStackTrace();
        }                
           return day;
    }
     
    /**
     * 每小时 【日期集】（24小时制）yyyy-MM-dd hh
     * @param pattern
     * @return
     */
    public static List<String> getEveryHour(Date beginDate , Date endDate,String pattern){
    	List<String> dates = converterDate(beginDate, endDate, pattern);
		if(dates == null){
			return null;
		}
    	for(Date iDate = beginDate; iDate.before(endDate) || iDate.equals(endDate);){
    		dates.add(toString(iDate,pattern));
	    	iDate = addHour(iDate, 1);
    	}
		return dates;
    }
    
    /**
     * 半天 【日期集】
     * @param pattern
     * @return
     */
    public static List<String> getEveryHalfDay(Date beginDate , Date endDate, String pattern){
    	List<String> dates = converterDate(beginDate, endDate, pattern);
		if(dates == null){
			return null;
		}
    	for(Date iDate = beginDate; iDate.before(endDate) || iDate.equals(endDate);){
    		dates.add(toString(iDate,pattern));
	    	iDate = addHour(iDate, 12);
    	}
		return dates;
    }
    
    public static List<String> getEveryDay(Date beginDate , Date endDate){
    	return getEveryDay(toString(beginDate),toString(endDate));
    }
    
    /**
    * 以循环的方式计算日期
    * @param beginDate 
    * @param endDate
    * @param days 格式：yyyy-MM-dd HH:mm:ss
    * @return
    */
    public static List<String> getEveryDay(String beginDate , String endDate){
        long days = countDay(beginDate, endDate);
        int[] ymd = splitYMDhms( beginDate );
        List<String> everyDays = new ArrayList<String>();
        everyDays.add(beginDate.split(" ")[0]);
        for(int i = 0; i < days; i++){
            ymd = addOneDay(ymd[Y], ymd[M], ymd[D]);
            everyDays.add(formatYear(ymd[Y])+"-"+formatMonthDay(ymd[M])+"-"+formatMonthDay(ymd[D])+
            		" 00:00:00");
        }
        return everyDays;
    }
    
    /**
     * 每天 日期集合
     * @param beginDate 
     * @param endDate
     * @return
     */
    public static List<String> getEveryDay(Date beginDate , Date endDate, String pattern){
    	List<String> dates = converterDate(beginDate, endDate, pattern);
		if(dates == null){
			return null;
		}
    	for(Date iDate = beginDate; iDate.before(endDate) || iDate.equals(endDate);){
    		dates.add(toString(iDate, pattern));
	    	iDate = addDay(iDate, 1);
    	}
		return dates;
    }

    /**
     * 每 个月 的 【日期集】
     * @param pattern
     * @return
     */
    public static List<String> getEveryMonth(Date beginDate , Date endDate,String pattern){
    	List<String> dates = converterDate(beginDate, endDate, pattern);
		if(dates == null){
			return null;
		}
    	for(Date iDate = beginDate; iDate.before(endDate) || iDate.equals(endDate);){
    		dates.add(toString(iDate, pattern));
	    	iDate = addMonth(iDate, 1);
    	}
		return dates;
    }
    
    /**
     * 每半个月 【日期集】
     * @param pattern
     * @return
     */
    public static List<String> getEveryHalfMonth(Date beginDate , Date endDate,String pattern){
    	List<String> dates = converterDate(beginDate, endDate, pattern);
		if(dates == null){
			return null;
		}
    	for(Date iDate = beginDate; iDate.before(endDate) || iDate.equals(endDate);){
    		dates.add(toString(iDate, pattern));
	    	iDate = addDay(iDate, 15);
    	}
		return dates;
    }
    
    /**
     * 每周 【日期集】
     * @param pattern
     * @return
     */
    public static List<String> getEveryWeek(Date beginDate , Date endDate,String pattern){
    	List<String> dates = converterDate(beginDate, endDate, pattern);
		if(dates == null){
			return null;
		}
    	for(Date iDate = beginDate; iDate.before(endDate) || iDate.equals(endDate);){
    		dates.add(toString(iDate,pattern));
	    	iDate = addDay(iDate, 7);// 7天
    	}
		return dates;
    }
    
    /**
     * 每三天 【日期集】
     * @param pattern
     * @return
     */
    public static List<String> getEveryThreeDay(Date beginDate , Date endDate,String pattern){
    	List<String> dates = converterDate(beginDate, endDate, pattern);
		if(dates == null){
			return null;
		}
    	for(Date iDate = beginDate; iDate.before(endDate) || iDate.equals(endDate);){
    		dates.add(toString(iDate,pattern));
	    	iDate = addDay(iDate, 3);// 7天
    	}
		return dates;
    }
    
    public static List<String> converterDate(Date beginDate , Date endDate,String pattern){
    	List<String> dates=new ArrayList<String>();
    	if(pattern == null){
    		pattern = PATTERN_DATE_TIME;
    	}
		try {
			beginDate = toDate(toString(beginDate, pattern)+"-01 00:00:01", pattern);
			endDate = toDate(toString(endDate, pattern)+"-01 23:59:59", pattern);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return dates;
    }
    
    public static List<String> getDayList(Date beginDate , Date endDate) throws Exception{
    	List<String> dates = new ArrayList<String>();
    	beginDate = toDate(toShortString(beginDate)+" 00:00:01", PATTERN_DATE_TIME);
		endDate = toDate(toShortString(endDate)+" 23:59:59", PATTERN_DATE_TIME);
		
    	for(Date iDate = beginDate; iDate.before(endDate) || iDate.equals(endDate);){
    		dates.add(toShortString(iDate));
	    	iDate = addDay(iDate, 1);
    	}
		return dates;
    }
    
    /**
     * 加 月份
     * @param date
     * @param n 月份量
     * @return
     */
    public static Date addMonth(Date date,int n) {  
        return addTime(date, Calendar.MONTH, n);
    }
    
    /**
     * 加 天数
     * @param date
     * @param n 天数量
     * @return
     */
    public static Date addDay(Date date, int n) {  
        return addTime(date, Calendar.DATE, n);
    }
    
    /**
     * 加 小时
     * @param date
     * @param n 天数量
     * @return
     */
    public static Date addHour(Date date, int n) {  
        return addTime(date, Calendar.HOUR, n);
    }
    
    /**
     * 加 分钟
     * @param date
     * @param n 分钟数量
     * @return
     */
    public static Date addMinute(Date date, int n) {  
        return addTime(date, Calendar.MINUTE, n);
    }
    
    /**
     * @param date
     * @param type 类型 Calendar.{年/月/日/时} 
     * @param n 累加量
     * @return
     */
    private static Date addTime(Date date, int type, int n) {  
        Calendar time = Calendar.getInstance();  
        time.setTime(date);  
        time.add(type, n);  
        return time.getTime();  
    }
    
    /**
     * 比较2个日期是否属于同一月，同一天
     * @param date1
     * @param date2
     * @param dateType  月：M ，日：D
     * @return
     */
    public static boolean isSameDate(Date date1,Date date2,String dateType){
    	Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		if("M".equals(dateType)){
			int month1=cal1.get(Calendar.MONTH);//获取月份
			int month2=cal2.get(Calendar.MONTH);//获取月份
			if(month1 == month2){
				return true;
			}else{
				return false;
			}
			
		}else if("D".equals(dateType)){
			int day1=cal1.get(Calendar.DATE);//获取日
			int day2=cal2.get(Calendar.DATE);//获取日
			if(day1 == day2){
				return true;
			}else{
				return false;
			}
		}
		return false;
    }
    
    /**
	 * 比较 两个日期, 返回天数差
	 * @param beginDate 开始日期时间
	 * @param endDate 结束日期时间
	 * @return int
	 */
	public static int compareDay(Date beginDate, Date endDate) {
		Calendar endDateYears = new GregorianCalendar();
		endDateYears.setTime(endDate);
		Calendar beginYears = new GregorianCalendar();
		beginYears.setTime(beginDate);
		long diffMillis = endDateYears.getTimeInMillis() - beginYears.getTimeInMillis();
		return Long.valueOf(diffMillis / (24 * 60 * 60 * 1000)).intValue();
	}

	/**
	 * 比较两个日期, 返回天数差
	 * @param beginDate 开始日期时间
	 * @return int
	 */
	public static int compareDay(Date beginDate) {
		return compareDay(beginDate, new Date());
	}

	/**
	 * 比较两个日期, 返回 小时差
	 * @param beginDate 开始日期时间
	 * @return int
	 */
	public static int compareHour(Date beginDate) {
		return compareHour(beginDate, new Date());
	}

	/**
	 * 比较两个日期, 返回 小时差
	 * @param beginDate 开始日期时间
	 * @param endDate 结束日期时间
	 * @return int
	 */
	public static int compareHour(Date beginDate, Date endDate) {
		Calendar beginYears = new GregorianCalendar();
		beginYears.setTime(beginDate);
		long diffMillis = endDate.getTime() - beginYears.getTimeInMillis();
		return Long.valueOf(diffMillis / (60 * 60 * 1000)).intValue();
	}

	/**
	 * 比较两个日期, 返回 分钟差
	 * @param beginDate 开始日期时间
	 * @return int
	 */
	public static int compareMinute(Date beginDate) {
		return compareHour(beginDate, new Date());
	}

	/**
	 * 比较两个日期, 返回 分钟差
	 * @param beginDate 开始日期时间
	 * @param endDate 结束日期时间
	 * @return int
	 */
	public static int compareMinute(Date beginDate, Date endDate) {
		Calendar beginYears = new GregorianCalendar();
		beginYears.setTime(beginDate);
		long diffMillis = endDate.getTime() - beginYears.getTimeInMillis();
		return Long.valueOf(diffMillis / (60 * 1000)).intValue();
	}
	
	/**
	 * 比较两个日期, 返回 毫秒差
	 * @param beginDate 开始日期时间
	 * @return long
	 */
	public static long compareMillis(Date beginDate) {
		Calendar beginYears = new GregorianCalendar();
		beginYears.setTime(beginDate);
		return  System.currentTimeMillis() - beginYears.getTimeInMillis();
	}
	
	/**
	 * 比较两个日期, 返回 毫秒差
	 * @param beginDate 开始日期时间
	 * @param endDate 结束日期时间
	 * @return long
	 */
	public static long compareMillis(Date beginDate,Date endDate) {
		Calendar endDateYears = new GregorianCalendar();
		endDateYears.setTime(endDate);
		Calendar beginYears = new GregorianCalendar();
		beginYears.setTime(beginDate);
		long diffMillis = endDateYears.getTimeInMillis() - beginYears.getTimeInMillis();
		return  diffMillis;
	}
	
	/**
     * 根据时间类型比较时间大小 
     * 
     * @param source
     * @param traget
     * @param type "YYYY-MM-DD" "yyyyMMdd HH:mm:ss"  类型可自定义
     * @param 传递时间的对比格式
     * @return 
     *  0 ：source和traget时间相同    
     *  1 ：source比traget时间大  
     *  -1：source比traget时间小
     * @throws Exception
     */
    public static int dateCompare(String source, String traget, String type) throws Exception {
        int ret = 2;
        SimpleDateFormat format = new SimpleDateFormat(type);
        Date sourcedate = format.parse(source);
        Date tragetdate = format.parse(traget);
        ret = sourcedate.compareTo(tragetdate);
        return ret;
    }
    
    /**
     * 直接比较两个日期时间大小 
     * @param source
     * @param traget
     * @return
     *  0 ：source和traget时间相同    
     *  1 ：source比traget时间大  
     *  -1：source比traget时间小
     */
    public static int compareDate(Date source,Date traget){
        if (source.getTime() > traget.getTime()) {
            return 1;
        } else if (source.getTime() < traget.getTime()) {
            return -1;
        } else {//相等
            return 0;
        }
    }
    
    /**
     * 根据 秒钟 返回时间
     * @param second 秒钟
     * @return String
     */
    public static final String showTime(Integer second) {
    	int hourSeconds = 60 * 60;
		int daySeconds = 60 * 60 * 24;  
		int seconds = 60;
		if(second < seconds) {
			return second+"秒";
		}
		if(second < hourSeconds) {
			return second/seconds+"分钟";
		}else if(second < daySeconds){
			int remain = second%hourSeconds;
			if(remain == 0){
				return second/hourSeconds+"小时";
			}else{
				String remainTime = showTime(remain);
				return second/hourSeconds+"时"+remainTime;
			}
		}else{
			int remain = second%daySeconds;
			if(remain == 0){
				return second/daySeconds+"天";
			}else{
				String remainTime = showTime(remain);
				return second/daySeconds+"天"+remainTime;
			}
		}
	}
    
    /**
     * 生成范围数 eg: {1-3, 4-6, 7-9}
     * @param minRange 最小范围值
     * @param maxRange 最大范围值
     * @param avgNum 平均 间隔数
     * @return
     */
    public static Map<Integer, Integer> generateRangeNumber(int minRange, int maxRange, Integer avgNum){
    	Map<Integer, Integer> rangeNumbers = new LinkedHashMap<Integer, Integer>();
    	int index = minRange;
    	int max = maxRange-(avgNum-1);
    	while(index <= max){
    		rangeNumbers.put(index, (index-1)+avgNum);
    		index += avgNum;
    	}
		return rangeNumbers;
    }
    
    /**
     * 获得 偏移月 的时间点
     * @return Date
     */
    public static Date getMoveMonthDate(int amount){
    	return addMonths(new Date(), amount);
    }
    
    /**
     * 获得 偏移天 的时间点
     * @return Date
     */
    public static Date getMoveDayDate(int amount){
    	return addDays(new Date(), amount);
    }
    
    /**
     * 获得 偏移 的起始时间
     * @param rangeType 范围类型：one_week=最近一周, one_month=最近一个月, three_month=最近三个月, halfone_year=最近半年, one_year=最近一年
     * @return Date
     */
    public static Date getMoveBeginDate(String rangeType){
    	Date beginDate = null;
    	if("one_week".equals(rangeType)){//最近一周
			beginDate = DateUtil.getMoveDayDate(-7);
		}else if("one_month".equals(rangeType)){//最近一个月
			beginDate = DateUtil.getMoveMonthDate(-1);
		}else if("three_month".equals(rangeType)){//最近三个月
			beginDate = DateUtil.getMoveMonthDate(-3);
		}else if("halfone_year".equals(rangeType)){//最近半年
			beginDate = DateUtil.getMoveMonthDate(-6);
		}else if("one_year".equals(rangeType)){//最近一年
			beginDate = DateUtil.getMoveMonthDate(-12);
		}
    	return beginDate;
    }
    
    public static String getNow(Date date, int n) {
		Calendar begin = Calendar.getInstance();
		Date d = addTime(date, Calendar.DATE, n);
		begin.setTime(d);
		begin.set(Calendar.HOUR_OF_DAY, 0);
		begin.set(Calendar.MINUTE, 0);
		begin.set(Calendar.SECOND, 0);
		return dateTimeFormatter.format(begin.getTime());
	}
    
    public static void main(String[] args) {
    	Date beginDate = new Date();
    	try {
    		System.out.println(toString(beginDate, "yyyy-MM-dd"));
			beginDate = toDate(toString(beginDate, "yyyy-MM-dd")+"-01 00:00:01", "yyyy-MM-dd");
			System.out.println(dateTimeFormatter.format(beginDate));
			beginDate = toDate(toString(addDay(beginDate, -1), "yyyy-MM-dd")+"-01 00:00:01", "yyyy-MM-dd");
			System.out.println(beginDate);
			System.out.println(dateTimeFormatter.format(beginDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//System.out.println(toString(getMoveMonthDate(-1)));
    	//System.out.println(toString(getMoveDayDate(-1)));
    	
//    	Map<Integer, Integer> rangeDays = generateRangeNumber(1, 180, 10);
//    	for (Integer start : rangeDays.keySet()) {
//			System.out.println(start+"-"+rangeDays.get(start));
//		}
	}
}
