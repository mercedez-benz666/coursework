package ru.sfedu.myApp.model;

import com.opencsv.bean.CsvBindByPosition;
import jakarta.xml.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    private Logger log = LogManager.getLogger(User.class);
    @XmlElement(name = "login")
    @CsvBindByPosition(position = 0)
    private String login;
    @XmlElement(name = "password")
    @CsvBindByPosition(position = 1)
    private String password;
    @XmlElement(name = "userType")
    @CsvBindByPosition(position = 2)
    private String userType;
    @XmlElement(name = "userId")
    @CsvBindByPosition(position = 3)
    private String id;
    @XmlTransient
    private String userActions = getUserActions();
    private List<History> histories = new ArrayList<>();
    private Order order = new Order();


    public User(){
    }
    public User(String login, String password, String userType) {
        this.id = UUID.randomUUID().toString();
        this.login = login;
        this.password = password;
        this.userType = userType;
    }
    public String getUserId() {
        return id;
    }

    public void setUserId(String id) {
        this.id = id;
    }

    public String getUserActions() {
        return userActions;
    }

    public void setUserActions(String userActions) {
        this.userActions = userActions;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    public void addDelivery(int length, int height, int width, double distance) throws Exception {
        order.setDelivery(new Delivery());
        order.addDelivery(length, height, width, distance);
    }
    public void addStorage(int length, int height, int width,int dayCount) throws Exception {
        order.setStorage(new Storage());
        order.addStorage(length, height, width, dayCount);
    }
    public void addStorageInsurance() throws Exception {
        order.addStorageInsurance();
    }
    public void addDeliveryInsurance() throws Exception {
        order.addDeliveryInsurance();
    }
    public void payOrder(double finalCost) throws Exception {
        if(order.getFinalCost()!=0 && order.getFinalCost() == finalCost){
            History history = new History();
            history.setDate(new Date());
            history.setUserId(this.id);
            history.setPrice(finalCost);
            histories.add(history);
            this.order = new Order();
            log.info("User payed order "+id);

        }
        else {
            log.error("Order doesn't exits or final cost is not enough");
            throw new Exception("Order doesn't exits or final cost is not enough");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return login == user.login && id.equals(user.id) && password.equals(user.password) && userType.equals(user.userType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password,userType, userActions);
    }

}
