package http.dtos;

import java.util.Set;

public class FilterResponseDTO {
    private final Set<Integer> rowsToInclude;

    public FilterResponseDTO(Set<Integer> rowsToInclude) {
        this.rowsToInclude = rowsToInclude;
    }

    public Set<Integer> getRowsToInclude() {return rowsToInclude;}
}
