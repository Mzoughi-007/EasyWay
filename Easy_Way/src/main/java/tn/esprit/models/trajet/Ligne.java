package tn.esprit.models.trajet;

public class Ligne {
    private int id,admin_id;
    private String dep,arr,type;

    public Ligne() {}

    public Ligne(int id, String dep, String arr, String type, int admin_id) {
        this.id = id;
        this.dep = dep;
        this.arr = arr;
        this.type = type;
        this.admin_id = admin_id;
    }

    public Ligne(String dep, String arr, String type, int admin_id) {
        this.dep = dep;
        this.arr = arr;
        this.type = type;
        this.admin_id = admin_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getArr() {
        return arr;
    }

    public void setArr(String arr) {
        this.arr = arr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Ligne{" +
                "dep='" + dep + '\'' +
                ", arr='" + arr + '\'' +
                ", type='" + type + '\'' +
                '}'+"\n";
    }
}
