package core.tables;

import org.slf4j.Logger;

/**
 * The Class H2Table that extends JDBCTable with H2 drivers.
 */
public class H2Table extends JDBCTable {

	/**
	 * Instantiates a new h2 table.
	 *
	 * @param databasePath the database path
	 * @param tablename the table in the database
	 * @param logger the logger
	 * @throws Exception the exception
	 */
	public H2Table(String databasePath, String tablename, Logger logger) throws Exception {
		this(databasePath, tablename, null, logger);
	}
	
	/**
	 * Instantiates a new h2 table.
	 *
	 * @param databasePath the database path
	 * @param tablename the tablename
	 * @throws Exception the exception
	 */
	public H2Table(String databasePath, String tablename) throws Exception {
		this(databasePath, tablename, null, null);
	}
	
	/**
	 * Instantiates a new h2 table.
	 *
	 * @param databasePath the database path
	 * @param tablename the table in the database
	 * @param orderBy the orderBy string
	 * @param logger the logger
	 * @throws Exception the exception
	 */
	public H2Table(String databasePath, String tablename, String orderBy, Logger logger) throws Exception {
		super("org.h2.Driver", "jdbc:h2:" + databasePath, tablename, orderBy, logger);
	}
	
	/**
	 * Instantiates a new h2 table.
	 *
	 * @param databasePath the database path
	 * @param tablename the tablename
	 * @param orderBy the order by
	 * @throws Exception the exception
	 */
	public H2Table(String databasePath, String tablename, String orderBy) throws Exception {
		this(databasePath, tablename, orderBy, null);
	}

}
