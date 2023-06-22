package ru.sfedu.myApp.model;

import com.opencsv.bean.CsvBindByPosition;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import ru.sfedu.myApp.model.Action;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Delivery extends Action {
    @XmlElement(name = "distance")
    @CsvBindByPosition(position = 8)
    private double distance;
    @XmlElement(name = "insurance")
    @CsvBindByPosition(position = 7)
    private boolean insurance;
    @XmlElement(name = "height")
    @CsvBindByPosition(position = 4)
    private int height;
    @XmlElement(name = "width")
    @CsvBindByPosition(position = 5)
    private int width;
    @XmlElement(name = "length")
    @CsvBindByPosition(position = 6)
    private int length;
    public Delivery(){

    }

    public Delivery(int height,int width, int length, boolean insurance,double distance, String actionType, String country,int numberAction) {
        super(actionType,country,numberAction);
        this.height=height;
        this.width=width;
        this.length=length;
        this.insurance=insurance;
        this.distance = distance;
    }
    public boolean getInsurance() {
        return insurance;
    }
    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
