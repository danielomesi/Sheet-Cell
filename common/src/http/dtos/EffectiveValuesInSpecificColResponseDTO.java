package http.dtos;

import java.util.List;

public class EffectiveValuesInSpecificColResponseDTO {
    private final List<Object> effectiveValuesOfSelectedCol;
    public EffectiveValuesInSpecificColResponseDTO(List<Object> effectiveValuesOfSelectedCol) {
        this.effectiveValuesOfSelectedCol = effectiveValuesOfSelectedCol;
    }
    public List<Object> getEffectiveValuesOfSelectedCol() {return effectiveValuesOfSelectedCol;}
}
