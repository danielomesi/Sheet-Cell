package entities.types;

import java.io.Serializable;
import java.text.DecimalFormat;

public class NumberWrapper implements Serializable {
    private Number value;
    public NumberWrapper(Double value) {
        this.value = value;
    }
    public Double getDoubleValue() {return value.doubleValue();}
    public void setValue(double value) {this.value = value;}
    public int getIntValue() {return value.intValue();}

    @Override
    public String toString() {
        if (value.doubleValue() % 1 == 0) {
            // For whole numbers with thousands separators
            return String.format("%,d", value.longValue());
        } else {
            // For decimal numbers with thousands separators and 2 decimal places
            return String.format("%,.2f", value.doubleValue());
        }
    }
}
