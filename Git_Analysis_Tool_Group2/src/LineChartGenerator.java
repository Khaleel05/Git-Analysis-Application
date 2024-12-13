import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Day;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class LineChartGenerator {

    public static JFreeChart createCommitsPerDayChart(Map<String, Integer> commitsPerDay) {
        TimeSeries series = new TimeSeries("Commits Per Day");
        commitsPerDay.forEach((date, count) -> {
            try {
                series.add(new Day(new SimpleDateFormat("yyyy-MM-dd").parse(date)), count);
            } catch (Exception e) {
                System.err.println("Error parsing date: " + date);
            }
        });

        TimeSeriesCollection dataset = new TimeSeriesCollection(series);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Commits Per Day",
                "Date",
                "Number of Commits",
                dataset,
                true,
                true,
                false
        );

        // Apply consistent formatting
        customizeChart(chart);

        return chart;
    }

    public static JFreeChart createCommitsPerPersonPerDayChart(Map<String, Map<String, Integer>> commitsPerPersonPerDay) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        commitsPerPersonPerDay.forEach((person, dailyCommits) -> {
            TimeSeries series = new TimeSeries(person);
            dailyCommits.forEach((date, count) -> {
                try {
                    series.add(new Day(new SimpleDateFormat("yyyy-MM-dd").parse(date)), count);
                } catch (Exception e) {
                    System.err.println("Error parsing date: " + date);
                }
            });
            dataset.addSeries(series);
        });

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Commits Per Person Per Day",
                "Date",
                "Number of Commits",
                dataset,
                true,
                true,
                false
        );

        // Apply consistent formatting
        customizeChart(chart);

        return chart;
    }

    // Method to export a chart as a PNG
    public static void saveChartAsPNG(JFreeChart chart, String filePath, int width, int height) {
        try {
            ChartUtils.saveChartAsPNG(new File(filePath), chart, width, height);
            System.out.println("Chart saved as PNG: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving chart as PNG: " + e.getMessage());
        }
    }

    public static JFreeChart createLinesAddedAndDeletedChart(Map<String, int[]> linesPerDay) {
        // Create time series for lines added and lines deleted
        TimeSeries addedSeries = new TimeSeries("Lines Added");
        TimeSeries deletedSeries = new TimeSeries("Lines Deleted");

        linesPerDay.forEach((date, lines) -> {
            try {
                addedSeries.add(new Day(new SimpleDateFormat("yyyy-MM-dd").parse(date)), lines[0]);
                deletedSeries.add(new Day(new SimpleDateFormat("yyyy-MM-dd").parse(date)), lines[1]);
            } catch (Exception e) {
                System.err.println("Error parsing date: " + date);
            }
        });

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(addedSeries);
        dataset.addSeries(deletedSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Lines Added and Deleted Over Time",
                "Date",
                "Lines",
                dataset,
                true,
                true,
                false
        );
        customizeChart(chart);

        return chart;
    }
    // Helper method for consistent chart formatting
    private static void customizeChart(JFreeChart chart) {
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();

        // Standardize date format (e.g., MM-dd)
        dateAxis.setDateFormatOverride(new SimpleDateFormat("MM-dd"));

        // Ensure labels are horizontal (or rotate if needed)
        dateAxis.setVerticalTickLabels(false);
    }
}


