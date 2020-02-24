
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by hug.
 */
public class Experiments {
    public static void experiment1() {
        List<Integer> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        BST<Double> bst = new BST<Double>();
        for (int i = 0; i < 5000; i++){
            Double r = Math.random();
            bst.add(r);
            xValues.add(i);
            yValues.add(bst.computeAveDepth());
        }
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("x label").yAxisTitle("y label").build();
        chart.addSeries("x", xValues, yValues);
        new SwingWrapper(chart).displayChart();
    }

    public static void experiment2() {
        List<Integer> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        BST<Double> bst = new BST<Double>();
        // Generate random items into bst
        for (int i = 0; i < 5000; i++){
            Double r = Math.random();
            bst.add(r);
        }
        for (int i = 0; i < 50000; i++){
            xValues.add(i);
            double key = bst.getRandomKey();
            bst.deleteTakingSuccessor(key);

            Double r = Math.random();
            bst.add(r);
            yValues.add(bst.computeAveDepth());
        }
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("x label").yAxisTitle("y label").build();
        chart.addSeries("x", xValues, yValues);
        new SwingWrapper(chart).displayChart();
    }

    public static void experiment3() {
    }

    public static void main(String[] args) {
        //experiment1();
        experiment2();
    }
}
