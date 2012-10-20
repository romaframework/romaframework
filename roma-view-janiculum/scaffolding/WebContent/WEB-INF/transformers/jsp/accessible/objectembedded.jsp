<%@page import="org.romaframework.aspect.view.form.ViewComponent"%>
<%@page import="org.romaframework.aspect.view.html.HtmlViewAspectHelper"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.romaframework.aspect.view.html.transformer.helper.TransformerHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	HtmlViewAspectHelper.renderByJsp((ViewComponent)component, request, pageContext.getOut());
%>