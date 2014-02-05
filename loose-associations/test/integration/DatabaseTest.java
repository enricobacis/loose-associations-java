package integration;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import utils.tuples.Triplet;
import acceptance.Checker;
import core.GreedyLoose;
import core.associations.BaseAssociations;
import core.fragments.BaseFragments;
import core.tables.JDBCTable;
import core.tables.SqliteTable;
import exporter.H2Exporter;


public class DatabaseTest {

	@Test
	public void testWithRealDatabase() throws Exception {

		boolean executedWithoutExceptions =  true;

		try {

			List<List<String>> fragments = Arrays.asList(
					Arrays.asList("region", "statefip", "age", "sex", "marst", "ind", "incwage", "inctot"),
				    Arrays.asList("educ", "occ", "hrswork", "health"));

			List<List<String>> constraints = Arrays.asList(
					Arrays.asList("statefip", "ind", "educ", "occ", "health"),
				    Arrays.asList("age", "sex", "marst", "educ", "occ", "health"));

			List<Integer> kList = Arrays.asList(2, 2);

			JDBCTable table = new SqliteTable(new File("test/resources/ipums100.db").getAbsolutePath(), "ipums", "educ");
			GreedyLoose loose = new GreedyLoose(table, constraints, fragments);

			Triplet<BaseAssociations, BaseFragments, Set<Integer>> result = loose.associate(kList);
			System.out.println(loose);

			H2Exporter exporter = new H2Exporter(table, result.getSecond(), result.getFirst());
			// database in memory
			exporter.export("mem:");

			Checker checker = new Checker(table, result.getSecond(), loose.getConstraints(), result.getFirst());
			assertTrue(checker.checkAll());

		} catch (Exception e) {
			executedWithoutExceptions = false;
		}

		assertTrue(executedWithoutExceptions);


	}

}
