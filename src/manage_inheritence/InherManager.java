package manage_inheritence;

import collect_classes.ClassFinder;
import collect_classes.FileExplorer;
import manage_collaboration.ClassObjectHandler;

import java.util.*;

public class InherManager {
    FileExplorer explorer;
    ArrayList<InherObject> inherObjects;
    private Map<String, InherObject> inherMap;

    public InherManager()
    {
        explorer= ClassFinder.getClassExplorer();
        inherObjects=new ArrayList<>();
        inherMap =new TreeMap<>();
        prepareClasses();
    }
    private void prepareClasses() {
        for(ClassObjectHandler handler : explorer.getListOfClasses())
            handler.prepareInheritenceMap(explorer);

        for (ClassObjectHandler objectHandler : explorer.getListOfClasses())
            inherMap.put(objectHandler.getInherObject().getFullName(), objectHandler.getInherObject());

        prepareInheriTree();
    }
    private void dfs(InherObject inherObject)
    {
        inherObject.visit();

        for(InherObject tmp : inherObject.getInheritedObjects())
        {
            InherObject parent = inherMap.get(tmp.getFullName());
            if(!parent.isVisited())
            {
                dfs(parent);
            }

            inherObject.addParentsAttributes(parent.getOwnAttributes());
            inherObject.addParentsMethods(parent.getOwnMethods());

            inherObject.addParentsAttributes(parent.getParentsAttributes());
            inherObject.addParentsMethods(parent.getParentsMethods());
        }
    }
    private void prepareInheriTree()
    {
        for(String name : inherMap.keySet())
            if(!inherMap.get(name).isVisited())
                dfs(inherMap.get(name));

        for(String name : inherMap.keySet())
            inherObjects.add(inherMap.get(name));
    }
    public ArrayList<ClassObjectHandler> getAllHandlers()
    {
        return explorer.getListOfClasses();
    }
    public Collection<InherObject> getAllInherObjects()
    {
        return this.inherObjects;
    }

    public static void main(String[] args) {
        //ClassFinder.setProjectPath("/home/rabbi/bin/samples/effective-java-examples-master");
        ClassFinder.setProjectPath("/home/rabbi/bin/samples/hablu");

        InherManager manager = new InherManager();

        System.out.println();

        for(InherObject object: manager.getAllInherObjects())
        {
            System.out.println(object.getFullName());
            //System.out.println("\t"+object);
            System.out.println("\t"+object.getOwnMethods());
            System.out.println("\t"+object.getParentsMethods());
        }
    }
    public void addUsedAttribute(String className, String attribute)
    {
        inherMap.get(className).addUsedAttribute(attribute);
    }
    public void addUsedMethod(String className, String method)
    {
        inherMap.get(className).addUsedMethod(method);
    }
}
