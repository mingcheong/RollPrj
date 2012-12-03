<%@ page contentType="text/html; charset=GBK"%>
<%@ page language="java" session="true" import="java.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>财政综合业务系统V6.0C</title>
</head>
<script language="javascript">
  function onLoad()
  {
    window.name="财政综合业务系统V6.0c";
    var tmpFrm=document.firstForm;
    tmpFrm.action="selectapp";
    tmpFrm.submit();
  }
</script>
<body onload="onLoad()">
<form id="firstForm" name="firstForm" action="selectapp" method="post"/>
</body>
</html>