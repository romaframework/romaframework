package org.romaframework.aspect.view.html.area.component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.view.ViewAspect;
import org.romaframework.aspect.view.area.AreaComponent;
import org.romaframework.aspect.view.form.ContentForm;
import org.romaframework.aspect.view.form.ViewComponent;
import org.romaframework.aspect.view.html.HtmlViewAspect;
import org.romaframework.aspect.view.html.HtmlViewAspectHelper;
import org.romaframework.aspect.view.html.area.HtmlViewFormArea;
import org.romaframework.aspect.view.html.component.HtmlViewContentComponent;
import org.romaframework.aspect.view.html.component.HtmlViewContentForm;
import org.romaframework.aspect.view.html.component.HtmlViewGenericComponent;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.type.Pair;

public class ChildrenMap {

	protected Log																													log				= LogFactory.getLog(this.getClass());

	protected Map<String, Pair<AreaComponent, HtmlViewGenericComponent>>	children	= new LinkedHashMap<String, Pair<AreaComponent, HtmlViewGenericComponent>>();

	public void addChild(final String fieldName, final AreaComponent iAreaComponent, final HtmlViewGenericComponent iComponent) {
		final Pair<AreaComponent, HtmlViewGenericComponent> values = children.get(fieldName);
		if (values == null) {
			children.put(fieldName, new Pair<AreaComponent, HtmlViewGenericComponent>(iAreaComponent, iComponent));
		} else {
			replaceChild(values.getValue(), iComponent, values.getKey());
			values.setValue(iComponent);

		}
	}

	private void replaceChild(HtmlViewGenericComponent oldComponent, HtmlViewGenericComponent newComponent, AreaComponent areaComponent) {
		if (newComponent == null) {
			oldComponent.clearComponents();
		}
		if (oldComponent instanceof HtmlViewContentForm) {
			if (newComponent == null && oldComponent instanceof ContentForm) {
				((HtmlViewAspect) Roma.aspect(ViewAspect.class)).releaseForm((ContentForm) oldComponent);
			} else {
				((HtmlViewAspect) Roma.aspect(ViewAspect.class)).removeObjectFormAssociation(oldComponent, null);
			}
			((HtmlViewContentForm) oldComponent).clearAreas();
		} else if (oldComponent instanceof HtmlViewContentComponent) {
			((HtmlViewAspect) Roma.aspect(ViewAspect.class)).removeObjectFormAssociation(oldComponent, null);
		}

		HtmlViewAspectHelper.getHtmlViewSession().removeRenderableBinding(oldComponent);
		if (areaComponent != null && areaComponent instanceof HtmlViewFormArea) {
			((HtmlViewFormArea) areaComponent).replaceComponent(oldComponent, newComponent);
		}
	}

	public void remove(final String fieldName) {
		final Pair<AreaComponent, HtmlViewGenericComponent> values = children.get(fieldName);
		if (values != null) {
			if (values.getValue() != null) {
				if (Roma.session().getActiveSystemSession() != null)
					HtmlViewAspectHelper.getHtmlViewSession().removeRenderableBinding(values.getValue());
			}
			children.put(fieldName, null);
			if (values.getValue() instanceof HtmlViewContentForm) {
				((HtmlViewAspect) Roma.aspect(ViewAspect.class)).releaseForm((ContentForm) values.getValue());
				((HtmlViewContentForm) values.getValue()).clearAreas();
			} else {
				removeComponentFromArea(values);
			}
		}
	}

	/**
	 * Clear the children map
	 */
	public void clear() {
		// Remove from any area the component previously added
		for (final Pair<AreaComponent, HtmlViewGenericComponent> pair : children.values()) {
			removeComponentFromArea(pair);
		}

		// remove children components
		for (final String key : children.keySet()) {
			remove(key);
		}
	}

	private void removeComponentFromArea(final Pair<AreaComponent, HtmlViewGenericComponent> pair) {
		if (pair == null) {
			return;
		}
		final AreaComponent area = pair.getKey();
		if (area != null && area instanceof HtmlViewFormArea) {
			final HtmlViewFormArea formArea = (HtmlViewFormArea) area;
			final ViewComponent value = pair.getValue();
			if (value instanceof HtmlViewGenericComponent) {
				((HtmlViewGenericComponent) value).clearComponents();
			}
			if (log.isDebugEnabled())
				log.debug("Removing component " + value.getContent() + " from area " + area.getName());
			final boolean res = formArea.removeComponent(value);
			if (log.isDebugEnabled())
				log.debug("Removed component " + value.getContent() + " from area " + area.getName() + "  result: " + res);
		}
	}

	public HtmlViewGenericComponent getChild(final String fieldName) {
		final Pair<AreaComponent, HtmlViewGenericComponent> values = children.get(fieldName);
		if (values == null) {
			return null;
		} else {
			return values.getValue();
		}
	}

	public Collection<HtmlViewGenericComponent> getChildren() {
		final Collection<HtmlViewGenericComponent> result = new LinkedList<HtmlViewGenericComponent>();
		for (final String fieldName : children.keySet()) {
			final HtmlViewGenericComponent component = getChild(fieldName);
			if (component == null) {
				continue;
			}
			result.add(component);
		}
		return result;
	}

}
