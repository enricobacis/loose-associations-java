package core.tables;

import java.util.Collections;
import java.util.List;

/**
 * The Class Row represents a row with its id and its data.
 */
public class Row {

	/** The row id. */
	private final int rowId;
	
	/** The row data. */
	private final List<Object> rowData;

	/**
	 * Instantiates a new row.
	 *
	 * @param rowId the row id
	 * @param rowData the row data
	 */
	public Row(int rowId, List<Object> rowData) {
		this.rowId = rowId;
		this.rowData = Collections.unmodifiableList(rowData);
	}
	
	/**
	 * Creates a new Row with rowId and rowData.
	 *
	 * @param rowId the row id
	 * @param rowData the row data
	 * @return the row
	 */
	public static Row of(int rowId, List<Object> rowData) {
		return new Row(rowId, rowData);
	}
	
	/**
	 * Returns the row id
	 *
	 * @return the row id
	 */
	public int id() {
		return rowId;
	}
	
	/**
	 * Returns the data in the row
	 *
	 * @return the row data
	 */
	public List<Object> data() {
		return rowData;
	}
	
}
