package gov.nbcs.rp.common;

public class FileInfo {

	public FileInfo() {
	}

	/**
	 * �ļ�contentType
	 */
	private String type;

	/**
	 * ���������ʾ���ļ���
	 */
	private String name;

	/**
	 * �ļ���ŵ�ַ
	 */
	private String url;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
