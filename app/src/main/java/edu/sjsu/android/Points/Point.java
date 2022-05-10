package edu.sjsu.android.Points;

public class Point {
    private double latitude;
    private double longitude;
    private float zoom;
    private String title;
    private String description;

    public Point(double latitude, double longitude, float zoom, String title, String description) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoom = zoom;
        this.title = title;
        this.description = description;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
