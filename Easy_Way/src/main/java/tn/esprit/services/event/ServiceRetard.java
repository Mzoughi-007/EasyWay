package tn.esprit.services.event;

import tn.esprit.models.Events.Retards;
import tn.esprit.util.MyDataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceRetard {
    private Connection cnx;

    public ServiceRetard() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    public static List<Retards> getTrainingData(){
        List<Retards> data = new ArrayList<Retards>();
        try{
            Connection conn = MyDataBase.getInstance().getCnx();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM retards");
            while(rs.next()){
                data.add(new Retards(rs.getInt("ligneAffectee "),
                        rs.getTimestamp("heurePrevue"),
                        rs.getTimestamp("heureActuelle"),
                        rs.getString("meteo"),
                        rs.getString("traffic"),
                        rs.getInt("retardMinutes")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  data;
    }


    public Connection getCnx() {
        return cnx;
    }

    public void setCnx(Connection cnx) {
        this.cnx = cnx;
    }
}
