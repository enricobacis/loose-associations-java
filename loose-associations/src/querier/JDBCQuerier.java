package querier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

/**
 * The Class JDBCQuerier extends BaseQuerier and
 * it's a querier for every JDBC connection.
 */
public class JDBCQuerier extends BaseQuerier {

	/** The connection. */
	private Connection connection;
	
	/** The to fragment can be used like: attributeName => fragmentId. */
	private Map<String, Integer> toFragment = new HashMap<String, Integer>();
	
	/** The pattern to get marked attributes. */
	Pattern getMarkedAttributes = Pattern.compile("\\?(\\w+)");

	/**
	 * Instantiates a new JDBC querier.
	 *
	 * @param connection the connection
	 * @throws Exception the exception
	 */
	public JDBCQuerier(Connection connection) throws Exception {
		this(connection, null);
	}
	
	/**
	 * Instantiates a new JDBC querier.
	 *
	 * @param connection the connection
	 * @param logger the logger
	 * @throws Exception the exception
	 */
	public JDBCQuerier(Connection connection, Logger logger) throws Exception {
		super(logger);
		this.connection = connection;
		fetchSchema();
	}
	
	/**
	 * Instantiates a new JDBC querier.
	 *
	 * @param classForName the class for name
	 * @param url the url
	 * @throws Exception the exception
	 */
	protected JDBCQuerier(String classForName, String url) throws Exception {
		this(classForName, url, null);
	}
	
	/**
	 * Instantiates a new JDBC querier.
	 *
	 * @param classForName the class for name
	 * @param url the url
	 * @param logger the logger
	 * @throws Exception the exception
	 */
	public JDBCQuerier(String classForName, String url, Logger logger) throws Exception {
		super(logger);
		
		try {
			
			Class.forName(classForName);
			connection = DriverManager.getConnection(url);
			fetchSchema();
			
		} catch (ClassNotFoundException e) {
			throw new Exception("class " + classForName + " not found. Be sure to add its jar to build path");
		}
	}

	/**
	 * Fetch the schema table stored by the exported from the database.
	 * This permit to know which fragment is each attribute stored.
	 *
	 * @throws Exception the exception
	 */
	private void fetchSchema() throws Exception {
		// get the table from the database
		Statement statement = connection.createStatement();
		String sql = "SELECT attribute, fragment FROM schema";
		logger.trace(sql);
		ResultSet rs = statement.executeQuery(sql);

		// iterate over all the attributes and save them in the map
		while (rs.next())
			toFragment.put(rs.getString(1), rs.getInt(2));

		// make toFragment unmodifiable
		toFragment = Collections.unmodifiableMap(toFragment);
	}

	/**
	 * Closes the connection to the database.
	 */
	public void close() {
		try {
			if (!connection.isClosed()) {
				logger.debug("Closing connection");
				connection.close();
			}
		} catch (Exception e) {
			logger.debug(e.toString());
		}
	}

	/* (non-Javadoc)
	 * @see querier.BaseQuerier#query(java.lang.String)
	 */
	@Override
	public List<List<Object>> query(String request) throws SQLException {
		
		List<List<Object>> result = null;
		Matcher matcher = getMarkedAttributes.matcher(request);
		Set<Integer> fragments = new HashSet<Integer>();
		
		// get all the captured groups
		while (matcher.find()) 
			fragments.add(toFragment.get(matcher.group(1)));

		Statement statement = connection.createStatement();
		String from = createFromClause(fragments);
		String sql = request.replaceAll("\\s+[Ff][Rr][Oo][Mm]\\s+\\?", " from " + from).replace("?", "");
		logger.info(sql);
		ResultSet rs = statement.executeQuery(sql);

		result = new ArrayList<List<Object>>();
		int columns = rs.getMetaData().getColumnCount();

		while (rs.next()) {
			ArrayList<Object> row = new ArrayList<Object>();
			for (int col = 1; col <= columns; ++col)
				row.add(rs.getObject(col));
			result.add(row);
		}
		
		return result;
	}
	
	/**
	 * Creates the from clause.
	 *
	 * @param fragments the fragments
	 * @return the string
	 */
	private String createFromClause(Set<Integer> fragments) {
		if (fragments.isEmpty())
			return "";
		
		if (fragments.size() == 1)
			return "fragment_" + new ArrayList<Integer>(fragments).get(0);
		
		StringBuilder sb = new StringBuilder("associations AS A");
		for (int fragment: fragments)
			sb.append(String.format(" LEFT JOIN fragment_%1$d AS F%1$d ON A.group_%1$d = F%0$s.group_%1$d", fragment));
		
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		this.close();
		super.finalize();
	}
	
	/**
	 * Start cli.
	 */
	public void startCLI() {
		System.out.println("\nEnter your queries using ? before loose attributes and 'FROM ?' to auto-join.");
	    System.out.println("example:  SELECT ?disease FROM ? WHERE ?name = \"Alice\"");
	    System.out.println("Enter a blank line to exit.");
	    
	    String sql;
	    Scanner reader = new Scanner(System.in);
	    while (true) {
	    	try {
		    	System.out.print("> ");
		    	sql = reader.nextLine();
		    	if (sql.isEmpty())
		    		break;
		    	else
		    		printQuery(sql);
	    	} catch (Exception e) {
	    		System.out.println("> bad query: " + e.getMessage());
	    	}
	    }
	    reader.close();
	    System.out.println("> bye");
	}

}
