package acceptance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomConfiguration {
	
	private Random random = new Random();
	List<List<Integer>> fragments;
	List<List<Integer>> constraints;
	List<Integer> kList;
	int attributes;

	public RandomConfiguration(int fragmentsNumber) {
		
		// initialize fragments each with an attribute
		List<List<Integer>> fragments = new ArrayList<>();
		for (int i = 0; i < fragmentsNumber * 2; i += 2) {
			ArrayList<Integer> fragment = new ArrayList<>();
			fragment.add(i);
			fragment.add(i + 1);
			fragments.add(fragment);
		}
		
		// put more attributes in the fragments
		attributes = random.nextInt(2 * fragmentsNumber) + fragmentsNumber * 2;
		for (int attribute = fragmentsNumber * 2; attribute < attributes; ++attribute)
			fragments.get(random.nextInt(fragmentsNumber)).add(attribute);
		
		// generate minimal constraints for that fragmentation
		Set<Set<Integer>> constraints = new HashSet<>();
		for (int fragment1 = 0; fragment1 < fragmentsNumber - 1; ++fragment1)
			for (int fragment2 = fragment1 + 1; fragment2 < fragmentsNumber; ++fragment2) {
				Set<Integer> constraint = new HashSet<Integer>();
				Collections.shuffle(fragments.get(fragment1));
				constraint.add(fragments.get(fragment1).get(0));
				if (random.nextBoolean())
					constraint.add(fragments.get(fragment1).get(1));
				Collections.shuffle(fragments.get(fragment2));
				constraint.add(fragments.get(fragment2).get(0));
				if (random.nextBoolean())
					constraint.add(fragments.get(fragment2).get(1));
				constraints.add(constraint);
			}
		
		// create other random constraints just to increase randomness and allow more-than-two-fragment-constraints
		// add more than maximum number of attributes in a fragment to avoid single fragment constraints
		int maxAttributesInFragment = 0;
		for (List<Integer> fragment: fragments)
			if (fragment.size() > maxAttributesInFragment)
				maxAttributesInFragment = fragment.size();
		
		ArrayList<Integer> attributesList = new ArrayList<>();
		for (int attribute = 0; attribute < attributes; ++attribute)
			attributesList.add(attribute);
		
		int otherConstraints = random.nextInt(fragmentsNumber);
		int min = maxAttributesInFragment + 1;
		int delta = attributes - min;
		
		for (int otherConstraint = 0; otherConstraint < otherConstraints; ++otherConstraint) {
			Collections.shuffle(attributesList);
			constraints.add(new HashSet<>(attributesList.subList(0, min + random.nextInt(delta))));
		}
		
		// generate kList
		this.kList = new ArrayList<>();
		for (int i = 0; i < fragmentsNumber; ++i)
			kList.add(2 + random.nextInt(3));
		
		this.fragments = fragments;
		this.constraints = new ArrayList<List<Integer>>();
		for (Set<Integer> constraint: constraints)
			this.constraints.add(new ArrayList<>(constraint));
		
		System.out.println("fragments:   " + this.fragments);
		System.out.println("constraints: " + this.constraints);
		System.out.println("kList:       " + this.kList);
	}
	
	public static void main(String[] args) {
		new RandomConfiguration(3);
	}

}
