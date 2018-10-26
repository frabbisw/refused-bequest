package collect_classes;

import java.io.File;
import java.util.Map;

public class ClassFinder {
    static FileExplorer explorer;
    static String projectPath;

    public static void setProjectPath(String path)
    {
        projectPath=path;
        explorer=new FileExplorer(new File(projectPath));

    }
    public static FileExplorer getClassExplorer()
    {
        return explorer;
    }
}