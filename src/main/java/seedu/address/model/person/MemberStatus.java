package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's member status in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidMemberStatus(String)}
 */
public class MemberStatus {

    public static final String MESSAGE_CONSTRAINTS =
            "Member status should only be either 'valid' or 'invalid' (case-insensitive).";
    public static final String VALIDATION_REGEX = "(?i)^(valid|invalid)$";

    public final String memberStatus;

    /**
     * Constructs a {@code MemberStatus}.
     *
     * @param memberStatus A valid member status.
     */
    public MemberStatus(String memberStatus) {
        requireNonNull(memberStatus);
        checkArgument(isValidMemberStatus(memberStatus), MESSAGE_CONSTRAINTS);
        this.memberStatus = memberStatus;
    }

    /**
     * Returns true if a given string is a valid member id.
     */
    public static boolean isValidMemberStatus(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return this.memberStatus;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MemberStatus)) {
            return false;
        }

        MemberStatus otherPhone = (MemberStatus) other;
        return this.memberStatus.equals(otherPhone.memberStatus);
    }

    @Override
    public int hashCode() {
        return this.memberStatus.hashCode();
    }

}


