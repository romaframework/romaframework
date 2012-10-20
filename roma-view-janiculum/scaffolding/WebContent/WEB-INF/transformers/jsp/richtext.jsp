<%@ taglib uri="/WEB-INF/roma.tld" prefix="roma"%>
<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%><%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);

	if (!("raw".equals(part) || "label".equals(part))){   %>
	<div class="<%=JaniculumWrapper.cssClass(component, "richtext", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
		<textarea id="<%=JaniculumWrapper.id(component, "content")%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" 
		name="<%=JaniculumWrapper.fieldName(component)%>" <%=JaniculumWrapper.disabled(component)?"disabled=\"disabled\"":""%>
		<%
		boolean existsChangeEvent=false;
		for(String event: JaniculumWrapper.availableEvents(component)){
		%>
		on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>', '<%=event%>')"
		<%	if("change".equals(event)){
		existsChangeEvent=true;
			}%>
	<% 	} 
		if(existsChangeEvent){
		%>
		onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>')"
		<%} %>
		><%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%></textarea> 
		<%if(!JaniculumWrapper.isValid(component)){%>
			<span class="<%=JaniculumWrapper.cssClass(component, "richtext", "validation_message")%>"></span>	
		<%} %>
	</div><%

	}
	if("raw".equals(part)){
%><%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%><%} 
if("label".equals(part)){
%>
	<label id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "richtext", "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label>
<%} 
%>
<roma:addjs>
var ed = CKEDITOR.instances.<%=JaniculumWrapper.id(component, "content") %>;);
if(ed != undefined){
	CKEDITOR.remove(ed);
}
$('#<%=JaniculumWrapper.id(component, "content") %>').ckeditor();
ed = CKEDITOR.instances.<%=JaniculumWrapper.id(component, "content") %>;
ed.on('key', function ( e ) {
	var myTextField = document.getElementById('<%=JaniculumWrapper.id(component, "content") %>');
	myTextField.value = ed.getData();
	var fun = $('#<%=JaniculumWrapper.id(component, "content") %>').attr("onChange");
	eval(""+fun+";");
} );
</roma:addjs>