


import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.*;
import java.awt.*;
import java.util.Map;


public class WorkAssignmentPanel extends TodoListPanel {


    // HashMap to store tasks with their names as keys
    private Map<String, Task> tasksMap;


    // JTextAreas for displaying task information
    private JTextArea taskTextArea;
    private JTextArea deadlineDateTextArea;
    private JTextArea deadlineTimeTextArea;


    // Buttons for adding, editing, and deleting tasks
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;


    // Constructor for initializing the work assignment panel
    public WorkAssignmentPanel() {
        // Call the constructor of the superclass
        super();


        // Customize the title label
        JLabel titleLabel = new JLabel("Next Work Assignments", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);


        // Initialize the tasks Hashmap
        tasksMap = new HashMap<>();  // Initialize HashMap


        // Customize JTextAreas
        taskTextArea = createVerticalTextArea("Task/Project");
        deadlineDateTextArea = createVerticalTextArea("Deadline Date");
        deadlineTimeTextArea = createVerticalTextArea("Deadline Time");


        // Panel for displaying task information
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 5, 5)); // Use GridLayout with 1 row and 3 columns
        infoPanel.add(createCenteredPanel(taskTextArea, "Task"));
        infoPanel.add(createCenteredPanel(deadlineDateTextArea, "Deadline Date"));
        infoPanel.add(createCenteredPanel(deadlineTimeTextArea, "Deadline Time"));
        add(infoPanel, BorderLayout.CENTER);


        // Customize buttons
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");


        // Panel for holding buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);


        add(buttonPanel, BorderLayout.EAST);


        // Add action listeners to buttons
        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> removeTask());
        editButton.addActionListener(e -> editTask());
    }


    // Custom class to represent a task
    static class Task implements Comparable<Task> {
        private String name;
        private String date;
        private String time;


        // Constructor for initializing a task
        public Task(String name, String date, String time) {
            this.name = name;
            this.date = date;
            this.time = time;
        }


        // Getter method for task name
        public String getName() {
            return name;
        }


        // Getter method for task deadline date
        public String getDate() {
            return date;
        }


        // Getter method for task deadline time
        public String getTime() {
            return time;
        }


        // Comparable interface method for comparing tasks based on date and time
        @Override
        public int compareTo(Task other) {
            int dateComparison = this.date.compareTo(other.date);
            if (dateComparison != 0) {
                return dateComparison;
            }
            return this.time.compareTo(other.time);
        }
    }


    // Method to add tasks to the tasksMap and update JTextAreas
    protected void addTask() {


        // Get the task name from the user
        String taskName = JOptionPane.showInputDialog(this, "Enter task/project name:");


        // Check if the entered name is not empty
        if (taskName != null && !taskName.trim().isEmpty()) {
            // Check if the task with the same name already exists
            if (isDuplicate(taskName)) {
                JOptionPane.showMessageDialog(this, "Task with the same name already exists.", "Duplicate Assignment", JOptionPane.WARNING_MESSAGE);
                return;
            }


            // Create combo boxes for selecting year, month, and day
            JComboBox<String> yearComboBox = new JComboBox<>(getYearStrings());
            JComboBox<String> monthComboBox = new JComboBox<>(getMonthStrings());
            JComboBox<String> dayComboBox = new JComboBox<>(getDayStrings());


            // Create a spinner for selecting the deadline time
            SpinnerDateModel dateModel = new SpinnerDateModel();
            JSpinner timeSpinner = new JSpinner(dateModel);
            JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
            timeSpinner.setEditor(timeEditor);


            // Create panels to organize the input components
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


            // Show a confirmation dialog with input components
            int result = JOptionPane.showConfirmDialog(
                    this,
                    inputPanel,
                    "Select deadline date and time",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );


            // Check if the user clicked OK
            if (result == JOptionPane.OK_OPTION) {
                // Get the selected date and time
                String selectedDate = yearComboBox.getSelectedItem() + "-"
                        + monthComboBox.getSelectedItem() + "-"
                        + dayComboBox.getSelectedItem();
                String selectedTime = new SimpleDateFormat("HH:mm").format((java.util.Date) timeSpinner.getValue());


                // Create a new task and add it to the map
                Task newTask = new Task(taskName, selectedDate, selectedTime);
                tasksMap.put(taskName, newTask);
                // Update JTextAreas with only the relevant information
                updateTaskTextAreas();
            }
        }
    }


    // Method to remove tasks from the tasksMap and update JTextAreas
    protected void removeTask() {
        // Check if the tasksMap is empty
        if (tasksMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks added.", "Remove Task", JOptionPane.INFORMATION_MESSAGE);
            return;
        }


        // Show a dialog to select the task to remove
        String[] taskNames = tasksMap.keySet().toArray(new String[0]);
        String selectedTask = (String) JOptionPane.showInputDialog(this, "Select a task to remove:",
                "Remove Task", JOptionPane.QUESTION_MESSAGE, null, taskNames, taskNames[0]);


        // Check if the user selected a task
        if (selectedTask != null) {
            // Remove the selected task from the map
            tasksMap.remove(selectedTask);
            updateTaskTextAreas();
            JOptionPane.showMessageDialog(this, "Task removed successfully.", "Remove Task", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    // Method to edit tasks in the tasksMap and update JTextAreas
    protected void editTask() {
        // Check if the tasksMap is empty
        if (tasksMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks added.", "Edit Task", JOptionPane.INFORMATION_MESSAGE);
            return;
        }


        // Show a dialog to select the task to edit
        String[] taskNames = tasksMap.keySet().toArray(new String[0]);
        String selectedTask = (String) JOptionPane.showInputDialog(this, "Select a task to edit:",
                "Edit Task", JOptionPane.QUESTION_MESSAGE, null, taskNames, taskNames[0]);


        // Check if the user selected a task
        if (selectedTask != null) {
            // Get the selected task from the map
            Task selectedTaskObject = tasksMap.get(selectedTask);


            // Show the input dialog to edit the task details
            String editedName = JOptionPane.showInputDialog(this, "Edit task name:", selectedTaskObject.getName());


            // Check if the edited name is not empty
            if (editedName != null && !editedName.trim().isEmpty()) {
                // Create combo boxes for selecting year, month, and day
                JComboBox<String> yearComboBox = new JComboBox<>(getYearStrings());
                JComboBox<String> monthComboBox = new JComboBox<>(getMonthStrings());
                JComboBox<String> dayComboBox = new JComboBox<>(getDayStrings());


                // Set the date directly using the values from the selected task
                String[] dateParts = selectedTaskObject.getDate().split("-");
                yearComboBox.setSelectedItem(dateParts[0]);
                monthComboBox.setSelectedItem(dateParts[1]);
                dayComboBox.setSelectedItem(dateParts[2]);


                // Create a spinner for selecting the deadline time
                SpinnerDateModel dateModel = new SpinnerDateModel();
                JSpinner timeSpinner = new JSpinner(dateModel);
                JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
                timeSpinner.setEditor(timeEditor);


                // Create panels to organize the input components
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


                // Show a confirmation dialog with input components
                int result = JOptionPane.showConfirmDialog(
                        this,
                        inputPanel,
                        "Edit deadline date and time",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );


                // Check if the user clicked OK
                if (result == JOptionPane.OK_OPTION) {
                    // Get the edited date and time from the components
                    String editedDate = yearComboBox.getSelectedItem() + "-"
                            + monthComboBox.getSelectedItem() + "-"
                            + dayComboBox.getSelectedItem();
                    String editedTime = new SimpleDateFormat("HH:mm").format((java.util.Date) timeSpinner.getValue());


                    // Update the selected task with edited details
                    selectedTaskObject.name = editedName;
                    selectedTaskObject.date = editedDate;
                    selectedTaskObject.time = editedTime;


                    // Update JTextAreas with only the relevant information
                    updateTaskTextAreas();
                }
            }
        }
    }
    // Method to update JTextAreas with only the relevant information
    void updateTaskTextAreas() {
        // Clear JTextAreas
        taskTextArea.setText("");
        deadlineDateTextArea.setText("");
        deadlineTimeTextArea.setText("");


        // Populate JTextAreas with task information
        for (Task task : tasksMap.values()) {
            taskTextArea.append(task.getName() + "\n");
            deadlineDateTextArea.append(task.getDate() + "\n");
            deadlineTimeTextArea.append(task.getTime() + "\n");
        }
    }


    // Method to update JTextAreas with only the relevant information
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
        return new String[]{
                "January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"
        };
    }
    // Helper method to get an array of day strings
    private String[] getDayStrings() {
        String[] dayStrings = new String[31];
        for (int i = 0; i < 31; i++) {
            dayStrings[i] = String.valueOf(i + 1);
        }
        return dayStrings;
    }


    // Helper method to check if a task name already exists
    private boolean isDuplicate(String taskName) {
        return tasksMap.containsKey(taskName);
    }


}

