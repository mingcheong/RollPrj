<%@ page contentType="text/html; charset=gb2312"%>
<%@ page import="java.util.*,com.foundercy.pf.framework.webstartbean.*"%>
<%
	List data = SysAppFactory.getSysApps();
	String urls = "";
	String years = "";
	String sid = "";
	String uid = "";
	String uCode = "";

	if (null != request.getParameter("uid")
			&& !"".equals(request.getParameter("uid"))) {
		uid = "&uid=" + request.getParameter("uid");
	}
	if (null != request.getParameter("sid")
			&& !"".equals(request.getParameter("sid"))) {
		sid = "&sid=" + request.getParameter("sid");
	}
	if (null != request.getParameter("uCode")
			&& !"".equals(request.getParameter("uCode"))) {
		uCode = "&uCode=" + request.getParameter("uCode");
	}

	for (int i = 0; i < data.size(); i++) {
		SysAppBean sysApp = (SysAppBean) data.get(i);
		String str = sysApp.getUrl() + "?sysapp=" + sysApp.getId()
		+ sid + uid + uCode;
		urls = urls + str + "||";
		for (int j = 0; j < sysApp.getSysYears().length; j++) {
			years = years + sysApp.getSysYears()[j] + ",";
		}
		years = years + "||";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>Insert title here</title>
		<script type="text/javascript">
		
//	window.moveTo((screen.width-494)/ 2, 100);
	window.resizeTo(500, 340);
	
	//  帐套系统 sysAppid
	var booksetSysapp="002";
	//  该年度无系统帐套的提示信息
	var nobooksetmessage="无该年度系统帐套";
	
	var yearStr="<%=years%>";
	var businessStr="<%=urls%>";
	
	var yearArray1=yearStr.split('||');
	var businessArray =businessStr.split('||');

function initYear(){
	var year = document.getElementById('year');
	var business = document.getElementById('business');
	var thisYear = new Date().getYear();
	var yearArray = yearArray1[business.selectedIndex].split(',');
	year.options.length=yearArray.length-1;
	for(var i=0;i<yearArray.length-1;i++){
		year.options[i].text = yearArray[i];
		year.options[i].value = yearArray[i];
		if(thisYear==yearArray[i]){
			year.selectedIndex=i;
		}
	}
//  系统帐套选择
//	initBookSet();
}
function initBookSet(){
	var year = document.getElementById('year');
	var business = document.getElementById('business');
	var booksetdiv = document.getElementById("booksetdiv");
	
	var display = business.value==booksetSysapp;
	if(display){
//		booksetdiv.style.display="block";
//  系统帐套选择
//		queryBookSet();
	}else{
		booksetdiv.style.display="none";
	}
}
function on_form_submit(form){
	var year = document.getElementById('year');
	var business = document.getElementById('business');
	var bookset = document.getElementById("bookset");
	var url = businessArray[business.selectedIndex];
	url =url +"&setYear="+year.options[year.selectedIndex].value;
	
//	if(business.value==booksetSysapp){
//		if(bookset.value!=""){
//			url = url+"&booksetid="+bookset.value;
//		}else{
//			alert(nobooksetmessage);
//			return false;
//		}
//	}
	
//	alert("url地址为："+url);
//	eval(parent.location=url);
	window.open(url);
	window.close();
}
//var xmlRequest;
function queryBookSet(){
 	var e;
 	var uid = '<%=request.getParameter("uid")%>';
	 try{
	 	xmlRequest = new XMLHttpRequest();
	 }catch(e) {
	    try {
	       xmlRequest = new ActiveXObject("Msxml2.XMLHTTP");
	    }
	    catch(e) {
		    try {
		       xmlRequest = new ActiveXObject("Microsoft.XMLHTTP");
		    }
		    catch(e) {
		    }
	    }
	}
    xmlRequest.open('POST','bookSetApp?uid='+uid+'&setyear='+document.getElementById('year').value,true);
    xmlRequest.onreadystatechange = formChange;
//    xmlRequest.send('uid='+uid+'&setyear='+document.getElementById('year').value);
	xmlRequest.send(null);
}
function formChange(){
	if (xmlRequest.readyState == 4) {
        if (xmlRequest.status == 200) {
           var result = xmlRequest.responseText;
         
           var bookset = document.getElementById("bookset");
           if(result.indexOf('||')>0){
           	   
	           var booksetArray=result.split('||');
	           
	           bookset.options.length=booksetArray.length-1;
	           
	           for(var i=0;i<booksetArray.length-1;i++){
	           		var bookArray =booksetArray[i].split('&&');
	           		bookset.options[i].value=bookArray[0];
	           		bookset.options[i].text=bookArray[1];
	           }
           }else{
           		bookset.options.length=1;
           		bookset.options[0].value="";
	           	bookset.options[0].text=nobooksetmessage;
           }
        }
     }
}
</SCRIPT>
	</HEAD>
	<BODY style="MARGIN-TOP: 0px; MARGIN-LEFT: 0px">

		<TABLE height=310 width=490 align=center background=images/background.jpg
			border=0>

			<TR>
				<TD width="244" height="150">
					&nbsp;
				</TD>
				<TD width="236">
					&nbsp;
				</TD>
			</TR>
			<TR>
				<TD>
					&nbsp;
				</TD>
				<TD align="left">
					<FORM id="form1" name="form1">

						<DIV style="FONT-SIZE: 12px" align=left>
							业&nbsp;&nbsp;务：
							<SELECT style="FONT-SIZE: 12px; WIDTH: 125px"
								onchange="javascript:initYear();" name="business" id="business">
								<%
										for (int i = 0; i < data.size(); i++) {
										SysAppBean sysApp = (SysAppBean) data.get(i);
								%>
								<option value="<%=sysApp.getId()%>">
									<%=sysApp.getName()%>
								</option>
								<%
								}
								%>
							</SELECT>
						</DIV>

						<DIV style="FONT-SIZE: 12px" align=left>
							年&nbsp;&nbsp;度：
							<LABEL for=textfield></LABEL>
							<SELECT id="year" name="year"
								style="FONT-SIZE: 12px; WIDTH: 125px"
								onchange="javascript:initBookSet();">
							</SELECT>

						</DIV>

						<DIV id="booksetdiv" style="FONT-SIZE: 12px;display: none"
							align=left>
							帐&nbsp;&nbsp;套：
							<SELECT id="bookset" name="bookset"
								style="FONT-SIZE: 12px; WIDTH: 125px">
							</SELECT>
						</DIV>
						<SCRIPT type=text/javascript>
							initYear();
      					</SCRIPT>
						<DIV align=left style="margin-left:50px;">
							<INPUT onclick=javascript:on_form_submit(this.form); type=button
								value="确 定" name=button>

							<INPUT onclick=javascript:window.close(); type=button value="关 闭"
								name=button>
						</DIV>
					</FORM>
				</TD>
			</TR>
		</TABLE>

	</BODY>
</HTML>
