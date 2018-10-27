package manage_collaboration;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import manage_inheritence.InherManager;

import java.util.*;

public class ClassCollaborator {
    Map<String,String>classesMap=new TreeMap<>();
    ClassObjectHandler classObjectHandler;
    InherManager inherManager;

    public ClassCollaborator(ClassObjectHandler classObjectHandler, InherManager inherManager)
    {
        this.classObjectHandler=classObjectHandler;
        this.inherManager=inherManager;

        ArrayList<ClassObjectHandler>accesedList=new ArrayList<>();
        accesedList.addAll(classObjectHandler.getAccessedClassesList());

        for(ClassObjectHandler foreign : accesedList)
        {
            classesMap.put(foreign.getClassName(), foreign.getFullName());
        }

        startActions();
    }
    private void startActions() {
        Set<String> globalSet=new HashSet<>();
        ClassOrInterfaceDeclaration clazz = classObjectHandler.clazz;
        String classNameWithPackage = classObjectHandler.getFullName();
        Map<String, String> globalMap = new TreeMap<>();

        for (FieldDeclaration field : clazz.getFields())
            for (VariableDeclarator variable : field.getVariables()) {
                globalSet.add(variable.getNameAsString());

                if(classesMap.containsKey(variable.getType().asString())) {
                    globalMap.put(variable.getNameAsString(), variable.getType().asString());
                }
            }


        for (String string : classesMap.keySet())
            globalMap.put(string, string);

        for (MethodDeclaration method : clazz.getMethods())
        {
            MethodManager methodManager = new MethodManager(method, classObjectHandler, globalMap, classesMap, inherManager);
        }

    }
}
