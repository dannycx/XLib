package com.danny.xui.trend;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class FunnelData {
    public String title;
    public List<Funnel> funnels;

    private static String[] labels = {"23", "74", "65", "98", "78"};
    private static int[] colors = {Color.parseColor("#C1CA62")
            , Color.parseColor("#7CCA62"), Color.parseColor("#4523aa")
            , Color.parseColor("#0BD90D"), Color.parseColor("#009DD9")};
    public static class Funnel {
        public String label;
        public int color;
        public String num;

    }

    public static FunnelData getData() {
        FunnelData data = new FunnelData();
        data.title = "中国";
        List<Funnel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Funnel funnel = new Funnel();
            funnel.label = labels[i];
            funnel.color = colors[i];
            funnel.num = String.valueOf(i);
            list.add(funnel);
        }
        data.funnels = list;
        return data;
    }
}
