package org.romaframework.aspect.view.html;

import java.util.HashMap;
import java.util.Map;

import org.romaframework.aspect.view.html.area.HtmlViewRenderable;
import org.romaframework.aspect.view.html.css.StyleBuffer;
import org.romaframework.aspect.view.html.util.SemiWeakHashMap;
import org.romaframework.core.flow.ThreadLocalContext;

public class HtmlViewSession {

	protected ThreadLocalContext	threadContext	= new ThreadLocalContext();

	public HtmlViewSession() {
	}

	/**
	 * The counter used for the generation of the bindings id's
	 */
	protected Long													counter							= 0l;

	protected Map<Long, HtmlViewRenderable>	idRenderableBinding	= new HashMap<Long, HtmlViewRenderable>();
	protected Map<HtmlViewRenderable, Long>	renderableIdBinding	= new HashMap<HtmlViewRenderable, Long>();

	protected Map<String, StyleBuffer>			cssBuffers					= new SemiWeakHashMap<String, StyleBuffer>(3);

	public void addContextParameter(final String key, final Object value) {
		threadContext.get().put(key, value);
	}

	public Object getContextParameter(final String key) {
		return threadContext.get().get(key);
	}

	public void removeContextParameter(final String key) {
		threadContext.get().remove(key);
	}

	public HtmlViewRenderable getRenderableById(final Long iId) {
		return idRenderableBinding.get(iId);
	}

	public long addRenderableBinding(final HtmlViewRenderable iRenderable) {
		final Long id = renderableIdBinding.get(iRenderable);

		if (id != null) {
			return id;
		}

		counter = counter + 1;
		idRenderableBinding.put(counter, iRenderable);
		renderableIdBinding.put(iRenderable, counter);
		return counter;
	}

	public void removeRenderableBinding(final HtmlViewRenderable iRenderable) {
		final Long id = renderableIdBinding.get(iRenderable);

		if (id != null) {
			renderableIdBinding.remove(iRenderable);
			idRenderableBinding.remove(id);
		} else {
			return;
		}
	}

	public StyleBuffer getCssBuffer(final String id) {
		StyleBuffer buffer = cssBuffers.get(id);
		if (buffer == null) {
			buffer = new StyleBuffer();
			cssBuffers.put(id, buffer);
		}
		return buffer;
	}

	public void removeCssBuffer(final String id) {
		if (cssBuffers.containsKey(id)) {
			cssBuffers.remove(id);
		}
	}

}
