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


<span id="<%=JaniculumWrapper.id(component, "content") %>" >
<img alt="<%=JaniculumWrapper.i18NHint(component) %>" id="<%=JaniculumWrapper.id(component, "content_img")%>" src="chart.png?imagePojo=<%=JaniculumWrapper.imageId(component)%>&t=<%=JaniculumWrapper.currentTime()%>"
	<%for(String event: JaniculumWrapper.getAvailableEvents(component)){ %> 
		
		on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component) %>'); romaEvent('<%=JaniculumWrapper.fieldName(component) %>', '<%=event%>')"
	<%} %>
	/>
</span>

<%} %>



<%if ("content".equals(part)){ %>
<img alt="<%=JaniculumWrapper.i18NHint(component) %>" id="<%=JaniculumWrapper.id(component, "content")%>" src="chart.png?imagePojo=<%=JaniculumWrapper.imageId(component)%>&t=<%=JaniculumWrapper.currentTime()%>" 
	<%for(String event: JaniculumWrapper.getAvailableEvents(component)){ %>
		on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component) %>'); romaEvent('<%=JaniculumWrapper.fieldName(component) %>', '<%=event%>')"
	<%} %>
	
	/>
<%=JaniculumWrapper.content(component)==null?"":JaniculumWrapper.content(component)%>

<%} %>


<%if ("label".equals(part)){%>
<label id="<%=JaniculumWrapper.id(component, "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label>
<% } %>

</div>