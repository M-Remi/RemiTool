import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class LongMethodSmell
{
    public static final String ANSI_RED = "\u001B[31m";
    public static final String Restore= "\u001B[0m";
   static int NoOfMethodWithLongSmell=0;


   public double CalculateDCF(MethodDeclaration method)
   {
       double dcf=0;

       List<Statement> statements = method.getBody().get().getStatements();
       Set<String> allVars = new HashSet<>();
       List<Set<String>> usedTogether = new ArrayList<>();

       for (Statement stmt : statements) {
           Set<String> varsInStmt = new HashSet<>();
           stmt.accept(new VoidVisitorAdapter<Void>() {
               @Override
               public void visit(NameExpr expr, Void arg) {
                   varsInStmt.add(expr.getNameAsString());
                   super.visit(expr, arg);
               }
           }, null);

           if (!varsInStmt.isEmpty()) {
               usedTogether.add(varsInStmt);
               allVars.addAll(varsInStmt);
           }
       }
       // Create all unique variable pairs
       List<String> vars = new ArrayList<>(allVars);
       int totalPairs = 0;
       int sharedPairs = 0;

       for (int i = 0; i < vars.size(); i++) {
           for (int j = i + 1; j < vars.size(); j++) {
               totalPairs++;
               String v1 = vars.get(i);
               String v2 = vars.get(j);
               boolean shared = usedTogether.stream().anyMatch(set -> set.contains(v1) && set.contains(v2));
               if (shared) {
                   sharedPairs++;
               }
           }
       }

       dcf = totalPairs == 0 ? 1.0 : (double) sharedPairs / totalPairs;
       //System.out.println("Total variable pairs: " + totalPairs);
       //System.out.println("Shared variable pairs: " + sharedPairs);


       return dcf;
   }
    public LongMethodSmell (MethodDeclaration method, long  lenght)
    {
        Statistics sta=new Statistics();
        int a=sta.MethodCallsCount(method);
        int b=sta.MethodRefCount(method);

        int condition=sta.ConditionExpressionCOunt(method);
        int noOfIf=sta.getIfStatement(method);
        int Iteration=sta.getIterationStatement(method);
        int switchCount=sta.getSwitchEntryStatement(method);
        int catchCount=sta.getCatchStatement(method);

        int CC=condition+ noOfIf+ Iteration + switchCount + catchCount;



        double dcf= CalculateDCF(method);

        if (dcf <0.7 & CC>5 )
        {
            System.out.print( ANSI_RED  + "\nDCF value: " + dcf);
            System.out.print( ANSI_RED  + "\nCyclomatic Complexity value: " + CC);
            System.out.print( ANSI_RED  + "\nThe method " +  method.getName() + " Has Long Method Code Smell ");
            LongMethodSmell.NoOfMethodWithLongSmell +=1;

        }
        else
        {

            System.out.println("DFC: " + dcf);
        }






    }



    }
