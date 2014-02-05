package core.tables;

/**
 * The Class Attribute represents an attribute in a DB Table
 * with a name and a type as described in the CREATE TABLE. 
 */
public class Attribute {
	
	/** The name of the Attribute. */
	private final String name;
	
	/** The type of the Attribute. */
	private final String type;
	
	/**
	 * Instantiates a new attribute.
	 *
	 * @param name the name of the Attribute
	 * @param type the type of the Attribute
	 */
	public Attribute(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	/**
	 * Static method that allows the user to create a new Attribute
	 * using an alternative syntax instead of the constructor.
	 * We can type Attribute.of(name, type)
	 *
	 * @param name the name of the Attribute
	 * @param type the type of the Attribute
	 * @return the attribute
	 */
	public static Attribute of(String name, String type) {
		return new Attribute(name, type);
	}
	
	/**
	 * Gets the name of the Attribute.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the type of the Attribute.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}	

}
