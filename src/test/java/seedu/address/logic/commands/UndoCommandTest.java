package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class UndoCommandTest {

    @Test
    public void execute_noHistory_throwsCommandException() {
        Model model = new ModelManager();
        CommandHistory history = new CommandHistory();
        UndoCommand undoCommand = new UndoCommand(history);

        assertThrows(CommandException.class, UndoCommand.MESSAGE_NOTHING_TO_UNDO, () -> undoCommand.execute(model));
    }

    @Test
    public void execute_withHistory_undoesLastCommand() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        deleteCommand.execute(model);

        CommandHistory history = new CommandHistory();
        if (deleteCommand.isUndoable()) {
            history.push(deleteCommand);
        }

        UndoCommand undoCommand = new UndoCommand(history);
        CommandResult commandResult = undoCommand.execute(model);

        assertEquals(UndoCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
        assertTrue(history.isEmpty());
        assertEquals(expectedModel, model);
    }
}
