package gov.nbcs.rp.common.populators;

import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.Populator;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class String2Date extends Populator {
    private String srcFormatString0 = "yyyyMMddHHmmss";
    private DateFormat srcFormat0 = new SimpleDateFormat(srcFormatString0);
    private String srcFormatString1 = "yyyyMMdd";
    private DateFormat srcFormat1 = new SimpleDateFormat(srcFormatString1);    
    private Pattern gmtTime = //sample: 2 Sep 2006 15:02:30 GMT
        Pattern.compile("\\d{1,2}\\D+\\d{4}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2} GMT",Pattern.CASE_INSENSITIVE);
   
    private Pattern cstTime = //Sample:Sat Sep 02 23:02:30 CST 2006
        Pattern.compile("\\D+\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2} CST \\d{4}",Pattern.CASE_INSENSITIVE);
    
    private Pattern timePattern = Pattern.compile("\\d+");
    
    private Map monthConvert = new MyMap();
    
    String2Date() {
        monthConvert.put("Jan","01");monthConvert.put("Fab","02");
        monthConvert.put("Mar","03");monthConvert.put("Apr","04");
        monthConvert.put("May","05");monthConvert.put("Jun","06");
        monthConvert.put("Jul","07");monthConvert.put("Aug","08");
        monthConvert.put("Sep","09");monthConvert.put("Oct","10");
        monthConvert.put("Nov","11");monthConvert.put("Dec","12");
    }
    
    /**
     * 转换String类型到Date类型，如果不指定format，将会自动判定，务必保证提供6个日期元素
     * 年、月、日、时、分、秒
     */
    public Object populate(Object target,Field field,Object value, int arrayIndex, String format)
    throws Exception {
        if(value!=null) {
            if(format!=null) {
                return Formatters.getDateFormat(format).parse(value.toString());
            }
            String s = value.toString();
            if(gmtTime.matcher(s).matches()) {
                s = gmtTimeString(s);
            }
            else if(cstTime.matcher(s).matches()) {
                s = cstTimeString(s);
            }
            else if(timePattern.matcher(s).matches()) {
                return new java.util.Date(Long.parseLong(s));
            }
            else {
                s = genericTimeString(s);
                if(s.length()<srcFormatString0.length()) {
                    return srcFormat1.parse(s);
                }
            }
            return srcFormat0.parse(s);        
        }
        return null;
    }
    
    private String genericTimeString(String date) {
        String dateElements[] = date.split("\\D");
        String s = dateElements[0];
        for(int i=1;i<dateElements.length;i++) {
            if(dateElements[i].length()==0) {
				continue;
			}
            if(dateElements[i].length()<2) {
				dateElements[i] = 0+dateElements[i];
			}
            s += dateElements[i];
        }     
        return s;
    }
    
    private String gmtTimeString(String date) {
        String dateElements[] = date.split("\\s+|:");
        String s = dateElements[2];//year
        s += monthConvert.get(dateElements[1]);//month
        if(dateElements[0].length()<2) {
            dateElements[0] = 0+dateElements[0];
        }
        s += dateElements[0];
        for(int i=3;i<6;i++) {
            if(dateElements[i].length()<2) {
                dateElements[i] = 0+dateElements[i];            
            }            
            s += dateElements[i];           
        }
        return s;
    }
    
    private String cstTimeString(String date) {
        String dateElements[] = date.split("\\s+|:");
        String s = dateElements[7];//year
        s += monthConvert.get(dateElements[1]);//month
        for(int i=2;i<6;i++) {
            if(dateElements[i].length()<2) {
                dateElements[i] = 0+dateElements[i];            
            }
            s += dateElements[i];
        }
        return s;
    }    
}
