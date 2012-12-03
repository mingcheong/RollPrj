package gov.nbcs.rp.common.populators;

import gov.nbcs.rp.common.BeanUtil;
import gov.nbcs.rp.common.Populator;

import java.lang.reflect.Array;
import java.lang.reflect.Field;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Any2Array extends Populator {
    public Object populate(Object target,Field field,Object value, int arrayIndex, String format) throws Exception {
        Class targetType = field.getType().getComponentType();
        Populator pop = PopulatorFactory.getInstance().getPopulator(targetType,value.getClass());
        Object[] array = (Object[])BeanUtil.getValue(target,field.getName());
        if(array==null) {
            array = (Object[])Array.newInstance(targetType,arrayIndex+1);
        }
        else {
            if(array.length<=arrayIndex) {
                Object newArray[] = (Object[])Array.newInstance(targetType,arrayIndex+1);
                System.arraycopy(array,0,newArray,0,array.length);
                array = newArray;
            }
        }
        if(arrayIndex<0) {
			arrayIndex = 0;
		}
        array[arrayIndex] = pop.populate(target,field,value,format);
        return array;
    }
}
