package core.associations;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import testutils.Lists;

public class AssociationsTest {
	
	Associations associations;
	List<Integer> kList;
	double epsilon = 0.001;

	@Before
	public void setUp() throws Exception {
		kList = Arrays.asList(2, 2);
		associations = new Associations(kList);
	}
	
	@Test
	public void testAssociations() throws Exception {
		assertThat(associations, notNullValue());
	}
	
	@Test
	public void testPut() throws Exception {
		assertFalse(associations.containsKey(0));
		List<Integer> list = Arrays.asList(1, 2);
		associations.put(0, list);
		assertTrue(associations.containsKey(0));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPutThrowsIfKListBigger() throws Exception {
		associations.put(0, Arrays.asList(1, 2, 3));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPutThrowsIfKListSmaller() throws Exception {
		associations.put(0, Arrays.asList(1));
	}
	
	@Test
	public void testGetExistingRow() throws Exception {
		List<Integer> list = Arrays.asList(1, 2);
		associations.put(0, list);
		assertEquals(associations.get(0), list);
	}
	
	@Test
	public void testGetNonExistingRow() throws Exception {
		assertEquals(associations.get(999), Arrays.asList());
	}

	@Test
	public void testRemove() throws Exception {
		List<Integer> list = Arrays.asList(1, 2);
		associations.put(0, list);
		associations.remove(0);
		assertFalse(associations.containsKey(0));
		assertEquals(associations.get(0), Arrays.asList());
	}
	
	@Test
	public void testEmptyGroupIsNotFull() throws Exception {
		assertFalse(associations.isGroupFull(0, 0));
	}

	@Test
	public void testIsGroupFull() throws Exception {
		List<Integer> list = Arrays.asList(1, 2);
		associations.put(0, list);
		assertFalse(associations.isGroupFull(0, 1));
		assertFalse(associations.isGroupFull(1, 2));
		associations.put(1, list);
		assertTrue(associations.isGroupFull(0, 1));
		assertTrue(associations.isGroupFull(1, 2));
		associations.remove(1);
		assertFalse(associations.isGroupFull(0, 1));
		assertFalse(associations.isGroupFull(1, 2));
	}
	
	@Test
	public void testGetEmptyGroup() throws Exception {
		assertTrue(associations.getGroup(0, 0).isEmpty());
	}
	
	@Test
	public void testGetNonEmptyGroup() throws Exception {
		associations.put(100, Arrays.asList(1, 2));
		assertEquals(associations.getGroup(0, 1), new HashSet<>(Arrays.asList(100)));
		assertEquals(associations.getGroup(1, 2), new HashSet<>(Arrays.asList(100)));
	}

	@Test
	public void testChangeGroupForExistingRow() throws Exception {
		associations.put(0, Arrays.asList(1, 2));
		associations.changeGroup(0, 0, 5);
		assertEquals(associations.get(0), Arrays.asList(5, 2));
		assertTrue(associations.getGroup(0, 1).isEmpty());
		assertEquals(associations.getGroup(0, 5), new HashSet<>(Arrays.asList(0)));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testChangeGroupForNonExistingRow() throws Exception {
		associations.changeGroup(999, 0, 5);
	}

	@Test
	public void testGetGroupSize() throws Exception {
		assertEquals(associations.getGroupSize(0, 1), 0);
		associations.put(0, Arrays.asList(1, 2));
		assertEquals(associations.getGroupSize(0, 1), 1);
		associations.remove(0);
		assertEquals(associations.getGroupSize(0, 1), 0);
	}

	@Test
	public void testGetGroups() throws Exception {
		associations.put(0, Arrays.asList(1, 2));
		assertTrue(associations.getGroups(0).containsKey(1));
		assertTrue(associations.getGroups(1).containsKey(2));
	}

	@Test
	public void testGetAverageGroupSizeInFragment() throws Exception {
		associations.put(0, Arrays.asList(1, 2));
		associations.put(1, Arrays.asList(1, 3));
		assertEquals(associations.getAverageGroupSizeInFragment(0), 2, epsilon);
		assertEquals(associations.getAverageGroupSizeInFragment(1), 1, epsilon);
	}

	@Test
	public void testGetAverageGroupSize() throws Exception {
		associations.put(0, Arrays.asList(1, 2));
		associations.put(1, Arrays.asList(1, 3));
		assertEquals(associations.getAverageGroupSize(), 1.5, epsilon);
	}

	@Test
	public void testGetAssociated() throws Exception {
		List<Integer> ass0 = Arrays.asList(1, 2);
		List<Integer> ass1 = Arrays.asList(1, 3);
		associations.put(0, ass0);
		associations.put(1, ass1);
		assertEquals(Lists.from(associations.getAssociated(0, 1)), Arrays.asList(ass0, ass1));
		assertEquals(Lists.from(associations.getAssociated(1, 2)), Arrays.asList(ass0));
		assertEquals(Lists.from(associations.getAssociated(1, 3)), Arrays.asList(ass1));
	}

	@Test
	public void testExists() throws Exception {
		assertFalse(associations.exists(0, 1, 1, 2));
		associations.put(0, Arrays.asList(1, 2));
		assertTrue(associations.exists(0, 1, 1, 2));
	}

}
