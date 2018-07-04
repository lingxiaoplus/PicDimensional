package com.lingxiaosuse.picture.tudimension.modle;

import java.util.List;

/**
 * Created by lingxiao on 2017/9/28.
 */

public class SearchResultModle {
        private StatusBean status;
        private int type;
        private List<SearchBean> search;

        public StatusBean getStatus() {
            return status;
        }

        public void setStatus(StatusBean status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<SearchBean> getSearch() {
            return search;
        }

        public void setSearch(List<SearchBean> search) {
            this.search = search;
        }

        public static class StatusBean {
            /**
             * live : 0
             * lock : 0
             * ring : 0
             * wallpaper : 0
             * subject : 0
             */

            private int live;
            private int lock;
            private int ring;
            private int wallpaper;
            private int subject;

            public int getLive() {
                return live;
            }

            public void setLive(int live) {
                this.live = live;
            }

            public int getLock() {
                return lock;
            }

            public void setLock(int lock) {
                this.lock = lock;
            }

            public int getRing() {
                return ring;
            }

            public void setRing(int ring) {
                this.ring = ring;
            }

            public int getWallpaper() {
                return wallpaper;
            }

            public void setWallpaper(int wallpaper) {
                this.wallpaper = wallpaper;
            }

            public int getSubject() {
                return subject;
            }

            public void setSubject(int subject) {
                this.subject = subject;
            }
        }

        public static class SearchBean {
            /**
             * items : []
             * total : 0
             * type : 8
             * title : 专题
             */

            private int total;
            private int type;
            private String title;
            private List<WallPaper> items;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<WallPaper> getItems() {
                return items;
            }

            public void setItems(List<WallPaper> items) {
                this.items = items;
            }
        }
        public static class WallPaper{
            private String img;
            private String desc;
            private String id;
            private String cover;
            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }
        }

}
