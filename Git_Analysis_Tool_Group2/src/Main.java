
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        //CodeInspection inspection = new CodeInspection();

        CodeInspection insp = new CodeInspection();
        ArrayList<String> filePaths = insp.getFilePath("Git_Analysis_Tool_Group2/src");

        for(String path : filePaths){
            insp.checkClassNames(path);
            if(CodeInspection.ClassNamingVisitor.classFlag)
                System.out.println("No invalid class name has been found " +
                        "in file '" + path + "'. Well done!\n");

        }
        System.out.println();
        System.out.println();
        System.out.println();

        for(String path : filePaths){
            insp.checkMethodNames(path);
            if(CodeInspection.MethodNamingVisitor.methodFlag)
                System.out.println("No invalid method name has been found " +
                        "in file '" + path + "'. Well done!\n");
        }

        System.out.println();
        System.out.println();
        System.out.println();

        for(String path : filePaths){
            insp.checkVariableNames(path);
            if(CodeInspection.VariableNamingVisitor.variableFlag){
                System.out.println("No invalid variable name has been found " +
                        "in file '" + path + "'. Well done!\n");
            }
        }
        System.out.println();
        System.out.println();
        System.out.println();

        for(String path : filePaths){
            if(insp.calculateComplexityScore(path) > 50){
                System.out.println("\nComplexity score of file " + path + " is of " +
                        insp.calculateComplexityScore(path) + ". We suggest refactoring the file.");
            }
            else{
                System.out.println("\nComplexity score of file " + path + " is of " + insp.calculateComplexityScore(path) + ".");
            }
        }
    }
}

