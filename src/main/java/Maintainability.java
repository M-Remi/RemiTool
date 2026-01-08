public class Maintainability
{

    Double LongMethodRatio=0.0;
    Double LongParameterRatio=0.0;
    double FE, RB, SummationOfIntensity;

    public Maintainability(int NoOfMethods, int NoOfMethodWithLongSmell, int NoOfMethodWithLongParaSmell, int NoOfFE, double RBScore)
    {

        RB=RBScore;

        double a=NoOfMethodWithLongSmell;
        double b=NoOfMethods;

        if (a !=0.0)
        {
            LongMethodRatio = a / b;
        }

        a=NoOfMethodWithLongParaSmell;
        if (a !=0.0)
        {

            LongParameterRatio = a / b;
        }

        a=NoOfFE;
        if (a !=0)
        {
            FE= (double) NoOfFE / (double) NoOfMethods;
        }
        CalculateMaintainability();

    }

    public void CalculateMaintainability()
    {
        SummationOfIntensity=LongMethodRatio +LongParameterRatio +FE+RB;

        double LMR=LongMethodRatio/SummationOfIntensity;
        double LPR=LongParameterRatio/SummationOfIntensity;
        double FER=FE/SummationOfIntensity;
        double RBR=RB/SummationOfIntensity;

        System.out.println("Smell                                                     Intensity");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Feature Envy                                             "+ FE);
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Long Method                                              " + LongMethodRatio);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Refuse Bequest                                           "+ RB);
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Long Parameter                                           " +LongParameterRatio);
        System.out.println("-------------------------------------------------------------------------------");

        System.out.println("Summation Of Intensity; " + SummationOfIntensity);

        System.out.println("Normalizing the Probabilities:");
        System.out.println("Smell                                                     Normalized value");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Feature Envy                                             "+ FER);
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Long Method                                              " + LMR);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Refuse Bequest                                           "+ RBR);
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Long Parameter                                           " +LPR);
        System.out.println("-------------------------------------------------------------------------------");

        double entropyLM=LMR * Math.log(LMR);
        double entropyLP=LPR * Math.log(LPR);
        double entropyFE=FER * Math.log(FER);
        double entropyRB=RBR * Math.log(RBR);

        if (Double.isNaN(entropyLM))
        {  entropyLM=0; }
        if (Double.isNaN(entropyLP))
        {  entropyLP=0; }
        if (Double.isNaN(entropyFE))
        {  entropyFE=0; }
        if (Double.isNaN(entropyRB))
        {  entropyRB=0; }



        System.out.println("Entropy Of Each Smell");


        System.out.println("Smell                                                     Entropy");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Feature Envy                                             "+ entropyFE);
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Long Method                                              " + entropyLM);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Refuse Bequest                                           "+ entropyRB);
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Long Parameter                                           " +entropyLP);
        System.out.println("-------------------------------------------------------------------------------");

        double entropy=-1 * (entropyLM+entropyLP+entropyFE+entropyRB);
        double NormalizeEntropy=entropy/1.386;

        double maintainability =1-NormalizeEntropy;

        System.out.println("Maintainability Ratio: " +maintainability);


    }
}
