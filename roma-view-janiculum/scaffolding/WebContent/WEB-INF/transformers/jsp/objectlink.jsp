<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);

%><div id="<%=JaniculumWrapper.id(component, null)%>" class="<%=JaniculumWrapper.cssClass(component, "objectlink", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>">
<table style="border-spacing:0;border-collapse:collapse">
<tr>
	<td>
		<input type="text" disabled="disabled" value="<%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%>"
		<%for(String event:JaniculumWrapper.availableEvents(component)){

		%>on<%=event%>="romaEvent('<%=JaniculumWrapper.fieldName(component)%>', '<%=event%>')"<%
} %> />
	</td>
	<%if(!JaniculumWrapper.disabled(component)){%>
		<td>
			<input id="<%=JaniculumWrapper.id(component, "open")%>" class="<%=JaniculumWrapper.cssClass(component, "objectlink", "open")%>" value="" name="<%=JaniculumWrapper.event(component, "open")%>" type="button"
			onclick="romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'open')"
			/>
		</td>
		<td>
			<input id="<%=JaniculumWrapper.id(component, "reset")%>" class="<%=JaniculumWrapper.cssClass(component, "objectlink", "reset")%>" value="" name="<%=JaniculumWrapper.action(component, "reset")%>" type="button"
			onclick="romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'reset')"/
			>
		</td>
	<% }%>
</tr>
</table>
</div>                        
