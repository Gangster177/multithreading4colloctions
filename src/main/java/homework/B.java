//package homework;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.atomic.AtomicReference;
//
//public class B {
//    public static BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
//    public static BlockingQueue<String> queueSafe = new ArrayBlockingQueue<>(10);
//    public static AtomicReference<String> textMax = new AtomicReference<>();
//
//    public static void main(String[] args) throws InterruptedException {
//        List<Thread> threads = new ArrayList<>();
//
//        Thread generate = new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                try {
//                    String text = generateText("abc", 100);
//                    queue.put(text);
//                    queueSafe.put(text);
//                } catch (InterruptedException e) {
//                    return;
//                }
//                System.out.println("ДОБАВИЛИ " + i);
//            }
//        });
//
//        Thread characterSearch = new Thread(() -> {
//            int max = 0;
//            for (int i = 0; i < 10; i++) {
//                String s;
//                try {
//                    s = queue.take();
//                } catch (InterruptedException e) {
//                    return;
//                }
//                int count = 0;
//                for (int j = 0; j < 100; j++) {
//                    if (s.charAt(j) == 'a') {
//                        count++;
//                    }
//                }
//                if (count > max) {
//                    textMax.set(s);
//                    max = count;
//                }
//                System.out.println("ВЗЯЛИ " + i);
//            }
//        });
//
//
//        System.out.println("Start thread");
//        threads.add(generate);
//        threads.add(characterSearch);
//        for (Thread thread : threads) {
//            thread.start();
//        }
//
//        System.out.println("Join thread");
//        for (Thread thread : threads) {
//            thread.join();
//        }
//
//        for (String s : queueSafe) {
//            System.out.println(s);
//        }
//        System.out.println("max: " + textMax.get());
//        System.out.println("Finished!");
//    }
//
//    public static String generateText(String letters, int length) {
//        Random random = new Random();
//        StringBuilder text = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            text.append(letters.charAt(random.nextInt(letters.length())));
//        }
//        return text.toString();
//    }
//}
