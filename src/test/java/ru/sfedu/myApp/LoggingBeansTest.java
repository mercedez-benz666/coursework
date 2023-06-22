package ru.sfedu.myApp;

import org.junit.jupiter.api.Test;
import ru.sfedu.myApp.model.Delivery;
import ru.sfedu.myApp.model.User;
import ru.sfedu.service.LoggingBeans;

class LoggingBeansTest {
    @Test
    public void testMongoDB() throws Exception{
        LoggingBeans logToHistory = new LoggingBeans();

        Delivery test1 = new Delivery(12,23,34,true,34555,"delivery","russia",10);
        logToHistory.logObjectChange(test1, "testAction", test1.getId());

        User test2 = new User("merc122222","123","user");
        logToHistory.logObjectChange(test2, "testUser", test2.getUserId());
    }
}

