<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Set"%>

<%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%>
<%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%@page import="java.util.Map"%>
<%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);
%>
<div class="<%=JaniculumWrapper.cssClass(component, "chart", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
<% if(part==null ||  "".equals(part) || "all".equals(part)) {%>
<span id="<%=JaniculumWrapper.id(component, "content") %>" class="<%=JaniculumWrapper.cssClass(component, "chart", "content") %>" >
<img alt="<%=JaniculumWrapper.i18NHint(component) %>" id="<%=JaniculumWrapper.id(component, "content")%>_img" class="<%=JaniculumWrapper.cssClass(component, "chart", "content")%>" src="chart.png?imagePojo=<%=JaniculumWrapper.imageId(component)%>&t=<%=JaniculumWrapper.currentTime()%>"/>
</span>
<%} %>
<%if ("content".equals(part)){ %>
<img id="<%=JaniculumWrapper.id(component, "content")%>" alt="<%=JaniculumWrapper.i18NHint(component) %>" class="<%=JaniculumWrapper.cssClass(component, "chart", "content")%>" src="chart.png?imagePojo=<%=JaniculumWrapper.imageId(component)%>&t=<%=JaniculumWrapper.currentTime()%>" />
<%=JaniculumWrapper.content(component)==null?"":JaniculumWrapper.content(component)%>

<%} %>


<%if (part.equals("label")){%>
<label id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "chart", "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label>
<% } %>

</div>