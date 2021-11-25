package com.icommerce.shopping.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class PageableRequest implements Serializable {
    private int page;
    private int size;
    private List<SortOrder> sorts;
    private HashMap<String, Object> query;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<SortOrder> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortOrder> sorts) {
        this.sorts = sorts;
    }

    public HashMap<String, Object> getQuery() {
        return query;
    }

    public void setQuery(HashMap<String, Object> query) {
        this.query = query;
    }
}
