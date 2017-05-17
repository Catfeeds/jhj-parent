package com.meijia.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ListUtil {

	/**
	 * Create a new list which contains the specified number of elements from
	 * the source list, in a
	 * random order but without repetitions.
	 *
	 * @param sourceList
	 *            the list from which to extract the elements.
	 * @param itemsToSelect
	 *            the number of items to select
	 * @param random
	 *            the random number generator to use
	 * @return a new list containg the randomly selected elements
	 */
	public static <T> List<T> chooseRandomly(List<T> sourceList, int n) {
		Random random = new Random();
        int sourceSize = sourceList.size();
        
        if (sourceSize < n) n = sourceSize;
        // Generate an array representing the element to select from 0... number of available
        // elements after previous elements have been selected.
        int[] selections = new int[n];
 
        // Simultaneously use the select indices table to generate the new result array
        ArrayList<T> resultArray = new ArrayList<T>();
 
        for (int count = 0; count < n; count++) {
 
            // An element from the elements *not yet chosen* is selected
            int selection = random.nextInt(sourceSize - count);
            selections[count] = selection;
            // Store original selection in the original range 0.. number of available elements
 
            // This selection is converted into actual array space by iterating through the elements
            // already chosen.
            for (int scanIdx = count - 1; scanIdx >= 0; scanIdx--) {
                if (selection >= selections[scanIdx]) {
                    selection++;
                }
            }
            // When the first selected element record is reached all selections are in the range
            // 0.. number of available elements, and free of collisions with previous entries.
 
            // Write the actual array entry to the results
            resultArray.add(sourceList.get(selection));
        }
        return resultArray;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		for(int i = 0; i < 1; i++) {
			Map<String, String> item = new HashMap<String, String>();
			item.put("aaa", String.valueOf(i));
			item.put("aaa", String.valueOf(i));
			list.add(item);
		}
		
		List<Map<String, String>> randList = ListUtil.chooseRandomly(list, 2);
		for (Map item :randList) {
			System.out.println(item.get("aaa"));
		}
	}

}
