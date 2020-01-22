import sun.awt.windows.ThemeReader;

import java.util.Arrays;
import java.util.concurrent.*;

public class ClassMain {
    public static final int CARS_COUNT = 4;
    private static volatile boolean startRace = false;

    public static void main(String[] args) {

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));


        Car[] cars = new Car[CARS_COUNT];

        // передаем параметры машины
        for (int i = 0; i < cars.length; i++) {

            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }

        // Общий старт
        CyclicBarrier commonStart = new CyclicBarrier(CARS_COUNT);
        // Все машиные завершили гонку
        CountDownLatch allCarArrived = new CountDownLatch(CARS_COUNT);

        // Создаем пул потоков
        ExecutorService service = Executors.newFixedThreadPool(CARS_COUNT);

        //
        ArrayBlockingQueue<String> limitTunnel= new ArrayBlockingQueue<>(CARS_COUNT/2);

        for (int i = 0; i < cars.length; i++) {
            final int w = i;
            Thread t1 = new Thread(()->{

                cars[w].runPrepare();

                // Запуск машин начинает после подготовки всех машин
                try {
                    commonStart.await();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

                if (w == (cars.length-1)) {
                    System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
                }

                cars[w].run(limitTunnel);
                allCarArrived.countDown();

                });

            service.execute(t1);

        }

       new Thread(()-> {

           try {
               allCarArrived.await();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");

       }).start();

        service.shutdown();

    }
}
