package core.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;

/**
 * The Class ListTable.
 */
public class ListTable extends BaseTable {
	
	/** The table. */
	protected ArrayList<List<Object>> table;
	
	/**
	 * Instantiates a new list table.
	 *
	 * @param logger the logger
	 */
	protected ListTable(Logger logger) {
		super(logger);
	}
	
	/**
	 * Instantiates a new list table.
	 */
	protected ListTable() {
		this(null);
	}
	
	/**
	 * Instantiates a new list table.
	 *
	 * @param attributes the attributes
	 * @param table the table
	 * @param logger the logger
	 */
	public ListTable(Attributes attributes, ArrayList<List<Object>> table, Logger logger) {
		this(logger);
		if (table == null)
			throw new IllegalArgumentException("The table must be not null");
		this.table = table;
		this.attributes = attributes;
	}
	
	/**
	 * Instantiates a new list table.
	 *
	 * @param attributes the attributes
	 * @param table the table
	 */
	public ListTable(Attributes attributes, ArrayList<List<Object>> table) {
		this(attributes, table, null);
	}
	
	/* (non-Javadoc)
	 * @see core.tables.BaseTable#size()
	 */
	public int size() {
		return table.size();
	}
	
	/* (non-Javadoc)
	 * @see core.tables.BaseTable#get(int)
	 */
	public final Row get(int index) {
		return Row.of(index, Collections.unmodifiableList(table.get(index)));
	}

}
