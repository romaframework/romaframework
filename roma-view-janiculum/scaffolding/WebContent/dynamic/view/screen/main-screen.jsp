<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="/WEB-INF/roma.tld" prefix="roma"%>
<%@ page buffer="none" %>
<%@ page import="org.romaframework.core.Roma"%>
<%@ page import="org.romaframework.core.config.ApplicationConfiguration"%>
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server

String appName = Roma.component(ApplicationConfiguration.class).getApplicationName();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html>
<head>
<%--<roma:css/>--%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/static/themes/default/css/ui.datepicker.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/static/themes/default/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/static/themes/default/css/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/static/themes/default/css/application-style.css" />
<!-- ADDITIONAL CSS -->
<link rel="icon" href="<%=request.getContextPath() %>/static/images/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=appName%></title>
<script type="text/javascript" src="<%=request.getContextPath() %>/static/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/static/jquery.timeentry.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/static/jquery-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/static/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/static/jqDnR.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/static/romaAjax.js"></script>
<roma:inlinejs/>
<roma:inlinecss/>
<%
if(Roma.component(ApplicationConfiguration.class).isApplicationDevelopment()){
 %>
<script type="text/javascript">
$(document).ready(function() {
	var res = '';
	var ids = null;	
	$('[id]').each(function(){
		  ids = $('[id='+this.id+']');
		  if(ids.length>1 && ids[0]==this) {
		      console.warn('Multiple IDs #'+this.id);
	      	  res = res+"\n"+'Multiple IDs #'+this.id;
		  }
		});
	if (res != '') {
	alert(res);
	}
});

</script>
<%} %>
<script type="text/javascript">
requestContextPath = "<%=request.getContextPath() %>/";
globalCharType = "charset=UTF-8";
</script>
</head>
<body>
<div id="janiculumWaitDiv" class="janiculumWaitImage" style="background-repeat: no-repeat; background-position: center center; background-image: url(<%=request.getContextPath()%>/static/themes/default/image/wait.gif); position:absolute; width: 100%; height: 100%; left:-10000px; top: -10000px; z-index: -1000">
</div>
<roma:screenArea name="/"/>
</body>
</html>
