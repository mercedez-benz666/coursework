
package ru.sfedu.service;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.sfedu.myApp.model.*;
import ru.sfedu.utils.ConfigUtils;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.sfedu.Constans.*;



public class DataProviderCSV implements IDataProvider{

    private static Logger log = LogManager.getLogger(DataProviderCSV.class);
    static ConfigUtils config = new ConfigUtils();

    //private String[] transportHeader = {"name", "id", "price", "weight"};
    //private String[] stockHeader = {"name", "id", "price", "pieces", "intensity"};


    public DataProviderCSV() {
        log.debug("create dataProviderCSV...");
    }

    public <T> List<T> getAllRecords(String[] columns, Class<T> tClass, String path) {
        try {
            log.debug("deserialize object...");
            FileReader reader = new FileReader(path);
            ColumnPositionMappingStrategy<T> strat = new ColumnPositionMappingStrategyBuilder<T>().build();
            strat.setType(tClass);
            strat.setColumnMapping(columns);
            CsvToBean csv = new CsvToBeanBuilder(reader).withMappingStrategy(strat).build();
            List list = csv.parse();
            log.debug("records initialization:" + list);
            return list;
        } catch (IOException e) {
            log.info("file is empty");
        }
        return new ArrayList<>();
    }


    public <T> void initRecord(List list, String[] headers, Class<T> tClass, String path) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        log.info("update info");
        new FileOutputStream(path).close();
        FileWriter writer = new FileWriter(path, false);
        ColumnPositionMappingStrategy<T> strategy = new ColumnPositionMappingStrategyBuilder<T>().build();
        strategy.setType(tClass);
        strategy.setColumnMapping(headers);
        StatefulBeanToCsvBuilder<T> builder = new StatefulBeanToCsvBuilder(writer);
        StatefulBeanToCsv beanToCsv = builder.withMappingStrategy(strategy).build();
        beanToCsv.write(list);
        writer.close();
    }


    @Override
    public String createId() {
        String id = String.valueOf(UUID.randomUUID());
        return id;
    }

    @Override
    public void saveActionRecord(Action object, String userId) throws Exception {
        String path = ACTION_CSV;
        Class cl = Action.class;
        String[] header = ACTION_HEADER;
        switch (object.getType()){
            case "cat":
                path = PAYMENT_CSV;
                cl = Payment.class;
                header=PAYMENT_HEADER;
                break;
            case "dog":
                path = DELIVERY_CSV;
                cl = Delivery.class;
                header=DELIVERY_HEADER;
                break;
            case "bird":
                path = STORAGE_CSV;
                cl = Storage.class;
                header=STORAGE_HEADER;
                break;
            default:
                break;
        }
        List<Action> list = getAllRecords(header, cl, config.getConfigurationEntry(path));
        List<Action> general = getAllRecords(ACTION_HEADER, Action.class, config.getConfigurationEntry(ACTION_CSV));
        object.setId(createId());
        if (list.stream().noneMatch(s -> ((((Action) s).getCountry().equals(object.getCountry())) && (((Action) s).getType().equals(object.getType())) ))) {
            list.add(object);
            general.add(object);
            log.debug(list);
            initRecord(list, header, cl, config.getConfigurationEntry(path));
            initRecord(general, ACTION_HEADER, Action.class, config.getConfigurationEntry(ACTION_CSV));
        } else {
            throw new Exception("Impossible to save pet, owner with this id not found, check owner's id");
        }
        //logToHistory.logObjectChange(object, "saveActionCSV", object.getId());
    }

    @Override
    public void saveUserRecord(User object) throws Exception {
        List<User> list = getAllRecords(USER_HEADER, User.class, config.getConfigurationEntry(USER_CSV));
        if (!list.stream().anyMatch(s -> ((User) s).getLogin() == object.getLogin())) {
            list.add(object);
            initRecord(list, USER_HEADER, User.class, config.getConfigurationEntry(USER_CSV));
        } else {
            throw new Exception("Owner with those parameters has been created previously");
        }
        //logToHistory.logObjectChange(object, "saveUserCSV", object.getId());
    }
    @Override
    public void findForDelActionByUser(String id) throws Exception {
        List<Action> list = getAllRecords(ACTION_HEADER, Action.class, config.getConfigurationEntry(ACTION_CSV));
        for (Action action : list) {
            if (action.getUserId().equals(id)) {
                deleteActionRecord(action);
            }
        }
        if (list.removeIf(s -> s.getUserId().equals(id))) {
            log.info("Action has been deleted");
        } else
            throw new Exception("Delete Action error, check ID");
        initRecord(list, ACTION_HEADER, Action.class, config.getConfigurationEntry(ACTION_CSV));
    }
    @Override
    public void deleteUserRecord(String id) throws Exception {
        List<User> list = getAllRecords(USER_HEADER, User.class, config.getConfigurationEntry(USER_CSV));
        if (list.removeIf(ow -> ow.getUserId().equals(id))) {
            findForDelActionByUser(id);
            log.info("user has been deleted");
        } else
            throw new Exception("Delete user error, check ID");
        initRecord(list, USER_HEADER, User.class, config.getConfigurationEntry(USER_CSV));
    }

    @Override
    public void deleteActionRecord(Action action) throws Exception {
        Class cl = Action.class;
        String[] header =ACTION_HEADER;
        String path = ACTION_CSV;
        switch (action.getType()) {
            case "cat":
                cl = Payment.class;
                header = ACTION_HEADER;
                path = PAYMENT_CSV;
                break;
            case "dog":
                cl = Delivery.class;
                header =DELIVERY_HEADER;
                path = DELIVERY_CSV;
                break;
            case "bird":
                cl = Storage.class;
                header = STORAGE_HEADER;
                path = STORAGE_CSV;
                break;
            default:
                break;
        }
        List<Action> list = getAllRecords(header, cl, config.getConfigurationEntry(path));
        if (list.removeIf(s -> s.getId().equals(action.getId()))) {
            log.info("Pet has been deleted");
        } else
            throw new Exception("Delete pet error, check ID");
        //logToHistory.logObjectChange(action, "deleteActionCSV", action.getId());
        initRecord(list, header, cl, config.getConfigurationEntry(path));
    }

    @Override
    public void deleteOneActionRecord(String id) throws Exception {
        List<Action> list = getAllRecords(ACTION_HEADER, Action.class, config.getConfigurationEntry(ACTION_CSV));
        for (Action action : list){
            if(action.getId().equals(id)){
                deleteActionRecord(action);
            }
        }
        if (list.removeIf(s -> s.getId().equals(id))) {
            log.info("Action has been deleted");
        } else
            throw new Exception("Delete Action error, check ID");
        initRecord(list, ACTION_HEADER, Action.class, config.getConfigurationEntry(ACTION_CSV));
    }

    public Action findForGetActionRecordByUserId(String id) throws Exception {
        List<Action> list = getAllRecords(ACTION_HEADER, Action.class, config.getConfigurationEntry(ACTION_CSV));
        for (Action action : list){
            if (action.getUserId().equals(id)) {
                return getAction(action);
            }
        }
        throw new Exception("Error at get user's list of Actions");
    }


    public Action getAction(Action action) throws Exception {
        String path = ACTION_CSV;
        Class cl = Action.class;
        String[] header = ACTION_HEADER;
        switch (action.getType()){
            case "cat":
                path = PAYMENT_CSV;
                cl = Payment.class;
                header=PAYMENT_HEADER;
                break;
            case "dog":
                path = DELIVERY_CSV;
                cl = Delivery.class;
                header=DELIVERY_HEADER;
                break;
            case "bird":
                path = STORAGE_CSV;
                cl = Storage.class;
                header=STORAGE_HEADER;
                break;
            default:
                break;
        }
        List<Action> list = getAllRecords(header, cl, config.getConfigurationEntry(path));
        for (Action p : list){
            if(p.getId().equals(action.getId()))
                return p;
        }
        throw new Exception("Error in method getPet");
    }

    @Override
    public User getUserRecordByID(String id) throws Exception {
        List<User> list = getAllRecords(USER_HEADER, User.class, config.getConfigurationEntry(USER_CSV));
        for (User user : list) {
            log.debug("get record with id " + id);
            if (user.getUserId().equals(id)) {
                return user;
            }
        }
        throw new Exception("Record not found");
    }

    @Override
    public Action getActionRecordByUserID(String id) throws Exception {
        List<Action> list = getAllRecords(ACTION_HEADER, Action.class, config.getConfigurationEntry(ACTION_CSV));
        for (Action Action : list) {
            log.debug("get record with id " + id);
            if (Action.getUserId().equals(id)) {
                return findForGetActionRecordByUserId(id);
            }
        }
        throw new Exception("Record not found");
    }

    @Override
    public Action getActionRecordByID(String id) throws Exception {
        List<Action> list = getAllRecords(ACTION_HEADER, Action.class, config.getConfigurationEntry(ACTION_CSV));

        for (Action s : list) {
            log.debug("get record with id " + id);
            if (s.getId().equals(id)) {
                return (Action) s;
            }
        }
        throw new Exception("Record not found");
    }


    }


   /* @Override
    public Action getDeliveryRecordByID(String id) throws Exception {
        List<Action> list = getAllRecords(actionHeader, Action.class, config.getConfigurationEntry(ACTION_CSV));

        for (Action s : list) {
            log.debug("get record with id " + id);
            if (s.getId().equals(id)) {
                return (Delivery) s;
            }
        }
        throw new Exception("Record not found");
}*/

}

