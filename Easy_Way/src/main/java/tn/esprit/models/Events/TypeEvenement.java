package tn.esprit.models.Events;

public enum TypeEvenement {
    RETARD, INCIDENT, GREVE ;

    public static TypeEvenement fromString(String typeEvenement) {
        for (TypeEvenement te : TypeEvenement.values()) {
            if (te.name().equalsIgnoreCase(typeEvenement)) {
                return te;
            }
        }
        throw new IllegalArgumentException("Valeur de type d'evenement invalide : " + typeEvenement);
    }
}