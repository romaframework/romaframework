<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/roma.tld" prefix="roma"%>
<%@page import="org.romaframework.aspect.view.form.ViewComponent"%>
<%@page import="org.romaframework.aspect.view.html.HtmlViewAspectHelper"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.romaframework.aspect.view.html.css.StyleBuffer"%>
<%@page import="org.romaframework.aspect.view.html.transformer.helper.TransformerHelper"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.core.Roma"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewScreenPopupAreaInstance"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
%>
<div class="<%=JaniculumWrapper.cssClass(component, "popup", null)%>" id="<%=JaniculumWrapper.id(component, null)%>">
<% ((HtmlViewScreenPopupAreaInstance)component).getForm().render(pageContext.getOut()); %>
</div>
<roma:addjs>
$('#<%=JaniculumWrapper.id(component, null)%>[toRemove="toRemove"]').remove();
var size = $('#<%=JaniculumWrapper.id(component, null)%> > div').width();
$('#<%=JaniculumWrapper.id(component, null)%>').dialog({
	modal:true,
	close:function (){ romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'ClosePopup');},
	autoOpen: true,
	width: size,
	title: '<%=JaniculumWrapper.i18NObjectLabel(((HtmlViewScreenPopupAreaInstance)component).getForm())%>'
});
romaAddRemove('<%=JaniculumWrapper.id(component, null)%>',function(){
	$('#<%=JaniculumWrapper.id(component, null)%>').dialog('destroy');
	$('#<%=JaniculumWrapper.id(component, null)%>').attr('toRemove','toRemove');
});
</roma:addjs>