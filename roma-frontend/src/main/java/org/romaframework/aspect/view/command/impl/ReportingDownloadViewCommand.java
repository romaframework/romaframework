package org.romaframework.aspect.view.command.impl;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

import org.romaframework.aspect.view.command.ViewCommand;
import org.romaframework.core.Roma;

public class ReportingDownloadViewCommand implements ViewCommand {

	private String	fileName;
	private String	contentType;
	private Object	toRender;

	public ReportingDownloadViewCommand(String fileName, String contentType, Object toRender) {
		super();
		this.fileName = fileName;
		this.contentType = contentType;
		this.toRender = toRender;
		if (this.toRender == null)
			throw new NullPointerException();
	}

	public String getFileName() {
		return fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public Object getToRender() {
		return toRender;
	}

	public void write(OutputStream outputStream) {
		if (getToRender() instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) getToRender();
			if (!collection.isEmpty()) {
				Iterator<?> it = collection.iterator();
				Object o = it.next();
				Roma.reporting().renderCollection((Collection<?>) getToRender(), getContentType(), Roma.session().getSchemaObject(o), outputStream);
			}
		} else {
			Roma.reporting().render(getToRender(), getContentType(), Roma.session().getSchemaObject(getToRender()), outputStream);
		}
	}

}
