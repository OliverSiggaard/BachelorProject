package com.digitalmicrofluidicbiochips.bachelorProject.reader.mappers.generic.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.PlatformInformation;

public interface IPlatformStateMapper<T> {
    public PlatformInformation mapToInternal(T external);

}
