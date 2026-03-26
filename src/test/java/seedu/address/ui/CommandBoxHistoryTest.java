package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.CommandBox.CommandExecutor;

public class CommandBoxHistoryTest {
    private static final long FX_TIMEOUT_SECONDS = 5;
    private static volatile boolean javaFxInitialized = false;

    @BeforeAll
    static void initJavaFx() {
        // JavaFX toolkit needs to be initialized once before creating Controls.
        try {
            if (Platform.isFxApplicationThread()) {
                javaFxInitialized = true;
                return;
            }

            // Configure headless mode only on Linux/CI environments.
            // (These properties can cause issues on Windows.)
            String osName = System.getProperty("os.name", "").toLowerCase();
            boolean isLinux = osName.contains("linux");
            if (isLinux) {
                System.setProperty("javafx.platform", "Monocle");
                System.setProperty("monocle.platform", "Headless");
                System.setProperty("prism.order", "sw");
                System.setProperty("glass.platform", "Monocle");
                System.setProperty("monocle.headless", "true");
            }

            Platform.startup(() -> {
                // no-op
            });
            javaFxInitialized = true;
        } catch (IllegalStateException e) {
            // toolkit is already initialized
            javaFxInitialized = true;
        } catch (UnsupportedOperationException | NullPointerException e) {
            javaFxInitialized = false;
        }
    }

    @Test
    void history_upAndDown_cyclesThroughPreviouslyEnteredCommands() throws Exception {
        Assumptions.assumeTrue(javaFxInitialized, "JavaFX toolkit could not be initialized in this environment.");
        runOnFxThread(() -> {
            CommandExecutor executor = new NoOpCommandExecutor();
            CommandBox commandBox = new CommandBox(executor);

            TextField commandTextField = getCommandTextField(commandBox);

            // Enter two commands to build history.
            commandTextField.setText("first");
            commandTextField.fireEvent(new ActionEvent());
            commandTextField.setText("second");
            commandTextField.fireEvent(new ActionEvent());

            // After Enter, the command box is cleared (history pointer at end).
            assertEquals("", commandTextField.getText());

            // UP -> previous command ("second")
            fireKeyPressed(commandTextField, KeyCode.UP);
            assertEquals("second", commandTextField.getText());

            // UP -> one more step back ("first")
            fireKeyPressed(commandTextField, KeyCode.UP);
            assertEquals("first", commandTextField.getText());

            // DOWN -> forward ("second")
            fireKeyPressed(commandTextField, KeyCode.DOWN);
            assertEquals("second", commandTextField.getText());

            // DOWN -> beyond newest -> clears
            fireKeyPressed(commandTextField, KeyCode.DOWN);
            assertEquals("", commandTextField.getText());
        });
    }

    private static TextField getCommandTextField(CommandBox commandBox) {
        try {
            Field field = CommandBox.class.getDeclaredField("commandTextField");
            field.setAccessible(true);
            return (TextField) field.get(commandBox);
        } catch (ReflectiveOperationException e) {
            fail("Unable to access commandTextField via reflection: " + e.getMessage());
            throw new AssertionError(e);
        }
    }

    private static void fireKeyPressed(TextField textField, KeyCode code) {
        @SuppressWarnings("unchecked")
        EventHandler<KeyEvent> handler = (EventHandler<KeyEvent>) textField.getOnKeyPressed();
        if (handler == null) {
            fail("Expected a key pressed handler to be registered.");
            return;
        }
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code, false, false, false, false);
        handler.handle(keyEvent);
    }

    private static void runOnFxThread(Runnable runnable) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final Throwable[] thrown = new Throwable[1];

        Platform.runLater(() -> {
            try {
                runnable.run();
            } catch (Throwable t) {
                thrown[0] = t;
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(FX_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            fail("Timed out waiting for JavaFX thread.");
        }

        if (thrown[0] != null) {
            throw new AssertionError(thrown[0]);
        }
    }

    private static class NoOpCommandExecutor implements CommandExecutor {
        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            return new CommandResult(commandText);
        }
    }
}

