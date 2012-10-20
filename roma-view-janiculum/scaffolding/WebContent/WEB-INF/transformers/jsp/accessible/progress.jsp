<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%><%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);

if(part==null || "".equals(part) || "all".equals(part)){ %>
<div>
<%=JaniculumWrapper.content(component, true)==null?"0":JaniculumWrapper.content(component, true)%>%
</div><%
}else if("raw".equals(part)){
%><%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%><%
}else if("label".equals(part)){
%><label id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "progress", "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label>
<%
}
%>