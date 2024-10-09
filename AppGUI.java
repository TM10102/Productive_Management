
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




class AppGUI extends JFrame {
    // Buttons for different sections
    private JButton calendarButton;
    private JButton todoListButton;
    private JButton workoutTrackerButton;
    private JButton workoutGoalsButton;
    private JButton workAssignmentsButton;


    // Panel for managing different cards (sections/panels)
    private JPanel cardPanel;
    private CardLayout cardLayout;


    // Panels for specific sections
    private WorkoutGoalsPanel workoutGoalsPanel;
    private TodoListPanel todoListPanel;




    public AppGUI() {
        super("Productive Management");


        setLayout(new BorderLayout());


        // Initialize cardPanel with CardLayout for managing different sections
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);


        // Buttons for navigating to different sections
        calendarButton = new JButton("Calendar");
        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "calendar");
                updateCalendar();
            }
        });


        todoListButton = new JButton("To-do List");
        todoListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "todolist");
            }
        });


        workoutTrackerButton = new JButton("Workout Tracker");
        workoutTrackerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "workouttracker");
            }
        });


        workoutGoalsButton = new JButton("Workout Goals");
        workoutGoalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "workoutgoals");
                updateWorkoutGoals();
            }
        });


        workAssignmentsButton = new JButton("Work Assignments");
        workAssignmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "workassignments");
            }
        });


        // Panel for holding navigation buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        buttonPanel.add(calendarButton);
        buttonPanel.add(todoListButton);
        buttonPanel.add(workoutTrackerButton);
        buttonPanel.add(workoutGoalsButton);
        buttonPanel.add(workAssignmentsButton);


        // Instances of panels for different sections
        todoListPanel = new TodoListPanel();
        WorkoutTrackerPanel workoutTrackerPanel = new WorkoutTrackerPanel();
        workoutGoalsPanel = new WorkoutGoalsPanel(workoutTrackerPanel);


        // Adding panels to cardPanel with corresponding names
        cardPanel.add(new CalendarPanel(todoListPanel), "calendar");
        cardPanel.add(workoutTrackerPanel, "workouttracker");
        cardPanel.add(workoutGoalsPanel, "workoutgoals");
        cardPanel.add(new WorkAssignmentPanel(), "workassignments");
        cardPanel.add(todoListPanel, "todolist");


        // Adding buttonPanel and cardPanel to the main frame
        add(buttonPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
    }


    // Method for updating workout goals progress bars
    public void updateWorkoutGoals() {
        workoutGoalsPanel.updateProgressBars();
    }


    // Method for updating the calendar
    public void updateCalendar() {
        CalendarPanel calendarPanel = (CalendarPanel) cardPanel.getComponent(0);
        calendarPanel.updateCalendar();
    }
}


