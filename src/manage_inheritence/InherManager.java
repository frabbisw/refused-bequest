package manage_inheritence;

import collect_classes.ClassFinder;
import collect_classes.FileExplorer;
import manage_classes.ClassObjectHandler;
import manage_classes.InherObject;

import java.util.*;

public class InherManager {
    FileExplorer explorer;
    ArrayList<InherObject> inherObjects;
    private Map<String, InherObject> dfsMap;

    public InherManager()
    {
        explorer= ClassFinder.getClassExplorer();
        inherObjects=new ArrayList<>();
        dfsMap=new TreeMap<>();

        prepareClasses();
    }
    private void prepareClasses() {
        for(ClassObjectHandler handler : explorer.getListOfClasses())
            handler.prepareInheritenceMap(explorer);

        for (ClassObjectHandler objectHandler : explorer.getListOfClasses())
            dfsMap.put(objectHandler.getInherObject().getFullName(), objectHandler.getInherObject());

        prepareInheriTree();
    }
    private void dfs(InherObject inherObject)
    {
        System.out.println(inherObject.getFullName());
        System.out.println("\t"+inherObject+" "+inherObject.isVisited());

        inherObject.visit();

        for(InherObject tmp : inherObject.getInheritedObjects())
        {
            InherObject parent = dfsMap.get(tmp.getFullName());
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
        for(String name : dfsMap.keySet())
            if(!dfsMap.get(name).isVisited())
                dfs(dfsMap.get(name));

        for(String name : dfsMap.keySet())
            inherObjects.add(dfsMap.get(name));
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
            System.out.println("\t"+object.getOwnAttributes());
            System.out.println("\t"+object.getParentsAttributes());
        }
    }
}
