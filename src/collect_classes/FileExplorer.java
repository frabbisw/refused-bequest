package collect_classes;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import manage_classes.ClassObjectHandler;
import manage_classes.InherObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileExplorer
{
    Map <String, ArrayList<ClassObjectHandler>> packageMap;
    Map <String, ArrayList<ClassObjectHandler>> sourceMap;
    Set <ClassObjectHandler> setOfClasses;

    public FileExplorer(File rootFile)
    {
        packageMap = new TreeMap<>();
        sourceMap = new TreeMap<>();
        setOfClasses=new TreeSet<>();

        browseClasses(rootFile);
    }
    public Map<String, ArrayList<ClassObjectHandler>> getPackageMap()
    {
        return packageMap;
    }
    public Map<String, ArrayList<ClassObjectHandler>> getSourceMap() {
        return sourceMap;
    }

    public void browseClasses(File rootFile) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            String src = file.getParent();
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(CompilationUnit n, Object arg) {
                        super.visit(n, arg);

                        String packName=null;
                        if(n.getPackageDeclaration().isPresent())
                        {
                            packName=n.getPackageDeclaration().get().getNameAsString();
                            includeToPackageMap(src,packName, n);
                            includeToSourceMap(src, n);
                        }
                    }
                }.visit(JavaParser.parse(file), null);
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(rootFile);
    }

    private void includeToSourceMap(String src, CompilationUnit n) {
        if(!sourceMap.containsKey(src))
            sourceMap.put(src, new ArrayList<>());
        for(TypeDeclaration type : n.getTypes()) {
            if(n.getClassByName(type.getNameAsString()).isPresent()) {
                ClassObjectHandler classObjectHandler = new ClassObjectHandler(n.getPackageDeclaration().get(), n.getClassByName(type.getNameAsString()).get());
                classObjectHandler.setImports(n.getImports());
                classObjectHandler.setFilePath(src);
                sourceMap.get(src).add(classObjectHandler);
            }
            else if(n.getInterfaceByName(type.getNameAsString()).isPresent()) {
                ClassObjectHandler classObjectHandler = new ClassObjectHandler(n.getPackageDeclaration().get(), n.getInterfaceByName(type.getNameAsString()).get());
                classObjectHandler.setImports(n.getImports());
                classObjectHandler.setFilePath(src);
                sourceMap.get(src).add(classObjectHandler);
            }
        }
    }

    private void includeToPackageMap(String src, String packName, CompilationUnit n) {
        if(!packageMap.containsKey(packName))
            packageMap.put(packName, new ArrayList<>());

        for(TypeDeclaration type : n.getTypes()) {
            ClassObjectHandler classObjectHandler =null;
            if(n.getClassByName(type.getNameAsString()).isPresent())
                classObjectHandler = new ClassObjectHandler(n.getPackageDeclaration().get(), n.getClassByName(type.getNameAsString()).get());

            else if(n.getInterfaceByName(type.getNameAsString()).isPresent())
                classObjectHandler = new ClassObjectHandler(n.getPackageDeclaration().get(), n.getInterfaceByName(type.getNameAsString()).get());

            if(classObjectHandler !=null)
            {
                classObjectHandler.setImports(n.getImports());
                classObjectHandler.setFilePath(src);
                packageMap.get(packName).add(classObjectHandler);

                packageMap.put(classObjectHandler.getFullName(),new ArrayList<>());
                packageMap.get(classObjectHandler.getFullName()).add(classObjectHandler);
                setOfClasses.add(classObjectHandler);
            }
        }
    }
    public ArrayList<ClassObjectHandler> getClassesByImportTag(String packageName)
    {
        if(packageMap.containsKey(packageName))
            return packageMap.get(packageName);

        return null;
    }
    public ArrayList<ClassObjectHandler> getClassesBySource(String path)
    {
        if(sourceMap.containsKey(path))
            return sourceMap.get(path);
        return null;
    }
    public ArrayList<ClassObjectHandler>getListOfClasses()
    {
        ArrayList<ClassObjectHandler>listOfClasses=new ArrayList<>();
        listOfClasses.addAll(setOfClasses);
        return listOfClasses;
    }
}