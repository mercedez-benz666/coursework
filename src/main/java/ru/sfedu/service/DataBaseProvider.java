package ru.sfedu.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.myApp.model.*;
import ru.sfedu.utils.ConfigUtils;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ru.sfedu.Constans.*;


public class DataBaseProvider implements IDataProvider {
    private Connection connection;
    LoggingBeans logToHistory = new LoggingBeans();

    ConfigUtils config = new ConfigUtils();
    Logger log = LogManager.getLogger(DataBaseProvider.class);

    public DataBaseProvider() throws Exception {
        try {
            connection = DriverManager.getConnection(config.getConfigurationEntry(PATH_TO_DB), config.getConfigurationEntry(USER_TO_DB), config.getConfigurationEntry(PASS_TO_DB));
            log.info("connect to dataBase");
        } catch (SQLException e) {
            throw new Exception(e + "\nfailed to connect");
        }
    }

    public void closeConnection() throws Exception {
        try {
            connection.close();
            log.info("connection close");
        }catch(SQLException e){
            log.info("connection already been closed");
            throw new Exception("connection already been closed");
        }
    }

    @Override
    public String createId() {
        String id = String.valueOf(UUID.randomUUID());
        return id;
    }

    @Override
    public void saveActionRecord(Action object, String userId) throws Exception {
        object.setUserId(userId);
        String sql = "INSERT INTO " + config.getConfigurationEntry(LINKTABLE_DB) + "(userId, id, actionType, numberAction) VALUES(?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, object.getUserId());
            statement.setString(2, object.getId());
            statement.setString(3, object.getType());
            statement.setInt(4, object.getNumberAction());
            if (statement.executeUpdate() == 0) {
                log.error("Impossible to save action");
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception("Error at saving data in Link Table");}
        switch (object.getType()) {
            case "payment": savePayment(object);
            case "delivery": saveDelivery(object);
            case "storage" : saveStorage(object);

        }
    }

    public void savePayment(Action obj) throws Exception {
        Payment payment = (Payment) obj;
        String sql = "INSERT INTO " + config.getConfigurationEntry(PAYMENT_DB) + "( id, country, actionType, money, numberAction) VALUES(?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, payment.getId());
            statement.setString(2, payment.getCountry());
            statement.setString(3, payment.getType());
            statement.setDouble(4, payment.getMoney());
            statement.setInt(5,payment.getNumberAction());
            log.info("save payment");
            if (statement.executeUpdate() == 0) {
                log.error("Impossible to save action");
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception("Action with this id has been saved previously");
        }
        logToHistory.logObjectChange(obj, "saveActionDB", obj.getId());
    }

    public void saveDelivery(Action obj) throws Exception {
        Delivery delivery = (Delivery) obj;
        String sql = "INSERT INTO " + config.getConfigurationEntry(DELIVERY_DB) + "(id, height, width, length, distance, country, actionType, insurance, numberAction ) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, delivery.getId());
            statement.setInt(2, delivery.getHeight());
            statement.setInt(3, delivery.getWidth());
            statement.setInt(4, delivery.getLength());
            statement.setDouble(5, delivery.getDistance());
            statement.setString(6, delivery.getCountry());
            statement.setString(7, delivery.getType());
            statement.setBoolean(8, delivery.getInsurance());
            statement.setInt(9,delivery.getNumberAction());
            log.info("save delivery");
            if (statement.executeUpdate() == 0) {
                log.error("Impossible to save delivery");
                throw new Exception();
            }
        }
        catch (Exception e) {
            throw new Exception("Action with this id has been saved previously");
        }
        logToHistory.logObjectChange(obj, "saveActionDB", obj.getId());
    }

    public void saveStorage(Action obj) throws Exception {
        Storage storage = (Storage) obj;
        String sql = "INSERT INTO " + config.getConfigurationEntry(STORAGE_DB) + "(id, height, width, length, dateCount, country, actionType, insurance,numberAction) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(5, storage.getDateCount());
            statement.setString(1, storage.getId());
            statement.setInt(2, storage.getHeight());
            statement.setInt(3, storage.getWidth());
            statement.setInt(4, storage.getLength());
            statement.setBoolean(8, storage.getInsurance());
            statement.setString(6, storage.getCountry());
            statement.setString(7, storage.getType());
            statement.setInt(9,storage.getNumberAction());
            log.info("save storage");
            if (statement.executeUpdate() == 0) {
                log.error("Impossible to save storage");
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception("Action with this id has been saved previously");
        }
        logToHistory.logObjectChange(obj, "saveActionDB", obj.getId());
    }
    @Override
    public void saveUserRecord(User object) throws Exception {
        String sql = "INSERT INTO " + config.getConfigurationEntry(USER_DB) + "(id, userType, login, password) VALUES(?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, object.getUserId());
            statement.setString(2, object.getUserType());
            statement.setString(3, object.getLogin());
            statement.setString(4, object.getPassword());
            log.info("save record");
            if (statement.executeUpdate() == 0) {
                log.error("record save error");
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception("record with this id has been saved previously");
        }
        logToHistory.logObjectChange(object, "saveUserDB", object.getUserId());
    }

    /*@Override
    public void saveFeedRecord(Feed object) throws Exception {
        String sql = "INSERT INTO " + config.getConfigurationEntry(FEED_DB) + " (feedName, id, priceForPack, weightOfPack, foractionType) VALUES(?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, object.getFeedName());
            statement.setString(2, object.getId());
            statement.setDouble(3, object.getPriceForPack());
            statement.setDouble(4, object.getWeightOfPack());
            statement.setString(5, object.getForactionType());
            log.info("save record");
            if (statement.executeUpdate() == 0) {
                log.error("record save error");
                throw new Exception();
            }
        }
        catch (Exception e) {
            throw new Exception("record with this id has been saved previously");
        }
        logToHistory.logObjectChange(object, "saveFeedDB", object.getId());
    }

    @Override
    public void saveDrugRecord(Drug object) throws Exception {
        String sql = "INSERT INTO " + config.getConfigurationEntry(DRUG_DB) + " (drugName, id, priceForPack, piecesInPack, intensityPerDay) VALUES(?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, object.getDrugName());
            statement.setString(2, object.getId());
            statement.setDouble(3, object.getPriceForPack());
            statement.setInt(4, object.getPiecesInPack());
            statement.setInt(5, object.getIntesityPerDay());
            log.info("save record");
            if (statement.executeUpdate() == 0) {
                log.error("record save error");
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception("record with this id has been saved previously");
        }
        logToHistory.logObjectChange(object, "saveDrugDB", object.getId());
    }
*/
    @Override
    public void deleteUserRecord(String id) throws Exception {
        try {
            logToHistory.logObjectChange(getUserRecordByID(id), "deleteUserDB", id);
        }catch (Exception e){
            throw new Exception("Error at saving Mongo DB record");
        }
        String deleteSQL = "DELETE FROM " + config.getConfigurationEntry(USER_DB) + " WHERE id = \"" + id+"\"";
        try (Statement statement = connection.createStatement()){
            if(statement.executeUpdate(deleteSQL) == 0){
                throw new Exception();
            }
        }catch (Exception e){
            throw new Exception("User with this id not found");
        }
    }


    @Override
    public void deleteActionRecord(Action action) throws Exception {
        switch (action.getType()) {
            case "payment":
                deleteAction(PAYMENT_DB, action.getId());
                break;
            case "delivery":
                deleteAction(DELIVERY_DB, action.getId());
                break;
            case "storage":
                deleteAction(STORAGE_DB, action.getId());
                break;
            default:
                throw new Exception("Incorrect action type");
        }
    }

    @Override
    public void deleteOneActionRecord(String id) throws Exception {
        String selectSQL = "Select * FROM " + config.getConfigurationEntry(LINKTABLE_DB) + " WHERE id = \"" + id+"\"";
        Map<String, String> type = new HashMap<String, String>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(selectSQL);
            if (rs.next()) {
                type.put(rs.getString("userId"), rs.getString("actionType"));
            }
        }
        if (type.isEmpty()) {
            log.error("Records not found");
            throw new Exception("No records with this id");
        }
        for (Map.Entry<String, String> entry : type.entrySet()) {
            deleteActionRecord(findForGetActionRecord(entry.getKey(), entry.getValue()));
        }
    }

    public void findForDelActionByUser(String id) throws Exception {
        String selectSQL = "Select * FROM " + config.getConfigurationEntry(LINKTABLE_DB) + " WHERE userId = \"" + id+"\"";
        Map<String, String> type = new HashMap<String, String>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(selectSQL);
            if (rs.next()) {
                type.put(rs.getString("id"), rs.getString("actionType"));
            }
        }
        if (type.isEmpty()) {
            log.error("Records not found");
            throw new Exception("No records with this id");
        }
        for (Map.Entry<String, String> entry : type.entrySet()) {
            deleteActionRecord(findForGetActionRecord(entry.getKey(), entry.getValue()));
        }
    }


    public void deleteAction(String path, String id) throws Exception{
        logToHistory.logObjectChange(getActionRecordByID(id), "deleteActionDB", id);
        String deleteSQL = "DELETE FROM " + config.getConfigurationEntry(path) + " WHERE id = \"" + id+"\"";
        try (Statement statement = connection.createStatement()){
            if(statement.executeUpdate(deleteSQL) == 0){
                throw new Exception();
            }
        } catch (Exception e){
            throw new Exception("record with this id not found");
        }
    }
    @Override
    public User getUserRecordByID(String id) throws Exception {
        String selectSQL = "Select * FROM " + config.getConfigurationEntry(USER_DB) + " WHERE id = \"" + id+"\"";
        User user = new User();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(selectSQL);
            if (rs.next()) {
                user.setUserId(rs.getString("id"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setUserType(rs.getString("userType"));
            }
        }
        if (user.getUserId() == null) {
            log.error("Not found гыук with id=" + id);
            throw new Exception("there is no record with this id");
        }
        return user;
    }

    public Action getPayment(String id) throws Exception {
        String selectSQL = "Select * FROM " + config.getConfigurationEntry(PAYMENT_DB) + " WHERE id = \"" + id+"\"";
        Payment payment = new Payment();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(selectSQL);
            if (rs.next()) {
                payment.setMoney(rs.getDouble("money"));
                payment.setId(rs.getString("id"));
                payment.setCountry(rs.getString("country"));
                payment.setType(rs.getString("actionType"));

            }
        }
        if (payment.getId() == null) {
            log.error("Not found payment with id=" + id);
            throw new Exception("there is no record with this id");
        }
        return payment;
    }

    public Action getDelivery(String id) throws Exception {
        String selectSQL = "Select * FROM " + config.getConfigurationEntry(DELIVERY_DB) + " WHERE id = \"" + id+"\"";
        Delivery delivery = new Delivery();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(selectSQL);
            if (rs.next()) {
                delivery.setCountry(rs.getString("country"));
                delivery.setId(rs.getString("id"));
                delivery.setHeight(rs.getInt("height"));
                delivery.setWidth(rs.getInt("width"));
                delivery.setLength(rs.getInt("length"));
                delivery.setType(rs.getString("type"));
                delivery.setDistance(rs.getInt("distance"));
                delivery.setInsurance(rs.getBoolean("insurance"));
            }
        }
        if (delivery.getId() == null) {
            log.error("Not found delivery with id=" + id);
            throw new Exception("there is no record with this id");
        }
        return delivery;
    }

    public Action getStorage(String id) throws Exception {
        String selectSQL = "Select * FROM " + config.getConfigurationEntry(STORAGE_DB) + " WHERE id = \"" + id+"\"";
        Storage storage = new Storage();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(selectSQL);
            if (rs.next()) {
                storage.setCountry(rs.getString("country"));
                storage.setId(rs.getString("id"));
                storage.setHeight(rs.getInt("height"));
                storage.setWidth(rs.getInt("width"));
                storage.setLength(rs.getInt("length"));
                storage.setType(rs.getString("actionType"));
                storage.setDateCount(rs.getInt("dateCount"));
                storage.setInsurance(rs.getBoolean("insurance"));
            }
        }
        if (storage.getId() == null) {
            log.error("Not found storage with id=" + id);
            throw new Exception("there is no record with this id");
        }
        return storage;
    }



    public Action findForGetActionRecord(String id, String actionType) throws Exception {
        switch (actionType) {
            case "payment":
                return getPayment(id);
            case "delivery":
                return getDelivery(id);
            case "storage":
                return getStorage(id);
            default:
                throw new Exception("Incorrect action type");
        }
    }

    @Override
    public Action getActionRecordByUserID(String id) throws Exception {
        String selectSQL = "Select * FROM " + config.getConfigurationEntry(LINKTABLE_DB) + " WHERE userId = \"" + id+"\"";
        Map<String, String> type = new HashMap<String, String>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(selectSQL);
            if (rs.next()) {
                type.put(rs.getString("id"), rs.getString("actionType"));
            }
        }
        if (type.isEmpty()) {
            log.error("Records not found");
            throw new Exception("No records with this id");
        }
        for (Map.Entry<String, String> entry : type.entrySet()) {
            return findForGetActionRecord(entry.getKey(), entry.getValue());
        }
        throw new Exception("No record in link table");
    }

    @Override
    public Action getActionRecordByID(String id) throws Exception {
        String selectSQL = "Select * FROM " + config.getConfigurationEntry(LINKTABLE_DB) + " WHERE id = \"" + id+"\"";
        Map<String, String> type = new HashMap<String, String>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(selectSQL);
            if (rs.next()) {
                type.put(rs.getString("id"), rs.getString("actionType"));
            }
        }
        if (type.isEmpty()) {
            log.error("Records not found");
            throw new Exception("No records with this id");
        }
        for (Map.Entry<String, String> entry : type.entrySet()) {
            return findForGetActionRecord(entry.getKey(), entry.getValue());
        }
        throw new Exception("No record in link table");
    }






/*
    @Override
    public Feed getFeedRecordByID(String id) throws Exception {
        String selectSQL = "Select * FROM " + config.getConfigurationEntry(FEED_DB) + " WHERE id = \"" + id+"\"";
        Feed feed = new Feed();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(selectSQL);
            if (rs.next()) {
                feed.setFeedName(rs.getString("feedName"));
                feed.setId(rs.getString("id"));
                feed.setForactionType(rs.getString("foractionType"));
                feed.setPriceForPack(rs.getDouble("priceForPack"));
                feed.setWeightOfPack(rs.getDouble("weightOfPack"));
            }
        }
        if (feed.getId() == null) {
            log.error("Not found feed with id=" + id);
            throw new Exception("there is no feed with this id");
        }
        return feed;
    }

    @Override
    public Drug getDrugRecordByID(String id) throws Exception {
        String selectSQL = "Select * FROM " + config.getConfigurationEntry(DRUG_DB) + " WHERE id = \"" + id+"\"";
        Drug drug = new Drug();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(selectSQL);
            if (rs.next()) {
                drug.setDrugName(rs.getString("drugName"));
                drug.setId(rs.getString("id"));
                drug.setPriceForPack(rs.getDouble("priceForPack"));
                drug.setPiecesInPack(rs.getInt("piecesInPack"));
                drug.setIntesityPerDay(rs.getInt("intensityPerDay"));
            }
        }
        if (drug.getId() == null) {
            log.error("Not found drug with id=" + id);
            throw new Exception("there is no drug with this id");
        }
        return drug;
    }
*/

}
