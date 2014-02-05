package querier;

import java.util.Scanner;

import org.slf4j.Logger;

/**
 * The Class H2Querier.
 */
public class H2Querier extends JDBCQuerier {

	/**
	 * Instantiates a new h2 querier.
	 *
	 * @param databasePath the database path
	 * @throws Exception the exception
	 */
	public H2Querier(String databasePath) throws Exception {
		this(databasePath, null);
	}
	
	/**
	 * Instantiates a new h2 querier.
	 *
	 * @param databasePath the database path
	 * @param logger the logger
	 * @throws Exception the exception
	 */
	public H2Querier(String databasePath, Logger logger) throws Exception {
		super("org.h2.Driver", "jdbc:h2:" + databasePath, null);
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
	    Scanner reader = new Scanner(System.in);
		System.out.println("== H2 loose querier ==");
		System.out.print("database path: ");
		String databasePath = reader.nextLine();
		
		try {
			new H2Querier(databasePath).startCLI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		reader.close();
	}

}
