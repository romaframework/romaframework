<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
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
%>
<%if (!("raw".equals(part) || "label".equals(part))){   %>
	<div class="<%=JaniculumWrapper.cssClass(component, "decimal", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
	<%if(part==null || "".equals(part) || "all".equals(part)){ %>
		<input id="<%=JaniculumWrapper.id(component, "content")%>" class="<%=JaniculumWrapper.cssClass(component, "decimal", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" type="text" 
		name="<%=JaniculumWrapper.fieldName(component)%>" value="<%=JaniculumWrapper.formatNumberContent(component)==null?"":JaniculumWrapper.formatNumberContent(component)%>" <%=JaniculumWrapper.disabled(component)?" disabled=\"disabled\"":""%> 
		<%
		boolean existsChangeEvent=false;
		for(String event:JaniculumWrapper.availableEvents(component)){
			if(!"change".equals(event)){
		%>
		on<%=event%>="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>', '<%=event%>')"
		<%}else{ %>
			existsChangeEvent=true;
		
		<%
		}
	}
		if(existsChangeEvent){
	%>
		
		onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>'); romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'change')"
		<%}else{ %>
		onchange="romaFieldChanged('<%=JaniculumWrapper.fieldName(component)%>')"
		<%} %>
		/>
		<%if(!JaniculumWrapper.isValid(component)){%>
			<span class="<%=JaniculumWrapper.cssClass(component, "decimal", "validation_message")%>"></span>	
		<%} %>
	<%} %>
	</div>
<%}else if("raw".equals(part)){%><%=JaniculumWrapper.content(component, true)==null?"":JaniculumWrapper.content(component, true)%><%}
else if("label".equals(part)){ %>
	<label id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "decimal", "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label>
<%}
StringBuffer buffer = new StringBuffer();
buffer.append("jQuery(\"#"+JaniculumWrapper.id(component, "content")+"\").keyup(function(component){");
buffer.append("re = /[^0-9\\.,]/ ;");
buffer.append("re2 = /[^0-9\\-]/ ;");
buffer.append("var beginning = jQuery(this).attr(\"value\").substring(0,1);");
buffer.append("while(beginning.match(re2)){");
buffer.append("beginning = beginning.replace(re2, \"\");");
buffer.append("}");
buffer.append("var end = jQuery(this).attr(\"value\").substring(1);");
buffer.append("while(end.match(re)){");
buffer.append("end = end.replace(re, \"\");");
buffer.append("}");
buffer.append("jQuery(this).attr(\"value\", beginning+end);");
buffer.append("}).keyup();");
JspTransformerHelper.addJs(JaniculumWrapper.id(component, TransformerConstants.PART_ALL), buffer.toString());

%>
