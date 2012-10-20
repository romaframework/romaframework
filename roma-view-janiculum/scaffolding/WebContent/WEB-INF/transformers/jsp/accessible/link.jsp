<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);

%> <div class="<%=JaniculumWrapper.cssClass(component,"link", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">

<%if(JaniculumWrapper.isField(component)){%>
	<button class="<%=JaniculumWrapper.cssClass(component, "link", "content")%>" id="<%=JaniculumWrapper.id(component, "content")%>" type="submit" value="<%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%>" name="<%=JaniculumWrapper.event(component, "change")%>"
	<%if(JaniculumWrapper.isDisabled(component)){%> disabled="disabled" <%} %> > <%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%>
	</button>
<%} 
if(JaniculumWrapper.isAction(component)){%>
	<button class="<%=JaniculumWrapper.cssClass(component, "link", "content")%>" id="<%=JaniculumWrapper.id(component,"content")%>" type="submit" value="<%=JaniculumWrapper.i18NLabel(component)%>" name="<%=JaniculumWrapper.actionName(component)%>"
	<%if(JaniculumWrapper.isDisabled(component)){%> disabled="disabled" <%} %>
	>
	<%=JaniculumWrapper.i18NLabel(component)%>
	</button>
<%} %>
</div>

