package com.lingxiaosuse.picture.tudimension.modle;

import java.util.List;

public class TuWanModle {
    private List<ImageData> res;

    public List<ImageData> getRes() {
        return res;
    }

    public void setRes(List<ImageData> res) {
        this.res = res;
    }

    public class ImageData{
        private String id;
        private String title;
        private String header;
        private String download;
        private String time;
        private String total;

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

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
}
