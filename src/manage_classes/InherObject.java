package manage_classes;

import java.util.*;

public class InherObject {
    private final String fullName;
    private ArrayList<String> ownAttributes;
    private ArrayList<String> ownMethods;
    private HashSet<String> parentsAttributes;
    private HashSet<String> parentsMethods;
    private ArrayList<InherObject> inheritedObjects;
    boolean visited;

    public InherObject(String fullName)
    {
        this.fullName=fullName;
        this.parentsAttributes=new HashSet<>();
        this.parentsMethods=new HashSet<>();
        this.inheritedObjects=new ArrayList<>();
        this.visited=false;
    }

    public String getFullName() {
        return fullName;
    }

    public ArrayList<String> getOwnAttributes() {
        return ownAttributes;
    }

    public void setOwnAttributes(ArrayList<String> ownAttributes) {
        this.ownAttributes = ownAttributes;
    }

    public ArrayList<String> getOwnMethods() {
        return ownMethods;
    }

    public void setOwnMethods(ArrayList<String> ownMethods) {
        this.ownMethods = ownMethods;
    }

    public Collection<String> getParentsAttributes() {
        return parentsAttributes;
    }

    public void addParentsAttributes(Collection<String> parentsAttributes) {
        this.parentsAttributes.addAll(parentsAttributes);
    }

    public Collection<String> getParentsMethods() {
        return parentsMethods;
    }

    public void addParentsMethods(Collection<String> parentsMethods) {
        this.parentsMethods.addAll(parentsMethods);
    }

    public void setInheritedObjects(Collection<ClassObjectHandler>handlers)
    {
        for(ClassObjectHandler handler : handlers)
        {
            inheritedObjects.add(handler.getInherObject());
        }
    }

    public ArrayList<InherObject> getInheritedObjects() {
        return inheritedObjects;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public void visit()
    {
        this.visited=true;
    }
}