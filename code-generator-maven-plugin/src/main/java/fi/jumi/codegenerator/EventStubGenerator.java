// Copyright © 2011, Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package fi.jumi.codegenerator;

import java.util.*;

@SuppressWarnings({"StringConcatenationInsideStringBufferAppend"})
public class EventStubGenerator {

    private Class<?> listenerType;
    private String targetPackage;
    private String eventInterface;
    private String factoryInterface;
    private String senderInterface;

    public String getFactoryPath() {
        return fileForClass(factoryName());
    }

    public String getFactorySource() {
        StringBuilder sb = new StringBuilder();
        sb.append(packageStatement());
        sb.append(importStatements());

        sb.append("public class " + factoryName() + " implements " + genericFactoryType() + " {\n");
        sb.append("\n");

        sb.append("    public Class<" + listenerName() + "> getType() {\n");
        sb.append("        return " + listenerName() + ".class;\n");
        sb.append("    }\n");

        sb.append("\n");

        sb.append("    public " + listenerName() + " newFrontend(" + genericSenderType() + " target) {\n");
        sb.append("        return new " + frontendName() + "(target);\n");
        sb.append("    }\n");

        sb.append("\n");

        sb.append("    public " + genericSenderType() + " newBackend(" + listenerName() + " target) {\n");
        sb.append("        return new " + backendName() + "(target);\n");
        sb.append("    }\n");

        sb.append("}\n");
        return sb.toString();
    }

    public String getFrontendPath() {
        return fileForClass(frontendName());
    }

    public String getFrontendSource() {
        StringBuilder sb = new StringBuilder();
        sb.append(packageStatement());
        sb.append(importStatements());

        sb.append("public class " + frontendName() + " implements " + listenerName() + " {\n");
        sb.append("\n");

        sb.append("    private final " + genericSenderType() + " sender;\n");
        sb.append("\n");

        sb.append("    public " + frontendName() + "(" + genericSenderType() + " sender) {\n");
        sb.append("        this.sender = sender;\n");
        sb.append("    }\n");

        sb.append("\n");

        // TODO: use reflection to get the methods
        sb.append("    public void onSomething(String param1, String param2) {\n");
        sb.append("        sender.send(new OnSomethingEvent(param1, param2));\n");
        sb.append("    }\n");

        sb.append("}\n");
        return sb.toString();
    }

    private String packageStatement() {
        return "package " + targetPackage + ";\n\n";
    }

    private StringBuilder importStatements() {
        StringBuilder sb = new StringBuilder();
        for (String classToImport : classesToImport()) {
            sb.append("import " + classToImport + ";\n");
        }
        sb.append("\n");
        return sb;
    }

    private Collection<String> classesToImport() {
        SortedSet<String> singleClassImports = new TreeSet<String>();
        singleClassImports.add(listenerType.getName());
        singleClassImports.add(eventInterface);
        singleClassImports.add(factoryInterface);
        singleClassImports.add(senderInterface);

        SortedSet<String> wildcardImports = new TreeSet<String>();
        for (String singleClassImport : singleClassImports) {
            wildcardImports.add(getPackage(singleClassImport) + ".*");
        }
        return wildcardImports;
    }

    private String genericFactoryType() {
        return factoryInterfaceName() + "<" + listenerName() + ">";
    }

    private String genericSenderType() {
        return senderInterfaceName() + "<" + eventInterfaceName() + "<" + listenerName() + ">>";
    }

    private String factoryName() {
        return listenerName() + "Factory";
    }

    private String frontendName() {
        return listenerName() + "ToEvent";
    }

    private String backendName() {
        return "EventTo" + listenerName();
    }

    private String listenerName() {
        return listenerType.getSimpleName();
    }

    private String eventInterfaceName() {
        return getSimpleName(eventInterface);
    }

    private String factoryInterfaceName() {
        return getSimpleName(factoryInterface);
    }

    private String senderInterfaceName() {
        return getSimpleName(senderInterface);
    }

    private static String getPackage(String className) {
        return className.substring(0, className.lastIndexOf('.'));
    }

    private static String getSimpleName(String className) {
        return className.substring(className.lastIndexOf('.') + 1);
    }

    private String fileForClass(String className) {
        return targetPackage.replace('.', '/') + "/" + className + ".java";
    }

    // generated setters

    public void setListenerType(Class<?> listenerType) {
        this.listenerType = listenerType;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public void setEventInterface(String eventInterface) {
        this.eventInterface = eventInterface;
    }

    public void setFactoryInterface(String factoryInterface) {
        this.factoryInterface = factoryInterface;
    }

    public void setSenderInterface(String senderInterface) {
        this.senderInterface = senderInterface;
    }
}