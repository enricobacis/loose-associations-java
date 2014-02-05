package core.tables;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class BaseTable represents the most general type of table that the
 * loose solver can use. This wants to be an interface, in fact it isn't a
 * fully usable class but has abstract methods that every implementation
 * must provide. It's an abstract class so we can also write some default
 * method implementation in order not to have to write it in every single
 * implementation if we just want the default behavior.
 * 
 * It's an iterable or Row, so with the method iterator() we can obtain
 * an iterator over all the rows of the table.
 */
public abstract class BaseTable implements Iterable<Row> {
	
	/** The Constant logger. */
	protected final Logger logger;
	
	/** The attributes. */
	protected Attributes attributes;
	
	/**
	 * Abstract constructor called by all the implementations to setup the logger.
	 *
	 * @param logger the logger
	 */
	public BaseTable(Logger logger) {
		if (logger == null)
			this.logger = LoggerFactory.getLogger(BaseTable.class);
		else
			this.logger = logger;
		
		// Fetch log4j configuration
		PropertyConfigurator.configure("log4j.properties");
		attributes = new Attributes();
	}
	
	/**
	 * Instantiates a new base table.
	 */
	public BaseTable() {
		this(null);
	}
	
	/**
	 * Returns the number of tuples in the table.
	 * Must be implemented in the subclass.
	 *
	 * @return the number of tuples in the table
	 */
	public abstract int size();
	
	/**
	 * Gets the row pointed by index.
	 *
	 * @param index the index
	 * @return the row pointed by the index
	 */
	public abstract Row get(int index);
	
	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	public Attributes getAttributes() {
		return attributes;
	}
	
	/**
	 * Takes a List of Object, they can be Integer or String (checkd by attributes.toNames)
	 * and returns a List of strings, if the input was a List<String> it will be returned
	 * as is, if there are integers in the input list, they will be used to index the
	 * attributes and they will be converted as names.
	 *
	 * @param indices the indices
	 * @return the list of names
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public List<String> toNames(List<Object> indices) throws IllegalArgumentException  {
		return attributes.toNames(indices);
	}

    /**
     * Takes a List of Object, they can be Integer or String (checkd by attributes.toIndices)
     * and returns a List of integers, if the input was a List<Integer> it will be returned
     * as is, if there are strings in the input list, they will be searched in the attributes
     * and they will be converted to the appropriate index. 
     *
     * @param fragment the names
     * @return the list of indices
     * @throws IllegalArgumentException the illegal argument exception
     */
    public <F> List<Integer> toIndices(List<F> fragment) throws IllegalArgumentException {
    	return attributes.toIndices(fragment);
    }
	
    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
	public Iterator<Row> iterator() {
		return new Iterator<Row>() {
			
			private int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return currentIndex < size();
			}

			@Override
			public Row next() {
				return get(currentIndex++);
			}

			@Override
			public void remove() {
				// do nothing, unmodifiable
			}
		};
	}

}
