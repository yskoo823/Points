package edu.sjsu.android.Points;

import android.os.Parcel;
import android.os.Parcelable;

public class Point implements Parcelable {
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


    protected Point(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        zoom = in.readFloat();
        title = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeFloat(zoom);
        dest.writeString(title);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

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
