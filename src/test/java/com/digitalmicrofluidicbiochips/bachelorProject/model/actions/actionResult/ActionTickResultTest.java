package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class ActionTickResultTest {

    @Test
    void testEmptyConstructorInitialization() {
        ActionTickResult sut = new ActionTickResult();
        Assertions.assertEquals(false, sut.somethingHappenedInTick());
        Assertions.assertEquals(0, sut.getTickCommands().size());
    }

    @Test
    void testStringConstructorInitialization() {
        IDmfCommand iDmfCommand = mock(IDmfCommand.class);
        ActionTickResult sut = new ActionTickResult(iDmfCommand);
        Assertions.assertEquals(true, sut.somethingHappenedInTick());

        Assertions.assertEquals(1, sut.getTickCommands().size());
        Assertions.assertEquals(iDmfCommand, sut.getTickCommands().get(0));
    }

    @Test
    void testStringListConstructorInitialization() {
        IDmfCommand iDmfCommand1 = mock(IDmfCommand.class);
        IDmfCommand iDmfCommand2 = mock(IDmfCommand.class);
        List<IDmfCommand> iDmfCommands = Arrays.asList(iDmfCommand1, iDmfCommand2);
        ActionTickResult sut = new ActionTickResult(iDmfCommands);
        Assertions.assertEquals(true, sut.somethingHappenedInTick());

        Assertions.assertEquals(2, sut.getTickCommands().size());
        Assertions.assertEquals(iDmfCommands.get(0), sut.getTickCommands().get(0));
        Assertions.assertEquals(iDmfCommands.get(1), sut.getTickCommands().get(1));
    }

    @Test
    void actionTickResultAddSingleString() {
        IDmfCommand iDmfCommand = mock(IDmfCommand.class);
        ActionTickResult sut = new ActionTickResult();
        Assertions.assertEquals(0, sut.getTickCommands().size());
        Assertions.assertEquals(false, sut.somethingHappenedInTick());

        sut.addCommand(iDmfCommand);
        Assertions.assertEquals(true, sut.somethingHappenedInTick());
        Assertions.assertEquals(1, sut.getTickCommands().size());
        Assertions.assertEquals(iDmfCommand, sut.getTickCommands().get(0));
    }


    @Test
    void actionTickResultAddTickResult() {
        IDmfCommand iDmfCommand1 = mock(IDmfCommand.class);
        IDmfCommand iDmfCommand2 = mock(IDmfCommand.class);
        IDmfCommand iDmfCommand3 = mock(IDmfCommand.class);
        List<IDmfCommand> iDmfCommands = Arrays.asList(iDmfCommand2, iDmfCommand3);
        ActionTickResult actionTickResult = new ActionTickResult(iDmfCommands);

        ActionTickResult sut = new ActionTickResult(iDmfCommand1);
        Assertions.assertEquals(1, sut.getTickCommands().size());
        Assertions.assertEquals(true, sut.somethingHappenedInTick());

        sut.addTickResult(actionTickResult);
        Assertions.assertEquals(true, sut.somethingHappenedInTick());
        Assertions.assertEquals(3, sut.getTickCommands().size());
        Assertions.assertEquals(iDmfCommand1, sut.getTickCommands().get(0));
        Assertions.assertEquals(iDmfCommand2, sut.getTickCommands().get(1));
        Assertions.assertEquals(iDmfCommand3, sut.getTickCommands().get(2));
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

    @Test
    void testAddCommandsEmpty() {
        ActionTickResult sut = new ActionTickResult();
        Assertions.assertEquals(false, sut.somethingHappenedInTick());

        sut.addCommands(Arrays.asList());
        Assertions.assertEquals(false, sut.somethingHappenedInTick());
    }

    @Test
    void testAddCommands() {
        IDmfCommand iDmfCommand1 = mock(IDmfCommand.class);
        when(iDmfCommand1.getDmfCommand()).thenReturn("iDmfCommand1");
        IDmfCommand iDmfCommand2 = mock(IDmfCommand.class);
        when(iDmfCommand2.getDmfCommand()).thenReturn("iDmfCommand2");

        List<IDmfCommand> commands = Arrays.asList(iDmfCommand1, iDmfCommand2);

        ActionTickResult sut = new ActionTickResult();
        Assertions.assertEquals(false, sut.somethingHappenedInTick());

        sut.addCommands(commands);
        Assertions.assertEquals(true, sut.somethingHappenedInTick());
        Assertions.assertEquals(2, sut.getTickCommandsAsStrings().size());
        Assertions.assertTrue(sut.getTickCommandsAsStrings().contains("iDmfCommand1"));
        Assertions.assertTrue(sut.getTickCommandsAsStrings().contains("iDmfCommand2"));
    }

    @Test
    void testAddCommandNull() {
        ActionTickResult sut = new ActionTickResult();
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.addCommand(null));
    }

    @Test
    void testAddCommandsContainsNull() {
        IDmfCommand iDmfCommand1 = mock(IDmfCommand.class);
        when(iDmfCommand1.getDmfCommand()).thenReturn("iDmfCommand1");
        IDmfCommand iDmfCommand2 = mock(IDmfCommand.class);
        when(iDmfCommand2.getDmfCommand()).thenReturn("iDmfCommand2");

        List<IDmfCommand> commands = Arrays.asList(iDmfCommand1, iDmfCommand2, null);

        ActionTickResult sut = new ActionTickResult();
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.addCommands(commands));
    }

    @Test
    void testAddCommandsListIsNull() {
        List<IDmfCommand> commands = null;

        ActionTickResult sut = new ActionTickResult();
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.addCommands(commands));
    }

    @Test
    void testAddNullActionTicketResult() {
        ActionTickResult sut = new ActionTickResult();
        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.addTickResult(null));
    }

    @Test
    void testConstructorNullCommand() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ActionTickResult((IDmfCommand) null));
    }

    @Test
    void testConstructorNullCommandList() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ActionTickResult((List<IDmfCommand>) null));
    }

    @Test
    void testConstructorListContainsNull() {
        IDmfCommand iDmfCommand1 = mock(IDmfCommand.class);
        when(iDmfCommand1.getDmfCommand()).thenReturn("iDmfCommand1");
        IDmfCommand iDmfCommand2 = mock(IDmfCommand.class);
        when(iDmfCommand2.getDmfCommand()).thenReturn("iDmfCommand2");

        List<IDmfCommand> commands = Arrays.asList(iDmfCommand1, iDmfCommand2, null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ActionTickResult(commands));

    }

}
