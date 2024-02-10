package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.IActionMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.actions.*;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.generic.DtoActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.actions.*;

import java.util.HashMap;
import java.util.Map;

public class JsonActionMapperFactory {
    private static final Map<Class<? extends DtoActionBase>, IActionMapper<?, ?>> mappers = new HashMap<>();

    static {
        mappers.put(JsonStartAction.class, new JsonStartActionMapper());
        mappers.put(JsonInputAction.class, new JsonInputActionMapper());
        mappers.put(JsonOutputAction.class, new JsonOutputActionMapper());
        mappers.put(JsonMoveAction.class, new JsonMoveActionMapper());
        mappers.put(JsonMergeAction.class, new JsonMergeActionMapper());
        mappers.put(JsonSplitAction.class, new JsonSplitActionMapper());
        mappers.put(JsonMixAction.class, new JsonMixActionMapper());
        mappers.put(JsonStoreAction.class, new JsonStoreActionMapper());
        mappers.put(JsonIfAction.class, new JsonIfActionMapper());
        mappers.put(JsonRepeatAction.class, new JsonRepeatActionMapper());
        mappers.put(JsonEndAction.class, new JsonEndActionMapper());
    }

    @SuppressWarnings("unchecked")
    public static <T extends DtoActionBase> IActionMapper<T, ?> getMapper(Class<T> dtoClass) {
        return (IActionMapper<T, ?>) mappers.get(dtoClass);
    }
}
