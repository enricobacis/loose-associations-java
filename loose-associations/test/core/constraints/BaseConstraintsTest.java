package core.constraints;

import java.util.Set;

import org.junit.Test;

import core.tables.Row;

public class BaseConstraintsTest {

	@Test
	public void coverage() throws Exception {
		new BaseConstraints() {
			protected Set<Integer> _involvedFragmentsFor(int constraintId) { return null; }
			protected Set<Integer> _constraintsFor(int fragmentId) { return null; }
			public boolean areRowsAlikeFor(Row row1, Row row2, int fragmentId, int constraintId) { return false; }
			public boolean areRowsAlike(Row row1, Row row2, int fragmentId) { return false; }
		};
		
	}

}
