package gui.controllers;

import java.util.Observer;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * The Class ObservableAppender.
 */
public class ObservableAppender extends AppenderSkeleton {
	
	/** The observable delegate. */
	private final ObservableDelegate observableDelegate = new ObservableDelegate();
	
	/**
	 * Adds the observer.
	 *
	 * @param o the observer
	 */
	public void addObserver(Observer o) {
		observableDelegate.addObserver(o);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
	 */
	@Override
	protected void append(LoggingEvent event) {
		observableDelegate.doSetChanged();
		observableDelegate.notifyObservers(event.getLevel() + ": " + event.getMessage());
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.Appender#close()
	 */
	@Override
	public void close() { }

	/* (non-Javadoc)
	 * @see org.apache.log4j.Appender#requiresLayout()
	 */
	@Override
	public boolean requiresLayout() {
		return false;
	}
	
}
