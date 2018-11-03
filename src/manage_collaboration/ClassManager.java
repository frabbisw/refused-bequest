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
        out+="Class Name,";
        out+="Inherited Attributes,";
        out+="Unused Attributes,";
        out+="Used Attributes Percentage,";
        out+="Inherited Methods,";
        out+="Unused Methods,";
        out+="Used Methods Percentage\n";

        double tpas=0,tuas=0,tpms=0,tums=0;
        for (InherObject object : inherManager.getAllInherObjects())
        {
            double pas = object.getParentsAttributes().size();
            double uas = object.getUnusedParentAttributes().size();
            double pms = object.getParentsMethods().size();
            double ums = object.getUnusedParentMethods().size();

            out+=object.getFullName()+", ";
            out+=pas+", ";
            out+=uas+", ";
            out+=100-100*uas/pas+", ";
            out+=pms+", ";
            out+=ums+", ";
            out+=100-100*ums/pms+", ";
            out+="\n";

            tpas+=pas;
            tuas+=uas;
            tpms+=pms;
            tums+=ums;
        }

        out+="Total, ";
        out+=tpas+", ";
        out+=tuas+", ";
        out+=100-100*tuas/tpas+", ";
        out+=tpms+", ";
        out+=tums+", ";
        out+=100-100*tums/tpms+", ";
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
        String projectPath="/home/rabbi/bin/samples/effective-java-examples-master";
        String outputFolder="/home/rabbi/bin/samples/output";

        ClassFinder.setProjectPath(projectPath);
        ClassManager classManager = new ClassManager(outputFolder);
    }
}