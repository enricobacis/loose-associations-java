package gui.views;

import org.apache.log4j.Level;

/**
 * The Enum ErrorLevel wraps log4j levels in an enum.
 */
public enum ErrorLevel { 
	
	/** The error. */
	ERROR (Level.ERROR),
	
	/** The warn. */
	WARN  (Level.WARN),
	
	/** The info. */
	INFO  (Level.INFO),
	
	/** The debug. */
	DEBUG (Level.DEBUG),
	
	/** The trace. */
	TRACE (Level.TRACE);
	
	/** The level. */
	private final Level level;
	
	/**
	 * Instantiates a new error level.
	 *
	 * @param level the log4j level
	 */
	ErrorLevel(Level level) {this.level = level;}
	
	/**
	 * Gets the value as log4j level.
	 *
	 * @return the value
	 */
	public Level getValue() { return level; }
	
}; 