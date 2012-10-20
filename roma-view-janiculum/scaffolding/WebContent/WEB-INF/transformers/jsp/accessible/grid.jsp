<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%><%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%><%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	pageContext.setAttribute("part", part);

%>

<div id="<%=JaniculumWrapper.id(component, null)%>" class="<%=JaniculumWrapper.cssClass(component, "grid", null)%>" style="display:table; <%=JaniculumWrapper.inlineStyle(component, null)%> ">
<%
int row = -1;
int col = 0;
for(Object c:JaniculumWrapper.getChildren(component)){
	HtmlViewRenderable child =(HtmlViewRenderable)c;
	if(col%JaniculumWrapper.areaSize(component)==0){

%>
<div style="display: table-row;">
<%		row++;
	}
%>
<div style="display: table-cell;" id="<%=JaniculumWrapper.id(component, null)%>_<%=row%>_<%=col%>" class="row_<%=row%> col_<%=col%> <%=JaniculumWrapper.cssClass(child, null, null)%>"><% JspTransformerHelper.delegate(child, null,pageContext.getOut());%></div>
<%
	if(col%JaniculumWrapper.areaSize(component)==JaniculumWrapper.areaSize(component)-1){
%>
</div>
<%
	col = 0;
	}else{
		col++;
	}
}
if(col%JaniculumWrapper.areaSize(component)!=0){
	for(int i=0; i<JaniculumWrapper.areaSize(component) - (col%JaniculumWrapper.areaSize(component))-1; i++){
		%><div style="display: table-cell;"></div><%
	}
	%></div><%
}
%>
</div>