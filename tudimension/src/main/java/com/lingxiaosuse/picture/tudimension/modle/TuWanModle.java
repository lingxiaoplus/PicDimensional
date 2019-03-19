package com.lingxiaosuse.picture.tudimension.modle;

import java.util.List;

public class TuWanModle {
    private String id;
    private String title;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private List<ImageData> imageList;

    public List<ImageData> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageData> res) {
        this.imageList = res;
    }

    public class ImageData{
        private String id;
        private String title;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
