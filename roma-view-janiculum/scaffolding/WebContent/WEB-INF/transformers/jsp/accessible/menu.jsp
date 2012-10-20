<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
%>
<div class="<%=JaniculumWrapper.cssClass(component, "menu", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>" inited="false">
	<%if(JaniculumWrapper.isAction(component)){%>
		<a class="<%=JaniculumWrapper.cssClass(component, "menu", "content")%>" id="<%=JaniculumWrapper.id(component, "content")%>" value="<%=JaniculumWrapper.i18NLabel(component)%>" href="javascript:void(0)" title="<%=JaniculumWrapper.i18NHint(component)%>"
		<%=JaniculumWrapper.disabled(component)?"disabled=\"disabled\"":""%>
			onclick="romaAction('<%=JaniculumWrapper.actionName(component)%>')">
			<label for="<%=JaniculumWrapper.id(component, "content")%>" id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "menu", "label")%>">
			<%=JaniculumWrapper.i18NLabel(component)%>
			</label> 
		</a>
	<%}else{ %>
			<label for="<%=JaniculumWrapper.id(component, "content")%>" id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "menu", "label")%>">
			<%=JaniculumWrapper.i18NLabel(component)%>
			</label> 
	<%} %>
	
	<%if(JaniculumWrapper.haveChildren(component)){%>
	<ul class="<%=JaniculumWrapper.cssClass(component, "menu", "content")%>"  id="<%=JaniculumWrapper.id(component, "content")%>">
		<%for(Object child:JaniculumWrapper.getChildren(component)){%>
			<li class="<%=JaniculumWrapper.cssClass(component, "menu", "content")%>">
					<% JspTransformerHelper.delegate((HtmlViewRenderable)child, null,pageContext.getOut()); %>							
			</li>			
		<%} %>
	</ul>
	<%} %>
</div>
%>