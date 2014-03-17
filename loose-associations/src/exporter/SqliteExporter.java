package exporter;

import org.slf4j.Logger;

import core.associations.BaseAssociations;
import core.fragments.BaseFragments;
import core.tables.BaseTable;

/**
 * The Class SqliteExporter.
 */
public class SqliteExporter extends JDBCExporter {

	/**
	 * Instantiates a new sqlite exporter.
	 *
	 * @param table the table
	 * @param fragments the fragments
	 * @param associations the associations
	 */
	public SqliteExporter(BaseTable table, BaseFragments fragments, BaseAssociations associations) {
		this(table, fragments, associations, null);
	}
	
	/**
	 * Instantiates a new sqlite exporter.
	 *
	 * @param table the table
	 * @param fragments the fragments
	 * @param associations the associations
	 * @param logger the logger
	 */
	public SqliteExporter(BaseTable table, BaseFragments fragments, BaseAssociations associations, Logger logger) {
		super(table, fragments, associations, logger);
	}
	

	/**
	 * Export the data to the database.
	 *
	 * @param databasePath the database path
	 * @return true, if successful
	 */
	public boolean export(String databasePath) {
		return super.export("org.sqlite.JDBC", "jdbc:sqlite:" + databasePath);
	}
	
	/**
	 * Export the data to the database.
	 *
	 * @param databasePath the database path
	 * @param preSQL command to execute on the database before exporting
	 * @return true, if successful
	 */
	public boolean export(String databasePath, String preSQL) {
		return super.export("org.sqlite.JDBC", "jdbc:sqlite:" + databasePath, preSQL);
	}

}
