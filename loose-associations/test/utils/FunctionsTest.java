package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static utils.Functions.average;
import static utils.Functions.neighbors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import testutils.Lists;
import utils.functional.Generable;

public class FunctionsTest {
	
	final double epsilon = 0.00001;
	
	@Test
	public void testAverageOfNullShouldBeZero() throws Exception {
		assertEquals(average(null), 0, 0);
	}
	
	@Test
	public void testAverageOfEmptyListShouldBeZero() throws Exception {
		List<Integer> list = new ArrayList<>();
		assertEquals(average(list), 0, 0);
	}
	
	@Test
	public void testAverageOfSingleNumber() throws Exception {
		List<Integer> list = Arrays.asList(1);
		assertEquals(average(list), 1, 0);
	}
	
	@Test
	public void testAverageOfSingleNegativeNumber() throws Exception {
		List<Integer> list = Arrays.asList(-1);
		assertEquals(average(list), -1, 0);
	}
	
	@Test
	public void testAverageOfTwoNumbers() throws Exception {
		List<Integer> list = Arrays.asList(2, 4);
		assertEquals(average(list), 3, epsilon);
	}
	
	@Test
	public void testAverageWithMixedSigns() throws Exception {
		List<Integer> list = Arrays.asList(-2, 2);
		assertEquals(average(list), 0, epsilon);
	}
	
	@Test
	public void testAverageOfSeveralNumbers() throws Exception {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
		assertEquals(average(list), 5, epsilon);
	}
	
	@Test
	public void testAverageOfSeveralMixedSignsNumbers() throws Exception {
		List<Integer> list = Arrays.asList(-4, -3, -2, -1, 0, 1, 2, 3, 4);
		assertEquals(average(list), 0, epsilon);
	}
	
	@Test
	public void testEmptyNeighbours() throws Exception {
		Generable<Integer> list = neighbors(0, 0, 0);
		assertFalse(list.iterator().hasNext());
	}
	
	@Test
	public void testWithNEqualsToLowerBound() throws Exception {
		List<Integer> list = Lists.from(neighbors(0, 0, 5));
		assertEquals(list, Arrays.asList(1, 2, 3, 4, 5));
	}
	
	@Test
	public void testWithNEqualsToUpperBound() throws Exception {
		List<Integer> list = Lists.from(neighbors(10, 5, 10));
		assertEquals(list, Arrays.asList(9, 8, 7, 6, 5));
	}
	
	@Test
	public void testWithNSymmetricalInRange() throws Exception {
		List<Integer> list = Lists.from(neighbors(5, 0, 10));
		assertEquals(list, Arrays.asList(4, 6, 3, 7, 2, 8, 1, 9, 0, 10));
	}
	
	@Test
	public void testWithNNonSymmetricalInRange() throws Exception {
		List<Integer> list = Lists.from(neighbors(3, 0, 10));
		assertEquals(list, Arrays.asList(2, 4, 1, 5, 0, 6, 7, 8, 9, 10));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWithNOutOfRangeDown() throws Exception {
		neighbors(-1, 0, 5);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWithNOutOfRangeUp() throws Exception {
		neighbors(6, 0, 5);
	}
	
	@Test
	public void testRemoveIsUneffective() throws Exception {
		Iterator<Integer> iter = neighbors(0, 0, 5).iterator();
		iter.remove();
		List<Integer> list = Lists.from(iter);
		assertEquals(list, Arrays.asList(1, 2, 3, 4, 5));
	}
	
	@Test
	public void coverage(){
	    new Functions() { };
	}
	
}
