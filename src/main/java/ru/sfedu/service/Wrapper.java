package ru.sfedu.service;

import jakarta.xml.bind.annotation.*;
import ru.sfedu.myApp.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Action.class, Delivery.class, Payment.class, Storage.class, User.class})
public class Wrapper<T> implements Serializable {


    @XmlElementWrapper(name = "wrapper")
    @XmlElement
    private List<T> list = new ArrayList<>();

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

