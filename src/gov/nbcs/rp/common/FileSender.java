/**
 * @(#)FileSender.java
 *
 * @title  - 预算编审子系统
 * 
 * @copyright 浙江易桥 版权所有
 */
package gov.nbcs.rp.common;

import com.foundercy.pf.util.BeanFactoryUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;


public class FileSender extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * 下载文件列表
	 */
	private static final String DownloadFiles_XML_Location = "download-files.xml";

	/**
	 * bean的id前缀
	 */
	private static final String DownloadFile_Prefix = "download-file-";

	/**
	 * 默认的下载文件存放目录，相对于systemframe/
	 */
	private static final String default_file_path = "WEB-INF/download/";

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 *
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String fileNo = request.getParameter("foca");
		if (fileNo != null) {
			BeanFactory bf = BeanFactoryUtil
					.getBeanFactory(DownloadFiles_XML_Location);
			FileInfo fi = (FileInfo) bf.getBean(DownloadFile_Prefix + fileNo);
			if (fi != null) {
				File file = new File(fi.getUrl());
				if (file.exists()) {
					sendFile(request, response, fi.getType(), fi.getName(),
							file);
				} else {
					sendError(response, fi.getUrl() + " does not exist");
				}
			} else {
				sendError(response, "File Info is null");
			}
		} else {
			String noca = request.getParameter("noca");
			if (noca != null) {
				String fn = new String(noca.getBytes("ISO-8859-1"), "UTF-8");
				ServletContext context = getServletContext();
				String rootPath = context.getRealPath("/");
				String serverType = request.getParameter("st");
				if ((serverType == null) || serverType.equals("w")
						|| serverType.equals("weblogic")) {
					// 默认为weblogic
					rootPath += "/";
				} else {
					// tomcat等

				}
				File file = new File(rootPath + default_file_path + fn);
				if (file.exists()) {
					String clientName = fn;
					sendFile(request, response, "application/x-msdownload",
							clientName, file);
				} else {
					sendError(response, rootPath + default_file_path + fn
							+ " does not exist");
				}
			} else {
				sendError(response, "Parameter is null");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private void sendFile(HttpServletRequest request,
			HttpServletResponse response, String contentType,
			String clientName, File file) throws ServletException, IOException {
		response.reset();
		response.setContentType(contentType);

		String filename = URLEncoder.encode(clientName, "UTF-8");
		// ie5.5中不需要"attachment;"
		if (request.getHeader("User-Agent").indexOf("MSIE 5.5") != -1) {
			response.addHeader("Content-Disposition", "filename=\"" + filename
					+ "\"");

		} else {
			response.addHeader("Content-Disposition", "attachment; filename=\""
					+ filename + "\"");
		}
		int fileLength = (int) file.length();
		response.setContentLength(fileLength);

		if (fileLength != 0) {
			// 输入流
			InputStream inStream = new FileInputStream(file);
			// 输出流
			ServletOutputStream servletOS = response.getOutputStream();
			byte[] buf = new byte[8192];
			int readLength;
			while (((readLength = inStream.read(buf)) != -1)) {
				servletOS.write(buf, 0, readLength);
			}
			servletOS.flush();
			servletOS.close();
			inStream.close();
		}
	}

	private void sendError(HttpServletResponse response, String message)
			throws IOException {
		// ServletContext context = getServletContext();
		// String rootPath = context.getRealPath("/");
		response.setContentType("text/html;charset=gb2312");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<meta http-equiv=\"error-message\" content=\"" + message
				+ "\">");
		out
				.println("<HTML><HEAD><TITLE>文件请求错误</TITLE></HEAD><BODY>没有找到您请求的文件</BODY></HTML>");
		out.flush();
		out.close();
	}
}
