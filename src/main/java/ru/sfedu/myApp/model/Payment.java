package ru.sfedu.myApp.model;

import com.opencsv.bean.CsvBindByPosition;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import ru.sfedu.myApp.model.Action;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Payment extends Action {

    @XmlElement(name = "money")
    @CsvBindByPosition(position = 4)
    private double money;

    public Payment(){
    }
    public Payment(double money,String actionType, String country, int numberAction) {
        super(actionType,country,numberAction);

        this.money = money;
    }
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
