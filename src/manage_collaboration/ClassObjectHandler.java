package manage_collaboration;

import collect_classes.FileExplorer;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import manage_inheritence.InherObject;
import utils.NameMaker;

import java.util.*;

public class ClassObjectHandler implements Comparable<ClassObjectHandler> {
    private String packageName;
    private String className;
    private String fullName;
    private String filePath;
    private ArrayList<String>imports;
    private Set<ClassObjectHandler> accessedClassesList;
    private ArrayList<ClassObjectHandler> inheritedClasses;
    public ClassOrInterfaceDeclaration clazz;

    InherObject classObject;

    public ClassObjectHandler(PackageDeclaration declaration, ClassOrInterfaceDeclaration clazz)
    {
        imports=new ArrayList<>();
        accessedClassesList =new TreeSet<>();
        inheritedClasses=new ArrayList<>();

        className=clazz.getNameAsString();
        this.clazz=clazz;

        this.packageName = declaration.getNameAsString();
        fullName= NameMaker.makeFullName(packageName,className);

        createInherObject();
        addAttributes(clazz);
        addMethods(clazz);
    }

    private void createInherObject() {
        classObject = new InherObject(getFullName());
    }

    private void prepareInherObject()
    {
        classObject.setInheritedObjects(inheritedClasses);
    }

    private void addMethods(ClassOrInterfaceDeclaration clazz) {
        ArrayList<String>methods=new ArrayList<>();
        for(MethodDeclaration declaration : clazz.getMethods())
            methods.add(declaration.getNameAsString());
        classObject.setOwnMethods(methods);
    }

    private void addAttributes(ClassOrInterfaceDeclaration clazz) {
        ArrayList<String>attributes=new ArrayList<>();
        for (FieldDeclaration field : clazz.getFields())
            for (VariableDeclarator variable : field.getVariables()) {
                attributes.add(variable.getNameAsString());
            }
        classObject.setOwnAttributes(attributes);
    }

    public String getPackageName() {
        return packageName;
    }

    public void setFilePath(String filePath)
    {
        this.filePath=filePath;
    }
    public String getFilePath(){return filePath;}

    public String getClassName() {
        return className;
    }

    public String  getFullName() {
        return fullName;
    }

    public ArrayList<String> getImports() {
        return imports;
    }

    public void setImports(List<ImportDeclaration>declarations) {
        for (ImportDeclaration declaration : declarations)
        {
            imports.add(declaration.getNameAsString());
        }
    }
    @Override
    public int compareTo(ClassObjectHandler classObjectHandler) {
        return this.fullName.compareTo(classObjectHandler.getFullName());
    }
    private void collectAccessedClassed(FileExplorer explorer) {
        accessedClassesList.addAll(explorer.getClassesBySource(getFilePath()));
        for(String immport : getImports())
        {
            try {
                accessedClassesList.addAll(explorer.getClassesByImportTag(immport));
            }
            catch (NullPointerException e) {}
        }
    }
    public void prepareInheritenceMap(FileExplorer explorer) {
        collectAccessedClassed(explorer);

        Map<String, ClassObjectHandler>accessedClassesMap=new TreeMap<>();
        for(ClassObjectHandler obj : accessedClassesList)
        {
            accessedClassesMap.put(obj.getClassName(), obj);
        }

        for(ClassOrInterfaceType type : clazz.getExtendedTypes())
        {
            if(accessedClassesMap.containsKey(type.getNameAsString()))
                inheritedClasses.add(accessedClassesMap.get(type.getNameAsString()));
        }

        for(ClassOrInterfaceType type : clazz.getImplementedTypes())
        {
            if(accessedClassesMap.containsKey(type.getNameAsString()))
                inheritedClasses.add(accessedClassesMap.get(type.getNameAsString()));
        }

        prepareInherObject();
    }

    public ArrayList<ClassObjectHandler>getInheritedClasses()
    {
        if(inheritedClasses==null)
            return new ArrayList<ClassObjectHandler>();
        return inheritedClasses;
    }

    public InherObject getInherObject()
    {
        return classObject;
    }

    public Set<ClassObjectHandler> getAccessedClassesList() {
        return accessedClassesList;
    }
}