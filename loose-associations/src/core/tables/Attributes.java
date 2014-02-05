package core.tables;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The Class Attributes keeps a list of all the Attributes in a DB Table.
 * It extends a LinkedList adding the addAttribute method which is a shortcut
 * to add a new Attribute and a way to convert indices to names and indices
 * to names (can be really useful).
 */
public class Attributes extends ArrayList<Attribute> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4960885311308673432L;

	/**
	 * Adds the attribute.
	 *
	 * @param name the name
	 * @param type the type
	 */
	public void addAttribute(String name, String type) {
		this.add(new Attribute(name, type));
	}
	
	/**
	 * Gets the names.
	 *
	 * @return the names
	 */
	public List<String> getNames() {
		List<String> result = new LinkedList<String>();
		for (Attribute attribute: this)
			result.add(attribute.getName());
		return result;
	}
	
	/**
	 * Takes a List of Object, they can be Integer of String and returns a List of strings,
	 * if the input was a List<String> it will be returned as is, if there are integers in the input list,
	 * they will be used to index the attributes and they will be converted as names.
	 *
	 * @param indices the list of mixed names and indices in input
	 * @return the list of names
	 * @throws IllegalArgumentException if there are some entries in the list that are not Strings or Integers
	 */
	public List<String> toNames(List<Object> indices) throws IllegalArgumentException {
		List<String> result = new ArrayList<String>();
		for (Object index: indices) {
			if (index instanceof Integer)
				result.add(this.get((Integer) index).getName());
			else if (index instanceof String)
				result.add((String) index);
			else
				throw new IllegalArgumentException("Expecting Integer or String, got " + index.getClass());
		}
			
		return result;
	}
	
	/**
	 * To indices.
	 *
	 * @param fragment the list of names in input
	 * @return a list of indices
	 * @throws IllegalArgumentException if there are some entries in the list that are not Strings or Integers
	 */
	public <F> List<Integer> toIndices(List<F> fragment) throws IllegalArgumentException {
		List<String> attributeNames = getNames();
        List<Integer> result = new ArrayList<Integer>();
        for (Object name: fragment) {
        	if (name instanceof Integer)
        		result.add((Integer) name);
        	else if (name instanceof String) {
        		int index = attributeNames.indexOf(name);
        		if (index == -1)
        			throw new IllegalArgumentException("No attribute named '" + name + "'");
        		result.add(index);
        	} else
        		throw new IllegalArgumentException("Expecting Integer or String, got " + name.getClass());
        }
        return result;
    }

}
