package gov.nbcs.rp.common.populators;

import java.lang.reflect.Field;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class String2Double extends String2Num {
    public Object populate(Object target,Field field,Object value, int arrayIndex, String format) throws Exception {
        Number num = (Number)super.populate(target,field,value,arrayIndex,format);
        return new Double(num.doubleValue());
    }
}
