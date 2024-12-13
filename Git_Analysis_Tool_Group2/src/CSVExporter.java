import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {

    public static void exportToCSV(String filePath, List<ContributorMetrics> topContributorsByCommits,
                                   List<ContributorMetrics> topContributorsByLinesAdded,
                                   List<ContributorMetrics> topContributorsByLinesDeleted) {

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Write the data for each "section" in the CSV file

            writer.writeNext(new String[] {"Top Contributors By Commits"});
            writeContributorMetrics(writer, topContributorsByCommits);

            writer.writeNext(new String[] {""});

            writer.writeNext(new String[] {"Top Contributors By Lines Added"});
            writeContributorMetrics(writer, topContributorsByLinesAdded);

            writer.writeNext(new String[] {""});

            writer.writeNext(new String[] {"Top Contributors By Lines Deleted"});
            writeContributorMetrics(writer, topContributorsByLinesDeleted);

            System.out.println("Exported to CSV: " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting to CSV: " + e.getMessage());
        }
    }

    private static void writeContributorMetrics(CSVWriter writer, List<ContributorMetrics> metricsList) {
        // Write headers
        writer.writeNext(new String[] {"Contributor", "Total Commits", "Lines Added", "Lines Deleted", "Total Days Active", "Average Commits Per Day"});

        // Write data rows
        for (ContributorMetrics metrics : metricsList) {
            String name = metrics.getName().replace("\"", "\"\""); // Escape any double quotes

            writer.writeNext(new String[] {
                    "\"" + name + "\"",
                    String.valueOf(metrics.getTotalCommits()),
                    String.valueOf(metrics.getLinesAdded()),
                    String.valueOf(metrics.getLinesDeleted()),
                    String.valueOf(metrics.getTotalDaysActive()),
                    String.format("%.2f", metrics.getAverageCommitsPerDay())
            });
        }
    }
}
