package core.fragments;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FragmentsTest {
	
	List<Integer> f0;
	List<Integer> f1;
	List<List<Integer>> list;
	Fragments fragments;

	@Before
	public void setUp() throws Exception {
		f0 = Arrays.asList(1, 2);
		f1 = Arrays.asList(3, 4);
		list = Arrays.asList(f0, f1);
		fragments = new Fragments(list);
	}
	
	@Test
	public void testFragments() throws Exception {
		assertThat(fragments, notNullValue());
	}

	@Test
	public void testGet() throws Exception {
		assertEquals(fragments.get(0).getAttributes(), f0);
		assertEquals(fragments.get(1).getAttributes(), f1);
	}

	@Test
	public void testSize() throws Exception {
		assertEquals(fragments.size(), list.size());
	}

	@Test
	public void testIterator() throws Exception {
		int i = 0;
		for (Fragment fragment: fragments)
			assertEquals(fragment.getAttributes(), list.get(i++));
		assertEquals(i, list.size());
	}

}
