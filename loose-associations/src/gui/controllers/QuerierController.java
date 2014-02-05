package gui.controllers;

import java.sql.SQLException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import querier.H2Querier;
import querier.JDBCQuerier;
import querier.SqliteQuerier;
import utils.tuples.Pair;

/**
 * The Class QuerierController.
 */
public class QuerierController extends Observable implements Controller {
	
	/** The querier. */
	private JDBCQuerier querier;
	
	/** The worker. */
	private QuerierWorker worker;
	
	/** The observable logger appender. */
	private final ObservableAppender observableLoggerAppender;
	
	/** The logger. */
	private final Logger logger;
	
	/** The logger implementation. */
	private final org.apache.log4j.Logger loggerImplementation;

	/**
	 * Instantiates a new querier controller.
	 *
	 * @param tab the tab
	 */
	public QuerierController(int tab) {
		loggerImplementation = org.apache.log4j.Logger.getLogger("TAB_" + tab);
		observableLoggerAppender = new ObservableAppender();
		loggerImplementation.addAppender(observableLoggerAppender);
		this.logger = LoggerFactory.getLogger("TAB_" + tab);
	}

	/* (non-Javadoc)
	 * @see gui.controllers.Controller#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return (worker != null && worker.isAlive());
	}

	/**
	 * Connect.
	 *
	 * @param databaseFile the database file
	 * @throws Exception the exception
	 */
	public void connect(String databaseFile) throws Exception {
		// stop previous querier if any
		stop();
		
		if (databaseFile.trim().endsWith(".h2.db"))
			querier = new H2Querier(databaseFile.replaceFirst("\\.h2\\.db$", ""), logger);
		else
			querier = new SqliteQuerier(databaseFile, logger);
	}

	/**
	 * Disconnect.
	 */
	public void disconnect() {
		stop();
	}

	/**
	 * Start query.
	 *
	 * @param request the request
	 */
	public void startQuery(final String request) {
		worker = new QuerierWorker(request);
		worker.start();
	}
	
	/**
	 * The Enum QuerierWorkerStatus.
	 */
	public enum QuerierWorkerStatus { /** The querying. */
 QUERYING, /** The done. */
 DONE, /** The error. */
 ERROR }
	
	/**
	 * The Class QuerierWorker.
	 */
	private class QuerierWorker extends Thread {
		
		/** The request. */
		private String request;
		
		/** The result. */
		private List<List<Object>> result;
		
		/**
		 * Instantiates a new querier worker.
		 *
		 * @param request the request
		 */
		public QuerierWorker(String request) {
			this.request = request;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				
				setChanged();
				notifyObservers(Pair.of(QuerierWorkerStatus.QUERYING, request));
				result = querier.query(request);
				
				setChanged();
				notifyObservers(Pair.of(QuerierWorkerStatus.DONE, result));
				
			} catch (SQLException e) {
				
				setChanged();
				notifyObservers(Pair.of(QuerierWorkerStatus.ERROR, e.getMessage()));
				
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see gui.controllers.Controller#stop()
	 */
	@Override
	public void stop() {
		// this automatically stops the query 
		if (querier != null)
			querier.close();
	}

	/* (non-Javadoc)
	 * @see gui.controllers.Controller#addLoggerObserver(java.util.Observer)
	 */
	@Override
	public void addLoggerObserver(Observer o) {
		observableLoggerAppender.addObserver(o);
	}

	/* (non-Javadoc)
	 * @see gui.controllers.Controller#setLoggerLevel(org.apache.log4j.Level)
	 */
	@Override
	public void setLoggerLevel(Level level) {
		loggerImplementation.setLevel(level);
	}

}
