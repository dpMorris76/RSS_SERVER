package com.centurylink.rss.web.controller;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
 
public class FileUpload {
 
    private List<MultipartFile> uploadedFiles;
 
    public List<MultipartFile> getFiles() {
        return uploadedFiles;
    }
 
    public void setFiles(List<MultipartFile> files) {
        this.uploadedFiles = files;
    }
}