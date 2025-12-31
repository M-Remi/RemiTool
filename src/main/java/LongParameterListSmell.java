import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;

public class LongParameterListSmell
{

    public static final String Restore= "\u001B[0m";

    public static final String ANSI_RED = "\u001B[31m";

    ArrayList<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
    ArrayList <String> ParameterTypes=new ArrayList<String>();

    public LongParameterListSmell (MethodDeclaration method, Long noOfParameters)
    {
          System.out.println( Restore + "Method Name: " + method.getName() + " ");
          System.out.print("\nReturn Type " + method.getType() + " ");
          System.out.println("\nParameters " + method.getParameters() + "\n ");

     //     System.out.print( ANSI_RED  + "The method " +  method.getName() + " Has Long Parameter List Code Smell ");



        int primitive=0;
        int nonPrimitive=0;

        for (Parameter parameter: method.getParameters())
      {
          //System.out.print("\n " + parameter.toString());
         //System.out.print("\n " + parameter.getType().toString());
          if ( parameter.getType().isPrimitiveType())
          {
            primitive +=1;

          }
          else
          {
              nonPrimitive +=1;


          }
      }
        if (nonPrimitive > noOfParameters/2)
        {
            methods.add(method);

           System.out.print( ANSI_RED  + "Details: " +  method.getName() + " Has More Non-Primitive Data Type Parameters");

            System.out.print( "\n-----------------------------------------------------------------------------------------\n" );
            System.out.print( ANSI_RED  + "The method " +  method.getName() + " Has Long Parameter List Code Smell ");




        }

    }


}
