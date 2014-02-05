package acceptance;

import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import utils.tuples.Triplet;
import core.GreedyLoose;
import core.associations.BaseAssociations;
import core.fragments.BaseFragments;
import core.tables.BaseTable;
import core.tables.RandomTable;

public class RandomTest {
	
	private final int howMany = 10;
	private final int tuples = 500;
	private final int delta = 40;
	private final int minvalue = 8;
	
	Random random;
	RandomConfiguration configuration;
	BaseTable table;
	GreedyLoose loose;
	Triplet<BaseAssociations, BaseFragments, Set<Integer>> result;
	Checker checker;
	
	@Before
	public void setUp() throws Exception {
		Logger.getLogger(GreedyLoose.class).setLevel(Level.ERROR);
		random = new Random();
	}
	
	private void testRandom(int fragmentsNumber) throws Exception {

		for (int test = 0; test < howMany; ++test) {
			System.out.println("\n====== RANDOM TEST with " + fragmentsNumber + " fragments #" + (test + 1) + " ======");
			configuration = new RandomConfiguration(fragmentsNumber);
			table = new RandomTable(tuples, configuration.attributes, random.nextInt(delta) + minvalue);
			
			loose = new GreedyLoose(table, configuration.constraints, configuration.fragments);
			result = loose.associate(configuration.kList);
			System.out.println("\n" + loose);
			
			checker = new Checker(table, result.getSecond(), loose.getConstraints(), result.getFirst());
			assertTrue(checker.checkAll());
			
			System.out.println("================ TEST PASSED ================\n");
			
		}
	}

	@Test
	public void testRandomWith2Fragments() throws Exception {
		testRandom(2);
	}
	
	@Test
	public void testRandomWith3Fragments() throws Exception {
		testRandom(3);
	}

}
