package com.danny.demo.home.baby

import com.danny.xui.check.bean.CheckBean
import com.danny.xui.table.bean.TableBean
import java.util.ArrayList

object XDaughterTools {

    fun cake(): ArrayList<CheckBean> {
        val list = ArrayList<CheckBean> ()
        val x1 = CheckBean("巧克力", "", false)
        val x2 = CheckBean("水果", "", false)
        val x3 = CheckBean("夹心", "", false)
        list.add(x1)
        list.add(x2)
        list.add(x3)
        return list
    }

    fun snacks(): ArrayList<CheckBean> {
        val list = ArrayList<CheckBean> ()
        val x1 = CheckBean("巧克力", "", false)
        val x2 = CheckBean("水果", "", false)
        val x3 = CheckBean("夹心", "", false)
        list.add(x1)
        list.add(x2)
        list.add(x3)
        return list
    }

    fun porridge(): ArrayList<CheckBean> {
        val list = ArrayList<CheckBean> ()
        val x1 = CheckBean("牛肉蛋花", "", false)
        val x2 = CheckBean("八宝粥", "", false)
        val x3 = CheckBean("南瓜粥", "", false)
        val x4 = CheckBean("大米粥", "", false)
        val x5 = CheckBean("小米粥", "", false)
        val x6 = CheckBean("豆浆", "", false)
        list.add(x1)
        list.add(x2)
        list.add(x3)
        list.add(x4)
        list.add(x5)
        list.add(x6)
        return list
    }

    fun fruit(): ArrayList<CheckBean> {
        val list = ArrayList<CheckBean> ()
        val x1 = CheckBean("草莓", "", false)
        val x2 = CheckBean("樱桃", "", false)
        val x3 = CheckBean("火龙果", "", false)
        val x4 = CheckBean("橙子", "", false)
        val x5 = CheckBean("葡萄", "", false)
        val x6 = CheckBean("荔枝", "", false)
        val x7 = CheckBean("山竹", "", false)
        val x8 = CheckBean("牛奶草莓", "", false)
        val x9 = CheckBean("哈密瓜", "", false)
        val x10 = CheckBean("椰子", "", false)
        list.add(x1)
        list.add(x2)
        list.add(x3)
        list.add(x4)
        list.add(x5)
        list.add(x6)
        list.add(x7)
        list.add(x8)
        list.add(x9)
        list.add(x10)
        return list
    }

    fun food(): ArrayList<TableBean> {
        val item = ArrayList<TableBean>()
        val a = intArrayOf(3, 6, 12, 16, 20)
        val bean1 = TableBean("#", "酸汤肥牛")
        val bean2 = TableBean("#", "水煮肉")
        val bean3 = TableBean("#", "酸辣里脊", "x_shape_table_bg")
        val bean4 = TableBean("#", "酸菜鱼")
        val bean5 = TableBean("#", "锅包肉")
        val bean6 = TableBean("#", "地三鲜")
        val bean7 = TableBean("#", "黄焖排骨")
        val bean8 = TableBean("#", "干锅土豆")
        val bean9 = TableBean("#", "大盘鸡")
        val bean10 = TableBean("#", "土豆牛腩")

        val bean11 = TableBean("#", "麻辣香锅")
        val bean12 = TableBean("#", "麻辣烫")
        val bean13 = TableBean("#", "红烧鱼")
        val bean14 = TableBean("#", "红烧茄子")
        val bean15 = TableBean("#", "糖醋小排", "x_shape_table_bg")
        val bean16 = TableBean("#", "高压土豆")

        val bean17 = TableBean("#", "番茄牛腩")
        val bean18 = TableBean("#", "辣白菜")
        val bean19 = TableBean("#", "番茄汤")
        val bean20 = TableBean("#", "火锅", "x_shape_table_bg")
        val bean21 = TableBean("#", "小龙虾", "x_shape_table_bg")
        val bean22 = TableBean("#", "醋溜白菜")
        val bean23 = TableBean("#", "羊肉泡馍")
        val bean24 = TableBean("#", "烧烤")
        val bean25 = TableBean("#", "豆腐脑", "x_shape_table_bg")
        val bean26 = TableBean("#", "可乐鸡翅", "x_shape_table_bg")
        val bean27 = TableBean("#", "烤串")
        val bean28 = TableBean("#", "暖锅")
        val bean29 = TableBean("#", "黄焖鸡")
        val bean30 = TableBean("#", "黄焖羊肉", "x_shape_table_bg")


        item.add(bean1)
        item.add(bean2)
        item.add(bean3)
        item.add(bean4)
        item.add(bean5)
        item.add(bean6)
        item.add(bean7)
        item.add(bean8)
        item.add(bean9)
        item.add(bean10)
        item.add(bean11)
        item.add(bean12)
        item.add(bean13)
        item.add(bean14)
        item.add(bean15)
        item.add(bean16)
        item.add(bean17)
        item.add(bean18)
        item.add(bean19)
        item.add(bean20)
        item.add(bean21)
        item.add(bean22)
        item.add(bean23)
        item.add(bean24)
        item.add(bean25)
        item.add(bean26)
        item.add(bean27)
        item.add(bean28)
        item.add(bean29)
//        item.add(bean30)
//        item.add(bean31)
        for (i in (0 until 23)) {

        }
        return item
    }

}