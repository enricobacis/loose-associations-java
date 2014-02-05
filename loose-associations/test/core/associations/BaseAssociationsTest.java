package core.associations;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import utils.functional.Generable;

public class BaseAssociationsTest {

	@Test
	public void coverage() throws Exception {
		new BaseAssociations() {
			private static final long serialVersionUID = 1L;
			public void changeGroup(int rowId, int fragmentId, int newGroupId) {}
			public boolean isGroupFull(int fragment, int groupId) {	return false; }
			public Set<Integer> getGroup(int fragmentId, int groupId) {	return null; }
			public int getGroupSize(int fragmentId, int groupId) { return 0; }
			public Map<Integer, Set<Integer>> getGroups(int fragmentId) { return null; }
			public double getAverageGroupSizeInFragment(int fragmentId) { return 0; }
			public double getAverageGroupSize() { return 0; }
			public Generable<List<Integer>> getAssociated(int fragmentId, int groupId) { return null; }
			public boolean exists(int fragment1, int group1, int fragment2, int group2) { return false; }
			
		};
	}

}
