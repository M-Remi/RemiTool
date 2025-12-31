import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.printer.DotPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;


public class Main {

    public void SmellDirection(MethodDeclaration method)
    {

        //long paramter
        Long a = method.getParameters().stream().count();
        if (a >= 5)
        {
            LongParameterListSmell smell = new LongParameterListSmell(method, a);
        }

        // Long method
        String codes = method.toString();// this removes all empty lines and comments;
        long lineCount = codes.lines().count(); //- 2; // 2 for the opening and closing braces
        if (lineCount >20)
        {
            System.out.println("The Method " + method.getName() + " With LOC= " + lineCount+ " Has Long Method Code Smell");
        }
    else
        {
            LongMethodSmell smell2 = new LongMethodSmell(method, lineCount);
        }

        // Feature Envy




    }
    public static void main(String[] args) throws IOException {

        Main obj=new Main();

        StringBuilder code= new StringBuilder();
        String path;

        Scanner inputReader=new Scanner(System.in);
        System.out.println("Enter File Directory: ");
        path=inputReader.nextLine();


        File codeFile=new File(path);


        List<String> allLines = Files.readAllLines(codeFile.toPath());

        Scanner scan=new Scanner(codeFile);

        while (scan.hasNext())
        {
            code.append(scan.nextLine());
        }

        ParserConfiguration config = new ParserConfiguration()
                .setAttributeComments(false)
                .setStoreTokens(true); // Optional, but useful for full location info

        JavaParser parser = new JavaParser(config);
        ParseResult<CompilationUnit> result = parser.parse(ParseStart.COMPILATION_UNIT, Providers.provider(new File(path)));
        CompilationUnit cu = result.getResult().orElseThrow();

        //handles AST diagram
        DotPrinter printer = new DotPrinter(true);
        String dotGraph = printer.output(cu);

        // Save to DOT file
        try (FileWriter writer = new FileWriter("ast.dot")) {
            writer.write(dotGraph);
            System.out.println("Abstarct Syntax Tree Is Saved In Directory");
        }


        cu.findAll(MethodDeclaration.class).forEach(method -> {

            //statistcs
            Statistics sta=new Statistics(method);



            if (method.getNameAsString().equals("main") && method.isStatic())
            {
               obj.SmellDirection(method);
            }
            else
            {
                obj.SmellDirection(method);

            }

        }



      );


       // FeatureEnvySmell envy=new FeatureEnvySmell(path);
        RefuseBequest RB=new RefuseBequest(path);


    }

}