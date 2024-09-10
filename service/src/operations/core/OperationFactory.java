package operations.core;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import operations.impl.logical.*;
import operations.impl.math.*;
import operations.impl.range.AVERAGERangeOperation;
import operations.impl.range.SUMRangeOperation;
import operations.impl.string.ConcatOperation;
import operations.impl.string.SUBOperation;
import operations.impl.unique.IFOperation;
import operations.impl.unique.REFOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
