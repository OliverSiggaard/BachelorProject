package com.digitalmicrofluidicbiochips.bachelorProject.compiler.factory;

import com.digitalmicrofluidicbiochips.bachelorProject.compiler.mappers.IActionToTaskMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.compiler.mappers.InputToTaskMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.compiler.mappers.MixToTaskMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.compiler.mappers.MoveToTaskMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.InputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.MixAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.MoveAction;

import java.util.HashMap;
import java.util.Map;

public class ActionToTaskMapperFactory {
    private static final Map<Class<? extends ActionBase>, IActionToTaskMapper<?, ?>> mappers = new HashMap<>();

    static {
        mappers.put(InputAction.class, new InputToTaskMapper());
        mappers.put(MoveAction.class, new MoveToTaskMapper());
        mappers.put(MixAction.class, new MixToTaskMapper());
    }

    @SuppressWarnings("unchecked")
    public static <T extends ActionBase> IActionToTaskMapper<T, ?> getMapper(Class<T> actionClass) {
        return (IActionToTaskMapper<T, ?>) mappers.get(actionClass);
    }
}
