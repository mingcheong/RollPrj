
<%@ page language="java" import="java.util.*,java.io.File" pageEncoding="UTF-8"%>
<%java.io.BufferedInputStream bis = null;
			java.io.BufferedOutputStream bos = null;
			try {
				String filename = (String) request.getAttribute("strFileName");
				filename = new String(filename.getBytes("iso8859-1"), "gb2312");
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-disposition",
						"attachment; filename="
								+ new String(filename.getBytes("gb2312"),
										"iso8859-1"));
				bis = new java.io.BufferedInputStream(
						new java.io.FileInputStream("c:\\" + filename));
				bos = new java.io.BufferedOutputStream(response
						.getOutputStream());
				byte[] buff = new byte[2048];
				int bytesRead;
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			}

		%>
