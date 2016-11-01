package com.firebolt.trac.models;

/**
 * Created by Firebolt-Mesh on 8/27/2016.
 */
public class List {

    private String list_name;
    private int list_item_count;
    private String list_created_by;
    private String list_creation_date;
    private String list_updated_on;
    private String list_type;
    private String list_id;

    public List(String list_name, int list_item_count, String list_created_by, String list_creation_date, String list_updated_on, String list_type, String list_id) {
        this.list_name = list_name;
        this.list_item_count = list_item_count;
        this.list_created_by = list_created_by;
        this.list_creation_date = list_creation_date;
        this.list_updated_on = list_updated_on;
        this.list_type = list_type;
        this.list_id = list_id;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public String getList_type() {
        return list_type;
    }

    public void setList_type(String list_type) {
        this.list_type = list_type;
    }

    public String getList_name() {
        return list_name;
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    }

    public int getList_item_count() {
        return list_item_count;
    }

    public void setList_item_count(int list_item_count) {
        this.list_item_count = list_item_count;
    }

    public String getList_created_by() {
        return list_created_by;
    }

    public void setList_created_by(String list_created_by) {
        this.list_created_by = list_created_by;
    }

    public String getList_creation_date() {
        return list_creation_date;
    }

    public void setList_creation_date(String list_creation_date) {
        this.list_creation_date = list_creation_date;
    }

    public String getList_updated_on() {
        return list_updated_on;
    }

    public void setList_updated_on(String list_updated_on) {
        this.list_updated_on = list_updated_on;
    }
}
