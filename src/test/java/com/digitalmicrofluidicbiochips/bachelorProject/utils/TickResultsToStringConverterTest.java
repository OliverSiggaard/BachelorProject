package com.digitalmicrofluidicbiochips.bachelorProject.utils;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.testUtils.DmfCommand;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TickResultsToStringConverterTest {
    @Test
    void testTickResultsAreConvertedToStringCorrectly() {
        // Sample tick results:
        List<ActionTickResult> tickResultList = new ArrayList<>();

        ActionTickResult tickResult1 = new ActionTickResult();
        tickResult1.addCommand(new DmfCommand("COMMAND 1;"));
        tickResult1.addCommand(new DmfCommand("COMMAND 2;"));

        ActionTickResult tickResult2 = new ActionTickResult();
        tickResult2.addCommand(new DmfCommand("COMMAND 3;"));

        tickResultList.add(tickResult1);
        tickResultList.add(tickResult2);

        String expectedString = "COMMAND 1;" + System.lineSeparator() +
                                "COMMAND 2;" + System.lineSeparator() +
                                "TICK;" + System.lineSeparator() +
                                "COMMAND 3;" + System.lineSeparator() +
                                "TICK;" + System.lineSeparator() +
                                "TSTOP;" + System.lineSeparator() +
                                "TICK;" + System.lineSeparator() +
                                "TICK;";


        String resultString = TickResultsToStringConverter.convertTickResultsToString(tickResultList);

        assertEquals(expectedString, resultString);
    }
}
