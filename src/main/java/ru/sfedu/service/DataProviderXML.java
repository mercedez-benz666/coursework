package ru.sfedu.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlTransient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.myApp.model.Action;
import ru.sfedu.myApp.model.Delivery;
import ru.sfedu.myApp.model.Order;
import ru.sfedu.myApp.model.User;
import ru.sfedu.utils.ConfigUtils;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.sfedu.Constans.*;


public class DataProviderXML implements IDataProvider {

    @XmlTransient
    private static Logger log = LogManager.getLogger(DataProviderXML.class);

    private JAXBContext context;

    static ConfigUtils config = new ConfigUtils();

    public <T> List<T> getAllRecords(String path) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(path);
        InputStreamReader inputData = new InputStreamReader(fis);
        try {
            context = JAXBContext.newInstance(Wrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Wrapper wrap = (Wrapper) unmarshaller.unmarshal(inputData);
            log.debug("records initialization:" + wrap.getList());
            return new ArrayList<T>(wrap.getList());
        } catch (Exception e) {
            log.info("SimpleTest " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public void initRecord(List list, String path) {
        try {
            File f = new File(path);
            FileOutputStream output = new FileOutputStream(f);
            context = JAXBContext.newInstance(Wrapper.class);
            Wrapper wrap = new Wrapper();
            wrap.setList(list);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(wrap, output);
        } catch (Exception e) {
            log.error("Updating error" + e.getMessage());
        }
    }

    @Override
    public String createId() {
        String id = String.valueOf(UUID.randomUUID());
        return id;
    }

    @Override
    public void saveUserRecord(User object) throws Exception {
        List<User> list = getAllRecords(config.getConfigurationEntry(USER_XML));
        if (!list.stream().anyMatch(s -> ((User) s).getLogin() == object.getLogin())) {
            list.add(object);
            initRecord(list, config.getConfigurationEntry(USER_XML));
        } else {
            throw new Exception("Owner with those parameters has been created previously");
        }
        //logToHistory.logObjectChange(object, "saveUserXML", object.getId());
    }

    @Override
    public void saveActionRecord(Action object, String userId) throws Exception {
        String path = ACTION_XML;
        switch (object.getType()) {
            case "payment":
                path = PAYMENT_XML;
                break;
            case "dog":
                path = DELIVERY_XML;
                break;
            case "bird":
                path = STORAGE_XML;
                break;
            default:
                break;
        }
        List<Action> list = getAllRecords(config.getConfigurationEntry(path));
        List<Action> general = getAllRecords(config.getConfigurationEntry(ACTION_XML));
        object.setId(createId());
        if (list.stream().noneMatch(s -> ((((Action) s).getCountry().equals(object.getCountry())) && (((Action) s).getType().equals(object.getType()))))) {
            list.add(object);
            general.add(object);
            log.debug(list);
            initRecord(list, config.getConfigurationEntry(path));
            initRecord(general, config.getConfigurationEntry(ACTION_XML));
        } else {
            throw new Exception("Impossible to save pet, owner with this id not found, check owner's id");
        }
        //logToHistory.logObjectChange(object, "saveActionXML", object.getId());
    }



    @Override
    public void findForDelActionByUser(String id) throws Exception {
        List<Action> list = getAllRecords(config.getConfigurationEntry(ACTION_XML));
        if (list.stream().noneMatch(p -> p.getUserId().equals(id))) {
            throw new Exception("WARNING: user doesn't have any Actions. Only user will be delete");
        }
        for (Action action : list) {
            if (action.getUserId().equals(id)) {
                deleteActionRecord(action);
            }
        }
        if (list.removeIf(s -> s.getUserId().equals(id))) {
            log.info("Action has been deleted");
        } else
            throw new Exception("Delete Action error, check ID");
        initRecord(list, config.getConfigurationEntry(ACTION_XML));
    }

    @Override
    public void deleteUserRecord(String id) throws Exception {
        List<User> list = getAllRecords(config.getConfigurationEntry(USER_XML));
        if (list.removeIf(ow -> ow.getUserId().equals(id))) {
            try {
                findForDelActionByUser(id);
            } catch (Exception e) {
                log.warn("WARNING: user doesn't have any Actions. Only user will be delete");
            }
            log.info("user has been deleted");
        } else
            throw new Exception("Delete user error, check ID");
        initRecord(list, config.getConfigurationEntry(USER_XML));
    }

    @Override
    public void deleteActionRecord(Action action) throws Exception {
        //logToHistory.logObjectChange(action, "deleteActionXML", action.getId());
        String path = ACTION_XML;
        switch (action.getType()) {
            case "cat":
                path = PAYMENT_XML;
                break;
            case "dog":
                path = DELIVERY_XML;
                break;
            case "bird":
                path = STORAGE_XML;
                break;
            default:
                break;
        }
        List<Action> list = getAllRecords(config.getConfigurationEntry(path));
        if (list.removeIf(s -> s.getId().equals(action.getId()))) {
            log.info("Pet has been deleted");
        } else
            throw new Exception("Delete pet error, check ID");
        initRecord(list, config.getConfigurationEntry(path));
    }

    @Override
    public void deleteOneActionRecord(String id) throws Exception {
        List<Action> list = getAllRecords(config.getConfigurationEntry(ACTION_XML));
        for (Action action : list) {
            if (action.getId().equals(id)) {
                deleteActionRecord(action);
            }
        }
        if (list.removeIf(s -> s.getId().equals(id))) {
            log.info("Action has been deleted");
        } else
            throw new Exception("Delete Action error, check ID");
        initRecord(list, config.getConfigurationEntry(ACTION_XML));
    }

    public Action findForGetDeliveryRecordByUserId(String id) throws Exception {
        List<Action> list = getAllRecords(config.getConfigurationEntry(DELIVERY_XML));
        if (list.stream().noneMatch(p -> p.getUserId().equals(id))) {
            return new Delivery();
        }
        for (Action action : list) {
            if (action.getUserId().equals(id)) {
                return getAction(action);
            }
        }
        throw new Exception("Error at get user's list of Actions");
    }

    public Action getAction(Action action) throws Exception {
        String path = ACTION_XML;
        switch (action.getType()) {
            case "payment":
                path = PAYMENT_XML;
                break;
            case "delivery":
                path = DELIVERY_XML;
                break;
            case "storage":
                path = STORAGE_XML;
                break;
            default:
                break;
        }
        List<Action> list = getAllRecords(config.getConfigurationEntry(path));
        for (Action p : list) {
            if (p.getId().equals(action.getId()))
                return p;
        }
        throw new Exception("Error in method getPet");
    }

    @Override
    public User getUserRecordByID(String id) throws Exception {
        List<User> list = getAllRecords(config.getConfigurationEntry(USER_XML));
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
        List<Action> list = getAllRecords(config.getConfigurationEntry(DELIVERY_XML));
        for (Action action : list) {
            log.debug("get record with id " + id);
            if (action.getUserId().equals(id)) {
                return findForGetDeliveryRecordByUserId(id);
            }
        }
        throw new Exception("Record not found");
    }

    /*@Override
    public Delivery getDeliveryRecordByID(String id) throws Exception {
        List<Delivery> list = getAllRecords(config.getConfigurationEntry(DELIVERY_XML));
        for ( Delivery delivery : list) {
            log.debug("get record with id " + id);
            if (delivery.getId().equals(id)) {
                return (Delivery) getAction(delivery);
            }
        }
        throw new Exception("Record not found");
    }*/

    @Override
    public Action getActionRecordByID(String id) throws Exception {
        List<Action> list = getAllRecords(config.getConfigurationEntry(ACTION_XML));
        for (Action action : list) {
            log.debug("get record with id " + id);
            if (action.getId().equals(id)) {
                return getAction(action);
            }
        }
        throw new Exception("Record not found");
    }


}


