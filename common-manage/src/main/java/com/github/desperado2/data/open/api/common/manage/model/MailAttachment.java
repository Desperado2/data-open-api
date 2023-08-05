package com.github.desperado2.data.open.api.common.manage.model;


import java.io.File;


public class MailAttachment {
    private String name;
    private File file;
    private String url = null;

    private boolean isImage = false;

    public MailAttachment(String name, File file, String url, boolean isImage) {
        this.name = name;
        this.file = file;
        this.url = url;
        this.isImage = isImage;
    }

    public MailAttachment(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }
}
