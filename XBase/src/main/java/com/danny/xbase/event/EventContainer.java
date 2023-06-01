/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xbase.event;

import android.util.ArrayMap;

import com.danny.xbase.module.IEventBus;

public class EventContainer<T> {
    private static volatile EventContainer container = new EventContainer();
    private ArrayMap<String, XLiveData<IEventBus.Event<T>>> arrayMap;

    private EventContainer() {
        arrayMap = new ArrayMap<>();
    }

    public static EventContainer getInstance() {
        return container;
    }

    public XLiveData<IEventBus.Event<T>> getEvent(String key) {
        return arrayMap.get(key);
    }

    public void registerEvent(String key, XLiveData<IEventBus.Event<T>> data) {
        arrayMap.put(key, data);
    }
}
