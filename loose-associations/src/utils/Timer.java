package utils;

import java.text.DecimalFormat;

/**
 * The Class Timer keeps track of the time passed from its
 * creation or its last reset.
 * 
 * This is not accurate for very small timings since we are
 * using System.currentTimeMillis() instead of the more
 * precise (but also more expensive) System.nanoTime().
 */
public class Timer {
	
	/** The time the timer has been created or reset. */
	private long started;
	private long stopped;
	
	/** The decimal formatter that keeps only 2 decimal places. */
	DecimalFormat df = new DecimalFormat("0.00");

	/**
	 * Instantiates a new timer.
	 */
	public Timer() {
		reset();
	}
	
	/**
	 * Reset the timer.
	 */
	public void reset() {
		started = System.currentTimeMillis();
		stopped = 0;
	}
	
	public void stop() {
		stopped = (System.currentTimeMillis() - started);
	}
	
	/**
	 * Gets the elapsed time since creation or last reset.
	 *
	 * @return the elapsed time in seconds
	 */
	public double get() {
		return ((stopped == 0) ? (System.currentTimeMillis() - started) : stopped) / 1000.0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + df.format(this.get()) + "s]";
	}

}
