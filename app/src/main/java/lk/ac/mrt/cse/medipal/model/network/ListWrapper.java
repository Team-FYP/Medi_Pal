package lk.ac.mrt.cse.medipal.model.network;

import java.util.List;

/**
 * Created by lakshan on 11/6/17.
 */

public class ListWrapper <T> {
    private List<T> items;

    public ListWrapper() {
    }

    public ListWrapper(List<T> items) {
        this.items = items;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
