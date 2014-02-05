package exporter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;

import core.associations.BaseAssociations;
import core.fragments.BaseFragments;
import core.fragments.Fragment;
import core.tables.Attribute;
import core.tables.Attributes;
import core.tables.BaseTable;

/**
 * The Class JDBCExporter extends BaseExporter adding
 * the capacity of exporting to a JDBC database.
 */
public class JDBCExporter extends BaseExporter {
	
	/**
	 * Instantiates a new JDBC exporter.
	 *
	 * @param table the table
	 * @param fragments the fragments
	 * @param associations the associations
	 */
	public JDBCExporter(BaseTable table, BaseFragments fragments, BaseAssociations associations) {	
		this(table, fragments, associations, null);
	}
	
	/**
	 * Instantiates a new JDBC exporter.
	 *
	 * @param table the table
	 * @param fragments the fragments
	 * @param associations the associations
	 * @param logger the logger
	 */
	public JDBCExporter(BaseTable table, BaseFragments fragments, BaseAssociations associations, Logger logger) {	
		super(table, fragments, associations, logger);
	}

	/**
	 * Export the loose association to the connection that
	 * can be created by classForName and url.
	 *
	 * @param classForName the class for name
	 * @param url the url to the database
	 * @return true, if successful
	 */
	protected boolean export(String classForName, String url) {
		boolean result = false;
		Connection connection = null;
		
		try {
			Class.forName(classForName);
			connection = DriverManager.getConnection(url);
			result = this.export(connection);
			logger.info("== Loose Exported ==");
			
		} catch (ClassNotFoundException e) {
			logger.error("class " + classForName + " not found. Be sure to add its jar to build path");
			logger.error(e.toString());
		} catch (SQLException e) {
			logger.error("can't connect to database " + url);
			logger.error(e.toString());
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					logger.debug("connection already closed");
					logger.trace(e.getMessage());
				}
		}
		
		return result;
	}

	/**
	 * Export the loose association to the connection.
	 *
	 * @param connection the connection
	 * @return true, if successful
	 */
	public boolean export(Connection connection) {
		if (connection == null) {
			logger.error("No connection set");
			return false;
		}
		
		try {
			connection.setAutoCommit(false);
			createAssociationsTable(connection);
			createFragmentsTables(connection);
			createSchemaTable(connection);
			connection.commit();
			
		} catch (SQLException e) {
			logger.error("Problem with table creation");
			logger.error(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Creates the associations table.
	 *
	 * @param connection the connection
	 * @throws SQLException the SQL exception
	 */
	private void createAssociationsTable(Connection connection) throws SQLException {
		Statement stmt = connection.createStatement();
		int n = fragments.size();
		PreparedStatement ps;
		StringBuilder sb;
		String sql;
		
		logger.info("== Exporting to {} ==", connection.getMetaData().getURL());
		logger.info("creating the associations table ...");
		
		// create the sql to create the table 
		sb = new StringBuilder("CREATE TABLE associations (");
		for (int i = 0; i < n; ++i)
			sb.append("group_").append(i).append(" INTEGER").append((i == n - 1) ? ")" : ", ");
		
		// create the table
		sql = sb.toString();
		logger.info(sql);
		stmt.execute(sql);
		
		// create the sql to create the index
		sb = new StringBuilder("CREATE UNIQUE INDEX i_associations ON associations (");
		for (int i = 0; i < n; ++i)
			sb.append("group_").append(i).append((i == n - 1) ? ")" : " , ");
		
		// create the index
		sql = sb.toString();
		logger.info(sql);
		stmt.execute(sql);
		
		// prepare the statement to insert the associations
		sql = "INSERT INTO associations VALUES (" + placeholders(n) + ")";
		logger.info(sql);
		ps = connection.prepareStatement(sql);
		
		// insert the associations in the table
		for (List<Integer> association: associations.values()) {
			int index = 0;
			for (int elem: association)
				ps.setInt(++index, elem);
			ps.addBatch();
		}
		
		// execute all the inserts and commit
		ps.executeBatch();
		connection.commit();
	}

	/**
	 * Creates the fragments tables.
	 *
	 * @param connection the connection
	 * @throws SQLException the SQL exception
	 */
	private void createFragmentsTables(Connection connection) throws SQLException {
		Statement stmt = connection.createStatement();
		PreparedStatement ps;
		StringBuilder sb;
		String sql;
		
		Attributes allAttributes = table.getAttributes();
		
		for (Fragment fragment: fragments) {
			
			// select only the attributes in this table
			List<Integer> attributesId = fragments.get(fragment.id).getAttributes();
			ArrayList<Attribute> attributes = itemgetter(allAttributes, attributesId);
			
			// create the sql to create the table
			sb = new StringBuilder("CREATE TABLE fragment_").append(fragment.id).append(" (");
			for (Attribute attribute: attributes)
				sb.append(String.format("%s %s, ", attribute.getName(), attribute.getType()));
			sb.append(String.format("group_%d INTEGER)", fragment.id));
			
			// create the table
			sql = sb.toString();
			logger.info(sql);
			stmt.execute(sql);
			
			// create the index
			sql = String.format("CREATE INDEX i_fragment_%1$d ON fragment_%1$d (group_%1$d)", fragment.id);
			logger.info(sql);
			stmt.execute(sql);
			
			// prepare the statement to add the tuples
			sql = String.format("INSERT INTO fragment_%1$d VALUES (%2$s)", fragment.id, placeholders(attributes.size() + 1));
			logger.info(sql);
			ps = connection.prepareStatement(sql.toString());
			
			int index;
			List<Object> rowData;
			
			for (Entry<Integer, Set<Integer>> entry : associations.getGroups(fragment.id).entrySet()) {
			    Integer groupId = entry.getKey();
			    List<Integer> groupRows = new ArrayList<Integer>(entry.getValue());
			    Collections.shuffle(groupRows);
			    
			    for (int row: groupRows) {
			    	rowData = table.get(row).data();
			    	index = 0;
			    	
			    	// add to the insert only the attributes in this fragment
			    	for (int attributeId: attributesId)
						ps.setObject(++index, rowData.get(attributeId));
			    	
			    	// add the groupId
			    	ps.setInt(++index, groupId);
			    	// add the insert to the batch
					ps.addBatch();
			    }
			}
			
			// execute batch of inserts and commit
			ps.executeBatch();
			connection.commit();
		}
	}
	
	/**
	 * Creates the schema table that will be used by the
	 * querier to know where the attributes are.
	 *
	 * @param connection the connection
	 * @throws SQLException if something goes wrong
	 */
	private void createSchemaTable(Connection connection) throws SQLException {
		
		String sql = "CREATE TABLE schema (attribute VARCHAR(128) PRIMARY KEY, typ VARCHAR(64), fragment INTEGER)";
		logger.info(sql);
		connection.createStatement().execute(sql);
		
		sql = "INSERT INTO schema VALUES (?, ?, ?)";
		logger.info(sql);
        PreparedStatement ps = connection.prepareStatement(sql);
        
        Attributes allAttributes = table.getAttributes();
        for (Fragment fragment: fragments) {
        	for (int attributeId: fragment.getAttributes()) {
        		Attribute attribute = allAttributes.get(attributeId);
        		ps.setString(1, attribute.getName());
            	ps.setString(2, attribute.getType());
            	ps.setInt(3, fragment.id);
            	ps.addBatch();
        	}
        }
        ps.executeBatch();
        connection.commit();
	}
	
	/**
	 * Create a placeholder of n "?" to use with PreparedStatement.
	 * For example placeholders(3) = "?, ?, ?" 
	 *
	 * @param n the numer of question marks
	 * @return the output string
	 */
	private String placeholders(int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; ++i)
			sb.append('?').append((i == n - 1) ? "" : ", ");
		return sb.toString();
	}
	
	/**
	 * Itemgetter is similar to the python function without the C speed.
	 * It takes a list and a list of indices and returns a list of
	 * items from the from list at the requested indices.
	 *
	 * @param <T> the generic type of the input list
	 * @param from the input list
	 * @param indices the indices to get
	 * @return the output list
	 */
	private <T> ArrayList<T> itemgetter(List<T> from, List<Integer> indices) {
		ArrayList<T> result = new ArrayList<T>();
		for (int index: indices)
			result.add(from.get(index));
		return result;
	}

}
