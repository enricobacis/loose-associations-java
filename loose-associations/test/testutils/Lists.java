package testutils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class Lists {

	public static <T> List<T> from(Iterator<T> iterator) {
		LinkedList<T> list = new LinkedList<>();
		while (iterator.hasNext())
			list.add(iterator.next());
		return list;
	}
	
	public static <T> List<T> from(Iterable<T> iterable) {
		return Lists.from(iterable.iterator());
	}

}
