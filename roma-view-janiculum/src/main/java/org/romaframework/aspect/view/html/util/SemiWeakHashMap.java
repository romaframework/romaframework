package org.romaframework.aspect.view.html.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * this is a weak hashmap where the last n inserted items (where n is the "dimension" of the map) are not eliminated by the garbage
 * collector as normal {@link WeakHashMap} behaviour
 * 
 * @author Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
 * 
 * @param <K>
 *          the map key
 * @param <V>
 *          the map value
 */
public class SemiWeakHashMap<K, V> implements Map<K, V> {

	protected Map<K, V>	map;
	protected List<K>		fixed;
	protected int				dimension;

	/**
	 * builds a semi-weak hashmap of the given dimension
	 * 
	 * @param dimension
	 *          the number of items that are not treated as "weak"
	 */
	public SemiWeakHashMap(final int dimension) {
		this.dimension = dimension;
		this.fixed = new LinkedList<K>();
		this.map = new WeakHashMap<K, V>();
	}

	/**
	 * the same as {@link #SemiWeakHashMap(int)} with dimention = 1
	 */
	public SemiWeakHashMap() {
		this(1);
	}

	public void clear() {
		this.fixed.clear();
		map.clear();
	}

	public boolean containsKey(final Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(final Object value) {
		return map.containsValue(value);
	}

	public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	@Override
	public boolean equals(final Object o) {
		return map.equals(o);
	}

	public V get(final Object key) {
		return map.get(key);
	}

	@Override
	public int hashCode() {
		return map.hashCode();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public V put(final K key, final V value) {
		fixed.add(key);
		if (fixed.size() > dimension) {
			fixed.remove(0);
		}
		return map.put(key, value);
	}

	public void putAll(final Map<? extends K, ? extends V> m) {
		if (m == null) {
			return;
		}
		for (final Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	public V remove(final Object key) {
		fixed.remove(key);
		return map.remove(key);
	}

	public int size() {
		return map.size();
	}

	public Collection<V> values() {
		return map.values();
	}

	/**
	 * returns the dimension of the map (the number of items that the map assures will not be removed by the garbage collector)
	 * 
	 * @return the dimension of the map
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * sets the dimension of the map (the number of items that the map assures will not be removed by the garbage collector)
	 * 
	 * @param dimension
	 *          the dimension of the map
	 */
	public void setDimension(final int dimension) {
		this.dimension = dimension;
	}

}
