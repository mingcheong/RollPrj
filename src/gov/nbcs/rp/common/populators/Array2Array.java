package gov.nbcs.rp.common.populators;

import gov.nbcs.rp.common.Populator;

import java.lang.reflect.Array;
import java.lang.reflect.Field;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Array2Array extends Populator {
    public Object populate(Object target,Field field, Object value, int arrayIndex, String format) throws Exception {
        Class targetType = field.getType().getComponentType();
        Class srcType = value.getClass().getComponentType();
        Populator pop = 
        PopulatorFactory.getInstance().getPopulator(targetType,srcType);
        Object [] srcValues = (Object[])value;
        if(arrayIndex<0) {
			arrayIndex=srcValues.length-1;
		}
        Object dstValues[] = (Object[])Array.newInstance(targetType,arrayIndex+1);
        for(int i=0;i<dstValues.length;i++) {
            dstValues[i] = 
            pop.populate(target,field,srcValues[i],format);
        }
        return dstValues;
    }
}
