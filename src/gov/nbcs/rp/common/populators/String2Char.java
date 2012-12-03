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
public class String2Char extends Populator {
    public Object populate(Object target,Field field, Object value, int arrayIndex, String format) throws Exception {
        String s = value.toString();
        if(Common.isNullStr(s)) {
			return null;
		}
        return new Character(s.charAt(0));
    }
}
