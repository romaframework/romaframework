package org.romaframework.aspect.view.html;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * this class provides a synchronized buffer where you can append string content. You can send the buffer content to a writer with
 * the {@link #write(Writer)} method; this method waits for the buffer to be closed to flush its content to the output writer.
 * 
 * @author Luigi Dell'Aquila
 * 
 */
public class SynchroBuffer implements HtmlViewCodeBuffer {
	
	protected Map<String , String> code = new HashMap<String, String>();
	
	protected StringBuffer	buffer	= new StringBuffer();
	private boolean					closed	= false;

	public SynchroBuffer() {
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.romaframework.aspect.view.html.HtmlViewCodeBuffer#append(java.lang.String)
//	 */
//	public synchronized void append(final String string) {
//		if (closed) {
//			throw new IllegalStateException("buffer closed");
//		}
//		if (buffer == null) {
//			buffer = new StringBuffer();
//		}
//		buffer.append(string);
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.view.html.HtmlViewCodeBuffer#write(java.io.Writer)
	 */
	public synchronized void write(final Writer writer) {
		if (buffer == null) {
			return;
		}
		while (!closed) {
			try {
				wait();
			} catch (final Exception e) {
				e.printStackTrace();// TODO
			}
		}
		try {
			writer.write(getBufferContent());
		} catch (final Exception e) {
			e.printStackTrace();// TODO
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.view.html.HtmlViewCodeBuffer#close()
	 */
	public synchronized void close() {
		closed = true;
		notifyAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.view.html.HtmlViewCodeBuffer#clean()
	 */
	public synchronized void clean() {
		closed = false;
		buffer = new StringBuffer();
		code.clear();
		notifyAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.view.html.HtmlViewCodeBuffer#isClosed()
	 */
	public synchronized boolean isClosed() {
		return closed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.view.html.HtmlViewCodeBuffer#getBufferContent()
	 */
	public String getBufferContent() {
		StringBuffer result = new StringBuffer();
		if(buffer!=null){
			result.append(buffer);
		}
		for(String snippet:code.values()){
			result.append(snippet);
		}
		return result.toString();
	}
	
	

	public synchronized void removeScript(String id) {
		if (closed) {
			throw new IllegalStateException("buffer closed");
		}
		this.code.remove(id);
	}

	public synchronized void setScript(String id, String code) {
		if (closed) {
			throw new IllegalStateException("buffer closed");
		}
		this.code.put(id, code);
	}

	public String getCodeById(String id) {
		return code.get(id);
	}
	
	public synchronized void open(){
		this.closed = false;
	}
}
