package core.fragments;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class BaseFragmentsTest {
	
	@Test
	public void testSetKListShouldThrowWhenKListIsDifferentSizeThanFragments() throws Exception {
		BaseFragments basefragments = mock(BaseFragments.class);
		doCallRealMethod().when(basefragments).setKList(anyListOf(Integer.class));
		when(basefragments.size()).thenReturn(3);
		
		Fragment fragment = mock(Fragment.class);
		when(basefragments.get(anyInt())).thenReturn(fragment);
		
		List<Integer> kList = Arrays.asList(2, 3, 4);
		basefragments.setKList(kList);
		
		ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
		verify(basefragments, times(3)).get(idCaptor.capture());
		assertEquals(Arrays.asList(0, 1, 2), idCaptor.getAllValues());
		
		ArgumentCaptor<Integer> kCaptor = ArgumentCaptor.forClass(Integer.class);
		verify(fragment, times(3)).setK(kCaptor.capture());
		assertEquals(kList, kCaptor.getAllValues());
		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetKListShouldCallSetKOnEveryFragment() throws Exception {
		BaseFragments basefragments = mock(BaseFragments.class);
		doCallRealMethod().when(basefragments).setKList(anyListOf(Integer.class));
		when(basefragments.size()).thenReturn(10);
		basefragments.setKList(Arrays.asList(1, 2));
	}
	
	@Test
	public void coverage() throws Exception {
		new BaseFragments() {
			public Iterator<Fragment> iterator() { return null; }
			public Fragment get(int fragmentId) { return null; }
			public int size() { return 0; }
		};
	}

}
