import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.util.Map;

public class PieChartGenerator {

    public static JFreeChart createCommitsByContributorPieChart(Map<String, Integer> commitsByContributor) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Populate the dataset
        for (Map.Entry<String, Integer> entry : commitsByContributor.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Create the Pie Chart
        return ChartFactory.createPieChart(
                "Commits by Contributor", // Chart title
                dataset,                  // Dataset
                true,                     // Include legend
                true,                     // Use tooltips
                false                     // URLs
        );
    }
}

