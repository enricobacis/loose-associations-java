package utils.functional;


/**
 * The Class Range represents a Generable range of integers.
 */
public class Range extends Generable<Integer> {

	/**
	 * Instantiates a new range as an until
	 * so new Range(1, 5) = 1, 2, 3, 4.
	 *
	 * @param start the start
	 * @param end the end
	 * @param step the step
	 */
	private Range(int start, int end, int step) {
		super(new RangeGenerator(start, end, step));
	}
	
	/**
	 * Generates the numbers from start UNTIL end with a step.
	 *
	 * @param start the start
	 * @param end the end
	 * @param step the step
	 * @return the range
	 */
	public static Range until(int start, int end, int step) {
		return new Range(start, end, step);
	}

	/**
	 * Generates the numbers from start UNTIL end.
	 *
	 * @param start the start
	 * @param end the end
	 * @return the range
	 */
	public static Range until(int start, int end) {
		return new Range(start, end, 1);
	}
	
	/**
	 * Generates the numbers from start TO end with a step.
	 *
	 * @param start the start
	 * @param end the end
	 * @param step the step
	 * @return the range
	 */
	public static Range to(int start, int end, int step) {
		return new Range(start, end + (int) Math.signum(step), step);
	}
	
	/**
	 * Generates the numbers from start TO end.
	 *
	 * @param start the start
	 * @param end the end
	 * @return the range
	 */
	public static Range to(int start, int end) {
		return new Range(start, end + 1, 1);
	}
	
	/**
	 * The Class RangeGenerator is used to produce the numbers.
	 */
	static class RangeGenerator extends Generator<Integer, Integer> {
		
		/** The start. */
		final int start;
		
		/** The end. */
		final int end;
		
		/** The step. */
		final int step;
		
		/** The next. */
		int next;
		
		/** The forward. */
		final boolean forward;
		
		/**
		 * Instantiates a new range generator as an until.
		 *
		 * @param start the start
		 * @param end the end
		 * @param step the step
		 */
		public RangeGenerator(int start, int end, int step) {
			this.start = start;
			this.end = end;
			this.step = step;
			this.next = start;
			this.forward = step > 0;
		}

		/* (non-Javadoc)
		 * @see functional.Generator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			if (forward)
				return next < end;
			else
				return next > end;
		}

		/* (non-Javadoc)
		 * @see functional.Generator#next()
		 */
		@Override
		public Integer next() {
			int current = next;
			next += step;
			return current;
		}
		
	}

}
