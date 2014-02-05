package core.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;

/**
 * The Class BaseGeneratedTable.
 */
public abstract class BaseGeneratedTable extends BaseTable {
	
	/** The tuples. */
	private final int tuples;
	
	/** The columns. */
	private final int columns;
	
	/** The random. */
	protected final Random random;
	
	//HashMap because we don't need synchronization
	/** The table. */
	private HashMap<Integer, List<Object>> table;
	
	/**
	 * Instantiates a new base generated table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 * @param logger the logger
	 */
	protected BaseGeneratedTable(int tuples, int columns, Logger logger) {
		super(logger);
		
		this.tuples = tuples;
		this.columns = columns;
		this.random = new Random();
		
		this.table = new HashMap<Integer, List<Object>>(tuples * 2);
		for (int i = 0; i < columns; ++i)
			this.attributes.addAttribute("attr_" + i, "INTEGER");
	}
	
	/**
	 * Instantiates a new base generated table.
	 *
	 * @param tuples the tuples
	 * @param columns the columns
	 */
	protected BaseGeneratedTable(int tuples, int columns) {
		this(tuples, columns, null);
	}
	
	/* (non-Javadoc)
	 * @see core.tables.BaseTable#size()
	 */
	public final int size() {
		return tuples;
	}
	
	/* (non-Javadoc)
	 * @see core.tables.BaseTable#get(int)
	 */
	public final Row get(int index) {
		if (table.containsKey(index))
			return Row.of(index, table.get(index));
		List<Object> rowData = new ArrayList<Object>();
		for (int c = 0; c < columns; ++c)
			rowData.add(generate());
		rowData = Collections.unmodifiableList(rowData);
		this.table.put(index, rowData);
		return Row.of(index, rowData);
	}
	
	/**
	 * Generate.
	 *
	 * @return the object
	 */
	protected abstract Object generate();

}
