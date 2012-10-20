<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/roma.tld" prefix="roma"%>
<%@ page buffer="none"%>

<%@page import="org.romaframework.core.config.ApplicationConfiguration"%>
<%@page import="org.romaframework.core.*"%>

<%
	String appName = Utility.getCapitalizedString(Roma.component(ApplicationConfiguration.class).getApplicationName());
	
%>



<table style="margin-left:auto; margin-right:auto;">
	<tr>
		<td align="center"><h1><a style="text-decoration: none" href='<%=request.getContextPath()%>/app/direct/home'><%=appName%></a></h1>
		</td>
	</tr>
	<tr>
		<td>
		<div class="loginbox">
		<roma:class /></div>
		</td>
	</tr>
	<tr>
	<td><div ><pre></pre></div></td>
	</tr>
</table>

<div style="position:fixed; bottom: 20px; right: 20px;">
<a href="http://www.romaframework.org"><img
	src='<%=request.getContextPath()%>/static/themes/default/image/poweredByRoma.jpg'
	alt="Roma" border='0' />
</a></div>
