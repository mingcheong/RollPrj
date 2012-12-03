package gov.nbcs.rp.common.populators;

import gov.nbcs.rp.common.Populator;

import java.lang.reflect.Field;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Long2Date extends Populator {
    public Object populate(Object target,Field field,Object value, int arrayIndex, String format) throws Exception {
        return new java.util.Date(((Long)value).longValue());
    }
}
