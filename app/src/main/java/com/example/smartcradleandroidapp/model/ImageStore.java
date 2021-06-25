package com.example.smartcradleandroidapp.model;

public class ImageStore {
    private String dateLabel;
    private String fileName;
    private String labelFound;

    public ImageStore() {
    }

    public ImageStore(String dateLabel, String fileName, String labelFound) {
        this.dateLabel = dateLabel;
        this.fileName = fileName;
        this.labelFound = labelFound;
    }

    public String getDateLabel() {
        return dateLabel;
    }

    public void setDateLabel(String dateLabel) {
        this.dateLabel = dateLabel;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLabelFound() {
        return labelFound;
    }

    public void setLabelFound(String labelFound) {
        this.labelFound = labelFound;
    }
}
