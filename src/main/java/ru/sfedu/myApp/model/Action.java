package ru.sfedu.myApp.model;

import com.opencsv.bean.CsvBindByPosition;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Action{
    @XmlElement(name = "Id")
    @CsvBindByPosition(position = 0)
    private String id;
    @XmlElement(name = "userId")
    @CsvBindByPosition(position = 1)
    private String userId;
    @XmlElement(name = "type")
    @CsvBindByPosition(position = 2)
    private String actionType;
    @XmlElement(name="country")
    @CsvBindByPosition(position = 3)
    private String country;
    @XmlElement(name = "numberAction")
    @CsvBindByPosition(position = 4)
    private int numberAction;


    public Action(String actionType,String country,int numberAction) {
        this.id = UUID.randomUUID().toString();
        this.userId = UUID.randomUUID().toString();
        this.actionType=actionType;
        this.country=country;
        this.numberAction=numberAction;
    }

    public Action() {
    }

    public int getNumberAction() {
        return numberAction;
    }
    public void setNumberAction(int numberAction) {
        this.numberAction = numberAction;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getType() {
        return actionType;
    }
    public void setType(String type) {
        this.actionType = actionType;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,country, actionType);
    }

}
