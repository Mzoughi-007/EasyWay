package tn.esprit.models.trajet;

public class Paiement {
    private int id,user_id,res_id;
    private String pay_id;
    private double montant;

    public Paiement() {}

    public Paiement(int id, String pay_id, double montant, int res_id, int user_id) {
        this.id = id;
        this.pay_id = pay_id;
        this.montant = montant;
        this.res_id = res_id;
        this.user_id = user_id;
    }

    public Paiement(String pay_id, double montant, int res_id, int user_id) {
        this.pay_id = pay_id;
        this.montant = montant;
        this.res_id = res_id;
        this.user_id = user_id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    public int getRes_id() {
        return res_id;
    }

    public void setRes_id(int res_id) {
        this.res_id = res_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "pay_id='" + pay_id + '\'' +
                ", montant=" + montant +
                '}';
    }
}
