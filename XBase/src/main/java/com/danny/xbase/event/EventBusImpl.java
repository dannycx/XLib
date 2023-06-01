/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xbase.event;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.danny.xbase.module.IEventBus;

/**
 * Event实现类
 *
 * @author x
 * @since 2023-06-01
 */
public class EventBusImpl implements IEventBus {
    public EventBusImpl(Context context) {}

    @Override
    public <T> LiveData<Event<T>> on(Class<? extends Event<T>> eventType) {
        String key = eventType.getName();
        return getLiveData(key);
    }

    private <T> XLiveData<IEventBus.Event<T>> getLiveData(String key) {
        XLiveData<IEventBus.Event<T>> data = EventContainer.getInstance().getEvent(key);
        if (data == null) {
            data = new XLiveData<>();
            EventContainer.getInstance().registerEvent(key, data);
        }
        return data;
    }

    @Override
    public <T> void post(Event<T> event) {
        String key = event.getClass().getName();
        XLiveData data = getLiveData(key);
        XLiveDataUtil.INSTANCE.setValue(data, event);
    }
}
