/**
 * @# MainSourceInfoPanel.java    <文件名>
 */
package gov.nbcs.rp.dicinfo.datadict.ui;

import gov.nbcs.rp.basinfo.common.BOCache;
//import gov.nbcs.rp.basinfo.common.BaseUtil;
import gov.nbcs.rp.dicinfo.datadict.ibs.IDataDictBO;
import com.foundercy.pf.control.FCheckBox;
import com.foundercy.pf.control.FComboBox;
import com.foundercy.pf.control.FLabel;
import com.foundercy.pf.control.FRadioGroup;
import com.foundercy.pf.control.FTextArea;
import com.foundercy.pf.control.FTextField;
import com.foundercy.pf.control.RowPreferedLayout;
import com.foundercy.pf.control.TableConstraints;
import com.foundercy.pf.gl.viewer.FInputPanel;
import com.foundercy.pf.util.XMLData;

/**
 * 功能说明:主信息的录入面板
 * <P>

 */
public class MainSourceInfoPanel extends FInputPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7338072914922998405L;

	// 表
	private static final String TAB_TYPE_TABLE = "T";

	// 视图
	private static final String TAB_TYPE_VIEW = "V";

	IDataDictBO dataDicBean = null;

	private XMLData curData = null;

	private XMLData oriData = null;// 原始数据,用于刷新

	private FTextField tableDivName = null;

	private FTextField dicID = null;

	FCheckBox isInUse;

	FCheckBox isaudit;

	FCheckBox issent;

	FCheckBox issource;

	FTextField tableName;

	// FTextField makedate;
	//
	// FTextField makeman;

	FTextArea amemo;

	FCheckBox isbatchno;

	FCheckBox sup_ver;

	public MainSourceInfoPanel(XMLData aData) {
		dataDicBean = (IDataDictBO) BOCache.getBO("rp.dataDictService");
		initUI();
		setDispData(aData);
	}

	public MainSourceInfoPanel() {
		dataDicBean = (IDataDictBO) BOCache.getBO("rp.dataDictService");
		initUI();
	}

	private void initUI() {
		// 用于记录，并不显示
		dicID = new FTextField("字典ID");
		dicID.setTitleAdapting(true);
		dicID.setId("dicid");
		dicID.setVisible(false);

		isInUse = new FCheckBox("           是否启用  ");
		isInUse.setTitleAdapting(true);
		isInUse.setId("isinuse");

		isaudit = new FCheckBox("是否支持钩稽审核 ");
		isaudit.setTitleAdapting(true);
		isaudit.setId("isaudit");

		issent = new FCheckBox("是否支持数据传送 ");
		issent.setTitleAdapting(true);
		issent.setId("issent");

		FRadioGroup box = new FRadioGroup();
		box.setId("objectTypeID");
		box.setRefModel(TAB_TYPE_TABLE + "#物理表+" + TAB_TYPE_VIEW + "#视图");

		issource = new FCheckBox("是否用于报表定义");
		issource.setTitleAdapting(true);
		issource.setId("issource");

		// 是否版本号
		sup_ver = new FCheckBox("是否支持版本号 ");
		sup_ver.setTitleAdapting(true);
		sup_ver.setId("sup_ver");

		isbatchno = new FCheckBox("      是否支持批次号 ");
		isbatchno.setTitleAdapting(true);
		isbatchno.setId("isbatchno");

		tableName = new FTextField("      表中文名称  ");
		tableName.setId("tablecname");
		tableName.setTitleAdapting(true);

		FTextField tableEName = new FTextField("      表英文名称  ");
		tableEName.setId("tableename");
		tableEName.setTitleAdapting(true);

		tableDivName = new FTextField("      单位用表名  ");
		tableDivName.setId("tableename_div");
		tableDivName.setTitleAdapting(true);

		// makedate = new FTextField(" 创建日期 ");
		// makedate.setId("makedate");
		// makedate.setTitleAdapting(true);
		//
		// makeman = new FTextField(" 创建人 ");
		// makeman.setId("makeman");
		// makeman.setTitleAdapting(true);

		FComboBox tabprop = new FComboBox("      表类别      ");
		tabprop.setId("parent_id");
//		BaseUtil.setFComboEditable(tabprop, false);
		tabprop.setRefModel(dataDicBean.getTableTypeRefString());
		tabprop.setTitleAdapting(true);

		amemo = new FTextArea("      表说明      ");
		amemo.setId("amemo");
		amemo.setTitleAdapting(true);

		// RowPreferedLayout layMain = new RowPreferedLayout(10);
		// layMain.setColumnWidth(100);
		// this.setLayout(layMain);
		//
		// this.add(sup_ver, new TableConstraints(1, 2, false, false));
		// this.add(isInUse, new TableConstraints(1, 2, false, false));
		// this.add(isbatchno, new TableConstraints(1, 2, false, false));
		// // --------------
		// this.add(tableName, new TableConstraints(1, 6, false, false));
		// this.add(tableEName, new TableConstraints(1, 6, false, false));
		// this.add(tableDivName, new TableConstraints(1, 6, false, false));
		// this.add(tabprop, new TableConstraints(1, 6, false, false));
		//		

		RowPreferedLayout layTitle = new RowPreferedLayout(10);

		layTitle.setColumnWidth(100);

		this.setLayout(layTitle);

		this.addControl(isInUse, new TableConstraints(1, 2, false, false));
		this.addControl(issource, new TableConstraints(1, 2, false, false));
		this.addControl(issent, new TableConstraints(1, 2, false, false));
		this.addControl(new FLabel(), new TableConstraints(1, 2, false, false));
		this.addControl(box, new TableConstraints(1, 2, false, false));
		this.addControl(isbatchno, new TableConstraints(1, 2, false, false));
		this.addControl(isaudit, new TableConstraints(1, 2, false, false));
		this.addControl(sup_ver, new TableConstraints(1, 2, false, false));
		this.addControl(tableName, new TableConstraints(1, 6, false, false));

		this.addControl(tableEName, new TableConstraints(1, 6, false, false));

		this.addControl(tableDivName, new TableConstraints(1, 6, false, false));
		// this.addControl(makedate, new TableConstraints(1, 6, false, false));
		// this.addControl(makeman, new TableConstraints(1, 6, false, false));
		this.addControl(tabprop, new TableConstraints(1, 6, false, false));
		this.addControl(amemo, new TableConstraints(3, 6, false, false));
		this.add(dicID);
		this.setData(curData);

	}

	public void setEdit(boolean isEdit) {
		tableDivName.setEditable(isEdit);
		isInUse.setEditable(isEdit);
		isaudit.setEditable(isEdit);
		issent.setEditable(isEdit);
		issource.setEditable(isEdit);
		tableName.setEditable(isEdit);
		// makedate.setEditable(isEdit);
		// makeman.setEditable(isEdit);
		amemo.setEditable(isEdit);
		isbatchno.setEditable(isEdit);
		sup_ver.setEditable(isEdit);

	}

	public void refreshData() {
		curData = (XMLData) oriData.clone();
		this.setData(curData);
		setEdit(false);

	}

	public void setDispData(XMLData aData) {
		if (aData == null) {
			this.oriData = null;
			this.curData = null;
			setData(null);
			return;
		}
		this.oriData = (XMLData) aData.clone();
		this.curData = (XMLData) aData.clone();
		setData(aData);

	}

}
