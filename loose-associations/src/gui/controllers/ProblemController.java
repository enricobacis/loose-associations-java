package gui.controllers;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.tuples.Pair;
import utils.tuples.Triplet;
import core.GreedyLoose;
import core.associations.BaseAssociations;
import core.fragments.BaseFragments;
import core.tables.BaseTable;
import exporter.H2Exporter;
import exporter.SqliteExporter;
import gui.DBType;

/**
 * The Class ProblemController.
 */
public abstract class ProblemController extends Observable implements Controller {
	
	/** The table. */
	protected BaseTable table;
	
	/** The logger. */
	protected final Logger logger;
	
	/** The logger implementation. */
	private final org.apache.log4j.Logger loggerImplementation;
	
	private final ObservableAppender observableLoggerAppender;
	/** The observable logger appender. */
	
	/** The loose. */
	private GreedyLoose loose;
	
	/** The k list. */
	private List<Integer> kList;
	
	/** The worker. */
	private ProblemWorker worker;
	
	/** The output name without extension. */
	private  String outputNameWithoutExtension;
	
	/** The db type. */
	private DBType dbType;
	
	/**
	 * Instantiates a new problem controller.
	 *
	 * @param tab the tab
	 */
	public ProblemController(int tab) {
		loggerImplementation = org.apache.log4j.Logger.getLogger("TAB_" + tab);
		observableLoggerAppender = new ObservableAppender();
		loggerImplementation.addAppender(observableLoggerAppender);
		this.logger = LoggerFactory.getLogger("TAB_" + tab);
	}
	
	/**
	 * Initialize loose.
	 *
	 * @param textFragments the text fragments
	 * @param textConstraints the text constraints
	 * @param textKList the text k list
	 * @return true, if successful
	 */
	public boolean initializeLoose(String textFragments, String textConstraints, String textKList) {
		String message = "ERROR: problem parsing fragments. ";
		try {
			List<List<Object>> fragments = ControllerUtils.getListList(textFragments);
			message = "ERROR: problem parsing constraints. ";
			List<List<Object>> constraints = ControllerUtils.getListList(textConstraints);
			message = "ERROR: problem parsing kList. ";
			kList = ControllerUtils.getList(textKList, Integer.class);
			message = "ERROR: can't inizialize loose. ";
			loose = new GreedyLoose(table, constraints, fragments, logger);
			return true;
		} catch (Exception e) {
			setChanged();
			notifyObservers(Pair.of(ProblemWorkerMessages.ERROR, message + e.getMessage()));
			return false;
		}
	}
	
	/**
	 * Sets the output.
	 *
	 * @param outputNameWithoutExtension the output name without extension
	 * @param dbType the db type
	 */
	public void setOutput(String outputNameWithoutExtension, DBType dbType) {
		this.outputNameWithoutExtension = outputNameWithoutExtension;
		this.dbType = dbType;
	}
	
	/**
	 * Start.
	 *
	 * @param observer the observer
	 * @throws Exception the exception
	 */
	public void start(Observer observer) throws Exception {
		loose.addObserver(observer);
		worker = new ProblemWorker();
		worker.start();
	}
	
	/* (non-Javadoc)
	 * @see gui.controllers.Controller#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return worker != null && worker.isAlive();
	}
	
	/**
	 * The Enum ProblemWorkerStatus.
	 */
	public enum ProblemWorkerMessages { 
		
		/** The associating. */
		ASSOCIATING,
		
		/** The exporting. */
		EXPORTING,
		
		/** The done. */
		DONE,
		
		/** The error. */
		ERROR,
		
		/** The message. */
		MESSAGE,
		
		/** The tablefetched. */
		TABLEFETCHED
	}
	
	/**
	 * The Class ProblemWorker.
	 */
	private class ProblemWorker extends Thread {
		
		/** The result. */
		Triplet<BaseAssociations, BaseFragments, Set<Integer>> result;
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			
			try {
				
				setChanged();
				notifyObservers(Pair.of(ProblemWorkerMessages.ASSOCIATING, table.size()));
				result = loose.associate(kList);
				
				// the result is null if we stopped the computation
				if (result != null) {
					String outputFileName = createOutputFileName();

					setChanged();
					notifyObservers(Pair.of(ProblemWorkerMessages.EXPORTING, outputFileName));
					boolean exportingResult = export(outputFileName);

					setChanged();
					notifyObservers(Pair.of(ProblemWorkerMessages.DONE, exportingResult));
				}
				
			} catch (Exception e) {
				
				setChanged();
				notifyObservers(Pair.of(ProblemWorkerMessages.ERROR, e.getMessage()));
				e.printStackTrace();
				
			}
		}
		
		/**
		 * Creates the output file name.
		 *
		 * @return the string
		 */
		private String createOutputFileName() {
			switch(dbType) {
			case H2:
				return outputNameWithoutExtension + ".loose";
			case SQLITE:
				return outputNameWithoutExtension + ".loose.db";
			default:
				return "";
			}
		}
		
		/**
		 * Export.
		 *
		 * @param outputFileName the output file name
		 * @return true, if successful
		 */
		private boolean export(String outputFileName) {
			switch(dbType) {
			case H2:
				return new H2Exporter(table, result.getSecond(), result.getFirst(), logger).export(outputFileName);
			case SQLITE:
				return new SqliteExporter(table, result.getSecond(), result.getFirst(), logger).export(outputFileName);
			default:
				return false;
			}
		}

		public void stopLoose() {
			loose.stop();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see gui.controllers.Controller#stop()
	 */
	@Override
	public void stop() {
		if (isRunning())
			worker.stopLoose();
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
