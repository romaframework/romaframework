<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%@page import="org.romaframework.aspect.view.html.component.HtmlViewInvisibleContentComponent"%>
<%@page import="java.util.Set"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%>
<%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);
	
	String vAlign = JaniculumWrapper.areaVerticalAlignment(component);
	String hAlign = JaniculumWrapper.areaHorizontalAlignment(component);
	String marginLeft = "";
	if("left".equals(hAlign)){
		marginLeft = "0";
	}else if("right".equals(hAlign)){
		marginLeft = "auto";
	}
	
	String marginRight = "";
	if("right".equals(hAlign)){
		marginRight = "0";
	}else if("left".equals(hAlign)){
		marginRight = "auto";
	}
	JspTransformerHelper.addCss(JaniculumWrapper.id(component, null)+" > div.POJO > table.area_main", "margin-left", marginLeft);
	JspTransformerHelper.addCss(JaniculumWrapper.id(component, null)+" > div.POJO > table.area_main", "margin-right", marginRight);
	
	JspTransformerHelper.addCss(JaniculumWrapper.id(component, null)+" > table", "margin-left", marginLeft);
	JspTransformerHelper.addCss(JaniculumWrapper.id(component, null)+" > table", "margin-right", marginRight);
%>
<div id="<%=JaniculumWrapper.id(component, null)%>" class="<%=JaniculumWrapper.cssClass(component, "placeholder", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>">
<%for(Object c:JaniculumWrapper.getChildren(component)){
	HtmlViewRenderable child = (HtmlViewRenderable)c;
	if (child == null ||child instanceof HtmlViewInvisibleContentComponent)
		continue;
	
%><%JspTransformerHelper.delegate(child, null,pageContext.getOut()); %><%} %>
</div>