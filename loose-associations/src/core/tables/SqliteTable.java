package core.tables;

import org.slf4j.Logger;

/**
 * The Class SqliteTable that extends JDBCTable with Sqlite drivers.
 */
public class SqliteTable extends JDBCTable {
	
	/**
	 * Instantiates a new sqlite table.
	 *
	 * @param databasePath the database path
	 * @param tablename the tablename
	 * @param logger the logger
	 * @throws Exception the exception
	 */
	public SqliteTable(String databasePath, String tablename, Logger logger) throws Exception {
		this(databasePath, tablename, null, null, logger);
	}
	
	/**
	 * Instantiates a new sqlite table.
	 *
	 * @param databasePath the database path
	 * @param tablename the tablename
	 * @throws Exception the exception
	 */
	public SqliteTable(String databasePath, String tablename) throws Exception {
		this(databasePath, tablename, null, null, null);
	}
	
	/**
	 * Instantiates a new sqlite table.
	 *
	 * @param databasePath the database path
	 * @param tablename the tablename
	 * @param orderBy the order by
	 * @param limit limit
	 * @param logger the logger
	 * @throws Exception the exception
	 */
	public SqliteTable(String databasePath, String tablename, String orderBy, Integer limit, Logger logger) throws Exception {
		super("org.sqlite.JDBC", "jdbc:sqlite:" + databasePath, tablename, orderBy, limit, logger);
	}
	
	/**
	 * Instantiates a new sqlite table.
	 *
	 * @param databasePath the database path
	 * @param tablename the tablename
	 * @param orderBy the order by
	 * @throws Exception the exception
	 */
	public SqliteTable(String databasePath, String tablename, String orderBy) throws Exception {
		this(databasePath, tablename, orderBy, null, null);
	}

}
