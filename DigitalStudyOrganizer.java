# JavaAssignmentimport javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class DigitalStudyOrganizer {

    private JFrame frame;
    private DefaultTableModel taskTableModel;
    private Map<String, String> progressData;
    private JLabel tasksCompletedLabel;
    private JLabel streakLabel;

    public DigitalStudyOrganizer() {
        // Initialize progress tracking data
        progressData = new HashMap<>();
        progressData.put("Tasks Completed", "0");
        progressData.put("Streak", "0 Days");

        // Create main frame
        frame = new JFrame("Digital Study Organizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        // Create tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Tasks", createTaskManagementPanel());
        tabbedPane.addTab("Schedule", createSchedulePanel());
        tabbedPane.addTab("Pomodoro Timer", createPomodoroPanel());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel dashboardLabel = new JLabel("Welcome to your Study Organizer Dashboard!", JLabel.CENTER);
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(dashboardLabel, BorderLayout.NORTH);

        JPanel progressPanel = new JPanel(new GridLayout(2, 2));
        progressPanel.add(new JLabel("Tasks Completed:"));
        tasksCompletedLabel = new JLabel(progressData.get("Tasks Completed"));
        progressPanel.add(tasksCompletedLabel);
        progressPanel.add(new JLabel("Streak:"));
        streakLabel = new JLabel(progressData.get("Streak"));
        progressPanel.add(streakLabel);

        panel.add(progressPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTaskManagementPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Task table
        String[] columns = {"Task", "Description", "Category", "Priority", "Deadline", "Status"};
        taskTableModel = new DefaultTableModel(columns, 0);
        JTable taskTable = new JTable(taskTableModel);
        panel.add(new JScrollPane(taskTable), BorderLayout.CENTER);

        // Task input
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField taskField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField categoryField = new JTextField();
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        JTextField deadlineField = new JTextField();
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"To-Do", "In Progress", "Completed"});

        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(taskField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Priority:"));
        inputPanel.add(priorityBox);
        inputPanel.add(new JLabel("Deadline (YYYY-MM-DD):"));
        inputPanel.add(deadlineField);
        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(statusBox);

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> {
            String task = taskField.getText();
            String description = descriptionField.getText();
            String category = categoryField.getText();
            String priority = (String) priorityBox.getSelectedItem();
            String deadline = deadlineField.getText();
            String status = (String) statusBox.getSelectedItem();

            if (!task.isEmpty() && !description.isEmpty() && !category.isEmpty() && !deadline.isEmpty()) {
                taskTableModel.addRow(new Object[]{task, description, category, priority, deadline, status});
                taskField.setText("");
                descriptionField.setText("");
                categoryField.setText("");
                deadlineField.setText("");

                // Update progress data
                if ("Completed".equals(status)) {
                    int completed = Integer.parseInt(progressData.get("Tasks Completed")) + 1;
                    progressData.put("Tasks Completed", String.valueOf(completed));
                    tasksCompletedLabel.setText(String.valueOf(completed));
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        inputPanel.add(addButton);
        panel.add(inputPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Weekly Schedule", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(label, BorderLayout.NORTH);

        JTextArea scheduleTextArea = new JTextArea("Enter your schedule here (e.g., Monday: Math 10:00 AM to 12:00 PM)");
        scheduleTextArea.setLineWrap(true);
        scheduleTextArea.setWrapStyleWord(true);
        panel.add(new JScrollPane(scheduleTextArea), BorderLayout.CENTER);

        JButton saveScheduleButton = new JButton("Save Schedule");
        saveScheduleButton.addActionListener(e -> {
            String schedule = scheduleTextArea.getText();
            if (!schedule.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Schedule saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Schedule is empty. Please add details before saving.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(saveScheduleButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPomodoroPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Pomodoro Timer", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(label, BorderLayout.NORTH);

        JPanel timerPanel = new JPanel();
        JLabel timerLabel = new JLabel("25:00", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        timerPanel.add(timerLabel);
        panel.add(timerPanel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            private Timer timer;
            private int timeLeft = 25 * 60; // 25 minutes in seconds

            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer != null) return;

                timer = new Timer(1000, event -> {
                    if (timeLeft > 0) {
                        timeLeft--;
                        int minutes = timeLeft / 60;
                        int seconds = timeLeft % 60;
                        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
                    } else {
                        timer.stop();
                        timer = null;
                        JOptionPane.showMessageDialog(frame, "Pomodoro session complete!", "Done", JOptionPane.INFORMATION_MESSAGE);

                        // Update streak on completion
                        int streak = Integer.parseInt(progressData.get("Streak").split(" ")[0]) + 1;
                        progressData.put("Streak", streak + " Days");
                        streakLabel.setText(streak + " Days");
                    }
                });
                timer.start();
            }
        });

        panel.add(startButton, BorderLayout.SOUTH);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DigitalStudyOrganizer::new);
    }
}
