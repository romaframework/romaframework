/*
 * Copyright 2006-2010 Luca Garulli (luca.garulli--at--assetdata.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */package org.romaframework.frontend.domain.page;

import java.util.ArrayList;
import java.util.List;

import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.core.Roma;

public abstract class PagePanelContainer {
	@ViewField(render = ViewConstants.RENDER_OBJECTEMBEDDED)
	protected List<PagePanel>	panels	= new ArrayList<PagePanel>();
	protected String					title;

	protected PagePanelContainer(final String iTitle) {
		title = iTitle;
	}

	public List<PagePanel> getPanels() {
		return panels;
	}

	@ViewField(render = ViewConstants.RENDER_LABEL, label = "")
	public String getTitle() {
		return title;
	}

	@SuppressWarnings("unchecked")
	public <RET extends PagePanel> RET getPanel(final Class<? extends PagePanel> iPanelClass) {
		for (PagePanel p : panels)
			if (p.getClass().equals(iPanelClass))
				return (RET) p;

		return null;
	}

	/**
	 * Add the panel only if not exists.
	 * 
	 * @param iPanel
	 *          Panel instance to add
	 * @return
	 */
	public boolean addPanel(final PagePanel iPanel) {
		if (panels.indexOf(iPanel) > -1)
			return false;

		panels.add(iPanel);
		Roma.objectChanged(this);

		return true;
	}
}
