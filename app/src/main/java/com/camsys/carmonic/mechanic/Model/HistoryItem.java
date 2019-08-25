package com.camsys.carmonic.mechanic.Model;

public class HistoryItem {

    String itemDate;
    String itemRequester;
    String amount;
    int numberOfStar;


    public void  setItemDate(String itemDate){
        this.itemDate = itemDate;
    }

    public String getItemDate(){
        return  itemDate;
    }


    public void  setItemRequester(String itemRequester){
        this.itemRequester = itemRequester;
    }

    public String getItemRequester(){
        return  itemRequester;
    }


    public void  setAmount(String amount){
        this.amount = amount;
    }

    public String getAmount(){
        return  amount;
    }


    public void  setNumberOfStar(int numberOfStar){
        this.numberOfStar = numberOfStar;
    }

    public int getNumberOfStar(){
        return  numberOfStar;
    }
}
