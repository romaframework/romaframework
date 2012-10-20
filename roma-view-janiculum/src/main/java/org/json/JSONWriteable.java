package org.json;

import java.io.Writer;

public interface JSONWriteable {

	public Writer write(Writer writer) throws JSONException;
	
}
