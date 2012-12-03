package gov.nbcs.rp.common.populators;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class String2Timestamp extends String2Date {
    public Object populate(Object target,Field field,Object value, int arrayIndex, String format)
    throws Exception {
        return new Timestamp(((Date)super.populate(target,field,value,arrayIndex,format)).getTime());
    }
}
