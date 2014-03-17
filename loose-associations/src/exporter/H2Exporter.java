package exporter;

import org.slf4j.Logger;

import core.associations.BaseAssociations;
import core.fragments.BaseFragments;
import core.tables.BaseTable;

/**
 * The Class H2Exporter.
 */
public class H2Exporter extends JDBCExporter {

	/**
	 * Instantiates a new h2 exporter.
	 *
	 * @param table the table
	 * @param fragments the fragments
	 * @param associations the associations
	 */
	public H2Exporter(BaseTable table, BaseFragments fragments, BaseAssociations associations) {
		this(table, fragments, associations, null);
	}
	
	/**
	 * Instantiates a new h2 exporter.
	 *
	 * @param table the table
	 * @param fragments the fragments
	 * @param associations the associations
	 * @param logger the logger
	 */
	public H2Exporter(BaseTable table, BaseFragments fragments, BaseAssociations associations, Logger logger) {
		super(table, fragments, associations, logger);
	}
	
	
	/**
	 * Export the data to the database.
	 *
	 * @param databasePath the database path
	 * @return true, if successful
	 */
	public boolean export(String databasePath) {
		return super.export("org.h2.Driver", "jdbc:h2:" + databasePath);
	}
	
	/**
	 * Export the data to the database.
	 *
	 * @param databasePath the database path
	 * @param preSQL command to execute on the database before exporting
	 * @return true, if successful
	 */
	public boolean export(String databasePath, String preSQL) {
		return super.export("org.h2.Driver", "jdbc:h2:" + databasePath, preSQL);
	}

}
