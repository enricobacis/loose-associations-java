package core.fragments;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FragmentTest {
	
	int id;
	List<Integer> attributes;
	Fragment fragment;

	@Before
	public void setUp() throws Exception {
		id = 1;
		attributes = Arrays.asList(1, 2, 3); 
		fragment = new Fragment(1, attributes);
	}

	@Test
	public void testFragment() throws Exception {
		assertThat(fragment, notNullValue());
	}

	@Test
	public void testK() throws Exception {
		int k = 5;
		fragment.setK(k);
		assertEquals(fragment.getK(), k);
	}

	@Test
	public void testGetAttributes() throws Exception {
		assertEquals(fragment.getAttributes(), attributes);
	}

	@Test
	public void testPointers() throws Exception {
		int firstNonfull = 100;
		fragment.setFirstNonfull(firstNonfull);
		assertEquals(fragment.getFirstNonfull(), firstNonfull);
		
		int lastUsable = 100;
		fragment.setLastUsable(lastUsable);
		assertEquals(fragment.getLastUsable(), lastUsable);
		
		fragment.resetPointers();
		assertEquals(fragment.getFirstNonfull(), 0);
		assertEquals(fragment.getLastUsable(), 0);
	}

	@Test
	public void testSetLastUsableIfBigger() throws Exception {
		fragment.setLastUsable(100);
		assertEquals(fragment.getLastUsable(), 100);
		
		fragment.setLastUsableIfBigger(50);
		assertEquals(fragment.getLastUsable(), 100);
		
		fragment.setLastUsableIfBigger(150);
		assertEquals(fragment.getLastUsable(), 150);
	}

}
