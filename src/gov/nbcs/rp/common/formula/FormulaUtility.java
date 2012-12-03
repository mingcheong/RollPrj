package gov.nbcs.rp.common.formula;

/**
 * @author 谢昀荪
 * 
 * @version 创建时间：Apr 10, 20123:32:51 PM
 * 
 * @Description
 */
/**
 * Copyright 江苏方正春元 版权所有 部门预算子系统
 * 
 * @author 汪佩雄
 * @version 1.0
 */
import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.FormulaTool;
import gov.nbcs.rp.common.MyHashSet;
import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.Field;
import gov.nbcs.rp.common.datactrl.event.DataChangeEvent;
import gov.nbcs.rp.common.datactrl.event.DataChangeListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;




public class FormulaUtility
{
	static final String ROW_FORMULA_ELEMENT_SYMBOL = "#ROW_FORMULA_ELEMENT_SYMBOL#";

	static final Pattern rowFormulaElementPattern = Pattern.compile(".+" + ROW_FORMULA_ELEMENT_SYMBOL + ".+");

	static final Pattern rowFormulaElementSplitter = Pattern.compile(ROW_FORMULA_ELEMENT_SYMBOL);

	private boolean negativeIsZero = false;



	public boolean isNegativeIsZero()
	{
		return negativeIsZero;
	}


	public void setNegativeIsZero(boolean negativeIsZero)
	{
		this.negativeIsZero = negativeIsZero;
	}


	/**
	 * 组织相关DataSet跨行计算的公式元素名字
	 * 
	 * @param bookmark
	 *            行标书签
	 * @param element
	 *            公式计算元素名
	 * @return
	 */
	public static final String genRowFormulaElementName(String bookmark, String elementName)
	{
		return bookmark + ROW_FORMULA_ELEMENT_SYMBOL + elementName;
	}


	/**
	 * 从跨行的公式元素名字中提取出元素本身的名字
	 */
	public static final String pickUpElementName(String rowFormulaElementName)
	{
		if (rowFormulaElementPattern.matcher(rowFormulaElementName).matches()) { return rowFormulaElementSplitter.split(rowFormulaElementName)[1]; }
		return rowFormulaElementName;
	}


	/**
	 * 从跨行的公式元素名字中提取出行标书签
	 */
	public static final String pickUpBookmark(String rowFormulaElementName)
	{
		if (rowFormulaElementPattern.matcher(rowFormulaElementName).matches()) { return rowFormulaElementSplitter.split(rowFormulaElementName)[0]; }
		return null;
	}



	DataSet ds;

	/**
	 * 以公式对象作为键值，所有关联字段作为值
	 */
	Map formulaAssoc = new MyMap();

	/**
	 * 和绑定的DataSet相关的公式集合
	 */
	TreeSet formulaSet = new TreeSet(new FormulaComparator());

	Set formulaElements = new MyHashSet();

	/**
	 * 公式计算启动的开关
	 */
	private boolean maskRecalcute = false;



	public void maskRecalcute(boolean maskRecalcute)
	{
		this.maskRecalcute = maskRecalcute;
	}


	public FormulaUtility(DataSet ds)
	{
		this.ds = ds;
		this.ds.addDataChangeListener(new DataChangeListener()
		{// 记录变化的时候根据需要进行公式计算

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;



					/*
					 * (non-Javadoc)
					 * 
					 * @see com.foundercy.fiscalbudget.common.datactrl.event.DataChangeListener#onDataChange(com.foundercy.fiscalbudget.common.datactrl.event.DataChangeEvent)
					 */
					public void onDataChange(DataChangeEvent event) throws Exception
					{
						if (event.type() == DataChangeEvent.FIELD_MODIRED && !maskRecalcute)
						{// 如果是字段值修改的事件
							Field field = (Field) event.getSource();
							String fieldEName = field.getName();
							if (field.isLatestModified())
							{
								startCalculate(fieldEName);
							}
						}
					}
				});
	}


	/**
	 * Starts the calculate. 由指定fieldEname开始进行列公式计算
	 * 
	 * @param fieldEName
	 *            the field e name
	 * @throws Exception
	 *             the exception
	 */
	protected void startCalculate(String fieldEName) throws Exception
	{
		// 计算本字段直接参与的公式，获得所有受影响字段
		List resultRowFields = recalculate(fieldEName);// 列公式计算

		String rowFormulaEleName = FormulaUtility.genRowFormulaElementName(FormulaUtility.this.ds.toogleBookmark(), fieldEName);
		for (Iterator itr = resultRowFields.iterator(); itr.hasNext();)
		{
			String s = (String) itr.next();
			// 排除本身
			if (!s.equals(rowFormulaEleName))
			{
				s = s.substring(s.lastIndexOf("#") + 1);
				recalculate(s);
			}
		}
	}


	/**
	 * 由一个字段开始触发，计算所有公式
	 * 
	 * @return 计算过程中影响的行公式字段
	 */
	protected List recalculate(String fieldName) throws Exception
	{
		List resultRowFields = new ArrayList();
		// 公式元素名
		String rowFormulaEleName = FormulaUtility.genRowFormulaElementName(ds.toogleBookmark(), fieldName);
		resultRowFields.add(rowFormulaEleName);
		if (formulaElements.contains(fieldName))
		{
			// 非固定行公式
			calc(resultRowFields, fieldName);
		}
		if (formulaElements.contains(rowFormulaEleName))
		{
			// 固定行公式
			calc(resultRowFields, rowFormulaEleName);
		}
		return resultRowFields;
	}


	/**
	 * Calc. 计算指定字段
	 * 
	 * @param resultRowFields
	 *            the result row fields
	 * @param fieldName
	 *            the field name
	 * @throws Exception
	 *             the exception
	 */
	private void calc(List resultRowFields, String fieldName) throws Exception
	{
		Collection resultFields = new HashSet();
		resultFields.add(fieldName);
		for (Iterator it = formulaSet.iterator(); it.hasNext();)
		{
			FormulaObject fo = (FormulaObject) it.next();
			if (containsFieldList(fo, resultFields))
			{
				Map values = getFormulaValues(fo);
				// 结果字段名
				String resultFieldName = fo.getResultField();// 下次循环用本次结果字段名去看是否有相关公式以本次计算结果为参数的
				Object result = null;
				if (negativeIsZero)
				{
					// 计算元素中有负值，则结果置为-1
					boolean hasNagetive = hasNegative(values);
					if (hasNagetive)
					{
						result = new BigDecimal(-1);
					}
					else
					{
						result = calcFormula(fo, values);
					}
				}
				else
				{
					result = calcFormula(fo, values);
				}
				writeResult(resultFieldName, result);
				// Common.consoleLogger().debug(ds
				// .toogleBookmark() + "|" + fieldName + ":=" + result);
				resultRowFields.add(FormulaUtility.genRowFormulaElementName(ds.toogleBookmark(), resultFieldName));
				resultFields.add(resultFieldName);
			}
		}
	}


	/**
	 * Checks for negative.
	 * 
	 * @param values
	 *            the values
	 * @return true, if successful
	 */
	private boolean hasNegative(Map values)
	{
		for (Iterator valItr = values.values().iterator(); valItr.hasNext();)
		{
			Object val = (Object) valItr.next();
			if (val instanceof BigDecimal)
			{
				BigDecimal dec = (BigDecimal) val;
				if (dec.compareTo(UntPub.ZERO_DECIMAL) < 0) { return true; }
			}
			else if (val instanceof Double)
			{
				Double dbl = (Double) val;
				if (dbl.doubleValue() < 0) { return true; }
			}
			else if (val instanceof Integer)
			{
				Integer integer = (Integer) val;
				if (integer.intValue() < 0) { return true; }
			}
		}
		return false;
	}


	/**
	 * Contains field list.
	 * 
	 * @param fo
	 *            the fo
	 * @param fields
	 *            the fields
	 * @return true, if successful
	 */
	protected boolean containsFieldList(FormulaObject fo, Collection fields)
	{
		for (Iterator it = fields.iterator(); it.hasNext();)
		{
			if (((Set) formulaAssoc.get(fo)).contains(it.next())) { return true; }
		}
		return false;
	}


	/**
	 * 从一个数据集中生成一条公式
	 * 
	 * @parem formulaData 含有公式信息的数据集
	 * @param resultField
	 *            计算结果字段
	 * @param expressionField
	 *            公式表达式
	 * @param priorityField
	 *            公式优先级
	 */
	public static FormulaObject createFormulaObject(final String resultField, final String expression, final int priority)
	{
		return new FormulaObjectImpl(resultField, expression, priority);
	}


	/**
	 * 将计算结果写回到DataSet中去
	 */
	protected void writeResult(String fieldName, Object result) throws Exception
	{
		this.maskRecalcute(true);
		String bookmark = pickUpBookmark(fieldName);
		String elementName = pickUpElementName(fieldName);
		if (bookmark != null)
		{
			ds.maskDataChange(true);
			String tmpBookmark = ds.toogleBookmark();
			ds.gotoBookmark(bookmark);
			ds.maskDataChange(false);
			ds.fieldByName(elementName).setValue(result);
			ds.maskDataChange(true);
			ds.gotoBookmark(tmpBookmark);
			ds.maskDataChange(false);
		}
		else
		{
			ds.fieldByName(elementName).setValue(result);
		}
		this.maskRecalcute(false);
	}


	/**
	 * 获取公式计算需要的各个元素对应值
	 */
	protected Map getFormulaValues(FormulaObject fo) throws Exception
	{
		Map values = new MyMap();
		Set assocFields = (Set) this.formulaAssoc.get(fo);
		for (Iterator it = assocFields.iterator(); it.hasNext();)
		{
			String fieldName = (String) it.next();
			String bookmark = pickUpBookmark(fieldName);
			String elementName = pickUpElementName(fieldName);
			if (bookmark != null)
			{
				ds.maskDataChange(true);
				String tmpBookmark = ds.toogleBookmark();
				ds.gotoBookmark(bookmark);
				Object value = ds.fieldByName(elementName).getValue();
				values.put(fieldName, value);
				ds.gotoBookmark(tmpBookmark);
				ds.maskDataChange(false);
			}
			else
			{
				Object value = ds.fieldByName(elementName).getValue();
				values.put(fieldName, value);
			}
		}
		return values;
	}


	/**
	 * Process value.
	 * 
	 * @param value
	 *            the value
	 * @return the object
	 */
	private Object processValue(Object value)
	{
		Object res;
		if (value instanceof BigDecimal)
		{
			BigDecimal dec = (BigDecimal) value;
			if (dec.compareTo(UntPub.ZERO_DECIMAL) < 0)
			{
				dec = new BigDecimal(0.0);
			}
			res = dec;
		}
		else if (value instanceof Double)
		{
			Double dbl = (Double) value;
			if (dbl.doubleValue() < 0)
			{
				dbl = new Double(0.0);
			}
			res = dbl;
		}
		else if (value instanceof Integer)
		{
			Integer integer = (Integer) value;
			if (integer.intValue() < 0)
			{
				integer = new Integer(0);
			}
			res = integer;
		}
		else
		{
			res = value;
		}
		return res;
	}


	/**
	 * 得到优先级的值最大（优先级最低）的公式
	 * 
	 * @return
	 */
	public FormulaObject getLastFormula()
	{
		return (FormulaObject) formulaSet.last();
	}


	/**
	 * 得到优先级的值最小（优先级最高）的公式
	 * 
	 * @return
	 */
	public FormulaObject getFirstFormula()
	{
		return (FormulaObject) formulaSet.first();
	}


	/**
	 * 计算一条公式
	 * 
	 * @param fo
	 */
	protected Object calcFormula(FormulaObject fo, Map values) throws Exception
	{
		Object result = FormulaTool.getValue(fo.getExpression(), values);
		values.put(fo.getResultField(), result);// 将本条公式的计算结果也加入到计算公式需要的字段值映射中去，
		// 这样下一条和本次计算结果有关的公式就可以用本次结果进行计算
		return result;
	}


	/**
	 * 添加一个公式对象
	 * 
	 * @param formulaObj
	 */
	public void addFormula(FormulaObject formulaObj)
	{
		String formula = formulaObj.getExpression();
		String formulaElementArray[] = Common.splitString(formula);
		Collection formulaElementSet = Arrays.asList(formulaElementArray);
		formulaElements.addAll(formulaElementSet);
		formulaAssoc.put(formulaObj, new MyHashSet(formulaElementSet));
		formulaSet.add(formulaObj);
	}



	/**
	 * 公式优先级排序需要的比较工具
	 */
	class FormulaComparator implements Comparator
	{
		public int compare(Object o1, Object o2)
		{
			int i1 = ((FormulaObject) o1).getPriority();
			int i2 = ((FormulaObject) o2).getPriority();
			return i1 >= i2 ? 1 : -1;
		}
	}

}
