package utils.tuples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ComparablePairTest {
	
	ComparablePair<Integer, String> cp1;
	
	@Before
	public void setUp() throws Exception {
		cp1 = new ComparablePair<>(1, "b");
	}
	
	@Test
	public void testWithFirstLower() throws Exception {
		ComparablePair<Integer, String> cp2 = new ComparablePair<>(0, "b");
		assertTrue(cp2.getFirst().compareTo(cp1.getFirst()) < 0);
		assertTrue(cp2.compareTo(cp1) < 0);
	}
	
	@Test
	public void testWithFirstHigher() throws Exception {
		ComparablePair<Integer, String> cp2 = new ComparablePair<>(2, "b");
		assertTrue(cp2.getFirst().compareTo(cp1.getFirst()) > 0);
		assertTrue(cp2.compareTo(cp1) > 0);
	}
	
	@Test
	public void testWithSameFirstSecondLower() throws Exception {
		ComparablePair<Integer, String> cp2 = new ComparablePair<>(1, "a");
		assertEquals(cp2.getFirst().compareTo(cp1.getFirst()), 0);
		assertTrue(cp2.getSecond().compareTo(cp1.getSecond()) < 0);
		assertTrue(cp2.compareTo(cp1) < 0);
	}
	
	@Test
	public void testWithSameFirstSecondHigher() throws Exception {
		ComparablePair<Integer, String> cp2 = new ComparablePair<>(1, "c");
		assertEquals(cp2.getFirst().compareTo(cp1.getFirst()), 0);
		assertTrue(cp2.getSecond().compareTo(cp1.getSecond()) > 0);
		assertTrue(cp2.compareTo(cp1) > 0);
	}
	
	@Test
	public void testEqualsShouldReturnZero() throws Exception {
		ComparablePair<Integer, String> cp2 = new ComparablePair<>(cp1.getFirst(), cp1.getSecond());
		assertEquals(cp2, cp1);
		assertEquals(cp2.compareTo(cp1), 0);
		}

}
