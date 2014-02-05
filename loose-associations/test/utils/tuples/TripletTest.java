package utils.tuples;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

public class TripletTest {
	
	Triplet<Integer, Boolean, String> triplet; 

	@Before
	public void setUp() throws Exception {
		triplet = new Triplet<>(1, true, "hello");
	}
	
	@Test
	public void testTriplet() throws Exception {
		assertThat(triplet, notNullValue());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testThirdArgumentNullShouldThrow() throws Exception {
		new Triplet<Integer, Integer, Integer>(1, 1, null);
	}

	@Test
	public void testGetThird() throws Exception {
		assertEquals(triplet.getThird(), "hello");
	}
	
	@Test
	public void testToString() throws Exception {
		assertEquals(triplet.toString(), "Triplet [first=1, second=true, third=hello]");
	}
	
	@Test
	public void testEqualsIdentity() throws Exception {
		assertEquals(triplet, triplet);
	}
	
	@Test
	public void testEqualsSameFirstAndSecondAndThird() throws Exception {
		assertEquals(triplet, new Triplet<Integer, Boolean, String>(triplet.getFirst(), triplet.getSecond(), triplet.getThird()));
	}
	
	@Test
	public void testOf() throws Exception {
		assertEquals(triplet, Triplet.of(triplet.getFirst(), triplet.getSecond(), triplet.getThird()));
	}
	
	@Test
	public void testEqualsAnotherClass() throws Exception {
		assertThat(triplet, is(not(instanceOf(String.class))));
	}
	
	@Test
	public void testEqualsForDifferentThird() throws Exception {
		String third = "world";
		assertThat(triplet.getThird(), is(not(third)));
		assertThat(triplet, is(not(Triplet.of(triplet.getFirst(), triplet.getSecond(), third))));
	}
	
	@Test
	public void testHashCode() throws Exception {
		Triplet<Integer, Boolean, String> other = Triplet.of(triplet.getFirst(), triplet.getSecond(), triplet.getThird());
		assertEquals(triplet.hashCode(), other.hashCode());
	}

}
