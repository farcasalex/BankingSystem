package util;

import java.util.Random;

public class AccountIdGenerator {
    public String randomRoId(){
        Random rand = new Random();
        StringBuilder string = new StringBuilder("RO");
        for (int i = 0; i < 22; i++)
        {
            int n = rand.nextInt(10);
            string.append(Integer.toString(n));
        }

        return string.toString();
    }
}
