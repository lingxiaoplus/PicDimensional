package com.lingxiaosuse.picture.tudimension.modle;

import java.util.List;

/**
 * Created by lingxiao on 2017/9/23.
 */

public class SearchKeyword {
        private List<KeywordBean> keyword;

        public List<KeywordBean> getKeyword() {
            return keyword;
        }

        public void setKeyword(List<KeywordBean> keyword) {
            this.keyword = keyword;
        }

        public static class KeywordBean {
            /**
             * items : ["Supreme","绝对领域","清澈","熊本熊","樱花","风筝","洛天依","多肉","水果","杨幂"]
             * atime : 1402991036
             * _id : 539ff1bc69401b0b5eda736f
             */

            private int atime;
            private String _id;
            private List<String> items;

            public int getAtime() {
                return atime;
            }

            public void setAtime(int atime) {
                this.atime = atime;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public List<String> getItems() {
                return items;
            }

            public void setItems(List<String> items) {
                this.items = items;
            }
        }
}
