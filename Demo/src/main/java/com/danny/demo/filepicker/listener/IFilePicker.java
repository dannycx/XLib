package com.danny.demo.filepicker.listener;


import com.danny.demo.filepicker.FilePicker;
import com.danny.xbase.module.IModule;

public interface IFilePicker extends IModule {
    FilePicker.XFilePickerEngine prepare();
}
