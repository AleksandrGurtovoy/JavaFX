/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafxapplication.managers.GenericDAO;
import javafxapplication.models.UserFx;

public class ChartView extends BorderPane {

    private static final Random random = new Random();
    private final HBox hbox = new HBox();
    private final XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> series3 = new XYChart.Series<>();
    private final StackPane chartWrapper = new StackPane();
    private final Button nextBut = new Button("Next value");
    private final ToggleGroup group = new ToggleGroup();

    public ChartView() {
        final ToggleButton lineBut = new ToggleButton("Line chart");
        lineBut.setToggleGroup(group);

        final ToggleButton pieBut = new ToggleButton("Pie chart");
        pieBut.setToggleGroup(group);

        final ToggleButton barBut = new ToggleButton("Bar chart");
        barBut.setToggleGroup(group);

        lineBut.setUserData(getLineChart());
        pieBut.setUserData(getPieChart());
        barBut.setUserData(getBarChart());
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
                if (new_toggle == null) {
                    chartWrapper.getChildren().clear();
                    nextBut.setDisable(true);
                } else {
                    chartWrapper.getChildren().clear();
                    if (group.getSelectedToggle().equals(lineBut)) {
                        nextBut.setDisable(false);
                    } else {
                        nextBut.setDisable(true);
                    }
                    chartWrapper.getChildren().add((Chart) group.getSelectedToggle().getUserData());
                }
            }
        });

        nextBut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                series1.getData().add(ChartView.getNextPoint(series1.getData().size()));
                series2.getData().add(ChartView.getNextPoint(series2.getData().size()));
                series3.getData().add(ChartView.getNextPoint(series3.getData().size()));
            }
        });
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(lineBut, pieBut, barBut, nextBut);

        lineBut.fire();

        this.setBottom(hbox);
        this.setCenter(chartWrapper);

    }

    public static XYChart.Data<Number, Number> getNextPoint(int i) {
        return new XYChart.Data<Number, Number>(10 * i + 5, random.nextDouble() * 100);
    }

    public LineChart<Number, Number> getLineChart() {
        nextBut.setDisable(false);
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<Number, Number>(xAxis, yAxis);
        chart.setTitle("Random values");
        xAxis.setLabel("X Axis");
        yAxis.setLabel("Y Axis");
        series1.getData().clear();
        series2.getData().clear();
        series3.getData().clear();
        
        series1.setName("Random Data 1");
        for (int i = 0; i < 10; i++) {
            series1.getData().add(getNextPoint(i));
        }
        series2.setName("Random Data 2");
        for (int i = 0; i < 10; i++) {
            series2.getData().add(getNextPoint(i));
        }
        
        series3.setName("Random Data 3");
        for (int i = 0; i < 10; i++) {
            series3.getData().add(getNextPoint(i));
        }
        chart.getData().addAll(series1, series2, series3);
        return chart;
    }

    public PieChart getPieChart() {
        nextBut.setDisable(true);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        GenericDAO<UserFx> userManager = new GenericDAO(UserFx.class);

        List<UserFx> list = userManager.getAll();
        Map<String, Integer> map = new HashMap();
        List<PieChart.Data> datalist = new ArrayList();
        UserFx ufx;
        for (int i = 0; i < list.size(); i++) {
            ufx = (UserFx) list.get(i);
            if (map.containsKey(ufx.getCity())) {
                map.put(ufx.getCity(), map.get(ufx.getCity()) + 1);
            } else {
                map.put(ufx.getCity(), 1);
            }
        }
        for (Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getKey().equals("")) {
                pieChartData.add(new PieChart.Data("Unknown", entry.getValue()));
            } else {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
        }
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("User's cities");
        return chart;
    }

    public BarChart getBarChart() {
        nextBut.setDisable(true);
        final String china = "China";
        final String india = "India";
        final String usa = "USA";
        final String indonesia = "Indonesia";
        final String brasil = "Brasil";
        final String russia = "Russia";
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> bc
                = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle("Demography forecasts of the UN");
        xAxis.setLabel("Country");
        yAxis.setLabel("People(M)");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("2004");
        series1.getData().add(new XYChart.Data(china, 1308.0));
        series1.getData().add(new XYChart.Data(india, 1087, 0));
        series1.getData().add(new XYChart.Data(usa, 295, 4));
        series1.getData().add(new XYChart.Data(indonesia, 220, 1));
        series1.getData().add(new XYChart.Data(brasil, 183, 9));
        series1.getData().add(new XYChart.Data(russia, 143, 9));
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("2015");
        series2.getData().add(new XYChart.Data(china, 1393, 0));
        series2.getData().add(new XYChart.Data(india, 1260, 0));
        series2.getData().add(new XYChart.Data(usa, 325, 7));
        series2.getData().add(new XYChart.Data(indonesia, 246, 8));
        series2.getData().add(new XYChart.Data(brasil, 209, 4));
        series2.getData().add(new XYChart.Data(russia, 136, 7));
        
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("2020");
        series3.getData().add(new XYChart.Data(china, 2000, 0));
        series3.getData().add(new XYChart.Data(india, 1900, 0));
        series3.getData().add(new XYChart.Data(usa, 500, 7));
        series3.getData().add(new XYChart.Data(indonesia, 300, 8));
        series3.getData().add(new XYChart.Data(brasil, 400, 4));
        series3.getData().add(new XYChart.Data(russia, 20, 7));
        bc.getData().addAll(series1, series2, series3);
        return bc;
    }

}
