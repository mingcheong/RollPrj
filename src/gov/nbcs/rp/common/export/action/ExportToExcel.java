package gov.nbcs.rp.common.export.action;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Alignment;
import jxl.write.Blank;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.WritableFont.FontName;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.datactrl.TableColumnInfo;
import gov.nbcs.rp.common.export.ibs.IExport;
import gov.nbcs.rp.common.export.reporttypes.REDsLst;
import gov.nbcs.rp.common.export.reporttypes.RELikeFace;
import gov.nbcs.rp.common.export.reporttypes.RESql;
import gov.nbcs.rp.common.export.reporttypes.ReportType;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.report.Report;
import gov.nbcs.rp.common.ui.report.ReportUI;
import gov.nbcs.rp.common.ui.report.TableHeader;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.fr.base.Constants;
import com.fr.report.io.ExcelExporter;

/**
 * <p>
 * Title:导出主类,提供接口供选用
 * </p>
 * <p>
 * Description:各种导出接口
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008 浙江易桥有限公司
 * </p>
 * <p>
 * Company: 浙江易桥有限公司
 * </p>
 * <p>
 * CreateData 2011-4-7
 * </p>
 * 
 * @author 钱自成
 * @version 1.0
 */

public class ExportToExcel {

	/** 大标题 */
	String tag_Title = "begin大标题end";

	/** 单位名称 */
	String tag_DivName = "begin单位名称end";

	/** 数量单位 */
	String tag_Unit = "begin货币单位end";

	/** 数据 */
	String tag_Data = "begin数据end";

	/** 数据起始行 */
	int iHeaderRow = -1;

	/** 数据内容单元格的格式 */
	CellFormat[] colFormats;

	/** 接口 */
	private IExport itserv = ExportStub.getMethod();

	/** 导出操作类 */
	private ExportAction eact = new ExportAction();

	/** 导出的Excel文件 */
	private String sFilePath = null;

	/** 文件选择器 */
	private JFileChooser fileChooser;

	/** 临时文件 */
	private String tempFile = "C:\\temp.xls";
	{
		String userHomePath = System.getProperty("user.home");
		tempFile = userHomePath + "\\temp.xls";
	}

	/** 导出文件表头的最后一列 */
	private Object[] tempLastCols = null;

	// //////////////////////////////////////////////单表导出///////////////////////////////////////////////

	/** 导出辅助类 */
	private ExportProp exprop;

	/** 报表编码 */
	private String reportID;

	/** 生成表头的NODE */
	private Node node;

	/** 大标题 */
	private String title;

	/** 小标题() */
	private String[] title_Child = new String[2];

	/** 获取值的字段名 */
	private String col_EName;

	/** 根据表头sql取出来的数据集 */
	private List list_Title = null;

	/** 根据表体sql取出来的数据集 */
	private List list = null;

	/** 字段名 */
	private String[] cols_EName;

	/** 每个字段对应的类型 */
	private String[] cols_Type;

	/** 每个字段对应的格式化信息 */
	private String[] cols_Format;

	/** 字段列宽信息 */
	private float[] cols_width;

	/** 列数 */
	private int colsum;

	/** 行数 */
	private int rowsum;

	/** 表头 */
	private DataSet dsHeader;

	/** 表头集 */
	private DataSet[] dsHeaders;

	/* //////////////////根据sql语句导出////////////////// */

	/** 获取表头信息的sql语句 */
	private String sql_Title;

	/** 获取表体信息的sql语句 */
	private String sql_Body;

	/*
	 * ///////////////////////////////////根据sql语句多表导出////////////////////////////
	 * //////////////////
	 */
	/** 报表编码 */
	private String[] reportIDs;

	/** nodes */
	private Node[] nodes;

	// private TableHeader[] tableheaders;

	/** 字段英文名 */
	private String[] enames;

	/** 多表导出辅助类 */
	private ExportBatchProp exprops;

	/** 多表导出表头 */
	private String[] titles;

	/** ** 多表导出小标题 */
	private List titles_child;

	/** 获取表头的sql语句 */
	private String[] sql_Titles;

	/** 获取表体数据的sql语句 */
	private String[] sql_Bodys;

	private String[] array_ChildTitle;

	/** 多表导出的列中文名 */
	private String[] cols_CName;

	private int EXPORTBYTYPE = 0; // 0：简单导出 1：根据sql语句但表导出 2：根据sql语句多表导出*/

	/** 封面数据集 */
	private DataSet[] dsFace;

	/** 多表导出的表体数据集 */
	private List dsBody;

	/** 标题参数 */
	private String[] titleParam; // 小标题参数（单表导出）*/

	/** 小标题参数（单表导出），位置 */
	private int[] titleParamPostion = new int[2];

	/** 小标题参数（单表导出），对齐方式 */
	private int[] titleParamOrient = new int[2];

	/** 已经被合并过的单元格(包含null) */
	String[] tagAlMergeCell = new String[rowsum * colsum];

	/** 已经被合并过的单元格(不包含null) */
	String[] tagCell = null;

	/** 需要合并的单元格 */
	int iTagAlMergeCell = 0;

	/** 报表类型标识符 */
	private int[] iReportTypes;

	/** 报表类型 */
	private Object[] oReportTypes;

	/** 错误信息 */
	private String sErrorInfo;

	/** 特殊数据集合 */
	private DataSet dsLikeFace;

	/** 封面标题 */
	private String[] faceTitles;

	/** 是否取消 */
	private boolean isCancel;

	/** 打印配置 */
	private SheetSettings ps;

	/** 是否导出收支总表 */
	private boolean isCurFace = false;

	/** 多表导出是否按照分报表 */
	private boolean isExportByReport = true;

	/** 当数据为空的时候是否导出 */
	private boolean isNotExportWhenEmpty = false;

	/** 空表的数量 */
	private int iEmptyTableCounts = 0;

	/** 中间表表名 */
	private List lstMidTabNames;

	/** 有要导出的中间表名 */
	private boolean isHaveTable = false;

	/** 是否需要标题 */
	private boolean isNeedTitle = true;

	/** list是否为sql */
	private boolean bListBySql = true;

	public ExportToExcel() {
	}

	/**
	 * 简单导出(带列宽) （调用的函数为 exportToSimpInfo）
	 * 
	 * @param list
	 *            表体数据
	 * @param cols_EName
	 *            主键名
	 * @param cols_CName
	 *            显示名
	 * @param aTitle
	 *            标题
	 */
	public ExportToExcel(List list, String[] cols_EName, String[] cols_CName,
			float[] widths, String aTitle) throws Exception {
		this.cols_width = widths;
		this.cols_EName = cols_EName;
		this.cols_CName = cols_CName;
		this.title = aTitle;
		this.list = list;
		this.bListBySql = false;
		this.cols_width = widths;
		EXPORTBYTYPE = 0;
	}

	/**
	 * 简单导出 （调用的函数为 exportToSimpInfo）
	 * 
	 * @param list
	 *            表体数据
	 * @param cols_EName
	 *            主键名
	 * @param cols_CName
	 *            显示名
	 * @param aTitle
	 *            标题
	 */
	public ExportToExcel(List list, String[] cols_EName, String[] cols_CName,
			String aTitle) throws Exception {
		this(list, cols_EName, cols_CName, null, aTitle);
	}

	/**
	 * 根据SQL语句的单表导出
	 * 
	 * @param node
	 *            生成表头的node
	 * @param sqlTitle
	 *            获取表头的sql语句
	 * @param sqlBody
	 *            获取表体的sql语句
	 * @param exprop
	 *            单表导出辅助类
	 */
	public ExportToExcel(String reporID, Node node, String sqlTitle,
			String sqlBody, String aEName, ExportProp exprop) {
		this.exprop = exprop;
		this.node = node;
		this.col_EName = aEName;
		this.reportID = reporID;
		// 这两项从库里取
		title = eact.getTitleWithDataSet(reportID);
		if (Common.isNullStr(title)) {
			title = exprop.getTitle();
			if (Common.isNullStr(title)) {
				title = "待定";
			}
		}
		//          
		DataSet dsConfig = eact.getConfigDataset();
		titleParam = exprop.getTitle_Child();
		setConfigByDataSet(dsConfig);
		this.sql_Body = sqlBody;
		this.sql_Title = sqlTitle;
		this.dsFace = exprop.getDsFace();
		EXPORTBYTYPE = 1;
	}

	/**
	 * 根据数据集设置小标题
	 * 
	 * @param dsConfig
	 */
	private void setConfigByDataSet(DataSet dsConfig) {
		try {
			if (titleParam == null) {
				titleParam = new String[2];
			}
			if ((dsConfig != null) && !dsConfig.isEmpty()
					&& dsConfig.locate("REPORT_ID", reportID)) {
				title_Child[0] = dsConfig.fieldByName("REPORT_TITLE_A1")
						.getString();
				title_Child[1] = dsConfig.fieldByName("REPORT_TITLE_A2")
						.getString();
				if ((titleParam != null) && (titleParam.length == 2)) {
					// 如果传递了小标题参数
					titleParamOrient[0] = dsConfig.fieldByName(
							"REPORT_PARAM_A1_ORIENT").getInteger();
					titleParamOrient[1] = dsConfig.fieldByName(
							"REPORT_PARAM_A2_ORIENT").getInteger();
					titleParamPostion[0] = dsConfig.fieldByName(
							"REPORT_PARAM_A1_POSTION").getInteger();
					titleParamPostion[1] = dsConfig.fieldByName(
							"REPORT_PARAM_A2_POSTION").getInteger();
				}
				if ("0".equals(dsConfig.fieldByName("REPORT_TITLE_A1_USE")
						.getString())) {
					// 如果不启用，则为空
					title_Child[0] = "";
				}
				if ("0".equals(dsConfig.fieldByName("REPORT_TITLE_A2_USE")
						.getString())) {
					// 如果不启用，则为空
					title_Child[1] = "";
				}
				if ("0".equals(dsConfig.fieldByName("REPORT_TITLE_A1_USE")
						.getString())
						&& (titleParam != null) && (titleParam.length == 2)) {
					// 如果不启用，则为空
					titleParam[0] = "";
				}
				if ("0".equals(dsConfig.fieldByName("REPORT_TITLE_A2_USE")
						.getString())
						&& (titleParam != null) && (titleParam.length == 2)) {
					// 如果不启用，则为空
					titleParam[1] = "";
				}
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		if ((exprop != null) && (exprop.getTitle_Child() != null)
				&& (exprop.getTitle_Child().length == 2)) {
			titleParam[0] = exprop.getTitle_Child()[0];
			titleParam[1] = exprop.getTitle_Child()[1];
		}
	}

	/**
	 * 根据SQL语句的单表导出
	 * 
	 * @param node
	 *            生成表头的node
	 * @param sqlTitle
	 *            获取表头的sql语句
	 * @param sqlBody
	 *            获取表体的sql语句
	 * @param exprop
	 *            单表导出辅助类
	 */
	public ExportToExcel(Node node, DataSet dsHeader, String sqlBody,
			String aEName, ExportProp exprop) {
		this(node, dsHeader, sqlBody, aEName, exprop, true);
	}

	/**
	 * 根据SQL语句的单表导出
	 * 
	 * @param node
	 *            生成表头的node
	 * @param sqlTitle
	 *            获取表头的sql语句
	 * @param sqlBody
	 *            获取表体的sql语句
	 * @param exprop
	 *            单表导出辅助类
	 * @param isNeedTitle
	 *            是否需要标题
	 */
	public ExportToExcel(Node node, DataSet dsHeader, String sqlBody,
			String aEName, ExportProp exprop, boolean isNeedTitle) {
		this.exprop = exprop;
		this.node = node;
		this.col_EName = aEName;
		// 这两项从库里取
		title = exprop.getTitle();
		this.title_Child = exprop.getTitle_Child();
		this.sql_Body = sqlBody;
		// this.sql_Title = sqlTitle;
		this.dsHeader = dsHeader;
		this.isNeedTitle = isNeedTitle;
		EXPORTBYTYPE = 3;
	}

	/**
	 * 表头，表体dataset
	 * 
	 * @param node
	 *            生成表头的node
	 * @param sqlTitle
	 *            获取表头的sql语句
	 * @param sqlBody
	 *            获取表体的sql语句
	 * @param exprop
	 *            单表导出辅助类
	 */
	public ExportToExcel(Node node, DataSet dsHeader, List dsBody,
			String aEName, ExportProp exprop) {
		this(node, dsHeader, dsBody, aEName, exprop, true);
	}

	/**
	 * 表头，表体dataset
	 * 
	 * @param node
	 *            生成表头的node
	 * @param sqlTitle
	 *            获取表头的sql语句
	 * @param sqlBody
	 *            获取表体的sql语句
	 * @param exprop
	 *            单表导出辅助类
	 */
	public ExportToExcel(Node node, DataSet dsHeader, List dsBody,
			String aEName, ExportProp exprop, boolean isNeedTitle) {
		this.exprop = exprop;
		this.node = node;
		this.col_EName = aEName;
		// 这两项从库里取
		title = exprop.getTitle();
		this.title_Child = exprop.getTitle_Child();
		this.dsBody = dsBody;
		this.dsHeader = dsHeader;
		this.isNeedTitle = isNeedTitle;
		this.bListBySql = false;
		EXPORTBYTYPE = 4;
	}

	/**
	 * 表头，表体dataset的多表导出
	 * 
	 * @param node
	 *            生成表头的node
	 * @param sqlTitle
	 *            获取表头的sql语句
	 * @param sqlBody
	 *            获取表体的sql语句
	 * @param exprop
	 *            单表导出辅助类
	 */
	public ExportToExcel(Node[] nodes, DataSet[] dsHeaders, List lstBodys,
			String[] aENames, ExportBatchProp exprops, String[] reportIDs) {
		this.exprops = exprops;
		this.nodes = nodes;
		this.cols_EName = aENames;
		// 这两项从库里取
		titles = exprops.getTitles();
		this.titles_child = exprops.getTitle_Childs();
		this.dsBody = lstBodys;
		this.dsHeaders = dsHeaders;
		this.reportIDs = reportIDs;
		EXPORTBYTYPE = 6;
	}

	/**
	 * EXPORTBYTYPE = 6方式导出的主体方法
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean doBatchExportByDataset() throws Exception {
		if (!exportHeader()) {
			return false;
		}
		if (Common.isNullStr(sFilePath)) {
			return false;
		}
		WritableWorkbook wwb = Workbook.createWorkbook(new File(sFilePath));
		for (int i = 0; i < reportIDs.length; i++) {
			reportID = reportIDs[i];
			if (titles != null) {
				title = titles[i];
			}
			if ((titles_child != null) && (titles_child.size() != 0)
					&& (titles_child.get(i) != null)) {
				title_Child = (String[]) titles_child.get(i);
			} else {
				title_Child = new String[2];
			}
			if (dsHeaders != null) {
				dsHeader = dsHeaders[i];
			}
			if ((dsHeader == null) || dsHeader.isEmpty()) {
				return false;
			}
			// if (dsBody!=null)
			if ((dsBody != null) && (dsBody.get(i) != null)) {
				list = (List) dsBody.get(i);
			}
			setConfigByDataSet(eact.getConfigDataset());
			// 获取表头的区域
			Object[][] headerCell = getTableHeaderCells(rowsum, colsum);

			WritableSheet ws = wwb.createSheet(title, i);
			// 添加标题
			// addTableTitleByDataSet(ws, title, null);
			addTableTitle(ws);

			// 添加表头
			// addTableHeaderByDataset(ws, headerCell);
			addTableHeader(ws, headerCell);

			// 导出表体
			if (list != null) {
				setCols();
				exportBody(ws, headerCell, list, rowsum + 1);
			}

			// 合并表头
			ws.mergeCells(0, 0, colsum - 1, 0);
			// ws.mergeCells(0, 1, colsum - 1, 1);
			ws.setRowView(0, 600);
			// 合并单元格
			// mergeCells(ws, headerCell);
			// 设置列宽
			setWidth(ws, cols_width);
			this.ps = ws.getSettings();
			this.setPaperPS(eact.getConfigDataset(), reportID);
		}
		// addTableChildTitle(ws);
		// 写入excel
		wwb.write();
		// 关闭文件
		wwb.close();
		itserv.closeSession();
		return true;
	}

	/**
	 * 表头，表体dataset,根据配置信息导出
	 * 
	 * @param node
	 *            生成表头的node
	 * @param sqlTitle
	 *            获取表头的sql语句
	 * @param sqlBody
	 *            获取表体的sql语句
	 * @param exprop
	 *            单表导出辅助类
	 */
	public ExportToExcel(Node node, DataSet dsHeader, List dsBody,
			String aEName, ExportProp exprop, String reportID) {
		this.reportID = reportID;
		this.exprop = exprop;
		this.node = node;
		this.col_EName = aEName;
		// 这两项从库里取
		title = exprop.getTitle();
		this.bListBySql = false;
		this.title_Child = exprop.getTitle_Child();
		this.dsBody = dsBody;
		this.dsHeader = dsHeader;
		setConfigByDataSet(eact.getConfigDataset());
		EXPORTBYTYPE = 4;
	}

	/**
	 * 批量导出（带设置列宽及格式化的）,小标题是多个
	 * aarray_ChildTitle的存储格式是list放的是各个表对应的小标题。该list里放的是数组，数组里放的是该表对应的所有的小标题
	 * 
	 * @param node
	 * @param sqlHeader
	 * @param sqlBody
	 * @param aEName
	 * @param aCName
	 * @param aType
	 * @param aTitle
	 * @param aTitle_Child
	 * @param edt
	 * @param width
	 */
	public ExportToExcel(String[] reportIDs, Node[] node, String sqlHeader[],
			String[] sqlBody, String[] aEName, ExportBatchProp exprops) {
		this.exprops = exprops;
		this.reportIDs = reportIDs;
		this.nodes = node;
		// this.tableheaders = tableheaders;
		this.enames = aEName;
		// 这两项从库里取
		titles = eact.getTitleWithDataSet(reportIDs);
		if (titles == null) {
			// 如果获取为空则从接口获取
			titles = exprops.getTitles();
			if (titles == null) {
				// 如果接口获取为空，则设置默认
				titles = new String[reportIDs.length];
			}
		} else {
			for (int i = 0; i < titles.length; i++) {
				if (titles[i] == null) {
					// 如果获取为空则从接口获取
					titles = exprops.getTitles();
					if (titles == null) {
						// 如果接口获取为空，则设置默认
						titles = new String[reportIDs.length];
					}
				}
			}
		}
		titles_child = eact.getTitle_ChildWithDataSet(reportIDs);
		if (titles_child == null) {
			// 如果获取为空则从接口获取
			titles_child = exprops.getTitle_Childs();
			if (titles_child == null) {
				// 如果接口获取为空，则设置默认
				titles_child = new ArrayList();
			}
		}
		this.sql_Bodys = sqlBody;
		this.sql_Titles = sqlHeader;
		EXPORTBYTYPE = 2;
	}

	// //////////////////////////////////////简单导出/////////////////////////////////////
	/**
	 * 简单导出
	 */
	private boolean exportToSimpInfo() {
		// 获取文件名
		try {
			sFilePath = getFilePath();
			if (Common.isNullStr(sFilePath)) {
				return false;
			}
			//
			colsum = cols_EName.length;
			// 创建导出文件流
			WritableWorkbook wwb = Workbook.createWorkbook(new File(sFilePath));
			WritableSheet ws = wwb.createSheet(title, 0);

			addHeaderToSimpInfo(ws, title);

			exportBodyToSimpInfo(ws, list, rowsum);

			// 合并表头
			ws.mergeCells(0, 0, colsum - 1, 0);
			if ((this.cols_width != null) && (this.cols_width.length != 0)) {
				for (int i = 0; i < cols_width.length; i++) {
					ws.setColumnView(i, (int) cols_width[i]);
				}
			} else {
				for (int i = 0; i < colsum; i++) {
					ws.setColumnView(i, 100);
				}
			}
			// 写入excel
			wwb.write();
			// 关闭文件
			wwb.close();
			return true;
		} catch (Exception ee) {
			JOptionPane.showMessageDialog(Global.mainFrame,
					"请首先确认要保存信息的文件没有被打开，导出失败！");
			ee.printStackTrace();
			return false;
		}
	}

	/**
	 * 添加信息表头
	 * 
	 * @throws WriteException
	 * 
	 */
	private void addHeaderToSimpInfo(WritableSheet ws, String title)
			throws WriteException, Exception {
		for (int jh = 0; jh < colsum; jh++) {
			ws.addCell(getLabel(jh, 0, title, 1, "ARIAL", 14));
		}
		rowsum++;
		for (int jh = 0; jh < colsum; jh++) {
			ws.addCell(getLabel(jh, 1, cols_CName[jh], 1, "ARIAL", 12));
		}
		rowsum++;
	}

	/**
	 * 导出简单信息表体
	 * 
	 * @throws Exception
	 */
	private InfoPackage exportBodyToSimpInfo(WritableSheet ws,
			List listDataRow, int iHeaderRowCount) throws Exception {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		for (int k = 0; k < listDataRow.size(); k++) {
			if (list.get(k) == null) {
				continue;
			}
			// Map temp = (Map) listDataRow.get(k);
			for (int j = 0; j < cols_EName.length; j++) {
				Object obj = ((Map) (list.get(k))).get(cols_EName[j]);
				// 在这个地方判断是数据的类型
				ws.addCell(getLabel(j, iHeaderRowCount, Common.nonNullStr(obj),
						1, "宋体", 10));
			}
			iHeaderRowCount++;
		}
		return info;
	}

	// //////////////////////////////////////////////单表导出///////////////////////////////////////////////
	/**
	 * 导出函数 根据sql语句（默认配置信息）
	 * 
	 * @throws Exception
	 */
	private boolean doExportBySql() throws Exception {
		lstConfig = eact.getConfigDataset(reportID);
		boolean isUseModel = false;
		if ((lstConfig != null) && (lstConfig.size() != 0)) {
			isUseModel = "1".equals(((Map) lstConfig.get(0)).get("IS_USEMODEL"
					.toLowerCase()));
		}
		if (!exportHeader()) {
			return false;
		}
		if (!Common.isNullStr(sql_Title)) {
			list_Title = itserv.executeQuery(sql_Title);
		} else {
			return false;
		}
		if (!Common.isNullStr(sql_Body)) {
			list = itserv.executeQueryWithInfo(sql_Body, exprop
					.getSqls_BeforeGetBody(), exprop.getSqls_AfterGetBody());
		} else {
			return true;
		}
		setCols();
		if (Common.isNullStr(sFilePath)) {
			return false;
		}
		if (list_Title.size() == 0) {
			return false;
		}

		// 获取表头的区域
		Object[][] headerCell = getTableHeaderCells(rowsum, colsum);
		WritableWorkbook wwb = Workbook.createWorkbook(new File(sFilePath));
		if (!isUseModel) {
			setConfigByDataSet(eact.getConfigDataset());
			// 非模板方式
			WritableSheet ws = wwb.createSheet(title, 0);
			// 添加标题
			addTableTitle(ws);

			// 添加表头
			addTableHeader(ws, headerCell);

			// 导出表体
			if (list != null) {
				exportBody(ws, headerCell, list, rowsum + 1);
			}

			// 合并表头
			ws.mergeCells(0, 0, colsum - 1, 0);

			setRowHeight(ws);
			ws.setRowView(0, 600);
			// 合并单元格
			eact.mergeCells(isNeedTitle, ws, headerCell, rowsum, colsum);
			// 设置列宽
			setWidth(ws, cols_width);
			ps = ws.getSettings();
			setPaperPS(eact.getConfigDataset(), reportID);
			itserv.closeSession();
		} else {
			// 如果导出了封面则从封面后导出，否则直接从第一个导出
			Sheet wsi = null;
			WritableSheet ws = null;
			// 取文件的时候要注意的是，先从本机找，如果本机找不到，则从库里找，找到后再保存到本机特定地址
			String reportname = null;
			String modifytime = null;
			// lstConfig = eact.getConfigDataset(reportID);
			if ((lstConfig != null) && !lstConfig.isEmpty()) {
				// // 如果数据集不为空
				reportname = Common
						.nonNullStr(((XMLData) eact.getConfigDataset(reportID)
								.get(0)).get("report_title"));
				modifytime = Common.nonNullStr(((XMLData) eact
						.getConfigDataset(reportID).get(0)).get("modifytime"));
			}
			if (getModelFile(reportID, reportname, modifytime)) {
				InputStream is = new FileInputStream(new File(efile
						+ reportname + ".XLS"));
				Workbook rwb = Workbook.getWorkbook(is);
				wsi = rwb.getSheet(0);
//				wwb.importSheet(reportname, 0, wsi);
				
				
				is.close();
				ws = wwb.getSheet(0);
				setDataCellFormats(ws);
			} else {
				this.sErrorInfo = "没有配置该表的格式信息,初始化失败";
				return false;
			}
			// 添加标题
			addTableTitleWithModel(ws, 0);
			// 导出表体
			InfoPackage info = new InfoPackage();
			int headerrow = iHeaderRow;
			if (headerrow == -1) {
				headerrow = rowsum + 3;
			}
			if (list != null) {
				info = exportBodyWithModel(ws, headerCell, list, headerrow);
			}
			if (!info.getSuccess()) {
				wwb.close();
				this.sErrorInfo = "表体导出失败";
				return false;
			}
		}
		// 写入excel
		wwb.write();
		// 关闭文件
		wwb.close();
		return true;
	}

	private void setRowHeight(WritableSheet ws) throws Exception {
		int titleHeight = 600;
		int titleHeight1 = 300;
		int titleHeight2 = 300;
		int bodyHeight = 300;
		if ((eact.getConfigDataset() != null)
				&& !eact.getConfigDataset().isEmpty()
				&& eact.getConfigDataset().locate("REPORT_ID", reportID)) {
			titleHeight = eact.getConfigDataset().fieldByName(
					"REPORT_TITLE_HEIGHT").getInteger();
			titleHeight1 = eact.getConfigDataset().fieldByName(
					"REPORT_TITLE_A1_HEIGHT").getInteger();
			titleHeight2 = eact.getConfigDataset().fieldByName(
					"REPORT_TITLE_A2_HEIGHT").getInteger();
			bodyHeight = eact.getConfigDataset().fieldByName(
					"REPORT_BODY_HEIGHT").getInteger();
		}
		ws.setRowView(0, titleHeight);
		ws.setRowView(1, titleHeight1);
		ws.setRowView(2, titleHeight2);
		for (int i = 3; i < rowsum; i++) {
			ws.setRowView(i, bodyHeight);
		}
	}

	/**
	 * 导出函数 根据dataset语句
	 * 
	 * @throws Exception
	 */
	public boolean doExportByDataset() throws Exception {
		lstConfig = eact.getConfigDataset(reportID);
		if (!exportHeader()) {
			return false;
		}
		if ((dsHeader == null) || dsHeader.isEmpty()) {
			return false;
		}
		if (!Common.isNullStr(sql_Body)) {
			list = itserv.executeQuery(sql_Body);
		}
		if (dsBody != null) {
			list = dsBody;
		}
		if (Common.isNullStr(sFilePath)) {
			return false;
		}

		// 获取表头的区域
		Object[][] headerCell = getTableHeaderCells(rowsum, colsum);

		WritableWorkbook wwb = Workbook.createWorkbook(new File(sFilePath));
		lstConfig = eact.getConfigDataset(reportID);
		boolean isUseModel = false;
		if ((lstConfig != null) && (lstConfig.size() > 0)) {
			isUseModel = "1".equals(((Map) lstConfig.get(0)).get("IS_USEMODEL"
					.toLowerCase()));
		}
		if (!isUseModel) {
			WritableSheet ws = wwb.createSheet(title, 0);
			// 添加标题
			if (isNeedTitle) {
				addTableTitle(ws);
			}

			// 添加表头
			addTableHeader(ws, headerCell);

			// 导出表体
			if (list != null) {
				setCols();
				if (this.isNeedTitle) {
					exportBody(ws, headerCell, list, rowsum + 1);
				} else {
					exportBody(ws, headerCell, list, rowsum);
				}
			}
			// 合并表头
			if (this.isNeedTitle) {
				ws.mergeCells(0, 0, colsum - 1, 0);
			}

			// ws.mergeCells(0, 1, colsum - 1, 1);
			ws.setRowView(0, 600);
			// 合并单元格
			eact.mergeCells(isNeedTitle, ws, headerCell, rowsum, colsum);
			// 设置列宽
			setWidth(ws, cols_width);
			this.ps = ws.getSettings();
			this.setPaperPS(eact.getConfigDataset(), reportID);

		} else {
			Sheet wsi = null;
			WritableSheet ws = null;
			// 取文件的时候要注意的是，先从本机找，如果本机找不到，则从库里找，找到后再保存到本机特定地址
			String reportname = null;
			String modifytime = null;
			if ((lstConfig != null) && !lstConfig.isEmpty()) {
				// 如果数据集不为空
				reportname = Common
						.nonNullStr(((XMLData) eact.getConfigDataset(reportID)
								.get(0)).get("report_title"));
				modifytime = Common.nonNullStr(((XMLData) eact
						.getConfigDataset(reportID).get(0)).get("modifytime"));
			}
			if (getModelFile(reportID, reportname, modifytime)) {
				InputStream is = new FileInputStream(new File(efile
						+ reportname + ".XLS"));
				Workbook rwb = Workbook.getWorkbook(is);
				wsi = rwb.getSheet(0);
//				wwb.importSheet(reportname, 0, wsi);
				is.close();
				ws = wwb.getSheet(0);
				setDataCellFormats(ws);
			} else {
				this.sErrorInfo = "没有配置该表的格式信息,初始化失败";
				return false;
			}
			addTableTitleWithModel(ws, 0);

			// //添加表头
			// addTableHeader(ws, headerCell);
			int headerrow = iHeaderRow;
			if (headerrow == -1) {
				headerrow = rowsum + 3;
			}
			// 导出表体
			if (list != null) {
				setCols();
				exportBodyWithModel(ws, headerCell, list, headerrow);
			}
		}
		wwb.write();
		// 关闭文件
		wwb.close();
		itserv.closeSession();
		return true;
	}

	/**
	 * 设置EXCEL的列宽
	 * 
	 * @param ws
	 * @param width
	 * @param colsum
	 */
	private void setWidth(WritableSheet ws, float[] width) {
		if ((width == null) || (width.length == 0)) {
			// for (int i = 0; i < colsum; i++)
			// ws.setColumnView(i, 50);
			return;
		}
		for (int i = 0; i < width.length; i++) {
			if (width[i] == 0) {
				width[i] = 100;
			}
			ws.setColumnView(i, (int) width[i] / 6);
		}
	}

	/**
	 * 导出主题函数
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean doExport() throws Exception {
		switch (EXPORTBYTYPE) {
		case 0:
			return exportToSimpInfo();
		case 1:
			return doExportBySql();
		case 2:
			return doBatchExport();
		case 3:
			return doExportByDataset();
		case 4:
			return doExportByDataset();
		case 5:
			return doExportOldFace();
		case 6:
			return doBatchExportByDataset();
		case 9:
			return doBatchExportWithModel();
		case 10:
			return doBatchExportWithModel();
		case 69:
			return doBatchExportWithModel();
		default:
			return true;
		}
	}

	// /////////////////////////////////多表导出/////////////////////////////////////////////
	/**
	 * 导出表头
	 * 
	 * @throws Exception
	 */
	private boolean exportHeader() throws Exception {
		if (exprop != null) {
			if (Common.isNullStr(exprop.getDefaultAddres())) {
				sFilePath = getFilePath();
			} else {
				sFilePath = exprop.getDefaultAddres();
			}
		}
		if (Common.isNullStr(sFilePath)) {
			this.sErrorInfo = "获取地址失败";
			return false;
		}
		if (Common.isNullStr(tempFile)) {
			this.sErrorInfo = "生成临时文件失败";
			return false;
		}
		if (node == null) {
			return true;
		}
		OutputStream osh = new FileOutputStream(tempFile);
		TableHeader tableheader = createTableHeader(node);
		colsum = tableheader.getColumns();
		rowsum = tableheader.getRows();
		exportTableHeader(tableheader);
		osh.close();
		return true;
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getFilePath() throws Exception {
		createChoose();
		int returnval = fileChooser.showSaveDialog(Global.mainFrame);
		if (returnval == JFileChooser.CANCEL_OPTION) {
			this.isCancel = true;
			return null;
		}
		File file = fileChooser.getSelectedFile();
		sFilePath = file.getPath();
		String sfilename = changeFileName(sFilePath, ".xls");
		if (Common.isNullStr(sfilename)) {
			return null;
		} else {
			setDefalutMemoryPath(sfilename.substring(0, sfilename
					.lastIndexOf("\\") + 1));
		}
		if (file.exists()) {
			if (JOptionPane.showConfirmDialog(Global.mainFrame, "是否覆盖已经存在的文件",
					"提示信息", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
				return sfilename;
			} else {
				return null;
			}
		} else {
			return sfilename;
		}
	}

	/**
	 * 创建文件选择器
	 * 
	 * @throws Exception
	 */
	private void createChoose() throws Exception {
		fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setDialogTitle("保存导出文件");
		// getDefaultMemoryPath()
		fileChooser.setSelectedFile(new File(getDefalutMemoryPath() + title
				+ ".xls"));//
		// 设定可用的文件的后缀名
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				if (f.getName().endsWith(".xls".toLowerCase())
						|| f.isDirectory()) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return "所有文件(*.xls)";
			}
		});
	}

	/**
	 * 设置上次报表导出的地址
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getDefalutMemoryPath() throws Exception {
		Properties pp = new Properties();
		// 获取WORKSHEET
		File f = new File(efile + "EXPORTSET" + ".properties");
		if (!f.exists()) {
			// 如果配置文件不存在在创建
			File expPath = new File(efile);
			if (!expPath.exists()) {
				expPath.mkdirs();
			}
			File tempFile = new File(expPath, "EXPORTSET.properties");
			tempFile.createNewFile();
		}
		InputStream fis = new FileInputStream(f);
		pp.load(fis);
		fis.close();
		String path = pp.getProperty("BUDGETEXPORTPATH");
		if (!Common.isNullStr(path)) {
			return path;
		} else {
			return "";
		}
	}

	/**
	 * 设置上次报表导出的地址
	 * 
	 * @return
	 * @throws Exception
	 */
	private void setDefalutMemoryPath(String path) throws Exception {
		Properties pp = new Properties();
		// 获取WORKSHEET
		File f = new File(efile + "EXPORTSET" + ".properties");
		InputStream fis = new FileInputStream(f);
		pp.load(fis);
		OutputStream fos = new FileOutputStream(f);
		pp.setProperty("BUDGETEXPORTPATH", path);
		pp.store(fos, "modify parameters");
		fos.close();
		fis.close();
	}

	/**
	 * 获得特定类型文件的路径
	 * 
	 * @param aFilePath
	 * @param aFileType
	 * @return
	 */
	private String changeFileName(String aFilePath, String aFileType)
			throws Exception {
		if (Common.isNullStr(aFilePath)) {
			return null;
		}
		int idex = aFilePath.lastIndexOf("\\");
		String sFileName = aFilePath.substring(idex + 1, aFilePath.length());
		if (sFileName.length() <= 4) {
			if (JOptionPane.showConfirmDialog(Global.mainFrame,
					"文件名不能为空，请重新输入", "提示信息", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
				return null;
			} else {
				getFilePath();
			}
		} else {
			int length = sFileName.length();
			String sLastStr = sFileName.substring(length - 4, length);
			if (!Common.isEqual(aFileType, sLastStr)) {
				aFilePath = aFilePath + aFileType;
			}
		}
		return aFilePath;
	}

	/**
	 * 文字段---sql版本
	 * 
	 * @param aEName
	 * @return
	 * @throws Exception
	 */
	private void setCols() throws Exception {
		if (list_Title != null) {
			cols_EName = new String[list_Title.size()];
			cols_Type = new String[list_Title.size()];
			cols_Format = new String[list_Title.size()];
			cols_width = new float[list_Title.size()];
		}
		if (dsHeader != null) {
			cols_EName = new String[dsHeader.getRecordCount()];
			cols_Type = new String[dsHeader.getRecordCount()];
			cols_Format = new String[dsHeader.getRecordCount()];
			cols_width = new float[dsHeader.getRecordCount()];
		}
		int k = 0;
		if ((list_Title == null) && (this.dsHeader != null)) {
			dsHeader.beforeFirst();
			while (dsHeader.next()) {
				String ename = String.valueOf(dsHeader.fieldByName(col_EName)
						.getString());
				// 如果字段为空
				if (Common.isEqual("NULL", ename.toUpperCase())
						|| Common.isNullStr(ename)||"无".equalsIgnoreCase(ename)) {
					continue;
				}
				cols_EName[k] = ename;
				if (!Common.isNullStr(exprop.getFieldTName())) {
					// 如果设置了类型字段
					cols_Type[k] = Common.nonNullStr(dsHeader.fieldByName(
							exprop.getFieldTName()).getString());
				}
				if (!Common.isNullStr(exprop.getFieldFName())) {
					// 如果设置了格式化字段
					cols_Format[k] = Common.nonNullStr(dsHeader.fieldByName(
							exprop.getFieldFName()).getString());
				}
				if (!Common.isNullStr(exprop.getFieldWName())) {
					// 如果设置了格式化字段
					String sWidth = Common.nonNullStr(dsHeader.fieldByName(
							exprop.getFieldWName()).getString());
					cols_width[k] = Common.isNullStr(sWidth) ? 72 : Float
							.parseFloat(sWidth);
				}
				k++;
			}
		} else {
			for (int i = 0; i < list_Title.size(); i++) {
				Map map = (Map) list_Title.get(i);
				String ename = String.valueOf(map.get(col_EName.toLowerCase()));
				// 如果字段为空
				if (Common.isEqual("NULL", ename.toUpperCase())
						|| Common.isNullStr(ename)) {
					continue;
				}
				cols_EName[k] = ename;
				if (!Common.isNullStr(exprop.getFieldTName())) {
					// 如果设置了类型字段
					cols_Type[k] = Common.nonNullStr(map.get(exprop
							.getFieldTName().toLowerCase()));
				}
				if (!Common.isNullStr(exprop.getFieldFName())) {
					// 如果设置了格式化字段
					cols_Format[k] = Common.nonNullStr(map.get(exprop
							.getFieldFName().toLowerCase()));
				}
				if (!Common.isNullStr(exprop.getFieldWName())) {
					// 如果设置了格式化字段
					String sWidth = Common.nonNullStr(map.get(exprop
							.getFieldWName().toLowerCase()));
					cols_width[k] = Common.isNullStr(sWidth) ? 72 : Float
							.parseFloat(sWidth);
				}
				k++;
			}
		}
		if (Common.isNullStr(exprop.getFieldTName())) {
			if (exprop.getTypes() != null) {
				cols_Type = exprop.getTypes();
			}
		}
		if (Common.isNullStr(exprop.getFieldWName())) {
			if (exprop.getWidths() != null) {
				cols_width = exprop.getWidths();
			}
		}
		if (Common.isNullStr(exprop.getFieldFName())) {
			if (exprop.getFormats() != null) {
				cols_Format = exprop.getFormats();
			}
		}
		if (this.EXPORTBYTYPE == 3) {
			// 获取field_ename
			TableColumnInfo[] fields = QueryStub.getClientQueryTool()
					.getColumnInfoBySQL(sql_Body);
			int length = fields.length;
			cols_EName = new String[colsum];
			for (int i = 0; i < length; i++) {
				cols_EName[i] = Common.nonNullStr(fields[i].getColumnName());
			}
		}
	}

	/**
	 * 根据英文字段名获得所有英文字段---sql版本
	 * 
	 * @param aEName
	 * @return
	 * @throws Exception
	 */
	private void setBatchCols(int cirNow) throws Exception {
		cols_EName = new String[colsum];
		cols_Type = new String[colsum];
		cols_Format = new String[colsum];
		cols_width = new float[colsum];
		int k = 0;
		String tname;
		String fname;
		String wname;
		if ((list_Title == null) && (this.dsHeader != null)) {
			dsHeader.beforeFirst();
			while (dsHeader.next()) {
				String ename = String.valueOf(dsHeader.fieldByName(col_EName)
						.getString());
				// 如果字段为空
				if (Common.isEqual("NULL", ename.toUpperCase())
						|| Common.isNullStr(ename)) {
					continue;
				}
				cols_EName[k] = ename;
				tname = ((exprops.getFieldTName() == null) || (exprops
						.getFieldTName().length == 0)) ? "" : exprops
						.getFieldTName()[cirNow];
				if (!Common.isNullStr(tname)) {
					// 如果设置了类型字段
					cols_Type[k] = Common.nonNullStr(dsHeader
							.fieldByName(tname));
				}
				fname = ((exprops.getFieldFName() == null) || (exprops
						.getFieldFName().length == 0)) ? "" : exprops
						.getFieldFName()[cirNow];
				if (!Common.isNullStr(fname)) {
					// 如果设置了格式化字段
					cols_Format[k] = Common.nonNullStr(dsHeader.fieldByName(
							fname).getString());
				}
				wname = ((exprops.getFieldWName() == null) || (exprops
						.getFieldWName().length == 0)) ? "" : exprops
						.getFieldWName()[cirNow];
				if (!Common.isNullStr(wname)) {
					// 如果设置了格式化字段
					String sWidth = Common.nonNullStr(dsHeader.fieldByName(
							wname).getString());
					cols_width[k] = Common.isNullStr(sWidth) ? 72 : Float
							.parseFloat(sWidth);
				}
				k++;
			}
		} else {
			for (int i = 0; i < list_Title.size(); i++) {
				if (this.EXPORTBYTYPE != 9) {
					col_EName = enames[cirNow];
				}
				Map ename1 = (Map) list_Title.get(i);
				String ename = String.valueOf(ename1.get(col_EName
						.toLowerCase()));
				// 如果字段为空
				if (Common.isEqual("NULL", ename.toUpperCase())
						|| Common.isNullStr(ename)) {
					continue;
				}
				cols_EName[k] = ename;
				tname = ((exprops.getFieldTName() == null) || (exprops
						.getFieldTName().length == 0)) ? "" : exprops
						.getFieldTName()[cirNow];
				if (!Common.isNullStr(tname)) {
					// 如果设置了类型字段
					cols_Type[k] = String.valueOf(((Map) list_Title.get(i))
							.get(tname.toLowerCase()));
				}
				fname = ((exprops.getFieldFName() == null) || (exprops
						.getFieldFName().length == 0)) ? "" : exprops
						.getFieldFName()[cirNow];
				if (!Common.isNullStr(fname.toLowerCase())) {
					// 如果设置了格式化字段
					cols_Format[k] = String.valueOf(((Map) list_Title.get(i))
							.get(fname));
				}
				wname = ((exprops.getFieldWName() == null) || (exprops
						.getFieldWName().length == 0)) ? "" : exprops
						.getFieldWName()[cirNow];
				if (!Common.isNullStr(wname)) {
					// 如果设置了格式化字段
					String sWidth = Common.nonNullStr(((Map) list_Title.get(i))
							.get(wname.toLowerCase()));
					cols_width[k] = Common.isNullStr(sWidth) ? 0 : Float
							.parseFloat(sWidth);
				}
				k++;
			}
		}
		if ((exprops.getFormats() != null) && (exprops.getFormats().size() != 0)
				&& (exprops.getFormats().get(cirNow) != null)) {
			cols_Format = (String[]) exprops.getFormats().get(cirNow);
		}
		if ((exprops.getTypes() != null) && (exprops.getTypes().size() != 0)
				&& (exprops.getTypes().get(cirNow) != null)) {
			cols_Type = (String[]) exprops.getTypes().get(cirNow);
		}
		if ((exprops.getWidths() != null) && (exprops.getWidths().size() != 0)
				&& (exprops.getWidths().get(cirNow) != null)) {
			cols_width = (float[]) exprops.getWidths().get(cirNow);
		}
	}

	/**
	 * 根据英文字段名获得所有英文字段---sql版本
	 * 
	 * @param aEName
	 * @return
	 * @throws Exception
	 */
	private void setBatchColsSql(int cirNow) throws Exception {
		cols_EName = new String[colsum];
		cols_Type = new String[colsum];
		cols_Format = new String[colsum];
		cols_width = new float[colsum];
		int k = 0;
		String tname;
		String fname;
		String wname;
		for (int i = 0; i < list_Title.size(); i++) {
			if ((this.EXPORTBYTYPE != 9) && (this.EXPORTBYTYPE != 10)) {
				col_EName = enames[cirNow];
			}
			Map ename1 = (Map) list_Title.get(i);
			String ename = String.valueOf(ename1.get(col_EName.toLowerCase()));
			// 如果字段为空
			if (Common.isEqual("NULL", ename.toUpperCase())
					|| Common.isNullStr(ename)) {
				continue;
			}
			cols_EName[k] = ename;
			tname = ((exprops.getFieldTName() == null) || (exprops.getFieldTName().length == 0)) ? ""
					: exprops.getFieldTName()[cirNow];
			if (!Common.isNullStr(tname)) {
				// 如果设置了类型字段
				cols_Type[k] = String.valueOf(((Map) list_Title.get(i))
						.get(tname.toLowerCase()));
			}
			fname = ((exprops.getFieldFName() == null) || (exprops.getFieldFName().length == 0)) ? ""
					: exprops.getFieldFName()[cirNow];
			if (!Common.isNullStr(fname)) {
				// 如果设置了格式化字段
				cols_Format[k] = String.valueOf(((Map) list_Title.get(i))
						.get(fname.toLowerCase()));
			}
			wname = ((exprops.getFieldWName() == null) || (exprops.getFieldWName().length == 0)) ? ""
					: exprops.getFieldWName()[cirNow];
			if (!Common.isNullStr(wname)) {
				// 如果设置了格式化字段
				String sWidth = Common.nonNullStr(((Map) list_Title.get(i))
						.get(wname.toLowerCase()));
				cols_width[k] = Common.isNullStr(sWidth) ? 0 : Float
						.parseFloat(sWidth);
			}
			k++;
		}
		if ((exprops.getFormats() != null) && (exprops.getFormats().size() != 0)
				&& (exprops.getFormats().get(cirNow) != null)) {
			cols_Format = (String[]) exprops.getFormats().get(cirNow);
		}
		if ((exprops.getTypes() != null) && (exprops.getTypes().size() != 0)
				&& (exprops.getTypes().get(cirNow) != null)) {
			cols_Type = (String[]) exprops.getTypes().get(cirNow);
		}
		if ((exprops.getWidths() != null) && (exprops.getWidths().size() != 0)
				&& (exprops.getWidths().get(cirNow) != null)) {
			cols_width = (float[]) exprops.getWidths().get(cirNow);
		}
	}

	/**
	 * 根据英文字段名获得所有英文字段---sql版本
	 * 
	 * @param aEName
	 * @return
	 * @throws Exception
	 */
	private void setBatchColsList(int cirNow) throws Exception {
		cols_EName = new String[colsum];
		cols_Type = new String[colsum];
		cols_Format = new String[colsum];
		cols_width = new float[colsum];
		int k = 0;
		String tname;
		String fname;
		String wname;
		dsHeader.beforeFirst();
		while (dsHeader.next()) {
			String ename = String.valueOf(dsHeader.fieldByName(col_EName)
					.getString());
			// 如果字段为空
			if (Common.isEqual("NULL", ename.toUpperCase())
					|| Common.isNullStr(ename)) {
				continue;
			}
			cols_EName[k] = ename;
			tname = ((exprops.getFieldTName() == null) || (exprops.getFieldTName().length == 0)) ? ""
					: exprops.getFieldTName()[cirNow];
			if (!Common.isNullStr(tname)) {
				// 如果设置了类型字段
				cols_Type[k] = Common.nonNullStr(dsHeader.fieldByName(tname)
						.getString());
			}
			fname = ((exprops.getFieldFName() == null) || (exprops.getFieldFName().length == 0)) ? ""
					: exprops.getFieldFName()[cirNow];
			if (!Common.isNullStr(fname)) {
				// 如果设置了格式化字段
				cols_Format[k] = Common.nonNullStr(dsHeader.fieldByName(fname)
						.getString());
			}
			wname = ((exprops.getFieldWName() == null) || (exprops.getFieldWName().length == 0)) ? ""
					: exprops.getFieldWName()[cirNow];
			if (!Common.isNullStr(wname)) {
				// 如果设置了格式化字段
				String sWidth = Common.nonNullStr(dsHeader.fieldByName(wname)
						.getString());
				cols_width[k] = Common.isNullStr(sWidth) ? 72 : Float
						.parseFloat(sWidth);
			}
			k++;
		}
		if ((exprops.getFormats() != null) && (exprops.getFormats().size() != 0)
				&& (exprops.getFormats().get(cirNow) != null)) {
			cols_Format = (String[]) exprops.getFormats().get(cirNow);
		}
		if ((exprops.getTypes() != null) && (exprops.getTypes().size() != 0)
				&& (exprops.getTypes().get(cirNow) != null)) {
			cols_Type = (String[]) exprops.getTypes().get(cirNow);
		}
		if ((exprops.getWidths() != null) && (exprops.getWidths().size() != 0)
				&& (exprops.getWidths().get(cirNow) != null)) {
			cols_width = (float[]) exprops.getWidths().get(cirNow);
		}
	}

	/**
	 * 获取表头单元格信息
	 * 
	 * @param iHeaderRowCount
	 * @param iColTableHeader
	 * @return
	 * @throws Exception
	 */
	private Object[][] getTableHeaderCells(int iHeaderRowCount,
			int iColTableHeader) throws Exception {
		FileInputStream osInput = new FileInputStream(new File(tempFile));
		Workbook bookHeader = Workbook.getWorkbook(osInput);
		Sheet sheetHeader = bookHeader.getSheet(0);
		Object[][] headerCell = new Object[iColTableHeader][rowsum];
		tagAlMergeCell = new String[rowsum * colsum]; // 已经被合并过的单元格(包含null)
		tagCell = null; // 已经被合并过的单元格(不包含null)
		iTagAlMergeCell = 0;
		for (int jh = 0; jh < colsum - 1; jh++) {
			for (int ih = 0; ih < rowsum; ih++) {
				headerCell[jh][ih] = sheetHeader.getCell(jh, ih).getContents(); // 第一个参数是例号,第二个是行号
			}
		}
		int itemplastcolslenght = tempLastCols.length;
		for (int temp = 0; temp < itemplastcolslenght; temp++) {
			headerCell[colsum - 1][temp] = tempLastCols[temp];
		}
		osInput.close();
		return headerCell;
	}

	/**
	 * 设置表体数据单元格数据格式
	 * 
	 * @throws Exception
	 */
	private void setDataCellFormats(WritableSheet ws) throws Exception {
		colFormats = new CellFormat[colsum];
		int headerrow = getDataRow(ws);
		if (headerrow == -1) {
			headerrow = rowsum + 3;
		}
		iHeaderRow = headerrow;
		for (int jh = 0; jh < colsum; jh++) {
			colFormats[jh] = (WritableCellFormat) ws.getCell(jh, headerrow)
					.getCellFormat();
		}
		colFormats[colsum - 1] = (WritableCellFormat) ws.getCell(colsum - 1,
				headerrow).getCellFormat();
	}

	/**
	 * 添加标题
	 * 
	 * @param ws
	 * @param title
	 * @throws WriteException
	 */
	private boolean addTableTitle(WritableSheet ws) throws Exception {
		int title_FontSize = 14; // 标题字体大小
		// FontName title_FontName = WritableFont.ARIAL; // 标题字体
		String sFontName = "黑体";
		// Alignment title_Postion = (Alignment Alignment.CENTRE;
		int iFontPos = Constants.CENTER;
		int iview = 1;
		if ((eact.getConfigDataset() != null)
				&& !eact.getConfigDataset().isEmpty()
				&& eact.getConfigDataset().locate("REPORT_ID", reportID)) {
			// 如果数据集不为空
			title_FontSize = eact.getConfigDataset().fieldByName(
					"REPORT_TITLE_FONT_SIZE").getInteger();
			sFontName = eact.getConfigDataset()
					.fieldByName("REPORT_TITLE_FONT").getString();
			iFontPos = eact.getConfigDataset().fieldByName(
					"REPORT_TITLE_ORIENT").getInteger();
			iview = eact.getConfigDataset().fieldByName("REPORT_AUTONEXT")
					.getInteger();
		}
		for (int jh = 0; jh < colsum; jh++) {
			jxl.write.WritableFont wfc = new jxl.write.WritableFont(
					WritableFont.createFont(sFontName), title_FontSize,
					WritableFont.BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat();
			jxl.write.Label labelC = null;
			if (!this.isExportByReport) {
				labelC = new jxl.write.Label(0, 0, this.getTitleByAddr(exprops
						.getDefaultAddres()));
			} else {
				labelC = new jxl.write.Label(0, 0, title);
			}
			setCellType(iFontPos, iview, wcfFC);
			wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcfFC.setShrinkToFit(true);
			wcfFC.setFont(wfc);
			labelC.setCellFormat(wcfFC);
			ws.addCell(labelC);
		}
		addTableChildTitile(ws);
		if (title_Child != null) {
			rowsum = rowsum + title_Child.length;
		}
		return true;
	}

	/**
	 * 添加子标题
	 * 
	 * @param ws
	 * @param title_Child
	 * @throws Exception
	 */
	private void addTableChildTitile(WritableSheet ws) throws Exception {
		if (reportIDs != null) {
			title_Child = this.array_ChildTitle;
		}
		if ((title_Child == null) && (this.titleParam == null)) {
			return;
		}
		if ((title_Child != null) && (title_Child.length == 2)) {
			title_Child[0] = (Common.isNullStr(title_Child[0])) ? ""
					: title_Child[0];
			title_Child[1] = (Common.isNullStr(title_Child[1])) ? ""
					: title_Child[1];
		}
		if ((titleParam != null) && (titleParam.length == 2)) {
			titleParam[0] = (Common.isNullStr(titleParam[0])) ? ""
					: titleParam[0];
			titleParam[1] = (Common.isNullStr(titleParam[1])) ? ""
					: titleParam[1];
		}
		// 加小标题1
		int title1_FontSize = 10; // 标题字体大小
		String sFontName1 = "宋体";
		int iFontPos1 = 0;
		int title2_FontSize = 10; // 标题字体大小
		String sFontName2 = "宋体";
		// Alignment title2_Postion = (Alignment) Alignment.RIGHT;
		int iFontPos2 = 0;
		if ((eact.getConfigDataset() != null)
				&& !eact.getConfigDataset().isEmpty()
				&& eact.getConfigDataset().locate("REPORT_ID", reportID)) {
			// 如果数据集不为空
			if ("1".equals(Common.nonNullStr(eact.getConfigDataset()
					.fieldByName("REPORT_TITLE_A1_USE").getString()))) {
				title1_FontSize = eact.getConfigDataset().fieldByName(
						"REPORT_TITLE_A1_FONT_SIZE").getInteger();
				sFontName1 = eact.getConfigDataset().fieldByName(
						"REPORT_TITLE_A1_FONT").getString();
				iFontPos1 = eact.getConfigDataset().fieldByName(
						"REPORT_TITLE_A1_ORIENT").getInteger();
			}
			if ("1".equals(Common.nonNullStr(eact.getConfigDataset()
					.fieldByName("REPORT_TITLE_A2_USE").getString()))) {
				title2_FontSize = eact.getConfigDataset().fieldByName(
						"REPORT_TITLE_A2_FONT_SIZE").getInteger();
				sFontName2 = eact.getConfigDataset().fieldByName(
						"REPORT_TITLE_A2_FONT").getString();
				iFontPos2 = eact.getConfigDataset().fieldByName(
						"REPORT_TITLE_A2_ORIENT").getInteger();
			}
		}
		int colt1 = 0; // 小标题1所在的列数
		int colt2 = 0; // 小标题2所在的列数
		int colp1 = 0; // 标题参数1所在的列数
		int colp2 = 0; // 标题参数2所在的列数
		colt1 = getPosWithOrient(iFontPos1);
		colt2 = getPosWithOrient(iFontPos2);
		colp1 = getPosWithOrient(titleParamOrient[0]);
		colp2 = getPosWithOrient(titleParamOrient[1]);
		// 加标题1
		if (titleParam == null) {
			return;
		}
		if (titleParam.length != 2) {
			return;
		}
		if ((titleParamPostion[0] == 1) && (titleParamPostion[1] != 1)) {
			// 如果参数1挂靠的是标题1,参数2不挂靠
			if (iFontPos1 == titleParamOrient[0]) {
				// 如果参数1和标题1在一起
				ws.addCell(getLabel(colt1, 1, title_Child[0] + titleParam[0],
						iFontPos1, sFontName1, title1_FontSize));
			} else {
				// 参数1和标题1不在一起
				ws.addCell(getLabel(colt1, 1, title_Child[0], iFontPos1,
						sFontName1, title1_FontSize)); // 加小标题1
				ws.addCell(getLabel(colp1, 1, titleParam[0],
						titleParamOrient[0], sFontName1, title1_FontSize)); // 加参数1
			}
		} else if ((titleParamPostion[1] == 1) && (titleParamPostion[0] != 1)) {
			// 如果参数2挂靠的是标题1,参数1不挂靠
			if (iFontPos1 == titleParamOrient[1]) {
				// 如果参数2和标题1在一起
				ws.addCell(getLabel(colt1, 1, title_Child[0] + titleParam[1],
						iFontPos1, sFontName1, title1_FontSize));
			} else {
				// 参数2和标题1不在一起
				ws.addCell(getLabel(colt1, 1, title_Child[0], iFontPos1,
						sFontName1, title1_FontSize)); // 加小标题1
				ws.addCell(getLabel(colp1, 1, titleParam[1],
						titleParamOrient[1], sFontName1, title1_FontSize)); // 加参数2
			}
		} else if ((titleParamPostion[0] == 1) && (titleParamPostion[1] == 1)) {
			// 如果参数1,2都挂靠的都是标题1
			if ((iFontPos1 != titleParamOrient[0])
					&& (iFontPos1 != titleParamOrient[1])
					&& (titleParamOrient[0] != titleParamOrient[1])) {
				// 如果参数1，参数2，小标题1都不在一起
				ws.addCell(getLabel(colt1, 1, title_Child[0], iFontPos1,
						sFontName1, title1_FontSize)); // 加小标题1
				ws.addCell(getLabel(colp1, 1, titleParam[0],
						titleParamOrient[0], sFontName1, title1_FontSize)); // 加参数1
				ws.addCell(getLabel(colp2, 1, titleParam[1],
						titleParamOrient[1], sFontName1, title1_FontSize)); // 加参数2
			} else if ((iFontPos1 == titleParamOrient[0])
					&& (iFontPos1 != titleParamOrient[1])) {
				// 如果参数1，小标题1在一起，参数2不在一起
				ws.addCell(getLabel(colt1, 1, title_Child[0] + titleParam[0],
						iFontPos1, sFontName1, title1_FontSize)); // 加小标题1
				ws.addCell(getLabel(colp2, 1, titleParam[1],
						titleParamOrient[1], sFontName1, title1_FontSize)); // 加参数2
			} else if ((titleParamOrient[0] == titleParamOrient[1])
					&& (iFontPos1 != titleParamOrient[0])) {
				// 如果参数1，参数2在一起，小标题1不在一起
				ws.addCell(getLabel(colt1, 1, title_Child[0], iFontPos1,
						sFontName1, title1_FontSize)); // 加小标题1
				ws.addCell(getLabel(colp2, 1, titleParam[0] + titleParam[1],
						titleParamOrient[1], sFontName1, title1_FontSize)); // 加参数1
			} else if ((iFontPos1 == titleParamOrient[0])
					&& (iFontPos1 == titleParamOrient[1])) {
				// 如果参数1，小标题1，参数2都在一起
				ws
						.addCell(getLabel(colt1, 1, title_Child[0]
								+ titleParam[0] + titleParam[1], iFontPos1,
								sFontName1, title1_FontSize)); // 加小标题1
			}
		} else {
			// 如果参数1,2都不挂靠标题1
			if (Common.isNullStr(title_Child[0])) {
				title_Child[0] = titleParam[0];
			}
			ws.addCell(getLabel(colt1, 1, title_Child[0], iFontPos1,
					sFontName1, title1_FontSize)); // 加小标题1
		}

		// 加标题2
		if ((titleParamPostion[0] == 2) && (titleParamPostion[1] != 2)) {
			// 如果参数1挂靠的是标题2,参数2不挂靠
			if (iFontPos2 == titleParamOrient[0]) {
				// 如果参数1和标题2在一起
				ws.addCell(getLabel(colt2, 2, title_Child[1] + titleParam[0],
						iFontPos2, sFontName2, title2_FontSize));
			} else {
				// 参数1和标题2不在一起
				ws.addCell(getLabel(colt2, 2, title_Child[1], iFontPos2,
						sFontName2, title2_FontSize)); // 加小标题2
				ws.addCell(getLabel(colp1, 2, titleParam[0],
						titleParamOrient[0], sFontName2, title2_FontSize)); // 加参数1
			}
		} else if ((titleParamPostion[1] == 2) && (titleParamPostion[0] != 2)) {
			// 如果参数2挂靠的是标题2,参数1不挂靠
			if (iFontPos2 == titleParamOrient[1]) {
				// 如果参数2和标题2在一起
				ws.addCell(getLabel(colt2, 2, title_Child[1] + titleParam[1],
						iFontPos2, sFontName2, title2_FontSize));
			} else {
				// 参数2和标题2不在一起
				ws.addCell(getLabel(colt2, 2, title_Child[1], iFontPos2,
						sFontName2, title2_FontSize)); // 加小标题1
				ws.addCell(getLabel(colp2, 2, titleParam[1],
						titleParamOrient[1], sFontName2, title2_FontSize)); // 加参数2
			}
		} else if ((titleParamPostion[0] == 2) && (titleParamPostion[1] == 2)) {
			// 如果参数1,2都挂靠的都是标题2
			if ((iFontPos2 != titleParamOrient[0])
					&& (iFontPos2 != titleParamOrient[1])
					&& (titleParamOrient[0] != titleParamOrient[1])) {
				// 如果参数1，参数2，小标题2都不在一起
				ws.addCell(getLabel(colt2, 2, title_Child[1], iFontPos2,
						sFontName2, title2_FontSize)); // 加小标题1
				ws.addCell(getLabel(colp1, 2, titleParam[0],
						titleParamOrient[0], sFontName2, title2_FontSize)); // 加参数1
				ws.addCell(getLabel(colp2, 2, titleParam[1],
						titleParamOrient[1], sFontName2, title2_FontSize)); // 加参数2
			} else if ((iFontPos2 == titleParamOrient[0])
					&& (iFontPos2 != titleParamOrient[1])) {
				// 如果参数1，小标题2在一起，参数2不在一起
				ws.addCell(getLabel(colt2, 2, title_Child[1] + titleParam[0],
						iFontPos2, sFontName2, title2_FontSize)); // 加小标题1
				ws.addCell(getLabel(colp2, 2, titleParam[1],
						titleParamOrient[1], sFontName2, title2_FontSize)); // 加参数2
			} else if ((titleParamOrient[0] == titleParamOrient[1])
					&& (iFontPos2 != titleParamOrient[0])) {
				// 如果参数1，参数2在一起，小标题1不在一起
				ws.addCell(getLabel(colt2, 2, title_Child[1], iFontPos2,
						sFontName2, title2_FontSize)); // 加小标题1
				ws.addCell(getLabel(colp2, 2, titleParam[0] + titleParam[1],
						titleParamOrient[1], sFontName2, title2_FontSize)); // 加参数1
			} else if ((iFontPos2 == titleParamOrient[0])
					&& (iFontPos2 == titleParamOrient[1])) {
				// 如果参数1，小标题1，参数2都在一起
				ws
						.addCell(getLabel(colt2, 2, title_Child[1]
								+ titleParam[0] + titleParam[1], iFontPos2,
								sFontName2, title2_FontSize)); // 加小标题1
			}
		} else {
			// 如果参数1,2都不挂靠标题1
			if (Common.isNullStr(title_Child[1])) {
				title_Child[1] = titleParam[1];
			}
			ws.addCell(getLabel(colt2, 2, title_Child[1], iFontPos2,
					sFontName2, title2_FontSize)); // 加小标题1
		}
	}

	/**
	 * 根据对齐方式获取小标题应该在的列数
	 * 
	 * @param iorient
	 * @return
	 */
	private int getPosWithOrient(int iorient) {
		if (iorient == 0) {
			return 0;
		} else if (iorient == 2) {
			if (this.isCurFace) {
				return colsum;
			} else {
				return colsum - 1;
			}
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @param acol
	 *            列
	 * @param arow
	 *            行
	 * @param aname
	 *            名称
	 * @param aori
	 *            对齐方式
	 * @param fontname
	 *            字体名称
	 * @param fontsize
	 *            字体大小
	 * @return
	 * @throws Exception
	 */
	private Label getLabel(int acol, int arow, String aname, int aori,
			String fontname, int fontsize) throws Exception {
		WritableFont wf = new jxl.write.WritableFont(WritableFont
				.createFont(fontname), fontsize, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		jxl.write.Label labelC = new jxl.write.Label(acol, arow, aname);
		jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat();
		wcfFC.setFont(wf);
		labelC.setCellFormat(wcfFC);
		wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
		// wcfFC.setAlignment(title1_Postion);
		if (aori == 0) {
			wcfFC.setAlignment(Alignment.LEFT);
		} else if (aori == 2) {
			wcfFC.setAlignment(Alignment.RIGHT);
		} else {
			wcfFC.setAlignment(Alignment.CENTRE);
		}
		return labelC;
	}

	/**
	 * 添加表头数据
	 * 
	 * @param ws
	 * @param headerCell
	 * @throws Exception
	 */
	private void addTableHeader(WritableSheet ws, Object[][] headerCell)
			throws Exception {
		FileInputStream osInput = new FileInputStream(new File(tempFile));
		Workbook bookHeader = Workbook.getWorkbook(osInput);
		Sheet sheetHeader = bookHeader.getSheet(0);
		// 添加表头
		String sFontName = "宋体";
		int iFontSize = 10;
		int iline = 1;
		if ((eact.getConfigDataset() != null)
				&& !eact.getConfigDataset().isEmpty()
				&& eact.getConfigDataset().locate("REPORT_ID", reportID)) {
			sFontName = eact.getConfigDataset().fieldByName("REPORT_BODY_FONT")
					.getString();
			iFontSize = eact.getConfigDataset().fieldByName(
					"REPORT_BODY_FONT_SIZE").getInteger();
			iline = eact.getConfigDataset().fieldByName("REPORT_LINE")
					.getInteger();
		}
		int kk = 0;
		if (this.title_Child != null) {
			kk = title_Child.length;
		} else {
			title_Child = new String[] { "", "" };
			kk = 2;
		}
		if (!this.isNeedTitle) {
			kk = 0;
		}
		for (int ih = kk; ih < rowsum; ih++) {
			for (int jh = 0; jh < colsum; jh++) {
				WritableFont wf = new jxl.write.WritableFont(WritableFont
						.createFont(sFontName), iFontSize,
						WritableFont.NO_BOLD, false,
						UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
				jxl.write.Label labelC = null;
				if (this.isNeedTitle) {
					labelC = new jxl.write.Label(jh, ih + kk - 1,
							(headerCell[jh][ih - kk] == null) ? ""
									: headerCell[jh][ih - kk].toString());
				} else {
					labelC = new jxl.write.Label(jh, ih,
							(headerCell[jh][ih] == null) ? ""
									: headerCell[jh][ih].toString());
				}
				jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat();
				wcfFC.setWrap(true);
				wcfFC.setShrinkToFit(true);
				wcfFC.setFont(wf);
				labelC.setCellFormat(wcfFC);
				wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
				wcfFC.setBackground(ExportPropType.bg_BodyHeaderColor);
				if (1 == iline) {
					wcfFC.setBorder(Border.ALL, BorderLineStyle.THIN);
				} else {
					wcfFC.setBorder(Border.ALL, BorderLineStyle.NONE);
				}
				wcfFC.setAlignment(Alignment.CENTRE);
				ws.addCell(labelC);
			}
		}
	}

	/**
	 * 导出表体
	 * 
	 * @throws Exception
	 */
	private InfoPackage exportBody(WritableSheet ws, Object[][] headerCell,
			List listDataRow, int iHeaderRowCount) throws Exception {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		// 获取配置信息
		String sFontName = "宋体";
		int iFontSize = 10;
		String viewIsNull = "";
		int iline = 1;
		int iview = 0;
		// BorderLineStyle linetype = (BorderLineStyle) BorderLineStyle.THICK;
		if ((eact.getConfigDataset() != null)
				&& !eact.getConfigDataset().isEmpty()
				&& eact.getConfigDataset().locate("REPORT_ID", reportID)) {
			sFontName = eact.getConfigDataset().fieldByName("REPORT_BODY_FONT")
					.getString();
			iFontSize = eact.getConfigDataset().fieldByName(
					"REPORT_BODY_FONT_SIZE").getInteger();
			iline = eact.getConfigDataset().fieldByName("REPORT_LINE")
					.getInteger();
			iview = eact.getConfigDataset().fieldByName("REPORT_AUTONEXT")
					.getInteger();
			viewIsNull = eact.getConfigDataset().fieldByName("REPORT_NULLVIEW")
					.getString();
		}
		// 字符格式化
		jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat();
		WritableFont wf = new jxl.write.WritableFont(WritableFont
				.createFont(sFontName), iFontSize, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		wcfFC.setFont(wf);
		wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
		// wcfFC.setBorder(Border.ALL, linetype);
		if (0 == iline) {
			wcfFC.setBorder(Border.ALL, BorderLineStyle.NONE);
		} else {
			wcfFC.setBorder(Border.ALL, BorderLineStyle.THIN);
		}
		setCellType(Constants.LEFT, iview, wcfFC);
		// 空数字格式化
		jxl.write.WritableCellFormat wcffN[] = getFormats(iview);
		String[] numbers = null;
		if (exprops == null) {
			numbers = exprop.getNumberViewInTable();
		} else {
			numbers = exprops.getNumberViewInTable();
		}
		Arrays.sort(numbers);
		for (int k = 0; k < list.size(); k++) {
			if (list.get(k) == null) {
				continue;
			}
			for (int j = 0; j < colsum; j++) {
				if (j >= cols_EName.length) {
					continue;
				}
				Map map = null;
				// 浮点数格式化
				if (list.get(k) instanceof XMLData) {
					map = (XMLData) (list.get(k));
				}
				if (list.get(k) instanceof Map) {
					map = (Map) (list.get(k));
				}
				Object obj = null;
				if (this.bListBySql) {
					obj = map.get(cols_EName[j].toLowerCase());
				} else {
					obj = map.get(cols_EName[j]);
				}
				String coltype = cols_Type[j];
				if ((obj == null) || "NULL".equals(obj.toString().toUpperCase())) {
					if (Common.isNullStr(viewIsNull)) {
						// 如果库里没有设置数据为“0”时显示内容，则添加空
						Blank blank = new Blank(j, iHeaderRowCount);
						blank.setCellFormat(wcfFC);
						ws.addCell(blank);
					} else {
						// 如果设置了，则显示设置内容
						jxl.write.Label labelC = new jxl.write.Label(j,
								iHeaderRowCount, viewIsNull);
						labelC.setCellFormat(wcfFC);
						ws.addCell(labelC);
					}
					continue;
				}
				if (Common.isNullStr(coltype)) {
					coltype = "";
				}
				if (Arrays.binarySearch(numbers, coltype) >= 0) {
					String sValue = Common.nonNullStr(obj);
					if (Common.isNumber(Common.nonNullStr(obj))
							&& (Float.compare(0, Float.parseFloat(Common
									.nonNullStr(obj))) == 0)) {
						Blank blank = new Blank(j, iHeaderRowCount);
						blank.setCellFormat(wcfFC);
						ws.addCell(blank);
						continue;
					} else {
						int ib = sValue.indexOf(".");
						if (ib > 0) {
							int ll = sValue.substring(ib + 1, sValue.length())
									.length();
							if (ll > 2) {
								sValue = sValue.substring(0, ib + 3);
							}
						}
						sValue = Common.isNullStr(sValue) ? "0" : sValue;
						Number number = null;
						try {
							number = new Number(j, iHeaderRowCount, Double
									.parseDouble(sValue));
							number.setCellFormat(wcffN[j]);
						} catch (Exception e) {
							jxl.write.Label labelC = new jxl.write.Label(j,
									iHeaderRowCount, sValue);
							if ((wcffN != null) && (wcffN[j] != null)) {
								labelC.setCellFormat(wcffN[j]);
							}
							ws.addCell(labelC);
							continue;
						}
						double bb = Double.parseDouble(sValue);
						number.setCellFormat(wcffN[j]);
						number.setValue(bb);
						ws.addCell(number);
					}
				} else {
					jxl.write.Label labelC = new jxl.write.Label(j,
							iHeaderRowCount, "NULL".equals(obj.toString()
									.toUpperCase()) ? "" : obj.toString());
					labelC.setCellFormat(wcfFC);
					ws.addCell(labelC);
				}
			}
			iHeaderRowCount++;
		}
		return info;
	}

	/**
	 * 获取格式
	 * 
	 * @return
	 * @throws Exception
	 */
	private jxl.write.WritableCellFormat[] getFormats(int iview)
			throws Exception {
		jxl.write.WritableCellFormat[] sFormat = new jxl.write.WritableCellFormat[this.cols_Format.length];
		DataSet dsConfig = eact.getConfigDataset();
		int fontsize = 10;
		String font = "宋体";
		// int rowHeight = 10;
		if ((dsConfig != null) && !dsConfig.isEmpty()
				&& dsConfig.locate("REPORT_ID", this.reportID)
				&& !dsConfig.bof() && !dsConfig.eof()) {
			fontsize = dsConfig.fieldByName("REPORT_BODY_FONT_SIZE")
					.getInteger();
			font = dsConfig.fieldByName("REPORT_BODY_FONT").getString();
		}
		WritableFont font1 = new WritableFont(WritableFont.createFont(font),
				fontsize, WritableFont.NO_BOLD);
		for (int i = 0; i < colsum; i++) {
			jxl.write.NumberFormat nff = null;
			if (i >= cols_Format.length) {
				continue;
			}
			if ((cols_Format != null) && !Common.isNullStr(cols_Format[i])
					&& !"NULL".equals(cols_Format[i].toUpperCase())) {
				nff = new jxl.write.NumberFormat(cols_Format[i]);
			} else {
				nff = new jxl.write.NumberFormat("###,###");
			}
			jxl.write.WritableCellFormat wcffN = new jxl.write.WritableCellFormat(
					nff);
			wcffN.setBorder(Border.ALL, BorderLineStyle.THIN,
					jxl.format.Colour.BLACK);
			wcffN.setFont(font1);
			switch (iview) {
			case 0:
				// 没有格式
				break;
			case 1:
				// 自动换行
				wcffN.setWrap(true);
				break;
			case 2:
				// 缩小自动填充
				wcffN.setShrinkToFit(true);
				break;
			}
			sFormat[i] = wcffN;
		}
		return sFormat;
	}

	/**
	 * 创建表头
	 * 
	 * @param aNode
	 * @return
	 * @throws Exception
	 */
	private TableHeader createTableHeader(Node aNode) throws Exception {
		if (aNode == null) {
			return null;
		}
		TableHeader tableheader = new TableHeader(aNode);
		tableheader.setFont(new Font("宋体", Font.PLAIN, 12));
		tableheader.setColor(new Color(250, 228, 184));
		return tableheader;
	}

	/**
	 * 导出表头数据
	 * 
	 * @param tableheader
	 * @throws Exception
	 */
	private void exportTableHeader(TableHeader tableheader) throws Exception {
		Report report = new Report();
		report.setReportHeader(tableheader);
		ReportUI reportUI = new ReportUI(report);
		tempLastCols = getTableLastColInfo(reportUI, tableheader);
		ExcelExporter eep = new ExcelExporter(tempFile);
		reportUI.updateUI();
		eep.exportReport(reportUI.getReport());
	}

	/**
	 * 因为finereport导出时最后一列无法读取，故采取以下方式获得最后一列信息，权宜之策
	 * 
	 * @param reportUI
	 * @param tableheader
	 * @return
	 */
	private Object[] getTableLastColInfo(ReportUI reportUI,
			TableHeader tableheader) {
		int templastcol = tableheader.getColumns() - 1;
		int templastrow = tableheader.getRows();
		Object[] atempLastCols = new Object[templastrow];
		for (int temp = 0; temp < templastrow; temp++) {
			atempLastCols[temp] = reportUI.getReportContent().getCellElement(
					templastcol, temp).getValue();
		}
		return atempLastCols;
	}

	/**
	 * 批量导出的执行函数(带配置格式的)
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean doBatchExport() throws Exception {
		if ((nodes.length != sql_Bodys.length)
				|| (sql_Bodys.length != sql_Titles.length)
				|| (sql_Bodys.length != titles.length)) {
			return false;
		}
		if ((nodes.length == 0) && (exprops.getFaceData() == null)) {
			return true;
		}
		sFilePath = getFilePath();
		if (Common.isNullStr(sFilePath)) {
			return false;
		}
		WritableWorkbook wwb = Workbook.createWorkbook(new File(sFilePath));
		int iBatch = nodes.length;
		boolean isEnableExportFace = false;
		WritableSheet wsface = null;
		WritableSheet[] wsBefore = new WritableSheet[2];
		if ((exprops.getFaceData() != null) && (exprops.getFaceData().length != 0)
				&& (exprops.getFaceData()[0] != null)
				&& !exprops.getFaceData()[0].isEmpty()) {
			wsface = wwb.createSheet("封面", 0);
			wsBefore[0] = wsface;
			// 导出封面
		}
		WritableSheet wsmulu = null;
		if ((exprops.getFaceData() != null) && (exprops.getFaceData().length != 0)
				&& (exprops.getFaceData().length == 2)
				&& (exprops.getFaceData()[1] != null)
				&& !exprops.getFaceData()[1].isEmpty()) {
			wsmulu = wwb.createSheet(sql_Titles[1], 1);
			wsBefore[1] = wsmulu;
			// 导出封面
		}
		isEnableExportFace = exportfaceSG(wsBefore, 0);
		for (int i = 0; i < iBatch; i++) {
			if (!exportHeader(i)) {
				return false;
			}
			// 导出封面
			if (!Common.isNullStr(sql_Titles[i])) {
				list_Title = itserv.executeQuery(sql_Titles[i]);
			} else {
				return false;
			}
			List list_doBefore = null;
			if ((exprops.getSqls_BeforeGetBody() != null)
					&& (exprops.getSqls_BeforeGetBody().get(i) != null)) {
				list_doBefore = (List) exprops.getSqls_BeforeGetBody().get(i);
			}
			List list_doAfter = null;
			if ((exprops.getSqls_AfterGetBody() != null)
					&& (exprops.getSqls_AfterGetBody().get(i) != null)) {
				list_doAfter = (List) exprops.getSqls_AfterGetBody().get(i);
			}
			if (!Common.isNullStr(sql_Bodys[i])) {
				list = itserv.executeQueryWithInfo(sql_Bodys[i], list_doBefore,
						list_doAfter);
			}
			setBatchCols(i);
			if (list_Title.size() == 0) {
				return false;
			}
			// 获取表头的区域
			Object[][] headerCell = getTableHeaderCells(rowsum, colsum);
			WritableSheet ws = null;
			if (isEnableExportFace) {
				ws = wwb.createSheet(titles[i], i
						+ exprops.getFaceData().length); // 该地方加1是因为已经有个表头
			} else {
				ws = wwb.createSheet(titles[i], i); // 该地方加1是因为已经有个表头
			}

			// 添加标题
			if ((titles_child != null) && (titles_child.size() != 0)) {
				array_ChildTitle = (String[]) titles_child.get(i);
			} else {
				array_ChildTitle = new String[2];
			}
			addTableTitle(ws, titles[i]);

			// 添加表头
			addTableHeader(ws, headerCell);

			// // 获得该表的格式化数据
			// format = (String[]) formats.get(i);
			// 导出表体
			if (list != null) {
				exportBody(ws, headerCell, list, rowsum + 1);
			}

			// 合并标题
			for (int pp = 0; pp < ExportPropType.num_ChildTitlesCount + 1; pp++) {
				ws.mergeCells(0, pp, colsum - 1, pp);
			}
			ws.setRowView(0, 600);

			// 合并单元格
			eact.mergeCells(isNeedTitle, ws, headerCell, rowsum, colsum);
			this.ps = ws.getSettings();
			this.setPaperPS(eact.getConfigDataset(), reportID);
			// 设置列宽
			// int[] nowWidth = (int[]) lwidth.get(i);
			// setWidth(ws, nowWidth);
		}
		// 写入excel
		wwb.write();
		// 关闭文件
		wwb.close();
		itserv.closeSession();
		return true;
	}

	/**
	 * 导出表头
	 * 
	 * @throws Exception
	 */
	private boolean exportHeader(int i) throws Exception {
		if (Common.isNullStr(tempFile)) {
			return false;
		}
		OutputStream osh = new FileOutputStream(tempFile);
		TableHeader tableheader = createTableHeader(nodes[i]);
		colsum = tableheader.getColumns();
		rowsum = tableheader.getRows();
		exportTableHeader(tableheader);
		osh.close();
		return true;
	}

	/**
	 * 添加标题 带配置信息(单表)
	 * 
	 * @param ws
	 * @param title
	 * @throws WriteException
	 */
	private void addTableTitle(WritableSheet ws, String title) throws Exception {
		for (int jh = 0; jh < colsum; jh++) {
			jxl.write.WritableFont wfc = new jxl.write.WritableFont(
					WritableFont.ARIAL, 14, WritableFont.BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat();
			jxl.write.Label labelC = new jxl.write.Label(jh, 0, title);
			wcfFC.setAlignment(Alignment.CENTRE);
			wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcfFC.setShrinkToFit(true);
			wcfFC.setFont(wfc);
			labelC.setCellFormat(wcfFC);
			ws.addCell(labelC);
		}
		addTableChildTitile(ws);
		rowsum = rowsum + array_ChildTitle.length;
	}

	/**
	 * 导出封面
	 */
	public boolean exportConvert(String title) throws Exception {
		sFilePath = getFilePath();
		if (Common.isNullStr(sFilePath)) {
			return false;
		}
		WritableWorkbook wwb = Workbook.createWorkbook(new File(sFilePath));
		WritableSheet wsface = wwb.createSheet(title, 0);
		// 导出封面
		boolean isEnableExportFace = exportfaceSG(wsface, 0);
		// 写入excel
		wwb.write();
		// 关闭文件
		wwb.close();
		return isEnableExportFace;
	}

	/**
	 * 导出封面--一个各自，设置行宽，列高,忽略单元格
	 * 
	 * @param ws
	 */
	private boolean exportfaceSG(WritableSheet ws, int k) {
		try {
			addFaceLabel(ws, dsFace[0]);
			setFaceSettings(ws, dsFace[0]);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return true;
	}

	/**
	 * 导出封面--一个各自，设置行宽，列高,忽略单元格
	 * 
	 * @param ws
	 */
	private boolean exportfaceSG(WritableSheet[] ws, int k) {
		if ((exprops.getFaceData() == null) || (exprops.getFaceData().length == 0)) {
			return false;
		}
		try {
			// 添加单元格
			for (int i = 0; i < ws.length; i++) {
				if (ws[i] == null) {
					continue;
				}
				addFaceLabel(ws[i], exprops.getFaceData()[i]);
				setFaceSettings(ws[i], exprops.getFaceData()[i]);
			}
		} catch (Exception ee) {
			this.sErrorInfo = "封面导出失败" + ee.getMessage();
			ee.printStackTrace();
		}
		return true;
	}

	/**
	 * 添加封面及目录的内容
	 * 
	 * @param ws
	 * @param dsConfig
	 * @throws Exception
	 */
	private void addFaceLabel(WritableSheet ws, DataSet dsConfig)
			throws Exception {
		if (ws == null) {
			return;
		}
		if (dsConfig == null) {
			return;
		}
		if (dsConfig.isEmpty()) {
			return;
		}
		dsConfig.beforeFirst();
		while (dsConfig.next()) {
			jxl.write.WritableFont wfc = new jxl.write.WritableFont(
					WritableFont.ARIAL, (int) (dsConfig
							.fieldByName("FIELD_FONTSIZE").getInteger()),
					WritableFont.NO_BOLD, false,
					jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat();
			jxl.write.Label labelC = new jxl.write.Label(dsConfig.fieldByName(
					"FIELD_COLUMN").getInteger(), dsConfig.fieldByName(
					"FIELD_ROW").getInteger(), dsConfig.fieldByName(
					"FIELD_VALUE").getString());
			wcfFC.setWrap(true);
			wcfFC.setBorder(Border.ALL, BorderLineStyle.NONE);
			wcfFC.setBackground(ExportPropType.bg_Face);
			int sizeFace = dsConfig.fieldByName(ExportPropType.size_FieldName)
					.getInteger();
			switch (sizeFace) {
			case Constants.CENTER:
				wcfFC.setAlignment(Alignment.CENTRE);
				break;
			case Constants.LEFT:
				wcfFC.setAlignment(Alignment.LEFT);
				break;
			case Constants.RIGHT:
				wcfFC.setAlignment(Alignment.RIGHT);
				break;
			default:
				break;
			}
			wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcfFC.setShrinkToFit(true);
			wcfFC.setFont(wfc);
			labelC.setCellFormat(wcfFC);
			ws.addCell(labelC);
		}
	}

	/**
	 * 合并封面及目录的单元格及设置行高列宽
	 * 
	 * @param ws
	 * @param dsConfig
	 * @throws Exception
	 */
	private void setFaceSettings(WritableSheet ws, DataSet dsConfig)
			throws Exception {
		// 设置列宽及合并单元格
		String reportid = null;
		dsConfig.beforeFirst();
		while (dsConfig.next()) {
			int col = dsConfig.fieldByName("FIELD_COLUMN").getInteger();
			int row = dsConfig.fieldByName("FIELD_ROW").getInteger();
			int colspan = dsConfig.fieldByName("FIELD_COLUMNSPAN").getInteger();
			int rowspan = dsConfig.fieldByName("FIELD_ROWSPAN").getInteger();
			reportid = dsConfig.fieldByName("REPORT_ID").getString();
			// 合并单元格子
			ws.mergeCells(col, row, col + colspan - 1, row + rowspan - 1);
		}
		// 设置列宽
		String sql = "select field_column,max(FIELD_COLWIDTH) as width from fb_u_qr_szzb"
				+ " where report_id = '"
				+ reportid
				+ "'"
				+ " group by field_column" + " order by field_column";
		DataSet ds = DBSqlExec.client().getDataSet(sql);
		if ((ds != null) && !ds.isEmpty()) {
			ds.beforeFirst();
			while (ds.next()) {
				ws.setColumnView(ds.fieldByName("field_column").getInteger(),
						ds.fieldByName("width").getInteger() / 6);
			}
		}
		// 设置行高
		sql = "select FIELD_ROW,max(FIELD_ROWHEIGHT) as height from fb_u_qr_szzb"
				+ " where report_id = '"
				+ reportid
				+ "'"
				+ " group by FIELD_ROW" + " order by FIELD_ROW";
		ds = DBSqlExec.client().getDataSet(sql);
		if ((ds != null) && !ds.isEmpty()) {
			ds.beforeFirst();
			while (ds.next()) {
				ws.setRowView(ds.fieldByName("FIELD_ROW").getInteger(), ds
						.fieldByName("height").getInteger() * 18);
			}
		}
		ps = ws.getSettings();
		setPaperPS(eact.getConfigDataset(), reportid);
	}

	/**
	 * 导出封面
	 */
	public ExportToExcel(String reportID, ExportProp pp) {
		EXPORTBYTYPE = 5;
		isCurFace = true;
		this.reportID = reportID;
		exprop = pp;
	}

	/**
	 * 导出封面
	 * 
	 * @return
	 */
	private boolean doExportOldFace() {
		try {
			lstConfig = eact.getConfigDataset(reportID);
			boolean isUseModel = false;
			if ((lstConfig != null) && (lstConfig.size() != 0)) {
				isUseModel = "1".equals(((Map) lstConfig.get(0))
						.get("IS_USEMODEL".toLowerCase()));
			}
			DataSet dsConfig = eact.getConfigDataset();
			if (dsConfig.locate("REPORT_ID", reportID)) {
				title = eact.getTitleWithDataSet(reportID);
			} else {
				title = exprop.getTitle();
			}
			sFilePath = getFilePath();
			if (Common.isNullStr(sFilePath)) {
				return false;
			}
			WritableWorkbook wwb = Workbook.createWorkbook(new File(sFilePath));
			// 这个地方要判断列宽
			String sql = "SELECT MAX(FIELD_COLUMN+FIELD_COLUMNSPAN) maxcolumn FROM fb_u_qr_szzb where report_id='"
					+ reportID + "'";
			DataSet dsMaxColumn = ExportStub.getMethod().getDataset(sql);
			if ((dsMaxColumn != null) && !dsMaxColumn.isEmpty()) {
				dsMaxColumn.beforeFirst();
				dsMaxColumn.next();
				this.colsum = dsMaxColumn.fieldByName("maxcolumn").getInteger();
			} else {
				this.colsum = 2;
			}
			this.cols_Format = exprop.getFormats();
			this.cols_Type = exprop.getTypes();
			this.titleParam = exprop.getTitle_Child();
			if (!isUseModel) {
				this.title_Child = eact.getTitle_ChildWithDataSet();
				setConfigByDataSet(dsConfig);
				if (title_Child == null) {
					title_Child = new String[2];
				}
				this.dsFace = exprop.getDsFace();

				WritableSheet wsface = wwb.createSheet(title, 0);
				// 导出封面
				exportLikeFaceSG(wsface, 0);
				addTableTitle(wsface);
				if ((eact.getConfigDataset() != null)
						&& !eact.getConfigDataset().isEmpty()
						&& eact.getConfigDataset().eof()
						&& !eact.getConfigDataset().bof()) {
					wsface.setRowView(0, eact.getConfigDataset().fieldByName(
							"REPORT_TITLE_HEIGHT").getInteger());
				}
				wsface.mergeCells(0, 0, colsum, 0);
				this.ps = wsface.getSettings();
				this.setPaperPS(dsConfig, reportID);
			} else {
				// 如果导出了封面则从封面后导出，否则直接从第一个导出
				Sheet wsi = null;
				WritableSheet ws = null;
				// 取文件的时候要注意的是，先从本机找，如果本机找不到，则从库里找，找到后再保存到本机特定地址
				String reportname = null;
				String modifytime = null;
				// lstConfig = eact.getConfigDataset(reportID);
				if ((lstConfig != null) && !lstConfig.isEmpty()) {
					// // 如果数据集不为空
					reportname = Common.nonNullStr(((XMLData) eact
							.getConfigDataset(reportID).get(0))
							.get("report_title"));
					modifytime = Common.nonNullStr(((XMLData) eact
							.getConfigDataset(reportID).get(0))
							.get("modifytime"));
				}
				if (getModelFile(reportID, reportname, modifytime)) {
					InputStream is = new FileInputStream(new File(efile
							+ reportname + ".XLS"));
					Workbook rwb = Workbook.getWorkbook(is);
					wsi = rwb.getSheet(0);
//					wwb.importSheet(reportname, 0, wsi);
					is.close();
					ws = wwb.getSheet(0);
					setDataCellFormats(ws);
				} else {
					this.sErrorInfo = "没有配置该表的格式信息,初始化失败";
					return false;
				}
				addTableTitleWithModel(ws, 0);
				// 导出封面
				exportLikeFaceWithModel(ws, 0);
			}
			// 写入excel
			wwb.write();
			// 关闭文件
			wwb.close();
		} catch (Exception ee) {
			this.sErrorInfo = "导出失败";
			ee.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 导出封面--一个各自，设置行宽，列高,忽略单元格
	 * 
	 * @param ws
	 */
	private boolean exportLikeFaceSG(WritableSheet ws, int k) throws Exception {
		if (k != 0) {
			return false;
		}
		if (dsFace == null) {
			return false;
		}
		// addTableChildTitile(ws);
		// rowsum = rowsum + title_Child.length;
		String[] saNumber = exprop.getNumberViewInTable();
		DataSet dsConfig = eact.getConfigDataset();
		int iview = 0;
		String nullview = null;
		if ((dsConfig != null) && !dsConfig.isEmpty()
				&& dsConfig.locate("REPORT_ID", reportID)) {
			iview = eact.getConfigDataset().fieldByName("REPORT_AUTONEXT")
					.getInteger();
			nullview = dsConfig.fieldByName("REPORT_NULLVIEW").getString();
		}
		try {
			Arrays.sort(saNumber);
			// 添加单元格
			dsFace[0].beforeFirst();
			while (dsFace[0].next()) {
				int colnow = dsFace[0].fieldByName("FIELD_COLUMN").getInteger();
				if ((Arrays.binarySearch(saNumber, (cols_Type == null) ? ""
						: this.cols_Type[colnow]) >= 1)
						&& Common.isNumber(dsFace[0].fieldByName("FIELD_VALUE")
								.getString())
						&& !Common.isNullStr(dsFace[0].fieldByName(
								"FIELD_VALUE").getString())) {
					// 如果是数字需要格式化
					String value = dsFace[0].fieldByName("FIELD_VALUE")
							.getString();
					value = (Common.isNullStr(value)) ? nullview : value;
					Number number = new Number(dsFace[0].fieldByName(
							"FIELD_COLUMN").getInteger(), dsFace[0]
							.fieldByName("FIELD_ROW").getInteger() + 2, Float
							.parseFloat(value));
					double bb = 0;
					if (!Common.isNullStr(value)) {
						bb = Double.parseDouble(value);
					}
					FontName font = WritableFont.createFont("宋体");
					WritableFont font1 = new WritableFont(font, 10,
							WritableFont.NO_BOLD);
					jxl.write.NumberFormat nff = null;
					if (this.cols_Format != null) {
						nff = new jxl.write.NumberFormat(
								this.cols_Format[colnow]);
					} else {
						nff = new jxl.write.NumberFormat("#,##0.00");
					}
					jxl.write.WritableCellFormat wcffN = new jxl.write.WritableCellFormat(
							nff);
					wcffN.setBorder(Border.ALL, BorderLineStyle.THIN,
							jxl.format.Colour.BLACK);
					wcffN.setAlignment(Alignment.RIGHT);
					wcffN.setFont(font1);
					number.setCellFormat(wcffN);
					if (!Common.isNullStr(value)) {
						number.setValue(bb);
					}
					if (Common.isNullStr(Common.nonNullStr(value))
							|| (Float.parseFloat(Common.nonNullStr(value)) == 0)) {
						Blank blank = new Blank(dsFace[0].fieldByName(
								"FIELD_COLUMN").getInteger(), dsFace[0]
								.fieldByName("FIELD_ROW").getInteger() + 2);
						blank.setCellFormat(wcffN);
						ws.addCell(blank);
					} else {
						setCellType(0, iview, wcffN);
						ws.addCell(number);
					}
				} else {
					jxl.write.WritableFont wfc = new jxl.write.WritableFont(
							WritableFont.ARIAL,
							(int) (dsFace[0].fieldByName("FIELD_FONTSIZE")
									.getInteger()), WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat();
					jxl.write.Label labelC = new jxl.write.Label(
							dsFace[0].fieldByName("FIELD_COLUMN").getInteger(),
							dsFace[0].fieldByName("FIELD_ROW").getInteger() + 2,
							dsFace[0].fieldByName("FIELD_VALUE").getString());
					wcfFC.setWrap(true);
					wcfFC.setBorder(Border.ALL, BorderLineStyle.THIN);
					wcfFC.setBackground(Colour.WHITE);
					int sizeFace = dsFace[0].fieldByName(
							ExportPropType.size_FieldName).getInteger();
					wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
					wcfFC.setFont(wfc);
					setCellType(sizeFace, iview, wcfFC);
					labelC.setCellFormat(wcfFC);
					ws.addCell(labelC);
				}
			}
			// 设置列宽及合并单元格
			dsFace[0].beforeFirst();
			while (dsFace[0].next()) {
				int col = dsFace[0].fieldByName("FIELD_COLUMN").getInteger();
				int row = dsFace[0].fieldByName("FIELD_ROW").getInteger();
				int colspan = dsFace[0].fieldByName("FIELD_COLUMNSPAN")
						.getInteger();
				int rowspan = dsFace[0].fieldByName("FIELD_ROWSPAN")
						.getInteger();
				// 设置列宽
				ws.setColumnView(col, (int) (dsFace[0]
						.fieldByName("FIELD_COLWIDTH").getInteger()) / 6);
				ws.setRowView(row, (int) (dsFace[0]
						.fieldByName("FIELD_ROWHEIGHT").getInteger()) * 18);
				ws.mergeCells(col, row + 2, col + colspan - 1, row + 2
						+ rowspan - 1);
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return true;
	}

	/**
	 * 设置单元格子
	 * 
	 * @param sizeFace
	 * @param iview
	 * @param wcfFC
	 * @throws Exception
	 */
	private void setCellType(int sizeFace, int iview,
			jxl.write.WritableCellFormat wcfFC) throws Exception {
		switch (iview) {
		case 0:
			// 没有格式
			break;
		case 1:
			// 自动换行
			wcfFC.setWrap(true);
			break;
		case 2:
			// 缩小自动填充
			wcfFC.setShrinkToFit(true);
			break;
		}
		switch (sizeFace) {
		case Constants.CENTER:
			wcfFC.setAlignment(Alignment.CENTRE);
			break;
		case Constants.LEFT:
			wcfFC.setAlignment(Alignment.LEFT);
			break;
		case Constants.RIGHT:
			wcfFC.setAlignment(Alignment.RIGHT);
			break;
		default:
			break;
		}
		wcfFC.setShrinkToFit(true);
	}

	/**
	 * 导出封面--一个各自，设置行宽，列高,忽略单元格
	 * 
	 * @param ws
	 */
	private boolean exportLikeFace(WritableSheet ws, int k) throws Exception {
		if (k != 0) {
			return false;
		}
		if ((dsLikeFace == null) || dsLikeFace.isEmpty()) {
			this.sErrorInfo = "数据内容为空";
			return false;
		}
		DataSet dsConfig = eact.getConfigDataset();
		int iview = 0;
		String nullview = null;
		if ((dsConfig != null) && !dsConfig.isEmpty()
				&& dsConfig.locate("REPORT_ID", reportID)) {
			iview = dsConfig.fieldByName("REPORT_AUTONEXT").getInteger();
			nullview = eact.getConfigDataset().fieldByName("REPORT_NULLVIEW")
					.getString();
		}
		try {
			Arrays.sort(exprops.getNumberViewInTable());
			// 添加单元格
			dsLikeFace.beforeFirst();
			while (dsLikeFace.next()) {
				int colnow = dsLikeFace.fieldByName("FIELD_COLUMN")
						.getInteger();
				if (Common.isNumber(dsLikeFace.fieldByName("FIELD_VALUE")
						.getString())
						&& !Common.isNullStr(dsLikeFace.fieldByName(
								"FIELD_VALUE").getString())) {
					// 如果是数字需要格式化
					String value = dsLikeFace.fieldByName("FIELD_VALUE")
							.getString();
					value = (Common.isNullStr(value)) ? nullview : value;
					Number number = new Number(dsLikeFace.fieldByName(
							"FIELD_COLUMN").getInteger(), dsLikeFace
							.fieldByName("FIELD_ROW").getInteger() + 2, Float
							.parseFloat(value));
					double bb = 0;
					if (!Common.isNullStr(value)) {
						bb = Double.parseDouble(value);
					}
					FontName font = WritableFont.createFont("宋体");
					WritableFont font1 = new WritableFont(font, 10,
							WritableFont.NO_BOLD);
					jxl.write.NumberFormat nff = null;
					if ((cols_Format != null) && (cols_Format.length != 0)
							&& (colnow < cols_Format.length)
							&& (this.cols_Format[colnow] != null)) {
						nff = new jxl.write.NumberFormat(
								this.cols_Format[colnow]);
					} else if (bb == 0) {
						nff = new jxl.write.NumberFormat("#");
					} else {
						nff = new jxl.write.NumberFormat("#,###.00");
					}
					jxl.write.WritableCellFormat wcffN = new jxl.write.WritableCellFormat(
							nff);
					wcffN.setBorder(Border.ALL, BorderLineStyle.THIN,
							jxl.format.Colour.BLACK);
					wcffN.setFont(font1);
					number.setCellFormat(wcffN);
					if (!Common.isNullStr(value)) {
						number.setValue(bb);
					}
					if (Common.isNullStr(Common.nonNullStr(value))
							|| (Float.parseFloat(Common.nonNullStr(value)) == 0)) {
						Blank blank = new Blank(dsLikeFace.fieldByName(
								"FIELD_COLUMN").getInteger(), dsLikeFace
								.fieldByName("FIELD_ROW").getInteger() + 2);
						blank.setCellFormat(wcffN);
						ws.addCell(blank);
					} else {
						setCellType(1, iview, wcffN);
						wcffN.setShrinkToFit(true);
						ws.addCell(number);
					}
				} else {
					jxl.write.WritableFont wfc = new jxl.write.WritableFont(
							WritableFont.ARIAL,
							(int) (dsLikeFace.fieldByName("FIELD_FONTSIZE")
									.getInteger()), WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat();
					jxl.write.Label labelC = new jxl.write.Label(
							dsLikeFace.fieldByName("FIELD_COLUMN").getInteger(),
							dsLikeFace.fieldByName("FIELD_ROW").getInteger() + 2,
							dsLikeFace.fieldByName("FIELD_VALUE").getString());
					wcfFC.setWrap(true);
					wcfFC.setBorder(Border.ALL, BorderLineStyle.THIN);
					wcfFC.setBackground(Colour.WHITE);
					int sizeFace = dsLikeFace.fieldByName(
							ExportPropType.size_FieldName).getInteger();
					// if (sizeFace > 2)
					// sizeFace = 0;
					// wcfFC.setAlignment(ExportPropType.size_Face[sizeFace]);
					wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
					wcfFC.setShrinkToFit(true);
					wcfFC.setFont(wfc);
					setCellType(sizeFace, iview, wcfFC);
					labelC.setCellFormat(wcfFC);
					wcfFC.setShrinkToFit(true);
					ws.addCell(labelC);
				}
			}
			// 设置列宽及合并单元格
			dsLikeFace.beforeFirst();
			while (dsLikeFace.next()) {
				int col = dsLikeFace.fieldByName("FIELD_COLUMN").getInteger();
				int row = dsLikeFace.fieldByName("FIELD_ROW").getInteger();
				int colspan = dsLikeFace.fieldByName("FIELD_COLUMNSPAN")
						.getInteger();
				int rowspan = dsLikeFace.fieldByName("FIELD_ROWSPAN")
						.getInteger();
				// 设置列宽
				ws.setColumnView(col, (int) (dsLikeFace
						.fieldByName("FIELD_COLWIDTH").getInteger()) / 6);
				ws.setRowView(row, (int) (dsLikeFace
						.fieldByName("FIELD_ROWHEIGHT").getInteger()) * 18);
				ws.mergeCells(col, row + 2, col + colspan - 1, row + 2
						+ rowspan - 1);
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return true;
	}

	/**
	 * 导出封面
	 */
	public ExportToExcel(String[] reportIDs, ExportBatchProp pp) {
		try {
			EXPORTBYTYPE = 6;
			this.exprops = pp;
			sFilePath = getFilePath();
			if (Common.isNullStr(sFilePath)) {
				return;
			}
			WritableWorkbook wwb = Workbook.createWorkbook(new File(sFilePath));
			for (int i = 0; i < reportIDs.length; i++) {
				this.reportID = reportIDs[i];
				// 这个地方要判断列宽
				String sql = "SELECT MAX(FIELD_COLUMN+FIELD_COLUMNSPAN) maxcolumn FROM fb_u_qr_szzb where report_id = '"
						+ reportIDs[i] + "'";
				DataSet dsMaxColumn = ExportStub.getMethod().getDataset(sql);
				if ((dsMaxColumn != null) && !dsMaxColumn.isEmpty()) {
					dsMaxColumn.beforeFirst();
					dsMaxColumn.next();
					this.colsum = dsMaxColumn.fieldByName("maxcolumn")
							.getInteger() - 1;
				} else {
					this.colsum = 2;
				}
				this.cols_Type = (String[]) exprops.getTypes().get(i);
				this.cols_Format = (String[]) exprops.getFormats().get(i);
				this.title = exprops.getTitles()[i];
				this.titleParam = (String[]) exprops.getTitle_Childs().get(i);
				this.title_Child = eact.getTitle_ChildWithDataSet();
				this.dsFace = exprops.getFaceData();
				WritableSheet wsface = wwb.createSheet(title, i);
				// 导出封面
				exportBatchLikeFaceSG(wsface, 0);
				wsface.mergeCells(0, 0, 10, 0);
			} // 写入excel
			wwb.write();
			// 关闭文件
			wwb.close();
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	/**
	 * 导出封面--一个各自，设置行宽，列高,忽略单元格
	 * 
	 * @param ws
	 */
	private boolean exportBatchLikeFaceSG(WritableSheet ws, int k)
			throws Exception {
		if (k != 0) {
			return false;
		}
		if (dsFace == null) {
			return false;
		}
		addTableTitle(ws);
		try {
			Arrays.sort(this.exprops.getNumberViewInTable());
			// 添加单元格
			for (int i = 0; i < dsFace.length; i++) {
				dsFace[i].beforeFirst();
				while (dsFace[i].next()) {
					int colnow = dsFace[0].fieldByName("FIELD_COLUMN")
							.getInteger();
					if ((Arrays.binarySearch(exprop.getNumberViewInTable(),
							this.cols_Type[colnow - 1]) >= 1)
							&& Common.isInteger(dsFace[0].fieldByName(
									"FIELD_VALUE").getString())) {
						// 如果是数字需要格式化
						String value = dsFace[i].fieldByName("FIELD_VALUE")
								.getString();
						value = (Common.isNullStr(value)) ? eact
								.getConfigDataset().fieldByName(
										"REPORT_NULLVIEW").getString() : value;
						Number number = new Number(dsFace[i].fieldByName(
								"FIELD_COLUMN").getInteger() - 1, dsFace[i]
								.fieldByName("FIELD_ROW").getInteger() + 2,
								Double.parseDouble(value));
						double bb = 0;
						if (!Common.isNullStr(value)) {
							bb = Double.parseDouble(value);
						}
						FontName font = WritableFont.createFont("宋体");
						WritableFont font1 = new WritableFont(font, 10,
								WritableFont.NO_BOLD);
						jxl.write.NumberFormat nff = null;
						nff = new jxl.write.NumberFormat(
								this.cols_Format[colnow - 1]);
						jxl.write.WritableCellFormat wcffN = new jxl.write.WritableCellFormat(
								nff);
						wcffN.setBorder(Border.ALL, BorderLineStyle.THIN,
								jxl.format.Colour.BLACK);
						wcffN.setFont(font1);
						number.setCellFormat(wcffN);
						if (!Common.isNullStr(value)) {
							number.setValue(bb);
						}
						ws.addCell(number);
					} else {
						jxl.write.WritableFont wfc = new jxl.write.WritableFont(
								WritableFont.ARIAL, (int) (dsFace[i]
										.fieldByName("FIELD_FONTSIZE")
										.getInteger()), WritableFont.NO_BOLD,
								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
								jxl.format.Colour.BLACK);
						jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat();
						jxl.write.Label labelC = new jxl.write.Label(
								dsFace[i].fieldByName("FIELD_COLUMN")
										.getInteger() - 1,
								dsFace[i].fieldByName("FIELD_ROW").getInteger() + 2,
								dsFace[i].fieldByName("FIELD_VALUE")
										.getString());
						wcfFC.setWrap(true);
						wcfFC.setBorder(Border.ALL, BorderLineStyle.THIN);
						wcfFC.setBackground(Colour.WHITE);
						int sizeFace = dsFace[i].fieldByName(
								ExportPropType.size_FieldName).getInteger();
						setCellType(sizeFace, Constants.LEFT, wcfFC);
						wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
						wcfFC.setShrinkToFit(true);
						wcfFC.setFont(wfc);
						labelC.setCellFormat(wcfFC);
						ws.addCell(labelC);
					}
				}
				// 设置列宽及合并单元格
				dsFace[i].beforeFirst();
				while (dsFace[i].next()) {
					int col = dsFace[i].fieldByName("FIELD_COLUMN")
							.getInteger() - 1;
					int row = dsFace[i].fieldByName("FIELD_ROW").getInteger();
					int colspan = dsFace[i].fieldByName("FIELD_COLUMNSPAN")
							.getInteger();
					int rowspan = dsFace[i].fieldByName("FIELD_ROWSPAN")
							.getInteger();
					// 设置列宽
					ws.setColumnView(col, (int) (dsFace[i]
							.fieldByName("FIELD_COLWIDTH").getInteger()) / 6);
					ws.setRowView(row, (int) (dsFace[i]
							.fieldByName("FIELD_ROWHEIGHT").getInteger()) * 18);
					ws.mergeCells(col, row + 2, col + colspan - 1, row + 2
							+ rowspan - 1);
				}
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return true;
	}

	// ///////////////////////////////////////////////////////
	// ///多表导出
	// ///////////////////////////////////////////////////////

	/**
	 * 多表导出
	 * 
	 * @param iReportTypes
	 *            报表类型标识符 从ReportType类里取
	 * @param oReportTypes
	 *            报表类型: 现在支持的有：REDsLst,RELikeFace,RESql
	 */
	public ExportToExcel(int[] aReportTypeTags, Object[] aReportTypes,
			ExportBatchProp props) {
		this(aReportTypeTags, aReportTypes, props, true);
		// this.iReportTypes = aReportTypeTags;
		// this.oReportTypes = aReportTypes;
		// this.exprops = props;
		// this.EXPORTBYTYPE = 9;
	}

	/**
	 * 多表导出
	 * 
	 * @param iReportTypes
	 *            报表类型标识符 从ReportType类里取
	 * @param oReportTypes
	 *            报表类型: 现在支持的有：REDsLst,RELikeFace,RESql
	 */
	public ExportToExcel(int[] aReportTypeTags, Object[] aReportTypes,
			ExportBatchProp props, boolean isExportByReport) {
		this(aReportTypeTags, aReportTypes, props, true, false, null);
		// this.iReportTypes = aReportTypeTags;
		// this.oReportTypes = aReportTypes;
		// this.exprops = props;
		// this.isExportByReport = isExportByReport;
		// this.EXPORTBYTYPE = 10;
	}

	/**
	 * 多表导出
	 * 
	 * @param iReportTypes
	 *            报表类型标识符 从ReportType类里取
	 * @param oReportTypes
	 *            报表类型: 现在支持的有：REDsLst,RELikeFace,RESql
	 */
	public ExportToExcel(int[] aReportTypeTags, Object[] aReportTypes,
			ExportBatchProp props, boolean isExportByReport,
			boolean isNotExportWhenEmpty, List lstMidTabNames) {
		this.iReportTypes = aReportTypeTags;
		this.oReportTypes = aReportTypes;
		this.exprops = props;
		this.isExportByReport = isExportByReport;
		this.isNotExportWhenEmpty = isNotExportWhenEmpty;
		this.lstMidTabNames = lstMidTabNames;
		this.EXPORTBYTYPE = 10;
	}

	private boolean doDelFile() throws IOException {
		// InputStream is = new FileInputStream(sFilePath);// 写入到FileInputStream
		return (new File(sFilePath)).delete();
	}

	/**
	 * 初始化类似封面导出的类型
	 * 
	 * @param ref
	 * @param i
	 * @throws Exception
	 */
	private void initExportLikeFace(RELikeFace ref, int i) throws Exception {
		this.reportID = ref.getReportID();
		this.dsLikeFace = ref.getDsData();
	}

	/**
	 * 根据路径获取文件名
	 * 
	 * @param addr
	 * @return
	 */
	private String getTitleByAddr(String addr) {
		if (this.isExportByReport) {
			return title;
		}
		String tts = eact.getTitleWithDataSet(reportID);
		if (Common.isNullStr(tts)) {
			if (!Common.isNullStr(exprops.getDefaultAddres())) {
				int ix = addr.lastIndexOf("\\");
				int id = addr.lastIndexOf(".");
				if (ix != (addr.length() - 1)) {
					if (id > 0) {
						tts = addr.substring(ix + 1, id);
					} else {
						tts = addr.substring(ix + 1, addr.length());
					}
				} else {
					return "";
				}
			}
			if (Common.isNullStr(tts)) {
				tts = "";
			}
		}
		return tts;
	}

	/**
	 * 带sql语句导出初始化
	 * 
	 * @param res
	 */
	private void initExportSql(RESql res, int i) throws Exception {
		this.bListBySql = true;
		this.sql_Body = res.getSqlBody();
		this.sql_Title = res.getSqlTitle();
		this.node = res.getNode();
		this.col_EName = res.getFieldKeyName();
		this.reportID = res.getReportID();
		lstConfig = eact.getConfigDataset(reportID);
		if (title_Child == null) {
			title_Child = new String[2];
		}
		title_Child = (String[]) exprops.getTitle_Childs().get(i);
		title = exprops.getTitles()[i];
		if (Common.isNullStr(title)) {
			if (this.isExportByReport && (exprops.getTitles() != null)) {
				title = exprops.getTitles()[i];
			}
			if (Common.isNullStr(title)) {
				title = String.valueOf(i);
			}
		}
	}

	/**
	 * 判断中间表是否为空记录，如果为空则返回true,不为空为false
	 * 
	 * @return
	 */
	private boolean getIsEmpTable(List list) throws Exception {
		if ((list == null) || (list.size() == 0)) {
			return true;
		}
		for (int i = 0; i < list.size(); i++) {
			if (itserv.getRecNum(Common.nonNullStr(list.get(i)), "") != 0) {
				// 如果获取记录条数不为0，则有数据
				return false;
			}
		}
		return true;
	}

	/**
	 * 多表导出中导出sql主体函数
	 * 
	 * @param resql
	 * @param i
	 * @param isEnableExportFace
	 * @param wwb
	 * @return
	 * @throws Exception
	 */
	private boolean doExportSql(RESql resql, int i, boolean isEnableExportFace,
			WritableWorkbook wwb) throws Exception {
		if (!exportHeader()) {
			return false;
		}

		if (!Common.isNullStr(sql_Title)) {
			list_Title = itserv.executeQuery(sql_Title);
		} else {
			this.sErrorInfo = "第" + i + "张报表为sql语句导出方式，但表头sql为空";
			return false;
		}
		List sqlb = null;
		List sqla = null;
		if (exprops.getSqls_BeforeGetBody() != null) {
			sqlb = (List) exprops.getSqls_BeforeGetBody().get(i);
		}
		if (exprops.getSqls_AfterGetBody() != null) {
			sqla = (List) exprops.getSqls_AfterGetBody().get(i);
		}
		if (!Common.isNullStr(sql_Body)) {
			list = itserv.executeQueryWithInfo(sql_Body, sqlb, null);
		} else {
			this.sErrorInfo = "第" + i + "张报表为sql语句导出方式，但表体sql为空";
			return false;
		}

		if (this.isNotExportWhenEmpty && (this.lstMidTabNames != null)
				&& (this.lstMidTabNames.size() != 0)
				&& getIsEmpTable((List) this.lstMidTabNames.get(i))) {
			// 判断数据为空时不导出
			itserv.executeQueryWithInfo("", null, sqla);
			return true;
		}
		this.isHaveTable = true;
		// 执行sqlAfter
		itserv.executeQueryWithInfo("", null, sqla);
		setBatchColsSql(i);
		if (list_Title.size() == 0) {
			this.sErrorInfo = "第" + i + "张报表表头为空";
			return false;
		}
		// 获取表头的区域
		Object[][] headerCell = getTableHeaderCells(rowsum, colsum);
		// 如果导出了封面则从封面后导出，否则直接从第一个导出
		int sheet = i;
		if (isEnableExportFace) {
			sheet = i + exprops.getFaceData().length;
		} else {
			sheet = i;
		}
		sheet = sheet - iEmptyTableCounts;
		WritableSheet ws = null;
		if (this.isExportByReport) {
			ws = wwb.createSheet(title, sheet);
		} else {
			ws = wwb.createSheet(exprops.getTitles()[i], sheet);
		}

		// 添加标题
		addTableTitle(ws);

		// 添加表头
		addTableHeader(ws, headerCell);

		// 导出表体
		InfoPackage info = new InfoPackage();
		if (list != null) {
			info = exportBody(ws, headerCell, list, rowsum + 1);
		}
		if (!info.getSuccess()) {
			this.sErrorInfo = "第" + i + "张报表，表体导出失败";
			return false;
		}

		// 合并表头
		ws.mergeCells(0, 0, colsum - 1, 0);

		setRowHeight(ws);
		ws.setRowView(0, 600);
		// 合并单元格
		eact.mergeCells(isNeedTitle, ws, headerCell, rowsum, colsum);
		// ws.mergeCells(0,3,0,6);
		// 设置列宽
		setWidth(ws, cols_width);
		this.ps = ws.getSettings();
		this.setPaperPS(eact.getConfigDataset(), reportID);
		if (this.cols_Format != null) {
			cols_Format = null;
		}
		if (this.cols_Type != null) {
			cols_Type = null;
		}
		if (this.cols_width != null) {
			cols_width = null;
		}
		if (this.col_EName != null) {
			cols_EName = null;
		}
		if (this.cols_CName != null) {
			cols_CName = null;
		}
		return true;
	}

	/**
	 * 初始化表体为list的导出类型
	 * 
	 * @param red
	 * @param i
	 * @throws Exception
	 */
	private void initExportList(REDsLst red, int i) throws Exception {
		this.bListBySql = false;
		this.reportID = red.getReportID();
		this.node = red.getNode();
		this.col_EName = red.getFieldKeyName();
		// 这两项从库里取
		// title = exprops.getTitles()[i];
		title = eact.getTitleWithDataSet(reportID);
		if (Common.isNullStr(title)) {
			if (this.isExportByReport && (exprops.getTitles() != null)) {
				title = exprops.getTitles()[i];
			}
			if (Common.isNullStr(title)) {
				title = String.valueOf(i);
			}
		}
		if ((exprops.getTitle_Childs() != null)
				&& (exprops.getTitle_Childs().size() != 0)
				&& (exprops.getTitle_Childs().size() > i)
				&& (exprops.getTitle_Childs().get(i) != null)) {
			this.titleParam = (String[]) exprops.getTitle_Childs().get(i);
		} else {
			titleParam = new String[2];
		}
		this.title_Child = eact.getTitle_ChildWithDataSet(lstConfig, reportID);
		if (title_Child == null) {
			title_Child = new String[2];
		}
		this.dsBody = red.getListBody();
		this.dsHeader = red.getDsHeader();
		setConfigByDataSet(eact.getConfigDataset());
	}

	/**
	 * 表体为list的导出的主体函数
	 * 
	 * @param red
	 * @param i
	 * @param isEnableExportFace
	 * @param wwb
	 * @throws Exception
	 */
	private boolean doExportList(REDsLst red, int i,
			boolean isEnableExportFace, WritableWorkbook wwb) throws Exception {
		this.isHaveTable = true;
		this.bListBySql = false;
		if (!exportHeader()) {
			return false;
		}
		if ((dsHeader == null) || dsHeader.isEmpty()) {
			this.sErrorInfo = "第" + i + "张报表的表头信息为空";
			return false;
		}
		list = dsBody;
		// 获取表头的区域
		Object[][] headerCell = getTableHeaderCells(rowsum, colsum);
		// 如果导出了封面则从封面后导出，否则直接从第一个导出
		int sheet = i;
		if (isEnableExportFace) {
			sheet = i + exprops.getFaceData().length;
		} else {
			sheet = i;
		}
		sheet = sheet - iEmptyTableCounts;
		WritableSheet ws = null;
		if (this.isExportByReport) {
			ws = wwb.createSheet(title, sheet);
		} else {
			ws = wwb.createSheet(exprops.getTitles()[i], sheet);
		}
		// addTableTitleByDataSet(ws, title, null);
		addTableTitle(ws);

		// 添加表头
		// addTableHeaderByDataset(ws, headerCell);
		addTableHeader(ws, headerCell);

		// 导出表体
		if (list != null) {
			setBatchColsList(i);
			exportBody(ws, headerCell, list, rowsum + 1);
		}

		// 合并表头
		ws.mergeCells(0, 0, colsum - 1, 0);
		// ws.mergeCells(0, 1, colsum - 1, 1);
		if ((eact.getConfigDataset() != null)
				&& (eact.getConfigDataset().getRecordCount() > 0)
				&& !eact.getConfigDataset().eof()
				&& !eact.getConfigDataset().bof()) {
			ws.setRowView(0, eact.getConfigDataset().fieldByName(
					"REPORT_TITLE_HEIGHT").getInteger());
			this.ps = ws.getSettings();
			this.setPaperPS(eact.getConfigDataset(), reportID);
		} else {
			ws.setRowView(0, 72);
		}
		// 设置列宽
		setWidth(ws, cols_width);
		if (this.cols_Format != null) {
			cols_Format = null;
		}
		if (this.cols_Type != null) {
			cols_Type = null;
		}
		if (this.cols_width != null) {
			cols_width = null;
		}
		if (this.col_EName != null) {
			cols_EName = null;
		}
		if (this.cols_CName != null) {
			cols_CName = null;
		}
		return true;
	}

	private boolean doExportFace(RELikeFace ref, int i,
			boolean isEnableExportFace, WritableWorkbook wwb) throws Exception {
		this.isHaveTable = true;
		this.reportID = ref.getReportID();
		// 这个地方要判断列宽
		String sql = "SELECT MAX(FIELD_COLUMN+FIELD_COLUMNSPAN) maxcolumn FROM fb_u_qr_szzb where report_id = '"
				+ reportID + "'";
		DataSet dsMaxColumn = ExportStub.getMethod().getDataset(sql);
		if ((dsMaxColumn != null) && !dsMaxColumn.isEmpty()) {
			dsMaxColumn.beforeFirst();
			dsMaxColumn.next();
			this.colsum = dsMaxColumn.fieldByName("maxcolumn").getInteger() - 1;
		} else {
			this.colsum = 2;
		}
		if ((this.cols_Format != null) && (exprops.getFormats() != null)) {
			this.cols_Format = (String[]) exprops.getFormats().get(i);
		}
		if ((this.cols_Type != null) && (exprops.getTypes() != null)) {
			this.cols_Type = (String[]) exprops.getTypes().get(i);
		}
		DataSet dsConfig = eact.getConfigDataset();
		if (dsConfig.locate("REPORT_ID", reportID)) {
			title = eact.getTitleWithDataSet(reportID);
		} else {
			if (this.isExportByReport) {
				title = exprops.getTitles()[i];
			}
		}
		if ((exprops.getTitle_Childs() != null)
				&& (exprops.getTitle_Childs().size() != 0)) {
			this.titleParam = (String[]) exprops.getTitle_Childs().get(i);
		}
		this.title_Child = eact.getTitle_ChildWithDataSet(lstConfig, reportID);
		setConfigByDataSet(dsConfig);
		if (title_Child == null) {
			title_Child = new String[2];
		}
		int sheet = 0;
		if (isEnableExportFace) {
			sheet = i + exprops.getFaceData().length;
		} else {
			sheet = i;
		}
		sheet = sheet - iEmptyTableCounts;
		WritableSheet wsface = null;
		if (this.isExportByReport) {
			wsface = wwb.createSheet(title, sheet);
		} else {
			wsface = wwb.createSheet(exprops.getTitles()[i], sheet);
		}
		// 如果导出了封面则从封面后导出，否则直接从第一个导出
		// 导出封面
		exportLikeFace(wsface, 0);
		addTableTitle(wsface);
		return true;
	}

	/**
	 * 检查输入的参数是否正确 如果传入的标识符与报表类型不符，则退出
	 * 
	 * @return 由方法getErrorInfo获取错误信息
	 */
	private boolean checkInput() {
		for (int i = 0; i < iReportTypes.length; i++) {
			switch (iReportTypes[i]) {
			case ReportType.Sql:
				RESql res = (RESql) oReportTypes[i];
				if (res.getReportType() != ReportType.Sql) {
					sErrorInfo = "第" + i + "个报表期望是SQL语句类型，但不是！";
					return false;
				}
				if (res.getNode() == null) {
					sErrorInfo = "第" + i + "个报表传入的表头node为空";
					return false;
				}
				if (Common.isNullStr(res.getSqlBody())
						|| Common.isNullStr(res.getSqlTitle())) {
					sErrorInfo = "第" + i + "个报表期望是SQL语句类型，但传入的表头或表体数据为空！";
					return false;
				}
				break;
			case ReportType.DsLst:
				if (((REDsLst) oReportTypes[i]).getReportType() != ReportType.DsLst) {
					sErrorInfo = "第" + i + "个报表期望是行表类型，但不是！";
					return false;
				}
				break;
			case ReportType.LikeFace:
				if (((RELikeFace) oReportTypes[i]).getReportType() != ReportType.LikeFace) {
					sErrorInfo = "第" + i + "个报表期望是封面类型，但不是！";
					return false;
				}
				break;
			default:
				break;
			}
		}
		return true;
	}

	/**
	 * 获取错误信息
	 * 
	 * @return
	 */
	public String getErrorInfo() {
		return this.sErrorInfo;
	}

	/**
	 * 获取是否执行了取消操作
	 * 
	 * @return
	 */
	public boolean getIsCancel() {
		return this.isCancel;
	}

	/**
	 * 设置封面标题
	 * 
	 * @param faceTitles
	 */
	public void setFaceTitles(String[] faceTitles) {
		this.faceTitles = faceTitles;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// 打印配置
	private void setPaperPS(DataSet dsConfig, String report_id)
			throws Exception {
		if ((dsConfig == null) || dsConfig.isEmpty()
				|| !dsConfig.locate("REPORT_ID", report_id) || dsConfig.bof()
				|| dsConfig.eof()) {
			return;
		}
		if (ps == null) {
			return;
		}
		String paperSize = dsConfig.fieldByName("PAPER_SIZE").getString();
		String paperOrient = dsConfig.fieldByName("PRINT_ORIENT").getString();
		String paperScale = dsConfig.fieldByName("PRINT_SCALE").getString();
		int verLines = dsConfig.fieldByName("TOP_TITLE_ENDROW").getInteger()
				- dsConfig.fieldByName("TOP_TITLE_STARTROW").getInteger() + 1;
		int horLines = dsConfig.fieldByName("LEFT_TITLE_ENDCOL").getInteger()
				- dsConfig.fieldByName("LEFT_TITLE_STARTCOL").getInteger() + 1;

		if (!Common.isNullStr(paperSize)) {
			// 纸张大小
			if ("A3".equals(paperSize)) {
				ps.setPaperSize(PaperSize.A3);
			}
			if ("A4".equals(paperSize)) {
				ps.setPaperSize(PaperSize.A4);
			}
		} else {
			// 默认为A4
			ps.setPaperSize(PaperSize.A4);
		}
		if (!Common.isNullStr(paperScale)) {
			// 比例
			ps.setScaleFactor(Integer.parseInt(paperScale));
		} else {
			ps.setScaleFactor(100);
		}
		if (!Common.isNullStr(paperOrient)) {
			// 纸张方向
			if ("0".equals(paperOrient)) {
				ps.setOrientation(PageOrientation.LANDSCAPE);
			} else {
				ps.setOrientation(PageOrientation.PORTRAIT);
			}
		} else {
			ps.setScaleFactor(100);
		}
		// 固定列，固定行
		if (verLines != 0) {
			ps.setVerticalFreeze(verLines);
			ps.setVerticalPrintResolution(verLines);
		}
		if (horLines != 0) {
			ps.setHorizontalFreeze(horLines);
			ps.setHorizontalPrintResolution(horLines);
		}
		// 设置左右边距
		ps.setLeftMargin(dsConfig.fieldByName("MARGIN_LEFT").getDouble() / 10);
		ps
				.setRightMargin(dsConfig.fieldByName("MARGIN_RIGHT")
						.getDouble() / 100);
		ps.setTopMargin(dsConfig.fieldByName("MARGIN_TOP").getDouble() / 10);
		ps
				.setBottomMargin(dsConfig.fieldByName("MARGIN_BOTTOM")
						.getDouble() / 100);
		// 左右居中
		boolean isv = dsConfig.fieldByName("N1").getBoolean();
		if (isv) {
			ps.setVerticalCentre(true);
		} else {
			ps.setHorizontalCentre(true);
		}
	}

	/**
	 * **********************************根据模板的导出********************************
	 * ************
	 */

	/** 模板的保存地址 */
	private String efile = "C:\\Documents and Settings\\"
			+ System.getProperty("user.name") + "\\exportfiles\\";

	private List lstConfig = null;

	/**
	 * 根据模块板的多表导出
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean doBatchExportWithModel() throws Exception {
		if (!checkInput()) {
			return false;
		}
		if (Common.isNullStr(exprops.getDefaultAddres())) {
			sFilePath = getFilePath();
		} else {
			sFilePath = exprops.getDefaultAddres();
		}

		if (Common.isNullStr(sFilePath)) {
			this.sErrorInfo = "获取保存地址失败";
			return false;
		}
		if ((sFilePath.toUpperCase()).lastIndexOf(".xls".toUpperCase()) < 0) {
			sFilePath = sFilePath + ".XLS";
		}
		int iBatch = iReportTypes.length;
		WritableWorkbook wwb = null;
		wwb = Workbook.createWorkbook(new File(sFilePath));
		boolean isEnableExportFace = exportModelFace(wwb);

		boolean isUseModel = false;
		for (int i = 0; i < iBatch; i++) {
			switch (iReportTypes[i]) {
			case ReportType.Sql:
				// 导出sql类型
				this.isCurFace = false;
				initExportSql((RESql) oReportTypes[i], i);
				lstConfig = eact.getConfigDataset(reportID);
				if ((lstConfig != null) && (lstConfig.size() != 0)) {
					isUseModel = "1".equals(((Map) lstConfig.get(0))
							.get("IS_USEMODEL".toLowerCase()));
				}
				if (isUseModel) {
					doExportSqlWithModel((RESql) oReportTypes[i], i,
							isEnableExportFace, wwb);
				} else {
					doExportSql((RESql) oReportTypes[i], i, isEnableExportFace,
							wwb);
				}
				break;
			case ReportType.DsLst:
				// 导出表体list类型
				this.isCurFace = false;
				initExportList((REDsLst) oReportTypes[i], i);
				lstConfig = eact.getConfigDataset(reportID);
				if ((lstConfig != null) && (lstConfig.size() != 0)) {
					isUseModel = "1".equals(((Map) lstConfig.get(0))
							.get("IS_USEMODEL".toLowerCase()));
				}
				if (isUseModel) {
					doExportListWithModel((REDsLst) oReportTypes[i], i,
							isEnableExportFace, wwb);
				} else {
					doExportList((REDsLst) oReportTypes[i], i,
							isEnableExportFace, wwb);
				}
				break;
			case ReportType.LikeFace:
				// 导出类似封面类型
				this.isCurFace = true;
				initExportLikeFace((RELikeFace) oReportTypes[i], i);
				lstConfig = eact.getConfigDataset(reportID);
				if ((lstConfig != null) && (lstConfig.size() != 0)) {
					isUseModel = "1".equals(((Map) lstConfig.get(0))
							.get("IS_USEMODEL".toLowerCase()));
				}
				if (isUseModel) {
					doExportFaceWithModel((RELikeFace) oReportTypes[i], i,
							isEnableExportFace, wwb);
				} else {
					doExportFace((RELikeFace) oReportTypes[i], i,
							isEnableExportFace, wwb);
				}
				break;
			default:
				break;
			}
		}
		if (this.isHaveTable) {
			// 写入excel
			wwb.write();
		}
		// 关闭文件
		wwb.close();
		itserv.closeSession();
		if (!this.isHaveTable && !doDelFile()) {
			return false;
		}
		return true;
	}

	private boolean exportModelFace(WritableWorkbook wwb) throws Exception {
		WritableSheet wsface = null;
		WritableSheet[] wsBefore = null;
		if ((exprops.getFaceData() != null) && (exprops.getFaceData().length != 0)) {
			wsBefore = new WritableSheet[exprops.getFaceData().length];
		} else {
			wsBefore = new WritableSheet[2];
		}
		if ((exprops.getFaceData() != null) && (exprops.getFaceData().length == 2)) {
			wsBefore = new WritableSheet[exprops.getFaceData().length];
		}
		int iface = 0;
		String faceTitle = "";
		if ((exprops.getFaceData() != null) && (exprops.getFaceData().length != 0)) {
			for (iface = 0; iface < exprops.getFaceData().length; iface++) {
				if ((faceTitles != null) && (faceTitles.length != 0)
						&& (faceTitles.length <= exprops.getFaceData().length)) {
					faceTitle = this.faceTitles[iface];
				}
				((DataSet) exprops.getFaceData()[iface]).beforeFirst();
				((DataSet) exprops.getFaceData()[iface]).next();
				reportID = ((DataSet) exprops.getFaceData()[iface])
						.fieldByName("REPORT_ID").getString();
				String divname = null;
				if (((DataSet) exprops.getFaceData()[iface]).locate(
						"FIELD_PARA", "DIV_NAME")) {
					divname = ((DataSet) exprops.getFaceData()[iface])
							.fieldByName("FIELD_VALUE").getString();
				}
				faceTitle = (Common.isNullStr(faceTitle)) ? "封面" + iface
						: faceTitle;
				this.isHaveTable = true;

				Sheet wsi = null;
				WritableSheet ws = null;
				// 取文件的时候要注意的是，先从本机找，如果本机找不到，则从库里找，找到后再保存到本机特定地址
				String reportname = null;
				String modifytime = null;
				lstConfig = eact.getConfigDataset(reportID);
				if ((lstConfig != null) && !lstConfig.isEmpty()) {
					// 如果数据集不为空
					reportname = Common.nonNullStr(((XMLData) eact
							.getConfigDataset(reportID).get(0))
							.get("report_title"));
					modifytime = Common.nonNullStr(((XMLData) eact
							.getConfigDataset(reportID).get(0))
							.get("modifytime"));
				}
				if (getModelFile(reportID, reportname, modifytime)) {
					InputStream is = new FileInputStream(new File(efile
							+ reportname + ".XLS"));
					Workbook rwb = Workbook.getWorkbook(is);
					wsi = rwb.getSheet(0);
//					wwb.importSheet(faceTitle, iface, wsi);
					is.close();
					ws = wwb.getSheet(iface);
					// setDataCellFormats(ws);

					int col = ws.getColumns();
					int row = ws.getRows();
					if (row <= 1) {
						row = 3;
					}
					for (int i = 0; i < col; i++) {
						for (int j = 1; j < row; j++) {
							String contentn = ws.getCell(i, j).getContents();
							if (Common.isNullStr(contentn)) {
								continue;
							}
							contentn = contentn.replaceAll("\\[", "begin");
							contentn = contentn.replaceAll("\\]", "end");
							contentn = contentn.replaceAll("null", "");
							contentn = contentn.replaceAll(
									"null".toUpperCase(), "");
							if (!(contentn.indexOf(this.tag_DivName) >= 0)) {
								continue;
							}
							contentn = contentn.replaceAll(this.tag_DivName,
									divname);
							Label label = new Label(i, j, contentn);
							if (ws.getWritableCell(i, j).getCellFormat() != null) {
								label.setCellFormat(ws.getCell(i, j)
										.getCellFormat());
							}
							if (ws.getWritableCell(i, j).getCellFeatures() != null) {
								label.setCellFeatures(ws.getWritableCell(i, j)
										.getWritableCellFeatures());
							}
							try {
								ws.addCell(label);
							} catch (Exception ee) {
								ee.printStackTrace();
							}
						}
					}
				} else {
					wsface = wwb.createSheet(faceTitle, iface);
					wsBefore[iface] = wsface;
				}
			}
		}
		return exportfaceSG(wsBefore, iface);
	}

	/**
	 * 多表导出中导出sql主体函数
	 * 
	 * @param resql
	 * @param i
	 * @param isEnableExportFace
	 * @param wwb
	 * @return
	 * @throws Exception
	 */
	private boolean doExportSqlWithModel(RESql resql, int i,
			boolean isEnableExportFace, WritableWorkbook wwb) throws Exception {
		if (!exportHeader()) {
			return false;
		}
		if (!Common.isNullStr(sql_Title)) {
			list_Title = itserv.executeQuery(sql_Title);
		} else {
			this.sErrorInfo = "第" + i + "张报表为sql语句导出方式，但表头sql为空";
			return false;
		}
		List sqlb = null;
		List sqla = null;
		if (exprops.getSqls_BeforeGetBody() != null) {
			sqlb = (List) exprops.getSqls_BeforeGetBody().get(i);
		}
		if (exprops.getSqls_AfterGetBody() != null) {
			sqla = (List) exprops.getSqls_AfterGetBody().get(i);
		}
		if (!Common.isNullStr(sql_Body)) {
			list = itserv.executeQueryWithInfo(sql_Body, sqlb, null);
		} else {
			this.sErrorInfo = "第" + i + "张报表为sql语句导出方式，但表体sql为空";
			return false;
		}

		if (this.isNotExportWhenEmpty && (this.lstMidTabNames != null)
				&& (this.lstMidTabNames.size() != 0)
				&& getIsEmpTable((List) this.lstMidTabNames.get(i))) {
			// 判断数据为空时不导出
			iEmptyTableCounts++;
			itserv.executeQueryWithInfo("", null, sqla);
			return true;
		}
		this.isHaveTable = true;
		// 执行sqlAfter
		itserv.executeQueryWithInfo("", null, sqla);
		setBatchColsSql(i);
		if (list_Title.size() == 0) {
			this.sErrorInfo = "第" + i + "张报表表头为空";
			return false;
		}
		// // 获取表头的区域
		Object[][] headerCell = getTableHeaderCells(rowsum, colsum);
		// 如果导出了封面则从封面后导出，否则直接从第一个导出
		int sheet = i;
		if (isEnableExportFace) {
			sheet = i + exprops.getFaceData().length;
		} else {
			sheet = i;
		}
		Sheet wsi = null;
		WritableSheet ws = null;
		// 取文件的时候要注意的是，先从本机找，如果本机找不到，则从库里找，找到后再保存到本机特定地址
		String reportname = null;
		String modifytime = null;
		if ((lstConfig != null) && !lstConfig.isEmpty()) {
			// // 如果数据集不为空
			reportname = Common.nonNullStr(((XMLData) eact.getConfigDataset(
					reportID).get(0)).get("report_title"));
			modifytime = Common.nonNullStr(((XMLData) eact.getConfigDataset(
					reportID).get(0)).get("modifytime"));
		}
		String titleNow = null;
		if (isExportByReport) {
			titleNow = reportname;
		} else {
			titleNow = exprops.getTitles()[i];
		}
		sheet = sheet - iEmptyTableCounts;
		if (getModelFile(reportID, reportname, modifytime)) {
			InputStream is = new FileInputStream(new File(efile + reportname
					+ ".XLS"));
			Workbook rwb = Workbook.getWorkbook(is);
			wsi = rwb.getSheet(0);
//			ws = wwb.importSheet(titleNow, sheet, wsi);
			is.close();
			// ws = wwb.getSheet(sheet);
			setDataCellFormats(ws);
		} else {
			// this.sErrorInfo = "没有配置该表的格式信息,初始化失败";
			// return false;
			doExportSql((RESql) oReportTypes[i], i, isEnableExportFace, wwb);
			return true;
		}
		// 添加标题
		addTableTitleWithModel(ws, i);
		// 添加表头
		// addTableHeader(ws, headerCell);
		// 导出表体
		InfoPackage info = new InfoPackage();
		int headerrow = iHeaderRow;
		if (headerrow == -1) {
			headerrow = rowsum + 3;
		}
		if (list != null) {
			info = exportBodyWithModel(ws, headerCell, list, headerrow);
		}
		setModelExcelHeight(ws);
		if (!info.getSuccess()) {
			this.sErrorInfo = "第" + i + "张报表，表体导出失败";
			return false;
		}
		return true;
	}

	/**
	 * 添加标题
	 * 
	 * @param ws
	 * @param title
	 * @throws WriteException
	 */
	private boolean addTableTitleWithModel(WritableSheet ws, int isheet)
			throws Exception {
		addTableChildTitileWithModel(ws, isheet);
		if (title_Child != null) {
			rowsum = rowsum + title_Child.length;
		}
		return true;
	}

	/**
	 * 添加子标题
	 * 
	 * @param ws
	 * @param title_Child
	 * @throws Exception
	 */
	private void addTableChildTitileWithModel(WritableSheet ws, int isheet)
			throws Exception {
		if (reportIDs != null) {
			title_Child = this.array_ChildTitle;
		}
		if ((title_Child == null) && (this.titleParam == null)) {
			return;
		}
		DataSet dstt = ExportStub.getMethod().getDataset(
				"SELECT NAME FROM FB_S_PUBCODE WHERE TYPEID LIKE 'PRINTTITLE'");
		title_Child = new String[2];
		if ((dstt != null) && !dstt.isEmpty()) {
			dstt.beforeFirst();
			if (dstt.next()) {
				title_Child[0] = dstt.fieldByName("name").getString();
			}
			if (dstt.next()) {
				title_Child[1] = dstt.fieldByName("name").getString();
			}
		}
		if ((titleParam != null) && (titleParam.length == 2)) {
			titleParam[0] = (Common.isNullStr(titleParam[0])) ? ""
					: titleParam[0];
			titleParam[1] = (Common.isNullStr(titleParam[1])) ? ""
					: titleParam[1];
		}
		if ((titleParam == null) && (exprops != null)
				&& (exprops.getTitle_Childs() != null)) {
			titleParam = (String[]) exprops.getTitle_Childs().get(isheet);
		}
		if ((titleParam == null) && (exprop != null)
				&& (exprop.getTitle_Child() != null)) {
			titleParam = (String[]) exprop.getTitle_Child();
		}

		// 查找单位名称，数量单位 并替换
		int col = ws.getColumns();
		int row = ws.getRows();
		if (row <= 1) {
			row = 3;
		}
		for (int i = 0; (i < col) && (titleParam != null); i++) {
			for (int j = 1; j < row; j++) {
				String contentn = ws.getCell(i, j).getContents();
				if (Common.isNullStr(contentn)) {
					continue;
				}
				contentn = contentn.replaceAll("\\[", "begin");
				contentn = contentn.replaceAll("\\]", "end");
				contentn = contentn.replaceAll("null", "");
				contentn = contentn.replaceAll("null".toUpperCase(), "");
				if (!(contentn.indexOf(this.tag_DivName) >= 0)
						&& !(contentn.indexOf(this.tag_Unit) >= 0)) {
					continue;
				}
				contentn = contentn.replaceAll(this.tag_DivName, titleParam[0]);
				contentn = contentn.replaceAll(this.tag_Unit, titleParam[1]);

				Label label = new Label(i, j, contentn);
				if (ws.getWritableCell(i, j).getCellFormat() != null) {
					label.setCellFormat(ws.getCell(i, j).getCellFormat());
				}
				if (ws.getWritableCell(i, j).getCellFeatures() != null) {
					label.setCellFeatures(ws.getWritableCell(i, j)
							.getWritableCellFeatures());
				}
				try {
					ws.addCell(label);
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取数据所在的行数
	 * 
	 * @param ws
	 * @return
	 */
	private int getDataRow(WritableSheet ws) {
		int col = ws.getColumns();
		for (int i = 0; i < col; i++) {
			for (int j = 1; j < 20; j++) {
				// if (count == 2)
				// break;
				String contentn = ws.getCell(i, j).getContents();
				if (Common.isNullStr(contentn)) {
					continue;
				}
				contentn = contentn.replaceAll("\\[", "begin");
				contentn = contentn.replaceAll("\\]", "end");
				if (this.tag_Data.equals(contentn)) {
					return j;
				}
			}
		}
		return -1;
	}

	/**
	 * 设置行高
	 * 
	 * @param ws
	 */
	private void setModelExcelHeight(WritableSheet ws) throws Exception {
		int dr = getDataRow(ws);
		if ((dr == -1) && (iHeaderRow > 0)) {
			dr = this.iHeaderRow;
		} else {
			return;
		}
		int height = ws.getRowView(dr).getSize();
		for (int i = iHeaderRow; i < ws.getRows(); i++) {
			ws.setRowView(i, height);
		}
	}

	/**
	 * 把库里文件保存到当前路径下
	 * 
	 */
	public boolean saveFileFromBase(String report_id, File f) {
		try {
			// 创建原文件输出流
			FileOutputStream fileOut = new FileOutputStream(f);
			BufferedOutputStream bufOut = new BufferedOutputStream(fileOut);
			// 分批读取blob，转化为字节,放入输出流
			int count = 1;
			while (true) {
				byte[] buf = ExportStub.getMethod().getDocBlob(count++,
						"fb_s_printinfo", "EXCEL",
						" where REPORT_ID='" + report_id + "' and is_usemodel = 1");
				if (buf == null) {
					JOptionPane.showMessageDialog(Global.mainFrame, "读取文件失败！",
							"消息", JOptionPane.INFORMATION_MESSAGE);
					break;
				} else {
					bufOut.write(buf);
					if (buf.length < 1024 * 350) {
						break;
					}
				}
			}

			/* 保存 */
			bufOut.flush();
			/* 关闭流 */
			bufOut.close();
			fileOut.close();
			/* 释放流资源 */
			bufOut = null;
			fileOut = null;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取report
	 * 
	 * @return
	 */
	private boolean getModelFile(String reportid, String reportname,
			String modifytime) {
		try {
			Properties pp = new Properties();
			// 获取WORKSHEET
			File f = new File(this.efile + reportname + ".XLS");
			String inipath = this.efile + "EXPORTSET" + ".properties";
			File fini = new File(inipath);
			File fdir = new File(efile);
			if (!fdir.isDirectory()) {
				// 如果不存在，则从库里创建
				fdir.mkdirs(); // 创建文件夹
				OutputStream osh = new FileOutputStream(fini);
				osh.close();
				saveFileFromBase(reportid, f); // 创建模板文件
			} else {
				// 如果存在
				if (f.exists()) {
					// 如果导出模板存在
					if (fini.exists()) {
						// 如果配置文件存在
						// 如果本地时间和数据库修改时间不一致，则重新下载
						InputStream fis = new FileInputStream(fini);
						pp.load(fis);
						String time = pp.getProperty(reportid);
						if (!Common.isEqual(time, modifytime)
								|| Common.isNullStr(time)) {
							// 导出新的模板
							if (!saveFileFromBase(reportid, f)) {
								return false;
							} else {
								// 如果时间不一样，则写入该模板信息
								// 写入该模板信息
								OutputStream fos = new FileOutputStream(fini);
								pp.setProperty(reportid, modifytime);
								pp.store(fos, "modify parameters");
								fos.close();
								fis.close();
							}
						}
					} else {
						// 如果配置文件不存在
						InputStream fis = new FileInputStream(fini);
						pp.load(fis);
						// 写入该模板信息
						OutputStream fos = new FileOutputStream(fini);
						pp.setProperty(reportid, modifytime);
						pp.store(fos, "modify parameters");
						fos.close();
						fis.close();
					}
				} else {
					// 如果导出模板不存在
					// 写配置文件
					if (!fini.exists()) {
						// 如果配置文件不存在在创建
						OutputStream osh = new FileOutputStream(fini);
						osh.close();
					}
					InputStream fis = new FileInputStream(fini);
					pp.load(fis);
					// 写入该模板信息
					OutputStream fos = new FileOutputStream(fini);
					pp.setProperty(reportid, modifytime);
					pp.store(fos, "modify parameters");
					fos.close();
					fis.close();
					// 导出新的模板
					if (!saveFileFromBase(reportid, f)) {
						return false;
					}
				}
			}
		} catch (Exception ee) {
			return false;
		}
		return true;
	}

	/**
	 * 导出表体
	 * 
	 * @throws Exception
	 */
	private InfoPackage exportBodyWithModel(WritableSheet ws,
			Object[][] headerCell, List listDataRow, int iHeaderRowCount)
			throws Exception {
		InfoPackage info = new InfoPackage();
		info.setSuccess(true);
		jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat();
		wcfFC.setBorder(Border.ALL, BorderLineStyle.THIN);
		// 空数字格式化
		CellFormat wcffN[] = colFormats;
		String[] numbers = null;
		if (exprops == null) {
			numbers = exprop.getNumberViewInTable();
		} else {
			numbers = exprops.getNumberViewInTable();
		}
		Arrays.sort(numbers);
		for (int k = 0; k < list.size(); k++) {
			if (list.get(k) == null) {
				continue;
			}
			for (int j = 0; j < colsum; j++) {
				Map map = null;
				// 浮点数格式化
				if (list.get(k) instanceof XMLData) {
					map = (XMLData) (list.get(k));
				}
				if (list.get(k) instanceof Map) {
					map = (Map) (list.get(k));
				}
				Object obj = null;
				if (this.bListBySql) {
					obj = map.get(cols_EName[j].toLowerCase());
				} else {
					obj = map.get(cols_EName[j]);
				}
				String coltype = cols_Type[j];
				if ((obj == null) || "NULL".equals(obj.toString().toUpperCase())) {
					// 如果库里没有设置数据为“0”时显示内容，则添加空
					Blank blank = new Blank(j, iHeaderRowCount);
					blank.setCellFormat(wcffN[j]);
					ws.addCell(blank);
					continue;
				}
				if (Common.isNullStr(coltype)) {
					coltype = "";
				}
				if (Arrays.binarySearch(numbers, coltype) >= 0) {
					String sValue = Common.nonNullStr(obj);
					if (Common.isNumber(Common.nonNullStr(obj))
							&& (Float.compare(0, Float.parseFloat(Common
									.nonNullStr(obj))) == 0)) {
						Blank blank = new Blank(j, iHeaderRowCount);
						blank.setCellFormat(wcffN[j]);
						ws.addCell(blank);
						continue;
					} else {
						int ib = sValue.indexOf(".");
						if (ib > 0) {
							int ll = sValue.substring(ib + 1, sValue.length())
									.length();
							if (ll > 2) {
								sValue = sValue.substring(0, ib + 3);
							}
						}
						sValue = Common.isNullStr(sValue) ? "0" : sValue;
						Number number = null;
						try {// 如果格式化失败
							number = new Number(j, iHeaderRowCount, Double
									.parseDouble(sValue));
							if (wcffN[j] != null) {
								number.setCellFormat(wcffN[j]);
							} else {
								number.setCellFormat(wcfFC);
							}
							ws.addCell(number);
						} catch (Exception e) {
							jxl.write.Label labelC = new jxl.write.Label(j,
									iHeaderRowCount, sValue);
							if (wcffN[j] != null) {
								labelC.setCellFormat(wcffN[j]);
							} else {
								labelC.setCellFormat(wcfFC);
							}
							ws.addCell(labelC);
						}// ---------------------------------
					}
				} else {
					jxl.write.Label labelC = new jxl.write.Label(j,
							iHeaderRowCount, "NULL".equals(obj.toString()
									.toUpperCase()) ? "" : obj.toString());
					if (wcffN[j] != null) {
						labelC.setCellFormat(wcffN[j]);
					} else {
						labelC.setCellFormat(wcfFC);
					}
					ws.addCell(labelC);
				}
			}
			iHeaderRowCount++;
		}
		return info;
	}

	/**
	 * 表体为list的导出的主体函数
	 * 
	 * @param red
	 * @param i
	 * @param isEnableExportFace
	 * @param wwb
	 * @throws Exception
	 */
	private boolean doExportListWithModel(REDsLst red, int i,
			boolean isEnableExportFace, WritableWorkbook wwb) throws Exception {
		this.isHaveTable = true;
		this.bListBySql = false;
		if (!exportHeader()) {
			return false;
		}
		list = dsBody;
		// // 获取表头的区域
		Object[][] headerCell = getTableHeaderCells(rowsum, colsum);
		int sheet = i;
		if (isEnableExportFace) {
			sheet = i + exprops.getFaceData().length;
		} else {
			sheet = i;
		}
		Sheet wsi = null;
		WritableSheet ws = null;
		// 取文件的时候要注意的是，先从本机找，如果本机找不到，则从库里找，找到后再保存到本机特定地址
		String reportname = null;
		String modifytime = null;
		lstConfig = eact.getConfigDataset(reportID);
		if ((lstConfig != null) && !lstConfig.isEmpty()) {
			// 如果数据集不为空
			reportname = Common.nonNullStr(((XMLData) eact.getConfigDataset(
					reportID).get(0)).get("report_title"));
			modifytime = Common.nonNullStr(((XMLData) eact.getConfigDataset(
					reportID).get(0)).get("modifytime"));
		}
		String titleNow = null;
		if (isExportByReport) {
			titleNow = reportname;
		} else {
			titleNow = exprops.getTitles()[i];
		}
		sheet = sheet - iEmptyTableCounts;
		if (getModelFile(reportID, reportname, modifytime)) {
			InputStream is = new FileInputStream(new File(efile + reportname
					+ ".XLS"));
			Workbook rwb = Workbook.getWorkbook(is);
			wsi = rwb.getSheet(0);
//			ws = wwb.importSheet(titleNow, sheet, wsi);
			is.close();
			// ws = wwb.getSheet(sheet);
			setDataCellFormats(ws);
		} else {
			doExportList((REDsLst) oReportTypes[i], i, isEnableExportFace, wwb);
			return true;
		}
		addTableTitleWithModel(ws, i);

		// //添加表头
		// addTableHeader(ws, headerCell);
		int headerrow = iHeaderRow;
		if (headerrow == -1) {
			headerrow = rowsum + 3;
		}
		// 导出表体
		if (list != null) {
			setBatchColsList(i);
			exportBodyWithModel(ws, headerCell, list, headerrow);
			setModelExcelHeight(ws);
		}
		return true;
	}

	/**
	 * 导出封面--一个各自，设置行宽，列高,忽略单元格
	 * 
	 * @param ws
	 */
	private boolean exportLikeFaceWithModel(WritableSheet ws, int k)
			throws Exception {
		// if (k != 0)
		// return false;
		try {
			jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat();
			wcfFC.setBorder(Border.ALL, BorderLineStyle.THIN);
			if (exprops != null) {
				if (oReportTypes[k] instanceof RELikeFace) {
					dsLikeFace = ((RELikeFace) oReportTypes[k]).getDsData();
				}
				Arrays.sort(exprops.getNumberViewInTable());
			} else {
				dsLikeFace = exprop.getDsFace()[k];
				Arrays.sort(exprop.getNumberViewInTable());
			}
			if ((dsLikeFace == null) || dsLikeFace.isEmpty()) {
				this.sErrorInfo = "数据内容为空";
				return false;
			}
			// 添加单元格
			dsLikeFace.beforeFirst();
			while (dsLikeFace.next()) {
				int colnow = dsLikeFace.fieldByName("FIELD_COLUMN")
						.getInteger();
				int rownow = dsLikeFace.fieldByName("FIELD_ROW").getInteger() + 2;
				CellFormat cellf = ws.getCell(colnow, rownow).getCellFormat();
				if (cellf == null) {
					cellf = wcfFC;
				}
				if (Common.isNumber(dsLikeFace.fieldByName("FIELD_VALUE")
						.getString())
						&& !Common.isNullStr(dsLikeFace.fieldByName(
								"FIELD_VALUE").getString())) {
					// 如果是数字需要格式化
					String value = dsLikeFace.fieldByName("FIELD_VALUE")
							.getString();
					value = Common.nonNullStr(value);
					Number number = new Number(dsLikeFace.fieldByName(
							"FIELD_COLUMN").getInteger(), dsLikeFace
							.fieldByName("FIELD_ROW").getInteger() + 2, Float
							.parseFloat(value));
					double bb = 0;
					if (!Common.isNullStr(value)) {
						bb = Double.parseDouble(value);
					}
					number.setCellFormat(cellf);
					if (!Common.isNullStr(value)) {
						number.setValue(bb);
					}
					if (Common.isNullStr(Common.nonNullStr(value))
							|| (Float.parseFloat(Common.nonNullStr(value)) == 0)) {
						Blank blank = new Blank(colnow, rownow);
						if ((colnow <= (colFormats.length - 1))
								&& (colFormats[colnow] != null)) {
							blank.setCellFormat(colFormats[colnow]);
						} else {
							blank.setCellFormat(cellf);
						}
						ws.addCell(blank);
					} else {
						ws.addCell(number);
					}
				} else {
					jxl.write.Label labelC = new jxl.write.Label(colnow,
							rownow, dsLikeFace.fieldByName("FIELD_VALUE")
									.getString());
					if (colnow < colFormats.length) {
						labelC.setCellFormat(cellf);
					}
					ws.addCell(labelC);
				}
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return true;
	}

	private boolean doExportFaceWithModel(RELikeFace ref, int i,
			boolean isEnableExportFace, WritableWorkbook wwb) throws Exception {
		this.isHaveTable = true;
		this.reportID = ref.getReportID();
		// 这个地方要判断列宽
		String sql = "SELECT MAX(FIELD_COLUMN+FIELD_COLUMNSPAN) maxcolumn FROM fb_u_qr_szzb where report_id = '"
				+ reportID + "'";
		DataSet dsMaxColumn = ExportStub.getMethod().getDataset(sql);
		if ((dsMaxColumn != null) && !dsMaxColumn.isEmpty()) {
			dsMaxColumn.beforeFirst();
			dsMaxColumn.next();
			this.colsum = dsMaxColumn.fieldByName("maxcolumn").getInteger();
		} else {
			this.colsum = 2;
		}
		if ((this.cols_Format != null) && (exprops.getFormats() != null)) {
			this.cols_Format = (String[]) exprops.getFormats().get(i);
		}
		if ((this.cols_Type != null) && (exprops.getTypes() != null)) {
			this.cols_Type = (String[]) exprops.getTypes().get(i);
		}
		this.title_Child = eact.getTitle_ChildWithDataSet(lstConfig, reportID);

		if (title_Child == null) {
			title_Child = new String[2];
		}
		// 如果导出了封面则从封面后导出，否则直接从第一个导出
		int sheet = i;
		if (isEnableExportFace) {
			sheet = i + exprops.getFaceData().length;
		} else {
			sheet = i;
		}
		Sheet wsi = null;
		WritableSheet ws = null;
		String titleNow = null;
		// 取文件的时候要注意的是，先从本机找，如果本机找不到，则从库里找，找到后再保存到本机特定地址
		String reportname = null;
		String modifytime = null;
		lstConfig = eact.getConfigDataset(reportID);
		if ((lstConfig != null) && !lstConfig.isEmpty()) {
			// 如果数据集不为空
			reportname = Common.nonNullStr(((XMLData) eact.getConfigDataset(
					reportID).get(0)).get("report_title"));
			modifytime = Common.nonNullStr(((XMLData) eact.getConfigDataset(
					reportID).get(0)).get("modifytime"));
		}
		if (isExportByReport) {
			titleNow = reportname;
		} else {
			titleNow = exprops.getTitles()[i];
		}
		sheet = sheet - iEmptyTableCounts;
		if (getModelFile(reportID, reportname, modifytime)) {
			InputStream is = new FileInputStream(new File(efile + reportname
					+ ".XLS"));
			Workbook rwb = Workbook.getWorkbook(is);
			wsi = rwb.getSheet(0);
//			ws = wwb.importSheet(titleNow, sheet, wsi);
			is.close();
			// if (wwb.getSheets()!=null){
			// if (sheet>=wwb.getSheets().length)
			// return false;
			// }
			// ws = wwb.getSheet(sheet);
			setDataCellFormats(ws);
		} else {
			// this.sErrorInfo = "没有配置该表的格式信息,初始化失败";
			// return false;
			doExportFace((RELikeFace) oReportTypes[i], i, isEnableExportFace,
					wwb);
			return true;
		}
		// 如果导出了封面则从封面后导出，否则直接从第一个导出
		addTableTitleWithModel(ws, i);
		// 导出封面
		exportLikeFaceWithModel(ws, i);
		return true;
	}

}
