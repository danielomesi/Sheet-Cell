package entities.types;

import java.io.Serializable;
import java.text.DecimalFormat;

public class NumberWrapper implements Serializable {
    private Number value;
    public NumberWrapper(double value) {
        this.value = value;
    }
    public double getDoubleValue() {return value.doubleValue();}
    public void setValue(double value) {this.value = value;}
    public int getIntValue() {return value.intValue();}

    @Override
    public String toString() {
        DecimalFormat wholeNumberFormatter = new DecimalFormat("#,###");
        DecimalFormat decimalFormatter = new DecimalFormat("#,###.00");
        if ((value.doubleValue()) % 1 == 0) {
            return wholeNumberFormatter.format(value);
        } else {
            return decimalFormatter.format(value);
        }
    }
}
