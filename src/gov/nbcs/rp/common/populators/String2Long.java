package gov.nbcs.rp.common.populators;

import gov.nbcs.rp.common.Common;

import java.lang.reflect.Field;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class String2Long extends String2Num {
    public Object populate(Object target,Field field,Object value, int arrayIndex, String format)
    throws Exception {
        String s = value.toString();
        if(Common.isNullStr(s)) {
			return null;
		}
        if(Formatters.isHexNumber(s)) {
			return Long.decode(s);
		}        
        return new Long(((Number)super.populate(target,field,value,arrayIndex,format)).longValue());
    }
}
