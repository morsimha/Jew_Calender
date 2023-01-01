package com.maman14.calender;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javax.swing.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;

public class CalenderController {

    @FXML
    private GridPane daysGrid;

    @FXML
    private ComboBox<String> monthBox, yearBox;

    private HashMap<LocalDate, String> meetings;
    private Button[][] buttons;
    private LocalDate currentDate;


    private final int WIDTH = 7;
    private final int HEIGHT = 6;

    public void initialize() {
        meetings = new HashMap<LocalDate, String>();
        buildComboBoxes();
        initGrid(null);
    }
    @FXML
    private void setDatePressed(ActionEvent event) {
        String monthName = monthBox.getValue();
        int month = monthBox.getItems().indexOf(monthName)+1;
        int year = Integer.parseInt(yearBox.getValue());
//        System.out.println();
        currentDate = LocalDate.of(year,month, 1);
        initGrid(currentDate);
    }



    private void buildComboBoxes() {
        final int START_YEAR = 1948, END_YEAR = LocalDate.now().getYear();

        for (Month month : Month.values()) {
            monthBox.getItems().add(month.getDisplayName(TextStyle.FULL, Locale.getDefault()));
            monthBox.setValue(LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        }
        for (int i = START_YEAR; i <= END_YEAR; i++) {
            yearBox.getItems().add(String.valueOf(i));
            yearBox.setValue(END_YEAR+"");
        }
    }

    private void initGrid(LocalDate newDate) {
        if (newDate == null)
            currentDate = LocalDate.now(); //init with current date grid
        else {
            daysGrid.getChildren().clear();
            currentDate = newDate; //init with current date grid
        }
        int firstDay = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1).getDayOfWeek().getValue();
        int daysInMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonth()).lengthOfMonth();

        int currentDayNum = 1;
        buttons = new Button[HEIGHT][WIDTH];

        for (int row = 0; row < HEIGHT; row++) {for (int column = 0; column < WIDTH; column++) {
                if ((row == 0 && column < firstDay) || currentDayNum > daysInMonth) {
                    buttons[row][column] = new Button("");
                    buttons[row][column].setDisable(true);
                    buttons[row][column].setStyle("-fx-base: rgb(196,193,193);");
                    buttons[row][column].setPrefSize(daysGrid.getPrefWidth() / WIDTH, daysGrid.getPrefWidth() / WIDTH);
                } else {
                    buttons[row][column] = new Button(currentDayNum+"");
                    buttons[row][column].setPrefSize(daysGrid.getPrefWidth() / WIDTH, daysGrid.getPrefWidth() / WIDTH);
                    int finalCurrentDayNum = currentDayNum, finalRow = row,finalColumn = column;
                    buttons[row][column].setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            currentDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), finalCurrentDayNum);
                            dayPressed(mouseEvent, currentDate, buttons[finalRow][finalColumn]);
                        }
                    }) ;
                    currentDayNum++;
                    currentDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), finalCurrentDayNum);
                    if (meetings.containsKey(currentDate))
                        buttons[finalRow][finalColumn].setStyle("-fx-base: rgba(241,170,134,0.8);");

                }
                daysGrid.add(buttons[row][column], column, row);
            }
        }
    }

    private void dayPressed(MouseEvent mouseEvent, LocalDate currentDate, Button btn) {
        String msg;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        String[] options = new String[4];
        options[0] = "Add new Meeting";
        options[1] = "Edit Meetings";
        options[2] = "Remove all";
        options[3] = "Exit";

        if (meetings.containsKey(currentDate))
            msg = currentDate.format(formatter) + " Meetings:\n" + meetings.get(currentDate);
        else
            msg = "No meetings for this day.";

        JOptionPane.showMessageDialog(null, msg, currentDate.format(formatter) + " Meetings", JOptionPane.INFORMATION_MESSAGE);
        int answer = JOptionPane.showOptionDialog(null, "What would you like to do?", currentDate.format(formatter) + " Meetings", 0, JOptionPane.QUESTION_MESSAGE, null, options, null);

        if (answer == 0)
            addNewMeeting(formatter, btn);
        else if (answer == 1)
            editMeeting(formatter);
        else if (answer == 2)
            removeAllMeetings(formatter, btn);
    }

    private void addNewMeeting(DateTimeFormatter formatter,Button btn) {
        String meetingInfo = JOptionPane.showInputDialog(null, "Set meeting details: ", "Set an Appointment", JOptionPane.INFORMATION_MESSAGE);
        if (meetingInfo != null && !meetingInfo.equals("")) {
            meetingInfo = "- " + meetingInfo;
            if (meetings.putIfAbsent(currentDate, meetingInfo) != null) {
                meetingInfo = meetings.get(currentDate) + "\n" + meetingInfo;
                meetings.put(currentDate, meetingInfo);
            } else
                btn.setStyle("-fx-base: rgba(241,170,134,0.8);");
            JOptionPane.showMessageDialog(null, "New meeting added.\n" + currentDate.format(formatter) + " Meetings:\n" + meetingInfo, "Hooray", JOptionPane.INFORMATION_MESSAGE);
        }
    }

        private void editMeeting(DateTimeFormatter formatter) {
        String meetingInfo;
            if (meetings.containsKey(currentDate)) {
                meetingInfo = JOptionPane.showInputDialog(null, "Insert your changes here.\nPlease notice all old meeting will be removed.", "Edit " + currentDate.format(formatter), JOptionPane.INFORMATION_MESSAGE);
                if (meetingInfo != null) {
                    meetings.put(currentDate, "- " +meetingInfo);
                    JOptionPane.showMessageDialog(null, "Meeting edited.\n" + currentDate.format(formatter) + " Meetings:\n" + "- " +meetingInfo, "Hooray", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else
                JOptionPane.showMessageDialog(null, "No meetings to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    private void removeAllMeetings(DateTimeFormatter formatter, Button btn) {

        if (meetings.containsKey(currentDate)) {
            JOptionPane.showMessageDialog(null, "All meeting were removed for \n" + currentDate.format(formatter), "So Empty..", JOptionPane.INFORMATION_MESSAGE);
            meetings.remove(currentDate);
            btn.setStyle("-fx-base:#eae9e9;");
        }
        else
            JOptionPane.showMessageDialog(null, "No meetings to remove.", "Error", JOptionPane.ERROR_MESSAGE);

    }
    }