import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GitAnalysisToolGUI extends JFrame {

    public static JFrame frame;
    public static String repositoryAddress;
    public static JTextArea consoleArea = new JTextArea();
    private static final Font currentFont = consoleArea.getFont();
    private static final Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), 13);
    private static final JTextField username = new JTextField();
    private static final JTextField keytoken = new JTextField();
    private static final JTextField local_path = new JTextField();
    private static final JPanel topRightPanel = new JPanel();

    static LightIndicator lightIndicator;

    public static void main(String[] args) {

        frame = new JFrame("Git Analysis Tool");
        consoleArea.setFont(newFont);
        lightIndicator = new LightIndicator(3, Color.red);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu gitMenu = new JMenu("Git");
        JMenu viewMenu = new JMenu("View");
        JMenu codeMenu = new JMenu("Code");

        JMenuItem codeInspectionItem = new JMenuItem("Inspect Code");

        JMenuItem exportMenuItem = new JMenuItem("Export");
        JMenuItem detectPairProgrammingMenuItem = new JMenuItem("Detect Pair Programming");

        exportMenuItem.addActionListener(e -> {
            storeDataToCSV();
        });
        detectPairProgrammingMenuItem.addActionListener(e -> {
            PairProgramming pairProgramming = new PairProgramming();
            pairProgramming.fetchCommits();
            pairProgramming.printArrayData();
            pairProgramming.checkForPairProgramming();
            consoleArea.append(pairProgramming.getConsole().getText());
        });
        codeInspectionItem.addActionListener(e->{
        inspectCode();
        });

        fileMenu.add(exportMenuItem);
        codeMenu.add(codeInspectionItem);

        gitMenu.add(detectPairProgrammingMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(gitMenu);
        menuBar.add(codeMenu);

        frame.setJMenuBar(menuBar);

        JLabel repository_label = new JLabel("Enter Repository URL: ");
        JLabel username_label = new JLabel("Username: ");
        JLabel keytoken_label = new JLabel("Token Key: ");
        JLabel local_path_label = new JLabel("Path to be Cloned: ");
        JTextField repository = new JTextField("");
        consoleArea = new JTextArea("Welcome Back User!" + '\n');
        outputToScreen("Console Running...");
        consoleArea.setEditable(false);


        JCheckBox commitsPerDayChartCheckBox = new JCheckBox("Commits Per Day Chart");
        JCheckBox commitsPerPersonPerDayChartCheckBox = new JCheckBox("Commits Per Person Per Day");
        JCheckBox linesAddedAndDeletedChartCheckBox = new JCheckBox("Lines Added And Deleted Chart");
        JCheckBox commitsByContributorPieChartCheckBox = new JCheckBox("Commits By Contributor Chart");

        JButton cloneButton = new JButton("Clone");

        cloneButton.addActionListener(e -> {
            repositoryAddress = repository.getText();
            openOrCloneRepository(repositoryAddress);
            System.out.println("Repository Address: " + repositoryAddress);
        });

        JPanel topPanel = new JPanel(new GridLayout(8, 1));
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel centrePanel = new JPanel(new BorderLayout());
        topPanel.setLayout(new GridLayout(8, 1)); // 8 rows, 1 column
        centrePanel.setLayout(new GridLayout(1, 1));


        JPanel topLeftDrawingPanel = new JPanel();
        topLeftDrawingPanel.setBackground(Color.LIGHT_GRAY);

        JPanel topLeftButtonPanel = new JPanel(new FlowLayout());
        topLeftButtonPanel.setBackground(Color.DARK_GRAY);

        topRightPanel.setLayout(new BorderLayout());

        JPanel topRightDrawingPanel = new JPanel();
        topRightDrawingPanel.setBackground(Color.GRAY);

        JPanel topRightButtonPanel = new JPanel(new FlowLayout());
        topRightButtonPanel.setBackground(Color.DARK_GRAY);



        topRightPanel.add(topLeftButtonPanel, BorderLayout.SOUTH);
        topRightPanel.add(topLeftDrawingPanel, BorderLayout.CENTER);

        centrePanel.add(topRightPanel);


        topPanel.add(repository_label, BorderLayout.WEST);
        topPanel.add(repository, BorderLayout.CENTER);
        topPanel.add(cloneButton, BorderLayout.EAST);

        JPanel repositoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel keytokenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel localPathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        repository.setPreferredSize(new Dimension(600, 30));
        username.setPreferredSize(new Dimension(600, 30));
        keytoken.setPreferredSize(new Dimension(600, 30));
        local_path.setPreferredSize(new Dimension(600, 30));

        repositoryPanel.add(repository_label);
        repositoryPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        repositoryPanel.add(repository);

        usernamePanel.add(username_label);
        usernamePanel.add(Box.createRigidArea(new Dimension(71, 0)));
        usernamePanel.add(username);

        keytokenPanel.add(keytoken_label);
        keytokenPanel.add(Box.createRigidArea(new Dimension(70, 0)));
        keytokenPanel.add(keytoken);

        localPathPanel.add(local_path_label);
        localPathPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        localPathPanel.add(local_path);

        topPanel.setLayout(new GridLayout(5, 1));
        topPanel.add(repositoryPanel);
        topPanel.add(usernamePanel);
        topPanel.add(keytokenPanel);
        topPanel.add(localPathPanel);
        topPanel.add(cloneButton);

        JScrollPane scrollPane = new JScrollPane(consoleArea);
        centrePanel.add(scrollPane, BorderLayout.SOUTH);

        bottomPanel.add(lightIndicator, BorderLayout.EAST);

        leftPanel.add(commitsPerDayChartCheckBox);
        leftPanel.add(commitsByContributorPieChartCheckBox);
        leftPanel.add(commitsPerPersonPerDayChartCheckBox);
        leftPanel.add(linesAddedAndDeletedChartCheckBox);
        // Button to check the selected checkboxes
        JButton checkButton = new JButton("Check Selections");

        checkButton.addActionListener(e -> {
            consoleArea.append("Fetching Data for the Graphs\n");
            System.out.println("Fetching Data for the Graphs");
            SwingUtilities.invokeLater(() -> {
                int selectedCount = 0;
                // Check if each checkbox is selected
                if (commitsPerDayChartCheckBox.isSelected()) {
                    selectedCount++;
                }
                if (commitsByContributorPieChartCheckBox.isSelected()) {
                    selectedCount++;
                }
                if (commitsPerPersonPerDayChartCheckBox.isSelected()) {
                    selectedCount++;
                }
                if (linesAddedAndDeletedChartCheckBox.isSelected()) {
                    selectedCount++;
                }

                if (selectedCount > 1) {
                    consoleArea.append("Please choose only one chart.\n");
                } else if (selectedCount == 1) {
                    if (commitsPerDayChartCheckBox.isSelected()) {
                        System.out.println("Commits Per Day Chart has been selected");
                        consoleArea.append("Commits Per Day Chart is selected\n");
                        displayChartpercheckbox("Commits Per Day");
                    }
                    // You can repeat this for other checkboxes
                    if (commitsByContributorPieChartCheckBox.isSelected()) {
                        System.out.println("Commits Per Person Pie Chart has been selected");
                        consoleArea.append("Commits By Contributor Pie Chart is selected\n");
                        displayChartpercheckbox("Commits by Contributor");
                    }
                    if (commitsPerPersonPerDayChartCheckBox.isSelected()) {
                        System.out.println("Commits Per Person Pie Chart has been selected");
                        consoleArea.append("Commits By Person Pie Chart is selected\n");
                        displayChartpercheckbox("Commits Per Person Per Day");
                    }

                    if (linesAddedAndDeletedChartCheckBox.isSelected()) {
                        System.out.println("Lines Added and Deleted Chart is selected");
                        consoleArea.append("Lines Added and Deleted Chart is selected\n");
                        displayChartpercheckbox("Lines Added and Deleted Over Time");
                    }
                }
            });
        });
        leftPanel.add(checkButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.add(centrePanel, BorderLayout.CENTER);

        frame.setSize(1920, 1080);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void openOrCloneRepository(String repositoryAddress) {
        File repoDir = new File(repositoryAddress);
        outputToScreen("Cloning started...");

        if (repoDir.exists() && repoDir.isDirectory()) {
            // Try to open an existing repository
            try {
                // Open the existing repository
                outputToScreen("Opening existing repository at: " + repositoryAddress);
                System.out.println("Opening existing repository at: " + repositoryAddress);
                FileRepositoryBuilder builder = new FileRepositoryBuilder();
                Repository repository = builder.setGitDir(new File(repoDir, ".git"))
                        .readEnvironment()
                        .findGitDir()
                        .build();

                outputToScreen("Opened repository: " + repository.getDirectory());

            } catch (IOException e) {
                outputToScreen("Error opening repository: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Failed to open repository: " + e.getMessage());
                consoleArea.append("Error cloning the repository: " + e.getMessage());

            }
        } else {
            // Clone a repository if the path is not an existing repository
            try {
                System.out.println("Cloning repository from URL: " + repositoryAddress);

// Retrieve the values from the text fields
                String usernameValue = username.getText().trim();
                String keyTokenValue = keytoken.getText().trim();
                String localPathValue = local_path.getText().trim();

                // Validate input values
                if (usernameValue.isEmpty() || keyTokenValue.isEmpty() || localPathValue.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields (username, token, and path) must be filled.");
                    return;
                }
                //Password has to be your token
                UsernamePasswordCredentialsProvider credentialsProvider =
                        new UsernamePasswordCredentialsProvider(usernameValue, keyTokenValue);

                //String localDirectory = JOptionPane.showInputDialog("Enter local directory to clone into:");
                if (!localPathValue.trim().isEmpty()) {
                    File localDir = new File(localPathValue);
                    if (!localDir.exists()) {
                        localDir.mkdirs();
                    }
                    Git.cloneRepository()
                            .setURI(repositoryAddress)
                            .setDirectory(localDir)
                            .setCredentialsProvider(credentialsProvider) // Set the credentials provider
                            .call();

                    JOptionPane.showMessageDialog(null, "Repository cloned successfully to: " + localPathValue);
                    System.out.println("Repository cloned successfully.");
                    username.setText("");
                    keytoken.setText("");
                    local_path.setText("");
                    lightIndicator.setColor(Color.GREEN);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid local directory.");
                }
            } catch (Exception e) {
                outputToScreen("Error cloning repository: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Failed to clone repository: " + e.getMessage());
            }
        }

    }

    private static void storeDataToCSV() {
        String repoPath = System.getProperty("user.dir");
        DataCollector collector = new DataCollector(repoPath);
        List<CommitData> commitDataList = collector.collectCommitData();

        Map<String, ContributorMetrics> contributorMetricsMap = ContributorMetrics.organizeByContributors(commitDataList);

        List<ContributorMetrics> topContributorsByCommits = ContributorMetrics.sortContributorsByMetric(contributorMetricsMap, "commits");
        List<ContributorMetrics> topContributorsByLinesAdded = ContributorMetrics.sortContributorsByMetric(contributorMetricsMap, "linesadded");
        List<ContributorMetrics> topContributorsByLinesDeleted = ContributorMetrics.sortContributorsByMetric(contributorMetricsMap, "linesdeleted");

        List<CommitData> pairProgrammingCommits = PairProgrammingDetector.detectPairProgrammingCommits(commitDataList);

        // Generate charts
        Map<String, Integer> commitsPerDay = DataCollector.getCommitsPerDay(commitDataList);
        Map<String, Map<String, Integer>> commitsPerPersonPerDay = DataCollector.getCommitsPerPersonPerDay(commitDataList);
        Map<String, int[]> linesPerDay = DataCollector.getLinesAddedAndDeletedPerDay(commitDataList);
        Map<String, Integer> commitsByContributor = DataCollector.getCommitsByContributor(commitDataList);

        JFreeChart commitsPerDayChart = LineChartGenerator.createCommitsPerDayChart(commitsPerDay);
        JFreeChart commitsPerPersonPerDayChart = LineChartGenerator.createCommitsPerPersonPerDayChart(commitsPerPersonPerDay);
        JFreeChart linesAddedAndDeletedChart = LineChartGenerator.createLinesAddedAndDeletedChart(linesPerDay);
        JFreeChart commitsByContributorPieChart = PieChartGenerator.createCommitsByContributorPieChart(commitsByContributor);
        // Add charts to a list
        List<JFreeChart> charts = List.of(
                commitsPerDayChart,
                commitsPerPersonPerDayChart,
                linesAddedAndDeletedChart,
                commitsByContributorPieChart
        );

        Map<String, Integer> driverFrequency = PairProgrammingSummary.getDriverFrequencies(pairProgrammingCommits);
        Map<String, Integer> navigatorFrequency = PairProgrammingSummary.getNavigatorFrequencies(pairProgrammingCommits);
        Map<String, Integer> pairFrequency = PairProgrammingSummary.getPairFrequencies(pairProgrammingCommits);
        String filePath = "contributors.csv";
        String filePathPDF = "contributor_collaboration_metrics.pdf";

        CSVExporter.exportToCSV(filePath, topContributorsByCommits, topContributorsByLinesAdded, topContributorsByLinesDeleted);
        PDFExporter.exportToPDF(
                filePathPDF,
                topContributorsByCommits,
                topContributorsByLinesAdded,
                topContributorsByLinesDeleted,
                driverFrequency,
                navigatorFrequency,
                pairFrequency,
                charts
        );
    }

    public static void outputToScreen(String message) {
        consoleArea.append(message + '\n');
        Font currentFont = consoleArea.getFont();
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), 13);
        consoleArea.setFont(newFont);
        consoleArea.update(consoleArea.getGraphics());
        frame.repaint();
    }
    private static void displayChartpercheckbox(String chartname) {
        try {
            // Step 1: Collect commit data

            String repoPath = System.getProperty("user.dir");
            DataCollector dataCollector = new DataCollector(repoPath);

            List<CommitData> commitDataList = dataCollector.collectCommitData();

            // Step 2: Generate Line Charts (Existing Charts)
            Map<String, Integer> commitsPerDay = DataCollector.getCommitsPerDay(commitDataList);
            JFreeChart commitsPerDayChart = LineChartGenerator.createCommitsPerDayChart(commitsPerDay);

            Map<String, Map<String, Integer>> commitsPerPersonPerDay = DataCollector.getCommitsPerPersonPerDay(commitDataList);
            JFreeChart commitsPerPersonPerDayChart = LineChartGenerator.createCommitsPerPersonPerDayChart(commitsPerPersonPerDay);

            Map<String, int[]> linesPerDay = DataCollector.getLinesAddedAndDeletedPerDay(commitDataList);
            JFreeChart linesAddedAndDeletedChart = LineChartGenerator.createLinesAddedAndDeletedChart(linesPerDay);

            // Step 3: Generate Pie Chart (New Chart)
            Map<String, Integer> commitsByContributor = DataCollector.getCommitsByContributor(commitDataList);
            JFreeChart commitsByContributorPieChart = PieChartGenerator.createCommitsByContributorPieChart(commitsByContributor);


            Map<String, JFreeChart> chartMap = new HashMap<>();
            chartMap.put("Commits Per Day", commitsPerDayChart);
            chartMap.put("Commits Per Person Per Day", commitsPerPersonPerDayChart);
            chartMap.put("Lines Added and Deleted Over Time", linesAddedAndDeletedChart);
            chartMap.put("Commits by Contributor", commitsByContributorPieChart);

            JFreeChart selectedChart = null;

            if (chartMap.containsKey(chartname)) {
                selectedChart = chartMap.get(chartname);
                System.out.println("The chart title matches the provided chart name: " + chartname);
            } else {
                System.out.println("The chart title does not match the provided chart name.");
            }

            ChartPanel chartPanel = new ChartPanel(selectedChart);
            chartPanel.setMouseWheelEnabled(true);

            // Clear any previous content and add the new chart
            topRightPanel.removeAll();
            topRightPanel.add(chartPanel, BorderLayout.CENTER);

            // Refresh the UI to show the chart
            topRightPanel.revalidate();
            topRightPanel.repaint();

        } catch (Exception e) {
            // Handle any exceptions gracefully
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private static void inspectCode(){
        CodeInspection insp = new CodeInspection();
        String filePath = JOptionPane.showInputDialog("Enter absolute path of src directory");

        ArrayList<String> filePaths = insp.getFilePath(filePath);

        for(String path : filePaths){
            insp.checkClassNames(path);
            if(CodeInspection.ClassNamingVisitor.classFlag)
                consoleArea.append("No invalid class name has been found " +
                        "in file '" + path + "'. Well done!\n");

        }
        consoleArea.append("\n");
        consoleArea.append("\n");
        consoleArea.append("\n");


        for(String path : filePaths){
            insp.checkMethodNames(path);
            if(CodeInspection.MethodNamingVisitor.methodFlag)
                consoleArea.append("No invalid method name has been found " +
                        "in file '" + path + "'. Well done!\n");
        }

        consoleArea.append("\n");
        consoleArea.append("\n");
        consoleArea.append("\n");

        for(String path : filePaths){
            insp.checkVariableNames(path);
            consoleArea.append(insp.getConsole().getText());
        }
        consoleArea.append("\n");
        consoleArea.append("\n");
        consoleArea.append("\n");

        for(String path : filePaths){
            if(insp.calculateComplexityScore(path) > 50){
                consoleArea.append("\nComplexity score of file " + path + " is of " +
                        insp.calculateComplexityScore(path) + ". We suggest refactoring the file.");
            }
            else{
                consoleArea.append("\nComplexity score of file " + path + " is of " + insp.calculateComplexityScore(path) + ".");
            }
        }

        consoleArea.append("\n");
        consoleArea.append("\n");
        consoleArea.append("\n");



        for (int i = 0; i < insp.getFilePath(filePath).size(); i++) {
            consoleArea.append("Number of lines in file " + insp.getFilePath(filePath).get(i) + ":" + insp.getNumberOfLines(new File(insp.getFilePath(filePath).get(i)))+"\n");
        }

    }

}
