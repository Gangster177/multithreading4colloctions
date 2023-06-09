package homework;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static final String SYMBOL = "abc";

    public static final int MAXSIZE = 100;
    public static final int SIZEOFSTRINGS = 10_000;
    public static final int STRINGLENGHT = 100_000;


    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(MAXSIZE);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(MAXSIZE);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(MAXSIZE);

    public static BlockingQueue<String> queueAllStr = new ArrayBlockingQueue<>(SIZEOFSTRINGS);

    public static final char A = 'a';
    public static final char B = 'b';
    public static final char C = 'c';

    public static AtomicReference<String> maxStringA = new AtomicReference<>();
    public static AtomicReference<String> maxStringB = new AtomicReference<>();
    public static AtomicReference<String> maxStringC = new AtomicReference<>();


    public static AtomicInteger maxIntA = new AtomicInteger();
    public static AtomicInteger maxIntB = new AtomicInteger();
    public static AtomicInteger maxIntC = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        long startTs = System.currentTimeMillis(); // start time
        System.out.println("Start generation strings");
        Thread generate = new Thread(() -> {
            for (int i = 0; i < SIZEOFSTRINGS; i++) {
                try {
                    String text = generateText(SYMBOL, STRINGLENGHT);
                    queueAllStr.put(text);
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        System.out.println("Searches for the max number of characters...");
        threads.add(generate);
        threads.add(characterSearch(queueA, A, maxStringA, maxIntA));
        threads.add(characterSearch(queueB, B, maxStringB, maxIntB));
        threads.add(characterSearch(queueC, C, maxStringC, maxIntC));

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("MAX 'A' " + maxIntA.get());
        System.out.println("MAX 'B' " + maxIntB.get());
        System.out.println("MAX 'C' " + maxIntC.get());

        long endTs = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (endTs - startTs) + "ms");

//        System.out.println("ALL STRINGS : ");
//        for(int i = 0; i < queueAllStr.size(); i++){
//            System.out.println(i + 1 + ". " + queueAllStr.poll() );
//        }

//        System.out.println("MAX 'A' " + maxIntA.get() + " in : " + maxStringA.get());
//        System.out.println("MAX 'B' " + maxIntB.get() + " in : " + maxStringB.get());
//        System.out.println("MAX 'C' " + maxIntC.get() + " in : " + maxStringC.get());
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Thread characterSearch(BlockingQueue<String> queue, char c, AtomicReference<String> maxStr, AtomicInteger max) {
        Runnable run = () -> {
            for (int i = 0; i < SIZEOFSTRINGS; i++) {
                String s;
                try {
                    s = queue.take();
                } catch (InterruptedException e) {
                    return;
                }
                int count = 0;
                for (int j = 0; j < STRINGLENGHT; j++) {
                    if (s.charAt(j) == c) {
                        count++;
                    }
                }
                if (count > max.get()) {
                    maxStr.set(s);
                    max.set(count);
                }
            }
        };
        return new Thread(run);
    }
}
