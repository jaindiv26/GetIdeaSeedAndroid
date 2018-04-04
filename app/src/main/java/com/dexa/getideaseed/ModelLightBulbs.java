package com.dexa.getideaseed;

public class ModelLightBulbs {
    private String ideaId,ideaName,lightbulbGiverId,lightbulbReceiverId;
    private int lightbulbs;

    public String getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(String ideaId) {
        this.ideaId = ideaId;
    }

    public String getIdeaName() {
        return ideaName;
    }

    public void setIdeaName(String ideaName) {
        this.ideaName = ideaName;
    }

    public String getLightbulbGiverId() {
        return lightbulbGiverId;
    }

    public void setLightbulbGiverId(String lightbulbGiverId) {
        this.lightbulbGiverId = lightbulbGiverId;
    }

    public String getLightbulbReceiverId() {
        return lightbulbReceiverId;
    }

    public void setLightbulbReceiverId(String lightbulbReceiverId) {
        this.lightbulbReceiverId = lightbulbReceiverId;
    }

    public int getLightbulbs() {
        return lightbulbs;
    }

    public void setLightbulbs(int lightbulbs) {
        this.lightbulbs = lightbulbs;
    }
}
