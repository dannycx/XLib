package com.danny.xui.check.bean

import java.io.Serializable

data class CheckEntity(var item: ArrayList<ArrayList<CheckBean>>): Serializable

data class CheckBean(var value: String, var code: String, var isChecked: Boolean, var extra: String = ""): Serializable
