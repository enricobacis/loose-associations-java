package gui.controllers;

import java.util.Observer;

import org.apache.log4j.Level;

/**
 * The Interface Controller.
 */
public interface Controller {
	
	/**
	 * Checks if is running.
	 *
	 * @return true, if is running
	 */
	public abstract boolean isRunning();

	/**
	 * Stop.
	 */
	public abstract void stop();
	
	/**
	 * Adds the logger observer.
	 *
	 * @param o the observer
	 */
	public abstract void addLoggerObserver(Observer o);

	/**
	 * Sets the logger level.
	 *
	 * @param level the new logger level
	 */
	void setLoggerLevel(Level level);

}
