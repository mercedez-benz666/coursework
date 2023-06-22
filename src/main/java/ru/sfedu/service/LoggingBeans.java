package ru.sfedu.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import org.bson.Document;
import ru.sfedu.utils.ConfigUtils;


import java.io.IOException;
import java.time.ZonedDateTime;

import static ru.sfedu.Constans.*;

public class LoggingBeans {
    ConfigUtils config = new ConfigUtils();

    private XStream xstream = new XStream(new JettisonMappedXmlDriver());
    private MongoClient mongoClient;
    private MongoDatabase database;
    private HistoryContent content;

    public LoggingBeans() throws IOException {
        xstream.setMode(XStream.NO_REFERENCES);
        mongoClient = MongoClients.create(config.getConfigurationEntry(MONGO_PATH));
        database = mongoClient.getDatabase(config.getConfigurationEntry(MONGO_DB_NAME));
    }

    private HistoryContent createHistoryContent(Object obj, String method, String id) {
        HistoryContent newHistory = new HistoryContent();
        newHistory.setClassName(obj.getClass().getName());
        newHistory.setMethodName(method);
        newHistory.setObjectId(id);
        newHistory.setOperationDate(ZonedDateTime.now().toString());
        newHistory.setObject(xstream.toXML(obj));
        return newHistory;
    }

    public void logObjectChange(Object obj, String method, String id) throws IOException {
        content = createHistoryContent(obj, method, id);
        writeChanges();
    }

    private void writeChanges() throws IOException {
        MongoCollection<Document> collection = database.getCollection(config.getConfigurationEntry(MONGO_COLLECTION));
        Document document = new Document("ID", content.getObjectId())
                .append("class Name",content.getClassName())
                .append("date", content.getOperationDate())
                .append("method", content.getMethodName())
                .append("JSON string", content.getObject());

        collection.insertOne(document);
    }
}
