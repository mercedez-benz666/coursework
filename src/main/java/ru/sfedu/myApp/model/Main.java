package ru.sfedu.myApp.model;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.service.DataBaseProvider;
import ru.sfedu.service.DataProviderCSV;
import ru.sfedu.service.DataProviderXML;
import ru.sfedu.service.IDataProvider;
import ru.sfedu.utils.ConfigUtils;

public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);
    private static IDataProvider dataProvider = new DataProviderXML();
    private static ConfigUtils configUtils = new ConfigUtils();

    public static void main(String[] args) throws Exception {
        Options options =new Options();
        Option optionUserSave = new Option("userSave", true, "create user");
        Option optionGetUser = new Option("userGet", true,"get user");
        Option optionDeleteUser = new Option("userDelete", true, "delete user");
        Option optionSaveAction = new Option("actionSave", true, "save action");
        Option optionGetAction = new Option("actionGet", true, "get action");
        Option optionDeleteAction = new Option("actionDelete", true, " delete action");
        Option optionSaveOrder = new Option("saveOrder", true,"save order");
        Option optionGetOrder = new Option("getOrder", true, "get order");
        Option optionDeleteOrder = new Option("deleteOrder", true,"delete order");
        Option optionUserAddDelivery = new Option("userAddDelivery", true, "user add delivery to order");
        Option optionUserAddStorage = new Option("userAddStorage", true, "user add storage to order");
        Option optionUserAddDeliveryInsurance = new Option("userAddDeliveryInsurance", true, "user delivery insurance to order");
        Option optionUserAddStorageInsurance = new Option("userAddStorageInsurance", "user add storage insurance to order");
        Option optionUserPayOrder = new Option("userPayOrder", true, "user pay order");

        options.addOption(optionUserSave);
        options.addOption(optionGetUser);
        options.addOption(optionDeleteUser);
        options.addOption(optionSaveAction);
        options.addOption(optionGetAction);
        options.addOption(optionDeleteAction);
        options.addOption(optionSaveOrder);
        options.addOption(optionGetOrder);
        options.addOption(optionDeleteOrder);
        options.addOption(optionUserAddDelivery);
        options.addOption(optionUserAddStorage);
        options.addOption(optionUserAddDeliveryInsurance);
        options.addOption(optionUserAddStorageInsurance);
        options.addOption(optionUserPayOrder);

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        if(commandLine.hasOption("userSave")){
            initProvider(args[1]);
            User user = new User(args[2], args[3], args[4]);
            dataProvider.saveUserRecord(user);
        }
        if(commandLine.hasOption("userGet")){
            initProvider(args[1]);
            log.info(dataProvider.getUserRecordByID(args[2]));
        }
        if(commandLine.hasOption("userDelete")){
            initProvider(args[1]);
            dataProvider.deleteUserRecord(args[2]);
        }
        if (commandLine.hasOption("actionSave")){
            initProvider(args[1]);
            Action action = new Action();
            switch (args[2]){
                case "delivery": action = new Delivery(Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Boolean.parseBoolean(args[6]), Double.parseDouble(args[7]), "delivery", args[8], Integer.parseInt(args[9]));
                dataProvider.saveActionRecord(action, args[10]);break;
                case "storage": action = new Storage(Integer.parseInt(args[2]), Integer.parseInt(args[3]),Integer.parseInt(args[4]) ,Boolean.parseBoolean(args[5]), Integer.parseInt(args[6]), "storage", args[7], Integer.parseInt(args[8]));
                dataProvider.saveActionRecord(action, args[7]);break;
            }
        }
        if(commandLine.hasOption("actionGet")){
            initProvider(args[1]);
            log.info(dataProvider.getActionRecordByID(args[2]));
        }
        if(commandLine.hasOption("actionDelete")){
            initProvider(args[1]);
            Action action = new Action();
            switch (args[2]){
                case "delivery": action = new Delivery(); action.setId(args[3]);
                case "storage": action = new Delivery(); action.setId(args[3]);
            }
            dataProvider.deleteActionRecord(action);
        }

        if(commandLine.hasOption("userAddDelivery")){
            initProvider(args[1]);
            User user = dataProvider.getUserRecordByID(args[2]);
            user.addDelivery(Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Double.parseDouble(args[6]));
            log.info(user.getOrder());
        }
        if(commandLine.hasOption("userAddStorage")){
            initProvider(args[1]);
            User user = dataProvider.getUserRecordByID(args[2]);
            user.addStorage(Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
            log.info(user.getOrder());
        }
        if(commandLine.hasOption("userAddDeliveryInsurance")){
            initProvider(args[1]);
            User user = dataProvider.getUserRecordByID(args[2]);
            user.addDeliveryInsurance();
        }
        if(commandLine.hasOption("userAddStorageInsurance")){
            initProvider(args[1]);
            User user = dataProvider.getUserRecordByID(args[2]);
            user.addStorageInsurance();
        }
        if(commandLine.hasOption("userPayOrder")){
            initProvider(args[1]);
            User user = dataProvider.getUserRecordByID(args[2]);
            user.payOrder(Double.parseDouble(args[3]));
            log.info("User history: "+user.getHistories());
        }
    }
    private static void initProvider(String name) throws Exception {
        switch (name) {
            case "DB":
                dataProvider = new DataBaseProvider();
                break;
            case "XML":
                dataProvider = new DataProviderXML();
                break;
            case "CSV":
                dataProvider = new DataProviderCSV();
                break;
        }
    }
}
