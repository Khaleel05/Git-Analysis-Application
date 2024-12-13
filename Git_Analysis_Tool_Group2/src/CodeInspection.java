import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.*;

import javax.swing.*;

public class CodeInspection {
    private static final String CLASS_NAME_REGEX = "[A-Z][a-zA-Z0-9]*";
    private static final String VARIABLE_NAME_REGEX = "^[a-z][a-zA-Z0-9]*$";
    private static final String METHOD_NAME_REGEX = "^[a-z][a-zA-Z0-9]*$";
    public static final JTextArea console = new JTextArea();

    public class VariableNamingVisitor extends ASTVisitor {
        private String filePath;
        private static final String VARIABLE_NAME_REGEX = "[a-z][a-zA-Z0-9]*"; // example pattern
        // No need for static flag, checking per variable is fine
        static boolean variableFlag = false;

        public VariableNamingVisitor(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public boolean visit(VariableDeclarationFragment node) {
            String varName = node.getName().getIdentifier();
            if (!varName.matches(VARIABLE_NAME_REGEX)) {
                console.append("Incorrect variable name in file " + filePath + ": " + varName + "\n");
            } else {
                variableFlag = true;  // flag that variable name is correct
            }
            return super.visit(node);
        }
    }
    public JTextArea getConsole() {
        return console;
    }
    static class MethodNamingVisitor extends ASTVisitor {
        private String filePath;
        static boolean methodFlag = false;


        public MethodNamingVisitor(String filePath) {
            this.filePath = filePath;
        }

        // This method is called when a method declaration is encountered
        @Override
        public boolean visit(MethodDeclaration node) {

            String methodName = node.getName().getIdentifier();


            TypeDeclaration classDeclaration = (TypeDeclaration) node.getParent();
            String className = classDeclaration.getName().getIdentifier();


            if (methodName.equals(className) && node.getReturnType2() == null) {

                return true;
            }


            if (!methodName.matches(METHOD_NAME_REGEX)) {
                System.out.println("Incorrect method name in file " + filePath + ": " + methodName + "\n");
            } else {
                methodFlag = true;
            }

            return true;
        }
    }


    static class ClassNamingVisitor extends ASTVisitor {
        private String filePath;
        static boolean classFlag = false;

        public ClassNamingVisitor(String filePath) {
            this.filePath = filePath;
        }


        @Override
        public boolean visit(TypeDeclaration node) {
            String className = node.getName().getIdentifier();

            if (!className.matches(CLASS_NAME_REGEX)) {
                System.out.println("Incorrect class name in file " + filePath + ": " + className + "\n");
            } else {
                classFlag = true;
            }

            return true;
        }
    }




    public int getNumberOfLines(File file) {
        int lines = 0;
        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(file));
            while (lineReader.readLine() != null) lines++;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines;
    }


    //Comment

    public ArrayList<String> getFilePath(String fileName) {
        ArrayList<String> filePaths = new ArrayList<>();
        File dir = new File(fileName);
        try {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.toString().endsWith(".java")) {
                        filePaths.add(file.getAbsolutePath());
                    }
                }
            } else {
                System.out.println("Directory is empty or not accessible: " + dir.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePaths;
    }


    public void checkVariableNames(String filePath) {
        try {

            String source = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);


            ASTParser parser = ASTParser.newParser(AST.JLS21);
            parser.setSource(source.toCharArray());
            parser.setKind(ASTParser.K_COMPILATION_UNIT);

            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);

            VariableNamingVisitor visitor = new VariableNamingVisitor(filePath);
            compilationUnit.accept(visitor);
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            e.printStackTrace();
        }
    }


    public void checkMethodNames(String filePath) {
        try {

            String source = Files.readString(Paths.get(filePath));


            ASTParser parser = ASTParser.newParser(AST.JLS21);
            parser.setSource(source.toCharArray());
            parser.setKind(ASTParser.K_COMPILATION_UNIT);

            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);

            MethodNamingVisitor visitor = new MethodNamingVisitor(filePath);
            compilationUnit.accept(visitor);
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            e.printStackTrace();
        }
    }


    public void checkClassNames(String filePath) {
        try {

            String source = Files.readString(Paths.get(filePath));

            ASTParser parser = ASTParser.newParser(AST.JLS21);
            parser.setSource(source.toCharArray());
            parser.setKind(ASTParser.K_COMPILATION_UNIT);

            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);

            ClassNamingVisitor visitor = new ClassNamingVisitor(filePath);
            compilationUnit.accept(visitor);
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            e.printStackTrace();
        }
    }

    public int calculateComplexityScore(String filePath) {
        int complexityScore = 0;

        // 1. Check Class Naming Convention
        ClassNamingVisitor classNamingVisitor = new ClassNamingVisitor(filePath);
        checkClassNames(filePath);
        if (ClassNamingVisitor.classFlag) {
            complexityScore += 10; // Add points if class name is correct
        }

        // 2. Check Method Naming Convention
        MethodNamingVisitor methodNamingVisitor = new MethodNamingVisitor(filePath);
        checkMethodNames(filePath);
        if (MethodNamingVisitor.methodFlag) {
            complexityScore += 10; // Add points if method names are correct
        }

        // 3. Check Variable Naming Convention
        VariableNamingVisitor variableNamingVisitor = new VariableNamingVisitor(filePath);
        checkVariableNames(filePath);
        if (VariableNamingVisitor.variableFlag) {
            complexityScore += 10; // Add points if variable names are correct
        }

        // 4. Count Lines of Code
        int numberOfLines = getNumberOfLines(new File(filePath));
        complexityScore += numberOfLines / 10; // Example: every 10 lines adds 1 to complexity

        // 5. Count Number of Methods
        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            ASTParser parser = ASTParser.newParser(AST.JLS21);
            parser.setSource(source.toCharArray());
            parser.setKind(ASTParser.K_COMPILATION_UNIT);
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);

            MethodCountVisitor methodCountVisitor = new MethodCountVisitor();
            compilationUnit.accept(methodCountVisitor);
            int methodCount = methodCountVisitor.getMethodCount();
            complexityScore += methodCount * 5; // Example: every method adds 5 to complexity
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 6. Estimate Cyclomatic Complexity (basic estimation)
        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            ASTParser parser = ASTParser.newParser(AST.JLS21);
            parser.setSource(source.toCharArray());
            parser.setKind(ASTParser.K_COMPILATION_UNIT);
            CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);

            CyclomaticComplexityVisitor cyclomaticComplexityVisitor = new CyclomaticComplexityVisitor();
            compilationUnit.accept(cyclomaticComplexityVisitor);
            int cyclomaticComplexity = cyclomaticComplexityVisitor.getCyclomaticComplexity();
            complexityScore += cyclomaticComplexity * 2; // Example: each independent path adds 2 to complexity
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(complexityScore > 100){
            complexityScore = 100;
        }

        return complexityScore;
    }

    // Helper class for counting methods in the class
    static class MethodCountVisitor extends ASTVisitor {
        private int methodCount = 0;

        @Override
        public boolean visit(MethodDeclaration node) {
            methodCount++;
            return super.visit(node);
        }

        public int getMethodCount() {
            return methodCount;
        }
    }

    // Helper class for estimating cyclomatic complexity
    static class CyclomaticComplexityVisitor extends ASTVisitor {
        private int complexity = 0;

        @Override
        public boolean visit(IfStatement node) {
            complexity++;
            return super.visit(node);
        }

        @Override
        public boolean visit(WhileStatement node) {
            complexity++;
            return super.visit(node);
        }

        @Override
        public boolean visit(ForStatement node) {
            complexity++;
            return super.visit(node);
        }

        @Override
        public boolean visit(SwitchStatement node) {
            complexity++;
            return super.visit(node);
        }

        public int getCyclomaticComplexity() {
            return complexity;
        }
    }

}
