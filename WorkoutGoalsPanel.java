import javax.swing.*;
import java.awt.*;

class WorkoutGoalsPanel extends TodoListPanel {


    // Progress bars for different goals
    private JProgressBar totalMinutesProgressBar;
    private JProgressBar totalDaysProgressBar;
    private JProgressBar hardIntensityProgressBar;
    private JLabel progressLabel;


    // Labels displaying current values of goals
    private JLabel totalMinutesValueLabel;
    private JLabel totalDaysValueLabel;
    private JLabel hardIntensityValueLabel;


    // Default goals values
    private int totalMinutesGoal = 1500;
    private int totalDaysGoal = 20;
    private int totalHardIntensityGoal = 8;


    private WorkoutTrackerPanel workoutTrackerPanel;


    // Constructor for initializing the panel
    public WorkoutGoalsPanel(WorkoutTrackerPanel workoutTrackerPanel) {
        setLayout(new BorderLayout());
        this.workoutTrackerPanel = workoutTrackerPanel;


        // Button for editing goals
        JButton editGoalsButton = new JButton("Edit Goals");
        editGoalsButton.setFont(new Font("Arial", Font.PLAIN, 24));


        // Panel for holding the edit goals button
        JPanel editButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 350));
        editButtonPanel.add(editGoalsButton);


        // ActionListener for the edit goals button
        editGoalsButton.addActionListener(e -> showEditGoalsDialog());


        // Progress bars and labels setup
        totalMinutesProgressBar = new JProgressBar(0, 100);
        totalDaysProgressBar = new JProgressBar(0, 100);
        hardIntensityProgressBar = new JProgressBar(0, 100);


        Dimension progressBarSize = new Dimension(100, 200);
        totalMinutesProgressBar.setPreferredSize(progressBarSize);
        totalDaysProgressBar.setPreferredSize(progressBarSize);
        hardIntensityProgressBar.setPreferredSize(progressBarSize);


        progressLabel = new JLabel("Goal Progress", SwingConstants.CENTER);
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 30));


        JLabel totalMinutesLabel = new JLabel("Total Workout Minutes", SwingConstants.CENTER);
        JLabel totalDaysLabel = new JLabel("Total Workout Days", SwingConstants.CENTER);
        JLabel hardIntensityLabel = new JLabel("Total Hard Intensity Workouts", SwingConstants.CENTER);


        // Labels above each progress bar
        totalMinutesValueLabel = new JLabel();
        totalDaysValueLabel = new JLabel();
        hardIntensityValueLabel = new JLabel();


        // Panel for organizing progress bars and labels
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.add(Box.createVerticalStrut(50));
        progressPanel.add(createCenteredLabel(progressLabel));
        progressPanel.add(Box.createVerticalStrut(30));
        progressPanel.add(createVerticalProgressBar(totalMinutesProgressBar, totalMinutesLabel, "Minutes", totalMinutesValueLabel));
        progressPanel.add(Box.createVerticalStrut(30));
        progressPanel.add(createVerticalProgressBar(totalDaysProgressBar, totalDaysLabel, "Days", totalDaysValueLabel));
        progressPanel.add(Box.createVerticalStrut(30));
        progressPanel.add(createVerticalProgressBar(hardIntensityProgressBar, hardIntensityLabel, "Intensity", hardIntensityValueLabel));


        // Initial update of progress bars
        updateProgressBars();


        // Adding components to the panel
        add(editButtonPanel, BorderLayout.WEST);
        add(progressPanel, BorderLayout.CENTER);
    }


    // Method for creating a centered label
    private Component createCenteredLabel(JLabel label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createHorizontalGlue());
        panel.add(label);
        panel.add(Box.createHorizontalGlue());
        return panel;
    }


    // Method for creating a panel with a vertical progress bar and labels
    private Component createVerticalProgressBar(JProgressBar progressBar, JLabel label, String valueType, JLabel valueLabel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(valueLabel);


        progressBar.setStringPainted(true);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(progressBar);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);


        return panel;
    }


    // Method for updating progress bars and labels
    public void updateProgressBars() {
        // Update progress bar and label for total workout days
        totalDaysProgressBar.setValue((int) ((double) workoutTrackerPanel.getWorkoutNames().size() / totalDaysGoal * 100));
        totalDaysValueLabel.setText(workoutTrackerPanel.getWorkoutNames().size() + "/" + totalDaysGoal + " " + "Days");


        // Update progress bar and label for total hard intensity workouts
        hardIntensityProgressBar.setValue((int) ((double) countHighIntensityWorkouts() / totalHardIntensityGoal * 100));
        hardIntensityValueLabel.setText(countHighIntensityWorkouts() + "/" + totalHardIntensityGoal + " " + "Intensity");


        // Update progress bar and label for total workout minutes
        totalMinutesProgressBar.setValue((int) ((double) calculateTotalMinutes() / totalMinutesGoal * 100));
        totalMinutesValueLabel.setText(calculateTotalMinutes() + "/" + totalMinutesGoal + " " + "Minutes");
    }


    // Method for counting high-intensity workouts
    private int countHighIntensityWorkouts() {
        int highIntensityCount = 0;
        for (String intensity : workoutTrackerPanel.getWorkoutIntensities()) {
            if ("High".equals(intensity)) {
                highIntensityCount++;
            }
        }
        return highIntensityCount;
    }


    // Method for calculating total workout minutes
    private int calculateTotalMinutes() {
        int totalMinutes = 0;
        for (String time : workoutTrackerPanel.getWorkoutTimes()) {
            totalMinutes += Integer.parseInt(time);
        }
        return totalMinutes;
    }


    // Method for displaying the edit goals dialog
    private void showEditGoalsDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Total Workout Minutes Goal:"));
        JTextField totalMinutesField = new JTextField(Integer.toString(totalMinutesGoal));
        panel.add(totalMinutesField);


        panel.add(new JLabel("Total Workout Days Goal:"));
        JTextField totalDaysField = new JTextField(Integer.toString(totalDaysGoal));
        panel.add(totalDaysField);


        panel.add(new JLabel("Total Hard Intensity Workouts Goal:"));
        JTextField totalHardIntensityField = new JTextField(Integer.toString(totalHardIntensityGoal));
        panel.add(totalHardIntensityField);


        // Display a dialog for editing goals
        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Goals", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Update goals values based on user input
                totalMinutesGoal = Integer.parseInt(totalMinutesField.getText());
                totalDaysGoal = Integer.parseInt(totalDaysField.getText());
                totalHardIntensityGoal = Integer.parseInt(totalHardIntensityField.getText());


                // Update progress bars and labels with new goals
                updateProgressBars();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid integer values.");
            }
        }
    }
}


