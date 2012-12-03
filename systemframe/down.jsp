
<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@ page import="gov.mof.exchange.client.DataExchangeManager,com.foundercy.pf.util.sessionmanager.SessionUtil"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String userName = request.getParameter("user_code");
String password = request.getParameter("password");
int year = 2006;
String system = "999";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'down.jsp' starting page</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
  </head>
  
  <body>
    <%try{
         String exchangeIP = (String) SessionUtil.getParaMap().get("EXCHANGE_IP"); 
         String exchangePortString = (String) SessionUtil.getParaMap().get("EXCHANGE_PORT");
		 int exchangePort = Integer.parseInt(exchangePortString);  
		 DataExchangeManager manager = DataExchangeManager.getInstance(exchangeIP, exchangePort);
		 List list = manager.getDownloadFileList(userName, password,year, system, 0);
		 request.setAttribute("result",list); 
		 request.setAttribute("user_code",userName);
		 request.setAttribute("password",password);
      }catch(Exception e){
      }
    %>
    <jsp:forward page="/exchange_new.jsp"/>
  </body>
</html>
