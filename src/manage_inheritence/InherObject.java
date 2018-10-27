package manage_inheritence;

import manage_collaboration.ClassObjectHandler;

import java.util.*;

public class InherObject {
    private final String fullName;
    private ArrayList<String> ownAttributes;
    private ArrayList<String> ownMethods;
    private HashSet<String> parentsAttributes;
    private HashSet<String> parentsMethods;
    private ArrayList<InherObject> inheritedObjects;
    private Set<String> usedMethods;
    private Set<String> usedAttributes;
    boolean visited;

    public InherObject(String fullName)
    {
        this.fullName=fullName;
        this.parentsAttributes=new HashSet<>();
        this.parentsMethods=new HashSet<>();
        this.inheritedObjects=new ArrayList<>();
        this.usedMethods=new HashSet<>();
        this.usedAttributes=new HashSet<>();
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

    public void addUsedAttribute(String att)
    {
        this.usedAttributes.add(att);
    }

    public void addUsedMethod(String method)
    {
        this.usedMethods.add(method);
    }

    public Collection<String> getAllUsedMethods()
    {
        return usedMethods;
    }

    public Collection<String> getAllUsedAttributes()
    {
        return usedAttributes;
    }

    public Collection<String> getUnusedParentMethods() {
        Set<String>tmp=new HashSet<>();
        tmp.addAll(parentsMethods);

        for(String m : usedMethods)
        {
            if(tmp.contains(m))
                tmp.remove(m);
        }
        return tmp;
    }

    public Collection<String> getUnusedParentAttributes() {
        Set<String>tmp=new HashSet<>();
        tmp.addAll(parentsAttributes);

        for(String m : usedAttributes)
        {
            if(tmp.contains(m))
                tmp.remove(m);
        }
        return tmp;
    }
}