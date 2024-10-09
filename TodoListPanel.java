
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class TodoListPanel extends JPanel {
    private JTextArea nameTextArea;
    private JTextArea dateTextArea;
    private JTextArea timeTextArea;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;


    private List<String[][]> events; // List to store events
    private List<String> eventNamesCalendar;  // List for Calendar Panel (event names)
    private List<String> eventDatesCalendar;  // List for Calendar Panel (event dates)


    // Constructor
    public TodoListPanel() {
        setLayout(new BorderLayout());


        // Initialize the events list
        events = new ArrayList<>();
        eventNamesCalendar = new ArrayList<>();
        eventDatesCalendar = new ArrayList<>();


        // Add label for "Next Up Tasks"
        JLabel titleLabel = new JLabel("Next Up Tasks", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);


        // Add JTextAreas for "Name," "Date," and "Time" with labels
        nameTextArea = createVerticalTextArea("Name");
        dateTextArea = createVerticalTextArea("Date");
        timeTextArea = createVerticalTextArea("Time");


        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 5, 5)); // Use GridLayout with 1 row and 3 columns
        infoPanel.add(createCenteredPanel(nameTextArea, "Name"));
        infoPanel.add(createCenteredPanel(dateTextArea, "Date"));
        infoPanel.add(createCenteredPanel(timeTextArea, "Time"));
        add(infoPanel, BorderLayout.CENTER);


        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");


        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);


        add(buttonPanel, BorderLayout.EAST);


        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEvent(); // Call the addEvent method when the "Add" button is clicked
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEvent(); // Call the removeEvent method when the "Delete" button is clicked
            }
        });


        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editEvent(); // Call the editEvent method when the "Edit" button is clicked
            }
        });
    }


    // Method to create a vertical text area with a label
    JTextArea createVerticalTextArea(String labelText) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.WHITE); // Set background color to white


        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 12));


        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);


        return textArea;
    }


    // Method to create a centered panel with a JTextArea and a label
    JPanel createCenteredPanel(JTextArea textArea, String labelText) {
        JPanel centeredPanel = new JPanel(new BorderLayout());
        centeredPanel.add(textArea, BorderLayout.CENTER);


        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        centeredPanel.add(label, BorderLayout.NORTH);


        return centeredPanel;
    }


    void editEvent() {
        if (events.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No events added.", "Edit Event", JOptionPane.INFORMATION_MESSAGE);
            return;
        }


        // Show a dialog to select the event to edit
        String[] eventNames = events.stream().map(event -> event[0][0]).toArray(String[]::new);
        String selectedEvent = (String) JOptionPane.showInputDialog(this, "Select an event to edit:",
                "Edit Event", JOptionPane.QUESTION_MESSAGE, null, eventNames, eventNames[0]);


        if (selectedEvent != null) {
            // Find the selected event in the list
            String[][] selectedEventArray = events.stream()
                    .filter(event -> event[0][0].equals(selectedEvent))
                    .findFirst()
                    .orElse(null);


            // Show the input dialog to edit the event details
            String editedName = JOptionPane.showInputDialog(this, "Edit event name:", selectedEventArray[0][0]);


            if (editedName != null && !editedName.trim().isEmpty()) {
                JComboBox<String> yearComboBox = new JComboBox<>(getYearStrings());
                JComboBox<String> monthComboBox = new JComboBox<>(getMonthStrings());
                JComboBox<String> dayComboBox = new JComboBox<>(getDayStrings());


                Calendar calendar = Calendar.getInstance();


                // Set the date directly without using setTime
                calendar.set(Calendar.YEAR, Integer.parseInt(Objects.requireNonNull(yearComboBox.getSelectedItem()).toString()));
                calendar.set(Calendar.MONTH, Arrays.asList(getMonthStrings()).indexOf(monthComboBox.getSelectedItem()));
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(Objects.requireNonNull(dayComboBox.getSelectedItem()).toString()));


                SpinnerDateModel dateModel = new SpinnerDateModel();
                dateModel.setValue(calendar.getTime());
                JSpinner timeSpinner = new JSpinner(dateModel);
                JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
                timeSpinner.setEditor(timeEditor);


                JPanel datePanel = new JPanel();
                datePanel.setLayout(new GridLayout(2, 1));
                datePanel.add(yearComboBox);
                datePanel.add(monthComboBox);
                datePanel.add(dayComboBox);


                JPanel timePanel = new JPanel();
                timePanel.add(new JLabel("Select time: "));
                timePanel.add(timeSpinner);


                JPanel inputPanel = new JPanel();
                inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
                inputPanel.add(datePanel);
                inputPanel.add(timePanel);


                int result = JOptionPane.showConfirmDialog(
                        this,
                        inputPanel,
                        "Edit date and time",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );


                if (result == JOptionPane.OK_OPTION) {
                    // Get the edited date and time from the components
                    String editedDate = yearComboBox.getSelectedItem() + "-"
                            + monthComboBox.getSelectedItem() + "-"
                            + dayComboBox.getSelectedItem();
                    String editedTime = new SimpleDateFormat("HH:mm").format((Date) timeSpinner.getValue());


                    // Update the selected event with edited details
                    selectedEventArray[0][0] = editedName;
                    selectedEventArray[1][0] = editedDate;
                    selectedEventArray[2][0] = editedTime;


                    // Update the corresponding values in the eventNamesCalendar and eventDatesCalendar lists
                    int index = eventNamesCalendar.indexOf(selectedEvent);
                    if (index != -1) {
                        eventNamesCalendar.set(index, editedName);
                        eventDatesCalendar.set(index, editedDate);
                    }


                    // Sort the events based on date and time
                    events.sort(Comparator.comparing(arr -> arr[1][0] + " " + arr[2][0]));


                    // Update JTextAreas with only the relevant information
                    updateTextAreas();
                }
            }
        }
    }


    // Method to add events to the events list and update JTextAreas
    void addEvent() {
        String eventName = JOptionPane.showInputDialog(this, "Enter event name:");
        if (eventName != null && !eventName.trim().isEmpty()) {
            JComboBox<String> yearComboBox = new JComboBox<>(getYearStrings());
            JComboBox<String> monthComboBox = new JComboBox<>(getMonthStrings());
            JComboBox<String> dayComboBox = new JComboBox<>(getDayStrings());


            SpinnerDateModel dateModel = new SpinnerDateModel();
            JSpinner timeSpinner = new JSpinner(dateModel);
            JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
            timeSpinner.setEditor(timeEditor);


            JPanel datePanel = new JPanel();
            datePanel.setLayout(new GridLayout(2, 1));
            datePanel.add(yearComboBox);
            datePanel.add(monthComboBox);
            datePanel.add(dayComboBox);


            JPanel timePanel = new JPanel();
            timePanel.add(new JLabel("Select time: "));
            timePanel.add(timeSpinner);


            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
            inputPanel.add(datePanel);
            inputPanel.add(timePanel);


            int result = JOptionPane.showConfirmDialog(
                    this,
                    inputPanel,
                    "Select date and time",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );


            if (result == JOptionPane.OK_OPTION) {
                String selectedDate = yearComboBox.getSelectedItem() + "-"
                        + monthComboBox.getSelectedItem() + "-"
                        + dayComboBox.getSelectedItem();
                String selectedTime = new SimpleDateFormat("HH:mm").format((Date) timeSpinner.getValue());


                // Create a new event and add it to the list
                String[][] newEvent = new String[3][1];
                newEvent[0][0] = eventName;
                newEvent[1][0] = selectedDate;
                newEvent[2][0] = selectedTime;
                events.add(newEvent);


                // Sort the events based on date and time
                events.sort(Comparator.comparing(arr -> arr[1][0] + " " + arr[2][0]));


                eventNamesCalendar.add(eventName);
                eventDatesCalendar.add(selectedDate);


                events.sort(Comparator.comparing(arr -> arr[1][0] + " " + arr[2][0]));
                // Update JTextAreas with only the relevant information
                updateTextAreas();
            }
        }
    }


    void removeEvent() {
        if (events.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No events added.", "Remove Event", JOptionPane.INFORMATION_MESSAGE);
            return;
        }


        // Show a dialog to select the event to remove
        String[] eventNamesArray = eventNamesCalendar.toArray(new String[0]);
        String selectedEvent = (String) JOptionPane.showInputDialog(this, "Select an event to remove:",
                "Remove Event", JOptionPane.QUESTION_MESSAGE, null, eventNamesArray, eventNamesArray[0]);


        if (selectedEvent != null) {
            // Find the selected event in the list
            String[][] selectedEventArray = events.stream()
                    .filter(event -> event[0][0].equals(selectedEvent))
                    .findFirst()
                    .orElse(null);


            if (selectedEventArray != null) {
                // Remove the selected event from the list
                events.remove(selectedEventArray);


                // Remove the selected event from the lists
                eventNamesCalendar.remove(selectedEvent);
                eventDatesCalendar.remove(selectedEventArray[1][0]);
                events.sort(Comparator.comparing(arr -> arr[1][0] + " " + arr[2][0]));
                updateTextAreas();
                JOptionPane.showMessageDialog(this, "Event removed successfully.", "Remove Event", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    // Method to update JTextAreas with only the relevant information
    void updateTextAreas() {
        nameTextArea.setText("");
        dateTextArea.setText("");
        timeTextArea.setText("");


        for (String[][] event : events) {
            nameTextArea.append(event[0][0] + "\n");
            dateTextArea.append(event[1][0] + "\n");
            timeTextArea.append(event[2][0] + "\n");
        }
    }


    // Helper method to get an array of years
    private String[] getYearStrings() {
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        String[] yearStrings = new String[10];
        for (int i = 0; i < 10; i++) {
            yearStrings[i] = String.valueOf(currentYear + i);
        }
        return yearStrings;
    }


    // Helper method to get an array of month names
    private String[] getMonthStrings() {
        return new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    }


    // Helper method to get an array of day numbers
    private String[] getDayStrings() {
        String[] dayStrings = new String[31];
        for (int i = 0; i < 31; i++) {
            dayStrings[i] = String.valueOf(i + 1);
        }
        return dayStrings;
    }
    //Getter methods for CalendarPanel
    public List<String> getEventNames() {
        return eventNamesCalendar;
    }
    public List<String> getEventDates() {
        return eventDatesCalendar;
    }
}


