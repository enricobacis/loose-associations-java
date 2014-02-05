package core.tables;

import org.slf4j.Logger;

/**
 * The Class RandomTable.
 */
public class RandomTable extends BaseGeneratedTable {
	
	/** The maxvalue. */
	private final int maxvalue;
	
	/**
	 * Instantiates a new random table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param logger the logger
	 */
	public RandomTable(int tuples, int columns, Logger logger) {
		this(tuples, columns, 100, logger);
	}
	
	/**
	 * Instantiates a new random table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 */
	public RandomTable(int tuples, int columns) {
		this(tuples, columns, 100, null);
	}

	/**
	 * Instantiates a new random table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param maxvalue the maxvalue
	 * @param logger the logger
	 */
	public RandomTable(int tuples, int columns, int maxvalue, Logger logger) {
		super(tuples, columns, logger);
		this.maxvalue = maxvalue;
	}
	
	/**
	 * Instantiates a new random table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param maxvalue the maxvalue
	 */
	public RandomTable(int tuples, int columns, int maxvalue) {
		this(tuples, columns, maxvalue, null);
	}

	/* (non-Javadoc)
	 * @see core.tables.BaseGeneratedTable#generate()
	 */
	@Override
	protected Object generate() {
		return random.nextInt(maxvalue);
	}
	
}
