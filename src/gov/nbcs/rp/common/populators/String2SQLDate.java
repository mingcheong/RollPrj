package gov.nbcs.rp.common.populators;

import java.lang.reflect.Field;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class String2SQLDate extends String2Date {
    public Object populate(Object target,Field field,Object value, int arrayIndex, String format)
    throws Exception {
        return new java.sql.Date(((java.util.Date)super.populate(target,field,value,arrayIndex,format)).getTime());
    }
}
