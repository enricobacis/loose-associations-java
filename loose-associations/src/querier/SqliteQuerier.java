package querier;

import java.util.Scanner;

import org.slf4j.Logger;

/**
 * The Class SqliteQuerier.
 */
public class SqliteQuerier extends JDBCQuerier {

	/**
	 * Instantiates a new sqlite querier.
	 *
	 * @param databaseFile the database file
	 * @throws Exception the exception
	 */
	public SqliteQuerier(String databaseFile) throws Exception {
		this(databaseFile, null);
	}
	
	/**
	 * Instantiates a new sqlite querier.
	 *
	 * @param databaseFile the database file
	 * @param logger the logger
	 * @throws Exception the exception
	 */
	public SqliteQuerier(String databaseFile, Logger logger) throws Exception {
		super("org.sqlite.JDBC", "jdbc:sqlite:" + databaseFile, logger);
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
	    Scanner reader = new Scanner(System.in);
		System.out.println("== Sqlite loose querier ==");
		System.out.print("database path: ");
		String databasePath = reader.nextLine();
		
		try {
			new SqliteQuerier(databasePath).startCLI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		reader.close();
	}

}
