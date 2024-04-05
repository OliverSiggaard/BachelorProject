package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ActionTickResultTest {

    @Test
    void testEmptyConstructorInitialization() {
        ActionTickResult sut = new ActionTickResult();
        Assertions.assertEquals(0, sut.getTickCommands().size());
    }

    @Test
    void testStringConstructorInitialization() {
        String stringResult = "test";
        ActionTickResult sut = new ActionTickResult("test");

        Assertions.assertEquals(1, sut.getTickCommands().size());
        Assertions.assertEquals(stringResult, sut.getTickCommands().get(0));
    }

    @Test
    void testStringListConstructorInitialization() {
        String stringResult1 = "test1";
        String stringResult2 = "test2";
        List<String> stringList = Arrays.asList(stringResult1, stringResult2);
        ActionTickResult sut = new ActionTickResult(stringList);

        Assertions.assertEquals(2, sut.getTickCommands().size());
        Assertions.assertEquals(stringList.get(0), sut.getTickCommands().get(0));
        Assertions.assertEquals(stringList.get(1), sut.getTickCommands().get(1));
    }

    @Test
    void actionTickResultAddSingleString() {
        String stringResult = "test";
        ActionTickResult sut = new ActionTickResult();
        Assertions.assertEquals(0, sut.getTickCommands().size());

        sut.addCommand(stringResult);
        Assertions.assertEquals(1, sut.getTickCommands().size());
        Assertions.assertEquals(stringResult, sut.getTickCommands().get(0));
    }


    @Test
    void actionTickResultAddStringListString() {
        String stringResult1 = "test1";
        String stringResult2 = "test2";
        String stringResult3 = "test3";
        List<String> stringList = Arrays.asList(stringResult2, stringResult3);
        ActionTickResult actionTickResult = new ActionTickResult(stringList);

        ActionTickResult sut = new ActionTickResult(stringResult1);
        Assertions.assertEquals(1, sut.getTickCommands().size());

        sut.addTickResult(actionTickResult);
        Assertions.assertEquals(3, sut.getTickCommands().size());
        Assertions.assertEquals(stringResult1, sut.getTickCommands().get(0));
        Assertions.assertEquals(stringResult2, sut.getTickCommands().get(1));
        Assertions.assertEquals(stringResult3, sut.getTickCommands().get(2));
    }
}
