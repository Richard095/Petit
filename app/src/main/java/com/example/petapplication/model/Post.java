package com.example.petapplication.model;



public class Post {

    private String imageURL;
    private String petName;
    private String status; /*available | adopted*/
    private int likes;
    private int petpostId;
    private int weight;

    private String vaccinated;
    private String dewormed;
    private String healthy;
    private String sterilized;
    private String descriptions;
    private int fromUserId;



    public Post(String imageURL, String petName, String status, int likes,int petpostId,
                int weight,String vaccinated,String dewormed,String healthy,String sterilized,String descriptions,int fromUserId) {
        this.imageURL = imageURL;
        this.petName = petName;
        this.status = status;
        this.likes = likes;
        this.petpostId = petpostId;
        this.weight = weight;
        this.vaccinated =vaccinated;
        this.dewormed =dewormed;
        this.healthy = healthy;
        this.sterilized = sterilized;
        this.descriptions = descriptions;
        this.fromUserId  = fromUserId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(String vaccinated) {
        this.vaccinated = vaccinated;
    }

    public String getDewormed() {
        return dewormed;
    }

    public void setDewormed(String dewormed) {
        this.dewormed = dewormed;
    }

    public String getHealthy() {
        return healthy;
    }

    public void setHealthy(String healthy) {
        this.healthy = healthy;
    }

    public String getSterilized() {
        return sterilized;
    }

    public void setSterilized(String sterilized) {
        this.sterilized = sterilized;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public int getPetpostId() {
        return petpostId;
    }

    public void setPetpostId(int petpostId) {
        this.petpostId = petpostId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
