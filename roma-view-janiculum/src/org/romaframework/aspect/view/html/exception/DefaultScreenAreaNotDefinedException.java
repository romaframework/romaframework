package org.romaframework.aspect.view.html.exception;

public class DefaultScreenAreaNotDefinedException extends HtmlViewAspectRuntimeException {

	public DefaultScreenAreaNotDefinedException() {
		super("The screen must be defined with a body area!");
	}

}
