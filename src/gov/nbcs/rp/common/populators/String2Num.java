package gov.nbcs.rp.common.populators;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.Populator;

import java.lang.reflect.Field;
import java.text.NumberFormat;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class String2Num extends Populator {
    public Object populate(Object target,Field field,Object value,int arrayIndex,String format)
    throws Exception {
        String s = value.toString();
        if(Common.isNullStr(s.trim())) {
            return null;
        }
        if(format!=null) {
            NumberFormat formatter = Formatters.getNumberFormat(format);
            return formatter.parse(s);
        }
        else if(Formatters.isFormattedNumber(s)) {
            return Formatters.getDefaultNumFormat().parse(s);
        }
        return new java.math.BigDecimal(s);
    }
}
