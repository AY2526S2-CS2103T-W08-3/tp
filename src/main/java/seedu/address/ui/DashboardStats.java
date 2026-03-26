package seedu.address.ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;

/**
 * Provides an overview of memberships, e.g. total members, total membership types, expiring memberships
 */
public class DashboardStats {
    public static int getTotal(ObservableList<Person> list) {
        return list.size();
    }
    public static int getAnnual(ObservableList<Person> list) {
        return (int) list.stream().filter(
                p -> p.getMembershipType().value.equalsIgnoreCase("annual")).count();
    }
    public static int getMonthly(ObservableList<Person> list) {
        return (int) list.stream().filter(
                p -> p.getMembershipType().value.equalsIgnoreCase("monthly")).count();
    }
    public static int getExpiring(ObservableList<Person> list) {
        return (int) list.stream().filter(
                p -> (p.getExpiryDate().getExpiryDate().isBefore(LocalDate.now().plusWeeks(1)))
                        && (p.getExpiryDate().getExpiryDate().isAfter(LocalDate.now()))
        ).count();
    }
    public static int getNewMembers(ObservableList<Person> list) {
        LocalDate prevSun = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate nextMon = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        return (int) list.stream().filter(
                p -> (p.getJoinDate().getDate().isAfter(prevSun))
                        && (p.getJoinDate().getDate().isBefore(nextMon))
        ).count();
    }
}
