package tn.esprit.services.VehiculeService;

import tn.esprit.interfaces.IService;
import tn.esprit.models.vehicules.*;
import tn.esprit.util.MyDataBase;
import tn.esprit.interfaces.IService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceVehicule implements IService<vehicule> {
    private Connection connection ;

    public ServiceVehicule(){
        connection = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(vehicule vehicule) {
        try {

            String query = "INSERT INTO vehicule (immatriculation, capacite, etat, idTrajet, typeVehicule, id_conducteur) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, vehicule.getImmatriculation());
                statement.setInt(2, vehicule.getCapacite());
                statement.setString(3, vehicule.getEtat().toString());
                statement.setInt(4, vehicule.getIdTrajet());
                statement.setString(5, vehicule.getTypeVehicule().toString());
                statement.setInt(6, vehicule.getIdConducteur());

                statement.executeUpdate();


                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    vehicule.setId(rs.getInt(1));
                }


                if (vehicule instanceof Bus) {
                    addBus((Bus) vehicule);
                } else if (vehicule instanceof Train) {
                    addTrain((Train) vehicule);
                } else if (vehicule instanceof Metro) {
                    addMetro((Metro) vehicule);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du véhicule : " + e.getMessage());
        }
    }

    private void addBus(Bus bus) throws SQLException {
        String query = "INSERT INTO bus (id, nombrePortes, typeService, nombreDePlaces, compagnie, climatisation) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bus.getId());
            statement.setInt(2, bus.getNombrePortes());
            statement.setString(3, bus.getTypeService().toString());
            statement.setInt(4, bus.getNombreDePlaces());
            statement.setString(5, bus.getCompagnie());
            statement.setBoolean(6, bus.isClimatisation());

            statement.executeUpdate();
        }
    }

    private void addTrain(Train train) throws SQLException {
        String query = "INSERT INTO train (id, longueurReseau, nombreLignes, nombreWagons, vitesseMaximale, proprietaire) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, train.getId());
            statement.setDouble(2, train.getLongueurReseau());
            statement.setInt(3, train.getNombreLignes());
            statement.setInt(4, train.getNombreWagons());
            statement.setDouble(5, train.getVitesseMaximale());
            statement.setString(6, train.getProprietaire());

            statement.executeUpdate();
        }
    }

    private void addMetro(Metro metro) throws SQLException {
        String query = "INSERT INTO metro (id, longueurReseau, nombreLignes, nombreRames, proprietaire) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, metro.getId());
            statement.setDouble(2, metro.getLongueurReseau());
            statement.setInt(3, metro.getNombreLignes());
            statement.setInt(4, metro.getNombreRames());
            statement.setString(5, metro.getProprietaire());

            statement.executeUpdate();
        }
    }


    @Override
    public List<vehicule> getAll() {
        List<vehicule> vehicules = new ArrayList<>();

        try {

            String query = "SELECT v.*, " +
                    "b.nombrePortes, b.typeService, b.nombreDePlaces, b.compagnie, b.climatisation, " +
                    "c.nom AS conducteurNom, c.prenom AS conducteurPrenom, " +
                    "t.longueurReseau AS train_longueurReseau, t.nombreLignes AS train_nombreLignes, " +
                    "t.nombreWagons, t.vitesseMaximale, t.proprietaire AS train_proprietaire, " +
                    "m.longueurReseau AS metro_longueurReseau, m.nombreLignes AS metro_nombreLignes, " +
                    "m.nombreRames, m.proprietaire AS metro_proprietaire " +
                    "FROM vehicule v " +
                    "LEFT JOIN bus b ON v.id = b.id " +
                    "LEFT JOIN train t ON v.id = t.id " +
                    "LEFT JOIN metro m ON v.id = m.id " +
                    "LEFT JOIN conducteur c ON v.id_conducteur = c.id_conducteur";

            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    vehicule vehicule = createVehiculeFromResultSet(resultSet);
                    if (vehicule != null) {
                        vehicules.add(vehicule);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des véhicules : " + e.getMessage());
        }

        return vehicules;
    }

    @Override
    public vehicule getById(int id_user) {
        return null;
    }

    private vehicule createVehiculeFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String immatriculation = resultSet.getString("immatriculation");
        int capacite = resultSet.getInt("capacite");
        String etat = resultSet.getString("etat");
        int idTrajet = resultSet.getInt("idTrajet");
        String typeVehicule = resultSet.getString("typeVehicule");
        int idConducteur = resultSet.getInt("id_conducteur");

        switch (typeVehicule) {
            case "BUS":
                String typeServiceStr = resultSet.getString("typeService");
                TypeService typeService = null;
                if (typeServiceStr != null) {
                    try {
                        typeService = TypeService.valueOf(typeServiceStr);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Valeur invalide pour typeService : " + typeServiceStr);
                        typeService = TypeService.URBAIN;  //
                    }
                }

                return new Bus(
                        id, immatriculation, capacite, Etat.valueOf(etat), idTrajet, TypeVehicule.valueOf(typeVehicule), idConducteur,
                        resultSet.getInt("nombrePortes"), typeService,
                        resultSet.getInt("nombreDePlaces"), resultSet.getString("compagnie"), resultSet.getBoolean("climatisation")
                );

            case "TRAIN":
                return new Train(
                        id, immatriculation, capacite, Etat.valueOf(etat), idTrajet, TypeVehicule.valueOf(typeVehicule), idConducteur,
                        resultSet.getDouble("train_longueurReseau"), resultSet.getInt("train_nombreLignes"),
                        resultSet.getInt("nombreWagons"), resultSet.getDouble("vitesseMaximale"),
                        resultSet.getString("train_proprietaire")
                );

            case "METRO":
                return new Metro(
                        id, immatriculation, capacite, Etat.valueOf(etat), idTrajet, TypeVehicule.valueOf(typeVehicule), idConducteur,
                        resultSet.getDouble("metro_longueurReseau"), resultSet.getInt("metro_nombreLignes"),
                        resultSet.getInt("nombreRames"), resultSet.getString("metro_proprietaire")
                );

            default:
                return null;
        }
    }




    private Bus getBusDetails(Bus bus) {
        String query = "SELECT * FROM bus WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bus.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    bus.setNombrePortes(resultSet.getInt("nombrePortes"));
                    bus.setTypeService(TypeService.valueOf(resultSet.getString("typeService")));
                    bus.setNombreDePlaces(resultSet.getInt("nombreDePlaces"));
                    bus.setCompagnie(resultSet.getString("compagnie"));
                    bus.setClimatisation(resultSet.getBoolean("climatisation"));
                    return bus;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des détails du bus : " + e.getMessage());
        }
        return null;
    }

    private Train getTrainDetails(Train train) {
        String query = "SELECT * FROM train WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, train.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    train.setLongueurReseau(resultSet.getDouble("longueurReseau"));
                    train.setNombreLignes(resultSet.getInt("nombreLignes"));
                    train.setNombreWagons(resultSet.getInt("nombreWagons"));
                    train.setVitesseMaximale(resultSet.getDouble("vitesseMaximale"));
                    train.setProprietaire(resultSet.getString("proprietaire"));
                    return train;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des détails du train : " + e.getMessage());
        }
        return null;
    }

    private Metro getMetroDetails(Metro metro) {
        String query = "SELECT * FROM metro WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, metro.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    metro.setLongueurReseau(resultSet.getDouble("longueurReseau"));
                    metro.setNombreLignes(resultSet.getInt("nombreLignes"));
                    metro.setNombreRames(resultSet.getInt("nombreRames"));
                    metro.setProprietaire(resultSet.getString("proprietaire"));
                    return metro;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des détails du métro : " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(vehicule vehicule) {
        try {

            String query = "UPDATE vehicule SET immatriculation = ?, capacite = ?, etat = ?, idTrajet = ?, typeVehicule = ?, id_conducteur = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, vehicule.getImmatriculation());
                statement.setInt(2, vehicule.getCapacite());
                statement.setString(3, vehicule.getEtat().toString());
                statement.setInt(4, vehicule.getIdTrajet());
                statement.setString(5, vehicule.getTypeVehicule().toString());
                statement.setInt(6, vehicule.getIdConducteur());
                statement.setInt(7, vehicule.getId());

                statement.executeUpdate();
            }


            if (vehicule instanceof Bus) {
                updateBus((Bus) vehicule);
            } else if (vehicule instanceof Train) {
                updateTrain((Train) vehicule);
            } else if (vehicule instanceof Metro) {
                updateMetro((Metro) vehicule);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du véhicule : " + e.getMessage());
        }
    }

    private void updateBus(Bus bus) throws SQLException {
        String query = "UPDATE bus SET nombrePortes = ?, typeService = ?, nombreDePlaces = ?, compagnie = ?, climatisation = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bus.getNombrePortes());
            statement.setString(2, bus.getTypeService().toString());
            statement.setInt(3, bus.getNombreDePlaces());
            statement.setString(4, bus.getCompagnie());
            statement.setBoolean(5, bus.isClimatisation());
            statement.setInt(6, bus.getId());

            statement.executeUpdate();

        }
    }

    private void updateTrain(Train train) throws SQLException {
        String query = "UPDATE train SET longueurReseau = ?, nombreLignes = ?, nombreWagons = ?, vitesseMaximale = ?, proprietaire = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, train.getLongueurReseau());
            statement.setInt(2, train.getNombreLignes());
            statement.setInt(3, train.getNombreWagons());
            statement.setDouble(4, train.getVitesseMaximale());
            statement.setString(5, train.getProprietaire());
            statement.setInt(6, train.getId());

            statement.executeUpdate();
        }
    }

    private void updateMetro(Metro metro) throws SQLException {
        String query = "UPDATE metro SET longueurReseau = ?, nombreLignes = ?, nombreRames = ?, proprietaire = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, metro.getLongueurReseau());
            statement.setInt(2, metro.getNombreLignes());
            statement.setInt(3, metro.getNombreRames());
            statement.setString(4, metro.getProprietaire());
            statement.setInt(5, metro.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(vehicule vehicule) {
        try {

            String query = "DELETE FROM vehicule WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, vehicule.getId());
                int rowsDeleted = statement.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Véhicule supprimé avec succès (ID: " + vehicule.getId() + ")");
                } else {
                    System.out.println("Aucun véhicule trouvé avec l'ID: " + vehicule.getId());
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du véhicule : " + e.getMessage());
        }
    }

    public void deleteByImmatriculation(String immatriculation) {
        try {

            String query = "DELETE FROM vehicule WHERE immatriculation = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, immatriculation);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Véhicule avec immatriculation " + immatriculation + " supprimé.");
                } else {
                    System.out.println("Véhicule avec immatriculation " + immatriculation + " non trouvé.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du véhicule : " + e.getMessage());
        }
    }

    public int getConducteurId(String nomConducteur, String prenomConducteur) {
        String query = "SELECT id_conducteur FROM conducteur WHERE nom = ? AND prenom = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nomConducteur);
            statement.setString(2, prenomConducteur);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_Conducteur");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID du conducteur : " + e.getMessage());
        }
        return -1;
    }

    public int getTrajetId(String lieuDepart, String lieuArret) {
        String query = "SELECT id FROM trajet WHERE depart = ? AND arret = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, lieuDepart);
            statement.setString(2, lieuArret);


            ResultSet rs = statement.executeQuery();


            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID du trajet : " + e.getMessage());
        }
        return -1;
    }


    public String getConducteurNomById(int idConducteur) {
        String nom = null;
        String query = "SELECT nom FROM conducteur WHERE id_conducteur = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idConducteur);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nom = resultSet.getString("nom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nom;
    }


    public String getConducteurPrenomById(int idConducteur) {
        String prenom = null;
        String query = "SELECT prenom FROM conducteur WHERE id_conducteur = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idConducteur);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                prenom = resultSet.getString("prenom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prenom;
    }
    public String getTrajetDepartById(int trajetId) {
        String query = "SELECT depart FROM trajet WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, trajetId);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getString("depart");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du lieu de départ : " + e.getMessage());
        }
        return null;
    }

    public String getTrajetArretById(int trajetId) {
        String query = "SELECT arret FROM trajet WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, trajetId);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getString("arret");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du lieu d'arrêt : " + e.getMessage());
        }
        return null;
    }

    public boolean isImmatriculationUnique(String immatriculation) {

        String query = "SELECT COUNT(*) FROM bus WHERE immatriculation = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, immatriculation);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getConducteurs() {
        List<String> conducteurs = new ArrayList<>();
        String query = "SELECT CONCAT(nom, ' ', prenom) AS conducteur FROM conducteur";
        try (
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                conducteurs.add(rs.getString("conducteur"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conducteurs;
    }

    public List<String> getTrajets() {
        List<String> trajets = new ArrayList<>();
        String query = "SELECT CONCAT(depart, ' - ', arret) AS trajet FROM trajet";
        try (
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                trajets.add(rs.getString("trajet"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trajets;
    }

    public List<String> getEtats() {
        List<String> etats = new ArrayList<>();
        String query = "SELECT DISTINCT etat FROM vehicule";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                etats.add(rs.getString("etat"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return etats;
    }
    public List<String> getTypesService() {
        List<String> typesService = new ArrayList<>();
        String query = "SELECT DISTINCT typeService FROM bus"; // Récupérer les types de service depuis la table bus
        try (
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                typesService.add(rs.getString("typeService"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typesService;
    }

    public List<String> getProprietaires(){
        List<String> proprietaires = new ArrayList<>();
        String query = "SELECT DISTINCT proprietaire FROM metro";
        try (
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                proprietaires.add(rs.getString("proprietaire"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proprietaires;
    }

    public List<String> getProprietairesTrain(){
        List<String> proprietaires = new ArrayList<>();
        String query = "SELECT DISTINCT proprietaire FROM train";
        try (
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                proprietaires.add(rs.getString("proprietaire"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proprietaires;
    }


}