package com.firebolt.trac.models;

/**
 * Created by Firebolt-Mesh on 9/9/2016.
 */
public class List_Item {

    String item_name;
    String item_quantity;
    String item_measure_type;
    int item_priority;
    String item_added_timestamp;

    public List_Item(String item_name, String item_quantity, String item_measure_type, int item_priority, String item_added_timestamp) {
        this.item_name = item_name;
        this.item_quantity = item_quantity;
        this.item_measure_type = item_measure_type;
        this.item_priority = item_priority;
        this.item_added_timestamp = item_added_timestamp;
    }

    public String getItem_added_timestamp() {
        return item_added_timestamp;
    }

    public void setItem_added_timestamp(String item_added_timestamp) {
        this.item_added_timestamp = item_added_timestamp;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_measure_type() {
        return item_measure_type;
    }

    public void setItem_measure_type(String item_measure_type) {
        this.item_measure_type = item_measure_type;
    }

    public int getItem_priority() {
        return item_priority;
    }

    public void setItem_priority(int item_priority) {
        this.item_priority = item_priority;
    }
}
