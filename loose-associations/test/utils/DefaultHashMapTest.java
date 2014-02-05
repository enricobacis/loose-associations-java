package utils;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DefaultHashMapTest {
	
	DefaultHashMap<Integer, List<Integer>> hashmap;

	@Before
	public void setUp() {
		hashmap = new DefaultHashMap<>(ArrayList.class);
	}

	@Test
	public void testDefaultHashMap() {
		assertThat(hashmap, notNullValue());
	}
	
	@Test(expected=Error.class)
	public void testDefaultHashMapThrowsWithNull() {
		DefaultHashMap<Integer, List<Integer>> nullhashmap = new DefaultHashMap<>(null);
		nullhashmap.get(999);
	}
	
	@Test
	public void testDefaultHashMapWithString() {
		DefaultHashMap<String, String> stringhashmap = new DefaultHashMap<>(String.class);
		assertEquals(stringhashmap.get("zzz"), "");
	}

	@Test
	public void testGetExistingElement() {
		List<Integer> item = Arrays.asList(1, 2); 
		hashmap.put(5, item);
		assertEquals(hashmap.get(5), item);
	}
	
	@Test
	public void testGetNonExistingElement() {
		List<Integer> item = new ArrayList<>();
		assertEquals(hashmap.get(999), item);
	}
	
	@Test
	public void testContainsExistingElement() {
		List<Integer> item = Arrays.asList(1, 2); 
		hashmap.put(5, item);
		assertTrue(hashmap.containsKey(5));
		assertTrue(hashmap.containsValue(item));
	}
	
	@Test
	public void testContainsNonExistingElement() {
		List<Integer> item = new ArrayList<>();
		assertFalse(hashmap.containsKey(999));
		assertFalse(hashmap.containsValue(item));
	}

}
