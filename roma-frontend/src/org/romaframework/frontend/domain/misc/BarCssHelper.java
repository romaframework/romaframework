package org.romaframework.frontend.domain.misc;

public class BarCssHelper {

	private final static String	URL							= "static/img/";

	private final static String	PROGRESSBAR			= "width:216px;height:41px;background:url(" + URL + "bg_bar.gif) no-repeat 0 0;position:relative;";

	private final static String	PROGRESSBARSPAN	= "position:absolute;display:block;width:200px;height:25px;background:url(" + URL
																									+ "bar.gif) no-repeat 0 0;top:8px;left:8px;overflow:hidden;text-indent:-8000px;";

	private final static String	PROGRESSBAREN1	= "position:absolute;display:block;width:200px;height:25px;background:url(" + URL + "bg_cover.gif) repeat-x 0 0;top:0;left:";

	private final static String	PROGRESSBAREN2	= "px;";

	private static String getCssProgressBarEn(int px) {
		return PROGRESSBAREN1 + px + PROGRESSBAREN2;
	}

	public static String genHtmlForProgressBar(float percent) {
		float size = 0;
		if (percent >= 0 && percent <= 100)
			size = 2 * percent;
		else if (percent > 100)
			size = 200;

		int px = Math.round(size);

		return "<div style='" + PROGRESSBAR + "'><span style='" + PROGRESSBARSPAN + "'><em style='" + getCssProgressBarEn(px) + "'>25%</em></span></div>";
	}

	public static String genHtmlForProgressBar(int num, int den) {
		float size = 0;
		if (den != 0)
			size = ((float) (num * 200) / (float) den);

		int px = Math.round(size);

		return "<div style='" + PROGRESSBAR + "'><span style='" + PROGRESSBARSPAN + "'><em style='" + getCssProgressBarEn(px) + "'>25%</em></span></div>";
	}

}
