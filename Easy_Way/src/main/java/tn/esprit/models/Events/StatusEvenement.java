package tn.esprit.models.Events;

public enum StatusEvenement {
    EN_COUR, RESOLU, ANNULE;

    public static StatusEvenement fromString(String statusEvenement) {
        for (StatusEvenement se : StatusEvenement.values()) {
            if (se.name().equalsIgnoreCase(statusEvenement)) {
                return se;
            }
        }
        throw new IllegalArgumentException("Valeur de status d'evenement invalide : " + statusEvenement);
    }
}