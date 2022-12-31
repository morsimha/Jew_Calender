package com.maman14.calender;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.HashMap;

public class CalenderController {

    @FXML
    private GridPane daysGrade;

    @FXML
    private ComboBox<String> monthBox, yearBox;

    private HashMap<Date, String> h;
    private Button[][] buttons;
    private LocalDate currentMonth;

    private final int WIDTH = 7;
    private final int HEIGHT = 6;

    public void initialize() {
        buttons = new Button[HEIGHT][WIDTH];
        currentMonth = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(currentMonth.getYear(), currentMonth.getMonth());
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstDayOfMonth = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), 1);
        DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek();
        int firstColumnIndex = firstDayOfWeek.getValue() % WIDTH;
        int currentDay = 1;
        for (int row = 0; row < HEIGHT; row++) {
            for (int column = 0; column < WIDTH; column++) {
                if ((row == 0 && column < firstColumnIndex) || currentDay > daysInMonth) {
                    buttons[row][column] = new Button("");
                    buttons[row][column].setDisable(true);
                    buttons[row][column].setPrefSize(daysGrade.getPrefWidth()/7,daysGrade.getPrefWidth()/7);

                } else {
                    buttons[row][column] = new Button(String.valueOf(currentDay));
                    buttons[row][column].setPrefSize(daysGrade.getPrefWidth()/7,daysGrade.getPrefWidth()/7);
                    currentDay++;
                }
                daysGrade.add(buttons[row][column], column, row);
            }
        }
    }
}
