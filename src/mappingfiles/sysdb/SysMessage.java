package mappingfiles.sysdb;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SysMessage implements Serializable {

	/** identifier field */
	private String MSG_ID;

	/** nullable persistent field */
	private String MSG_TITLE;

	/** nullable persistent field */
	private String MSG_CONTENT;

	/** nullable persistent field */
	private Integer MSG_TYPE_CODE;

	/** nullable persistent field */
	private String USER_ID;

	/** nullable persistent field */
	private String ROLE_ID;

	/** nullable persistent field */
	private String MODULE_ID;

	/** nullable persistent field */
	private String PARA_STRING;

	/** nullable persistent field */
	private Integer SEND_TYPE;

	/** nullable persistent field */
	private Integer IS_TEMP;

	/** nullable persistent field */
	private Integer IS_SEND;

	/** nullable persistent field */
	private Integer IS_RELATEROLE;

	/** nullable persistent field */
	private Integer IS_RECEIVE;

	/** nullable persistent field */
	private Integer SEND_NUM;

	/** nullable persistent field */
	private String LAST_VER;

	/** nullable persistent field */
	private String CANCEL_RELATION_ID;

	/** nullable persistent field */
	private String FROMUSER;

	/** nullable persistent field */
	private String SENT_TIME;

	/** nullable persistent field */
	private String RECEIVE_TIME;

	/** full constructor */
	public SysMessage(String MSG_ID, String MSG_TITLE, String MSG_CONTENT,
			Integer MSG_TYPE_CODE, String USER_ID, String ROLE_ID,
			String MODULE_ID, String PARA_STRING, Integer SEND_TYPE,
			Integer IS_TEMP, Integer IS_SEND, Integer IS_RECEIVE,
			Integer SEND_NUM, String LAST_VER, String CANCEL_RELATION_ID,
			String FROMUSER, String SENT_TIME, String RECEIVE_TIME) {
		this.MSG_ID = MSG_ID;
		this.MSG_TITLE = MSG_TITLE;
		this.MSG_CONTENT = MSG_CONTENT;
		this.MSG_TYPE_CODE = MSG_TYPE_CODE;
		this.USER_ID = USER_ID;
		this.ROLE_ID = ROLE_ID;
		this.MODULE_ID = MODULE_ID;
		this.PARA_STRING = PARA_STRING;
		this.SEND_TYPE = SEND_TYPE;
		this.IS_TEMP = IS_TEMP;
		this.IS_SEND = IS_SEND;
		this.IS_RECEIVE = IS_RECEIVE;
		this.SEND_NUM = SEND_NUM;
		this.LAST_VER = LAST_VER;
		this.CANCEL_RELATION_ID = CANCEL_RELATION_ID;
		this.FROMUSER = FROMUSER;
		this.SENT_TIME = SENT_TIME;
		this.RECEIVE_TIME = RECEIVE_TIME;
	}

	/** default constructor */
	public SysMessage() {
	}

	/** minimal constructor */
	public SysMessage(String MSG_ID) {
		this.MSG_ID = MSG_ID;
	}

	public String getMSG_ID() {
		return this.MSG_ID;
	}

	public void setMSG_ID(String MSG_ID) {
		this.MSG_ID = MSG_ID;
	}

	public String getMSG_TITLE() {
		return this.MSG_TITLE;
	}

	public void setMSG_TITLE(String MSG_TITLE) {
		this.MSG_TITLE = MSG_TITLE;
	}

	public String getMSG_CONTENT() {
		return this.MSG_CONTENT;
	}

	public void setMSG_CONTENT(String MSG_CONTENT) {
		this.MSG_CONTENT = MSG_CONTENT;
	}

	public Integer getMSG_TYPE_CODE() {
		return this.MSG_TYPE_CODE;
	}

	public void setMSG_TYPE_CODE(Integer MSG_TYPE_CODE) {
		this.MSG_TYPE_CODE = MSG_TYPE_CODE;
	}

	public String getUSER_ID() {
		return this.USER_ID;
	}

	public void setUSER_ID(String USER_ID) {
		this.USER_ID = USER_ID;
	}

	public String getROLE_ID() {
		return this.ROLE_ID;
	}

	public void setROLE_ID(String ROLE_ID) {
		this.ROLE_ID = ROLE_ID;
	}

	public String getMODULE_ID() {
		return this.MODULE_ID;
	}

	public void setMODULE_ID(String MODULE_ID) {
		this.MODULE_ID = MODULE_ID;
	}

	public String getPARA_STRING() {
		return this.PARA_STRING;
	}

	public void setPARA_STRING(String PARA_STRING) {
		this.PARA_STRING = PARA_STRING;
	}

	public Integer getSEND_TYPE() {
		return this.SEND_TYPE;
	}

	public void setSEND_TYPE(Integer SEND_TYPE) {
		this.SEND_TYPE = SEND_TYPE;
	}

	public Integer getIS_TEMP() {
		return this.IS_TEMP;
	}

	public void setIS_TEMP(Integer IS_TEMP) {
		this.IS_TEMP = IS_TEMP;
	}

	public Integer getIS_SEND() {
		return this.IS_SEND;
	}

	public void setIS_SEND(Integer IS_SEND) {
		this.IS_SEND = IS_SEND;
	}

	public Integer getIS_RECEIVE() {
		return this.IS_RECEIVE;
	}

	public void setIS_RECEIVE(Integer IS_RECEIVE) {
		this.IS_RECEIVE = IS_RECEIVE;
	}

	public Integer getSEND_NUM() {
		return this.SEND_NUM;
	}

	public void setSEND_NUM(Integer SEND_NUM) {
		this.SEND_NUM = SEND_NUM;
	}

	public String getLAST_VER() {
		return this.LAST_VER;
	}

	public void setLAST_VER(String LAST_VER) {
		this.LAST_VER = LAST_VER;
	}

	public String getCANCEL_RELATION_ID() {
		return this.CANCEL_RELATION_ID;
	}

	public void setCANCEL_RELATION_ID(String CANCEL_RELATION_ID) {
		this.CANCEL_RELATION_ID = CANCEL_RELATION_ID;
	}

	public String getFROMUSER() {
		return this.FROMUSER;
	}

	public void setFROMUSER(String FROMUSER) {
		this.FROMUSER = FROMUSER;
	}

	public String getSENT_TIME() {
		return this.SENT_TIME;
	}

	public void setSENT_TIME(String SENT_TIME) {
		this.SENT_TIME = SENT_TIME;
	}

	public String getRECEIVE_TIME() {
		return this.RECEIVE_TIME;
	}

	public void setRECEIVE_TIME(String RECEIVE_TIME) {
		this.RECEIVE_TIME = RECEIVE_TIME;
	}

	public String toString() {
		return new ToStringBuilder(this).append("MSG_ID", getMSG_ID())
				.toString();
	}

	public Integer getIS_RELATEROLE() {
		return IS_RELATEROLE;
	}

	public void setIS_RELATEROLE(Integer is_relaterole) {
		IS_RELATEROLE = is_relaterole;
	}

}
