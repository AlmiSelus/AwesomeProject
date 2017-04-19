package com.awesomegroup.ingredients;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michal on 2017-04-18.
 */
public enum IngredientMeasurement {
    ALL(0, "ALL"), TBLSPOON(1, "TBL SPOON"), SPOON(2, "SPOON"),
    PINCH(3, "PINCH"), ML(4, "ML"), MG(5, "MG"),
    G(6, "G"), L(7, "L");

    private long measurementID;
    private String measurementName;

    IngredientMeasurement(long measurementID, String measurementName) {
        this.measurementID = measurementID;
        this.measurementName = measurementName;
    }

    public long getMeasurementID() {
        return measurementID;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public static List<IngredientMeasurement> except(IngredientMeasurement... measurements) {
        List<IngredientMeasurement> toFilter = Arrays.asList(measurements);
        return Arrays.stream(values()).filter(measurement -> !toFilter.contains(measurement)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "IngredientMeasurement{" +
                "measurementID=" + measurementID +
                ", measurementName='" + measurementName + '\'' +
                '}';
    }
}
