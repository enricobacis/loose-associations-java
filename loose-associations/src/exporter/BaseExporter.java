package exporter;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.associations.BaseAssociations;
import core.fragments.BaseFragments;
import core.tables.BaseTable;

/**
 * The BaseExporter represent an exporter of
 * Loose associations. An exporter should have a method
 * export with some parameters. We don't enforce here
 * the export method because different export formats
 * could have different parameters.
 * We can think about different exporters to different formats.
 * JDBC databases, CSV tables, XML and so on.
 */
public abstract class BaseExporter {
	
	/** The logger. */
	protected final Logger logger;
	
	/** The table. */
	protected final BaseTable table;
	
	/** The fragments. */
	protected final BaseFragments fragments;
	
	/** The associations. */
	protected final BaseAssociations associations;
	
	/**
	 * Instantiates a new base exporter.
	 *
	 * @param table the table
	 * @param fragments the fragments
	 * @param associations the associations
	 * @param logger the logger
	 */
	protected BaseExporter(BaseTable table, BaseFragments fragments, BaseAssociations associations, Logger logger) {
		if (logger == null)
			this.logger = LoggerFactory.getLogger(JDBCExporter.class);
		else
			this.logger = logger;
		
		// Fetch log4j configuration
		PropertyConfigurator.configure("log4j.properties");
		this.table = table;
		this.fragments = fragments;
		this.associations = associations;
	}
	
	/**
	 * Instantiates a new base exporter.
	 *
	 * @param table the table
	 * @param fragments the fragments
	 * @param associations the associations
	 */
	protected BaseExporter(BaseTable table, BaseFragments fragments, BaseAssociations associations) {
		this(table, fragments, associations, null);
	}

}
