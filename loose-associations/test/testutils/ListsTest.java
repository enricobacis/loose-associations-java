package testutils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ListsTest {
	
	List<Integer> source;
	
	@Before
	public void setUp() throws Exception {
		source = Arrays.asList(1, 2);
	}

	@Test
	public void testFromIterator() throws Exception {
		List<Integer> list = Lists.from(source.iterator());
		assertEquals(list, source);
	}
	
	@Test
	public void testFromIterable() throws Exception {
		List<Integer> list = Lists.from(source);
		assertEquals(list, source);
	}

}
