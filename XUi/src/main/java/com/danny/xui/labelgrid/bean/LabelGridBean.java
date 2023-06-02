package com.danny.xui.labelgrid.bean;

import java.util.List;

public class LabelGridBean {
    List<LabelGrid> data;

    public List<LabelGrid> getData() {
        return data;
    }

    public class LabelGrid {
        private String title;
        private String name;
        private String value;
        private boolean isSmpbolSpecial;

        public String getTitle() {
            return title;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public boolean isSmpbolSpecial() {
            return isSmpbolSpecial;
        }
    }
}
