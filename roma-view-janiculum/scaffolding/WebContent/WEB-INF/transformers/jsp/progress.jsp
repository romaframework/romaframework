<%@ taglib uri="/WEB-INF/roma.tld" prefix="roma"%>
<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%><%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);

	if (!("raw".equals(part) || "label".equals(part))){   %>
	<div class="<%=JaniculumWrapper.cssClass(component, "progress", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%> width:400px;" id="<%=JaniculumWrapper.id(component, null)%>">
	<%if(part==null || "".equals(part) || "all".equals(part)){ %>
	<table>
		<tr>
			<td id="<%=JaniculumWrapper.id(component, "content")%>" />
			<td><%=JaniculumWrapper.content(component, true)==null?"0":JaniculumWrapper.content(component, true)%>%</td>
		</tr>
	</table>
	<%
	} %>
	</div><%
	}
if("raw".equals(part)){
%><%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%><%
}else if("label".equals(part)){
%><label id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "progress", "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label>
<%
}
%>
<roma:addjs>
$(function() {
	$("#<%= JaniculumWrapper.id(component, "content")%>).progressbar(component, {
		value: parseFloat(String("<%=(JaniculumWrapper.content(component, true)==null ? "0" : JaniculumWrapper.content(component, true)) %>").replace(component, ',','.'))
	});
});
</roma:addjs>