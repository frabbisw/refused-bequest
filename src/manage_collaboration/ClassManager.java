package manage_collaboration;

import collect_classes.ClassFinder;
import collect_classes.FileExplorer;
import manage_inheritence.InherManager;

import java.util.ArrayList;

public class ClassManager {
    FileExplorer explorer;
    InherManager inherManager;
    ArrayList<ClassObjectHandler>classObjectHandlers;
    public ClassManager()
    {
        explorer=ClassFinder.getClassExplorer();
        inherManager=new InherManager();
        classObjectHandlers=inherManager.getAllHandlers();

        collaborate();
    }
    private void collaborate() {
        for(ClassObjectHandler handler : classObjectHandlers)
        {
            ClassCollaborator collaborator = new ClassCollaborator(handler);
        }
    }

    public static void main(String[] args) {
        ClassFinder.setProjectPath("/home/rabbi/bin/samples/effective-java-examples-master");
        //ClassFinder.setProjectPath("/home/rabbi/bin/samples/hablu");

        ClassManager classManager = new ClassManager();
    }
}
