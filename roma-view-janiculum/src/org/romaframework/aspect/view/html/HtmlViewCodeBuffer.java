/*
 * Copyright 2006-2007 Giordano Maestro (giordano.maestro--at--assetdata.it)
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
 */
package org.romaframework.aspect.view.html;

import java.io.Writer;

public interface HtmlViewCodeBuffer {

//	/**
//	 * appends a string to the end of the buffer
//	 * 
//	 * @param string
//	 */
//	public abstract void append(String string);

	public abstract void setScript(String id, String code);
	
	public abstract void removeScript(String id);
	
	public abstract String getCodeById(String id);
	
	/**
	 * waits for the buffer to be closed and then writes its content on the writer
	 * 
	 * @param writer
	 *          the writer where the buffer content has to be written
	 */
	public abstract void write(Writer writer);

	/**
	 * closes the buffer
	 */
	public abstract void close();

	/**
	 * cleans and re-opens the buffer
	 */
	public abstract void clean();

	/**
	 * returns true if the buffer is closed
	 * 
	 * @return true if the buffer is closed
	 */
	public abstract boolean isClosed();

	/**
	 * returns a copy of the current buffer content. Do not use this method for synchronized output, use {@link #write(Writer)}
	 * instead
	 * 
	 * @return a copy of the current buffer content
	 */
	public abstract String getBufferContent();

	public void open();
}