package ru.sfedu.myApp.model;

import com.opencsv.bean.CsvBindByPosition;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class History {
    @XmlElement(name = "actionName")
    @CsvBindByPosition(position = 0)
    private String actionName;

    @XmlElement(name = "actionId")
    @CsvBindByPosition(position = 1)
    private String actionId;

    @XmlElement(name = "userId")
    @CsvBindByPosition(position = 2)
    private String userId;

/*
    @XmlElement(name = "serviceName")
    @CsvBindByPosition(position = 3)
    private String serviceName;
    */


    @XmlElement(name = "priceForService")
    @CsvBindByPosition(position = 5)
    private double price;

    @XmlElement(name = "date")
    @CsvBindByPosition(position = 6)
    private Date date;
    public History(){
    }
    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /*public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    */

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "History{" +
                "actionName='" + actionName + '\'' +
                ", actionId='" + actionId + '\'' +
                ", userId='" + userId + '\'' +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
