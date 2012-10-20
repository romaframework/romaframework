package org.romaframework.aspect.view.html.css;

import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * TODO:
 * 
 * @author Fabio Massimo Ercoli (fabio.ercoli@assetdata.it)
 * 
 */

public class StyleBuffer {

	private final Map<String, Map<String, String>>	styles	= new LinkedHashMap<String, Map<String, String>>();

	private boolean																	closed	= false;
	
	private boolean changed = false;

	public synchronized boolean isClosed() {
		return closed;
	}

	public synchronized void close() {
		closed = true;
		notifyAll();
	}

	public synchronized String createRules(final String key) {
		if (closed) {
			throw new IllegalStateException("buffer closed");
		}
		changed = true;
		final Map<String, String> rules = new HashMap<String, String>();
		styles.put(key, rules);
		return key;
	}

	public synchronized void deleteRules(final String key) {
		if (closed) {
			throw new IllegalStateException("buffer closed");
		}
		
		if (styles.containsKey(key)) {
			changed = true;
			styles.remove(key);
		}
	}

	public synchronized void addRule(final String key, final String rulekey, final String rule) {
		if (closed) {
			throw new IllegalStateException("buffer closed");
		}
		if (styles.containsKey(key)) {
			final Map<String, String> rules = styles.get(key);
			if(rules.get(rulekey)==null || !(rules.get(rulekey).equals(rule))){
				changed = true;
				rules.put(rulekey, rule);
			}
		}
	}

	public synchronized void removeRule(final String key, final String rulekey) {
		if (closed) {
			throw new IllegalStateException("buffer closed");
		}
		changed = true;
		if (styles.containsKey(key)) {
			final Map<String, String> rules = styles.get(key);
			if (rules.containsKey(rulekey)) {
				rules.remove(rulekey);
			}
		}
	}
	
	public synchronized void removeRule(final String key) {
		if (closed) {
			throw new IllegalStateException("buffer closed");
		}
		changed = true;
		if (styles.containsKey(key)) {
			styles.remove(key);
		}
	}

	public synchronized boolean containsRule(final String key) {
		return styles.containsKey(key);
	}

	public synchronized boolean containsRule(final String key, final String rule) {
		if (styles.containsKey(key)) {
			return styles.get(key).containsKey(rule);
		} else {
			return false;
		}
	}

	public synchronized Set<String> getAllRulesKey() {
		return styles.keySet();
	}

	public synchronized Set<String> getAllRuleKey(final String key) {
		return styles.get(key).keySet();
	}

	public synchronized StringBuffer getStyleBuffer() {
		final StringBuffer stylebuffer = new StringBuffer("\n");

		for (final String key : styles.keySet()) {
			stylebuffer.append(" " + key + "{ ");
			final Map<String, String> rules = styles.get(key);

			for (final String rulekey : rules.keySet()) {
				stylebuffer.append(rulekey + ": " + rules.get(rulekey) + "; ");
			}

			stylebuffer.append("}\n");
		}

		return stylebuffer;
	}

	/**
	 * waits for the buffer to be closed and then writes its content on the writer
	 * 
	 * @param writer
	 *          the writer where the buffer content has to be written
	 */
	public synchronized void write(final Writer writer) {
		while (!closed) {
			try {
				wait();
			} catch (final Exception e) {
				e.printStackTrace();// TODO
			}
		}
		final StringBuffer buffer = getStyleBuffer();
		if (buffer.length() > 0) {
			try {
				writer.write(buffer.toString());
			} catch (final Exception e) {
				e.printStackTrace();// TODO
			}
		}
	}

	/**
	 * cleans and re-opens the buffer
	 */
	public synchronized void clean() {
		closed = false;
		styles.clear();
		notifyAll();
	}
	
	public void open(){
		changed = false;
		closed = false;
	}
	
	public boolean isChanged(){
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	
	

}
