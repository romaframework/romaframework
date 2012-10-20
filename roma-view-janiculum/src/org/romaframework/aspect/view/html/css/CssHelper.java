package org.romaframework.aspect.view.html.css;

import org.romaframework.aspect.view.area.AreaComponent;
import org.romaframework.aspect.view.html.area.HtmlViewAbstractAreaInstance;

/**
 * TODO:
 * 
 * @author Fabio Massimo Ercoli (fabio.ercoli@assetdata.it)
 * 
 */

public class CssHelper {

	public static final String	ROWITEM					= "rowitem";
	public static final String	OUTERDIV				= "outerdiv";
	public static final String	TABLE						= "table";
	public static final String	CLEAR						= "clear";

	public static final String	TOP_LEFT				= "top left";
	public static final String	TOP_CENTER			= "top center";
	public static final String	TOP_RIGHT				= "top right";
	public static final String	TOP_JUSTIFY			= "top justify";

	public static final String	CENTER_LEFT			= "center left";
	public static final String	CENTER_CENTER		= "center center";
	public static final String	CENTER_RIGHT		= "center right";
	public static final String	CENTER_JUSTIFY	= "center justify";

	public static final String	BOTTOM_LEFT			= "bottom left";
	public static final String	BOTTOM_CENTER		= "bottom center";
	public static final String	BOTTOM_RIGHT		= "bottom right";
	public static final String	BOTTOM_JUSTIFY	= "bottom justify";

	public static final String	TOP							= "top";
	public static final String	CENTER					= "center";
	public static final String	BOTTOM					= "bottom";

	public static final String	LEFT						= "left";
	public static final String	RIGHT						= "right";
	public static final String	JUSTIFY					= "justify";

	public static final String	AUTO						= "auto";
	public static final String	ZERO						= "0";

	public static void addCssAlignRule(final StyleBuffer style, final AreaComponent area) {

		final String align = area.getAreaAlign();

		if (align != null) {

			final String rowItemId = "#" + ((HtmlViewAbstractAreaInstance) area).getHtmlId() + "_" + CssHelper.ROWITEM;
			final String tableId = "#" + ((HtmlViewAbstractAreaInstance) area).getHtmlId();

			// RIGHT
			if (align.equals(RIGHT)) {
				if (!style.containsRule(rowItemId)) {
					style.createRules(rowItemId);
				}
				style.addRule(rowItemId, CssConstants.TEXT_ALIGN, "right");

				if (!style.containsRule(tableId)) {
					style.createRules(tableId);
				}
				style.addRule(tableId, CssConstants.MARGIN_LEFT, "auto");
				style.addRule(tableId, CssConstants.MARGIN_RIGHT, "0");
				style.addRule(tableId, CssConstants.TEXT_ALIGN, "left");
			}

			// JUSTIFY
			else if (align.equals(CssHelper.JUSTIFY)) {
				if (!style.containsRule(tableId)) {
					style.createRules(tableId);
				}
				style.addRule(tableId, CssConstants.WIDTH, CssConstants._100);
			}

			// top left
			else if (align.equals(CssHelper.TOP_LEFT) || align.equals(CssHelper.TOP)) {
				if (!style.containsRule(rowItemId)) {
					style.createRules(rowItemId);
				}
				style.addRule(rowItemId, "vertical-align", "top");
			}

			// top center
			else if (align.equals(CssHelper.TOP_CENTER)) {
				if (!style.containsRule(rowItemId)) {
					style.createRules(rowItemId);
				}
				style.addRule(rowItemId, "vertical-align", "top");
				style.addRule(rowItemId, CssConstants.TEXT_ALIGN, "center");

				if (!style.containsRule(tableId)) {
					style.createRules(tableId);
				}
				style.addRule(tableId, CssConstants.MARGIN_LEFT, "auto");
				style.addRule(tableId, CssConstants.MARGIN_RIGHT, "auto");
				style.addRule(tableId, CssConstants.TEXT_ALIGN, "left");
			}

			// top right
			else if (align.equals(CssHelper.TOP_RIGHT)) {
				if (!style.containsRule(rowItemId)) {
					style.createRules(rowItemId);
				}
				style.addRule(rowItemId, "vertical-align", "top");
				style.addRule(rowItemId, CssConstants.TEXT_ALIGN, "right");

				if (!style.containsRule(tableId)) {
					style.createRules(tableId);
				}
				style.addRule(tableId, CssConstants.MARGIN_LEFT, "auto");
				style.addRule(tableId, CssConstants.MARGIN_RIGHT, "0");
				style.addRule(tableId, CssConstants.TEXT_ALIGN, "left");
			}

			// top justify
			else if (align.equals(CssHelper.TOP_JUSTIFY)) {
				if (!style.containsRule(rowItemId)) {
					style.createRules(rowItemId);
				}
				style.addRule(rowItemId, "vertical-align", "top");

				if (!style.containsRule(tableId)) {
					style.createRules(tableId);
				}
				style.addRule(tableId, CssConstants.WIDTH, CssConstants._100);
			}

			// center left
			else if (align.equals(CssHelper.CENTER_LEFT) || align.equals(CssHelper.CENTER)) {
				if (!style.containsRule(rowItemId)) {
					style.createRules(rowItemId);
				}
				style.addRule(rowItemId, "vertical-align", "middle");
			}

			// center center
			else if (align.equals(CssHelper.CENTER_CENTER)) {
				if (!style.containsRule(rowItemId)) {
					style.createRules(rowItemId);
				}
				style.addRule(rowItemId, "vertical-align", "middle");
				style.addRule(rowItemId, CssConstants.TEXT_ALIGN, "center");

				if (!style.containsRule(tableId)) {
					style.createRules(tableId);
				}
				style.addRule(tableId, CssConstants.MARGIN_LEFT, "auto");
				style.addRule(tableId, CssConstants.MARGIN_RIGHT, "auto");
				style.addRule(tableId, CssConstants.TEXT_ALIGN, "left");
			}

			// center right
			else if (align.equals(CssHelper.CENTER_RIGHT)) {
				if (!style.containsRule(rowItemId)) {
					style.createRules(rowItemId);
				}
				style.addRule(rowItemId, "vertical-align", "middle");
				style.addRule(rowItemId, CssConstants.TEXT_ALIGN, "right");

				if (!style.containsRule(tableId)) {
					style.createRules(tableId);
				}
				style.addRule(tableId, CssConstants.MARGIN_LEFT, "auto");
				style.addRule(tableId, CssConstants.MARGIN_RIGHT, "0");
				style.addRule(tableId, CssConstants.TEXT_ALIGN, "left");
			}

			// center justify
			else if (align.equals(CssHelper.CENTER_JUSTIFY)) {
				if (!style.containsRule(rowItemId)) {
					style.createRules(rowItemId);
				}
				style.addRule(rowItemId, "vertical-align", "middle");

				if (!style.containsRule(tableId)) {
					style.createRules(tableId);
				}
				style.addRule(tableId, CssConstants.WIDTH, CssConstants._100);
			}

			// bottom left
			else if (align.equals(CssHelper.BOTTOM_LEFT) || align.equals(CssHelper.BOTTOM)) {
				if (!style.containsRule(rowItemId)) {
					style.createRules(rowItemId);
				}
				style.addRule(rowItemId, "vertical-align", "bottom");
			}

			// bottom center
			else if (align.equals(CssHelper.BOTTOM_CENTER)) {
				if (!style.containsRule(rowItemId)) {
					style.createRules(rowItemId);
				}
				style.addRule(rowItemId, "vertical-align", "bottom");
				style.addRule(rowItemId, CssConstants.TEXT_ALIGN, "center");

				if (!style.containsRule(tableId)) {
					style.createRules(tableId);
				}
				style.addRule(tableId, CssConstants.MARGIN_LEFT, "auto");
				style.addRule(tableId, CssConstants.MARGIN_RIGHT, "auto");
				style.addRule(tableId, CssConstants.TEXT_ALIGN, "left");
			}

			// bottom right
			else if (align.equals(CssHelper.BOTTOM_RIGHT)) {
				if (!style.containsRule(rowItemId)) {
					style.createRules(rowItemId);
				}
				style.addRule(rowItemId, "vertical-align", "bottom");
				style.addRule(rowItemId, CssConstants.TEXT_ALIGN, "right");

				if (!style.containsRule(tableId)) {
					style.createRules(tableId);
				}
				style.addRule(tableId, CssConstants.MARGIN_LEFT, "auto");
				style.addRule(tableId, CssConstants.MARGIN_RIGHT, "0");
				style.addRule(tableId, CssConstants.TEXT_ALIGN, "left");
			}

			// bottom justify
			else if (align.equals(CssHelper.BOTTOM_JUSTIFY)) {
				if (!style.containsRule(rowItemId)) {
					style.createRules(rowItemId);
				}
				style.addRule(rowItemId, "vertical-align", "bottom");

				if (!style.containsRule(tableId)) {
					style.createRules(tableId);
				}
				style.addRule(tableId, CssConstants.WIDTH, CssConstants._100);
			}
		}
	}

}
