package com.maman14.calender;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javax.swing.*;
import java.time.*;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;

public class CalenderController {

    @FXML
    private GridPane daysGrade;

    @FXML
    private ComboBox<String> monthBox, yearBox;

    private HashMap<LocalDate, String> meetings;
    private Button[][] buttons;
    private LocalDate currentDate;
    private LocalDate currentYear;


    private final int WIDTH = 7;
    private final int HEIGHT = 6;

    public void initialize() {
        buildComboBoxes();
        initGrid();
    }




    private void buildComboBoxes() {
        final int DAYS = 31, MONTHS = 12, START_YEAR = 1948, END_YEAR = LocalDate.now().getYear();
        meetings = new HashMap<LocalDate, String>();

        for (Month month : Month.values()) {
            monthBox.getItems().add(month.getDisplayName(TextStyle.FULL, Locale.getDefault()));
            monthBox.setValue(LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        }
        for (int i = START_YEAR; i <= END_YEAR; i++) {
            yearBox.getItems().add(String.valueOf(i));
            yearBox.setValue(END_YEAR+"");
        }
    }

    private void initGrid() {
        buttons = new Button[HEIGHT][WIDTH];
        currentDate = LocalDate.now(); //init with current date grid
        int firstDay = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1).getDayOfWeek().getValue();
        int daysInMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonth()).lengthOfMonth();
        int currentDay = 1;

        for (int row = 0; row < HEIGHT; row++) {
            for (int column = 0; column < WIDTH; column++) {
                if ((row == 0 && column < firstDay) || currentDay > daysInMonth) {
                    buttons[row][column] = new Button("");
                    buttons[row][column].setDisable(true);
                    buttons[row][column].setPrefSize(daysGrade.getPrefWidth() / WIDTH, daysGrade.getPrefWidth() / WIDTH);
                } else {
                    buttons[row][column] = new Button(currentDay+"");
                    buttons[row][column].setPrefSize(daysGrade.getPrefWidth() / WIDTH, daysGrade.getPrefWidth() / WIDTH);
                    int finalRow = row, finalColumn = column;
                    buttons[row][column].setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            dayPressed(mouseEvent, finalRow, finalColumn, currentDate);
                        }
                    }) ;
                    currentDay++;
                }
                daysGrade.add(buttons[row][column], column, row);
            }
        }
    }

    private void dayPressed(MouseEvent mouseEvent, int row, int column, LocalDate currentDate) {

//        LocalDate selectedDate = (LocalDate) buttons[finalRow][finalColumn].get();
        String meeting = JOptionPane.showInputDialog(null, "Set meeting details: ", "Set an Appointment", JOptionPane.INFORMATION_MESSAGE);
        if (meeting != null)
//            if meetings.containsKey(currentDate)
            meetings.put(currentDate, meeting);
    }


}