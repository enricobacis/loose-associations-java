package gui.controllers;

import java.util.LinkedList;
import java.util.List;

/**
 * The Class ControllerUtils.
 */
public abstract class ControllerUtils {
	
	/**
	 * Gets the list.
	 *
	 * @param <T> the generic type
	 * @param input the input
	 * @param clazz the clazz
	 * @return the list
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getList(String input, Class<T> clazz) throws Exception {
		List<T> output = new LinkedList<T>();
		for (Object o: getList(input))
			output.add((T) o);
		return output;
	}
	
	/**
	 * Gets the list.
	 *
	 * @param input the input
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<Object> getList(String input) throws Exception {
		List<Object> output = new LinkedList<Object>();
		
		if (input.isEmpty() || input.charAt(0) != '[' || !input.endsWith("]"))
			throw new Exception("Missing square bracket in " + input);
		input = input.substring(1, input.length() - 1).trim();

		for (String elem: input.split(",")) {
			elem = elem.trim();
			if (isInteger(elem))
				output.add(Integer.parseInt(elem));
			else
				output.add(elem);
		}
		return output;
	}
	
	/**
	 * Checks if is integer.
	 *
	 * @param string the string
	 * @return true, if is integer
	 */
	public static boolean isInteger(String string) {
		
		// the usual way involves an exception handling, but CodePro
		// is not happy with that:
		
        //	try {
		//		Integer.parseInt(string);
		//		return true;
		//	} catch (NumberFormatException e) {
		//		return false
		//	}
		
		return string.matches("[+-]?\\d+");
	}
	
	/**
	 * Gets the list list.
	 *
	 * @param input the input
	 * @return the list list
	 * @throws Exception the exception
	 */
	public static List<List<Object>> getListList(String input) throws Exception {
		input = input.trim();
		if (input.isEmpty() || input.charAt(0) != '[' || !input.endsWith("]"))
			throw new Exception("Missing square bracket in " + input);
		input = input.substring(1, input.length() - 1).trim();
		
		if (input.isEmpty() || input.charAt(0) != '[' || !input.endsWith("]"))
			throw new Exception("Missing square bracket in " + input);
		input = input.substring(1, input.length() - 1).trim();
		
		List<List<Object>> output = new LinkedList<List<Object>>();
		for (String list: input.split("\\]\\s*,\\s*\\["))
			output.add(getList("["+list.trim()+"]"));
		return output;
	}
	
}
