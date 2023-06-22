package ru.sfedu;

public class Constans {
    public static final String ACTION_XML = "ACTION_XML";
    public static final String USER_XML="USER_XML";
    public static final  String PAYMENT_XML="PAYMENT_XML";
    public static final  String DELIVERY_XML="DELIVERY_XML";
    public static final  String STORAGE_XML="STORAGE_XML";
    public static final String[] USER_HEADER = {"id", "login", "password", "type"};
    public static final String[] ACTION_HEADER = {"id", "type", "country","userId"};
    public static final String[] DELIVERY_HEADER = {"id", "height", "width", "length", "insurance", "distance", "type", "country", "userId"};
    public static final String[] STORAGE_HEADER = {"id", "height", "width", "length","insurance","dateCount", "type","country", "userId"};
    public static final String[] PAYMENT_HEADER = {"id", "money", "type", "country", "userId"};
    public static final String[] HISTORY_HEADER = {"actionName", "actionId", "userId", "date"};

    public static final String ACTION_CSV = "ACTION_CSV";
    public static final String USER_CSV="USER_CSV";
    public static final String DELIVERY_CSV = "DELIVERY_CSV";
    public static final String PAYMENT_CSV = "PAYMENT_CSV";
    public static final String STORAGE_CSV = "STORAGE_CSV";

    public static final String PATH_TO_DB = "PATH_TO_DB";
    public static final String USER_TO_DB = "USER_TO_DB";
    public static final String PASS_TO_DB = "PASS_TO_DB";

    public static final String ACTION_DB = "ACTION_DB";
    public static final String PAYMENT_DB = "PAYMENT_DB";
    public static final String STORAGE_DB = "STORAGE_DB";
    public static final String DELIVERY_DB = "DELIVERY_DB";


    public static final String USER_DB = "USER_DB";
    public static final String LINKTABLE_DB = "LINKTABLE_DB";

    public static final String STOCK_DB = "STOCK_DB";
    public static final String TRANSPORT_DB = "TRANSPORT_DB";

    public static final String MONGO_DB_NAME = "MONGO_DB_NAME";
    public static final String MONGO_COLLECTION = "MONGO_COLLECTION";
    public static final String MONGO_PATH = "MONGO_PATH";


    public static final String TOTAL = "TOTAL";
    public static final String[] USER_HEADERS = {"id", "login", "password", "type"};
    public static final String[] ACTION_HEADERS = {"id", "type", "country","userId"};
    public static final String[] DELIVERY_HEADERS = {"id", "height", "width", "length", "insurance", "distance", "type", "country", "userId"};
    public static final String[] STORAGE_HEADERS = {"id", "height", "width", "length","insurance","dateCount", "type","country", "userId"};
    public static final String[] PAYMENT_HEADERS = {"id", "money", "type", "country", "userId"};
    public static final String[] HYSTORY_HEADERS = {"actionName", "actionId", "userId", "date"};


    public enum ActionTypes {
        DELIVERY,PAYMENT,STORAGE
    }
}
