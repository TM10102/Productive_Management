import javax.swing.*;
import java.awt.*;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class CalendarPanel extends JPanel {
    private int currentMonth;
    private int currentYear;
    private JLabel titleLabel;
    private TodoListPanel todoListPanel;
    private List<String> eventNames;
    private List<String> eventDates;


    // Constructor
    public CalendarPanel(TodoListPanel todoListPanel) {
        setLayout(new BorderLayout());
        this.todoListPanel = todoListPanel;


        // Initialize with the current month and year
        Calendar cal = Calendar.getInstance();
        currentMonth = cal.get(Calendar.MONTH);
        currentYear = cal.get(Calendar.YEAR);


        titleLabel = new JLabel("", SwingConstants.CENTER);
        updateTitleLabel(); // Update title label with current month and year
        add(titleLabel, BorderLayout.NORTH);


        updateCalendar(); // Initialize and update the calendar grid
    }


    // Method to show the previous month
    private void showPreviousMonth() {
        if (currentMonth == 0) {
            currentMonth = 11;
            currentYear--;
        } else {
            currentMonth--;
        }
        updateCalendar(); // Update the calendar grid for the new month
    }


    // Method to show the next month
    private void showNextMonth() {
        if (currentMonth == 11) {
            currentMonth = 0;
            currentYear++;
        } else {
            currentMonth++;
        }
        updateCalendar(); // Update the calendar grid for the new month
    }


    // Method to update the entire calendar grid
    public void updateCalendar() {
        removeAll(); // Clear the current components in the panel


        // Set lists from events from To-Do List Panel
        eventNames = new ArrayList<>(todoListPanel.getEventNames());
        eventDates = new ArrayList<>(todoListPanel.getEventDates());


        // Update the title label
        updateTitleLabel();


        JPanel calendarGrid = new JPanel(new GridLayout(0, 7));
        calendarGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        // Add day names
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String dayName : dayNames) {
            calendarGrid.add(new JLabel(dayName, SwingConstants.CENTER));
        }


        // Add empty labels for days before the first day of the month
        Calendar cal = new GregorianCalendar(currentYear, currentMonth, 1);
        for (int i = 1; i < cal.get(Calendar.DAY_OF_WEEK); i++) {
            calendarGrid.add(new JLabel(""));
        }


        // Add day numbers and event areas
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= daysInMonth; day++) {
            JTextArea numberTextArea = new JTextArea(String.valueOf(day));
            JTextArea eventTextArea = new JTextArea();


            // Set background color based on whether the day is even or odd
            if (day % 2 == 0) {
                numberTextArea.setBackground(Color.WHITE); // Even day
                eventTextArea.setBackground(Color.WHITE); // Even day event area
            } else {
                numberTextArea.setBackground(Color.LIGHT_GRAY); // Odd day
                eventTextArea.setBackground(Color.LIGHT_GRAY); // Odd day event area
            }


            // Set text alignment
            numberTextArea.setAlignmentX(CENTER_ALIGNMENT);
            numberTextArea.setAlignmentY(TOP_ALIGNMENT);
            eventTextArea.setAlignmentX(CENTER_ALIGNMENT);
            eventTextArea.setAlignmentY(TOP_ALIGNMENT);


            // Set layout for each day
            JPanel dayPanel = new JPanel();
            dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
            dayPanel.add(numberTextArea);
            dayPanel.add(eventTextArea);


            // Find events for the current day
            String currentDate = String.format("%d-%s-%d", currentYear, getMonthName(currentMonth), day);
            int eventIndex = eventDates.indexOf(currentDate);


            // Display events in the eventTextArea
            if (eventIndex != -1) {
                eventTextArea.append(eventNames.get(eventIndex) + "\n");
            }


            // Add the day panel to the calendar grid
            calendarGrid.add(dayPanel);
        }


        add(calendarGrid, BorderLayout.CENTER);


        // Add navigation buttons for changing months
        JPanel monthNavigationPanel = new JPanel();
        JButton prevMonthButton = new JButton("Previous Month");
        prevMonthButton.addActionListener(e -> showPreviousMonth());


        JButton nextMonthButton = new JButton("Next Month");
        nextMonthButton.addActionListener(e -> showNextMonth());


        monthNavigationPanel.add(prevMonthButton);
        monthNavigationPanel.add(nextMonthButton);


        add(monthNavigationPanel, BorderLayout.SOUTH);


        revalidate(); // Refresh the layout
        repaint(); // Repaint the panel
    }


    // Helper method to get the month name
    private String getMonthName(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }


    // Helper method to update the title label with the current month and year
    private void updateTitleLabel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        add(titleLabel, BorderLayout.NORTH);
        titleLabel.setText(dateFormat.format(new GregorianCalendar(currentYear, currentMonth, 1).getTime()));
    }
}
