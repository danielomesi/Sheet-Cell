package http.dtos;

import java.util.List;

public class SortResponseDTO {
    private final List<Integer> sortedRowsOrder;

    public SortResponseDTO(List<Integer> sortedRowsOrder) {
        this.sortedRowsOrder = sortedRowsOrder;
    }

    public List<Integer> getSortedRowsOrder() {return sortedRowsOrder;}
}
