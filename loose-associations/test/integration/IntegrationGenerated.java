package integration;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import utils.tuples.Triplet;
import core.GreedyLoose;
import core.LooseSolver;
import core.associations.BaseAssociations;
import core.fragments.BaseFragments;
import core.tables.RandomTable;

public class IntegrationGenerated {

	@Test
	public void testIntegrationWithRandomTable() throws Exception {

		boolean executedWithoutExceptions =  true;

		try {

			RandomTable table = new RandomTable(1000, 6, 20);

			List<List<Integer>> constraints = Arrays.asList(
					Arrays.asList(1, 2),
					Arrays.asList(3, 4),
					Arrays.asList(0, 5));

			List<List<Integer>> fragments = Arrays.asList(
					Arrays.asList(0, 1),
					Arrays.asList(2, 3),
					Arrays.asList(4, 5));

			List<Integer> kList = Arrays.asList(2, 2, 2);

			LooseSolver loose = new GreedyLoose(table, constraints, fragments);

			Triplet<BaseAssociations, BaseFragments, Set<Integer>> result = loose.associate(kList);
			System.out.println(loose);

			Set<Integer> dropped = result.getThird();
			assertTrue(dropped.isEmpty());

		} catch (Exception e) {
			executedWithoutExceptions = false;
		}

		assertTrue(executedWithoutExceptions);
	}

}
