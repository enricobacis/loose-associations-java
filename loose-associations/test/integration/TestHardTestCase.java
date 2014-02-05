package integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import utils.tuples.Triplet;
import core.GreedyLoose;
import core.LooseSolver;
import core.associations.BaseAssociations;
import core.fragments.BaseFragments;
import core.tables.Attributes;
import core.tables.ListTable;
import static org.junit.Assert.*;

public class TestHardTestCase {

	@Test
	public void testHardTestCase() throws Exception {

		boolean executedWithoutExceptions =  true;

		try {

			Attributes attributes = new Attributes();
			attributes.addAttribute("a", "INTEGER");
			attributes.addAttribute("b", "INTEGER");

			ArrayList<List<Object>> data = new ArrayList<>();
			data.add(Arrays.asList((Object) 10, (Object) 10));
			data.add(Arrays.asList((Object) 11, (Object) 11));
			data.add(Arrays.asList((Object) 12, (Object) 12));
			data.add(Arrays.asList((Object) 13, (Object) 13));

			data.add(Arrays.asList((Object) 6, (Object) 6));
			data.add(Arrays.asList((Object) 7, (Object) 7));
			data.add(Arrays.asList((Object) 8, (Object) 8));
			data.add(Arrays.asList((Object) 9, (Object) 9));

			data.add(Arrays.asList((Object) 1, (Object) 1));
			data.add(Arrays.asList((Object) 1, (Object) 1));
			data.add(Arrays.asList((Object) 2, (Object) 2));
			data.add(Arrays.asList((Object) 2, (Object) 5));
			data.add(Arrays.asList((Object) 3, (Object) 3));
			data.add(Arrays.asList((Object) 4, (Object) 4));
			data.add(Arrays.asList((Object) 4, (Object) 4));
			data.add(Arrays.asList((Object) 5, (Object) 5));

			ListTable table = new ListTable(attributes, data);

			List<List<String>> constraints = Arrays.asList(
					Arrays.asList("a", "b"));

			List<List<Integer>> fragments = Arrays.asList(
					Arrays.asList(0),
					Arrays.asList(1));

			List<Integer> kList = Arrays.asList(2, 2);

			LooseSolver loose = new GreedyLoose(table, constraints, fragments);

			Triplet<BaseAssociations, BaseFragments, Set<Integer>> result = loose.associate(kList);
			assertTrue(result.getThird().isEmpty());

		} catch (Exception e) {
			executedWithoutExceptions = false;
		}

		assertTrue(executedWithoutExceptions);
	}

}
