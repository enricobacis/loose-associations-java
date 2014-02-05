package utils.functional;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import testutils.Lists;

public class RangeTest {
	
	@Test
	public void testToUpwards() throws Exception {
		assertEquals(Lists.from(Range.to(1, 10)),
				Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
	}
	
	@Test
	public void testToUpwardsWithStep() throws Exception {
		assertEquals(Lists.from(Range.to(1, 10, 3)),
				Arrays.asList(1, 4, 7, 10));
	}
	
	@Test
	public void testToBackwards() throws Exception {
		assertEquals(Lists.from(Range.to(10, 1, -1)),
				Arrays.asList(10, 9, 8, 7, 6, 5, 4, 3, 2, 1));
	}
	
	@Test
	public void testToBackwardWithStep() throws Exception {
		assertEquals(Lists.from(Range.to(10, 1, -3)),
				Arrays.asList(10, 7, 4, 1));
	}
	
	@Test
	public void testToEmpty() throws Exception {
		assertEquals(Lists.from(Range.to(5, 4)),
				Arrays.asList());
	}
	
	@Test
	public void testToEmptyWithStep() throws Exception {
		assertEquals(Lists.from(Range.to(5, 4, 2)),
				Arrays.asList());
	}
	
	@Test
	public void testUntilUpwards() throws Exception {
		assertEquals(Lists.from(Range.until(1, 10)),
				Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
	}
	
	@Test
	public void testUntilUpwardsWithStep() throws Exception {
		assertEquals(Lists.from(Range.until(1, 10, 3)),
				Arrays.asList(1, 4, 7));
	}
	
	@Test
	public void testUntilBackwards() throws Exception {
		assertEquals(Lists.from(Range.until(10, 1, -1)),
				Arrays.asList(10, 9, 8, 7, 6, 5, 4, 3, 2));
	}
	
	@Test
	public void testUntilBackwardWithStep() throws Exception {
		assertEquals(Lists.from(Range.until(10, 1, -3)),
				Arrays.asList(10, 7, 4));
	}
	
	@Test
	public void testUntilEmpty() throws Exception {
		assertEquals(Lists.from(Range.until(5, 3)),
				Arrays.asList());
	}
	
	@Test
	public void testUntilEmptyWithStep() throws Exception {
		assertEquals(Lists.from(Range.until(5, 3, 2)),
				Arrays.asList());
	}

}
