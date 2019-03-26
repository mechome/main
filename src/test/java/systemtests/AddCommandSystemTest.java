package systemtests;

import static seedu.address.logic.commands.CommandTestUtil.DETAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FOO;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TYPE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_BOB;
import static seedu.address.testutil.TypicalSources.AMY;

import org.junit.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.source.Source;
import seedu.address.testutil.SourceBuilder;
import seedu.address.testutil.SourceUtil;

public class AddCommandSystemTest extends SourceManagerSystemTest {

    @Test
    public void add() {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a source without tags to a non-empty source manager,
         * command with leading spaces and trailing spaces
         * -> added
         */
        /*
         * TODO: This test will fail, because ALICE isn't AMY. Ask Fabian about this.
         * Basically, you need to see how ALICE and AMY were originally implemented as Persons,
         * and replicate as Sources.
         */
        Source toAdd = AMY;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + TITLE_DESC_AMY + "  "
                + TYPE_DESC_AMY + "   " + DETAIL_DESC_AMY + "   " + TAG_DESC_FOO + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding Amy to the list -> Amy deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addSource(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a source with all fields same as another source in the source manager except title -> added */
        toAdd = new SourceBuilder(AMY).withTitle(VALID_TITLE_BOB).build();
        command = AddCommand.COMMAND_WORD + TITLE_DESC_BOB + TYPE_DESC_AMY + DETAIL_DESC_AMY
                + TAG_DESC_FOO;
        assertCommandSuccess(command, toAdd);

        /* Case: add a source with all fields same as another source in the source manager except type
         * -> added
         */
        //        toAdd = new SourceBuilder(AMY).withType(VALID_TYPE_BOB).build();
        //        command = SourceUtil.getAddCommand(toAdd);
        //        assertCommandSuccess(command, toAdd);
        //
        //        /* Case: add to empty source manager -> added */
        //        deleteAllSources();
        //        assertCommandSuccess(AMY);
        //
        //        /* Case: add a source with tags, command with parameters in random order -> added */
        //        toAdd = BENSON;
        //        command = AddCommand.COMMAND_WORD + TAG_DESC_FOO + DETAIL_DESC_BOB + TITLE_DESC_BOB
        //                + TAG_DESC_BAR + TYPE_DESC_BOB;
        //        assertCommandSuccess(command, toAdd);
        //
        //        /* Case: add a source, missing tags -> added */
        //        assertCommandSuccess(HOON);
        //
        //        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */
        //
        //        /* Case: filters the source list before adding -> added */
        //        showSourcesWithTitle(KEYWORD_MATCHING_MEIER);
        //        assertCommandSuccess(IDA);
        //
        //        /* ------------------------ Perform add operation while a source card is selected --------------------------- */
        //
        //        /* Case: selects first card in the source list, add a source -> added, card selection remains unchanged */
        //        selectSource(Index.fromOneBased(1));
        //        assertCommandSuccess(CARL);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: add a duplicate source -> rejected */
        //        command = SourceUtil.getAddCommand(HOON);
        //        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_SOURCE);

        // I
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Storage} and {@code SourceListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code SourceManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see SourceManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Source toAdd) {
        assertCommandSuccess(SourceUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Source)}. Executes {@code command}
     * instead.
     * @see AddCommandSystemTest#assertCommandSuccess(Source)
     */
    private void assertCommandSuccess(String command, Source toAdd) {
        Model expectedModel = getModel();
        expectedModel.addSource(toAdd);
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Source)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Storage} and {@code SourceListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see AddCommandSystemTest#assertCommandSuccess(String, Source)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Storage} and {@code SourceListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code SourceManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see SourceManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
