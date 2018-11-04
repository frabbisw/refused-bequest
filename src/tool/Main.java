package tool;

public class Main {
    public static void main(String [] args)
    {
        String projectPath="/home/rabbi/bin/samples/effective-java-examples-master";
        String outputFolder="/home/rabbi/bin/samples/output";
        RefusedBequest refusedBequest = new RefusedBequest(projectPath, outputFolder);
    }
}
