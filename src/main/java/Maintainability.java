public class Maintainability
{

    Double LongMethodRatio=1.0;
    Double LongParameterRatio=1.0;
    double FE, RB, SummationOfIntensity;

    public Maintainability(int NoOfMethods, int NoOfMethodWithLongSmell, int NoOfMethodWithLongParaSmell, double FE, double RB)
    {
        FE=FE;
        RB=RB;

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
    }

    public void CalculateMaintainability()
    {
        SummationOfIntensity=LongMethodRatio +LongParameterRatio +FE+RB;

        double LMR=LongMethodRatio/SummationOfIntensity;
        double LPR=LongParameterRatio/SummationOfIntensity;
        double FER=FE/SummationOfIntensity;
        double RBR=RB/SummationOfIntensity;

        double entropyLM=LMR * Math.log(LMR);
        double entropyLP=LPR * Math.log(LPR);
        double entropyFE=FER * Math.log(FER);
        double entropyRB=RBR * Math.log(RB);

        double entropy=-1 * (entropyLM+entropyLP+entropyFE+entropyRB);
        double NormalizeEntropy=entropy/1.386;

        double maintainability =1-NormalizeEntropy;




    }
}
