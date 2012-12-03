package gov.nbcs.rp.common.populators;

import gov.nbcs.rp.common.Populator;

import java.lang.reflect.Field;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Array2Any extends Populator {
    public Object populate(Object target,Field field, Object value, int arrayIndex, String format) throws Exception {
        Class targetType = field.getType();
        Class srcType = value.getClass().getComponentType();
        Populator pop = PopulatorFactory.getInstance().getPopulator(targetType,srcType);
        if(arrayIndex<0) {
			arrayIndex=0;
		}
        return pop.populate(target,field,((Object[])value)[arrayIndex],format);
    }
}
