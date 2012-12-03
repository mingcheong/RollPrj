/**
 * 
 * titile : ��Ŀ��ϸ�����ݲ���
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
     * Ϊ����Ƿ��� ��Ŀ�����Ƿ�ʹ�ö�����
     */
    public final static String check_PrjAttr = "PRJATTR_CODE";

    public final static String check_PrjAccord = "PRJACCORD_CODE";

    public final static String check_PrjStd = "PRJSTD_CODE";

    public final static String check_PrjSort = "PRJSORT_CODE";

    public void Action() {
    }

    /**
     * ���캯��
     * 
     */
    public PrjDetailAction() {
    };

    /**
     * ���ݱ����������parentID
     * 
     * @param dsPost
     * @param aFieldName
     *            �ֶ���
     * @param acodeRule
     *            �������
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
     * �����ֶ�ֵ����������ȡ���׽ڵ��ֵ
     * 
     * @param aFieldValue
     *            �ֶ�ֵ
     * @param acodeRule
     *            �������
     * @throws Exception
     */
    public static String getParentID(String aFieldValue, SysCodeRule aCodeRule)
            throws Exception {
        return aCodeRule.previous(aFieldValue);
    }

    /**
     * �޸�fieldIDʱ,������ӽڵ����޸��ӽڵ���Ϣ ͬʱ�޸������ӽڵ��FName
     * 
     * @param dsPost
     * @param aFieldOldValue
     *            ԭ����ֵ
     * @param aFieldNewValue
     *            �ı���ֵ
     * @param aFieldName
     *            ԭ����LvlIDֵ
     * @return dsPost
     */
    public static void modifyFieldIDWithChild(DataSet dsPost,
            String aFieldOldValue, String aFieldNewValue, String aFieldName)
            throws Exception {
        String bmk = dsPost.toogleBookmark();
        dsPost.maskDataChange(true);
        // �޸ı����ڵ�
        dsPost.edit();
        dsPost.locate(aFieldName, aFieldOldValue);
        dsPost.fieldByName(aFieldName).setValue(aFieldNewValue);
        dsPost.beforeFirst();
        InfoPackage info = new InfoPackage();
        info.setSuccess(true);
        // �ж��Ƿ����ӽڵ�,��������޸������ӽڵ�
        while (dsPost.next()) {
            String sParentValue = dsPost.fieldByName(IPrjDetail.PAR_ID)
                    .getString();
            if (aFieldOldValue.equals(sParentValue)) {
                String sFieldValue = dsPost.fieldByName(aFieldName).getString(); // �ֶ�ֵ
                String sFieldAfter = sFieldValue.substring(sParentValue
                        .length() - 1); // �ֶα�������С����
                String sFieldNewValue = (!Common.isNullStr(sFieldAfter)) ? aFieldNewValue
                        + sFieldAfter
                        : aFieldNewValue; // �����ɵ��ֶ�ֵ
                dsPost.fieldByName(aFieldName).setValue(sFieldNewValue);
            }
        }
        // �������
        dsPost.gotoBookmark(bmk);
        dsPost.maskDataChange(false);
    }

    /**
     * �޸�lvlidʱ,������ӽڵ����޸��ӽڵ���Ϣ
     * 
     * @param dsPost
     * @param aFieldName
     * @param aFieldOldValue
     *            ԭ����ֵ
     * @param aFieldNewValue
     *            �ı���ֵ
     * @param aFieldName
     *            ԭ����LvlIDֵ
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
        // �ж��Ƿ����ӽڵ�,��������޸�����
        while (dsPost.next()) {
            String sParentValue = aCodeRule.previous(dsPost.fieldByName(
                    aFieldName).getString());
            if (aFieldOldValue.equals(sParentValue)) {
                String sFieldValue = dsPost.fieldByName(aFieldName).getString(); // �ֶ�ֵ
                String sFieldAfter = sFieldValue.substring(sParentValue
                        .length()); // �ֶα�������С����
                String sFieldNewValue = (!Common.isNullStr(sFieldAfter)) ? aFieldNewValue
                        + sFieldAfter
                        : aFieldNewValue; // �����ɵ��ֶ�ֵ
                dsPost.fieldByName(aFieldName).setValue(sFieldNewValue);
            }
        }
        // �������
        dsPost.gotoBookmark(bmk);
        dsPost.maskDataChange(false);
    }

    /**
     * ��ȡename
     * 
     * @param aType
     *            ����
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
     * ɾ�����������ʱ��������ӽӵ����޸��ӽڵ����Ϣ(FieldID,LvlID,FName)
     * 
     * @param ds
     * @param aFieldName
     * @param aDelFieldValue
     *            ɾ�����ֶ�field_id
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
                    .getString())) { // ��������ӽڵ���������²���
                String sFieldOldValue = dsPost.fieldByName(IPrjDetail.FIELD_ID)
                        .getString(); // ԭ����Field_ID ֵ
                String sLvlOldValue = dsPost.fieldByName(IPrjDetail.LVL_ID)
                        .getString(); // ԭ����Lvl_ID��ֵ
                String sCName = dsPost.fieldByName(IPrjDetail.FIELD_CNAME)
                        .getString();
                String sFieldSelfValue = sFieldOldValue
                        .substring(aDelFieldValue.length() - 1); // Field_ID�������볤��
                String sFieldNewValue = (Common.isNullStr(sDelParentID)) ? sFieldSelfValue
                        : sDelParentID + sFieldSelfValue; // �����ɵ�sFieldNewValue
                String sLvlSelfValue = sLvlOldValue.substring(sDelLvlID
                        .length() - 1); // lvl_id ��������ĳ���
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
     * ��ȡ�ýڵ��ӽڵ�����������¼ֵ��bmk
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
     * ����bookmark,�ֶ�����ȡ�ֶ�ֵ
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
     * ��ȡ�ֶθ��׽ڵ��fname��ֵ
     * 
     * @param dsPost
     * @param aFieldName
     *            ��λ�ֶ�
     * @param aFieldValue
     *            ��λ�ֶε�ֵ
     * @return ���
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
     * ��ȡ�ӽڵ����
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
     * �ڽڵ�֮�����һ�ڵ㣬����Ľڵ�˳���һ������������ӽڵ���ı��ӽڵ� ����ͬ���ڵ�
     * 
     * @param ds
     * @param bmk
     *            ����ı��������¼��bookmark
     * @param aFieldName
     *            �ֶ���
     * @param aFieldValue
     *            �ֶ�ֵ
     * @param aSysCodeRule
     *            �������
     * @throws Exception
     * 
     */
    public static String insertCodeDependCell(DataSet dsPost,
            String aDetailCodeValue, String bmk, String aFieldName,
            String aFieldValue, SysCodeRule aSysCodeRule) throws Exception {
        String bmkInsert = null;
        dsPost.maskDataChange(true);
        // ��ȡǰ��Ҫ׼����ֵ
        String sLvlID = dsPost.fieldByName(IPrjDetail.LVL_ID).getString(); // �����
        String sParID = dsPost.fieldByName(IPrjDetail.PAR_ID).getString(); // �����ڵ�
        String sParFName = getParFName(dsPost, aFieldName, aFieldValue);
        String sCodeNow = null;
        String sLvlNow = null;
        dsPost.edit();
        dsPost.beforeFirst();
        while (dsPost.next()) {
            sCodeNow = dsPost.fieldByName(aFieldName).getString();
            sLvlNow = dsPost.fieldByName(IPrjDetail.LVL_ID).getString();
            // ������ڵı��볤�ȱ�Ҫ�ı��С���򲻸ı�
            sCodeNow = modCode(aFieldValue, sCodeNow);
            // �޸����ڵı���
            sLvlNow = modCode(sLvlID, sLvlNow);
            dsPost.fieldByName(aFieldName).setValue(sCodeNow);
            dsPost.fieldByName(IPrjDetail.LVL_ID).setValue(sLvlNow);
            dsPost.fieldByName(IPrjDetail.PAR_ID).setValue(sParID);
        }
        dsPost.gotoBookmark(bmkInsert);
        dsPost.append();
        bmkInsert = dsPost.toogleBookmark();
        sParFName = Common.isNullStr(sParFName) ? "����" : sParFName + ".����";
        setColData(dsPost, aFieldName, aFieldValue, sParFName, sLvlID,
                aDetailCodeValue);
        dsPost.gotoBookmark(bmk);
        dsPost.maskDataChange(false);
        return bmkInsert;
    }

    /**
     * �޸ı���
     * 
     * @param codeDep
     *            ���ݵı���
     * @param codeNow
     *            Ҫ�޸ĵı���
     * @return
     */
    private static String modCode(String codeDep, String codeNow) {
        if (codeNow.length() < codeDep.length())
            return codeNow;
        String codeReturn = null;
        int iDepLen = codeDep.length();
        String codeFor = null; // ǰ�벿��
        String codeBeh = null; // ��벿��
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
     * �����ͬ���ڵ��ʱ��Ҫ����Ŀհ�ֵ
     * 
     * @param dsPost
     *            Ҫ���������ݼ�
     * @param aFieldName
     *            �����ֶ���
     * @param aFieldValue
     *            �����ֶ�ֵ
     * @param sParFName
     *            ���ڵ�����
     * @param sLvlID
     *            �����
     * @param aDetailCodeValue
     *            ��ϸ���
     * @throws Exception
     */
    private static void setColData(DataSet dsPost, String aFieldName,
            String aFieldValue, String sParFName, String sLvlID,
            String aDetailCodeValue) throws Exception {
        dsPost.fieldByName(aFieldName).setValue(aFieldValue);
        dsPost.fieldByName(IPrjDetail.LVL_ID).setValue(sLvlID);
        dsPost.fieldByName(IPrjDetail.FIELD_FNAME).setValue(sParFName);
        dsPost.fieldByName(IPrjDetail.FIELD_CNAME).setValue("����");
        dsPost.fieldByName(IPrjDetail.SET_YEAR).setValue(Global.loginYear);
        dsPost.fieldByName(IPrjDetail.DETAIL_CODE).setValue(aDetailCodeValue);
        String bmkNow = dsPost.toogleBookmark();
        String sEName = getFieldEName(dsPost, "F", bmkNow);
        dsPost.fieldByName(IPrjDetail.FIELD_ENAME).setValue(sEName);
        dsPost.fieldByName(IPrjDetail.STD_TYPE).setValue("0");
        dsPost.fieldByName(IPrjDetail.FIELD_INDEX).setValue("0");
    }

    /**
     * �ڽڵ��²���һ�ӽڵ㣬����ӽڵ㲻Ϊ�գ�������ӽڵ�����һλ,���Ϊ����ֱ����������� �����Ӽ��ڵ�
     * 
     * @param ds
     * @param bmk
     *            ����ı��������¼��bookmark
     * @param aFieldName
     *            �ֶ���
     * @param aFieldValue
     *            �ֶ�ֵ
     * @param aSysCodeRule
     *            �������
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
        if (iChildCount < 1) { // ����ýڵ�û���ӽڵ�
            int iFieldLength = aCodeRule.nextLevelLength(aFieldValue);
            if (iFieldLength <= 0) {
                info.setSuccess(false);
                info.setsMessage("���ݱ���������ɱ���Ϊ��,����������¼��ڵ�");
                return info;
            }
            iFieldLength = iFieldLength - aFieldValue.length(); // �������볤��
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
        } else { // ����ýڵ����ӽڵ�
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
            // ".����");
            // dsPost.fieldByName(IPrjDetail.FIELD_CNAME).setValue("����");
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
        dsPost.fieldByName(IPrjDetail.FIELD_FNAME).setValue(sFName + ".����");
        dsPost.fieldByName(IPrjDetail.FIELD_CNAME).setValue("����");
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

        dsPost.fieldByName(IPrjDetail.DATA_TYPE).setValue("������");
        dsPost.fieldByName(IPrjDetail.CALL_PRI).setValue("0");
        dsPost.fieldByName(IPrjDetail.STD_TYPE).setValue("0");
        dsPost.fieldByName(IPrjDetail.FIELD_INDEX).setValue("0");
        dsPost.fieldByName(IPrjDetail.PRIMARY_INDEX).setValue("0");

        dsPost.fieldByName(IPrjDetail.FIELD_COLUMN_WIDTH).setValue("72");
        dsPost.fieldByName(IPrjDetail.FIELD_KIND).setValue("¼��");
        dsPost.fieldByName(IPrjDetail.IS_SUMCOL).setValue("0");
        dsPost.fieldByName(IPrjDetail.IS_HIDECOL).setValue("0");
        dsPost.fieldByName(IPrjDetail.NOTNULL).setValue("0");
    }

    /**
     * ȡ����ͬһ�����׽ڵ��µ�ĳ�������Ժ�����ݣ�����������������ĳ���ֶ�ֵ�ȸ������ֶ�ֵ��
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
        // ȡ�����ֶεĸ��׽ڵ��ֵ
        String sParentID = null;
        dsPost.locate(aFieldName, aFieldValue);
        sParentID = dsPost.fieldByName(aParIDName).getString();
        dsPost.beforeFirst();
        // ȡ�������ڸýڵ��ͬһ���ڵ��� ������ڵ��������нڵ��bookmark
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
     * ����Ƿ�����Ŀ���Ѿ�ʹ��
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
     * ���ݱ����������fieldID()
     */
    public String createFieldID(DataSet dsPost, Node nodePost,
            SysCodeRule aCodeRule, String aFieldName) throws Exception {
        // ��ȡ�������볤��
        DataSet ds = (DataSet) dsPost.clone();
        int iCodeLength = 0; // �ýڵ����ɵ�field_id Ӧ�õĳ���
        iCodeLength = getCodeLengthAppendNodeParentCount(nodePost, aCodeRule);
        // ����fieldid
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
     * �ɸ��ڵ�id���ɱ���fieldid
     * 
     * @param fieldid
     * @param dsPost
     * @param aParentID
     * @param aCodeLength
     *            ��������С����
     * @return ���
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
                int iParentLength = aParentID.length(); // ���ڵ�ĳ���
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
     * ����node�ĸ��׽ڵ�����ñ�������С����
     */
    public int getCodeLengthAppendNodeParentCount(String code,
            SysCodeRule aCodeRule) {
        return aCodeRule.nextLevelLength(code);
    }

    /**
     * ���ݸ��׽ڵ�ĸ�����ñ�������ĳ���
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
        if (i == 0) { // �ýڵ�û�и��׽ڵ�
            int iPlace = aCodeRule.originRuleStr().indexOf("|"); // ��һ����|�����ֵ�λ��
            iCodeLength = Integer.parseInt(aCodeRule.originRuleStr().substring(
                    0, iPlace)); //
        } else { // �ڵ���i�����׽ڵ�
            int iBeginPlace = aCodeRule.originRuleStr().indexOf("|", i);
            int j = i + 1;
            int iEndPlace = aCodeRule.originRuleStr().indexOf("|", j + 1);
            if (iEndPlace == -1)
                iEndPlace = aCodeRule.originRuleStr().length();
            iCodeLength = Integer.parseInt(aCodeRule.originRuleStr().substring(
                    iBeginPlace + 1, iEndPlace));
            int iCodeLengthBefore = Integer.parseInt(aCodeRule.originRuleStr()
                    .substring(iBeginPlace - 1, iBeginPlace)); // �����׽ڵ�ı��볤��
            iCodeLength = iCodeLength - iCodeLengthBefore;
        }
        return iCodeLength;
    }

    /**
     * ���ݽڵ��ȡ���׽ڵ��FNAME
     * 
     * @param ds
     * @param aNode
     * @return ���
     * @throws Exception
     */
    public String getParentFName(DataSet dsPost, Node aNode) throws Exception {
        DataSet ds = (DataSet) dsPost.clone();
        String sParentFName = null;
        if (aNode.getParent().getIdentifier() == null) // ������׽ڵ�Ϊ��
            return sParentFName;
        else {
            ds.locate("nodeID", aNode.getParent().getIdentifier().toString());
            sParentFName = ds.fieldByName(IPrjDetail.FIELD_FNAME).getString();
            return sParentFName;
        }
    }

    /**
     * ��ȡһ���ڵ�ĸ��׽ڵ�ĸ������������׽ڵ���һ�����׽ڵ㣬���ýڵ�ĸ��׸��׵Ľڵ�û�и��׽ڵ㣬�򷵻�2��
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
     * ����lvlID
     */
    public String createLvlID(Node nodePost, DataSet dsPost,
            SysCodeRule lvlRule, String aFieldName) throws Exception {
        String sLvlID = null;
        String sParentLvlID = null;
        int iLvlIDLength = 0;
        int iParentNum = getNodeParentCount(nodePost); // ��ȡ���ڵ����
        Object[] listLvlID = lvlRule.originRules().toArray(); // ������������

        if (iParentNum > 0) { // ����и��ڵ�
            iLvlIDLength = Integer.parseInt(listLvlID[iParentNum].toString()) // ȡ���������ı���С����
                    - (Integer.parseInt(listLvlID[iParentNum - 1].toString()));
            DataSet ds = (DataSet) dsPost.clone();
            ds.locate("nodeID", nodePost.getParent().getIdentifier());
            sParentLvlID = ds.fieldByName(aFieldName).getString();
            sLvlID = Common.getStrID(new BigDecimal(getFIeldIDAppendParent(
                    dsPost, sParentLvlID, iLvlIDLength, aFieldName)),
                    iLvlIDLength);
            return (sParentLvlID + sLvlID);
        } else { // ���û�и��ڵ�
            if (dsPost.isEmpty()) // ���
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
     * ɾ����ڵ㶨λ
     * 
     * @param dsPost
     *            ���ݼ�
     * @param aFieldIDName
     *            ��λ���ֶ���
     * @param aParentIDName
     *            ���ڵ���ֶ���
     * @param aParentIDValue
     *            ���ڵ���ֶ�ֵ
     * @param treePost
     *            Ҫ��λ����
     * @param aNextID
     *            ɾ���ڵ�ĺ���ڵ�
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
     * ���dataset��ĳһ���ֶ��Ƿ��Ѿ����ظ���¼
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
                info.setsMessage(fieldvalue + "�Ѿ�����");
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
     * ������ݼ��е�ĳ���ֶ��Ƿ�Ϊ�գ����Ϊ���򷵻�false
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
     * �޸����Ƶ�ʱ��Ҫ�޸��ӽڵ��ȫ��
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
        // ���弸������
        String sModNewRecs_ID[] = null; // �޸ĺ�Ҫ�޸ĵ��ӽڵ��ID����
        String sChildParFName = null;
        DataSet dsChild = null; // �޸ĵ��ӽڵ�ļ�¼��
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
     * �������¼ʱҪ�޸Ĳ����¼�Ժ�����еļ�¼����ر���(ʹ��1)
     * 
     * @param dsPost
     *            ���ݼ�
     * @param bmk
     *            ����������¼��bmk
     * @param aModifyFieldName
     *            Ҫ�޸ĵ��ֶ�ֵ������
     * @param aParIDName
     *            ���׽ڵ���ֶ���
     * @param aParIDValue
     *            ���׽ڵ���ֶ�ֵ
     * @throws Exception
     */
    public static InfoPackage modifyByInsert(DataSet dsPost, String bmk,
            String[] aModifyFieldName, String aParIDName, String aParIDValue) {
        InfoPackage info = new InfoPackage();
        info.setSuccess(true);
        String sMes = "�޸Ĳ�����¼��ֵʧ��";
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
     * ��ȡ���ݼ���ĳ���ֶε����м�¼
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
