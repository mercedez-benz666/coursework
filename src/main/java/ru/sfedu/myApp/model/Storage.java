package ru.sfedu.myApp.model;

import com.opencsv.bean.CsvBindByPosition;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import ru.sfedu.myApp.model.Action;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Storage extends Action {
    @XmlElement(name = "dateCount")
    @CsvBindByPosition(position = 8)
    private int dateCount;
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


    public Storage(int height,int width, int length, boolean insurance, int dateCount, String actionType,String country,int numberAction){
        super(actionType,country,numberAction);
        this.height=height;
        this.width=width;
        this.length=length;
        this.dateCount=dateCount;
        this.insurance=insurance;
    }
    public Storage(){
    }
    public boolean getInsurance() {
        return insurance;
    }
    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }
    public int getDateCount() {
        return dateCount;
    }

    public void setDateCount(int dateCount) {
        this.dateCount = dateCount;
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
