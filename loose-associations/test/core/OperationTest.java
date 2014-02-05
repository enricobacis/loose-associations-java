package core;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

// CodePro doesn't like this import
// import core.Operation.Action;

public class OperationTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testOperation() throws Exception {
		Operation operation = new Operation(Operation.Action.DELETE, 0);
		assertThat(operation, notNullValue());
	}
	
	@Test
	public void testActionsHaveDifferentValues() throws Exception {
		assertThat(Operation.Action.valueOf("DELETE"), is(not(Operation.Action.valueOf("REDISTRIBUTE"))));
	}

	@Test
	public void testOf() throws Exception {
		Operation operation = new Operation(Operation.Action.DELETE, 0);
		assertEquals(operation, Operation.of(Operation.Action.DELETE, 0));
	}
	
	@Test
	public void testGetActionWhenDelete() throws Exception {
		Operation delete = Operation.of(Operation.Action.DELETE);
		assertEquals(delete.getAction(), Operation.Action.DELETE);
	}
	
	@Test
	public void testGetActionWhenRedistribute() throws Exception {
		Operation delete = Operation.of(Operation.Action.REDISTRIBUTE);
		assertEquals(delete.getAction(), Operation.Action.REDISTRIBUTE);
	}

	@Test
	public void testOfRedistribution() throws Exception {
		Operation redistribution = new Operation(Operation.Action.REDISTRIBUTE, 0, 1);
		assertEquals(redistribution, Operation.ofRedistribution(0, 1));
	}

	@Test
	public void testOfDeletion() throws Exception {
		Operation deletion = new Operation(Operation.Action.DELETE, 0);
		assertEquals(deletion, Operation.ofDeletion(0));
	}

	@Test
	public void testGetArgsWhenNoArgs() throws Exception {
		Operation operation = Operation.ofDeletion();
		assertArrayEquals(operation.getArgs(), new int[]{});
	}
	
	@Test
	public void testGetArgsWhenOneArgs() throws Exception {
		Operation operation = Operation.ofDeletion(0);
		assertArrayEquals(operation.getArgs(), new int[]{0});
	}
	
	@Test
	public void testGetArgsWhenMultipleArgs() throws Exception {
		Operation operation = Operation.ofDeletion(0, 1);
		assertArrayEquals(operation.getArgs(), new int[]{0, 1});
	}
	
	@Test
	public void testToStringWhenDelete() throws Exception {
		Operation deletion = Operation.ofDeletion(0);
		assertEquals(deletion.toString(), "Operation [action=DELETE, args=[0]]");
	}
	
	@Test
	public void testToStringWhenRedistribution() throws Exception {
		Operation redistribution = Operation.ofRedistribution(0, 1);
		assertEquals(redistribution.toString(), "Operation [action=REDISTRIBUTE, args=[0, 1]]");
	}
	
	@Test
	public void testEqualsOfIdentity() throws Exception {
		Operation operation = Operation.ofDeletion(0);
		assertEquals(operation, operation);
	}
	
	@Test
	public void testEqualsDifferentClass() throws Exception {
		Operation operation = Operation.ofDeletion();
		assertThat(operation, is(not(instanceOf(String.class))));
	}
	
	@Test
	public void testEqualsDifferentAction() throws Exception {
		Operation deletion = Operation.ofDeletion();
		Operation redistribution = Operation.ofRedistribution();
		assertThat(deletion, is(not(redistribution)));
	}
	
	@Test
	public void testEqualsDifferentArgs() throws Exception {
		Operation op1 = Operation.ofDeletion(0);
		Operation op2 = Operation.ofDeletion(0, 1);
		assertThat(op1, is(not(op2)));
	}
	
	@Test
	public void testHashCode() throws Exception {
		Operation op1 = Operation.ofDeletion(0, 4, 8);
		Operation op2 = Operation.ofDeletion(op1.getArgs());
		assertEquals(op1.hashCode(), op2.hashCode());
	}

}
