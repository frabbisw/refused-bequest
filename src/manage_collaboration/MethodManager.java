package manage_collaboration;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import manage_inheritence.InherManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MethodManager {
    Map<String, String> localMap;
    Map<String,String>classesMap;
    Map<String, String> globalMap;
    Set<String>localVariables;
    String myClassName;
    String myMethodName ="";
    InherManager inherManager;
    ClassObjectHandler handler;

    public MethodManager(MethodDeclaration method, ClassObjectHandler handler, Map<String, String> globalMap, Map<String,String>classesMap, InherManager inherManager)
    {
        this.handler=handler;
        this.globalMap=globalMap;
        this.classesMap=classesMap;
        this.myMethodName =method.getNameAsString();
        this.myClassName=handler.getFullName();
        this.localVariables=new HashSet<>();
        this.inherManager=inherManager;

        localMap = new TreeMap<>();

        for (Parameter parameter : method.getParameters())
            for(String flazz : classesMap.keySet())
                if(flazz.equals(parameter.getTypeAsString()))
                    localMap.put(parameter.getNameAsString(), parameter.getTypeAsString());

        method.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(VariableDeclarator variable, Void arg) {
                localVariables.add(variable.getNameAsString());
                addVariableToLocalMap(variable);
                super.visit(variable, arg);
            }
            @Override
            public void visit(ForeachStmt forEach , Void arg)
            {
                forEach.accept(new VoidVisitorAdapter<Void>() {
                    @Override
                    public void visit(VariableDeclarator variable, Void arg) {
                        addVariableToLocalMap(variable);
                        super.visit(variable, arg);
                    }
                }, null);
                super.visit(forEach, arg);
            }

            @Override
            public void visit(MethodCallExpr methodCall, Void arg) {
                inspectMethodCall(methodCall);
                super.visit(methodCall, arg);
            }

            @Override
            public void visit(AssignExpr n, Void arg) {
                inspectAssignExpr(n);
                super.visit(n, arg);
            }
        }, null);
    }

    private void inspectAssignExpr(AssignExpr n) {
        String objectName = n.getTarget().toString();
        //do staff with full class name and parent attributes
        if(handler.getInherObject().getParentsAttributes().contains(objectName))
        {
            handler.getInherObject().addUsedAttribute(objectName);
        }
    }

    private void inspectMethodCall(MethodCallExpr methodCall)
    {
        for (Expression expression : methodCall.getArguments())
        {
            if(expression.isMethodCallExpr())
                inspectMethodCall(expression.asMethodCallExpr());
        }
        if(methodCall.getScope().isPresent())
        {
            String objectName=methodCall.getScope().get().toString();
            String className=null;
            if(globalMap.containsKey(objectName))   className=globalMap.get(objectName);
            if(localMap.containsKey(objectName))   className=localMap.get(objectName);

            if(className!=null)
            {
                String fullClassName=classesMap.get(className);
                String methodName=methodCall.getNameAsString();

                //do staff with full class name and method
                inherManager.addUsedMethod(fullClassName, methodName);
            }

            //do staff with full class name and parent attributes
            if(handler.getInherObject().getParentsAttributes().contains(objectName))
            {
                handler.getInherObject().addUsedAttribute(objectName);
            }
        }
        else
        {
            //own class name and method
            inherManager.addUsedMethod(myClassName, methodCall.getNameAsString());
        }
    }
    private void addVariableToLocalMap(VariableDeclarator variable)
    {
        boolean isAForeignClass=false;
        for(String flazz : classesMap.keySet())
            if(flazz.equals(variable.getType().asString()))
            {
                localMap.put(variable.getNameAsString(), variable.getTypeAsString());
                isAForeignClass=true;
            }
        if(!isAForeignClass)
            localMap.put(variable.getNameAsString(), null);
    }
}