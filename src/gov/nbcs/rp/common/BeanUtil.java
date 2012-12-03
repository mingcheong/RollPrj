/*
 * Created on 2006-2-5
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nbcs.rp.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class BeanUtil {
    /**
     * ��ÿ���Ѿ������������ֶ���Ϣ���л���
     */
    private static Map refFields = new HashMap();

    /**
     * ��ÿ���Ѿ����������෽��������л���
     */
    private static Map refMethods = new HashMap();
    
    /**
     * ÿ����ķ���������Ϣ����
     */
    private static Map methodNames = new HashMap();

    /**
     * getter������ǰ׺
     */
    private static final String GETTER_PREFIX = "get";

    /**
     * setter������ǰ׺
     */    
    private static final String SETTER_PREFIX = "set";

    public static void reset() {
        refFields.clear();
        refMethods.clear();
    }
        
    /**
     * �ɷ��������ж�һ�������Ƿ����
     * @param c �����
     * @param methodName ������
     * @return �жϽ��
     * @throws Exception ���ڲ���ȷ�Ĳ����ȵȣ����ܻ�������ָ�롢����δ���صȵ��쳣
     */
    public static boolean methodExist(Class c,String methodName)
    throws Exception {
        if(Common.isNullStr(methodName)) {
			return false;
		}
        if(!methodNames.containsKey(c)) {
	        Method methods[] = c.getMethods();
	        Set set = new HashSet();
	        for(int i=0;i<methods.length;i++) {
	            set.add(methods[i].getName());
	        }
	        methodNames.put(c,set);
        }
        return ((Set)methodNames.get(c)).contains(methodName);
    }

    /**
     * ���ֶ������ɸ��ֶε�setter��
     * 
     * @param fieldName
     * @return setter������
     */
    static String genSetMethodName(String fieldName) {
        return SETTER_PREFIX + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }

    /**
     * ���ֶ������ɸ��ֶε�getter��
     * 
     * @param fieldName
     * @return getter������
     */
    static String genGetMethodName(String fieldName) {
        return GETTER_PREFIX + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }

    /**
     * ȡ��һ��ʵ����ֶ�ֵ
     * 
     * @param obj
     *            ʵ�����
     * @param fieldName
     *            �ֶ���
     * @return �ֶ�ֵ
     * @throws Exception
     */
    public static Object getValue(Object obj, String fieldName)
            throws Exception {
        Class c = obj.getClass();
        Method method = getMethodObj(c, BeanUtil.GETTER_PREFIX, fieldName, null);
        if(method!=null) {
            return method.invoke(obj, null);
        }
        return null;
    }

    /**
     * overload
     */
    public static void setValue(Object obj, String fieldName, Object value)
            throws Exception {
        setValue(obj, fieldName, value, -1, null);
    }

    public static void setValue(Object obj, String fieldName, Object value,
            int arrayIndex, String format) throws Exception {
        Class c = obj.getClass();
        setValue(obj, c.getDeclaredField(fieldName), value, arrayIndex, format);
    }

    public static void setValue(Object obj, String fieldName, Object value,
            int arrayIndex) throws Exception {
        setValue(obj, fieldName, value, arrayIndex, null);
    }

    public static void setValue(Object obj, String fieldName, Object value,
            String format) throws Exception {
        setValue(obj, fieldName, value, -1, format);
    }

    public static void setValue(Object obj, Field field, Object value)
            throws Exception {
        setValue(obj, field, value, -1, null);
    }

    public static void setValue(Object obj, Field field, Object value,
            int arrayIndex) throws Exception {
        setValue(obj, field, value, arrayIndex, null);
    }

    public static void setValue(Object obj, Field field, Object value,
            String format) throws Exception {
        setValue(obj, field, value, -1, format);
    }

    /**
     * ����һ��ʵ����ֶ�ֵ
     * 
     * @param obj
     *            ʵ�����
     * @param field
     *            �ֶ�
     * @param arrayIndex
     *            ���Դ���������飬�����������±�Ĳ���
     * @param format
     *            ��������ڡ��������͵ĸ�ʽ������������ָ��format
     * @throws Exception
     */
    public static void setValue(Object obj, Field field, Object value,
            int arrayIndex, String format) throws Exception {
        Class c = obj.getClass();
        Class targetType = field.getType();
        Class srcType = value.getClass();
        Method method = getMethodObj(c, BeanUtil.SETTER_PREFIX,
                field.getName(), field.getType());
        if(method!=null) {
	        Populator pop  = Populator.getPopulator(targetType, srcType);
	        if(pop!=null) {
	            value = pop.populate(
	                obj,field, value, arrayIndex, format);
	        }
	        method.invoke(obj, new Object[] { value });
        }
    }

    private static Method getMethodObj(Class c, String type, String fieldName,
            Class fieldType) throws Exception {
        Method method = null;
        String methodCacheKey = type + fieldName;
        if (refMethods.containsKey(c)) {
            Map m = (Map) refMethods.get(c);
            if (m.containsKey(methodCacheKey)) {
				method = (Method) m.get(methodCacheKey);
			}
        }
        if (method == null) {
            if (type == BeanUtil.SETTER_PREFIX) {
                String methodName = genSetMethodName(fieldName);
                if(!methodExist(c,methodName)) {
					return null;
				}
                method = c.getMethod(methodName, new Class[] { fieldType });
            }
            else {
                String methodName = genGetMethodName(fieldName);
                if(!methodExist(c,methodName)) {
					return null;
				}
                method = c.getMethod(methodName, null);
            }
            Map m = new MyMap();
            m.put(methodCacheKey, method);
            refMethods.put(c, m);
        }
     
        return method;
    }

    /**
     * ��ȡ����ֶ���Ϣ��ע�ⲻ֧�ּ̳�
     * 
     * @param c
     * @return
     */
    public static Field[] getFields(Class c) {
        if (refFields.containsKey(c)) {
            return (Field[]) refFields.get(c);
        } else {
            Field[] fs = c.getDeclaredFields();
            List list = new LinkedList();
            for (int i = 0; i < fs.length; i++) {
                if (!fs[i].getName().equals("this$0")) {//����Inner-class���ⲿָ���Ա(һ����Ϊthis$0)
                    list.add(fs[i]);
                }
            }
            Field[] newFs = new Field[list.size()];
            System.arraycopy(list.toArray(), 0, newFs, 0, list.size());
            refFields.put(c, newFs);
            return newFs;
        }
    }

    /**
     * ��ȡ����ֶ���Ϣ��ע�ⲻ֧�ּ̳�
     * 
     * @param c
     * @return
     */
    public static Field[] getFields(Class c, boolean includeSuperClass) {
        return includeSuperClass ? getChainFields(c) : getFields(c);
    }

    /**
     * ��ȡ����ֶ���Ϣ��֧�ּ̳�
     * 
     * @param c
     * @return
     */
    static Field[] getChainFields(Class c) {
        String key = c.getName() + "_chain";
        if (refFields.containsKey(key)) {
            return (Field[]) refFields.get(key);
        } else {
            Field[] fs = new Field[0];
            for (; (c != null) && !c.getName().equals("Object"); c = c
                    .getSuperclass()) {
                Field[] sfs = c.getDeclaredFields();
                Field[] tmp = new Field[fs.length + sfs.length];
                System.arraycopy(fs, 0, tmp, 0, fs.length);
                System.arraycopy(sfs, 0, tmp, fs.length, sfs.length);
                fs = tmp;
            }
            refFields.put(key, fs);
            return fs;
        }
    }

    /**
     * ����һϵ��Map��������ָ���Ķ�������
     * 
     * @param dataList
     *            Map��������
     * @param entityClass
     *            ָ���Ķ���������
     * @param includeSuperClass
     *            �Ƿ�֧����̳�
     * @param arrayIndex
     *            ���Դ���������飬�����������±�Ĳ���
     * @param format
     *            ��������ڡ��������͵ĸ�ʽ������������ָ��format
     * @return ���ɵĶ�������
     * @throws Exception
     */
    public static Object[] mapping(Map[] dataList, Class entityClass,
            boolean includeSuperClass, int arrayIndex, String format)
            throws Exception {
        Object[] entities = new Object[dataList.length];
        for (int i = 0; i < dataList.length; i++) {
            entities[i] = entityClass.newInstance();
            mapping(entities[i], dataList[i], includeSuperClass, arrayIndex,
                    format);
        }
        return entities;
    }

    /**
     * overload
     */
    public static Object[] mapping(Map[] dataList, Class entityClass,
            boolean includeSuperClass, int arrayIndex) throws Exception {
        return mapping(dataList, entityClass, includeSuperClass, arrayIndex,
                null);
    }

    public static Object[] mapping(Map[] dataList, Class entityClass)
            throws Exception {
        return mapping(dataList, entityClass, false, -1, null);
    }

    public static Object[] mapping(Map[] dataList, Class entityClass,
            int arrayIndex) throws Exception {
        return mapping(dataList, entityClass, false, arrayIndex, null);
    }

    public static Object[] mapping(Map[] dataList, Class entityClass,
            String format) throws Exception {
        return mapping(dataList, entityClass, false, -1, format);
    }

    public static Object[] mapping(Map[] dataList, Class entityClass,
            boolean includeSuperClass, String format) throws Exception {
        return mapping(dataList, entityClass, includeSuperClass, -1, format);
    }

    public static Object[] mapping(Map[] dataList, Class entityClass,
            int arrayIndex, String format) throws Exception {
        return mapping(dataList, entityClass, false, -1, format);
    }

    public static Object[] mapping(Map[] dataList, Class entityClass,
            boolean includeSuperClass) throws Exception {
        return mapping(dataList, entityClass, includeSuperClass, -1, null);
    }    
    
    /**
     * ��һ��Map������ӳ�䵽��һ������������ֶ� ���磺 <br>
     * <code>
     * Map a = new Hashtable();<br>
     * a.put("ig",new Integer);<br>
     * class B {<br>
     *    private Number ig;<br>
     *    public void setIg(Number i){...}<br>
     *    public Number getIg(){...}<br>
     * }<br>
     * B b = new B();<br>
     * SQLAction.mapping(b,a);<br>
     * </code> ��ʱb�г�Ա����ig�����ú�a��ͬ������Ӧ��ֵ��ֵ��ע����� <br>
     * �����������Ŀ�������ֶ���Ϊ��׼ȥMap����������ͬ���ֶΣ��� <br>
     * ȡ����ֵ�����Map������ͬ���ֶ��������ϲ���Ҫ���Ŀ���ֶ���ͬ <br>
     * ���Ǳ�����Ŀ���ֶεļ������ͣ�ͨ�������ࣩ���߿���ת��������(�ο�@see Populator)
     * arrayIndex��format�����ο�
     * @see BeanUtil#setValue(Object, String, Object, int, String)
     */
    public static void mapping(Object entity, Map data,
            boolean includeSuperClass,int arrayIndex,
            String format) throws Exception {
        Class entityClass = entity.getClass();
        Field[] fields = getFields(entityClass, includeSuperClass);
        int i = 0;
        try {
            for (; i < fields.length; i++) {
                if (data.containsKey(fields[i].getName())) {
                    Object value = data.get(fields[i].getName());
                    if (value != null) {
                        setValue(entity, fields[i], value ,arrayIndex,format);
                    }
                }
            }
        } catch (Exception ex) {
            printException(data.getClass(), fields[i], entityClass, fields[i],
                    ex);
            throw ex;
        }
    }

    public static void mapping(Object entity, Map data) throws Exception {
        mapping(entity, data, false,-1,null);
    }
    
    public static void mapping(Object entity, Map data,boolean includeSuperClass)
    throws Exception {
        mapping(entity, data, includeSuperClass,-1,null);
    } 

    public static void mapping(Object entity, Map data,boolean includeSuperClass,int arrayIndex)
    throws Exception {
        mapping(entity, data, includeSuperClass,arrayIndex,null);
    } 
    
    public static void mapping(Object entity, Map data,boolean includeSuperClass,String format)
    throws Exception {
        mapping(entity, data, includeSuperClass,-1,format);
    }    
    
    public static void mapping(Object entity, Map data,int arrayIndex,String format)
    throws Exception {
        mapping(entity, data, false,arrayIndex,format);
    }    
    
    public static void mapping(Object entity, Map data,int arrayIndex)
    throws Exception {
        mapping(entity, data, false,arrayIndex,null);
    }     
    
    public static void mapping(Object entity, Map data,String format)
    throws Exception {
        mapping(entity, data, false,-1,format);
    }    

    /**
     * ��һ������������ֶ�ӳ�䵽��һ��Map������ mapping(Object entity,Map data)�������෴����
     */
    public static void decodeObject(Map data, Object entity,
            boolean includeSuperClass) throws Exception {
        Class entityClass = entity.getClass();
        Field[] fields = getFields(entityClass, includeSuperClass);
        int i = 0;
        try {
            for (; i < fields.length; i++) {
                Object value = getValue(entity, fields[i].getName());
                if (value != null) {
                    data.put(fields[i].getName(), value);
                }
            }
        } catch (Exception ex) {
            printException(entityClass, fields[i], data.getClass(), fields[i],
                    ex);
            throw ex;
        }
    }

    public static void decodeObject(Map data, Object entity) throws Exception {
        decodeObject(data, entity, false);
    }

    /**
     * ��һ��������ֶ�ӳ�䵽��һ������������ֶΣ����Դ�����������Ŀ�������ǵ�һ���� Ҫ���ж������ ���磺 <br>
     * <code>
     * class A {<br>
     *    private Integer ig;<br>
     *    public void setIg(Integer i){...}<br>
     *    public Integer getIg(){...}<br>
     * }<br>
     * class B {<br>
     *    private Number ig;<br>
     *    public void setIg(Number i){...}<br>
     *    public Number getIg(){...}<br>
     * }<br>
     * A a = new A();a.setIg(new Integer(10));<br>
     * B b = new B();<br>
     * SQLAction.mapping(b,a);<br>
     * </code> ��ʱb�г�Ա����ig�����ú�a��ͬ������һ����ֵ��ֵ��ע����� <br>
     * �����������Ŀ�������ֶ���Ϊ��׼ȥԴ����������ͬ���ֶΣ��� <br>
     * ȡ����ֵ�����Դ������ͬ���ֶ��������ϲ���Ҫ���Ŀ���ֶ���ͬ <br>
     * ���Ǳ�����Ŀ���ֶεļ������ͣ�ͨ�������ࣩ���߿���ת��������(�ο�@see Populator)
     */
    public static void copyProperties(Object dst, Object src,
            boolean includeDstSuperClass, boolean includeSrcSuperClass,
            int arrayIndex,String format) throws Exception {
        Class srcClass = src.getClass();
        Class dstClass = dst.getClass();
        Field dstFields[] = getFields(dstClass, includeDstSuperClass);
        Field srcFields[] = getFields(srcClass, includeSrcSuperClass);
        Map srcFldMap = new MyMap();
        for (int i = 0; i < srcFields.length; i++) {
            srcFldMap.put(srcFields[i].getName(), srcFields[i]);
        }
        int i = 0;
        try {
            for (; i < dstFields.length; i++) {
                if (srcFldMap.containsKey(dstFields[i].getName())) {
                    Field srcField = (Field) srcFldMap.get(dstFields[i]
                            .getName());
                    Object value = getValue(src, srcField.getName());
                    if (value != null) {
                        setValue(dst, dstFields[i], value,arrayIndex,format);
                    }
                }
            }
        } catch (Exception ex) {
            printException(srcClass, srcFields[i], dstClass, dstFields[i], ex);
            throw ex;
        }
    }

    public static void copyProperties(Object dst, Object src) throws Exception {
        copyProperties(dst, src, false, false, -1,null);
    }

    public static void copyProperties(Object dst, Object src,
            boolean includeDstSuperClass, boolean includeSrcSuperClass)
            throws Exception {
        copyProperties(dst, src, includeSrcSuperClass, includeSrcSuperClass, -1,null);
    }

    public static void copyProperties(Object dst, Object src, int arrayIndex,String format)
            throws Exception {
        copyProperties(dst, src, false, false, arrayIndex,format);
    }
    
    public static void copyProperties(Object dst, Object src, String format)
    throws Exception {
        copyProperties(dst, src, false, false, -1,format);
    } 
    
    public static void copyProperties(Object dst, Object src, int arrayIndex)
    throws Exception {
        copyProperties(dst, src, false, false, arrayIndex,null);
    }
    
    public static void copyProperties(Object dst, Object src,
            boolean includeDstSuperClass, boolean includeSrcSuperClass,int arrayIndex)
            throws Exception {
        copyProperties(dst, src, includeSrcSuperClass, includeSrcSuperClass, arrayIndex,null);
    } 
    
    public static void copyProperties(Object dst, Object src,
            boolean includeDstSuperClass, boolean includeSrcSuperClass,String format)
            throws Exception {
        copyProperties(dst, src, includeSrcSuperClass, includeSrcSuperClass, -1,format);
    }     

    public static void printException(Class src, Field srcFld, Class dst,
            Field dstFld, Exception ex) {
        String s_return = null;
        if ((ex instanceof IllegalArgumentException)
                || (ex instanceof ClassCastException)) {
            s_return = "Type mismatch:\n source--" + src.getName() + "."
                    + srcFld.getName() + "--" + srcFld.getType().getName()
                    + "\ndestination--" + dst.getName() + "."
                    + srcFld.getName() + "--" + dstFld.getType().getName();
        } else if (ex instanceof NoSuchMethodException) {
            s_return = "May be lost of setter or getter:\n source--"
                    + src.getName() + "." + genGetMethodName(srcFld.getName())
                    + "\ndestination--" + dst.getName() + "."
                    + genGetMethodName(dstFld.getName());
        } else {
            s_return = ex.toString();
        }
        System.out.println(s_return);
    }
}