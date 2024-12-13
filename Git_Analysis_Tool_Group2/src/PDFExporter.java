import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PDFExporter {
    public static void exportToPDF(String filePath,
                                   List<ContributorMetrics> topContributorsByCommits,
                                   List<ContributorMetrics> topContributorsByLinesAdded,
                                   List<ContributorMetrics> topContributorsByLinesDeleted,
                                   Map<String, Integer> driverFrequency,
                                   Map<String, Integer> navigatorFrequency,
                                   Map<String, Integer> pairFrequency,
                                   List<JFreeChart> charts) { // Include the charts as a parameter

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Title
            document.add(new Paragraph("Contributor and Collaboration Metrics Report\n\n"));

            // Add Top Contributors by Commits
            document.add(new Paragraph("Top Contributors By Commits:"));
            addContributorMetricsTable(document, topContributorsByCommits);

            // Add Top Contributors by Lines Added
            document.add(new Paragraph("Top Contributors By Lines Added:"));
            addContributorMetricsTable(document, topContributorsByLinesAdded);

            // Add Top Contributors by Lines Deleted
            document.add(new Paragraph("Top Contributors By Lines Deleted:"));
            addContributorMetricsTable(document, topContributorsByLinesDeleted);

            // Add Driver Frequencies
            document.add(new Paragraph("Driver Frequencies:"));
            addFrequencyTable(document, driverFrequency);

            // Add Navigator Frequencies
            document.add(new Paragraph("Navigator Frequencies:"));
            addFrequencyTable(document, navigatorFrequency);

            // Add Driver-Navigator Pair Frequencies
            document.add(new Paragraph("Driver-Navigator Pair Frequencies:"));
            addFrequencyTable(document, pairFrequency);

            // Add Charts
            document.add(new Paragraph("\nVisual Charts:\n"));
            int chartIndex = 1;
            for (JFreeChart chart : charts) {
                addChartToPDF(document, chart, "chart_" + chartIndex++ + ".png");
            }

            document.close();
            System.out.println("PDF exported successfully to: " + filePath);
        } catch (DocumentException | IOException e) {
            System.err.println("Error exporting PDF: " + e.getMessage());
        }
    }

    private static void addContributorMetricsTable(Document document, List<ContributorMetrics> metricsList)
            throws DocumentException {
        PdfPTable table = new PdfPTable(6); // 6 columns

        // Add headers
        table.addCell("Contributor");
        table.addCell("Total Commits");
        table.addCell("Lines Added");
        table.addCell("Lines Deleted");
        table.addCell("Total Days Active");
        table.addCell("Average Commits Per Day");

        // Add data rows
        for (ContributorMetrics metrics : metricsList) {
            table.addCell(metrics.getName());
            table.addCell(String.valueOf(metrics.getTotalCommits()));
            table.addCell(String.valueOf(metrics.getLinesAdded()));
            table.addCell(String.valueOf(metrics.getLinesDeleted()));
            table.addCell(String.valueOf(metrics.getTotalDaysActive()));
            table.addCell(String.format("%.2f", metrics.getAverageCommitsPerDay()));
        }

        document.add(table);
    }

    private static void addFrequencyTable(Document document, Map<String, Integer> frequencyMap)
            throws DocumentException {
        PdfPTable table = new PdfPTable(2); // 2 columns: Key, Frequency

        // Add headers
        table.addCell("Key");
        table.addCell("Frequency");

        // Add data rows
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            table.addCell(entry.getKey());
            table.addCell(String.valueOf(entry.getValue()));
        }

        document.add(table);
    }

    private static void addChartToPDF(Document document, JFreeChart chart, String tempFileName) {
        try {
            // Save the chart as a temporary PNG
            File tempFile = new File(tempFileName);
            ChartUtils.saveChartAsPNG(tempFile, chart, 600, 400);

            // Add the image to the PDF
            Image chartImage = Image.getInstance(tempFileName);
            chartImage.scaleToFit(500, 300); // Scale the image to fit
            document.add(chartImage);

            // Delete the temporary file after adding it to the PDF
            tempFile.delete();
        } catch (Exception e) {
            System.err.println("Error adding chart to PDF: " + e.getMessage());
        }
    }
}
