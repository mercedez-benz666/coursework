package ru.sfedu.myApp.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.utils.ConfigUtils;
import ru.sfedu.service.DataProviderXML;

import java.io.IOException;
import java.util.UUID;

public class Order {
    Logger log = LogManager.getLogger(Order.class);
    private String userId;
    private String id;
    private static final int RUBLES_PER_CUB_METER = 22;
    private static final int RUBLES_PER_KILOMETER = 20;
    private static final int INSURANCE_COST = 20000;
    private double finalCost ;
    private boolean deliveryFlag;
    private boolean storageFlag;
    private boolean insuranceForDeliveryFlag;
    private boolean insuranceForStorageFlag;
    private Delivery delivery ;
    private Storage storage ;
    public Order()  {
    }

    public Order(boolean deliveryFlag, boolean storageFlag, boolean insuranceForDeliveryFlag, boolean insuranceForStorageFlag) {
        this.deliveryFlag = deliveryFlag;
        this.storageFlag = storageFlag;
        this.insuranceForDeliveryFlag = insuranceForDeliveryFlag;
        this.insuranceForStorageFlag = insuranceForStorageFlag;
    }

    public String createId() {
        String id = String.valueOf(UUID.randomUUID());
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDeliveryFlag(boolean deliveryFlag) {
        this.deliveryFlag = deliveryFlag;
    }

    public void setStorageFlag(boolean storageFlag) {
        this.storageFlag = storageFlag;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public double getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(double finalCost) {
        this.finalCost = finalCost;
    }

    public boolean isInsuranceForDeliveryFlag() {
        return insuranceForDeliveryFlag;
    }

    public void setInsuranceForDeliveryFlag(boolean insuranceForDeliveryFlag) {
        this.insuranceForDeliveryFlag = insuranceForDeliveryFlag;
    }

    public boolean isInsuranceForStorageFlag() {
        return insuranceForStorageFlag;
    }

    public void setInsuranceForStorageFlag(boolean insuranceForStorageFlag) {
        this.insuranceForStorageFlag = insuranceForStorageFlag;
    }

    public boolean isDeliveryFlag() {
        return deliveryFlag;
    }

    public boolean isStorageFlag() {
        return storageFlag;
    }

    public void addDelivery(int length, int height, int width, double distance) throws Exception {
        if(!deliveryFlag){
            delivery = new Delivery();
            delivery.setLength(length);
            delivery.setHeight(height);
            delivery.setWidth(width);
            delivery.setDistance(distance);
            this.deliveryFlag = true;

            finalCost += RUBLES_PER_KILOMETER * distance;
            log.info("Add delivery to order with id "+id);
        }
        else {
            log.error("Delivery has been already added in order "+id);
            throw new Exception("Delivery has been already added");
        }
    }
    public void addStorage(int length,int width,int height,int dateCount) throws Exception {
        if(!storageFlag){
            storage = new Storage();
            storage.setLength(length);
            storage.setWidth(width);
            storage.setHeight(height);
            storage.setDateCount(dateCount);
            this.storageFlag = true;
            finalCost += (RUBLES_PER_CUB_METER * countVolume(length,height,width)) * dateCount;
            log.info("Add storage to order with id "+id);
        }
        else {
            log.error("Storage has been already added in order "+id);
            throw new Exception("Storage has been already added");
        }
    }
    public void addStorageInsurance() throws Exception {
        if(!insuranceForStorageFlag){
            insuranceForStorageFlag = true;
            storage.setInsurance(true);
            finalCost += INSURANCE_COST;
            log.info("Add storage insurance to order with id "+id);
        }
        else {
            log.error("Storage insurance has been already added in order "+id);
            throw new Exception("Storage insurance has been already added");
        }
    }
    public void addDeliveryInsurance() throws Exception {
        if(!insuranceForDeliveryFlag){
            insuranceForDeliveryFlag = true;
            delivery.setInsurance(true);
            finalCost += INSURANCE_COST;
            log.info("Add delivery insurance to order with id "+id);
        }
        else {
            log.error("Delivery insurance has been already added in order "+id);
            throw new Exception("Delivery insurance has been already added");
        }
    }
    public int countVolume(int len,int height,int width){
        return  len*height*width;
    }

    @Override
    public String toString() {
        return "Order{" +
                "finalCost=" + finalCost +
                ", deliveryFlag=" + deliveryFlag +
                ", storageFlag=" + storageFlag +
                ", insuranceForDeliveryFlag=" + insuranceForDeliveryFlag +
                ", insuranceForStorageFlag=" + insuranceForStorageFlag +
                ", delivery=" + delivery +
                ", storage=" + storage +
                '}';
    }
}
