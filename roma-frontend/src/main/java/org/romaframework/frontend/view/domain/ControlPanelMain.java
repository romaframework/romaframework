package org.romaframework.frontend.view.domain;

import org.romaframework.aspect.security.annotation.SecurityClass;
import org.romaframework.frontend.domain.page.ContainerPage;

@SecurityClass(readRoles = "profile:Administrator")
public class ControlPanelMain extends ContainerPage {

	public ControlPanelMain() {
	}

}
