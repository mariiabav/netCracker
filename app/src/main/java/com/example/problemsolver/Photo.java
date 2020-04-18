
package com.example.problemsolver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("fileDownloadUri")
    @Expose
    private String fileDownloadUri;
    @SerializedName("fileType")
    @Expose
    private String fileType;
    @SerializedName("size")
    @Expose
    private Integer size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

}
