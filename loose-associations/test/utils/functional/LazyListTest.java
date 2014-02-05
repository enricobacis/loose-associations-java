package utils.functional;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import testutils.Lists;

public class LazyListTest {

	Iterable<Integer> source;
	LazyList<Integer> lazylist;
	
	@Before
	public void setUp() throws Exception {
		source = Arrays.asList(1, 2, 3);
		lazylist = new LazyList<>(source);
	}
	
	@Test
	public void testIterator() throws Exception {
		assertEquals(Lists.from(lazylist), source);
	}
	
	@Test
	public void testFrom() throws Exception {
		assertEquals(Lists.from(LazyList.from(source)), source);
	}

	@Test
	public void testInsertAtBeginning() throws Exception {
		lazylist.insert(0);
		assertEquals(Lists.from(lazylist), Arrays.asList(0, 1, 2, 3));
	}
	
	@Test
	public void testInsertInMiddle() throws Exception {
		lazylist.iterator().next();
		lazylist.insert(0);
		assertEquals(Lists.from(lazylist), Arrays.asList(1, 0, 2, 3));
	}
	
	@Test
	public void testRemoveIsUneffective() throws Exception {
		Iterator<Integer> iter = lazylist.iterator();
		iter.remove();
		assertEquals(Lists.from(iter), source);
	}
	
	@Test
	public void testValuesAreStored() throws Exception {
		assertEquals(Lists.from(lazylist.iterator()), Lists.from(lazylist.iterator()));
	}
	
	@Test
	public void testStructureIsLazy() throws Exception {
		Iterator<Integer> iter = source.iterator();
		lazylist = LazyList.from(iter);
		
		// iter hasn't advanced
		assertEquals(iter.next(), Integer.valueOf(1));
	}

}
