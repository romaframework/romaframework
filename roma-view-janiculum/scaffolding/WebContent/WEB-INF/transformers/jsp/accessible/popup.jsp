<%@page import="org.romaframework.aspect.view.form.ViewComponent"%>
<%@page import="org.romaframework.aspect.view.html.HtmlViewAspectHelper"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.romaframework.aspect.view.html.css.StyleBuffer"%>
<%@page import="org.romaframework.aspect.view.html.transformer.helper.TransformerHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewRenderable"%>
<%@page import="org.romaframework.core.Roma"%>
<%@page import="org.romaframework.aspect.view.html.area.HtmlViewScreenPopupAreaInstance"%>
<%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Set"%><%@page import="org.romaframework.aspect.view.html.transformer.jsp.JspTransformer"%><%@page import="org.romaframework.aspect.view.html.transformer.helper.JaniculumWrapper"%><%@page import="org.romaframework.aspect.view.html.constants.RequestConstants"%><%@page import="java.util.Map"%><%
	HtmlViewRenderable component = (HtmlViewRenderable)request.getAttribute(RequestConstants.CURRENT_COMPONENT_IN_TRANSFORMER);
%><table cellpadding="0" cellspacing="0" class="jqDnr <%=JaniculumWrapper.cssClass(component, "popup", null)%>" id="<%=JaniculumWrapper.id(component, null)%>"> 
<tr class="popup_row_1"><td class="popup_col_1"></td><td class="popup_col_2"></td><td class="popup_col_3"></td></tr>
<tr class="popup_row_2"><td class="popup_col_1 popup_left_border"></td><td class="popup_col_2"><div class="header jqHandle">
<input class="<%=JaniculumWrapper.cssClass(component, "popup", "close")%>" id="<%=JaniculumWrapper.id(component, "close")%>" type="button" value="" onclick="romaEvent('<%=JaniculumWrapper.fieldName(component)%>', 'ClosePopup')"/>
<div class="jqDrag"><%=JaniculumWrapper.i18NObjectLabel(((HtmlViewScreenPopupAreaInstance)component).getForm())%></div></div><%
HtmlViewAspectHelper.renderByJsp(((HtmlViewScreenPopupAreaInstance)component).getForm(), request, pageContext.getOut());
%></td><td class="popup_col_3 popup_right_border"></td></tr>
<tr class="popup_row_3"><td class="popup_col_1"></td><td class="popup_col_2"></td><td class="popup_col_3 jqResize"></td></tr></table>
<%
	final StyleBuffer style = HtmlViewAspectHelper.getCssBuffer();
	if (!style.containsRule("#screen_main_popups")) {
		style.createRules("#screen_main_popups");
	}
	style.addRule("#screen_main_popups", "background-repeat", "repeat-x");
	style.addRule("#screen_main_popups", "position", "fixed");
	style.addRule("#screen_main_popups", "top", "0");
	style.addRule("#screen_main_popups", "bottom", "0");
	style.addRule("#screen_main_popups", "left", "0");
	style.addRule("#screen_main_popups", "z-index", "1000");
	style.addRule("#screen_main_popups", "width", "100%");
	style.addRule("#screen_main_popups", "height", "100%");
	style.addRule("#screen_main_popups", "background-image", "url(/"+Roma.view().getContextPath() + "/static/themes/default/image/test01.png)");
%>