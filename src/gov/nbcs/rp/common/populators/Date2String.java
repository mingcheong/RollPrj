package gov.nbcs.rp.common.populators;

import gov.nbcs.rp.common.Populator;

import java.lang.reflect.Field;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Date2String extends Populator {
    public Object populate(Object target,Field field,Object value, int arrayIndex, String format) throws Exception {
        if(value!=null) {
            if(format!=null) {
                return Formatters.getDateFormat(format).format(value);
            }
            return Formatters.getDefaultDateFormat().format(value);
        }
        return null;
    }
}
