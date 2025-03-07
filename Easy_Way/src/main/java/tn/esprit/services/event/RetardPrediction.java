package tn.esprit.services.event;

import tn.esprit.models.Events.Retards;
import weka.classifiers.functions.LinearRegression;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class RetardPrediction {
    public static double predictDelay(LinearRegression model, Instances dataset, Retards inputData) throws Exception {
        Instance instance = new DenseInstance(dataset.numAttributes());
        instance.setDataset(dataset);

        // assigner les valeurs de la condition de meteo et la condition de traffic
        instance.setValue(0, inputData.getMeteo());
        instance.setValue(1, inputData.getTraffic());
        instance.setValue(2, inputData.getHeurePrevue().getTime());

        return model.classifyInstance(instance);
    }
}
