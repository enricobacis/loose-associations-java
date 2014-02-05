package core.tables;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class RowTest {
	
	Row row;

	@Before
	public void setUp() throws Exception {
		row = new Row(1, Arrays.<Object>asList(1, "hello", 3));
	}

	@Test
	public void testRow() throws Exception {
		assertThat(row, notNullValue());
	}

	@Test
	public void testOf() throws Exception {
		Row other = Row.of(1, Arrays.<Object>asList(1, "hello", 3));
		assertEquals(row.id(), other.id());
		assertEquals(row.data(), other.data());
	}

	@Test
	public void testId() throws Exception {
		assertEquals(row.id(), 1);
	}

	@Test
	public void testData() throws Exception {
		assertEquals(row.data(), Arrays.<Object>asList(1, "hello", 3));
	}

}
