import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {

    static AtomicInteger length3 = new AtomicInteger(0);
    static AtomicInteger length4 = new AtomicInteger(0);
    static AtomicInteger length5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread thread1 = new Thread(() -> {
            for (String text : texts) {
                if (IntStream.range(0, text.length() / 2).noneMatch(i -> text.charAt(i) != text.charAt(text.length() - i - 1))) {
                    increment(text);
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            for (String text : texts) {
                if (IntStream.range(1, text.length()).noneMatch(i -> text.charAt(i) != text.charAt(0))) {
                    increment(text);
                }
            }
        });
        Thread thread3 = new Thread(() -> {
            for (String text : texts) {
                if (IntStream.range(1, text.length()).noneMatch(i -> text.charAt(i) < text.charAt(i - 1))) {
                    increment(text);
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("Красивых слов с длиной 3: " + length3 + " шт");
        System.out.println("Красивых слов с длиной 4: " + length4 + " шт");
        System.out.println("Красивых слов с длиной 5: " + length5 + " шт");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void increment(String text) {
        if (text.length() == 3) {
            length3.getAndIncrement();
        }
        if (text.length() == 4) {
            length4.getAndIncrement();
        }
        if (text.length() == 5) {
            length5.getAndIncrement();
        }
    }
}
