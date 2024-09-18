package utils;

import java.util.Comparator;
import java.util.List;

public class RowsComparator implements Comparator<RowData> {
    private final List<String> orderedColsToSortBy;

    public RowsComparator(List<String> orderedColsToSortBy) {this.orderedColsToSortBy = orderedColsToSortBy;}

    @Override
    public int compare(RowData o1, RowData o2) {
        return compareByColumns(o1,o2,0);
    }

    private int compareByColumns(RowData o1, RowData o2, int colIndex) {
        if (colIndex >= orderedColsToSortBy.size()) {
            return 0;
        }

        Double firstValue = o1.getEffectiveValueByColName(orderedColsToSortBy.get(colIndex));
        Double secondValue = o2.getEffectiveValueByColName(orderedColsToSortBy.get(colIndex));
        ;
        int comparisonResult = Double.compare(firstValue, secondValue);

        if (comparisonResult == 0) {
            comparisonResult = compareByColumns(o1, o2, colIndex + 1);
        }

        return comparisonResult;
    }
}
