package tn.esprit.services.event;

import tn.esprit.models.Events.Retards;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public class WekaDataConverter {

    // converting to instance ARFF in order to train the model
    public static Instances convertToInstances(List<Retards> dataList) {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("meteo", (ArrayList<String>) null));
        attributes.add(new Attribute("traffic", (ArrayList<String>) null));
        attributes.add(new Attribute("heurePrevue"));
        attributes.add(new Attribute("heureActuelle"));

        Instances dataset = new Instances("Retards", attributes, dataList.size());
        dataset.setClassIndex(dataset.numAttributes() - 1);

        for (Retards data : dataList) {
            DenseInstance instance = new DenseInstance(attributes.size());
            instance.setValue(attributes.get(0), data.getMeteo());
            instance.setValue(attributes.get(1), data.getTraffic());
            instance.setValue(attributes.get(2), data.getHeurePrevue().getTime());
            instance.setValue(attributes.get(3), data.getHeureActuelle().getTime());
            dataset.add(instance);
        }
        return dataset;
    }
}
