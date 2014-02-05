package utils;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

public class TimerTest {

	Timer timer;
	
	@Before
	public void setUp() throws Exception {
		timer = new Timer();
	}

	@Test
	public void testTimer() throws Exception {
		assertThat(timer, notNullValue());
	}

	@Test
	public void testGet() throws Exception {
		assertTrue(timer.get() < 1);
	}
	
	@Test
	public void testReset() throws Exception {
		timer.reset();
		double first = timer.get();
		timer.reset();
		Thread.sleep(50);
		double second = timer.get();
		assertTrue(first < second);
	}

	@Test
	public void testToString() throws Exception {
		String string = timer.toString();
		assertEquals(string.substring(0, 2), "[0");
		assertEquals(string.substring(5, 7), "s]");
	}

}
