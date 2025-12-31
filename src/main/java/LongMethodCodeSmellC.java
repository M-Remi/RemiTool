import java.io.IOException;

public class LongMethodCodeSmellC {

    public static void main(String[] args) throws IOException {
    }
        public void test()
    {
        for (int i = 0; i < 20; i++)

        {
            Integer c= i;
            for (int a = 0; a < 20; a++)

            {
                 c= a;
                for (int b = 0; b < 20; b++)

                {
                     c= b;
                }
            }
        }

    }
}
