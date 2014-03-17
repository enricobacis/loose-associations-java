package core.tables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

/**
 * The Class JDBCTable.
 */
public class JDBCTable extends ListTable {
	
	/** The connection. */
	private Connection connection;
	
	/** The tablename. */
	private final String tablename;
	
	/** The orderBy string. */
	private final String orderBy;
	
	/**
	 * Instantiates a new JDBC table.
	 *
	 * @param tablename the tablename
	 * @param orderBy the orderBy string
	 * @param logger the logger
	 */
	private JDBCTable(String tablename, String orderBy, Logger logger) {
		super(logger);
		this.tablename = tablename;
		this.orderBy = orderBy;
	}
	
	/**
	 * Instantiates a new JDBC table.
	 *
	 * @param classForName the class for name
	 * @param url the url
	 * @param tablename the tablename
	 * @param orderBy the orderBy
	 * @param limit limit
	 * @param logger the logger
	 * @throws Exception the exception
	 */
	protected JDBCTable(String classForName, String url, String tablename, String orderBy, Integer limit, Logger logger) throws Exception {
		this(tablename, orderBy, logger);
		String message;
		
		try {
			Class.forName(classForName);
			Connection connection = DriverManager.getConnection(url);
			fetchTable(connection, limit);
			
			if (connection != null && !connection.isClosed())
				connection.close();
			
		} catch (ClassNotFoundException e) {
			message = "class " + classForName + " not found. Be sure to add its jar to build path. ";
			this.logger.error(message);
			throw new Exception(message + e.getMessage());
		} catch (SQLException e) {
			this.logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * Instantiates a new JDBC table.
	 *
	 * @param classForName the class for name
	 * @param url the url
	 * @param tablename the tablename
	 * @param orderBy the order by
	 * @throws Exception the exception
	 */
	protected JDBCTable(String classForName, String url, String tablename, String orderBy) throws Exception {
		this(classForName, url, tablename, orderBy, null, null);
	}

	/**
	 * Instantiates a new JDBC table.
	 *
	 * @param connection the connection
	 * @param tablename the tablename
	 * @param logger the logger
	 * @throws SQLException the SQL exception
	 */
	public JDBCTable(Connection connection, String tablename, Logger logger) throws SQLException {
		this(connection, tablename, null, null, logger);
	}
	
	/**
	 * Instantiates a new JDBC table.
	 *
	 * @param connection the connection
	 * @param tablename the tablename
	 * @throws SQLException the SQL exception
	 */
	public JDBCTable(Connection connection, String tablename) throws SQLException {
		this(connection, tablename, null, null, null);
	}
	
	/**
	 * Instantiates a new JDBC table.
	 *
	 * @param connection the connection
	 * @param tablename the tablename
	 * @param orderBy the orderBy
	 * @param limit limit
	 * @param logger the logger
	 * @throws SQLException the SQL exception
	 */
	public JDBCTable(Connection connection, String tablename, String orderBy, Integer limit, Logger logger) throws SQLException {
		this(tablename, orderBy, logger);
		fetchTable(connection, limit);
	}
	
	/**
	 * Instantiates a new JDBC table.
	 *
	 * @param connection the connection
	 * @param tablename the tablename
	 * @param orderBy the order by
	 * @throws SQLException the SQL exception
	 */
	public JDBCTable(Connection connection, String tablename, String orderBy) throws SQLException {
		this(connection, tablename, orderBy, null, null);
	}
	
	/**
	 * Fetch table.
	 *
	 * @param connection the connection
	 * @throws SQLException the SQL exception
	 */
	protected void fetchTable(Connection connection, Integer limit) throws SQLException {
		this.connection = connection;
		
		StringBuilder sb = new StringBuilder("SELECT * FROM ").append(tablename);
		
		// we have a double select * from so we can chain the other order by requested
		// the function RANDOM() is to make a sample
		if (limit != null)
			sb = new StringBuilder("SELECT * FROM (").append(sb.toString()).append(" ORDER BY RANDOM() LIMIT ").append(limit).append(") ");
		
		if (orderBy != null && !orderBy.isEmpty())
			sb.append(" ORDER BY ").append(orderBy);
		
		String sql = sb.toString();
		logger.info(sql);
		
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		
	    extractAttributes(rs);
	    fetchRows(rs);
	}
	
	/**
	 * Extract attributes.
	 *
	 * @param rs the rs
	 * @throws SQLException the SQL exception
	 */
	private void extractAttributes(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columns = rsmd.getColumnCount();
		
		for (int col = 1; col <= columns; ++col) {
	       	String name = rsmd.getColumnName(col).toLowerCase();
	        String type = rsmd.getColumnTypeName(col);
	        logger.trace("extracted attribute " + name + " [" + type + "]");
	        this.attributes.addAttribute(name, type);
	    }
		
		logger.info("extracted attributes: " + attributes);
	}
	
	/**
	 * Fetch rows.
	 *
	 * @param rs the rs
	 * @throws SQLException the SQL exception
	 */
	private void fetchRows(ResultSet rs) throws SQLException {
		table = new ArrayList<List<Object>>();
		int columns = rs.getMetaData().getColumnCount();
		
	    while (rs.next()) {
    		ArrayList<Object> row = new ArrayList<Object>();
	    	for (int col = 1; col <= columns; ++col)
	    		row.add(rs.getObject(col));
	    	table.add(row);
	    }
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		if (!connection.isClosed())
			connection.close();
	}
	
}
