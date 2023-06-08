package homework;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static final String SYMBOL = "abc";

    public static final int MAXSIZE = 10;
    public static final int STRINGSIZE = 100;
    public static final int STRINGLENGHT = 1000;

    public static final char A = 'a';
    public static final char B = 'b';
    public static final char C = 'c';

    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(MAXSIZE);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(MAXSIZE);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(MAXSIZE);

//    public static AtomicReference<String> textMaxA = new AtomicReference<>();
//    public static AtomicReference<String> textMaxB = new AtomicReference<>();
//    public static AtomicReference<String> textMaxC = new AtomicReference<>();


    public static void main(String[] args) throws InterruptedException {

        Thread generate = new Thread(() -> {
            for (int i = 0; i < STRINGSIZE; i++) {
                try {
                    queueA.put(generateText(SYMBOL, STRINGLENGHT));
                    queueB.put(queueA.take());
                    queueC.put(queueB.take());
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        List<Thread> threads = new ArrayList<>();
        threads.add(generate);
        threads.add(characterSearch(queueA, A));
        threads.add(characterSearch(queueB, B));
        threads.add(characterSearch(queueC, C));

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("Finished!");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Thread characterSearch(BlockingQueue<String> queue, char ch) {
        AtomicReference<String> textMax = new AtomicReference<>();
        Runnable run = () -> {
            int max = 0;
            for (int i = 0; i < MAXSIZE; i++) {
                String s = null;
                try {
                    s = queue.take();
                    queue.put(s);
                } catch (InterruptedException e) {
                    return;
                }
                int count = 0;
                for (int j = 0; j < STRINGLENGHT; j++) {
                    if (s.charAt(j) == ch) {
                        count++;
                    }
                }
                if (count > max) {
                    textMax.set(s);
                    max = count;
                }
            }
        };
        return new Thread(run);
    }
}
