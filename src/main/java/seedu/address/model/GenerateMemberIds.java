package seedu.address.model;

import seedu.address.model.person.MemberId;

/**
 * Generates a unique membership ID for a new gym member
 */
public class GenerateMemberIds {
    private static int currentId = 0;

    public static void initialize(int maxId) {
        currentId = maxId;
    }
    /**
     * Ensures no gym members share the same ID
     * @return an id representing the next unique available membership
     */
    public static MemberId generateNextId() {
        currentId++;
        return new MemberId(currentId);
    }
}
