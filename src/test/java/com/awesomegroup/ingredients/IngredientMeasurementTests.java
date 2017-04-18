package com.awesomegroup.ingredients;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by c309044 on 2017-04-18.
 */
public class IngredientMeasurementTests {

    @Test
    public void createMeasurementsWithExcept_shouldGive4_G_MG_ML_L() {
        List<IngredientMeasurement> actual = IngredientMeasurement.except(IngredientMeasurement.ALL, IngredientMeasurement.PINCH, IngredientMeasurement.SPOON, IngredientMeasurement.TBLSPOON);
        List<IngredientMeasurement> expected = Arrays.asList(IngredientMeasurement.G, IngredientMeasurement.MG, IngredientMeasurement.ML, IngredientMeasurement.L);

        Assert.assertNotNull(actual);
        Assert.assertTrue(actual.size() == 4);
        Assert.assertTrue(actual.contains(expected.get(0)));
        Assert.assertTrue(actual.contains(expected.get(1)));
        Assert.assertTrue(actual.contains(expected.get(2)));
        Assert.assertTrue(actual.contains(expected.get(3)));
    }

}
