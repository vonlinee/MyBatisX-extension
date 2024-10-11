//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.baomidou.mybatisx.plugin.component;

import java.awt.Component;
import java.awt.Container;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public final class ConstantSize implements Size, Serializable {
    public static final Unit PIXEL = new Unit("Pixel", "px", true);
    public static final Unit POINT = new Unit("Point", "pt", true);
    public static final Unit DIALOG_UNITS_X = new Unit("Dialog units X", "dluX", true);
    public static final Unit DLUX;
    public static final Unit DIALOG_UNITS_Y;
    public static final Unit DLUY;
    public static final Unit MILLIMETER;
    public static final Unit MM;
    public static final Unit CENTIMETER;
    public static final Unit CM;
    public static final Unit INCH;
    public static final Unit IN;
    private static final Unit[] VALUES;
    private final double value;
    private final Unit unit;

    public ConstantSize(int value, Unit unit) {
        this.value = (double)value;
        this.unit = unit;
    }

    public ConstantSize(double value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    static ConstantSize valueOf(String encodedValueAndUnit, boolean horizontal) {
        String[] split = splitValueAndUnit(encodedValueAndUnit);
        String encodedValue = split[0];
        String encodedUnit = split[1];
        Unit unit = ConstantSize.Unit.valueOf(encodedUnit, horizontal);
        double value = Double.parseDouble(encodedValue);
        if (unit.requiresIntegers && value != (double)((int)value)) {
            throw new IllegalArgumentException(unit.toString() + " value " + encodedValue + " must be an integer.");
        } else {
            return new ConstantSize(value, unit);
        }
    }

    static ConstantSize dluX(int value) {
        return new ConstantSize(value, DLUX);
    }

    static ConstantSize dluY(int value) {
        return new ConstantSize(value, DLUY);
    }

    public double getValue() {
        return this.value;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public int getPixelSize(Component component) {
        if (this.unit == PIXEL) {
            return this.intValue();
        } else if (this.unit == POINT) {
            return Sizes.pointAsPixel(this.intValue(), component);
        } else if (this.unit == INCH) {
            return Sizes.inchAsPixel(this.value, component);
        } else if (this.unit == MILLIMETER) {
            return Sizes.millimeterAsPixel(this.value, component);
        } else if (this.unit == CENTIMETER) {
            return Sizes.centimeterAsPixel(this.value, component);
        } else if (this.unit == DIALOG_UNITS_X) {
            return Sizes.dialogUnitXAsPixel(this.intValue(), component);
        } else if (this.unit == DIALOG_UNITS_Y) {
            return Sizes.dialogUnitYAsPixel(this.intValue(), component);
        } else {
            throw new IllegalStateException("Invalid unit " + this.unit);
        }
    }

    public int maximumSize(Container container, List components, FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure, FormLayout.Measure defaultMeasure) {
        return this.getPixelSize(container);
    }

    public boolean compressible() {
        return false;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof ConstantSize)) {
            return false;
        } else {
            ConstantSize size = (ConstantSize)o;
            return this.value == size.value && this.unit == size.unit;
        }
    }

    public int hashCode() {
        return (new Double(this.value)).hashCode() + 37 * this.unit.hashCode();
    }

    public String toString() {
        return this.value == (double)this.intValue() ? Integer.toString(this.intValue()) + this.unit.abbreviation() : Double.toString(this.value) + this.unit.abbreviation();
    }

    private int intValue() {
        return (int)Math.round(this.value);
    }

    static String[] splitValueAndUnit(String encodedValueAndUnit) {
        String[] result = new String[2];
        int len = encodedValueAndUnit.length();

        int firstLetterIndex;
        for(firstLetterIndex = len; firstLetterIndex > 0 && Character.isLetter(encodedValueAndUnit.charAt(firstLetterIndex - 1)); --firstLetterIndex) {
        }

        result[0] = encodedValueAndUnit.substring(0, firstLetterIndex);
        result[1] = encodedValueAndUnit.substring(firstLetterIndex);
        return result;
    }

    static {
        DLUX = DIALOG_UNITS_X;
        DIALOG_UNITS_Y = new Unit("Dialog units Y", "dluY", true);
        DLUY = DIALOG_UNITS_Y;
        MILLIMETER = new Unit("Millimeter", "mm", false);
        MM = MILLIMETER;
        CENTIMETER = new Unit("Centimeter", "cm", false);
        CM = CENTIMETER;
        INCH = new Unit("Inch", "in", false);
        IN = INCH;
        VALUES = new Unit[]{PIXEL, POINT, DIALOG_UNITS_X, DIALOG_UNITS_Y, MILLIMETER, CENTIMETER, INCH};
    }

    public static final class Unit implements Serializable {
        private final transient String name;
        private final transient String abbreviation;
        final transient boolean requiresIntegers;
        private static int nextOrdinal = 0;
        private final int ordinal;

        private Unit(String name, String abbreviation, boolean requiresIntegers) {
            this.ordinal = nextOrdinal++;
            this.name = name;
            this.abbreviation = abbreviation;
            this.requiresIntegers = requiresIntegers;
        }

        static Unit valueOf(String str, boolean horizontal) {
            String lowerCase = str.toLowerCase(Locale.ENGLISH);
            if (!lowerCase.equals("px") && lowerCase.length() != 0) {
                if (lowerCase.equals("dlu")) {
                    return horizontal ? ConstantSize.DIALOG_UNITS_X : ConstantSize.DIALOG_UNITS_Y;
                } else if (lowerCase.equals("pt")) {
                    return ConstantSize.POINT;
                } else if (lowerCase.equals("in")) {
                    return ConstantSize.INCH;
                } else if (lowerCase.equals("mm")) {
                    return ConstantSize.MILLIMETER;
                } else if (lowerCase.equals("cm")) {
                    return ConstantSize.CENTIMETER;
                } else {
                    throw new IllegalArgumentException("Invalid unit name '" + str + "'. Must be one of: " + "px, dlu, pt, mm, cm, in");
                }
            } else {
                return ConstantSize.PIXEL;
            }
        }

        public String toString() {
            return this.name;
        }

        public String abbreviation() {
            return this.abbreviation;
        }

        private Object readResolve() {
            return ConstantSize.VALUES[this.ordinal];
        }
    }
}
