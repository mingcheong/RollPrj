package gov.nbcs.rp.common.export.bs;

/**
 * ����
 * qzc
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.sql.BLOB;

import org.hibernate.Session;

import gov.nbcs.rp.common.Common;
import gov.nbcs.rp.common.DBSqlExec;
import gov.nbcs.rp.common.MyMap;
import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.datactrl.QueryStub;
import gov.nbcs.rp.common.export.ibs.IExport;
import com.foundercy.pf.util.Global;
import com.foundercy.pf.util.XMLData;
import com.foundercy.pf.util.dao.springdao.GeneralDAO;

public class ExportBO implements IExport {
	private GeneralDAO dao;

	private Session session;

	private Connection con;

	public GeneralDAO getDao() {
		return dao;
	}

	public void setDao(GeneralDAO dao) {
		this.dao = dao;
	}

	/**
	 * ��ȡ���ݼ�(���ֶ���)
	 * 
	 * @param aTableName
	 * @param aFilter
	 * @return
	 * @throws Exception
	 */
	public List getData(String sql) throws Exception {
		// Session session = dao.getSession();
		// Connection con = session.connection();
		/** ����� */
		// ResultSet rs;
		//
		// Statement stmt;
		List lstList = new ArrayList();

		try {
			lstList = dao.findBySql(sql);
			// if (session == null || !session.isConnected())
			// session = dao.getSession();
			// if (con == null || con.isClosed())
			// con = session.connection();
			// stmt = con.createStatement();
			// rs = stmt.executeQuery(sql);
			// int iColumnCount = rs.getMetaData().getColumnCount();
			// rs.beforeFirst();
			// while (rs.next()) {
			// ArrayList arrayList = new ArrayList();
			// for (int i = 0; i <= iColumnCount; i++) {
			// arrayList.add(rs.getString(i));
			// }
			// lstList.add(arrayList);
			// }
			// rs.close();
			// st.close();
		} finally {
			// dao.closeSession(session);
		}
		return lstList;
	}

	/**
	 * ��ȡ���ݼ������ֶ�����
	 * 
	 * @param aTableName
	 * @param Fields
	 * @param aFilter
	 * @return
	 * @throws Exception
	 */
	public List getData(String aTableName, List fields, String aFilter)
			throws Exception {
		String sFilter = (Common.isNullStr(aFilter) ? "" : " where " + aFilter);
		String sql = "select * from " + aTableName + sFilter;

		// Session session = dao.getSession();
		// Connection con = session.connection();
		/** ����� */
		ResultSet rs;

		Statement stmt;

		List lstList = new ArrayList();
		try {
			if ((session == null) || !session.isConnected()) {
				session = dao.getSession();
			}
			if ((con == null) || con.isClosed()) {
				con = session.connection();
			}
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			int iColumnCount = fields.size();
			rs.beforeFirst();
			while (rs.next()) {
				ArrayList arrayList = new ArrayList();
				for (int i = 0; i <= iColumnCount; i++) {
					arrayList.add(rs.getString((String) fields.get(i)));
				}
				lstList.add(arrayList);
			}
			rs.close();
			// st.close();
		} finally {
			// dao.closeSession(session);
		}
		return lstList;
	}

	/**
	 * ��ȡ����ļ�¼��
	 * 
	 * @param aTableName
	 * @param aFilter
	 * @return
	 * @throws Exception
	 */
	public int getRecNum(String aTableName, String aFilter) throws Exception {
		int rec = 0;
		String sFilter = (Common.isNullStr(aFilter) ? "" : " where " + aFilter);
		String sql = "select * from " + aTableName + sFilter;
		// Session session = dao.getSession();
		// Connection con = session.connection();
		/** ����� */
		ResultSet rs;
		Statement stmt;
		try {
			if ((session == null) || !session.isConnected()) {
				session = dao.getSession();
			}
			if ((con == null) || con.isClosed()) {
				con = session.connection();
			}
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sql);
			rs.beforeFirst();
			while (rs.next()) {
				rec++;
			}
			rs.close();
			// st.close();
		} finally {
			// dao.closeSession(session);
		}
		return rec;
	}

	/**
	 * ��ѯ������һ��List<Map>���У�����ÿ��Ԫ��<Map>���� �ֶ���:�ֶ�ֵ ��pair
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List executeQuery(String sql) throws Exception {
		// Statement stmt = null;
		// ResultSet rs = null;
		// Session session = null;
		// try {
		// if (session == null || !session.isConnected())
		// session = dao.getSession();
		// if (con == null || con.isClosed())
		// con = session.connection();
		// stmt = con.createStatement();
		// if (Common.RUNTIME_DEBUG) {
		// System.out.println(sql);
		// }
		// rs = stmt.executeQuery(sql);
		List listR = dao.findBySql(sql);
		// List listR = new ArrayList();
		// for (int i = 0; i < list.size(); i++) {
		// XMLData data = (XMLData) list.get(i);
		// listR.add(getMapFromXMLData(data));
		// }
		return listR;
		// return asList(rs);
		// } finally {
		// if (rs != null)
		// rs.close();
		// if (stmt != null)
		// stmt.close();
		// }
	}

	private Map getMapFromXMLData(XMLData data) throws Exception {
		Map map = new MyMap();
		Object[] sinValue = null;
		for (int i = 0; i < data.size(); i++) {
			Object[] values = data.entrySet().toArray();
			for (int j = 0; j < values.length; j++) {
				sinValue = getMapFormat(values[j]);
				if ((sinValue == null) || (sinValue.length != 2)
						|| (sinValue[0] == null)
						|| Common.isNullStr(Common.nonNullStr(sinValue[0]))) {
					continue;
				}
				map.put(sinValue[0], sinValue[1]);
			}
		}
		return map;
	}

	private Object[] getMapFormat(Object value) {
		String aa = Common.nonNullStr(value);
		int i = aa.indexOf("=");
		if (i < 0) {
			return null;
		}
		return aa.split("=");
	}

	/**
	 * �ڲ�ѯ���֮ǰ��֮��Ҫִ����ص����
	 */
	public List executeQueryWithInfo(String sql, List sqls_Before,
			List sqls_After) {
		 if (sqls_Before != null)
		 try {
			QueryStub.getQueryTool().executeBatch(sqls_Before);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ((sqls_Before != null) && (sqls_Before.size() != 0)) {
			for (int i = 0; i < sqls_Before.size(); i++) {
				if (!Common.isNullStr(Common.nonNullStr(sqls_Before.get(i)))) {
					try {
						dao.executeBySql(Common.nonNullStr(sqls_Before.get(i)));
					} catch (Exception ee) {
						System.out.println("\nִ�е���֮ǰ��ѯ��䱨��\n");
						ee.printStackTrace();
						System.out.println(sqls_Before.get(i));
						System.out.println("\nִ�е���֮ǰ��ѯ��䱨��\n");
					}
				}
			}
		}
		List listR = null;
		if (!Common.isNullStr(sql)) {
			listR = dao.findBySql(sql);
		}
		if ((sqls_After != null) && (sqls_After.size() != 0)) {
			for (int i = 0; i < sqls_After.size(); i++) {
				if (!Common.isNullStr(Common.nonNullStr(sqls_After.get(i)))) {
					try {
						dao.executeBySql(Common.nonNullStr(sqls_After.get(i)));
					} catch (Exception ee) {
						System.out.println("\nִ�е���֮���ѯ��䱨��\n");
						ee.printStackTrace();
						System.out.println(sqls_Before.get(i));
						System.out.println("\nִ�е���֮���ѯ��䱨��\n");
					}
				}
			}
		}
		// if (sqls_After != null)
		// QueryStub.getQueryTool().executeBatch(sqls_After);
		return listR;
	}

	/**
	 * ����ѯ�����ResultSetת��ΪList<Map>���ݽṹ
	 * 
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	protected List asList(ResultSet rs) throws Exception {
		List result = new LinkedList();
		ResultSetMetaData meta = rs.getMetaData();
		if (rs.getRow() <= 0) {
			return result;
		}
		while (rs.next()) {
			Map row = new MyMap();
			for (int i = 0; i < meta.getColumnCount(); i++) {
				row.put(meta.getColumnName(i + 1), rs.getObject(i + 1));
			}
			result.add(row);
		}
		return result;
	}

	/**
	 * ����SQL��ȡdataset
	 */
	public DataSet getDataset(String sql) throws Exception {
		DataSet ds = DataSet.create();
		ds.setSqlClause(sql);
		ds.open();
		return ds;
	}

	/**
	 * �رջỰ
	 */
	public void closeSession() throws Exception {
		if (session == null) {
			return;
		}
		if ((session != null) || session.isConnected() || session.isOpen()) {
			if (dao != null) {
				dao.closeSession(session);
			}
			if (session != null) {
				session.close();
			}
		}
		// if ((dao != null) && (session != null))
		// dao.closeSession(session);
	}

	/**
	 * ��������
	 */
	public void dsPost(DataSet ds, String[] keyName) throws Exception {
		ds.setPrimarykey(keyName);
		ds.post();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nbcs.rp.prj.prjinput.ibs.IPrjInput#getDocBlob(int,
	 *      java.lang.String)
	 */
	public byte[] getDocBlob(int i, String tableName, String fieldName,
			String filter) throws SQLException, IOException,
			ClassNotFoundException {
		// String sqlSelect =
		// " SELECT EXCEL FROM fb_s_printinfo WHERE REPORT_ID='"
		// + report_id + "' and is_usemodel = 1";
		InputStream is = getBigDataStream(tableName, fieldName, filter);
		// WHERE REPORT_ID='"
		// + report_id + "' and is_usemodel = 1";
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		if (is == null) {
			return null;
		} else {
			int bit;
			int count = 1;
			is.skip(1024 * 1024 * (i - 1));
			while (((bit = is.read()) != -1) && (count <= 1024 * 1024)) {
				data.write(bit);
				count++;
			}
			is.close();
			return data.toByteArray();
		}
	}

	/**
	 * ��ȡblob����
	 * 
	 * @param row_id
	 *            ����
	 * @return ����InputStream
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private InputStream getBigDataStream(String tableName, String fieldName,
			String filter) throws SQLException, IOException,
			ClassNotFoundException {
		// String sqlSelect =
		// " SELECT EXCEL FROM fb_s_printinfo WHERE REPORT_ID='"
		// + report_id + "' and is_usemodel = 1";
		String sqlSelect = " select " + fieldName + " from " + tableName
				+ filter;
		InputStream is = null;
		Session session = dao.getSession();
		PreparedStatement pstmt2 = session.connection().prepareStatement(
				sqlSelect);
		ResultSet rs2 = pstmt2.executeQuery();
		if (rs2.next()) {
			if (Global.loginmode == 0) {// ����
				is = rs2.getBlob(1).getBinaryStream();
			} else {// ����
				is = rs2.getBinaryStream(1);
			}
		}
		rs2.close();
		pstmt2.close();
		if (session != null) {
			if (session.isConnected() || session.isOpen()) {
				dao.closeSession(session);
			}
		}
		return is;
	}

	// /**
	// * ����blob���ݣ�
	// *
	// * @throws Exception
	// */
	// public void modifyDocBlobs(String tableName, String fieldID, String
	// filter,
	// byte[] longColData) throws Exception {
	// // �������û�и�����¼���������
	// // ����������
	// String serverType = PubInterfaceStub.getServerMethod().getServerType();
	// if (Global.loginmode == 0) {// ����
	// String sqlSelect = " SELECT " + fieldID + ",LENGTH(" + fieldID
	// + ")+1 LEN FROM " + tableName + filter + " FOR UPDATE ";
	// Session session = dao.getSession();
	// PreparedStatement pstmt2 = session.connection().prepareStatement(
	// sqlSelect);
	// ResultSet rs2 = pstmt2.executeQuery();
	// // System.out.println(rs2.getRow());
	// if (longColData != null && longColData.length > 0) {
	//
	// if (rs2.next()) {
	//
	// // Blob b = rs2.getBlob("EXCEL");
	// // if (b instanceof
	// // weblogic.jdbc.vendor.oracle.OracleThinBlob) {
	// // weblogic.jdbc.vendor.oracle.OracleThinBlob blob =
	// // (weblogic.jdbc.vendor.oracle.OracleThinBlob) rs2
	// // .getBlob("EXCEL");
	// // OutputStream os = blob.getBinaryOutputStream();
	// // os.write(longColData);
	// // os.close();
	// // os = null;
	// // } else if (b instanceof oracle.sql.BLOB) {
	// // oracle.sql.BLOB blob = (oracle.sql.BLOB) b;
	// // OutputStream os = blob.getBinaryOutputStream();
	// // os.write(longColData);
	// // os.close();
	// // os = null;
	// // } else if (b == null) {
	// //
	// // } else {
	// // throw new Exception("δ֪�Ĵ��ֶ����ͣ�");
	// // }
	//
	// if ("weblogic".equalsIgnoreCase(serverType)) {
	// weblogic.jdbc.vendor.oracle.OracleThinBlob blob =
	// (weblogic.jdbc.vendor.oracle.OracleThinBlob) rs2
	// .getBlob(fieldID);
	// OutputStream os = blob.getBinaryOutputStream();
	// os.write(longColData);
	// os.close();
	// os = null;
	// } else if ("tomcat".equalsIgnoreCase(serverType)
	// || "weblogic".equalsIgnoreCase(serverType)) {
	// BLOB blob = (BLOB) rs2.getBlob(fieldID);
	// OutputStream os = blob.getBinaryOutputStream();
	// os.write(longColData);
	// os.close();
	// os = null;
	// } else {
	// BLOB blob = (BLOB) rs2.getBlob(fieldID);
	// OutputStream os = blob.getBinaryOutputStream();
	// os.write(longColData);
	// os.close();
	// os = null;
	// }
	// }
	// }
	// rs2.close();
	// pstmt2.close();
	// if (session != null) {
	// dao.closeSession(session);
	// }
	// } else {// ����
	// String sqlSelect = " UPDATE " + tableName + " SET " + fieldID
	// + "= ? " + filter;
	// Session session = dao.getSession();
	// PreparedStatement pstmt2 = session.connection().prepareStatement(
	// sqlSelect);
	// ByteArrayInputStream bis = new ByteArrayInputStream(longColData);
	// pstmt2.setBinaryStream(1, bis, longColData.length);
	// pstmt2.executeUpdate();
	// pstmt2.close();
	// if (session != null) {
	// dao.closeSession(session);
	// }
	// }
	// }

	/**
	 * ����blob���ݣ�
	 * 
	 * @throws Exception
	 */
	public void modifyDocBlobs(String tableName, String fieldID, String filter,
			byte[] longColData) throws Exception {
		// ����������
		if (Global.loginmode == 0) {// ����
			String sql = " SELECT " + fieldID + ",LENGTH(" + fieldID
					+ ")+1 LEN FROM " + tableName + filter + " FOR UPDATE ";
			Session session = dao.getSession();
			PreparedStatement pstmt2 = session.connection().prepareStatement(
					sql.toString());
			ResultSet rs2 = pstmt2.executeQuery();
			if ((longColData != null) && (longColData.length > 0)) {
				if (rs2.next()) {
					Blob b = rs2.getBlob(fieldID);
					if (b instanceof weblogic.jdbc.vendor.oracle.OracleThinBlob) {
						weblogic.jdbc.vendor.oracle.OracleThinBlob blob = (weblogic.jdbc.vendor.oracle.OracleThinBlob) rs2
								.getBlob(fieldID);
						OutputStream os = blob.getBinaryOutputStream();
						os.write(longColData);
						os.close();
						os = null;
					} else if (b instanceof oracle.sql.BLOB) {
						BLOB blob = (BLOB) rs2.getBlob(fieldID);
						OutputStream os = blob.getBinaryOutputStream();
						os.write(longColData);
						os.close();
						os = null;
					} else {
						BLOB blob = (BLOB) rs2.getBlob(fieldID);
						OutputStream os = blob.getBinaryOutputStream();
						os.write(longColData);
						os.close();
						os = null;
					}
				}
			}
			rs2.close();
			pstmt2.close();
			if (session != null) {
				dao.closeSession(session);
			}
		} else {// ����
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE ");
			sql.append(tableName);
			sql.append(" set "+fieldID);
			sql.append(" = ?  ");
			sql.append(filter);
			Session session = dao.getSession();
			PreparedStatement pstmt2 = session.connection().prepareStatement(
					sql.toString());
			ByteArrayInputStream bis = new ByteArrayInputStream(longColData);
			pstmt2.setBinaryStream(1, bis, longColData.length);
			pstmt2.executeUpdate();
			pstmt2.close();
			if (session != null) {
				dao.closeSession(session);
			}
		}
	}

	/**
	 * ���ñ���������¼
	 * 
	 * @param report_id
	 * @param set_year
	 * @throws Exception
	 */
	public void newSetData(String report_id, String set_year, String title)
			throws Exception {
		String sqlDelete = null;
		sqlDelete = "delete from rp_S_PRINTINFO where report_id='" + report_id
				+ "'";
		QueryStub.getQueryTool().executeUpdate(sqlDelete);
		// }
		String sqlInsert = null;
		if (Global.loginmode == 0) {
			sqlInsert = "INSERT INTO rp_S_PRINTINFO(REPORT_ID,REPORT_TYPE,SET_YEAR,EXCEL,MODIFYTIME,REPORT_TITLE) VALUES('"
					+ report_id
					+ "',40,"
					+ set_year
					+ ",empty_blob(),SYSDATE,'" + title + "')";
		} else {
			sqlInsert = "INSERT INTO rp_S_PRINTINFO(REPORT_ID,REPORT_TYPE,SET_YEAR,EXCEL,MODIFYTIME,REPORT_TITLE) VALUES('"
					+ report_id
					+ "',40,"
					+ set_year
					+ ",empty_blob(),GETDATE(),'" + title + "')";
		}
		QueryStub.getQueryTool().executeUpdate(sqlInsert);
	}

	/**
	 * ���ñ���������¼
	 * 
	 * @param report_id
	 * @param set_year
	 * @throws Exception
	 */
	public void newImportSetData(String tableEName, String tableCName,
			String set_year) throws Exception {
		String sqlDelete = null;
		sqlDelete = "delete from fb_s_importinfo where table_ename='"
				+ tableEName + "'";
		QueryStub.getQueryTool().executeUpdate(sqlDelete);
		// }
		String sqlInsert = null;
		if (Global.loginmode == 0) {
			sqlInsert = "INSERT INTO fb_s_importinfo(table_ename,SET_YEAR,EXCEL,MODIFYTIME,table_cname) VALUES('"
					+ tableEName
					+ "',"
					+ set_year
					+ ",empty_blob(),SYSDATE,'"
					+ tableCName + "')";
		} else {
			sqlInsert = "INSERT INTO fb_s_importinfo(table_ename,SET_YEAR,EXCEL,MODIFYTIME,table_cname) VALUES('"
					+ tableEName
					+ "',"
					+ set_year
					+ ",empty_blob(),GETDATE(),'" + tableCName + "')";
		}
		QueryStub.getQueryTool().executeUpdate(sqlInsert);
	}

	/**
	 * ����BLOB�ֶ�����
	 * 
	 * @param report_id
	 *            �������
	 * @param longColData
	 *            blob�Ķ������ֶ���Ϣ
	 * @throws SQLException
	 *             sql�쳣
	 * @throws IOException
	 *             �ļ��쳣
	 * @throws ClassNotFoundException
	 *             ��û���ҵ����쳣
	 */
	public void updateBlobs(String report_id, byte[] longColData)
			throws SQLException, IOException, ClassNotFoundException {
		try {
			if (Global.loginmode == 0) {// ����
				String sqlSelect = " SELECT EXCEL,LENGTH(EXCEL)+1 LEN FROM FB_S_PRINTINFO WHERE REPORT_ID='"
						+ report_id + "' FOR UPDATE ";
				Session session = dao.getSession();
				PreparedStatement pstmt2 = session.connection()
						.prepareStatement(sqlSelect);
				ResultSet rs2 = pstmt2.executeQuery();
				if ((longColData != null) && (longColData.length > 0)) {
					if (rs2.next()) {
						weblogic.jdbc.vendor.oracle.OracleThinBlob blob = (weblogic.jdbc.vendor.oracle.OracleThinBlob) rs2
								.getBlob("EXCEL");
						// long blobLen = rs2.getLong("len");
						OutputStream os = blob.getBinaryOutputStream();
						os.write(longColData);
						os.close();
						os = null;
					}
				}
				rs2.close();
				pstmt2.close();
				if (session != null) {
					dao.closeSession(session);
				}
			} else {// ����
				String sqlSelect = " UPDATE FB_S_PRINTINFO SET EXCEL= ? WHERE REPORT_ID='"
						+ report_id + "' ";
				Session session = dao.getSession();
				PreparedStatement pstmt2 = session.connection()
						.prepareStatement(sqlSelect);

				ByteArrayInputStream bis = new ByteArrayInputStream(longColData);

				pstmt2.setBinaryStream(1, bis, longColData.length);
				pstmt2.executeUpdate();
				pstmt2.close();
				if (session != null) {
					dao.closeSession(session);
				}
			}

		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	public void execCute(String sql) throws Exception {
		QueryStub.getQueryTool().execute(sql);
	}

	/**
	 * ��Ȩ�޵ĵ�λ���ݼ�
	 */
	public DataSet getDivDataPop(String sYear, int isSetted, String swhere)
			throws Exception {
		String sSql = " select * from vw_fb_division ENTAB "
				+ " where is_leaf = 1 and set_year="
				+ sYear
				+ " "
				+ swhere
				+ " union all "
				+ " select * from vw_fb_division tab "
				+ " where tab.is_leaf <> 1 and tab.set_year="
				+ sYear
				+ " and "
				+ " exists (select 1 from vw_fb_division ENTAB where ENTAB.div_code like tab.div_code||'%'"
				+ " and ENTAB.is_leaf = 1 and ENTAB.set_year=" + sYear + " "
				+ swhere + ")";
		sSql = getFilterSetted(sSql, isSetted);
		DataSet dsData = DataSet.create();
		DBSqlExec.getDataSet(sSql, dsData);
		return dsData;
	}

	/**
	 * ��ȡ�Ѿ������˵�λ�ֵ��ĵ�λ��Ϣ
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getFilterSetted(String sql, int iSetted) throws Exception {
		if (iSetted == 1) {
			String divCodes1 = getDivParID("select distinct(div_code) as div_code from fb_s_ration_divtotype");
			sql = "select * from ("
					+ sql
					+ ") kkl where div_code in ("
					+ "select distinct(div_code) as div_code from fb_s_ration_divtotype"
					+ " union all "
					+ "select div_code from vw_fb_division where div_code in ("
					+ divCodes1 + "))";
		} else if (iSetted == 2) {
			sql = "select * from ("
					+ sql
					+ ") kkl where div_code not in ("
					+ "select distinct(div_code) as div_code from fb_s_ration_divtotype)";
		}
		return sql;
	}

	/**
	 * ��ȡdivcode�����и��ڵ����
	 * 
	 * @param sql1
	 *            ���ڵ�div_code�Ĳ�ѯ���
	 * @return ���磺"'001','002','003'"
	 * @throws Exception
	 */
	private String getDivParID(String sql) throws Exception {
		// ���ڵ�λ���൵����ȡ�����е�λ����
		// ������Щ��λ�����ڵ�λ�����ҵ�������Ӧ�Ĳ����level_num
		// ���ݲ�����Ҹñ������еĸ��׽ڵ㣬�ŵ�list��
		// ����listд��sql����union all���ŵ����
		if (Common.isNullStr(sql)) {
			return null;
		}
		String divCodes = null;
		DataSet ds = DataSet.create();
		DBSqlExec
				.getDataSet(
						"select distinct(div_code) as div_code from fb_s_ration_divtotype",
						ds);
		if ((ds == null) || ds.isEmpty()) {
			return sql;
		}
		String[] divcodes = new String[ds.getRecordCount()];
		String divCode = null;
		int i = 0;
		Set list = new HashSet();
		int levnum = 0;
		ds.beforeFirst();
		while (ds.next()) {
			if (i == 0) {
				divCodes = "'" + ds.fieldByName("DIV_CODE").getString() + "'";
			} else {
				divCodes = divCodes + ",'"
						+ ds.fieldByName("DIV_CODE").getString() + "'";
			}
			divcodes[i++] = ds.fieldByName("DIV_CODE").getString();
		}
		DBSqlExec.getDataSet(
				"select div_code,level_num from vw_fb_division where div_code in ("
						+ divCodes + ")", ds);
		if ((ds == null) || ds.isEmpty()) {
			return sql;
		}
		ds.beforeFirst();
		while (ds.next()) {
			if ("0".equals(ds.fieldByName("IS_LEAF").getString())) {
				continue;
			}
			levnum = ds.fieldByName("level_num").getInteger();
			for (int j = 0; j < levnum - 1; j++) {
				divCode = ds.fieldByName("div_code").getString();
				list.add(divCode.substring(3 * j, 3 * (j + 1)));
			}
		}
		list.toArray(divcodes);
		for (int j = 0; j < divcodes.length; j++) {
			if (Common.isNullStr(divcodes[j])) {
				continue;
			}
			if (j == 0) {
				divCodes = "'" + divcodes[j] + "'";
			} else {
				divCodes = divCodes + ",'" + divcodes[j] + "'";
			}
		}
		return divCodes;
	}

}
