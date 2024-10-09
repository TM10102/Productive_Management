


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.util.Calendar;






public class WorkoutTrackerPanel extends TodoListPanel {
    private JTextArea nameTextArea;
    private JTextArea dateTextArea;
    private JTextArea timeTextArea;
    private JTextArea intensityTextArea;


    //  lists to store workouts
    private List<String> workoutNames = new ArrayList<>();
    private List<String> workoutDates = new ArrayList<>();
    private List<String> workoutTimes = new ArrayList<>();
    private List<String> workoutIntensities = new ArrayList<>();


    public WorkoutTrackerPanel() {
        super();


        JLabel titleLabel = new JLabel("Previous Workouts", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);


        // Customize for Workout Tracker
        JTextArea workoutTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(workoutTextArea);
        add(scrollPane, BorderLayout.CENTER);


        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Remove");


        nameTextArea = createVerticalTextArea("Name");
        dateTextArea = createVerticalTextArea("Date");
        timeTextArea = createVerticalTextArea("Time (Minutes)");
        intensityTextArea = createVerticalTextArea("Intensity");


        JPanel infoPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        infoPanel.add(createCenteredPanel(nameTextArea, "Name"));
        infoPanel.add(createCenteredPanel(dateTextArea, "Date"));
        infoPanel.add(createCenteredPanel(timeTextArea, "Time (Minutes)"));
        infoPanel.add(createCenteredPanel(intensityTextArea, "Intensity"));
        add(infoPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.EAST);


        // Add action listeners
        addButton.addActionListener(e -> addWorkout());
        deleteButton.addActionListener(e -> removeWorkout());
        editButton.addActionListener(e -> editWorkout());
    }


    private void addWorkout() {
        // Prompt for workout name
        String name = JOptionPane.showInputDialog(this, "Enter workout name:");
        if (name == null || name.trim().isEmpty()) {
            return; // Cancel or empty input, do nothing
        }


        // Dropdown menu for day
        String[] dayOptions = new String[31];
        for (int i = 1; i <= 31; i++) {
            dayOptions[i - 1] = String.valueOf(i);
        }
        JComboBox<String> dayComboBox = new JComboBox<>(dayOptions);


        // Dropdown menu for month
        String[] monthOptions = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthComboBox = new JComboBox<>(monthOptions);


        // Dropdown menu for year
        String[] yearOptions = new String[10];
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 10; i++) {
            yearOptions[i] = String.valueOf(currentYear + i);
        }
        JComboBox<String> yearComboBox = new JComboBox<>(yearOptions);


        JPanel datePanel = new JPanel(new GridLayout(1, 3));
        datePanel.add(dayComboBox);
        datePanel.add(monthComboBox);
        datePanel.add(yearComboBox);


        int result = JOptionPane.showOptionDialog(
                this,
                datePanel,
                "Select workout date:",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null
        );


        if (result != JOptionPane.OK_OPTION) {
            return; // Cancelled, do nothing
        }


        // Get selected day, month, and year
        String selectedDay = (String) dayComboBox.getSelectedItem();
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        String selectedYear = (String) yearComboBox.getSelectedItem();


        // Format the date
        String formattedDate = String.format("%s %s %s", selectedDay, selectedMonth, selectedYear);


        // Prompt for workout time (in minutes) with error handling
        String time = JOptionPane.showInputDialog(this, "Enter workout time (in minutes):");
        if (time == null || time.trim().isEmpty()) {
            return; // Cancel or empty input, do nothing
        }


        // Validate that the input for workout time is a number
        try {
            int minutes = Integer.parseInt(time);
            if (minutes <= 0) {
                JOptionPane.showMessageDialog(this, "Workout time must be a positive number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return; // Invalid input, do nothing
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for workout time. Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return; // Invalid input, do nothing
        }


        // Prompt for workout intensity
        String[] intensityOptions = {"High", "Moderate", "Low"};
        JComboBox<String> intensityComboBox = new JComboBox<>(intensityOptions);
        result = JOptionPane.showOptionDialog(
                this,
                intensityComboBox,
                "Select workout intensity:",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null
        );


        if (result == JOptionPane.OK_OPTION) {
            String intensity = (String) intensityComboBox.getSelectedItem();


            // Add workout details to the static lists
            workoutNames.add(name);
            workoutDates.add(formattedDate);
            workoutTimes.add(time);
            workoutIntensities.add(intensity);


            // Update JTextAreas or any other components based on the added workout details
            updateTextAreas();
        }
    }






    void removeWorkout() {
        if (workoutNames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No workouts added.", "Remove Workout", JOptionPane.INFORMATION_MESSAGE);
            return;
        }


        // Show a dialog to select the workout to remove
        String[] workoutOptions = workoutNames.toArray(new String[0]);
        String selectedWorkout = (String) JOptionPane.showInputDialog(this, "Select a workout to remove:",
                "Remove Workout", JOptionPane.QUESTION_MESSAGE, null, workoutOptions, workoutOptions[0]);


        if (selectedWorkout != null) {
            // Find the selected workout in the list
            int selectedIndex = workoutNames.indexOf(selectedWorkout);


            // Remove the selected workout from the lists
            workoutNames.remove(selectedIndex);
            workoutDates.remove(selectedIndex);
            workoutTimes.remove(selectedIndex);
            workoutIntensities.remove(selectedIndex);


            // Update JTextAreas after removing the workout
            updateTextAreas();
        }
    }


    void editWorkout() {
        if (workoutNames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No workouts added.", "Edit Workout", JOptionPane.INFORMATION_MESSAGE);
            return;
        }


        // Show a dialog to select the workout to edit
        String[] workoutOptions = workoutNames.toArray(new String[0]);
        String selectedWorkout = (String) JOptionPane.showInputDialog(this, "Select a workout to edit:",
                "Edit Workout", JOptionPane.QUESTION_MESSAGE, null, workoutOptions, workoutOptions[0]);


        if (selectedWorkout != null) {
            // Find the selected workout in the list
            int selectedIndex = workoutNames.indexOf(selectedWorkout);


            // Show input dialogs to edit workout details
            String editedName = JOptionPane.showInputDialog(this, "Edit workout name:", workoutNames.get(selectedIndex));
            String editedDate = JOptionPane.showInputDialog(this, "Edit workout date:", workoutDates.get(selectedIndex));


            // Prompt for workout time (in minutes) with error handling
            String editedTime = JOptionPane.showInputDialog(this, "Edit workout time (Minutes):", workoutTimes.get(selectedIndex));
            if (editedTime == null || editedTime.trim().isEmpty()) {
                return; // Cancel or empty input, do nothing
            }


            // Validate that the input for edited workout time is a number
            try {
                int minutes = Integer.parseInt(editedTime);
                if (minutes <= 0) {
                    JOptionPane.showMessageDialog(this, "Workout time must be a positive number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return; // Invalid input, do nothing
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input for workout time. Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return; // Invalid input, do nothing
            }


            // Show a dropdown menu to edit intensity
            String[] intensityOptions = {"High", "Moderate", "Low"};
            int intensityIndex = JOptionPane.showOptionDialog(
                    this,
                    "Select workout intensity:",
                    "Edit Intensity",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    intensityOptions,
                    intensityOptions[0]);


            if (editedName != null && !editedName.trim().isEmpty() && editedDate != null && !editedDate.trim().isEmpty() && !editedTime.trim().isEmpty() && intensityIndex != -1) {
                // Update the workout details in the lists
                workoutNames.set(selectedIndex, editedName);
                workoutDates.set(selectedIndex, editedDate);
                workoutTimes.set(selectedIndex, editedTime);
                workoutIntensities.set(selectedIndex, intensityOptions[intensityIndex]);


                // Update JTextAreas with the edited information
                updateTextAreas();
            }
        }
    }
    @Override
    void updateTextAreas() {
        nameTextArea.setText("");
        dateTextArea.setText("");
        timeTextArea.setText("");
        intensityTextArea.setText("");


        for (int i = 0; i < workoutNames.size(); i++) {
            nameTextArea.append(workoutNames.get(i) + "\n");
            dateTextArea.append(workoutDates.get(i) + "\n");
            timeTextArea.append(workoutTimes.get(i) + "\n");
            intensityTextArea.append(workoutIntensities.get(i) + "\n");
        }
    }
    //Getter methods for WorkoutGoalsPanel
    public List<String> getWorkoutNames() {
        return workoutNames;
    }
    public List<String> getWorkoutTimes() {
        return workoutTimes;
    }
    public List<String> getWorkoutIntensities() {
        return workoutIntensities;
    }
}

