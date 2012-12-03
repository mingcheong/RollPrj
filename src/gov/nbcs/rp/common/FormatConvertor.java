/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */
package gov.nbcs.rp.common;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FormatConvertor {
	private static FormatConvertor inst;
	
	private FormatConvertor() {}
	
	private Pattern numPattern = Pattern.compile("#+.+;;.*\\s*",Pattern.CASE_INSENSITIVE);
    
    private Pattern numPattern1 = Pattern.compile("[#,\\.0]+");
	
	private Pattern dateTimePattern = 
Pattern.compile(
".*((y{2,4})*.+(m{1,2})*.+(d{1,2})*)*.*(h{1,2})+.+(m{1,2})*.+.+(s{1,2})*.*",
Pattern.CASE_INSENSITIVE);
	
	private Pattern datePattern = 
		Pattern.compile(
		".*(y{2,4})*.+(m{1,2})*.+(d{1,2})+.*",
		Pattern.CASE_INSENSITIVE);	
	
	private String dateSplit="[^yYmMdDhHmMsS]";
	private String nonDateSplit="(y|Y{2,4})+|(m|M{1,2})+|(d|D{1,2})+|(h|H{2,4})+|(m|M{2,4})+|(s|S{2,4})+";
	
	private Pattern yPattern = Pattern.compile("(y{2,4})+",Pattern.CASE_INSENSITIVE);
	private Pattern mPattern = Pattern.compile("(m{1,2})+",Pattern.CASE_INSENSITIVE);
	private Pattern dPattern = Pattern.compile("(d{1,2})+",Pattern.CASE_INSENSITIVE);
	private Pattern hPattern = Pattern.compile("(h{1,2})+",Pattern.CASE_INSENSITIVE);
	private Pattern sPattern = Pattern.compile("(s{1,2})+",Pattern.CASE_INSENSITIVE);
	
	private Map patternMap = new MyMap();
	private Map formatMap = new MyMap();
	
	public static FormatConvertor getInstance() {
		return inst == null?inst = new FormatConvertor():inst;
	}
	
	public boolean isNumberFormat(String format) {
		return numPattern.matcher(format).matches() || numPattern1.matcher(format).matches();
	}
	
	public boolean isDateFormat(String format) {
		return datePattern.matcher(format).matches();
	}
	
	public String convert(String format) {
		if(patternMap.containsKey(format)) {
			return patternMap.get(format).toString();
		}
		else {
			String _format = null;
			if(numPattern.matcher(format).matches()) {
				_format = format.split(";;")[0];
			}
			else if(datePattern.matcher(format).matches()) {
				_format = processDateFormat(format);
			}
			else if(dateTimePattern.matcher(format).matches()) {
				_format = processDateFormat(format);
			} else {
				_format = format;
			}
			patternMap.put(format,_format);
			return _format;
		}
	}
	
	public Format convertFormat(String _format) {
		String format = convert(_format);
		if(formatMap.containsKey(format)) {
			return (Format)formatMap.get(format);
		}
		Format fm  = null;
		if(isNumberFormat(_format)) {
			fm = new DecimalFormat(format);
		}
		else if(isDateFormat(_format)) {
			fm = new SimpleDateFormat(format);
		}
		if(fm!=null) {
			formatMap.put(format,fm);
			return fm;
		}
		return null;
	}
	
	protected String processDateFormat(String format) {
		String sd [] = format.split(dateSplit); 
		String snd [] = format.split(nonDateSplit);
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<sd.length;i++) {
			if(this.yPattern.matcher(sd[i]).matches()) {
				sd[i] = sd[i].replaceAll("Y","y");
			}
			else if(this.mPattern.matcher(sd[i]).matches()) {
				if(((i+1<sd.length-1) && this.dPattern.matcher(sd[i+1]).matches())
				|| ((i-1>=0) && this.yPattern.matcher(sd[i-1]).matches())) {
					sd[i] = sd[i].replaceAll("m","M");
				}		
				else if(((i+1<sd.length-1) && this.sPattern.matcher(sd[i+1]).matches())
				|| ((i-1>=0) && this.hPattern.matcher(sd[i-1]).matches())) {
					sd[i] = sd[i].replaceAll("M","m");
				}					
			}
			else if(this.dPattern.matcher(sd[i]).matches()) {
				sd[i] = sd[i].replaceAll("D","d");
			}	
			else if(this.hPattern.matcher(sd[i]).matches()) {
				sd[i] = sd[i].replaceAll("h","H");
			}
			else if(this.sPattern.matcher(sd[i]).matches()) {
				sd[i] = sd[i].replaceAll("S","s");
			}
		}
		for(int i=0;i<snd.length;i++) {
			sb.append(snd[i]);
			if(i<=sd.length-1) {
				sb.append(sd[i]);
			}
		}
		return sb.toString();
	}
}
