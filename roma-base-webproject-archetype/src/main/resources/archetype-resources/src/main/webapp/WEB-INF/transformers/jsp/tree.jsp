#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%@ taglib uri="/WEB-INF/roma.tld" prefix="roma"%>
<%@page import="org.romaframework.aspect.view.html.component.HtmlViewContentComponent"%>
<%@page import="java.util.Collection"%>
<%@page import="org.romaframework.aspect.view.html.component.HtmlViewGenericComponent"%>
<%@page import="org.romaframework.aspect.view.html.constants.TransformerConstants"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.directive.JspTransformerHelper"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%>
<%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%>
<%
	
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
	
	String pId = JaniculumWrapper.id(component, null);
	int index = 0;
%>
<div id="<%=JaniculumWrapper.id(component, null)%>"	class="<%=JaniculumWrapper.cssClass(component, "tree", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" inited="false">
  <div id="<%=pId%>">
	  <ul>
	  <%index = tree(component, JaniculumWrapper.getChildren(component), "open", out, index); %>
    </ul>			
  </div>	
  <input id="<%=JaniculumWrapper.id(component, "hidden")%>" class="<%=JaniculumWrapper.cssClass(component, "tree", null)%>" style="<%=JaniculumWrapper.inlineStyle(component, null)%>" type="hidden" name="<%=JaniculumWrapper.fieldName(component)%>"  />
</div>
<%!

private int tree(HtmlViewRenderable comp, Collection<?> children, String cssClass, JspWriter out, int index){
	try{
		if(children.size()==0){
			out.print("<li id=${symbol_escape}""+comp.getHtmlId()+"_content${symbol_escape}">");
			out.print("<a ");
			if(JaniculumWrapper.isSelected(comp, index)){
				out.print("class=${symbol_escape}"clicked${symbol_escape}" ");
			}
			HtmlViewContentComponent component = (HtmlViewContentComponent)comp;
			out.print("idx=${symbol_escape}""+index+"${symbol_escape}" >"+component.getContent()==null?"":component.getContent()+"</a></li>");
			index++;
		}else{
			HtmlViewContentComponent component = (HtmlViewContentComponent)comp;
			out.print("<li id=${symbol_escape}""+comp.getHtmlId()+"_content${symbol_escape}" class=${symbol_escape}""+cssClass+"${symbol_escape}"><a ");
			if(JaniculumWrapper.isSelected(component, index)){
				out.print("class=${symbol_escape}"clicked${symbol_escape}" ");
			}
			out.print("idx=${symbol_escape}""+index+"${symbol_escape}" >"+component.getContent()==null?"":component.getContent()+"</a></li>");
			index++;
			out.print("<ul>");
			for(Object c:children){
				HtmlViewContentComponent child = (HtmlViewContentComponent)c;
				index = tree((HtmlViewRenderable)child, child.getChildren(), "open", out, index);
			}
			out.print("</ul>");
			out.print("</li>");
		}
	}catch(Exception e){}
	return index;
}
%>
<roma:addjs>
jQuery("${symbol_pound}<%=pId%>").tree({
	callback : {
		onselect : function() {
			jQuery("${symbol_pound}<%=JaniculumWrapper.id(component, "hidden") %>").attr('value', ${symbol_dollar}.tree_reference('<%=pId %>').selected.find("a").attr("idx"));
			romaFieldChanged('<%=JaniculumWrapper.fieldName(component) %>');
			romaSendAjaxRequest();
		}
	}
});
if (${symbol_dollar}.tree_reference("<%=pId %>") != null) {");
	buffer.append("${symbol_dollar}.tree_reference("<%=pId%>").open_all();
}
%>
</roma:addjs>