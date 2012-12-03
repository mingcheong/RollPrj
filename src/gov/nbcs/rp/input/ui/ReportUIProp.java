/*
 * $Id: ReportUIProp.java,v 1.5 2010/03/10 05:52:00 Exp $
 * 
 * Copyright 2008 by Founder March 19, Inc. All rights reserved.
 */

package gov.nbcs.rp.input.ui;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.ErrorInfo;
import gov.nbcs.rp.common.FormatConvertor;
import gov.nbcs.rp.common.ui.report.cell.PropertyProvider;
import gov.nbcs.rp.input.action.PrjInputDTO;

import java.io.Serializable;
import java.text.Format;

import com.fr.cell.editor.CellEditor;




/**
 * <p>
 * Title:分组reportUI的属性类
 * </p>
 * <p>
 * Description:分组reportUI的属性类
 * </p>
 * CreateData 2011-1-30
 * </p>
 * 
 * @author 钱自成
 * @version 1.0
 */

public class ReportUIProp implements PropertyProvider,Serializable
{

	private static final long serialVersionUID = 1L;

	private PrjInputDTO dto;



	/**
	 * 属性类的构造函数
	 * 
	 * @param dsHeader
	 * @param dsBody
	 */
	public ReportUIProp(PrjInputDTO dto)
	{
		this.dto = dto;
	}


	/**
	 * 设置该单元格是否可编辑
	 * 
	 * @param bookmark
	 */
	public boolean isEditable(String bookmark, Object fieldId)
	{
		try
		{
			if (dto.getCurState() == -1) { return false; }
			if (dto.getDsBody().gotoBookmark(bookmark))
			{
				String supType = dto.getDsBody().fieldByName("SB_CODE").getString();

				if ("111".equalsIgnoreCase(supType) || "222".equalsIgnoreCase(supType))
				{
					if ("111".equalsIgnoreCase(supType))
						return false;
					// 周期性项目可以编辑总预算和已安排数
					if (!dto.isOneYearPrj())
					{
						if ("F2".equalsIgnoreCase(Common.nonNullStr(fieldId))) { return true; }
						if ("F3".equalsIgnoreCase(Common.nonNullStr(fieldId))) { return true; }
						if ("F6".equalsIgnoreCase(Common.nonNullStr(fieldId))) { return true; }
						if ("F8".equalsIgnoreCase(Common.nonNullStr(fieldId))) { return true; }
						if ("F9".equalsIgnoreCase(Common.nonNullStr(fieldId))) { return true; }
						if ("F10".equalsIgnoreCase(Common.nonNullStr(fieldId))) { return true; }
					}

					return false;
				}
				else
				{
					if ("TOTAL_SUM".equalsIgnoreCase(Common.nonNullStr(fieldId)))
						return false;
					if ("F1".equalsIgnoreCase(Common.nonNullStr(fieldId))) { return false; }

					if ("F7".equalsIgnoreCase(Common.nonNullStr(fieldId)))
						return false;
					if ("SB_TYPE".equalsIgnoreCase(Common.nonNullStr(fieldId)))
						return false;
					if ("333".equals(supType))
					{
						// 如果项目只有在本年执行，则本年预算不可编辑
						return false;
					}
					if ("222".equals(supType))
					{
						// 如果项目只有在本年执行，则本年预算不可编辑
						if (dto.isCurYearBegin())
							return false;
						else
						{
							if ("YSJC_MC".equalsIgnoreCase(Common.nonNullStr(fieldId)) || "ACCT_NAME".equalsIgnoreCase(Common.nonNullStr(fieldId))
									|| "ACCT_NAME_JJ".equalsIgnoreCase(Common.nonNullStr(fieldId)))
								return false;
							else
								return true;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			ErrorInfo.showErrorDialog(e, e.getMessage());
		}
		return true;
	}


	/**
	 * 获取该单元格格式
	 */
	public CellEditor getEditor(String bookmark, Object fieldId)
	{
		try
		{
			if ("YSJC_MC".equalsIgnoreCase(Common.nonNullStr(fieldId)))
				return dto.getYSJCFormat();
			if ("ACCT_NAME".equalsIgnoreCase(Common.nonNullStr(fieldId)))
				return dto.getAcctInfo();
			if ("ACCT_NAME_JJ".equalsIgnoreCase(Common.nonNullStr(fieldId)))
				return dto.getAcctJJInfo();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 获取列宽
	 */
	public double getColumnWidth(Object fieldId)
	{
		return 0;
	}


	/**
	 * 获取格式化格式 英文只有两个固定行为字符型，其他都是浮点型，所以有以下格式
	 */
	public Format getFormat(String bookmark, Object fieldId)
	{
		if ("F1".equalsIgnoreCase(Common.nonNullStr(fieldId)) || "F2".equalsIgnoreCase(Common.nonNullStr(fieldId)) || "F3".equalsIgnoreCase(Common.nonNullStr(fieldId))
				|| "F6".equalsIgnoreCase(Common.nonNullStr(fieldId)) || "F7".equalsIgnoreCase(Common.nonNullStr(fieldId)) || "F8".equalsIgnoreCase(Common.nonNullStr(fieldId))
				|| "F9".equalsIgnoreCase(Common.nonNullStr(fieldId)) || "F10".equalsIgnoreCase(Common.nonNullStr(fieldId)) || "TOTAL_SUM".equalsIgnoreCase(Common.nonNullStr(fieldId)))
			return FormatConvertor.getInstance().convertFormat("#,##0.00");
		else
			return null;
	}


	/**
	 * 获取显示名称
	 */
	public String getFieldName(Object fieldId)
	{
		String fieldname = "";
		try
		{
			// 定位表头，显示dsHeader中code字段对应的字段值
			if (dto.getDsHeader().locate("field_ename", fieldId.toString()))
				fieldname = dto.getDsHeader().fieldByName("field_ename").getValue().toString();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return fieldname;
	}


	/**
	 * 获取编码
	 */
	public Object getFieldId(String fieldName)
	{
		return fieldName;
	}

}
