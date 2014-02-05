package utils;

import java.util.HashMap;

/**
 * The Class DefaultHashMap is an HashMap that
 * automatically creates a new instance of an object
 * and adds it to the HashMap if it doesn't contain
 * a requested (get) element.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DefaultHashMap<K, V> extends HashMap<K, V> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1679761918746650035L;
	
	/** The cls. */
	private final Class<V> cls;
 
	/**
	 * Instantiates a new default hash map.
	 *
	 * @param clazz the class whose instances will be put in the HashMap
	 */
	public DefaultHashMap(Class clazz) {
        this.cls = clazz;
    }
    
	/* (non-Javadoc)
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@Override
    public V get(Object key) {
        V value = super.get(key);
        if (!super.containsKey(key)) {
            try {
                value = cls.newInstance();
            } catch (Exception e) {
            	throw new Error("Can't instantiate a new value for DefaultHashMap");
            }
            this.put((K) key, value);
        }
        return value;
    }
 
}
