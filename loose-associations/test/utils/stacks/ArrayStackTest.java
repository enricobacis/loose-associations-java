package utils.stacks;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.EmptyStackException;

import org.junit.Before;
import org.junit.Test;


public class ArrayStackTest {
	
	ArrayStack<Integer> stack;
	
	@Before
	public void setUp() throws Exception {
		stack = new ArrayStack<>();
	}

	@Test
	public void testArrayStack() {
		assertThat(stack, notNullValue());
	}

	@Test
	public void testCanCreateArrayStackWithInitialCapacity() {
		new ArrayStack<>(10);
	}
	
	@Test
	public void testInitialSizeIsZero() {
		assertEquals(stack.size(), 0);
	}
	
	@Test
	public void testIsEmpty() {
		assertTrue(stack.isEmpty());
	}
	
	@Test(expected=EmptyStackException.class)
	public void testPeekAnEmptyListThrowsEmptyStackException() {
		stack.peek();
	}
	
	@Test(expected=EmptyStackException.class)
	public void testPopAnEmptyListThrowsEmptyStackException() {
		stack.pop();
	}
	
	@Test
	public void testPushedItemIsReturned() {
		assertEquals(stack.push(0), Integer.valueOf(0));
	}
	
	@Test
	public void testSizeAfterOnePushIsOne() {
		stack.push(0);
		assertEquals(stack.size(), 1);
	}
	
	@Test
	public void testPushAndPop() {
		stack.push(0);
		assertEquals(stack.pop(), Integer.valueOf(0));
	}
	
	@Test
	public void testPushAndPeekTwoTimesGetsTheSameElement() {
		stack.push(0);
		assertEquals(stack.peek(), Integer.valueOf(0));
		assertEquals(stack.peek(), Integer.valueOf(0));
	}
	
	@Test
	public void testSizeIncreasesAfterPush() {
		stack.push(0);
		assertEquals(stack.size(), 1);
		stack.push(0);
		assertEquals(stack.size(), 2);
	}
	
	@Test
	public void testSizeDecreasesAfterPop() {
		stack.push(0);
		stack.push(0);
		assertEquals(stack.size(), 2);
		stack.pop();
		assertEquals(stack.size(), 1);
	}
	
	@Test
	public void testSizeIsTheSameWithPeek() {
		stack.push(0);
		stack.push(0);
		assertEquals(stack.size(), 2);
		stack.peek();
		assertEquals(stack.size(), 2);
	}

	@Test
	public void testAccessIsLifo() {
		stack.push(1);
		stack.push(2);
		assertEquals(stack.pop(), Integer.valueOf(2));
		assertEquals(stack.pop(), Integer.valueOf(1));
	}

}
