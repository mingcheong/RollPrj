package gov.nbcs.rp.queryreport.batchreport.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.foundercy.pf.control.FPanel;
import com.foundercy.pf.reportcy.common.exception.MaxNumOfOnlineUsersConnectException;
import com.foundercy.pf.reportcy.common.exception.ReportException;
import com.foundercy.pf.reportcy.common.exception.ReportUiException;
import com.foundercy.pf.reportcy.common.gui.util.MessageBox;
import com.foundercy.pf.reportcy.common.gui.util.local.LocalCacheUtil;
import com.foundercy.pf.reportcy.common.ifun.IExpansionReport;
import com.foundercy.pf.reportcy.common.ifun.IReportBasicAttr;
import com.foundercy.pf.reportcy.common.ifun.operator.IClientReportOperation;
import com.foundercy.pf.reportcy.common.ifun.ui.IClientReportDisplay;
import com.foundercy.pf.reportcy.common.op.CommonOpFactory;
import com.foundercy.pf.reportcy.common.util.IoUtil;
import com.foundercy.pf.reportcy.common.util.Log;
import com.foundercy.pf.reportcy.common.util.ReportParameterTools;
import com.foundercy.pf.reportcy.common.util.ReportTools;
import com.foundercy.pf.reportcy.common.util.StringEx;
import com.foundercy.pf.reportcy.report.op.ReportCore;
import com.foundercy.pf.reportcy.report.ui.ReportDisplayPanel;
import com.foundercy.pf.reportcy.summary.iface.IDataSourceManager;
import com.foundercy.pf.reportcy.summary.iface.IReportQuerySource;
import com.foundercy.pf.reportcy.summary.iface.enumer.IEnumSourceManager;
import com.foundercy.pf.reportcy.summary.iface.paras.IParameter;
import com.foundercy.pf.reportcy.summary.rstatic.op.RuntimeStaticReportCore;
import com.foundercy.pf.reportcy.summary.rstatic.op.SummaryClientOperation;
import com.foundercy.pf.reportcy.summary.summaryset.SummaryReportOperation;
import com.foundercy.pf.reportcy.summary.ui.SinglePageDisplayPanel;
import com.foundercy.pf.reportcy.summary.util.SummaryReportLocalCatchManager;
import com.fr.base.core.FRCoreContext;
import com.fr.report.GroupReport;
import com.fr.report.GroupReportAttr;
import com.fr.report.Report;

public class BrClientReport extends FPanel {

	public static IClientReportDisplay createDisplayPanel(
			IClientReportOperation reportCore) throws ReportException {
		IClientReportDisplay plugin = null;
		IExpansionReport exp = reportCore.getExpansionReport();
		if (exp == null)
			throw new ReportException("请设置报表属性");
		IReportBasicAttr attr = exp.getReportBasicAttr();
		if (attr == null)
			throw new ReportException("请设置报表属性");
		int dispType = attr.getDispType();
		switch (dispType) {
		case 1: // '\001'
			plugin = new SinglePageDisplayPanel(reportCore);
			break;

		case 0: // '\0'
		default:
			plugin = new BrReportDisplayPanel(reportCore);
			break;
		}
		return plugin;
	}

	public BrClientReport() {
		displayClass = null;
		summaryReportMap = null;
		summaryReportLocalCatchMap = null;
		summaryReportExecuteMap = null;
		summaryCurrentReport = null;
		summaryReportIdAndNameMap = null;
		currentReportID = "";
		currentReportName = "";
		iParametersModel = null;
		iDataSourceManagerModel = null;
		iEnumSourceManagerModel = null;
		releaseListener = new AncestorListener() {

			public void ancestorAdded(AncestorEvent ancestorevent) {
			}

			public void ancestorMoved(AncestorEvent ancestorevent) {
			}

			public void ancestorRemoved(AncestorEvent ancestorevent) {
			}

		};
		mouseListener = new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getClickCount() <= 1)
					;
			}

		};
		isFirstLoadData = false;
		isAutoMatchParametersByHyperling = false;
		queryMode = 0;
		addAncestorListener(releaseListener);
		addMouseListener(mouseListener);
	}

	public void close() {
		try {
			if (reportDisplay != null)
				reportDisplay.release();
			reportDisplay = null;
		} catch (Exception ex) {
			Log.logger.error("", ex);
		}
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportId() {
		return reportId;
	}

	public void initize() {
		Report report = null;
		try {
			byte byteArray[] = CommonOpFactory.getOP().readReportByteByID(
					reportId);
			report = IoUtil.readReportFromByteArray(byteArray);
			byteArray = null;
		} catch (Exception ex) {
			MessageBox.showErrorMessge(ex, this);
		}
		String reportType = "";
		String summaryReportId = "";
		if (report == null)
			return;
		com.fr.base.XMLable xmlAble = report.getXMLable();
		IReportQuerySource iReportQuerySource = null;
		if (xmlAble != null) {
			iReportQuerySource = (IReportQuerySource) xmlAble;
			if (iReportQuerySource != null) {
				reportType = iReportQuerySource.getReportBasicAttr()
						.getReportType();
				summaryReportId = iReportQuerySource.getReportBasicAttr()
						.getReportID();
			}
		}
		if ("RSTATIC".equalsIgnoreCase(reportType))
			try {
				initSummaryReport(reportType, summaryReportId);
				initSummaryReportDisplay(summaryCurrentReport);
			} catch (Exception ex) {
				MessageBox.showErrorMessge(ex, this);
			}
		else if ("FUNC_FILL".equalsIgnoreCase(reportType)) {
			setLayout(new BorderLayout());
			setPreferredSize(new Dimension(800, 600));
			ReportCore reportCore = new ReportCore(report);
			reportCore.setParamMap(paramMap);
			reportCore.setPareParameterList(pareParameterList);
			reportCore.setFirstLoadData(isFirstLoadData());
			reportCore.setQueryMode(getQueryMode());
			reportDisplay = getReportDisplay(reportCore);
			try {
				reportCore.execute(report);
				reportDisplay.repaintWithQueryPanel();
			} catch (Exception ex) {
				MessageBox.showErrorMessge(ex, MessageBox.getMainFrame());
			}
			if (reportDisplay != null) {
				removeAll();
				add(reportDisplay.getPluginComponent(), "Center");
			}
			setVisible(true);
		} else {
			setLayout(new BorderLayout());
			setPreferredSize(new Dimension(800, 600));
			try {
				boolean isSinglePage = ReportParameterTools
						.isSinglePageDisplay(report);
				if (isSinglePage)
					reportDisplay = initSinglePageDisplay(report);
				else
					reportDisplay = initMultilPageDisplay(report);
			} catch (RuntimeException ex) {
				Log.logger.error("", ex);
				release();
				RuntimeException runtimeException = ex;
				Throwable throwable = runtimeException.getCause();
				if (throwable instanceof MaxNumOfOnlineUsersConnectException) {
					JPanel panel = new JPanel(new BorderLayout());
					JLabel label = new JLabel("对不起，当前报表服务器已经达到最大连接数，请稍后再进行操作。");
					panel.add(label, "North");
					add(panel);
					setVisible(true);
					MessageBox
							.showMessageDialog("对不起，当前报表服务器已经达到最大连接数，请稍后再进行操作。");
					return;
				} else {
					MessageBox.showErrorMessge(ex, this);
					return;
				}
			} catch (Exception ex) {
				release();
				MessageBox.showErrorMessge(ex, this);
				return;
			}
			try {
				reportDisplay.repaintWithQueryPanel();
			} catch (Exception ex) {
				MessageBox.showErrorMessge(ex, MessageBox.getMainFrame());
			}
			setLayout(new BorderLayout());
			if (reportDisplay != null)
				add(reportDisplay.getPluginComponent(), "Center");
			setVisible(true);
		}
	}

	public void initSummaryReportDisplay(Report report) {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(800, 600));
		try {
			boolean isSinglePage = ReportParameterTools
					.isSinglePageDisplay(report);
			if (isSinglePage) {
				reportDisplay = initSinglePageDisplay(report);
			} else {
				reportDisplay = initMultilPageDisplay(report);
				if (reportDisplay instanceof ReportDisplayPanel) {
					((ReportDisplayPanel) reportDisplay)
							.setSummaryCurrentReport(summaryCurrentReport);
					((ReportDisplayPanel) reportDisplay)
							.setSummaryReportExecuteMap(summaryReportExecuteMap);
					((ReportDisplayPanel) reportDisplay)
							.setSummaryReportIdAndNameMap(summaryReportIdAndNameMap);
					((ReportDisplayPanel) reportDisplay)
							.setSummaryReportLocalCatchMap(summaryReportLocalCatchMap);
					((ReportDisplayPanel) reportDisplay)
							.setSummaryReportMap(summaryReportMap);
				}
			}
		} catch (RuntimeException ex) {
			Log.logger.error("", ex);
			release();
			RuntimeException runtimeException = ex;
			Throwable throwable = runtimeException.getCause();
			if (throwable instanceof MaxNumOfOnlineUsersConnectException) {
				JPanel panel = new JPanel(new BorderLayout());
				JLabel label = new JLabel("对不起，当前报表服务器已经达到最大连接数，请稍后再进行操作。");
				panel.add(label, "North");
				add(panel);
				setVisible(true);
				MessageBox.showMessageDialog("对不起，当前报表服务器已经达到最大连接数，请稍后再进行操作。");
				return;
			} else {
				MessageBox.showErrorMessge(ex, this);
				return;
			}
		} catch (Exception ex) {
			release();
			MessageBox.showErrorMessge(ex, this);
			return;
		}
		try {
			reportDisplay.repaintWithQueryPanel();
		} catch (Exception ex) {
			MessageBox.showErrorMessge(ex, MessageBox.getMainFrame());
		}
		setLayout(new BorderLayout());
		if (reportDisplay != null) {
			removeAll();
			add(reportDisplay.getPluginComponent(), "Center");
		}
		setVisible(true);
	}

	public ReportDisplayPanel getReportDisplay(ReportCore reportCore) {
		ReportDisplayPanel reportDisplay = null;
		String displayPanelClass;
		boolean isNoError = false;
		try {
			reportDisplay = null;
			displayPanelClass = StringEx.sNullTrim(getDisplayClass());
			if (StringEx.isEmpty(displayPanelClass)) {
				reportDisplay = new BrReportDisplayPanel(reportCore);
				return reportDisplay;
			}
			Class class1 = null;
			class1 = FRCoreContext.classForName(displayPanelClass);
			Object value = class1.newInstance();
			if (value instanceof ReportDisplayPanel) {
				reportDisplay = (ReportDisplayPanel) value;
				ReportDisplayPanel rdp = (ReportDisplayPanel) value;
				rdp.setReportCore(reportCore);
			} else {
				throw new ReportUiException(
						"传人的:\n\""
								+ displayPanelClass
								+ "\"类必须是 com.foundercy.pf.reportcy.report.ui.ReportDisplayPanel的子类.");
			}
			isNoError = true;
		} catch (ClassNotFoundException ex) {
			Log.logger.error("", ex);
			String message = "对不起,配置功能时,传人的displayClass不正确,无法找到这个类.\n"
					+ ex.getMessage() + "\n已经调用系统的默认配置来进行展现报表.";
			MessageBox.showMessageDialog(message);
		} catch (InstantiationException ex) {
			Log.logger.error("", ex);
			String message = "对不起,配置功能时,传人的displayClass不正确,类实例化错误.\n"
					+ ex.getMessage() + "\n已经调用系统的默认配置来进行展现报表.";
			MessageBox.showMessageDialog(message);
		} catch (IllegalAccessException ex) {
			Log.logger.error("", ex);
			String message = "对不起,配置功能时,传人的displayClass不正确,类实例化错误.\n"
					+ ex.getMessage() + "\n已经调用系统的默认配置来进行展现报表.";
			MessageBox.showMessageDialog(message);
		} catch (ReportUiException ex) {
			Log.logger.error("", ex);
			String message = "对不起,配置功能时,传人的displayClass不正确,无法构造面板.\n"
					+ ex.getMessage() + "\n已经调用系统的默认配置来进行展现报表.";
			MessageBox.showMessageDialog(message);
		} catch (Exception ex) {
			Log.logger.error("", ex);
			String message = "对不起,配置功能时,传人的displayClass不正确,无法构造面板.\n"
					+ ex.getMessage() + "\n已经调用系统的默认配置来进行展现报表.";
			MessageBox.showMessageDialog(message);
		}
		if (!isNoError)
			reportDisplay = new BrReportDisplayPanel(reportCore);
		return reportDisplay;
	}

	public static void main(String args[]) {
		new BrClientReport();
	}

	public void setPareParameterList(java.util.List pareParameterList) {
		this.pareParameterList = pareParameterList;
	}

	public boolean isFirstLoadData() {
		return isFirstLoadData;
	}

	public void setFirstLoadData(boolean isFirstLoadData) {
		this.isFirstLoadData = isFirstLoadData;
	}

	public void setAutoMatchParametersByHyperling(
			boolean isAutoMatchParametersByHyperling) {
		this.isAutoMatchParametersByHyperling = isAutoMatchParametersByHyperling;
	}

	public Map getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map paramMap) {
		this.paramMap = paramMap;
	}

	public void release() {
		try {
			if (reportDisplay != null)
				reportDisplay.release();
			reportDisplay = null;
		} catch (Exception ex) {
			Log.logger.error("", ex);
		}
	}

	public String getDisplayClass() {
		return displayClass;
	}

	public void setDisplayClass(String displayClass) {
		this.displayClass = displayClass;
	}

	protected IClientReportDisplay initSinglePageDisplay(Report report)
			throws CloneNotSupportedException, Exception {
		SummaryClientOperation reportCore = new SummaryClientOperation(report);
		reportCore.setQueryMode(getQueryMode());
		reportCore.setRetroParameterList(pareParameterList);
		reportCore
				.setAutoMatchParametersByHyperling(isAutoMatchParametersByHyperling);
		IClientReportDisplay previewPane = new SinglePageDisplayPanel(
				reportCore);
		reportCore.execute(report);
		reportCore.getDisplay().getDisplayReportAble().displayReport(0);
		switch (getQueryMode()) {
		case 0: // '\0'
		case 1: // '\001'
		case 2: // '\002'
		default:
			reportCore.setQueryMode(1);
			// fall through

		case 3: // '\003'
			return previewPane;
		}
	}

	protected IClientReportDisplay initMultilPageDisplay(Report report)
			throws Exception {
		ReportCore reportCore = null;
		reportCore = new ReportCore(report);
		reportCore.setParamMap(paramMap);
		reportCore.setQueryMode(getQueryMode());
		reportCore
				.setAutoMatchParametersByHyperling(isAutoMatchParametersByHyperling);
		reportDisplay = getReportDisplay(reportCore);
		reportCore.setPareParameterList(pareParameterList);
		if ("RSTATIC".equals(ReportTools.getReportType(report))
				|| "RSTATIC".equals(ReportTools.getReportType(report))) {
			IClientReportOperation op = reportCore.getReportOperationProxy();
			if (op instanceof RuntimeStaticReportCore) {
				RuntimeStaticReportCore core = (RuntimeStaticReportCore) op;
				core.setNowReport(report);
				GroupReport groupReport = (GroupReport) report;
				int rowOfeveryPage = 5;
				GroupReportAttr groupReportAttr = groupReport
						.getGroupReportAttr();
				if (groupReportAttr.getPageBreakAfterDetailRepeatTimes() <= -1)
					groupReportAttr
							.setPageBreakAfterDetailRepeatTimes(rowOfeveryPage);
			}
		}
		reportCore.execute(report);
		return reportDisplay;
	}

	public int getQueryMode() {
		return queryMode;
	}

	public void setQueryMode(int queryMode) {
		this.queryMode = queryMode;
	}

	public void initSummaryReport(String reportType, String reportId)
			throws Exception {
		if ("RSTATIC".equalsIgnoreCase(reportType)) {
			getAllSummaryReport(reportType, reportId);
			getAllSummaryReportLocalCatchNew(reportType, reportId);
		}
	}

	public void getAllSummaryReportNew(String reportType, String reportId)
			throws Exception {
		if ("RSTATIC".equalsIgnoreCase(reportType)) {
			summaryReportMap = SummaryReportOperation
					.getSummaryReport(reportId);
			if (summaryReportIdAndNameMap == null)
				summaryReportIdAndNameMap = new LinkedHashMap();
			else
				summaryReportIdAndNameMap.clear();
			Iterator it = summaryReportMap.entrySet().iterator();
			do {
				if (!it.hasNext())
					break;
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				Report summaryReport = (Report) entry.getValue();
				String reportID = (String) entry.getKey();
				com.fr.base.XMLable xmlAble = summaryReport.getXMLable();
				IReportQuerySource iReportQuerySource = null;
				if (xmlAble != null) {
					iReportQuerySource = (IReportQuerySource) xmlAble;
					String reportName = iReportQuerySource.getReportBasicAttr()
							.getReportName();
					if (reportID.indexOf("_") < 0) {
						reportName = reportName + "(\u6A21\u677F)";
						iParametersModel = iReportQuerySource
								.getParameterArray();
						iDataSourceManagerModel = iReportQuerySource
								.getDataSourceManager();
						iEnumSourceManagerModel = iReportQuerySource
								.getEnumSourceManager();
					}
					summaryReportIdAndNameMap.put(reportID, reportName);
				}
			} while (true);
		}
	}

	public void getAllSummaryReportLocalCatchNew(String reportType,
			String reportId) throws Exception {
		boolean flag = false;
		if (summaryReportLocalCatchMap == null)
			summaryReportLocalCatchMap = new LinkedHashMap();
		else
			summaryReportLocalCatchMap.clear();
		com.foundercy.pf.reportcy.common.gui.util.local.ILocalCatchManagerInterface iLocalCatchManagerInterface = LocalCacheUtil
				.getLocalCatch(reportId, reportType);
		if (iLocalCatchManagerInterface instanceof SummaryReportLocalCatchManager) {
			SummaryReportLocalCatchManager summaryReportLocalCatchManager = (SummaryReportLocalCatchManager) iLocalCatchManagerInterface;
			if (summaryReportMap != null && summaryReportMap.size() > 0) {
				Report summaryReport = (Report) summaryReportMap.get(reportId);
				com.fr.base.XMLable xmlAble = summaryReport.getXMLable();
				if (xmlAble != null) {
					IReportQuerySource iReportQuerySource = (IReportQuerySource) xmlAble;
					if (iDataSourceManagerModel == null)
						iDataSourceManagerModel = iReportQuerySource
								.getDataSourceManager();
					if (iEnumSourceManagerModel == null)
						iEnumSourceManagerModel = iReportQuerySource
								.getEnumSourceManager();
					if (iParametersModel == null)
						iParametersModel = iReportQuerySource
								.getParameterArray();
				}
			}
			Vector vecFile = summaryReportLocalCatchManager
					.getAllSummaryReportCptFileNames(reportId);
			if (vecFile != null) {
				for (int i = 0; i < vecFile.size(); i++) {
					String reportIDLocal = "";
					String reportNameLocal = "";
					String fileName = (String) vecFile.get(i);
					Report reportLocal = summaryReportLocalCatchManager
							.loadLocalCpt(fileName);
					IParameter iParametersLocal[] = null;
					IParameter iParametersClone[] = null;
					if (reportLocal == null)
						continue;
					com.fr.base.XMLable xmlAbleLocal = reportLocal.getXMLable();
					if (xmlAbleLocal != null) {
						IReportQuerySource iReportQuerySourceLocal = (IReportQuerySource) xmlAbleLocal;
						iReportQuerySourceLocal
								.setDataSourceManager(iDataSourceManagerModel);
						iReportQuerySourceLocal
								.setEnumSourceManager(iEnumSourceManagerModel);
						iParametersLocal = iReportQuerySourceLocal
								.getParameterArray();
						iParametersClone = getIParametersClone(iParametersModel);
						if (iParametersLocal == null) {
							iParametersLocal = iParametersClone;
						} else {
							String parName = "";
							IParameter iParameter = null;
							if (iParametersClone != null) {
								for (int j = 0; j < iParametersClone.length; j++) {
									if (iParametersClone[j] == null)
										continue;
									parName = iParametersClone[j].getName();
									iParameter = getIParameter(
											iParametersLocal, parName);
									if (iParameter != null)
										iParametersClone[j] = iParameter;
								}

							}
							iParametersLocal = iParametersClone;
						}
						iReportQuerySourceLocal
								.setParameterArray(iParametersLocal);
						reportIDLocal = iReportQuerySourceLocal
								.getReportBasicAttr().getReportID();
						reportNameLocal = iReportQuerySourceLocal
								.getReportBasicAttr().getReportName();
						reportLocal.setXMLable(iReportQuerySourceLocal);
					}
					if (reportLocal instanceof GroupReport)
						((GroupReport) reportLocal).setTableData(null);
					summaryReportLocalCatchMap.put(reportIDLocal, reportLocal);
					if (!summaryReportIdAndNameMap.containsKey(reportIDLocal))
						summaryReportIdAndNameMap.put(reportIDLocal,
								reportNameLocal);
					if (fileName.endsWith("-true.cpt")) {
						flag = true;
						summaryCurrentReport = (GroupReport) reportLocal
								.clone();
					}
				}

			}
		}
		if (!flag) {
			Iterator it = summaryReportMap.entrySet().iterator();
			if (it.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				summaryCurrentReport = (GroupReport) ((GroupReport) entry
						.getValue()).clone();
			}
		}
	}

	public void getAllSummaryReport(String reportType, String reportId)
			throws Exception {
		if ("RSTATIC".equalsIgnoreCase(reportType)) {
			summaryReportMap = SummaryReportOperation
					.getAllSummaryReport(reportId);
			if (summaryReportIdAndNameMap == null)
				summaryReportIdAndNameMap = new LinkedHashMap();
			else
				summaryReportIdAndNameMap.clear();
			Iterator it = summaryReportMap.entrySet().iterator();
			do {
				if (!it.hasNext())
					break;
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				Report summaryReport = (Report) entry.getValue();
				String reportID = (String) entry.getKey();
				com.fr.base.XMLable xmlAble = summaryReport.getXMLable();
				IReportQuerySource iReportQuerySource = null;
				if (xmlAble != null) {
					iReportQuerySource = (IReportQuerySource) xmlAble;
					String reportName = iReportQuerySource.getReportBasicAttr()
							.getReportName();
					if (reportID.indexOf("_") < 0) {
						reportName = reportName + "(\u6A21\u677F)";
						iParametersModel = iReportQuerySource
								.getParameterArray();
						iDataSourceManagerModel = iReportQuerySource
								.getDataSourceManager();
						iEnumSourceManagerModel = iReportQuerySource
								.getEnumSourceManager();
					}
					summaryReportIdAndNameMap.put(reportID, reportName);
				}
			} while (true);
		}
	}

	public void getAllSummaryReportLocalCatch(String reportType, String reportId)
			throws Exception {
		if (summaryReportLocalCatchMap == null)
			summaryReportLocalCatchMap = new LinkedHashMap();
		else
			summaryReportLocalCatchMap.clear();
		com.foundercy.pf.reportcy.common.gui.util.local.ILocalCatchManagerInterface iLocalCatchManagerInterface = LocalCacheUtil
				.getLocalCatch(reportId, reportType);
		if (iLocalCatchManagerInterface instanceof SummaryReportLocalCatchManager) {
			SummaryReportLocalCatchManager summaryReportLocalCatchManager = (SummaryReportLocalCatchManager) iLocalCatchManagerInterface;
			int i = 0;
			for (Iterator it = summaryReportMap.entrySet().iterator(); it
					.hasNext(); i = 2) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				String reportID = (String) entry.getKey();
				if (i == 0) {
					Report summaryReport = (Report) entry.getValue();
					com.fr.base.XMLable xmlAble = summaryReport.getXMLable();
					if (xmlAble != null) {
						IReportQuerySource iReportQuerySource = (IReportQuerySource) xmlAble;
						if (iDataSourceManagerModel == null)
							iDataSourceManagerModel = iReportQuerySource
									.getDataSourceManager();
						if (iEnumSourceManagerModel == null)
							iEnumSourceManagerModel = iReportQuerySource
									.getEnumSourceManager();
						if (iParametersModel == null)
							iParametersModel = iReportQuerySource
									.getParameterArray();
					}
				}
				Report reportLocal = summaryReportLocalCatchManager
						.loadCpt(reportID);
				IParameter iParametersLocal[] = null;
				IParameter iParametersClone[] = null;
				if (reportLocal == null)
					continue;
				com.fr.base.XMLable xmlAbleLocal = reportLocal.getXMLable();
				if (xmlAbleLocal != null) {
					IReportQuerySource iReportQuerySourceLocal = (IReportQuerySource) xmlAbleLocal;
					iReportQuerySourceLocal
							.setDataSourceManager(iDataSourceManagerModel);
					iReportQuerySourceLocal
							.setEnumSourceManager(iEnumSourceManagerModel);
					iParametersLocal = iReportQuerySourceLocal
							.getParameterArray();
					iParametersClone = getIParametersClone(iParametersModel);
					if (iParametersLocal == null) {
						iParametersLocal = iParametersClone;
					} else {
						String parName = "";
						IParameter iParameter = null;
						for (int j = 0; j < iParametersClone.length; j++) {
							if (iParametersClone[j] == null)
								continue;
							parName = iParametersClone[j].getName();
							iParameter = getIParameter(iParametersLocal,
									parName);
							if (iParameter != null)
								iParametersClone[j] = iParameter;
						}

						iParametersLocal = iParametersClone;
					}
					iReportQuerySourceLocal.setParameterArray(iParametersLocal);
					reportLocal.setXMLable(iReportQuerySourceLocal);
				}
				if (reportLocal instanceof GroupReport)
					((GroupReport) reportLocal).setTableData(null);
				summaryReportLocalCatchMap.put(reportID, reportLocal);
			}

		}
	}

	private IParameter getIParameter(IParameter iParameters[], String name) {
		IParameter iParameter = null;
		if (iParameters != null) {
			int m = 0;
			do {
				if (m >= iParameters.length)
					break;
				IParameter iParameterTemp = iParameters[m];
				if (iParameterTemp != null
						&& iParameterTemp.getName().equalsIgnoreCase(name)) {
					iParameter = iParameterTemp;
					break;
				}
				m++;
			} while (true);
		}
		return iParameter;
	}

	private IParameter[] getIParametersClone(IParameter iParameters[])
			throws Exception {
		IParameter iParametersClone[] = null;
		if (iParameters != null) {
			iParametersClone = new IParameter[iParameters.length];
			for (int m = 0; m < iParameters.length; m++)
				if (iParameters[m] != null) {
					IParameter iParameter = (IParameter) iParameters[m].clone();
					iParametersClone[m] = iParameter;
				}

		}
		return iParametersClone;
	}

	public void getDefaultSummaryReport(String reportType, String reportId)
			throws Exception {
		com.foundercy.pf.reportcy.common.gui.util.local.ILocalCatchManagerInterface iLocalCatchManagerInterface = LocalCacheUtil
				.getLocalCatch(reportId, reportType);
		if (iLocalCatchManagerInterface instanceof SummaryReportLocalCatchManager) {
			SummaryReportLocalCatchManager summaryReportLocalCatchManager = (SummaryReportLocalCatchManager) iLocalCatchManagerInterface;
			boolean flag = false;
			Iterator it = summaryReportLocalCatchMap.entrySet().iterator();
			do {
				if (!it.hasNext())
					break;
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				String reportID = (String) entry.getKey();
				flag = summaryReportLocalCatchManager
						.getSummaryLastOpFlag(reportID);
				if (!flag)
					continue;
				summaryCurrentReport = (GroupReport) ((GroupReport) entry
						.getValue()).clone();
				break;
			} while (true);
			if (!flag) {
				it = summaryReportMap.entrySet().iterator();
				if (it.hasNext()) {
					java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
					summaryCurrentReport = (GroupReport) ((GroupReport) entry
							.getValue()).clone();
				}
			}
		}
	}

	public Map getSummaryReportMap() {
		return summaryReportMap;
	}

	public void setSummaryReportMap(Map summaryReportMap) {
		this.summaryReportMap = summaryReportMap;
	}

	public Map getSummaryReportLocalCatchMap() {
		return summaryReportLocalCatchMap;
	}

	public void setSummaryReportLocalCatchMap(Map summaryReportLocalCatchMap) {
		this.summaryReportLocalCatchMap = summaryReportLocalCatchMap;
	}

	public Map getSummaryReportExecuteMap() {
		return summaryReportExecuteMap;
	}

	public void setSummaryReportExecuteMap(Map summaryReportExecuteMap) {
		this.summaryReportExecuteMap = summaryReportExecuteMap;
	}

	public Report getSummaryCurrentReport() {
		return summaryCurrentReport;
	}

	public void setSummaryCurrentReport(GroupReport summaryCurrentReport) {
		this.summaryCurrentReport = summaryCurrentReport;
	}

	public Map getSummaryReportIdAndNameMap() {
		return summaryReportIdAndNameMap;
	}

	public void setSummaryReportIdAndNameMap(Map summaryReportIdAndNameMap) {
		this.summaryReportIdAndNameMap = summaryReportIdAndNameMap;
	}

	public IClientReportDisplay getReportDisplay() {
		return reportDisplay;
	}

	public void setReportDisplay(IClientReportDisplay reportDisplay) {
		this.reportDisplay = reportDisplay;
	}

	private static final long serialVersionUID = -3902967151865686473L;
	protected String reportId;
	protected IClientReportDisplay reportDisplay;
	protected java.util.List pareParameterList;
	protected Map paramMap;
	protected String displayClass;
	private Map summaryReportMap;
	private Map summaryReportLocalCatchMap;
	private Map summaryReportExecuteMap;
	private GroupReport summaryCurrentReport;
	private Map summaryReportIdAndNameMap;
	private String currentReportID;
	private String currentReportName;
	private IParameter iParametersModel[];
	private IDataSourceManager iDataSourceManagerModel;
	private IEnumSourceManager iEnumSourceManagerModel;
	private AncestorListener releaseListener;
	private MouseAdapter mouseListener;
	protected boolean isFirstLoadData;
	protected boolean isAutoMatchParametersByHyperling;
	protected int queryMode;
}

// Messages from Jad:
// Overlapped try statements detected. Not all exception handlers will be
// resolved in the method getReportDisplay
// Couldn't fully decompile method getReportDisplay
// Couldn't resolve all exception handlers in method getReportDisplay
// 