package tn.esprit.models.reclamation;

import java.util.Objects;

public class categories {

    private int id;

    private String type;

    public categories(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public categories(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        categories that = (categories) o;
        return id == that.id && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return "categories{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
