import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class FeatureEnvySmell {


    public static final String Restore = "\u001B[0m";

    public static final String ANSI_RED = "\u001B[31m";


    public FeatureEnvySmell(String filePath) throws FileNotFoundException {

        File codeFile=new File(filePath);



        // Setup symbol solver with source root and reflection
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(new JavaParserTypeSolver("C:\\code\\Ats\\src"));

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        // Parse Java source
        CompilationUnit cu = StaticJavaParser.parse(codeFile);
        fillObjectCreatedArray(cu);

        //cu.findAll(MethodDeclaration.class).forEach(method -> {

          //  fillObjectCreatedArray(cu);
            //    }

        //);
    }

    public long objectUsageInMethod(MethodDeclaration method, String variableName) {
        long calls = 0;
        try {
            long methodCalls = method.findAll(MethodCallExpr.class).stream()
                    .map(MethodCallExpr::getScope)
                    .filter(scope -> scope.isPresent() && scope.get().isNameExpr())
                    .filter(scope -> scope.get().asNameExpr().getNameAsString().equals(variableName))
                    .count();
            calls = methodCalls;
        } catch (Exception e) {
            System.out.println("Error in objectUsageInMethod: " + e.getMessage());
        }
        return calls;
    }
    public long objectUseage (CompilationUnit cu, String vairableName)
    {
            long calls=0;
            try {
                long methodCalls = cu.findAll(MethodCallExpr.class).stream()
                        .map(MethodCallExpr::getScope)
                        .filter(scope -> scope.isPresent() && scope.get().isNameExpr())
                        .filter(scope -> scope.get().asNameExpr().getNameAsString().equals(vairableName))
                        .count();


                calls=methodCalls;



            }
            catch (Exception e) {
                System.out.println("Error objectUseage '" +  e.getMessage());
            }

        return calls;

    }


    long totalVairableAndUsageInAMethod(CompilationUnit cu, MethodDeclaration method)
    {
        AtomicLong total = new AtomicLong();
        try {


            AtomicLong totalVaiarableUsauge = new AtomicLong();

            method.findAll(VariableDeclarator.class).forEach(expr -> {
                if (expr.getInitializer().isPresent() && expr.getInitializer().get().isObjectCreationExpr()) {

                    ObjectCreationExpr creationExpr = expr.getInitializer().get().asObjectCreationExpr();
                    long i = objectUsageInMethod(method, expr.getNameAsString());
                    //System.out.println(Restore + "\n Total usages of vairable '" + expr.getNameAsString() + "' belonging to method " + method.getNameAsString() + " is: " + i);

                    totalVaiarableUsauge.addAndGet(i);
                    total.set(totalVaiarableUsauge.get());
                }
            });

        }

        catch (Exception e)
            {
            System.out.println("Error in totalVairableAndUsageInAMethod '" +  e.getMessage());
        }


        return total.get();




    }


    long totalVairableAndUsageGlobal(CompilationUnit cu, MethodDeclaration method)
    {
        AtomicLong total = new AtomicLong();
        try {


            AtomicLong totalVaiarableUsauge = new AtomicLong();

            method.findAll(VariableDeclarator.class).forEach(expr -> {
                if (expr.getInitializer().isPresent() && expr.getInitializer().get().isObjectCreationExpr()) {

                    ObjectCreationExpr creationExpr = expr.getInitializer().get().asObjectCreationExpr();
                    long i = objectUseage(cu, expr.getNameAsString());
                    System.out.println(Restore + "\n Total usages of vairable '" + expr.getNameAsString() + "' belonging to method " + method.getNameAsString() + " is: " + i);

                    totalVaiarableUsauge.addAndGet(i);
                    total.set(totalVaiarableUsauge.get());
                }
            });

        }

        catch (Exception e)
        {
            System.out.println("Error in totalVairableAndUsageInAMethod '" +  e.getMessage());
        }


        return total.get();




    }


    public void fillObjectCreatedArray(CompilationUnit cu)
    {
        try {


           //  List<ObjectCreationExpr> objectCreationss = method.findAll(ObjectCreationExpr.class);

            List<ObjectCreationExpr> objectCreations = cu.findAll(ObjectCreationExpr.class);


            AtomicReference<String> fqName = new AtomicReference<>("");


            // get all objects created at global and local level

            cu.findAll(VariableDeclarator.class).forEach(v -> {
                if (v.getInitializer().isPresent() && v.getInitializer().get().isObjectCreationExpr()) {

                    ObjectCreationExpr creationExpr = v.getInitializer().get().asObjectCreationExpr();


                    fqName.set(creationExpr.getType().resolve().describe());

                    if (fqName.get().startsWith("java") || fqName.get().startsWith("com.sun")
                            || fqName.get().startsWith("sun") || fqName.get().startsWith("oracle")
                            || fqName.get().startsWith("org.xml") || fqName.get().startsWith("com.oracle")) {
                        //System.out.println( Restore + "\n  Object created from jdk: " + fqName);

                    }
                    else {  //System.out.println("\n  Normally created object: "  + v.getNameAsString() + " " + fqName);

                        // System.out.println(Restore + "\n Normal Creation " +  v.getNameAsString() );
                        v.findAncestor(MethodDeclaration.class)
                                .ifPresentOrElse(method -> {
                                            String methodName = method.getNameAsString();
                                            long i = objectUsageInMethod(method, v.getNameAsString());
                                            System.out.println(Restore + "\n Total usages of vairable '" + v.getNameAsString() + "' belonging to method " + methodName + " is: " + i);



                                            long totalUsageInMethod = totalVairableAndUsageInAMethod(cu, method);
                                            System.out.println(Restore + "\n Total Vairable usage (n): " + totalUsageInMethod);


                                           if (i==0 || totalUsageInMethod==0)
                                           {
                                               System.out.println(Restore + "\n The vairable : " + v.getNameAsString() + "was never used");
                                           }
                                           else
                                           {
                                               double ratio = (double) i / (double) totalUsageInMethod;


                                               double FEF= (0.5 * ratio);

                                               FEF=  FEF + (1-0.5)*(1-Math.pow (0.5,i) );

                                               double roundedUpFEF = new BigDecimal(Double.toString(FEF))
                                                       .setScale(2, RoundingMode.UP)
                                                       .doubleValue();
                                               System.out.println(Restore + "\n Feature Envy Factor of : " + v.getNameAsString() + "= " + i + "/" + totalUsageInMethod + " = " + roundedUpFEF);
                                           }
                                        },
                                        () -> {
                                    // no need for feature envy of global object as feature envy is a method code smell
                                           // long i = objectUseage(cu, v.getNameAsString());
                                            //System.out.println(Restore +
                                              //      "\n Total usages of variable '" + v.getNameAsString() +
                                                //    "' (not inside any method, possibly a field) is: " + i);


                                        }
                                );


                    }



                }
            });


        } catch (Exception e) {
            System.out.println("Error processing variable '" +  e.getMessage());
        }


    }

}