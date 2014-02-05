package utils.tuples;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class PairTest {

	Pair<Integer, String> pair;
	
	@Before
	public void setUp() throws Exception {
		pair = new Pair<>(1, "hello");
	}

	@Test
	public void testPair() throws Exception {
		assertThat(pair, notNullValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFirstArgumentNullShouldThrow() throws Exception {
		new Pair<Integer, Integer>(null, 1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSecondArgumentNullShouldThrow() throws Exception {
		new Pair<Integer, Integer>(1, null);
	}

	@Test
	public void testGetFirst() throws Exception {
		assertEquals(pair.getFirst(), Integer.valueOf(1));
	}

	@Test
	public void testGetSecond() throws Exception {
		assertEquals(pair.getSecond(), "hello");
	}
	
	@Test
	public void testToString() throws Exception {
		assertEquals(pair.toString(), "Pair [first=1, second=hello]");
	}
	
	@Test
	public void testEqualsIdentity() throws Exception {
		assertEquals(pair, pair);
	}
	
	@Test
	public void testEqualsSameFirstAndSecond() throws Exception {
		assertEquals(pair, new Pair<Integer, String>(pair.getFirst(), pair.getSecond()));
	}
	
	@Test
	public void testOf() throws Exception {
		
		assertEquals(pair, Pair.of(pair.getFirst(), pair.getSecond()));
	}
	
	@Test
	public void testEqualsAnotherClass() throws Exception {
		assertThat(pair, is(not(instanceOf(String.class))));
	}
	
	@Test
	public void testEqualsForDifferentFirst() throws Exception {
		Integer first = 2;
		assertThat(pair.getFirst(), is(not(first)));
		assertThat(pair, is(not(Pair.of(first, pair.getSecond()))));
	}
	
	@Test
	public void testEqualsForDifferentSecond() throws Exception {
		String second = "world";
		assertThat(pair.getSecond(), is(not(second)));
		assertThat(pair, is(not(Pair.of(pair.getFirst(), second))));
	}
	
	@Test
	public void testHashCode() throws Exception {
		Pair<Integer, String> other = Pair.of(pair.getFirst(), pair.getSecond());
		assertEquals(pair, other);
		assertEquals(pair.hashCode(), other.hashCode());
	}

}
