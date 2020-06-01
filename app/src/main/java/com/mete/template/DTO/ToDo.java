package com.mete.template.DTO;

import java.util.ArrayList;
import java.util.List;

public class ToDo {
    private Long id = 1L;
    private String name = "";
    private String dueDate = "";
    private List items = (List)new ArrayList();

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDueDate() {
        return dueDate;
    }

    public List getItems() {
        return items;
    }
}
