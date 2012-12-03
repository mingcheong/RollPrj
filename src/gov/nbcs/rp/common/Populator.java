package gov.nbcs.rp.common;

import gov.nbcs.rp.common.populators.PopulatorFactory;

import java.lang.reflect.Field;


/**
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class Populator {
    /**
     * ������ֵת��Ϊ���ʵ�����
     * 
     * @param target
     *            Ŀ������
     * @param value
     *            ����ֵ
     * @param arrayIndex
     *            �����������ȡ�ض��±�Ĳ�������ָ����������-1��
     * @param format
     *            ���ݵĸ�ʽ��
     * @return
     */
    public abstract Object populate(Object target,Field field,Object value,int arrayIndex,String format)
    throws Exception ;
    
    public final Object populate(Object target,Field field,Object value,String format) throws Exception {
        return populate(target,field,value,-1,format);
    }
    
    public final Object populate(Object target,Field field,Object value,int arrayIndex) throws Exception {
        return populate(target,field,value,arrayIndex,null);
    }    
    
    public final Object populate(Object target,Field field,Object value) throws Exception {
        return populate(target,field,value,-1,null);
    } 
    
    public static Populator getPopulator(Class targetType,Class srcType) {
        return PopulatorFactory.getInstance().getPopulator(targetType,srcType);
    }

	public static void main(String args[]) throws Exception {
//        java.sql.Timestamp i[] = new java.sql.Timestamp[] {};
//        java.util.Date s[] = { new java.util.Date(), new java.util.Date() };
//        Populator pop = Populator.getPopulator(i.getClass(), s.getClass());
//
//        Object obj = pop.populate(i.getClass(), s);
//        i = (java.sql.Timestamp[]) obj;
//        for (int j = 0; j < i.length; j++) {
//            System.out.println(i[j]);
//        }
//        System.out.println("class=" + obj.getClass().getName() + " value="
//                + obj);
    }
}
