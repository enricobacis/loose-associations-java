package acceptance;

import java.util.List;
import java.util.Set;

import core.associations.BaseAssociations;
import core.constraints.BaseConstraints;
import core.fragments.BaseFragments;
import core.fragments.Fragment;
import core.tables.BaseTable;

public class Checker {
	
	private final BaseTable table;
	private final BaseFragments fragments;
	private final BaseConstraints constraints;
	private final BaseAssociations associations;

	public Checker(BaseTable table, BaseFragments fragments, BaseConstraints constraints, BaseAssociations associations) {
		this.table = table;
		this.fragments = fragments;
		this.constraints = constraints;
		this.associations = associations;
	}
	
	public boolean checkAll() {
		return (checkMinimumGroupSizes() &&
				checkGroupHeterogenity() &&
				checkAssociationHeterogenity() &&
				checkDeepHeterogenity());
	}
	
	public boolean checkMinimumGroupSizes() {
		for (Fragment fragment: fragments)
			for (Set<Integer> group: associations.getGroups(fragment.id).values())
				if (group.size() > 0 && group.size() < fragment.getK())
					return false;
		return true;
	}
	
	public boolean checkGroupHeterogenity() {
		for (Fragment fragment: fragments)
			for (Set<Integer> group: associations.getGroups(fragment.id).values())
				for (int row1: group)
					for (int row2: group)
						if (row1 != row2)
							if (constraints.areRowsAlike(table.get(row1), table.get(row2), fragment.id))
								return false;
		return true;
	}
	
	private <T> int countEquals(List<T> l1, List<T> l2) {
		int count = 0;
		for (int i = 0; i < Math.min(l1.size(), l2.size()); ++i)
			if (l1.get(i).equals(l2.get(i)))
				count += 1;
		return count;
	}
	
	public boolean checkAssociationHeterogenity() {
		for (List<Integer> association1: associations.values())
			for (int fragmentId = 0; fragmentId < association1.size(); ++fragmentId)
				for (List<Integer> association2: associations.getAssociated(fragmentId, association1.get(fragmentId)))
					if (!association1.equals(association2))
						if (countEquals(association1, association2) > 1)
							return false;
		return true;
	}
	
	private boolean areGroupsAlike(int group1, int group2, int fragmentId, int constraintId) {
		for (int row1: associations.getGroup(fragmentId, group1))
			for (int row2: associations.getGroup(fragmentId, group2))
				if (constraints.areRowsAlikeFor(table.get(row1), table.get(row2), fragmentId, constraintId))
					return true;
		return false;
	}
	
	public boolean checkDeepHeterogenity() {
		for (List<Integer> association1: associations.values())
			for (int fragment1 = 0; fragment1 < fragments.size(); ++fragment1)
				for (List<Integer> association2: associations.getAssociated(fragment1, association1.get(fragment1)))
					if (!association1.equals(association2))
						for (int constraintId: constraints.constraintsFor(fragment1)) {
							boolean found = false;
							for (int fragment2: constraints.involvedFragmentsFor(constraintId))
								if (fragment1 != fragment2)
									if (!areGroupsAlike(association1.get(fragment2), association2.get(fragment2), fragment2, constraintId)) {
										found = true;
										break;
									}
							if (!found)
								return false;
						}
		return true;		
	}

}
