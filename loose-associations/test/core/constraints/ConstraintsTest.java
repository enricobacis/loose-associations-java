package core.constraints;


import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import core.tables.Row;

public class ConstraintsTest {
	
	Constraints constraints;
	List<List<Integer>> cons;
	List<List<Integer>> frags;

	@Before
	public void setUp() throws Exception {
		cons = Arrays.asList(Arrays.asList(0, 1), Arrays.asList(0, 2));
		frags = Arrays.asList(Arrays.asList(0), Arrays.asList(1, 2));
		constraints = new Constraints(cons, frags);
	}
	
	@Test
	public void testConstraints() throws Exception {
		assertThat(constraints, notNullValue());
	}

	@Test
	public void test_involvedFragmentsFor() throws Exception {
		assertEquals(constraints._involvedFragmentsFor(0), new HashSet<Integer>(Arrays.asList(0, 1)));
		assertEquals(constraints._involvedFragmentsFor(1), new HashSet<Integer>(Arrays.asList(0, 1)));
	}
	
	@Test
	public void testInvolvedFragmentsForUsesCache() throws Exception {
		// yes, we really want to use == to check if they are the same object
		assertTrue(constraints.involvedFragmentsFor(0) == constraints.involvedFragmentsFor(0));
	}
	
	@Test
	public void test_constraintsFor() throws Exception {
		assertEquals(constraints._constraintsFor(0), new HashSet<Integer>(Arrays.asList(0, 1)));
		assertEquals(constraints._constraintsFor(1), new HashSet<Integer>(Arrays.asList(0, 1)));
	}
	
	@Test
	public void testConstraintsForUsesCache() throws Exception {
		// yes, we really want to use == to check if they are the same object
		assertTrue(constraints.constraintsFor(0) == constraints.constraintsFor(0));
	}

	@Test
	public void testAreRowsAlikeFor() throws Exception {
		Row row1 = mock(Row.class);
		when(row1.data()).thenReturn(Arrays.<Object>asList(1, 2, 3));
		Row row2 = mock(Row.class);
		when(row2.data()).thenReturn(Arrays.<Object>asList(1, 2, 4));
		Row row3 = mock(Row.class);
		when(row3.data()).thenReturn(Arrays.<Object>asList(2, 2, 3));
		
		assertTrue(constraints.areRowsAlikeFor(row1, row2, 0, 0));
		assertFalse(constraints.areRowsAlikeFor(row2, row3, 0, 0));
		assertTrue(constraints.areRowsAlikeFor(row1, row2, 1, 0));
		assertFalse(constraints.areRowsAlikeFor(row1, row2, 1, 1));
	}

	@Test
	public void testAreRowsAlike() throws Exception {
		Row row1 = mock(Row.class);
		when(row1.data()).thenReturn(Arrays.<Object>asList(1, 2, 3));
		Row row2 = mock(Row.class);
		when(row2.data()).thenReturn(Arrays.<Object>asList(1, 2, 4));
		Row row3 = mock(Row.class);
		when(row3.data()).thenReturn(Arrays.<Object>asList(2, 2, 3));
		
		assertTrue(constraints.areRowsAlike(row1, row2, 0));
		assertTrue(constraints.areRowsAlike(row1, row2, 1));
		assertTrue(constraints.areRowsAlike(row1, row3, 1));
		assertFalse(constraints.areRowsAlike(row1, row3, 0));
	}

}
