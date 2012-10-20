<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib uri="/WEB-INF/roma.tld" prefix="roma"%>
<%@ page buffer="none" %>
<%@ page import="org.romaframework.core.Roma"%>
<%@ page import="org.romaframework.core.config.ApplicationConfiguration"%>
<%@page import="org.romaframework.aspect.view.html.screen.HtmlViewScreen"%>
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server

String appName = Roma.component(ApplicationConfiguration.class).getApplicationName();
((HtmlViewScreen)Roma.view().getScreen()).setRenderSet("accessible");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml" lang="<%=Roma.session().getActiveLocale()%>" xml:lang="<%=Roma.session().getActiveLocale()%>">
<head>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/static/themes/default/css/ui.datepicker.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/static/themes/default/css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/static/themes/default/css/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/static/themes/default/css/application-style.css" />
<!-- ADDITIONAL CSS -->
<link rel="icon" href="<%=request.getContextPath() %>/static/images/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=appName%></title>
<roma:inlinecss/>
</head>
<body>
<form method="post" action="<%=Roma.view().getContextPath()+"/app"%>">
<%
	if(Roma.view().getScreen().getArea("popups").getChildren()!= null && Roma.view().getScreen().getArea("popups").getChildren().size() >0){
		%>
		<roma:screenArea name="/popups"/>
		<%
	}else{
%>
 <roma:screenArea name="/"/>
 <%} %>
</form>
</body>
</html>
