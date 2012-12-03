package gov.nbcs.rp.common.populators;

import gov.nbcs.rp.common.Populator;

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Map;


/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PopulatorFactory {
    private static PopulatorFactory instance;
    
    private Map populatorCache = new Hashtable();
        
    public static PopulatorFactory getInstance() {
        return instance==null?instance=new PopulatorFactory():instance;
    }
    
    protected PopulatorFactory() {
        init();
    }
    
    public Populator getPopulator(Class target,Class src) {
        String targetTypeName = target.getName();
        String srcTypeName = src.getName(); 
        String key = targetTypeName+srcTypeName;
        Populator pop = (Populator)populatorCache.get(targetTypeName+srcTypeName);
        if(pop!=null) {
			return pop;
		}
        if(target.isArray() || src.isArray()) {
	        String _key = String.valueOf(target.isArray())+String.valueOf(src.isArray());
	        if(populatorCache.containsKey(_key)) {
	            return (Populator)populatorCache.get(_key);
	        }	        
        }
        if(isCompatibleType(target,src)) {
            pop = (Populator)populatorCache.get("compatible");
        }
        if(pop==null) {
            if(target.hashCode()==String.class.hashCode()) {
                pop = (Populator)populatorCache.get("toString"); 
            }
            else {
                pop = (Populator)populatorCache.get(targetTypeName+String.class.getName());
            }
        }
        if(pop!=null) {
			populatorCache.put(key,pop);
		}
        return pop;
    }
    
    private boolean isCompatibleType(Class targetType,Class srcType) {
        if ((targetType.hashCode() != Object.class.hashCode())
                && (targetType.hashCode() != srcType.hashCode())) {
            if (isSuperClass(targetType, srcType)) {
                return true;
            } else {
                Class[] intfs = srcType.getInterfaces();
                for (int i = 0; i < intfs.length; i++) {
                    if ((intfs[i].hashCode() == targetType.hashCode())
                            || isSuperClass(targetType, intfs[i])) {
                        return true;
                    }
                }
                return false;
            }
        }
        return true;
    }
    
    private boolean isSuperClass(Class targetType,Class srcType) {
        Class type = srcType.getSuperclass();
        for(;(type!=null) && (type.hashCode()!=targetType.hashCode());
        	type = type.getSuperclass()) {
			;
		}    
        return type!=null;
    }
    
    public void registerPopulator(Class target,Class src,Populator pop) {
        populatorCache.put(target.getName()+src.getName(),pop);
    }
    
    private void init() {        
        registerPopulator(java.util.Date.class,Long.class,new Long2Date());
        registerPopulator(java.util.Date.class,String.class,new String2Date());
        registerPopulator(java.sql.Date.class,String.class,new String2SQLDate());
        registerPopulator(java.sql.Timestamp.class,String.class,new String2Timestamp());
        registerPopulator(Double.class,String.class,new String2Double());
        registerPopulator(Float.class,String.class,new String2Float());
        registerPopulator(Integer.class,String.class,new String2Int());
        registerPopulator(java.math.BigDecimal.class,String.class,new String2Num());
        registerPopulator(Number.class,String.class,new String2Num());
        registerPopulator(String.class,Number.class,new Num2String());
        registerPopulator(String.class,Integer.class,new Num2String());
        registerPopulator(String.class,Float.class,new Num2String());
        registerPopulator(String.class,Double.class,new Num2String());
        registerPopulator(String.class,java.math.BigDecimal.class,new Num2String());    
        registerPopulator(String.class,java.util.Date.class,new Date2String());      
        registerPopulator(String.class,java.sql.Date.class,new Date2String());   
        registerPopulator(String.class,java.sql.Timestamp.class,new Date2String());
        registerPopulator(Short.class,String.class,new String2Short());
        registerPopulator(Long.class,String.class,new String2Long());  
        registerPopulator(Long.class,java.util.Date.class,new Date2Long());
        registerPopulator(Byte.class,String.class,new String2Byte());
        registerPopulator(Character.class,String.class,new String2Char());
        registerPopulator(Boolean.class,String.class,new String2Bool());
        //primitive type
        registerPopulator(double.class,String.class,new String2Double());
        registerPopulator(float.class,String.class,new String2Float());
        registerPopulator(int.class,String.class,new String2Int());
        registerPopulator(String.class,Number.class,new Num2String());
        registerPopulator(String.class,int.class,new Num2String());
        registerPopulator(String.class,float.class,new Num2String());
        registerPopulator(String.class,double.class,new Num2String());  
        registerPopulator(short.class,String.class,new String2Short());
        registerPopulator(long.class,String.class,new String2Long());  
        registerPopulator(long.class,java.util.Date.class,new Date2Long());
        registerPopulator(byte.class,String.class,new String2Byte());
        registerPopulator(char.class,String.class,new String2Char());
        registerPopulator(boolean.class,String.class,new String2Bool());        
        populatorCache.put("truetrue",new Array2Array());
        populatorCache.put("falsetrue",new Array2Any());
        populatorCache.put("truefalse",new Any2Array());
        populatorCache.put("toString",new Populator() {
            public Object populate(Object target,Field field, Object value, int arrayIndex, String format) throws Exception {
                return value==null?null:value.toString();
            }
        });
        populatorCache.put("compatible",new Populator() {
            public Object populate(Object target,Field field, Object value, int arrayIndex, String format) throws Exception {
                return value;
            }
        });
    }
}
