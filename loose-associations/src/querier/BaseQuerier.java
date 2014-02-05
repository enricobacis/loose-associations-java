package querier;

import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class BaseQuerier represents a general querier.
 */
public abstract class BaseQuerier {
	
	/** The logger. */
	protected final Logger logger;
	
	/**
	 * Instantiates a new base querier.
	 *
	 * @param logger the logger
	 */
	public BaseQuerier(Logger logger) {
		if (logger == null)
			this.logger = LoggerFactory.getLogger(BaseQuerier.class);
		else
			this.logger = logger;
		
		// Fetch log4j configuration
		PropertyConfigurator.configure("log4j.properties");
	}
	
	/**
	 * Instantiates a new base querier.
	 */
	public BaseQuerier(){
		this(null);
	}
	
	/**
	 * Query the database.
	 *
	 * @param request the request
	 * @return the result of the query
	 * @throws Exception the exception
	 */
	public abstract List<List<Object>> query(String request) throws Exception;
	
	/**
	 * Prints the query results.
	 *
	 * @param request the request
	 * @throws Exception the exception
	 */
	public void printQuery(String request) throws Exception {
		for (Object tuple: query(request))
			System.out.println(tuple);
	}

}
