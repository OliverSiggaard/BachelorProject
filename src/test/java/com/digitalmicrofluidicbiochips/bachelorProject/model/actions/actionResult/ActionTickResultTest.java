package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class ActionTickResultTest {

    @Test
    void testEmptyConstructorInitialization() {
        ActionTickResult sut = new ActionTickResult();
        Assertions.assertEquals(0, sut.getTickCommands().size());
    }

    @Test
    void testStringConstructorInitialization() {
        IDmfCommand iDmfCommand = mock(IDmfCommand.class);
        ActionTickResult sut = new ActionTickResult(iDmfCommand);

        Assertions.assertEquals(1, sut.getTickCommands().size());
        Assertions.assertEquals(iDmfCommand, sut.getTickCommands().get(0));
    }

    @Test
    void testStringListConstructorInitialization() {
        IDmfCommand iDmfCommand1 = mock(IDmfCommand.class);
        IDmfCommand iDmfCommand2 = mock(IDmfCommand.class);
        List<IDmfCommand> iDmfCommands = Arrays.asList(iDmfCommand1, iDmfCommand2);
        ActionTickResult sut = new ActionTickResult(iDmfCommands);

        Assertions.assertEquals(2, sut.getTickCommands().size());
        Assertions.assertEquals(iDmfCommands.get(0), sut.getTickCommands().get(0));
        Assertions.assertEquals(iDmfCommands.get(1), sut.getTickCommands().get(1));
    }

    @Test
    void actionTickResultAddSingleString() {
        IDmfCommand iDmfCommand = mock(IDmfCommand.class);
        ActionTickResult sut = new ActionTickResult();
        Assertions.assertEquals(0, sut.getTickCommands().size());

        sut.addCommand(iDmfCommand);
        Assertions.assertEquals(1, sut.getTickCommands().size());
        Assertions.assertEquals(iDmfCommand, sut.getTickCommands().get(0));
    }


    @Test
    void actionTickResultAddStringListString() {
        IDmfCommand iDmfCommand1 = mock(IDmfCommand.class);
        IDmfCommand iDmfCommand2 = mock(IDmfCommand.class);
        IDmfCommand iDmfCommand3 = mock(IDmfCommand.class);
        List<IDmfCommand> iDmfCommands = Arrays.asList(iDmfCommand2, iDmfCommand3);
        ActionTickResult actionTickResult = new ActionTickResult(iDmfCommands);

        ActionTickResult sut = new ActionTickResult(iDmfCommand1);
        Assertions.assertEquals(1, sut.getTickCommands().size());

        sut.addTickResult(actionTickResult);
        Assertions.assertEquals(3, sut.getTickCommands().size());
        Assertions.assertEquals(iDmfCommand1, sut.getTickCommands().get(0));
        Assertions.assertEquals(iDmfCommand2, sut.getTickCommands().get(1));
        Assertions.assertEquals(iDmfCommand3, sut.getTickCommands().get(2));
    }

    @Test
    void actionTickResultUpdateModelWithCommands() {
        IDmfCommand iDmfCommand1 = mock(IDmfCommand.class);
        IDmfCommand iDmfCommand2 = mock(IDmfCommand.class);
        IDmfCommand iDmfCommand3 = mock(IDmfCommand.class);
        List<IDmfCommand> iDmfCommands = Arrays.asList(iDmfCommand1, iDmfCommand2, iDmfCommand3);
        ActionTickResult sut = new ActionTickResult(iDmfCommands);

        // Call the method to be tested
        sut.updateModelWithCommands();

        // Verify that the updateModelWithCommand method is called for each mocked IDmfCommand instance
        verify(iDmfCommand1).updateModelWithCommand();
        verify(iDmfCommand2).updateModelWithCommand();
        verify(iDmfCommand3).updateModelWithCommand();
    }

    @Test
    void testGetTickCommandsAsString() {
        IDmfCommand iDmfCommand1 = mock(IDmfCommand.class);
        when(iDmfCommand1.getDmfCommand()).thenReturn("iDmfCommand1");
        IDmfCommand iDmfCommand2 = mock(IDmfCommand.class);
        when(iDmfCommand2.getDmfCommand()).thenReturn("iDmfCommand2");
        IDmfCommand iDmfCommand3 = mock(IDmfCommand.class);
        when(iDmfCommand3.getDmfCommand()).thenReturn("iDmfCommand3");
        List<IDmfCommand> iDmfCommands = Arrays.asList(iDmfCommand1, iDmfCommand2, iDmfCommand3);
        ActionTickResult sut = new ActionTickResult(iDmfCommands);

        Assertions.assertEquals(3, sut.getTickCommandsAsStrings().size());
        Assertions.assertTrue(sut.getTickCommandsAsStrings().contains("iDmfCommand1"));
        Assertions.assertTrue(sut.getTickCommandsAsStrings().contains("iDmfCommand2"));
        Assertions.assertTrue(sut.getTickCommandsAsStrings().contains("iDmfCommand3"));
    }

}