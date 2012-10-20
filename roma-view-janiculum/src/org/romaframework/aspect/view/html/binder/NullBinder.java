package org.romaframework.aspect.view.html.binder;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.view.html.area.HtmlViewBinder;
import org.romaframework.aspect.view.html.area.HtmlViewRenderable;

public final class NullBinder implements HtmlViewBinder {

	private static NullBinder	instance	= new NullBinder();
	private static Log				log				= LogFactory.getLog(NullBinder.class);

	private NullBinder() {
	}

	public static NullBinder getInstance() {
		return NullBinder.instance;
	}

	public void bind(final HtmlViewRenderable renderable, final Map<String, Object> values) {
		if (log.isDebugEnabled())
			log.debug("null debugger invoked on " + renderable);
	}

}
