import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RefuseBequest
{
    double IMUR, IFUR;
    int Field, FieldUsed;
    int NoOfSuperMethod=0;
    int NoOfOverrideMethod=0;
    public RefuseBequest(String filePath) throws FileNotFoundException {

        File codeFile=new File(filePath);

        // Setup symbol solver with source root and reflection
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(new JavaParserTypeSolver("C:\\code\\Ats\\src"));

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        // Parse Java source
        CompilationUnit cu = StaticJavaParser.parse(codeFile);

        String SuperClassName="";

        ClassOrInterfaceDeclaration entityClass =
                cu.findFirst(ClassOrInterfaceDeclaration.class)
                        .orElseThrow(() -> new RuntimeException("Class not found"));
        if (!entityClass.getExtendedTypes().isEmpty()) {
            SuperClassName = entityClass.getExtendedTypes(0).getNameAsString();
            System.out.println("Extends: " + SuperClassName);


            // Everything here handles the super class
            File SuperClassCodeFile = new File(codeFile.getParent(), SuperClassName + ".java" );

            // Setup symbol solver with source root and reflection
            CombinedTypeSolver SuperClassTypeSolver = new CombinedTypeSolver();
            SuperClassTypeSolver.add(new ReflectionTypeSolver());
            SuperClassTypeSolver.add(new JavaParserTypeSolver("C:\\code\\Ats\\src"));

            JavaSymbolSolver SuperClassSymbolSolver = new JavaSymbolSolver(SuperClassTypeSolver);
            StaticJavaParser.getConfiguration().setSymbolResolver(SuperClassSymbolSolver);

            // Parse Java source
            CompilationUnit SuperClassCU = StaticJavaParser.parse(SuperClassCodeFile);

            ClassOrInterfaceDeclaration superClassDecl =SuperClassCU.findFirst(ClassOrInterfaceDeclaration.class)
                            .orElseThrow(() -> new RuntimeException("Superclass not found"));



            Map<String, Set<String>> superMethods = new HashMap<>();

            for (MethodDeclaration method : superClassDecl.getMethods())
            {
                //System.out.println("- " + method.getNameAsString());
                NoOfSuperMethod +=1;

                String sig = method.getParameters().stream()
                        .map(p -> p.getType().toString())
                        .collect(Collectors.joining(","));

                superMethods
                        .computeIfAbsent(method.getNameAsString(), k -> new HashSet<>())
                        .add(sig);
            }
            Set<String> superFields = superClassDecl.getFields().stream()
                    .flatMap(f -> f.getVariables().stream())
                    .map(v -> v.getNameAsString())
                    .collect(Collectors.toSet());

            Set<String> usedSuperFields = new HashSet<>();

            entityClass.findAll(NameExpr.class).forEach(nameExpr -> {
                String name = nameExpr.getNameAsString();
                if (superFields.contains(name)) {
                    usedSuperFields.add(name);
                }
            });
            Field=superFields.size();
            FieldUsed=usedSuperFields.size();

            System.out.println("Used super field " + usedSuperFields.size());

            for (MethodDeclaration subMethod : entityClass.getMethods())
            {

                String name = subMethod.getNameAsString();
                String sig = subMethod.getParameters().stream()
                        .map(p -> p.getType().toString())
                        .collect(Collectors.joining(","));

                if (superMethods.containsKey(name)) {
                    if (superMethods.get(name).contains(sig)) {
                        NoOfOverrideMethod +=1;

                        System.out.println("OVERRIDDEN METHOD: " + name + "(" + sig + ")");
                    } else {
                        System.out.println("OVERLOADED METHOD: " + name + "(" + sig + ")");
                    }
                } else {
                    System.out.println("NEW METHOD: " + name + "(" + sig + ")");
                }
            }

            System.out.println("Super Method " +NoOfSuperMethod + " override methd " + NoOfOverrideMethod );



        }
        //This is useful if am trying to get the list of interfaces as well
//        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(c -> {
//            System.out.println("Class/Interface: " + c.getName());
//
//            // Get extended classes
//            c.getExtendedTypes().forEach(ext -> System.out.println("  Extends: " + ext.getName()));
//
//            // Get implemented interfaces
//            c.getImplementedTypes().forEach(impl -> System.out.println("  Implements: " + impl.getName()));
//        });



    }
    public void calculate()
    {
         IMUR=NoOfOverrideMethod/NoOfSuperMethod;

         IFUR=FieldUsed/Field;

         Double RBScore= 1- (IFUR+IMUR)/2;



    }
}
