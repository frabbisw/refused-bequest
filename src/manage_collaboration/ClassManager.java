package manage_collaboration;

import collect_classes.ClassFinder;
import collect_classes.FileExplorer;
import manage_inheritence.InherManager;
import manage_inheritence.InherObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ClassManager {
    FileExplorer explorer;
    InherManager inherManager;
    ArrayList<ClassObjectHandler>classObjectHandlers;
    String outputPath;
    public ClassManager(String outputFolder)
    {
        this.outputPath=outputFolder+"/refusedBequest.csv";
        explorer=ClassFinder.getClassExplorer();
        inherManager=new InherManager();
        classObjectHandlers=inherManager.getAllHandlers();

        collaborate();
        prepareRefusedBequests();
    }

    private void prepareRefusedBequests() {
        String out="";
        out+="Class Name;";
        out+="Unused Parents Attributes;";
        out+="Unused Parents Methods \n";

        for (InherObject object : inherManager.getAllInherObjects())
        {
            out+=object.getFullName();
            out+=";";
            out+=object.getUnusedParentAttributes();
            out+=";";
            out+=object.getUnusedParentMethods();
            out+="\n";
        }

        System.out.println(out);

        printCSV(out);
    }

    private void printCSV(String out) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
            writer.write(out);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void collaborate() {
        for(ClassObjectHandler handler : classObjectHandlers)
        {
            ClassCollaborator collaborator = new ClassCollaborator(handler, inherManager);
        }
    }

    public static void main(String[] args) {
        ClassFinder.setProjectPath("/home/rabbi/bin/samples/effective-java-examples-master");

        ClassManager classManager = new ClassManager("/home/rabbi/bin/samples/output");
    }
}