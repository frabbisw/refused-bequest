package manage_collaboration;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ClassCollaborator {
    Map<String,String>classesMap=new TreeMap<>();
    ClassObjectHandler classObjectHandler;
    public ClassCollaborator(ClassObjectHandler classObjectHandler)
    {
        this.classObjectHandler=classObjectHandler;

        ArrayList<ClassObjectHandler>accesedList=new ArrayList<>();
        accesedList.addAll(classObjectHandler.getAccessedClassesList());

        for(ClassObjectHandler foreign : accesedList)
        {
            classesMap.put(foreign.getClassName(), foreign.getFullName());
        }
    }
    public void startActions()
    {
        ClassOrInterfaceDeclaration clazz = classObjectHandler.clazz;

    }
}
