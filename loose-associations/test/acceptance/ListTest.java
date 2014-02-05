package acceptance;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import utils.tuples.Triplet;
import core.GreedyLoose;
import core.associations.BaseAssociations;
import core.fragments.BaseFragments;
import core.tables.RandomTable;

public class ListTest {

	@Test
	public void testIntegrationWithRandomTable() throws Exception {
		
		RandomTable table = new RandomTable(1000, 7, 70);

		List<List<Integer>> constraints = Arrays.asList(
				Arrays.asList(1, 2),
				Arrays.asList(3, 4),
				Arrays.asList(0, 5),
				Arrays.asList(1, 3, 6));

		List<List<Integer>> fragments = Arrays.asList(
				Arrays.asList(0, 1),
				Arrays.asList(2, 3),
				Arrays.asList(4, 5, 6));

		List<Integer> kList = Arrays.asList(4, 3, 4);

		GreedyLoose loose = new GreedyLoose(table, constraints, fragments);

		Triplet<BaseAssociations, BaseFragments, Set<Integer>> result = loose.associate(kList);
		System.out.println(loose);
		
		Checker checker = new Checker(table, result.getSecond(), loose.getConstraints(), result.getFirst());
		assertTrue(checker.checkMinimumGroupSizes());
		assertTrue(checker.checkGroupHeterogenity());
		assertTrue(checker.checkAssociationHeterogenity());
		assertTrue(checker.checkDeepHeterogenity());
	}
	

}
