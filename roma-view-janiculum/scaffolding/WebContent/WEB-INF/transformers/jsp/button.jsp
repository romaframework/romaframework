<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%> 
<%@page import="java.util.Set"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%>
<%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%@page import="org.romaframework.core.Roma"%>
<%@page import="java.util.Map"%>
<%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	String img = Roma.view().getContextPath()+"/static/themes/default/image/basebutton.png"; 
	if(JaniculumWrapper.isField(component)){
		%><button id="<%=JaniculumWrapper.id(component, null)%>" type="button"  class="<%=JaniculumWrapper.cssClass(component,"button", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" name="<%=JaniculumWrapper.event(component, "change")%>"
		<%if(JaniculumWrapper.isDisabled(component)){%> disabled="disabled" <%} %>
		<%
		for(String event:JaniculumWrapper.getAvailableEvents(component)){%>
			on<%=event %>="romaEvent('<%=JaniculumWrapper.fieldName(component)%>', '<%=event%>')"
		<%} %> ><img class="<%=JaniculumWrapper.cssClass(component, "button", "icon")%>" src="<%=img%>" alt="<%=JaniculumWrapper.i18NLabel(component)%>"/><%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%>
		</button><%
	} 
	if(JaniculumWrapper.isAction(component)){
	%><button  class="<%=JaniculumWrapper.cssClass(component,"button", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component,null)%>" type="button" name="<%=JaniculumWrapper.actionName(component)%>"
		<%if(JaniculumWrapper.isDisabled(component)){%> disabled="disabled" <%} %>
		onclick="romaAction('<%=JaniculumWrapper.actionName(component)%>')"
		> <img class="<%=JaniculumWrapper.cssClass(component, "button", "icon")%>" src="<%=img%>" alt="<%=JaniculumWrapper.i18NLabel(component)%>"/><%=JaniculumWrapper.i18NLabel(component)%>
		</button><%
	} %>
