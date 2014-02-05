package core.tables;

import org.slf4j.Logger;

/**
 * The Class SelfSimilarTable.
 */
public class SelfSimilarTable extends BaseGeneratedTable {

	/** The coeff. */
	private final double coeff;
	
	/** The maxvalue. */
	private final int maxvalue;
	
	/**
	 * Instantiates a new self similar table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param logger the logger
	 */
	public SelfSimilarTable(int tuples, int columns, Logger logger) {
		this(tuples, columns, 0.5, 100, logger);
	}
	
	/**
	 * Instantiates a new self similar table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 */
	public SelfSimilarTable(int tuples, int columns) {
		this(tuples, columns, 0.5, 100, null);
	}
	
	/**
	 * Instantiates a new self similar table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param coeff the coeff
	 * @param logger the logger
	 */
	public SelfSimilarTable(int tuples, int columns, double coeff, Logger logger) {
		this(tuples, columns, coeff, 100, logger);
	}
	
	/**
	 * Instantiates a new self similar table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param coeff the coeff
	 */
	public SelfSimilarTable(int tuples, int columns, double coeff) {
		this(tuples, columns, coeff, 100, null);
	}
	
	/**
	 * Instantiates a new self similar table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param coeff the coeff
	 * @param maxvalue the maxvalue
	 * @param logger the logger
	 */
	public SelfSimilarTable(int tuples, int columns, double coeff, int maxvalue, Logger logger) {
		super(tuples, columns, logger);
		this.coeff = Math.log(coeff) / Math.log(1 - coeff);
		this.maxvalue = maxvalue;
	}
	
	/**
	 * Instantiates a new self similar table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param coeff the coeff
	 * @param maxvalue the maxvalue
	 */
	public SelfSimilarTable(int tuples, int columns, double coeff, int maxvalue) {
		this(tuples, columns, coeff, maxvalue, null);
	}
	
	/* (non-Javadoc)
	 * @see core.tables.BaseGeneratedTable#generate()
	 */
	@Override
	protected Object generate() {
		return (int) (maxvalue * (Math.pow(random.nextDouble(), coeff)));
	}

}
