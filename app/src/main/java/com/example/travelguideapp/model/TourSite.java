package com.example.travelguideapp.model;

public class TourSite {
    String siteName;
    String description;
    String siteLocation;
    double latitude;
    double longitude;
    String createdBy;
    String pictureUrl;
    String videoUrl;
    String recommendedSeason;

    public TourSite() {
    }

    public TourSite(String siteName, String description, String siteLocation, double latitude, double longitude, String createdBy, String pictureUrl, String videoUrl, String recommendedSeason) {
        this.siteName = siteName;
        this.description = description;

        this.siteLocation = siteLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdBy = createdBy;
        this.pictureUrl = pictureUrl;
        this.videoUrl = videoUrl;
        this.recommendedSeason = recommendedSeason;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getDescription() {
        return description;
    }

    public String getSiteLocation() {
        return siteLocation;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getRecommendedSeason() {
        return recommendedSeason;
    }
}
