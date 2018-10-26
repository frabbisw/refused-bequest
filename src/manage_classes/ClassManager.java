package manage_classes;

import collect_classes.ClassFinder;
import collect_classes.FileExplorer;

import java.util.ArrayList;

public class ClassManager {
    FileExplorer explorer;
    public ClassManager()
    {
        explorer=ClassFinder.getClassExplorer();
        prepareClasses();
    }
    private void prepareClasses() {
        for(ClassObjectHandler object : explorer.getListOfClasses())
        {
            object.prepareInheritenceMap(explorer);
        }
    }
    public ArrayList<ClassObjectHandler>getAllClasses()
    {
        return explorer.getListOfClasses();
    }
    /*
    public static void main(String[] args) {
        ClassFinder.setProjectPath("/home/rabbi/bin/samples/effective-java-examples-master");
        //ClassFinder.setProjectPath("/home/rabbi/bin/samples/hablu");

        ClassManager classManager = new ClassManager();
        for(ClassObjectHandler object : classManager.getAllClasses())
        {
            System.out.println(object.getFullName());
            for(ClassObjectHandler fob : object.getInheritedClasses())
            {
                System.out.println("\t"+fob.getFullName());
            }
        }
    }
    */
}
