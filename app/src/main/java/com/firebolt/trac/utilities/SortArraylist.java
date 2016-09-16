package com.firebolt.trac.utilities;

import com.firebolt.trac.models.List_Item;

import java.util.Comparator;

/**
 * Created by Firebolt-Mesh on 9/14/2016.
 */
public class SortArraylist implements Comparator<List_Item> {
    @Override
    public int compare(List_Item item1, List_Item item2) {
        return String.valueOf(item1.getItem_priority()).compareTo(String.valueOf(item2.getItem_priority()));
    }
}
