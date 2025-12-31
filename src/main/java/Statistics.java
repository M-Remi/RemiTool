import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.github.javaparser.ast.body.MethodDeclaration;
public class Statistics {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String Restore= "\u001B[0m";

    // for counting conditions like Exp1 ? Exp2 : Exp3;
    //  int cc= (10>5) ? Testing2("a","a","x","a","s","d","q") : 4;

    public int getSwitchEntryStatement(MethodDeclaration method)
    {
        class MutableInt {
            int counter = 0;
        }
        MutableInt m = new MutableInt();
        method.getBody().ifPresent(body ->
        {
            for (Statement stmt : body.getStatements()) {
                if (stmt.getClass().getSimpleName().equals("SwitchEntry"))
                {
                    m.counter += 1;
                }



            }
        });
        return m.counter;
    }

    public int getCatchStatement(MethodDeclaration method)
    {
        class MutableInt {
            int counter = 0;
        }
        MutableInt m = new MutableInt();
        method.getBody().ifPresent(body ->
        {
            for (Statement stmt : body.getStatements()) {
                if (stmt.getClass().getSimpleName().equals("CatchClause"))
                {
                    m.counter += 1;
                }



            }
        });
        return m.counter;
    }

    public int getIfStatement(MethodDeclaration method)
    {
        class MutableInt {
            int counter = 0;
        }
        MutableInt m = new MutableInt();
        method.getBody().ifPresent(body ->
        {
            for (Statement stmt : body.getStatements()) {
                if (stmt.getClass().getSimpleName().equals("IfStmt"))
                {
                    m.counter += 1;
                }



            }
        });
        return m.counter;
    }

    public int getIterationStatement(MethodDeclaration method)
    {
        class MutableInt {
            int counter = 0;
        }
        MutableInt m = new MutableInt();
        method.getBody().ifPresent(body ->
        {
            for (Statement stmt : body.getStatements()) {
                if (stmt.getClass().getSimpleName().equals("ForEachStmt"))
                {
                    m.counter += 1;
                }
                if (stmt.getClass().getSimpleName().equals("ForStmt"))
                {
                    m.counter += 1;
                }
                if (stmt.getClass().getSimpleName().equals("WhileStmt"))
                {
                    m.counter += 1;
                }
                if (stmt.getClass().getSimpleName().equals("DoStmt"))
                {
                    m.counter += 1;
                }

            }
        });
        return m.counter;
    }
    public int ConditionExpressionCOunt(MethodDeclaration method)
    {
        int counter=0;
        List<ExpressionStmt> expressionStatements = method.findAll(ExpressionStmt.class);
        for (ExpressionStmt stmt : expressionStatements)
        {
            Expression expr = stmt.getExpression();

            if( expr.getClass().getSimpleName().equals("ConditionalExpr"))
            {
                counter +=1;

            }


        }

        return counter;



    }

    public int MethodRefCount(MethodDeclaration method)
    {
        int counter=0;
        List<ExpressionStmt> expressionStatements = method.findAll(ExpressionStmt.class);
        for (ExpressionStmt stmt : expressionStatements)
        {
            Expression expr = stmt.getExpression();

            if( expr.getClass().getSimpleName().equals("MethodReferenceExpr"))
            {
                counter +=1;

            }


        }

        return counter;



    }
    public int MethodCallsCount(MethodDeclaration method)
    {
        int counter=0;
        List<ExpressionStmt> expressionStatements = method.findAll(ExpressionStmt.class);
        for (ExpressionStmt stmt : expressionStatements)
        {
            Expression expr = stmt.getExpression();

            if( expr.getClass().getSimpleName().equals("MethodCallExpr"))
            {
                counter +=1;

            }



        }

        return counter;



    }
    public  Statistics() {
    }

    public  Statistics(MethodDeclaration method)
    {

        class MutableInt {

            int AssertStmt = 0;
            int BlockStmt = 0;
            int BreakStmt = 0;
            int ContinueStmt = 0;
            int DoStmt = 0;
            int EmptyStmt = 0;
            int ExplicitConstructorInvocationStmt = 0;
            int ExpressionStmt = 0;
            int ForEachStmt = 0;
            int ForStmt = 0;
            int IfStmt = 0;
            int LabeledStmt = 0;
            int LocalClassDeclarationStmt = 0;
            int ReturnStmt = 0;
            int SwitchStmt = 0;
            int SwitchExprStmt = 0;
            int SynchronizedStmt = 0;
            int ThrowStmt = 0;
            int TryStmt = 0;
            int WhileStmt = 0;
            int YieldStmt = 0;
            int UnparsableStmt = 0;
        }
        MutableInt m = new MutableInt();
        //to be able to modify the vairable in the lambda

        method.getBody().ifPresent(body ->{
            for (Statement stmt : body.getStatements()) {
               // System.out.println(Restore + "Statement: " + stmt);
                //System.out.println(Restore + "Type: " + stmt.getClass().getSimpleName());
                if (stmt.getClass().getSimpleName()=="AssertStmt")
                {
                    m.AssertStmt +=1;
                }

                if (stmt.getClass().getSimpleName().equals("BlockStmt")) {
                    m.BlockStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("BreakStmt")) {
                    m.BreakStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("ContinueStmt")) {
                    m.ContinueStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("DoStmt")) {
                    m.DoStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("EmptyStmt")) {
                    m.EmptyStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("ExplicitConstructorInvocationStmt")) {
                    m.ExplicitConstructorInvocationStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("ExpressionStmt")) {
                    m.ExpressionStmt +=1;
                }

                if (stmt.getClass().getSimpleName().equals("ForEachStmt")) {
                    m.ForEachStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("ForStmt")) {
                    m.ForStmt +=1;
                }

                if (stmt.getClass().getSimpleName().equals("LabeledStmt")) {
                    m.LabeledStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("LocalClassDeclarationStmt")) {
                    m.LocalClassDeclarationStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("ReturnStmt")) {
                    m.ReturnStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("SwitchStmt")) {
                    m.SwitchStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("SwitchExprStmt")) {
                    m.SwitchExprStmt +=1;
                }

                if (stmt.getClass().getSimpleName().equals("SynchronizedStmt")) {
                    m.SynchronizedStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("ThrowStmt")) {
                    m.ThrowStmt +=1;
                }

                if (stmt.getClass().getSimpleName().equals("TryStmt")) {
                    m.TryStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("WhileStmt")) {
                    m.WhileStmt +=1;
                }

                if (stmt.getClass().getSimpleName().equals("YieldStmt")) {
                    m.YieldStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("UnparsableStmt")) {
                    m.UnparsableStmt +=1;
                }
                if (stmt.getClass().getSimpleName().equals("IfStmt")) {
                    m.IfStmt +=1;
                }

            }
        });
        System.out.print(Restore + "\n Below are qualitative analysis of the method: "+ method.getName());

        System.out.print( Restore + "\n No. of If statement: " + m.IfStmt);
        System.out.print( Restore + "\n No. of Assert statement: " + m.AssertStmt);
        System.out.print( Restore + "\n No. of Block statement: " + m.BlockStmt);
        System.out.print( Restore + "\n No. of Break statement: " + m.BreakStmt);
        System.out.print( Restore + "\n No. of Continue statement: " + m.ContinueStmt);
        System.out.print( Restore + "\n No. of Do statement: " + m.DoStmt);
        System.out.print( Restore + "\n No. of Explicit Constructor Invocation statement: " + m.ExplicitConstructorInvocationStmt);
        System.out.print( Restore + "\n No. of Expression statement: " + m.ExpressionStmt);
        System.out.print( Restore + "\n No. of For-Each statement: " + m.ForEachStmt);
        System.out.print( Restore + "\n No. of For statement: " + m.ForStmt);
        System.out.print( Restore + "\n No. of Label statement: " + m.LabeledStmt);
        System.out.print( Restore + "\n No. of Local Class Declaration statement: " + m.LocalClassDeclarationStmt);
        System.out.print( Restore + "\n No. of Return statement: " + m.ReturnStmt);

        System.out.print( Restore + "\n No. of Switch statement: " + m.SwitchStmt);
        System.out.print( Restore + "\n No. of Switch Expression statement: " + m.SwitchExprStmt);
        System.out.print( Restore + "\n No. of Synchronized statement: " + m.SynchronizedStmt);
        System.out.print( Restore + "\n No. of Throw statement: " + m.ThrowStmt);
        System.out.print( Restore + "\n No. of Throw statement: " + m.EmptyStmt);

        System.out.print( Restore + "\n No. of Try statement: " + m.TryStmt);
        System.out.print( Restore + "\n No. of While statement: " + m.WhileStmt);
        System.out.print( Restore + "\n No. of Yield statement: " + m.YieldStmt);
        System.out.print( Restore + "\n No. of Unparsable statement: " + m.UnparsableStmt);


        System.out.print( Restore + "\n ..................................................................................................\n");

    }
}
