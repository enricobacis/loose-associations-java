package gui.controllers;

import utils.tuples.Pair;
import core.tables.RandomTable;

/**
 * The Class RandomProblemController.
 */
public class RandomProblemController extends ProblemController {

	/**
	 * Instantiates a new random problem controller.
	 *
	 * @param tab the tab
	 */
	public RandomProblemController(int tab) {
		super(tab);
	}

	/**
	 * Creates the table.
	 *
	 * @param textTuples the text tuples
	 * @param textAttributes the text attributes
	 * @param textMaxvalue the text maxvalue
	 * @throws Exception the exception
	 */
	public void createTable(String textTuples, String textAttributes, String textMaxvalue) throws Exception {
		String field = "tuples";
		try {
			int tuples = Integer.parseInt(textTuples);
			field = "attributes";
			int attributes = Integer.parseInt(textAttributes);
			field = "maxvalue";
			int maxvalue = Integer.parseInt(textMaxvalue);
			table = new RandomTable(tuples, attributes, maxvalue, logger);
			
			setChanged();
			notifyObservers(Pair.of(ProblemWorkerMessages.TABLEFETCHED, tuples));
		} catch (NumberFormatException nfe) {
			throw new Exception("ERROR: " + field + " not a number");
		}
	}

}
