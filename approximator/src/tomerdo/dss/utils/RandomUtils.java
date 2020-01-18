package tomerdo.dss.utils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomUtils {

    Random random =  new Random(1971603567);


    public static <E> List<E> pickNRandomElements(List<E> list, int n) {
        Random r = new Random();
        int length = list.size();

        if (length < n) return null;

        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i , r.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    public int drawLargeInt() {
        return random.nextInt();
    }

    public static int drawRandom(int size) {
        double random = Math.random();
        double randomIndex = random * size;
        return (int) Math.floor(randomIndex);
    }


}
