<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);
%>
<div id="<%=JaniculumWrapper.id(component, null)%>" class="<%=JaniculumWrapper.cssClass(component, "objectlink", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>">
<div style="display:table;border-spacing:0;border-collapse:collapse">
<div style="display:table-row">
	<div style="display:table-cell">
		<label for="<%=JaniculumWrapper.id(component, "text")%>" style="display:none;"><%=JaniculumWrapper.i18NLabel(component)%></label>
		<input id="<%=JaniculumWrapper.id(component, "text")%>" type="text" disabled="disabled" value="<%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%>" />
	</div>
	<%if(!JaniculumWrapper.disabled(component)){%>
		<div style="display:table-cell">
			<input id="<%=JaniculumWrapper.id(component, "open")%>" class="<%=JaniculumWrapper.cssClass(component, "objectlink", "open")%>" value="" name="<%=JaniculumWrapper.event(component, "open")%>" type="submit" />
		</div>
		<div style="display:table-cell">
			<input id="<%=JaniculumWrapper.id(component, "reset")%>" class="<%=JaniculumWrapper.cssClass(component, "objectlink", "reset")%>" value="" name="<%=JaniculumWrapper.action(component, "reset")%>" type="submit" />
		</div>
	<% }%>
</div>
</div>
</div>                        

