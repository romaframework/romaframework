<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%>
<%@page import="java.util.Map"%><%
	//
	//HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	//String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	String part = (String) request.getAttribute(RequestConstants.CURRENT_COMPONENT_PART_IN_TRANSFORMER);
	//pageContext.setAttribute("part", part);
	//String codeToPrint = (String) ctx.get(JspTransformer.CODE_TO_PRINT);
	//pageContext.setAttribute("codeToPrint", codeToPrint);
	
	//if("html".equals(codeToPrint)){
		if(part==null || "".equals(part)|| "all".equals(part)){
%>
<div class="<%=JaniculumWrapper.cssClass(component, "html", null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
<%=JaniculumWrapper.content(component)==null?"":JaniculumWrapper.content(component)%>
</div>
<%		} else if("content".equals(part) || "raw".equals(part)){
			%><%=JaniculumWrapper.content(component)==null?"":JaniculumWrapper.content(component)%><%
		}else if("label".equals(part)){
			%><label id="<%=JaniculumWrapper.id(component, "label")%>" class="<%=JaniculumWrapper.cssClass(component, "html", "label")%>" for="<%=JaniculumWrapper.id(component, "content")%>"><%=JaniculumWrapper.i18NLabel(component)%></label><%
		}
	//}
%>