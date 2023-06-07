package homework;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static final String SYMBOL = "abc";

    public static final int MAXSIZE = 100;
    public static final int STRINGSIZE = 10_000;
    public static final int STRINGLENGHT = 100_000;

    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(MAXSIZE);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(MAXSIZE);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(MAXSIZE);

    public static void main(String[] args) {

        String textMaxA;
        int countA;
        String textMaxB;
        int countB;
        String textMaxC;
        int countC;

        new Thread( () -> {
            for (int i = 0; i < STRINGSIZE; i++) {
                try {
                    queueA.put(generateText(SYMBOL, STRINGLENGHT));
                    queueB.put(generateText(SYMBOL, STRINGLENGHT));
                    queueC.put(generateText(SYMBOL, STRINGLENGHT));
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        new Thread(() -> {
            for (String s : queueA) {
                for (int i = 0; i < s.length(); i++) {
                    if( s.charAt(i) ){

                    }
                }
            }

        }).start();
        System.out.println(textMaxA);
        System.out.println(textMaxB);
        System.out.println(textMaxC);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
