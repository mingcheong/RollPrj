/**
 * 
 * titile : 项目明细的数据操作
 * 
 * author : qzc
 * 
 * version: 1.0
 * 
 */
package gov.nbcs.rp.dicinfo.prjdetail.action;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.InfoPackage;
import gov.nbcs.rp.common.SysCodeRule;
import gov.nbcs.rp.common.UntPub;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.DataSetUtil;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.tree.CustomTree;
import gov.nbcs.rp.dicinfo.prjdetail.ibs.IPrjDetail;
import gov.nbcs.rp.dicinfo.prjdetail.ui.PrjDetailStub;
import com.foundercy.pf.util.Global;

public class PrjDetailAction {
    public final static int state_Browse = 0;

    public final static int state_Edit = 1;

    /**
     * 为检查是否在 项目表里是否使用而定义
     */
    public final static String check_PrjAttr = "PRJATTR_CODE";

    public final static String check_PrjAccord = "PRJACCORD_CODE";

    public final static String check_PrjStd = "PRJSTD_CODE";

    public final static String check_PrjSort = "PRJSORT_CODE";

    public void Action() {
    }

    /**
     * 构造函数
     * 
     */
    public PrjDetailAction() {
    };

    /**
     * 根据编码规则生成parentID
     * 
     * @param dsPost
     * @param aFieldName
     *            字段名
     * @param acodeRule
     *            编码规则
     * @return ds
     * @throws Exception
     */
    public static void createParentIDDependCodeRule(DataSet dsPost,
            String aFieldName, SysCodeRule aCodeRule) throws Exception {
        String sFieldValue = null;
        String sParentValue = null;
        dsPost.beforeFirst();
        dsPost.edit();
        while (dsPost.next()) {
            sFieldValue = dsPost.fieldByName(aFieldName).getString();
            sParentValue = getParentID(sFieldValue, aCodeRule);
            dsPost.fieldByName("PARID").setValue(sParentValue);
        }
    }

    /**
     * 根据字段值，编码规则获取父亲节点的值
     * 
     * @param aFieldValue
     *            字段值
     * @param acodeRule
     *            编码规则
     * @throws Exception
     */
    public static String getParentID(String aFieldValue, SysCodeRule aCodeRule)
            throws Exception {
        return aCodeRule.previous(aFieldValue);
    }

    /**
     * 修改fieldID时,如果有子节点则修改子节点信息 同时修改它的子节点的FName
     * 
     * @param dsPost
     * @param aFieldOldValue
     *            原来的值
     * @param aFieldNewValue
     *            改变后的值
     * @param aFieldName
     *            原来的LvlID值
     * @return dsPost
     */
    public static void modifyFieldIDWithChild(DataSet dsPost,
            String aFieldOldValue, String aFieldNewValue, String aFieldName)
            throws Exception {
        String bmk = dsPost.toogleBookmark();
        dsPost.maskDataChange(true);
        // 修改本级节点
        dsPost.edit();
        dsPost.locate(aFieldName, aFieldOldValue);
        dsPost.fieldByName(aFieldName).setValue(aFieldNewValue);
        dsPost.beforeFirst();
        InfoPackage info = new InfoPackage();
        info.setSuccess(true);
        // 判断是否有子节点,如果有则修改她的子节点
        while (dsPost.next()) {
            String sParentValue = dsPost.fieldByName(IPrjDetail.PAR_ID)
                    .getString();
            if (aFieldOldValue.equals(sParentValue)) {
                String sFieldValue = dsPost.fieldByName(aFieldName).getString(); // 字段值
                String sFieldAfter = sFieldValue.substring(sParentValue
                        .length() - 1); // 字段本级编码小长度
                String sFieldNewValue = (!Common.isNullStr(sFieldAfter)) ? aFieldNewValue
                        + sFieldAfter
                        : aFieldNewValue; // 新生成的字段值
                dsPost.fieldByName(aFieldName).setValue(sFieldNewValue);
            }
        }
        // 如果有字
        dsPost.gotoBookmark(bmk);
        dsPost.maskDataChange(false);
    }

    /**
     * 修改lvlid时,如果有子节点则修改子节点信息
     * 
     * @param dsPost
     * @param aFieldName
     * @param aFieldOldValue
     *            原来的值
     * @param aFieldNewValue
     *            改变后的值
     * @param aFieldName
     *            原来的LvlID值
     * @return dsPost
     */
    public static void modifyLvlIDWithChild(DataSet dsPost, String aFieldName,
            String aFieldOldValue, String aFieldNewValue, SysCodeRule aCodeRule)
            throws Exception {
        String bmk = dsPost.toogleBookmark();
        dsPost.maskDataChange(true);
        dsPost.edit();
        dsPost.locate(aFieldName, aFieldOldValue);
        dsPost.fieldByName(aFieldName).setValue(aFieldNewValue);
        dsPost.beforeFirst();
        // 判断是否有子节点,如果有则修改她的
        while (dsPost.next()) {
            String sParentValue = aCodeRule.previous(dsPost.fieldByName(
                    aFieldName).getString());
            if (aFieldOldValue.equals(sParentValue)) {
                String sFieldValue = dsPost.fieldByName(aFieldName).getString(); // 字段值
                String sFieldAfter = sFieldValue.substring(sParentValue
                        .length()); // 字段本级编码小长度
                String sFieldNewValue = (!Common.isNullStr(sFieldAfter)) ? aFieldNewValue
                        + sFieldAfter
                        : aFieldNewValue; // 新生成的字段值
                dsPost.fieldByName(aFieldName).setValue(sFieldNewValue);
            }
        }
        // 如果有字
        dsPost.gotoBookmark(bmk);
        dsPost.maskDataChange(false);
    }

    /**
     * 获取ename
     * 
     * @param aType
     *            类型
     */
    public static String getFieldEName(DataSet dsPost, String aType, String bmk)
            throws Exception {
        dsPost.maskDataChange(true);
        String bmkBefore = dsPost.toogleBookmark();
        dsPost.gotoBookmark(bmk);
        String sEName = dsPost.fieldByName(IPrjDetail.FIELD_ENAME).getString();
        dsPost.maskDataChange(false);
        dsPost.gotoBookmark(bmkBefore);
        if (!Common.isNullStr(sEName)) {
            String sFirst = (String) sEName.subSequence(0, 1);
            if (Common.isEqual(aType, sFirst))
                return sEName;
        }
        String sValue = null;
        String sFilter = IPrjDetail.FIELD_ENAME + ".indexOf('" + aType
                + "')>=0";
        DataSet ds = DataSetUtil.filterBy(dsPost, sFilter);
        if (ds.isEmpty())
            sValue = aType + "1";
        else {
            int iENameIntValue = 1;
            ds.beforeFirst();
            while (ds.next()) {
                String s = ds.fieldByName(IPrjDetail.FIELD_ENAME).getString();
                int iGetValue = Integer.parseInt(s.substring(1));
                iENameIntValue = (iGetValue > iENameIntValue) ? iGetValue
                        : iENameIntValue;
            }
            iENameIntValue++;
            sValue = aType + iENameIntValue;
        }
        return sValue;
    }

    /**
     * 删除本级编码的时候如果有子接点则修改子节点的信息(FieldID,LvlID,FName)
     * 
     * @param ds
     * @param aFieldName
     * @param aDelFieldValue
     *            删除的字段field_id
     * @return
     * @throws Exception
     */
    public static void deleteWithChildInfo(DataSet dsPost, String aDelFieldValue)
            throws Exception {
        String bmk = null;
        if (dsPost.prior())
            bmk = dsPost.toogleBookmark();
        dsPost.maskDataChange(true);
        dsPost.locate(IPrjDetail.FIELD_ID, aDelFieldValue);
        String sDelLvlID = dsPost.fieldByName(IPrjDetail.LVL_ID).getString();
        String sDelParentID = dsPost.fieldByName("PARID").getString();
        String sDelParentLvlID = getParentID(sDelLvlID, UntPub.lvlRule);
        String sDelParFName = getParFName(dsPost, IPrjDetail.FIELD_ID,
                aDelFieldValue);
        dsPost.delete();
        dsPost.edit();
        dsPost.beforeFirst();
        while (dsPost.next()) {
            if (Common.isEqual(aDelFieldValue, dsPost.fieldByName("PARID")
                    .getString())) { // 如果是其子节点则进行以下操作
                String sFieldOldValue = dsPost.fieldByName(IPrjDetail.FIELD_ID)
                        .getString(); // 原来的Field_ID 值
                String sLvlOldValue = dsPost.fieldByName(IPrjDetail.LVL_ID)
                        .getString(); // 原来的Lvl_ID的值
                String sCName = dsPost.fieldByName(IPrjDetail.FIELD_CNAME)
                        .getString();
                String sFieldSelfValue = sFieldOldValue
                        .substring(aDelFieldValue.length() - 1); // Field_ID本级编码长度
                String sFieldNewValue = (Common.isNullStr(sDelParentID)) ? sFieldSelfValue
                        : sDelParentID + sFieldSelfValue; // 新生成的sFieldNewValue
                String sLvlSelfValue = sLvlOldValue.substring(sDelLvlID
                        .length() - 1); // lvl_id 本级编码的长度
                String sLvlIDNewValue = (Common.isNullStr(sDelParentLvlID)) ? sLvlSelfValue
                        : sDelParentLvlID + sLvlSelfValue;
                modifyFieldIDWithChild(dsPost, sFieldOldValue, sFieldNewValue,
                        IPrjDetail.FIELD_ID);
                modifyLvlIDWithChild(dsPost, IPrjDetail.LVL_ID, sLvlOldValue,
                        sLvlIDNewValue, UntPub.lvlRule);
                String sNewFName = (Common.isNullStr(sDelParFName)) ? sCName
                        : sDelParFName + sCName;
                dsPost.fieldByName(IPrjDetail.FIELD_FNAME).setValue(sNewFName);
            }
        }
        dsPost.gotoBookmark(bmk);
        dsPost.maskDataChange(false);
    }

    /**
     * 获取该节点子节点最大的那条记录值的bmk
     * 
     * @param dsPost
     * @param aFieldName
     * @param aFieldValue
     * @param aCodeRule
     * @throws Exception
     */
    public static String getChildInfo(DataSet dsPost, String aFieldName,
            String aFieldValue) throws Exception {
        dsPost.maskDataChange(true);
        String bmkFirst = dsPost.toogleBookmark();
        String bmk = null;
        int iValue = 0;
        String sFieldValue = "0";
        dsPost.beforeFirst();
        while (dsPost.next()) {
            if (Common.isEqual(aFieldValue, dsPost.fieldByName("PARID")
                    .getString())) {
                sFieldValue = dsPost.fieldByName(aFieldName).getString();
                iValue = (Integer.parseInt(sFieldValue) > iValue) ? Integer
                        .parseInt(sFieldValue) : iValue;
            }
        }
        if (dsPost.locate(aFieldName, Common.getStrID(new BigDecimal(iValue),
                sFieldValue.length())))
            ;
        bmk = dsPost.toogleBookmark();
        dsPost.maskDataChange(false);
        dsPost.gotoBookmark(bmkFirst);
        return bmk;
    }

    /**
     * 根据bookmark,字段名获取字段值
     * 
     * @param ds
     * @param bmk
     * @param aFieldName
     * @return
     */
    public static String getValue(DataSet dsPost, String bmk, String aFieldName)
            throws Exception {
        dsPost.maskDataChange(true);
        String bmkFirst = dsPost.toogleBookmark();
        dsPost.gotoBookmark(bmk);
        String value = (Common.isNullStr(dsPost.fieldByName(aFieldName)
                .getString())) ? "" : dsPost.fieldByName(aFieldName)
                .getString();
        dsPost.maskDataChange(false);
        dsPost.gotoBookmark(bmkFirst);
        return value;
    }

    /**
     * 获取字段父亲节点的fname的值
     * 
     * @param dsPost
     * @param aFieldName
     *            定位字段
     * @param aFieldValue
     *            定位字段的值
     * @return 结果
     * @throws Exception
     */
    public static String getParFName(DataSet dsPost, String aFieldName,
            String aFieldValue) throws Exception {
        dsPost.maskDataChange(true);
        String bmk = dsPost.toogleBookmark();
        String sParFName = null;
        String sParID = dsPost.fieldByName("PARID").getString();
        if (dsPost.locate(aFieldName, sParID))
            sParFName = dsPost.fieldByName(IPrjDetail.FIELD_FNAME).getString();
        dsPost.gotoBookmark(bmk);
        dsPost.maskDataChange(false);
        return sParFName;
    }

    /**
     * 获取子节点个数
     * 
     * @param dsPost
     * @param aFieldValue
     * @return
     * @throws Exception
     */
public static int getChildNum(DataSet dsPost, String aFieldValue)
			throws Exception {
		dsPost.maskDataChange(true);
		String bmk = dsPost.toogleBookmark();
		int num = 0;
		dsPost.beforeFirst();
		while (dsPost.next()) {
            String parID = dsPost.fieldByName("PARID").getString();
			if (Common.isEqual(aFieldValue, parID))
				num++;
		}
		dsPost.gotoBookmark(bmk);
		dsPost.maskDataChange(false);
		return num;
	}
    /**
     * 在节点之后插入一节点，后面的节点顺序加一，并且如果有子节点则改变子节点 插入同级节点
     * 
     * @param ds
     * @param bmk
     *            引起改变的那条记录的bookmark
     * @param aFieldName
     *            字段名
     * @param aFieldValue
     *            字段值
     * @param aSysCodeRule
     *            编码规则
     * @throws Exception
     * 
     */
    public static String insertCodeDependCell(DataSet dsPost,
            String aDetailCodeValue, String bmk, String aFieldName,
            String aFieldValue, SysCodeRule aSysCodeRule) throws Exception {
        String bmkInsert = null;
        dsPost.maskDataChange(true);
        // 获取前期要准备的值
        String sLvlID = dsPost.fieldByName(IPrjDetail.LVL_ID).getString(); // 层次码
        String sParID = dsPost.fieldByName(IPrjDetail.PAR_ID).getString(); // 父级节点
        String sParFName = getParFName(dsPost, aFieldName, aFieldValue);
        String sCodeNow = null;
        String sLvlNow = null;
        dsPost.edit();
        dsPost.beforeFirst();
        while (dsPost.next()) {
            sCodeNow = dsPost.fieldByName(aFieldName).getString();
            sLvlNow = dsPost.fieldByName(IPrjDetail.LVL_ID).getString();
            // 如果现在的编码长度比要改变的小，则不改变
            sCodeNow = modCode(aFieldValue, sCodeNow);
            // 修改现在的编码
            sLvlNow = modCode(sLvlID, sLvlNow);
            dsPost.fieldByName(aFieldName).setValue(sCodeNow);
            dsPost.fieldByName(IPrjDetail.LVL_ID).setValue(sLvlNow);
            dsPost.fieldByName(IPrjDetail.PAR_ID).setValue(sParID);
        }
        dsPost.gotoBookmark(bmkInsert);
        dsPost.append();
        bmkInsert = dsPost.toogleBookmark();
        sParFName = Common.isNullStr(sParFName) ? "待定" : sParFName + ".待定";
        setColData(dsPost, aFieldName, aFieldValue, sParFName, sLvlID,
                aDetailCodeValue);
        dsPost.gotoBookmark(bmk);
        dsPost.maskDataChange(false);
        return bmkInsert;
    }

    /**
     * 修改编码
     * 
     * @param codeDep
     *            依据的编码
     * @param codeNow
     *            要修改的编码
     * @return
     */
    private static String modCode(String codeDep, String codeNow) {
        if (codeNow.length() < codeDep.length())
            return codeNow;
        String codeReturn = null;
        int iDepLen = codeDep.length();
        String codeFor = null; // 前半部分
        String codeBeh = null; // 后半部分
        codeFor = codeNow.substring(0, iDepLen);
        codeBeh = codeNow.substring(iDepLen, codeNow.length());
        BigDecimal codeForValue = new BigDecimal(codeFor);
        BigDecimal codeDepValue = new BigDecimal(codeDep);
        if (codeForValue.compareTo(codeDepValue) < 0)
            return codeNow;
        codeReturn = Common.getStrID(codeForValue.add(new BigDecimal(1)),
                codeFor.length());
        codeReturn = codeReturn + codeBeh;
        return codeReturn;
    }

    /**
     * 当添加同级节点的时候要插入的空白值
     * 
     * @param dsPost
     *            要操作的数据集
     * @param aFieldName
     *            编码字段名
     * @param aFieldValue
     *            编码字段值
     * @param sParFName
     *            父节点名称
     * @param sLvlID
     *            层次码
     * @param aDetailCodeValue
     *            明细编号
     * @throws Exception
     */
    private static void setColData(DataSet dsPost, String aFieldName,
            String aFieldValue, String sParFName, String sLvlID,
            String aDetailCodeValue) throws Exception {
        dsPost.fieldByName(aFieldName).setValue(aFieldValue);
        dsPost.fieldByName(IPrjDetail.LVL_ID).setValue(sLvlID);
        dsPost.fieldByName(IPrjDetail.FIELD_FNAME).setValue(sParFName);
        dsPost.fieldByName(IPrjDetail.FIELD_CNAME).setValue("待定");
        dsPost.fieldByName(IPrjDetail.SET_YEAR).setValue(Global.loginYear);
        dsPost.fieldByName(IPrjDetail.DETAIL_CODE).setValue(aDetailCodeValue);
        String bmkNow = dsPost.toogleBookmark();
        String sEName = getFieldEName(dsPost, "F", bmkNow);
        dsPost.fieldByName(IPrjDetail.FIELD_ENAME).setValue(sEName);
        dsPost.fieldByName(IPrjDetail.STD_TYPE).setValue("0");
        dsPost.fieldByName(IPrjDetail.FIELD_INDEX).setValue("0");
    }

    /**
     * 在节点下插入一子节点，如果子节点不为空，则插在子节点的最后一位,如果为空则直接添加在下面 插入子级节点
     * 
     * @param ds
     * @param bmk
     *            引起改变的那条记录的bookmark
     * @param aFieldName
     *            字段名
     * @param aFieldValue
     *            字段值
     * @param aSysCodeRule
     *            编码规则
     * @throws Exception
     * 
     */
    public static InfoPackage insertChildCodeDependCell(DataSet dsPost,
            String aDetailCodeValue, String bmk, String aFieldName,
            String aFieldValue, SysCodeRule aCodeRule) throws Exception {
        InfoPackage info = new InfoPackage();
        info.setSuccess(true);
        dsPost.maskDataChange(true);
        dsPost.gotoBookmark(bmk);
        String sLvlID = dsPost.fieldByName(IPrjDetail.LVL_ID).getString();
        String sParentID = dsPost.fieldByName(aFieldName).getString();
        String sFName = dsPost.fieldByName(IPrjDetail.FIELD_FNAME).getString();
        int iChildCount = getChildNum(dsPost, aFieldValue);
        if (iChildCount < 1) { // 如果该节点没有子节点
            int iFieldLength = aCodeRule.nextLevelLength(aFieldValue);
            if (iFieldLength <= 0) {
                info.setSuccess(false);
                info.setsMessage("根据编码规则生成编码为空,不允许添加下级节点");
                return info;
            }
            iFieldLength = iFieldLength - aFieldValue.length(); // 本级编码长度
            int iLvlLength = UntPub.lvlRule.nextLevelLength(sLvlID);
            iLvlLength = iLvlLength - sLvlID.length();
            dsPost.append();
            dsPost.fieldByName(aFieldName).setValue(
                    aFieldValue
                            + Common.getStrID(new BigDecimal(1), iFieldLength));
            dsPost.fieldByName(IPrjDetail.LVL_ID).setValue(
                    sLvlID + Common.getStrID(new BigDecimal(1), iLvlLength));
            setInitDataOnInsertChild(dsPost, sFName, sParentID,
                    aDetailCodeValue);
        } else { // 如果该节点有子节点
            dsPost.append();
            String bmkMaxChild = getChildInfo(dsPost, aFieldName, aFieldValue);
            String sMaxChildFieldID = getValue(dsPost, bmkMaxChild, aFieldName);
            String sMaxChildLvlID = getValue(dsPost, bmkMaxChild,
                    IPrjDetail.LVL_ID);
            dsPost.fieldByName(aFieldName)
                    .setValue(
                    		Common.getStrID(new BigDecimal(sMaxChildFieldID)
                                    .add(new BigDecimal(1)), sMaxChildFieldID
                                    .length()));
            dsPost.fieldByName(IPrjDetail.LVL_ID).setValue(
            		Common.getStrID(new BigDecimal(sMaxChildLvlID)
                            .add(new BigDecimal(1)), sMaxChildLvlID.length()));
            setInitDataOnInsertChild(dsPost, sFName, sParentID,
                    aDetailCodeValue);

            // dsPost.fieldByName(IPrjDetail.FIELD_FNAME).setValue(sFName +
            // ".待定");
            // dsPost.fieldByName(IPrjDetail.FIELD_CNAME).setValue("待定");
            // dsPost.fieldByName(IPrjDetail.SET_YEAR).setValue(Global.loginYear);
            // dsPost.fieldByName(IPrjDetail.DETAIL_CODE).setValue(
            // aDetailCodeValue);
            // dsPost.fieldByName( "PARID" ).setValue( sParentID );
            // dsPost.fieldByName( IPrjDetail.STD_TYPE ).setValue( "0" );
            // dsPost.fieldByName( IPrjDetail.PRIMARY_INDEX ).setValue( "0" );
        }
        return info;
    }

    private static void setInitDataOnInsertChild(DataSet dsPost, String sFName,
            String sParentID, String aDetailCodeValue) throws Exception {
        dsPost.fieldByName(IPrjDetail.FIELD_FNAME).setValue(sFName + ".待定");
        dsPost.fieldByName(IPrjDetail.FIELD_CNAME).setValue("待定");
        dsPost.fieldByName(IPrjDetail.SET_YEAR).setValue(Global.loginYear);
        dsPost.fieldByName(IPrjDetail.DETAIL_CODE).setValue(aDetailCodeValue);
        dsPost.fieldByName("PARID").setValue(sParentID);
        dsPost.fieldByName(IPrjDetail.STD_TYPE).setValue("0");
        dsPost.fieldByName(IPrjDetail.PRIMARY_INDEX).setValue("0");
        String sFieldEName = PrjDetailAction.getFieldEName(dsPost, "F", dsPost
                .toogleBookmark());
        dsPost.fieldByName(IPrjDetail.FIELD_ENAME).setValue(sFieldEName);
        dsPost.fieldByName(IPrjDetail.FIELD_INDEX).setValue("0");
        dsPost.fieldByName(IPrjDetail.FIELD_COLUMN_WIDTH).setValue("72");

        dsPost.fieldByName(IPrjDetail.DATA_TYPE).setValue("浮点型");
        dsPost.fieldByName(IPrjDetail.CALL_PRI).setValue("0");
        dsPost.fieldByName(IPrjDetail.STD_TYPE).setValue("0");
        dsPost.fieldByName(IPrjDetail.FIELD_INDEX).setValue("0");
        dsPost.fieldByName(IPrjDetail.PRIMARY_INDEX).setValue("0");

        dsPost.fieldByName(IPrjDetail.FIELD_COLUMN_WIDTH).setValue("72");
        dsPost.fieldByName(IPrjDetail.FIELD_KIND).setValue("录入");
        dsPost.fieldByName(IPrjDetail.IS_SUMCOL).setValue("0");
        dsPost.fieldByName(IPrjDetail.IS_HIDECOL).setValue("0");
        dsPost.fieldByName(IPrjDetail.NOTNULL).setValue("0");
    }

    /**
     * 取出在同一个父亲节点下的某条数据以后的数据，该数据满足条件：某个字段值比给定的字段值大
     * 
     * @param dsPost
     * @param aFieldName
     * @param aFieldValue
     * @param aParIDName
     * @return
     * @throws Exception
     */
    public static String[] getAfterBK(DataSet dsPost, String aFieldName,
            String aFieldValue, String aParIDName) throws Exception {
        String bmkBefore = dsPost.toogleBookmark();
        dsPost.maskDataChange(true);
        String[] bmkAfter = new String[dsPost.getRecordCount()];
        int k = 0;
        // 取出该字段的父亲节点的值
        String sParentID = null;
        dsPost.locate(aFieldName, aFieldValue);
        sParentID = dsPost.fieldByName(aParIDName).getString();
        dsPost.beforeFirst();
        // 取出所有在该节点的同一父节点下 在这个节点后面的所有节点的bookmark
        while (dsPost.next()) {
            if (Common.isEqual(sParentID, dsPost.fieldByName(aParIDName)
                    .getString())) {
                String sFieldID = dsPost.fieldByName(aFieldName).getString();
                if (Integer.parseInt(sFieldID) > Integer.parseInt(aFieldValue)) {
                    bmkAfter[k] = dsPost.toogleBookmark();
                    k++;
                }
            }
        }
        dsPost.gotoBookmark(bmkBefore);
        dsPost.maskDataChange(false);
        return bmkAfter;
    }

    /**
     * 检查是否则项目中已经使用
     * 
     * @param aType
     * @param sCodeValue
     * @return
     * @throws Exception
     */
    public static boolean checkUseInPrj(String aType, String sCodeValue)
            throws Exception {
        String sFilter = aType + "='" + sCodeValue + "'";
        DataSet ds = PrjDetailStub.getMethod().getColInfoAccordDetailCode(
                "fb_p_base", sFilter);
        if (!ds.isEmpty())
            return false;
        return true;
    }

    /**
     * 根据编码规则生成fieldID()
     */
    public String createFieldID(DataSet dsPost, Node nodePost,
            SysCodeRule aCodeRule, String aFieldName) throws Exception {
        // 获取本级编码长度
        DataSet ds = (DataSet) dsPost.clone();
        int iCodeLength = 0; // 该节点生成的field_id 应该的长度
        iCodeLength = getCodeLengthAppendNodeParentCount(nodePost, aCodeRule);
        // 生成fieldid
        String sFieldID = null;
        String sParentFieldID = null;
        Node nodeParent = nodePost.getParent();
        if (nodeParent.getIdentifier() != null) {
            String sNodeParentID = nodeParent.getIdentifier().toString();
            ds.locate("nodeID", sNodeParentID);
            sParentFieldID = ds.fieldByName(aFieldName).getString();
            String sCode = Common.getStrID(new BigDecimal(
                    getFIeldIDAppendParent(ds, sParentFieldID, iCodeLength,
                            aFieldName)), iCodeLength);
            sFieldID = sParentFieldID
                    + Common.getStrID(new BigDecimal(sCode), iCodeLength);
        } else {
            sFieldID = Common.getStrID(new BigDecimal(getFIeldIDAppendParent(
                    ds, null, iCodeLength, aFieldName)), iCodeLength);
        }
        return sFieldID;
    }

    /**
     * 由父节点id生成本级fieldid
     * 
     * @param fieldid
     * @param dsPost
     * @param aParentID
     * @param aCodeLength
     *            本级编码小长度
     * @return 结果
     * @throws Exception
     */
    public int getFIeldIDAppendParent(DataSet dsPost, String aParentID,
            int aCodeLength, String aFieldName) throws Exception {
        DataSet ds = (DataSet) dsPost.clone();
        ds.beforeFirst();
        int iCode = 1;
        if (Common.isNullStr(aParentID)) {
            while (ds.next()) {
                String sFieldID = ds.fieldByName(aFieldName).getString();
                int iFieldID = (Common.isNullStr(sFieldID)) ? 0 : Integer
                        .parseInt(sFieldID);
                if (sFieldID.length() == aCodeLength)
                    iCode = (iFieldID > iCode) ? iFieldID + 1 : iCode + 1;
            }
        } else {
            while (ds.next()) {
                String sFieldID = ds.fieldByName(aFieldName).getString();
                int iParentLength = aParentID.length(); // 父节点的长度
                if (sFieldID.length() == (aParentID.length() + aCodeLength)) {
                    int iLastCode = Integer.parseInt(sFieldID.substring(
                            iParentLength, iParentLength + aCodeLength));
                    iCode = (iCode > iLastCode) ? iCode + 1 : iLastCode + 1;
                }
            }
        }
        return iCode;
    }

    /**
     * 根据node的父亲节点数获得本级编码小长度
     */
    public int getCodeLengthAppendNodeParentCount(String code,
            SysCodeRule aCodeRule) {
        return aCodeRule.nextLevelLength(code);
    }

    /**
     * 根据父亲节点的个数获得本级编码的长度
     * 
     * @param nodePost
     * @param aCodeRule
     * @return
     */
    public int getCodeLengthAppendNodeParentCount(Node nodePost,
            SysCodeRule aCodeRule) {
        int iCodeLength = 0;
        int i = 0;
        i = getNodeParentCount(nodePost);
        if (i == 0) { // 该节点没有父亲节点
            int iPlace = aCodeRule.originRuleStr().indexOf("|"); // 第一个“|”出现的位置
            iCodeLength = Integer.parseInt(aCodeRule.originRuleStr().substring(
                    0, iPlace)); //
        } else { // 节点有i个父亲节点
            int iBeginPlace = aCodeRule.originRuleStr().indexOf("|", i);
            int j = i + 1;
            int iEndPlace = aCodeRule.originRuleStr().indexOf("|", j + 1);
            if (iEndPlace == -1)
                iEndPlace = aCodeRule.originRuleStr().length();
            iCodeLength = Integer.parseInt(aCodeRule.originRuleStr().substring(
                    iBeginPlace + 1, iEndPlace));
            int iCodeLengthBefore = Integer.parseInt(aCodeRule.originRuleStr()
                    .substring(iBeginPlace - 1, iBeginPlace)); // 它父亲节点的编码长度
            iCodeLength = iCodeLength - iCodeLengthBefore;
        }
        return iCodeLength;
    }

    /**
     * 根据节点获取父亲节点的FNAME
     * 
     * @param ds
     * @param aNode
     * @return 结果
     * @throws Exception
     */
    public String getParentFName(DataSet dsPost, Node aNode) throws Exception {
        DataSet ds = (DataSet) dsPost.clone();
        String sParentFName = null;
        if (aNode.getParent().getIdentifier() == null) // 如果父亲节点为空
            return sParentFName;
        else {
            ds.locate("nodeID", aNode.getParent().getIdentifier().toString());
            sParentFName = ds.fieldByName(IPrjDetail.FIELD_FNAME).getString();
            return sParentFName;
        }
    }

    /**
     * 获取一个节点的父亲节点的个数（即它父亲节点有一个父亲节点，而该节点的父亲父亲的节点没有父亲节点，则返回2）
     * 
     * @param nodePost
     */
    public int getNodeParentCount(Node nodePost) {
        int iParentNum = 0;
        Node nodeParent = nodePost.getParent();
        for (int i = 0;; i++) {
            if (nodeParent.getIdentifier() == null)
                return iParentNum;
            else {
                nodeParent = nodeParent.getParent();
                iParentNum++;
            }
        }
    }

    /**
     * 生成lvlID
     */
    public String createLvlID(Node nodePost, DataSet dsPost,
            SysCodeRule lvlRule, String aFieldName) throws Exception {
        String sLvlID = null;
        String sParentLvlID = null;
        int iLvlIDLength = 0;
        int iParentNum = getNodeParentCount(nodePost); // 获取父节点个数
        Object[] listLvlID = lvlRule.originRules().toArray(); // 编码规则的数组

        if (iParentNum > 0) { // 如果有父节点
            iLvlIDLength = Integer.parseInt(listLvlID[iParentNum].toString()) // 取出编码规则的本级小长度
                    - (Integer.parseInt(listLvlID[iParentNum - 1].toString()));
            DataSet ds = (DataSet) dsPost.clone();
            ds.locate("nodeID", nodePost.getParent().getIdentifier());
            sParentLvlID = ds.fieldByName(aFieldName).getString();
            sLvlID = Common.getStrID(new BigDecimal(getFIeldIDAppendParent(
                    dsPost, sParentLvlID, iLvlIDLength, aFieldName)),
                    iLvlIDLength);
            return (sParentLvlID + sLvlID);
        } else { // 如果没有父节点
            if (dsPost.isEmpty()) // 如果
                return null;
            else {
                iLvlIDLength = Integer.parseInt(listLvlID[0].toString());
                DataSet ds = (DataSet) dsPost.clone();
                sLvlID = Common.getStrID(new BigDecimal(getFIeldIDAppendParent(
                        ds, sParentLvlID, iLvlIDLength, aFieldName)),
                        iLvlIDLength);
                return sLvlID;
            }
        }
    }

    /**
     * 删除后节点定位
     * 
     * @param dsPost
     *            数据集
     * @param aFieldIDName
     *            定位的字段名
     * @param aParentIDName
     *            父节点的字段名
     * @param aParentIDValue
     *            父节点的字段值
     * @param treePost
     *            要定位的树
     * @param aNextID
     *            删除节点的后个节点
     * @throws Exception
     */
    public static boolean gotoParentNode(DataSet dsPost, String aFieldIDName,
            String aNextID, String aParentIDValue, String aParentIDName,
            CustomTree treePost) throws Exception {
        boolean info = true;
        dsPost.maskDataChange(true);
        if (dsPost.locate(aFieldIDName, aNextID) == false)
            if (dsPost.locate(aFieldIDName, aParentIDValue) == false) {
                TreePath path = new TreePath(((DefaultTreeModel) treePost
                        .getModel()).getPathToRoot(treePost.getRoot()));
                treePost.expandPath(path);
                return false;
            }
        dsPost.maskDataChange(false);
        return info;
    }

    /**
     * 检查dataset中某一个字段是否已经有重复记录
     * 
     * @param dsPost
     * @param fieldname
     * @param fieldvalue
     * @return
     */
    public static InfoPackage checkIsRep(DataSet dsPost, String fieldname,
            String fieldvalue) {
        InfoPackage info = new InfoPackage();
        try {
            info.setSuccess(true);
            String[] names = new String[dsPost.getRecordCount()];
            String bmkNow = dsPost.toogleBookmark();
            dsPost.maskDataChange(true);
            int i = 0;
            dsPost.beforeFirst();
            while (dsPost.next()) {
                names[i] = dsPost.fieldByName(fieldname).getString();
                i++;
            }
            Arrays.sort(names);
            int idex = Arrays.binarySearch(names, fieldvalue);
            if (idex >= 0) {
                info.setSuccess(false);
                info.setsMessage(fieldvalue + "已经存在");
                return info;
            }
            dsPost.gotoBookmark(bmkNow);
            dsPost.maskDataChange(false);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return info;
    }

    /**
     * 检查数据集中的某个字段是否为空，如果为空则返回false
     * 
     * @param ds
     * @param aField
     * @return
     * @throws Exception
     */
    public static boolean checkIsNotNull(DataSet ds, String aField)
            throws Exception {
        String bmk = ds.toogleBookmark();
        ds.maskDataChange(true);
        ds.beforeFirst();
        while (ds.next()) {
            String sName = ds.fieldByName(aField).getString().trim();
            if (Common.isNullStr(sName))
                return false;
        }
        ds.gotoBookmark(bmk);
        ds.maskDataChange(false);
        return true;
    }

    /**
     * 修改名称的时候要修改子节点的全称
     * 
     * @param dsCol
     * @param dsPost
     * @param aFNewFName
     * @param aModRecs_ID
     * @param aCodeRule
     * @return InfoPackage
     * @throws Exception
     */
    public static InfoPackage modifyChildFullName(DataSet dsCol,
            DataSet dsPost, String aFNewFName, String[] aModRecs_ID,
            SysCodeRule aCodeRule) throws Exception {
        InfoPackage info = new InfoPackage();
        info.setSuccess(true);
        dsCol.maskDataChange(true);
        String bmk = dsCol.toogleBookmark();
        // 定义几个变量
        String sModNewRecs_ID[] = null; // 修改后要修改的子节点的ID数组
        String sChildParFName = null;
        DataSet dsChild = null; // 修改的子节点的记录集
        int iModRecCount = aModRecs_ID.length;
        for (int i = 0; i < iModRecCount; i++) {
            if (Common.isNullStr(aModRecs_ID[i]))
                continue;
            int iNextLength = aCodeRule.nextLevelLength(aModRecs_ID[i]);
            String sDetailCode = dsPost.fieldByName(IPrjDetail.DETAIL_CODE)
                    .getString();
            String sFilter = IPrjDetail.SET_YEAR + "=" + Global.loginYear
                    + " and " + IPrjDetail.FIELD_ID + " like '"
                    + aModRecs_ID[i] + "%' and " + IPrjDetail.FIELD_ID + "!='"
                    + aModRecs_ID[i] + "'" + " and " + IPrjDetail.DETAIL_CODE
                    + "='" + sDetailCode + "' and length("
                    + IPrjDetail.FIELD_ID + ")=" + iNextLength + " order by "
                    + IPrjDetail.FIELD_ID;
            int iChildCount = PrjDetailStub.getMethod().getRecNum(
                    IPrjDetail.Table_SIMP_COLINFO, sFilter);
            if (iChildCount == 0)
                return info;
            dsChild = PrjDetailStub.getMethod().getColInfoAccordDetailCode(
                    IPrjDetail.Table_SIMP_COLINFO, sFilter);
            dsChild.edit();
            sModNewRecs_ID = new String[dsChild.getRecordCount()];
            int iNowID = 0;
            dsChild.beforeFirst();
            while (dsChild.next()) {
                String sChildID = dsChild.fieldByName(IPrjDetail.FIELD_ID)
                        .getString();
                sModNewRecs_ID[iNowID] = sChildID;
                iNowID++;
                String sCName = dsChild.fieldByName(IPrjDetail.FIELD_CNAME)
                        .getString();
                sChildParFName = aFNewFName + "." + sCName;
                String sOldChildParFName = dsChild.fieldByName(
                        IPrjDetail.FIELD_FNAME).getString();
                if (Common.isEqual(sOldChildParFName, sChildParFName))
                    continue;
                dsCol.locate(IPrjDetail.FIELD_ID, sChildID);
                dsCol.edit();
                dsCol.fieldByName(IPrjDetail.FIELD_FNAME).setValue(
                        sChildParFName);
            }
        }
        dsChild.beforeFirst();
        dsChild.next();
        dsCol.gotoBookmark(bmk);
        dsCol.maskDataChange(false);
        modifyChildFullName(dsCol, dsChild, sChildParFName, sModNewRecs_ID,
                aCodeRule);
        return info;
    }

    /**
     * 当插入记录时要修改插入记录以后的所有的记录的相关编码(使加1)
     * 
     * @param dsPost
     *            数据集
     * @param bmk
     *            插入那条记录的bmk
     * @param aModifyFieldName
     *            要修改的字段值的数组
     * @param aParIDName
     *            父亲节点的字段名
     * @param aParIDValue
     *            父亲节点的字段值
     * @throws Exception
     */
    public static InfoPackage modifyByInsert(DataSet dsPost, String bmk,
            String[] aModifyFieldName, String aParIDName, String aParIDValue) {
        InfoPackage info = new InfoPackage();
        info.setSuccess(true);
        String sMes = "修改插入后记录的值失败";
        try {
            dsPost.gotoBookmark(bmk);
            if (dsPost.eof() || dsPost.bof()) {
                info.setSuccess(false);
                info.setsMessage(sMes);
                return info;
            }
            int imodifyFieldNum = aModifyFieldName.length;
            dsPost.maskDataChange(true);
            dsPost.edit();
            while (dsPost.next()) {
                String sParID = dsPost.fieldByName(aParIDName).getString();
                if (!Common.isEqual(aParIDValue, sParID))
                    continue;
                for (int i = 0; i < imodifyFieldNum; i++) {
                    String sCode = dsPost.fieldByName(aModifyFieldName[i])
                            .getString();
                    int iCodeLength = sCode.length();
                    int iCode = Integer.parseInt(sCode) + 1;
                    String sNewCode = Common.getStrID(new BigDecimal(iCode),
                            iCodeLength);
                    dsPost.fieldByName(aModifyFieldName[i]).setValue(sNewCode);
                }
            }
            dsPost.gotoBookmark(bmk);
            dsPost.maskDataChange(false);
            return info;
        } catch (Exception ee) {
            info.setSuccess(false);
            info.setsMessage(sMes);
            return info;
        }
    }

    /**
     * 获取数据集里某个字段的所有记录
     * 
     * @param dsPost
     * @param aParName
     * @return
     * @throws Exception
     */
    public String[] getParents(DataSet dsPost, String aFieldName)
            throws Exception {
        String[] values = new String[dsPost.getRecordCount()];
        int rec = 0;
        dsPost.maskDataChange(true);
        String bmk = dsPost.toogleBookmark();
        dsPost.beforeFirst();
        while (dsPost.next()) {
            values[rec] = dsPost.fieldByName(aFieldName).getString();

            rec++;
        }
        dsPost.gotoBookmark(bmk);
        dsPost.maskDataChange(false);
        return values;
    }
}
