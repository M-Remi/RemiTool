public class Parent {
    public int balance;
    public int limit;

    public String familyName()
    {
        return "James";
    }

    public String firstName()
    {
        return "Paul";

    }


    public void deposit(int amount) {
        balance += amount;
    }
}
