package core.tables;

import org.slf4j.Logger;

/**
 * The Class GaussianTable.
 */
public class GaussianTable extends BaseGeneratedTable {
	
	/** The mean. */
	private final double mean;
	
	/** The variance. */
	private final double variance;

	/**
	 * Instantiates a new Gaussian table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param logger the logger
	 */
	public GaussianTable(int tuples, int columns, Logger logger) {
		this(tuples, columns, 0, 10, logger);
	}
	
	/**
	 * Instantiates a new Gaussian table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 */
	public GaussianTable(int tuples, int columns) {
		this(tuples, columns, 0, 10, null);
	}
	
	/**
	 * Instantiates a new gaussian table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param mean the mean
	 * @param logger the logger
	 */
	public GaussianTable(int tuples, int columns, double mean, Logger logger) {
		this(tuples, columns, mean, 10, logger);
	}
	
	/**
	 * Instantiates a new gaussian table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param mean the mean
	 */
	public GaussianTable(int tuples, int columns, double mean) {
		this(tuples, columns, mean, 10, null);
	}
	
	/**
	 * Instantiates a new gaussian table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param mean the mean
	 * @param variance the variance
	 * @param logger the logger
	 */
	public GaussianTable(int tuples, int columns, double mean, double variance, Logger logger) {
		super(tuples, columns, logger);
		this.mean = mean;
		this.variance = variance;
	}
	
	/**
	 * Instantiates a new gaussian table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param mean the mean
	 * @param variance the variance
	 */
	public GaussianTable(int tuples, int columns, double mean, double variance) {
		this(tuples, columns, mean, variance, null);
	}

	/* (non-Javadoc)
	 * @see core.tables.BaseGeneratedTable#generate()
	 */
	@Override
	protected Object generate() {
		return (int) (mean + random.nextGaussian() * variance);
	}

}
