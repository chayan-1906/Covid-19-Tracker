package com.example.covid_19tracker;

public class StateWiseModel {

    private final String state;
    private final String confirmed;
    private final String confirmedNew;
    private final String active;
    private final String death;
    private final String deathNew;
    private final String recovered;
    private final String recoveredNew;
    private final String lastUpdate;

    public StateWiseModel(String state, String confirmed, String confirmedNew, String active, String death, String deathNew, String recovered, String recoveredNew, String lastUpdate) {
        this.state = state;
        this.confirmed = confirmed;
        this.confirmedNew = confirmedNew;
        this.active = active;
        this.death = death;
        this.deathNew = deathNew;
        this.recovered = recovered;
        this.recoveredNew = recoveredNew;
        this.lastUpdate = lastUpdate;
    }

    public String getState() {
        return state;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getConfirmedNew() {
        return confirmedNew;
    }

    public String getActive() {
        return active;
    }

    public String getDeath() {
        return death;
    }

    public String getDeathNew() {
        return deathNew;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getRecoveredNew() {
        return recoveredNew;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }
}
