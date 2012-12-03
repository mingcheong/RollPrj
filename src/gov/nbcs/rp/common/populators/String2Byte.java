package gov.nbcs.rp.common.populators;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.Populator;

import java.lang.reflect.Field;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class String2Byte extends Populator {
    public Object populate(Object target,Field field,Object value, int arrayIndex, String format) throws Exception {
        String s = value.toString();
        if(Common.isNullStr(s)) {
			return null;
		}
        if(Formatters.isHexNumber(s) && (s.length()>1)) {
            return Byte.decode(s);
        }
        return new Byte((byte)s.charAt(0));
    }
}
