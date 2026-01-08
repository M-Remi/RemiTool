import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;


import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class FeatureEnvySmell {


    public static final String Restore = "\u001B[0m";

    public static final String ANSI_RED = "\u001B[31m";
static long ParameterUsage=0;
static  long totalUsage=0;
static int FEMethods=0;

    public FeatureEnvySmell(String filePath) throws FileNotFoundException {

        File codeFile=new File(filePath);



        // Setup symbol solver with source root and reflection
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(new JavaParserTypeSolver("C:\\code\\Ats\\src\\main\\java"));

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        // Parse Java source
        CompilationUnit cu = StaticJavaParser.parse(codeFile);
        fillObjectCreatedArray(cu);


    }

    private boolean isCallOnVariable(MethodCallExpr mc, String variableName) {

        Optional<Expression> scopeOpt = mc.getScope();

        while (scopeOpt.isPresent()) {
            Expression scope = scopeOpt.get();

            if (scope.isNameExpr()) {
                return scope.asNameExpr()
                        .getNameAsString()
                        .equals(variableName);
            }

            if (scope.isMethodCallExpr()) {
                scopeOpt = scope.asMethodCallExpr().getScope();
            } else if (scope.isFieldAccessExpr()) {
                scopeOpt = Optional.of(
                        scope.asFieldAccessExpr().getScope()
                );
            } else {
                break;
            }
        }

        return false;
    }
    public long objectUsageInMethod(MethodDeclaration method, String variableName) {
        long calls = 0;

            long methodCalls =  method.findAll(MethodCallExpr.class).stream()
                    .filter(mc -> isCallOnVariable(mc, variableName))
                    .count();

            calls = methodCalls;

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
//        try {


           //  List<ObjectCreationExpr> objectCreationss = method.findAll(ObjectCreationExpr.class);

            List<ObjectCreationExpr> objectCreations = cu.findAll(ObjectCreationExpr.class);


            AtomicReference<String> fqName = new AtomicReference<>("");


            // get all objects created at global and local level

        cu.findAll(MethodDeclaration.class).forEach(method -> {

            FeatureEnvySmell.totalUsage = totalVairableAndUsageInAMethod(cu, method);
            String methodName = method.getNameAsString();

            method.getParameters().forEach(param -> {
                ResolvedType type = param.getType().resolve();

                if (!type.isReferenceType()) return;

                String qName =
                        type.asReferenceType().getQualifiedName();

                // 🔒 FILTER OUT LIBRARIES
                if (
                        qName.startsWith("java.") ||
                                qName.startsWith("javax.") ||
                                qName.startsWith("jdk.") ||
                                qName.startsWith("com.sun.") ||
                                qName.startsWith("org.xml.") ||
                                qName.startsWith("org.w3c.")
                ) return;

                long usage = objectUsageInMethod(method, param.getNameAsString());
                FeatureEnvySmell.ParameterUsage+=usage;


            });
            getFE(FeatureEnvySmell.ParameterUsage, FeatureEnvySmell.totalUsage, methodName);
        });






    }

    public void getFE(long usage,long totalUsage, String methodName)
    {

        //if (usage == 0 || totalUsage == 0) return;

        double ratio = (double) usage / totalUsage;

        if (ratio > 0.5) {
            System.out.println("⚠ Feature Envy detected: Method: " + methodName );
            FeatureEnvySmell.FEMethods +=1;


        }


        }



}