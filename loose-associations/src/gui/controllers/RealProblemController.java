package gui.controllers;

import utils.tuples.Pair;
import gui.DBType;
import core.tables.H2Table;
import core.tables.SqliteTable;

/**
 * The Class RealProblemController.
 */
public class RealProblemController extends ProblemController {

	/**
	 * Instantiates a new real problem controller.
	 *
	 * @param tab the tab
	 */
	public RealProblemController(int tab) {
		super(tab);
	}

	/**
	 * Creates the table.
	 *
	 * @param type the type
	 * @param inputFile the input file
	 * @param tablename the tablename
	 * @param orderBy the order by
	 * @throws Exception the exception
	 */
	public void createTable(DBType type, String inputFile, String tablename, String orderBy) throws Exception {
		
		if (inputFile == null || inputFile.isEmpty())
			throw new Exception("Missing database");
		
		if (tablename == null || tablename.isEmpty())
			throw new Exception("Missing tablename");
		
		TableReader reader = new TableReader(type, inputFile, tablename, orderBy);
		reader.start();
	}
	
	private class TableReader extends Thread {
		
		DBType type;
		String inputFile;
		String tablename;
		String orderBy;
		
		public TableReader(DBType type, String inputFile, String tablename, String orderBy) {
			this.type = type;
			this.inputFile = inputFile;
			this.tablename = tablename;
			this.orderBy = orderBy;
		}
		
		@Override
		public void run() {
			
			try {
				boolean fetched = false;
				
				switch (type) {
				case H2:
					table = new H2Table(inputFile, tablename, orderBy, null, logger);
					fetched = true;
					break;
				case SQLITE:
					table = new SqliteTable(inputFile, tablename, orderBy, null, logger);
					fetched = true;
					break;
				default:
					setChanged();
					notifyObservers(Pair.of(ProblemWorkerMessages.ERROR, "Unknown database type"));
				}
				
				if (fetched) {
					setChanged();
					notifyObservers(Pair.of(ProblemWorkerMessages.TABLEFETCHED, table.size()));
				}
				
			} catch (Exception e) {
				setChanged();
				logger.debug(e.getMessage());
				// message already logged from the table
				notifyObservers(Pair.of(ProblemWorkerMessages.ERROR, ""));
			}
			
		}
		
	}

}
