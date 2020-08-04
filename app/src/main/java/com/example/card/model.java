package com.example.card;

public class model {
    private String totalCases;
    private String totalCurrentCases;
    private String totalRecovered;
    private String totalDeaths;
    private String stateName;

    model(String tc,String tcc,String tr,String td,String state)
    {
        this.totalCases=tc;
        totalCurrentCases=tcc;
        totalDeaths=td;
        totalRecovered=tr;
        stateName=state;
    }

    public String getTotalCases() {
        return totalCases;
    }

    public String getTotalCurrentCases() {
        return totalCurrentCases;
    }

    public String getTotalRecovered() {
        return totalRecovered;
    }

    public String getTotalDeaths() {
        return totalDeaths;
    }
    public String getStateName(){
        return stateName;
    }
}
