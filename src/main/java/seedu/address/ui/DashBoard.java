package seedu.address.ui;

import java.time.LocalDate;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.logic.Logic;

/**
 * Dashboard UI for FitDesk
 * Gives a quick overview of the gym's statistics
 */
public class DashBoard extends UiPart<Region> {
    private static final String FXML = "Dashboard.fxml";

    private final Logic logic;
    @FXML
    private Label memberCount;
    @FXML
    private Label expiringMemberships;
    @FXML
    private Label annualMembers;
    @FXML
    private Label monthlyMembers;
    @FXML
    private Label newMembers;

    /**
     *
     */
    public DashBoard(Logic logic) {
        super(FXML);
        this.logic = logic;
        update();
    }

    /**
     * Updates the dashboard with member statistics when counts of relevant data has changed
     */
    public void update() {
        var list = logic.getAddressBook().getPersonList();
        memberCount.setText(String.valueOf(DashboardStats.getTotal(list)));
        annualMembers.setText(String.valueOf(DashboardStats.getAnnual(list)));
        monthlyMembers.setText(String.valueOf(DashboardStats.getMonthly(list)));
        expiringMemberships.setText(String.valueOf(DashboardStats.getExpiring(list)));
        newMembers.setText("+" + String.valueOf(DashboardStats.getNewMembers(list)));
    }
}
