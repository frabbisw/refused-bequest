package tool;

import collect_classes.ClassFinder;
import manage_collaboration.ClassManager;

public class RefusedBequest {
    public RefusedBequest(String projectPath, String outputFolder)
    {
        ClassFinder.setProjectPath(projectPath);
        ClassManager classManager = new ClassManager(outputFolder);
    }
}
