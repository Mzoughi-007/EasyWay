package tn.esprit.services.event;

import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

public class TrainModel {

    public static LinearRegression trainModel(Instances dataset) throws Exception {
        LinearRegression model = new LinearRegression();
        model.buildClassifier(dataset);
        return model;
    }
}
