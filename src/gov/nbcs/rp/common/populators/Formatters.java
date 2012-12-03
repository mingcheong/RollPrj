package gov.nbcs.rp.common.populators;

import java.text.*;
import java.util.*;
import java.util.regex.Pattern;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Formatters {
    private static Formatters formatters;
        
    static {
        formatters = new Formatters();   
    }
    
    private DateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private NumberFormat defaultNumFormat = new DecimalFormat("#,###,00");
    
    private Hashtable formatCache = new Hashtable();
    
    private static Pattern hexPattern = Pattern.compile("0x[0-F]+",Pattern.CASE_INSENSITIVE);
    private static Pattern formattedPattern = Pattern.compile("(\\+|\\-)?(\\d{0,2}\\d,)+\\d{3}(\\.\\d+)*");
    
    public static DateFormat getDateFormat(String format) {
        DateFormat df = null;
        if(formatters.formatCache.containsKey(format)) {
            df = (DateFormat)formatters.formatCache.get(format);
        }
        else {
	        df = new SimpleDateFormat(format);
	        formatters.formatCache.put(format,df);
        }
        return df;
    }
    
    public static DateFormat getDefaultDateFormat() {
        return formatters.defaultFormat;
    }
    
    public static NumberFormat getDefaultNumFormat() {
        return formatters.defaultNumFormat;
    }
    
    public static boolean isHexNumber(String s) {
        return hexPattern.matcher(s).matches();
    }
    
    public static boolean isFormattedNumber(String s) {
        return formattedPattern.matcher(s).matches();
    }
    
    public static NumberFormat getNumberFormat(String format) {
        NumberFormat nf = null;
        if(formatters.formatCache.containsKey(format)) {
            nf = (NumberFormat)formatters.formatCache.get(format);
        }
        else {
	        nf = new DecimalFormat(format);
	        formatters.formatCache.put(format,nf);
        }
        return nf;
    }
}
