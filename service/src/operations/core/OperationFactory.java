package operations.core;

import entities.cell.CoreCell;
import entities.coordinates.CoordinateFactory;
import entities.coordinates.Coordinates;
import entities.range.Range;
import entities.sheet.CoreSheet;
import service_exceptions.InvalidRangeException;
import operations.impl.logical.*;
import operations.impl.math.*;
import operations.impl.range.AVERAGERangeOperation;
import operations.impl.range.SUMRangeOperation;
import operations.impl.string.ConcatOperation;
import operations.impl.string.SUBOperation;
import operations.impl.unique.IFOperation;
import operations.impl.unique.REFOperation;

import java.util.*;

public class OperationFactory {
    protected static final Map<String, OperationInfo> funcName2OperationInfo = new HashMap<>();


    static {
        funcName2OperationInfo.put("PLUS", new OperationInfo(PLUSOperation.class, 2));
        funcName2OperationInfo.put("MINUS", new OperationInfo(MINUSOperation.class, 2));
        funcName2OperationInfo.put("TIMES", new OperationInfo(TIMESOperation.class, 2));
        funcName2OperationInfo.put("DIVIDE", new OperationInfo(DIVIDEOperation.class, 2));
        funcName2OperationInfo.put("MOD", new OperationInfo(MODOperation.class, 2));
        funcName2OperationInfo.put("POW", new OperationInfo(POWOperation.class, 2));
        funcName2OperationInfo.put("ABS", new OperationInfo(ABSOperation.class, 1));
        funcName2OperationInfo.put("CONCAT", new OperationInfo(ConcatOperation.class, 2));
        funcName2OperationInfo.put("SUB", new OperationInfo(SUBOperation.class, 3));
        funcName2OperationInfo.put("REF", new OperationInfo(REFOperation.class, 1));
        //
        funcName2OperationInfo.put("AND", new OperationInfo(ANDOperation.class, 2));
        funcName2OperationInfo.put("OR", new OperationInfo(OROperation.class, 2));
        funcName2OperationInfo.put("EQUAL", new OperationInfo(EQUALOperation.class, 2));
        funcName2OperationInfo.put("NOT", new OperationInfo(NOTOperation.class, 1));
        funcName2OperationInfo.put("BIGGER", new OperationInfo(BIGGEROperation.class, 2));
        funcName2OperationInfo.put("LESS", new OperationInfo(LESSOperation.class, 2));
        funcName2OperationInfo.put("PERCENT", new OperationInfo(PERCENTOperation.class, 2));
        funcName2OperationInfo.put("IF", new OperationInfo(IFOperation.class, 3));
        funcName2OperationInfo.put("AVERAGE", new OperationInfo(AVERAGERangeOperation.class, 1));
        funcName2OperationInfo.put("SUM", new OperationInfo(SUMRangeOperation.class, 1));

    }

    public static Operation createSpecificOperationUsingReflection(CoreSheet sheet, Coordinates coordinates, OperationInfo operationInfo, List<Object> arguments) {
        Class<? extends Operation> operationSpecificClazz = operationInfo.getOperationClass();
        if (operationSpecificClazz != null) {
            try {
                return operationSpecificClazz.getDeclaredConstructor(CoreSheet.class, Coordinates.class,List.class).newInstance(sheet,coordinates,arguments);
            } catch (Exception e) {
                throw new RuntimeException("Error creating instance of " + operationSpecificClazz.getSimpleName(), e);
            }
        } else {
            throw new IllegalArgumentException("No operation found");
        }
    }

    public static Map<String, OperationInfo> getOperationsMap() {return funcName2OperationInfo; }

    public static Object getArgValue(Object arg) {
        Object result;

        if (arg instanceof Operation operation) {
            result = operation.execute();

        }
        else {
            result = arg;
        }
        return result;
    }


    public static List<Object> convertToNonOperationObjects(List<Object> arguments)
    {
        List<Object> result = new ArrayList<>();
        for (Object argument : arguments) {
            result.add(getArgValue(argument));
        }
        return result;
    }

    public static boolean areActualArgumentsMatchingToExpectedArguments(Class<?>[] clazzes, List<Object> list) {
        boolean result = true;
        for (int i = 0; i < clazzes.length; i++) {
            Class<?> clazz = clazzes[i];
            Object obj = list.get(i);
            if (!clazz.isInstance(obj)) {
                result = false;
                break;
            }
        }

        return result;
    }

    public static void updateDependenciesOfRange(Range range, CoreSheet sheet, Coordinates coordinates) {
        CoreCell affectedCell = CoordinateFactory.getCellObjectFromCellID(sheet,coordinates.getCellID());
        for (Coordinates coordinatesOfCellInRange : range.getCells()) {
            CoreCell affectingCell;
            if (sheet.getCoreCellsMap().containsKey(coordinatesOfCellInRange)) {
                affectingCell = sheet.getCoreCellsMap().get(coordinatesOfCellInRange);
            }
            else {
                affectingCell = new CoreCell(sheet,coordinatesOfCellInRange.getRow(),coordinatesOfCellInRange.getCol());
                sheet.getCoreCellsMap().put(coordinatesOfCellInRange,affectingCell);
            }
            affectedCell.getCellsAffectingMe().add(affectingCell.getCoordinates());
            affectingCell.getCellsAffectedByMe().add(affectedCell.getCoordinates());
        }
    }

    public static List<Number> getRangesAsListOfNumbers(Range range, CoreSheet sheet) {
        List<Number> result = new ArrayList<>();
        Set<Coordinates> cellsCoordinates = range.getCells();
        for (Coordinates coordinates : cellsCoordinates) {
            CoreCell coreCell = CoordinateFactory.getCellObjectFromCellID(sheet,coordinates.getCellID());
            Object obj = getArgValue(coreCell.getEffectiveValue());
            if (obj instanceof Number) {
                result.add((Number)obj);
            }
        }
        return result;
    }

    public static Range getRangeOrThrow(CoreSheet sheet, String rangeName) {
        Range range = sheet.getRange(rangeName);
        if (range == null) {
            throw new InvalidRangeException("A range with the name " + rangeName + " was not found");
        }
        return range;
    }
}
