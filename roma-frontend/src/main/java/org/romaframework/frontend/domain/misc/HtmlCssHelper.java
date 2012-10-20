package org.romaframework.frontend.domain.misc;

public class HtmlCssHelper {

	public final static String	AUTO								= "auto";
	public final static String	PX									= "px";

	public final static String	STD_BORDER					= "1px dashed #87B1D3";
	public final static String	STD_PADDING					= "3px";
	public final static String	STD_MARGIN					= "3px";
	public final static String	STD_BACKGROUNDCOLOR	= "transparent";

	public final static String	SEL_BORDER					= "1px dashed #bc2515";
	public final static String	SEL_PADDING					= "6px";
	public final static String	SEL_BACKGROUNDCOLOR	= "transparent";

	private final static String	HTML_IMG_OPEN				= "<img ";
	private final static String	HTML_CLOSE					= " />";

	private final static String	STYLE_OPEN					= "style=\"";
	private final static String	SRC_OPEN						= "src=\"";
	private final static String	CLOSE								= "\" ";

	private final static String	WIDTH								= "width:";
	private final static String	HEIGHT							= "height:";
	private final static String	BORDER							= "border:";
	private final static String	PADDING							= "padding:";
	private final static String	MARGIN							= "margin:";
	private final static String	BACKGROUND_COLOR		= "background-color:";
	private final static String	BACKGROUND_IMAGE		= "font:";
	private final static String	COLOR								= "color:";
	private final static String	FONT_WEIGHT					= "font-weight:";

	public static String getHtmlImg(String src, String width, String height) {
		return HTML_IMG_OPEN + addSrc(src) + addStyle(width, height) + HTML_CLOSE;
	}

	public static String getHtmlImg(String src, String width, String height, String border, String padding) {
		return HTML_IMG_OPEN + addSrc(src) + addStyle(width, height, border, padding) + HTML_CLOSE;
	}

	public static String getHtmlImg(String src, String width, String height, String border, String padding, String backgroundcolor) {
		return HTML_IMG_OPEN + addSrc(src) + addStyle(width, height, border, padding, backgroundcolor) + HTML_CLOSE;
	}

	public static String getHtmlImg(String src, String width, String height, String border, String padding, String margin, String backgroundcolor) {
		return HTML_IMG_OPEN + addSrc(src) + addStyle(width, height, border, padding, margin, backgroundcolor) + HTML_CLOSE;
	}

	public static String getHtmlDiv(String content, String backgroundimage, String color, String fontWeight) {
		return "<div " + addStyle(backgroundimage, color, fontWeight) + ">" + content + "</div>";
	}

	public static String addSrc(String src) {
		return SRC_OPEN + src + CLOSE;
	}

	public static String addStyle(String width, String height) {
		return STYLE_OPEN + getWidthStyle(width) + getHeightStyle(height) + CLOSE;
	}

	public static String addStyle(String backgroundimage, String color, String fontWeight) {
		return STYLE_OPEN + getBackgroundimageStyle(backgroundimage) + getColorStyle(color) + getFontWeightStyle(fontWeight) + CLOSE;
	}

	public static String addStyle(String width, String height, String border, String padding) {
		return STYLE_OPEN + getWidthStyle(width) + getHeightStyle(height) + getBorderStyle(border) + getPaddingStyle(padding) + CLOSE;
	}

	public static String addStyle(String width, String height, String border, String padding, String backgroundcolor) {
		return STYLE_OPEN + getWidthStyle(width) + getHeightStyle(height) + getBorderStyle(border) + getPaddingStyle(padding) + getBackgroundcolorStyle(backgroundcolor) + CLOSE;
	}

	public static String addStyle(String width, String height, String border, String padding, String margin, String backgroundcolor) {
		return STYLE_OPEN + getWidthStyle(width) + getHeightStyle(height) + getBorderStyle(border) + getPaddingStyle(padding) + getMarginStyle(margin)
				+ getBackgroundcolorStyle(backgroundcolor) + CLOSE;
	}

	protected static String getWidthStyle(String width) {
		return WIDTH + width + ";";
	}

	protected static String getHeightStyle(String height) {
		return HEIGHT + height + ";";
	}

	protected static String getBorderStyle(String border) {
		return BORDER + border + ";";
	}

	protected static String getPaddingStyle(String padding) {
		return PADDING + padding + ";";
	}

	protected static String getBackgroundcolorStyle(String backgroundcolor) {
		return BACKGROUND_COLOR + backgroundcolor + ";";
	}

	protected static String getMarginStyle(String margin) {
		return MARGIN + margin + ";";
	}

	protected static String getBackgroundimageStyle(String backgroundimage) {
		return BACKGROUND_IMAGE + backgroundimage + ";";
	}

	protected static String getColorStyle(String color) {
		return COLOR + color + ";";
	}

	protected static String getFontWeightStyle(String fontWeight) {
		return FONT_WEIGHT + fontWeight + ";";
	}

}
