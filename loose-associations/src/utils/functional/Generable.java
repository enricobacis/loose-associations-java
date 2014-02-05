package utils.functional;

import java.util.Iterator;
import java.util.List;

/**
 * The Class Generable represents an Iterable whose elements
 * are computed as requested and not stored.
 *
 * @param <O> the generic type
 */
public class Generable<O> implements Iterable<O> {
	
	/** The already generated checks if the iterator has already been called. */
	protected boolean alreadyGenerated = false;
	
	/** The generator. */
	protected final Generator<?, O> generator;
	
	/**
	 * Instantiates a new generable.
	 *
	 * @param <I> the generic type
	 * @param generator the generator
	 */
	public <I> Generable(Generator<I, O> generator) {
		this.generator = generator;
	}
	
	/**
	 * Instantiates a new generable.
	 *
	 * @param <I> the generic type
	 * @param input the input
	 * @param function the function
	 */
	public <I> Generable(final Iterator<I> input, final Function<I, O> function) {
		this(Generator.from(input, function));
	}
	
	/**
	 * Creates a new Generable from a generator
	 *
	 * @param <I> the generic type
	 * @param <O> the generic type
	 * @param generator the generator
	 * @return the generable
	 */
	public static final <I, O> Generable<O> from(Generator<I, O> generator) {
		return new Generable<O>(generator);
	}
	
	/**
	 * Creates a new Generable from an Iterator input and a function. 
	 *
	 * @param <I> the generic type
	 * @param <O> the generic type
	 * @param input the input
	 * @param function the function
	 * @return the generable
	 */
	public static final <I, O> Generable<O> from(final Iterator<I> input, final Function<I, O> function) {
		return new Generable<O>(input, function);
	}
	
	/**
	 * Creates a new Generable from an Iterable input and a function.
	 *
	 * @param <I> the generic type
	 * @param <O> the generic type
	 * @param input the input
	 * @param function the function
	 * @return the generable
	 */
	public static final <I, O> Generable<O> from(final Iterable<I> input, final Function<I, O> function) {
		return new Generable<O>(input.iterator(), function);
	}
	
	/**
	 * Filter this Generable with a function returning only the elements
	 * that are true for the function (predicate).
	 *
	 * @param function the function
	 * @return the generable
	 */
	public final Generable<O> filter(final Function<O, Boolean> function) {
		Generator<O, O> filtered = Filter.with(this.iterator(), function);
		return new Generable<O>(filtered);
	}
	
	/**
	 * Map the current Generable with a function.
	 *
	 * @param <P> the generic type
	 * @param function the function
	 * @return the generable
	 */
	public final <P> Generable<P> map(final Function<O, P> function) {
		Generator<O, P> mapped = Generator.from(this.iterator(), function);
		return new Generable<P>(mapped);
	}
	
	/**
	 * Zip this generable with other iterables.
	 *
	 * @param iterables the iterables
	 * @return the generable
	 */
	@SafeVarargs
	public final Generable<List<O>> zip(final Iterable<O> ... iterables) {
		Generator<O, List<O>> zipped = Zip.with(this.iterator(), iterables);
		return new Generable<List<O>>(zipped);
	}
	
	/**
	 * Zip longest this generable with other iterables.
	 *
	 * @param iterables the iterables
	 * @return the generable
	 */
	@SafeVarargs
	public final Generable<List<O>> zipLongest(final Iterable<O> ... iterables) {
		Generator<O, List<O>> zipped = Zip.withLongest(this.iterator(), iterables);
		return new Generable<List<O>>(zipped);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<O> iterator() {
		if (alreadyGenerated)
			throw new Error("Generator already generated");
		else
			alreadyGenerated = true;
		
		return generator;
	}

}
