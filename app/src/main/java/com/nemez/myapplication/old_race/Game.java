package com.nemez.myapplication.old_race;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Game extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Полный экран
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Запуск игры
        Thread game = new Thread(new Runnable() {
            @Override
            public void run() {

                while (!fail) {
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(!fail) {
                        runOnUiThread(showCar);
                    }
                }

            }
        });

        //Плеер
        player.stop();
        //Звук
        player = MediaPlayer.create(this, R.raw.speed);
        player.start();

        //Старт игры
        game.start();

        //Конец onCreate
    }

    //системная кнопка Назад
    long backPressedTime;
    Toast backToast;
    @Override
    public void onBackPressed(){
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }
        else {
            backToast = Toast.makeText(getBaseContext(), R.string.exittext, Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    //Переменные:
    //Обочина
    int x = 10;
    int y = 20;
    //Жизни
    int health = 3;
    //Доп.жизнь
    boolean isHeart = false;
    boolean needHelp = true;
    int heartCoord;
    //Положение машины
    int carPosition = 409;
    //Рандомайзер
    Random rand = new Random();
    //Яма
    int holeCoord;
    boolean isHole = false;
    //Противники
    int enemyCoord;
    boolean isEnemy = false;
    int enemyStart = 0;
    //Босс
    int bossCoord;
    int bossHealth = 12;
    int bossMove = 0;
    boolean isBoss = false;
    //Очистка от пулемётной очереди
    ArrayList<Integer> antiFireList = new ArrayList<>();
    //Счёт игры
    int score = 0;
    //Скорость игры
    int speed = 300;
    boolean fail = false;
    //Звуки
    MediaPlayer player = new MediaPlayer();

    //Отображение объектов
    Runnable showCar = new Runnable(){
        public void run(){

            //Очистка поля от пулемётной очереди
            antiFire();
            antiFireList.clear();

            //Обочина
            road();

            //Машина
            car();

            //Босс
            if (score == 10 && !isBoss) {
                isBoss = true;
                bossCoord = 400;
            }
            if (isBoss){
                boss();
            }

            //Создание препятствий
            hole();

            //Противники
            if(enemyStart > 14){
                enemy();
            }

            //Доп.жизнь
            if(health == 1 && isBoss && needHelp){
                heart();
            }

            //Попадание
            if(holeCoord == carPosition + 1 || enemyCoord == carPosition + 1) {
                bang();
            }

            //Регулятор скорости
            gameSpeed();

            //Отображение счёта игры
            gameScore();

        }
    };

    //Отображение обочины
    void road(){

        Resources res = getResources();
        String str = "imageView" + x;
        int ident = res.getIdentifier(str, "id", getPackageName());
        ImageView test = (ImageView) findViewById(ident);
        test.setImageResource(R.color.yellow);

        Resources res2 = getResources();
        String str2 = "imageView" + y;
        int ident2 = res.getIdentifier(str2, "id", getPackageName());
        ImageView test2 = (ImageView) findViewById(ident2);
        test2.setImageResource(R.color.yellow);

        x++;
        y++;
        enemyStart++;

        if (x > 19 || y > 29){
            x=10;
            y=20;
        }

        Resources res3 = getResources();
        String str3 = "imageView" + x;
        int ident3 = res.getIdentifier(str3, "id", getPackageName());
        ImageView test3 = (ImageView) findViewById(ident3);
        test3.setImageResource(R.color.black);

        Resources res4 = getResources();
        String str4 = "imageView" + y;
        int ident4 = res.getIdentifier(str4, "id", getPackageName());
        ImageView test4 = (ImageView) findViewById(ident4);
        test4.setImageResource(R.color.black);
    }

    //Отображение машины
    void car(){

        //Очистка полей
        Resources res1 = getResources();
        String str1 = "imageView" + 209;
        int ident1 = res1.getIdentifier(str1, "id", getPackageName());
        ImageView test1 = (ImageView) findViewById(ident1);
        test1.setImageDrawable(null);

        Resources res2 = getResources();
        String str2 = "imageView" + 309;
        int ident2 = res2.getIdentifier(str2, "id", getPackageName());
        ImageView test2 = (ImageView) findViewById(ident2);
        test2.setImageDrawable(null);

        Resources res3 = getResources();
        String str3 = "imageView" + 409;
        int ident3 = res3.getIdentifier(str3, "id", getPackageName());
        ImageView test3 = (ImageView) findViewById(ident3);
        test3.setImageDrawable(null);

        Resources res4 = getResources();
        String str4 = "imageView" + 509;
        int ident4 = res4.getIdentifier(str4, "id", getPackageName());
        ImageView test4 = (ImageView) findViewById(ident4);
        test4.setImageDrawable(null);

        Resources res5 = getResources();
        String str5 = "imageView" + 609;
        int ident5 = res5.getIdentifier(str5, "id", getPackageName());
        ImageView test5 = (ImageView) findViewById(ident5);
        test5.setImageDrawable(null);

        //Установка машины
        Resources res = getResources();
        String str = "imageView" + carPosition;
        int ident = res.getIdentifier(str, "id", getPackageName());
        ImageView test = (ImageView) findViewById(ident);
        test.setImageResource(R.drawable.car);
    }

    //Босс
    void boss(){

        bossMove++;

        //Очистка поля
        Resources res = getResources();
        String str = "imageView" + bossCoord;
        int ident = res.getIdentifier(str, "id", getPackageName());
        ImageView test = (ImageView) findViewById(ident);
        test.setImageDrawable(null);

        //Перемещение
        if(bossMove % 100 > 80){
            bossCoord = 400;
            Resources res2 = getResources();
            String str2 = "imageView" + bossCoord;
            int ident2 = res2.getIdentifier(str2, "id", getPackageName());
            ImageView test2 = (ImageView) findViewById(ident2);
            test2.setImageResource(R.drawable.boss);
        }
        else if (bossMove % 100 > 70){
            bossCoord = 300;
            Resources res2 = getResources();
            String str2 = "imageView" + bossCoord;
            int ident2 = res2.getIdentifier(str2, "id", getPackageName());
            ImageView test2 = (ImageView) findViewById(ident2);
            test2.setImageResource(R.drawable.boss);
        }
        else if(bossMove % 100 > 60){
            bossCoord = 200;
            Resources res2 = getResources();
            String str2 = "imageView" + bossCoord;
            int ident2 = res2.getIdentifier(str2, "id", getPackageName());
            ImageView test2 = (ImageView) findViewById(ident2);
            test2.setImageResource(R.drawable.boss);
        }
        else if(bossMove % 100 > 50){
            bossCoord = 300;
            Resources res2 = getResources();
            String str2 = "imageView" + bossCoord;
            int ident2 = res2.getIdentifier(str2, "id", getPackageName());
            ImageView test2 = (ImageView) findViewById(ident2);
            test2.setImageResource(R.drawable.boss);
        }
        else if (bossMove % 100 > 40){
            bossCoord = 400;
            Resources res2 = getResources();
            String str2 = "imageView" + bossCoord;
            int ident2 = res2.getIdentifier(str2, "id", getPackageName());
            ImageView test2 = (ImageView) findViewById(ident2);
            test2.setImageResource(R.drawable.boss);
        }
        else if (bossMove % 100 > 30){
            bossCoord = 500;
            Resources res2 = getResources();
            String str2 = "imageView" + bossCoord;
            int ident2 = res2.getIdentifier(str2, "id", getPackageName());
            ImageView test2 = (ImageView) findViewById(ident2);
            test2.setImageResource(R.drawable.boss);
        }
        else if (bossMove % 100 > 20){
            bossCoord = 600;
            Resources res2 = getResources();
            String str2 = "imageView" + bossCoord;
            int ident2 = res2.getIdentifier(str2, "id", getPackageName());
            ImageView test2 = (ImageView) findViewById(ident2);
            test2.setImageResource(R.drawable.boss);
        }
        else if (bossMove % 100 > 10){
            bossCoord = 500;
            Resources res2 = getResources();
            String str2 = "imageView" + bossCoord;
            int ident2 = res2.getIdentifier(str2, "id", getPackageName());
            ImageView test2 = (ImageView) findViewById(ident2);
            test2.setImageResource(R.drawable.boss);
        }
        else{
        bossCoord = 400;
        Resources res2 = getResources();
        String str2 = "imageView" + bossCoord;
        int ident2 = res2.getIdentifier(str2, "id", getPackageName());
        ImageView test2 = (ImageView) findViewById(ident2);
        test2.setImageResource(R.drawable.boss);
    }


        //Стрельба Босса
        if(bossMove % 10 == 4 || bossMove % 10 == 5){
            Resources res5 = getResources();
            String str5 = "imageView" + (bossCoord + 1);
            int ident5 = res5.getIdentifier(str5, "id", getPackageName());
            ImageView test5 = (ImageView) findViewById(ident5);
            test5.setImageResource(R.drawable.flamethrower);
        }
        if(bossMove % 10 == 6 && bossHealth < 12){

            for(int i = 2; i < 10; i++){
                Resources res5 = getResources();
                String str5 = "imageView" + (bossCoord + i);
                int ident5 = res5.getIdentifier(str5, "id", getPackageName());
                ImageView test5 = (ImageView) findViewById(ident5);
                test5.setImageResource(R.drawable.flamethrower);

                //Попадание из огнемёта босса
                if(carPosition == (bossCoord + i)){
                    bang();
                }
            }
        }

        //Очистка поля от огнемёта
        if(bossMove % 10 == 7){
            for(int i = 1; i < 10; i++){
                Resources res5 = getResources();
                String str5 = "imageView" + (bossCoord + i);
                int ident5 = res5.getIdentifier(str5, "id", getPackageName());
                ImageView test5 = (ImageView) findViewById(ident5);
                test5.setImageDrawable(null);
            }
        }
    }

    //Создание препятствий
    void hole(){

        //Установка ямы, если её нет на поле
        if(!isHole){
            holeCoord = (rand.nextInt(5) + 2) * 100;
            while(holeCoord == enemyCoord || holeCoord == (enemyCoord - 1) || holeCoord == bossCoord || holeCoord == heartCoord || holeCoord == (heartCoord - 1)){
                holeCoord = (rand.nextInt(5) + 2) * 100;
            }
            isHole = true;
        }

        //Очистка поля
        if(holeCoord % 10 != 0){
            Resources res1 = getResources();
            String str1 = "imageView" + (holeCoord - 1);
            int ident1 = res1.getIdentifier(str1, "id", getPackageName());
            ImageView test1 = (ImageView) findViewById(ident1);
            test1.setImageDrawable(null);
        }

        //Отображение ямы
        Resources res = getResources();
        String str = "imageView" + holeCoord;
        int ident = res.getIdentifier(str, "id", getPackageName());
        ImageView test = (ImageView) findViewById(ident);
        test.setImageResource(R.drawable.hole);
        holeCoord = holeCoord + 1;

        //Уход ямы за экран
        if(holeCoord == 210 || holeCoord == 310 || holeCoord == 410 || holeCoord == 510 || holeCoord == 610){
            isHole = false;
        }
    }

    //Создание противников
    void enemy(){

        //Установка противника, если его нет на поле
        if(!isEnemy){
            enemyCoord = (rand.nextInt(5) + 2) * 100;
            while(enemyCoord== holeCoord || enemyCoord == (holeCoord - 1) || enemyCoord == bossCoord){//ЫЫЫ
                enemyCoord = (rand.nextInt(5) + 2) * 100;
            }
            isEnemy = true;
        }

        //Очистка поля
        if(enemyCoord % 10 != 0){
            Resources res1 = getResources();
            String str1 = "imageView" + (enemyCoord - 1);
            int ident1 = res1.getIdentifier(str1, "id", getPackageName());
            ImageView test1 = (ImageView) findViewById(ident1);
            test1.setImageDrawable(null);
        }

        //Отображение противника
        Resources res = getResources();
        String str = "imageView" + enemyCoord;
        int ident = res.getIdentifier(str, "id", getPackageName());
        ImageView test = (ImageView) findViewById(ident);
        test.setImageResource(R.drawable.enemy);
        enemyCoord = enemyCoord + 1;

        //Уход противника за экран
        if(enemyCoord == 210 || enemyCoord == 310 || enemyCoord == 410 || enemyCoord == 510 || enemyCoord == 610){
            isEnemy = false;
        }
    }

    //Попадание в игрока
    void bang(){
        //Уменьшение очков жизни
        health--;
        //Отображение взрыва
        Resources res = getResources();
        String str = "imageView" + carPosition;
        int ident = res.getIdentifier(str, "id", getPackageName());
        ImageView test = (ImageView) findViewById(ident);
        test.setImageResource(R.drawable.bang);
        //Проигрыш
        if(health <= 0){
        fail();}
    }

    //Поворот налево
    public void left(View view){
        if (carPosition > 209){
            carPosition = carPosition - 100;
            player = MediaPlayer.create(this, R.raw.turn);
            player.start();
        }
    }

    //Поворот направо
    public void right(View view){
        if (carPosition < 609){
            carPosition = carPosition + 100;
            player = MediaPlayer.create(this, R.raw.turn);
            player.start();
        }
    }

    //Стрельба
    public void fire(View view){
        for(int i = 1; i < 10; i++){
            Resources res = getResources();
            String str = "imageView" + (carPosition - i);
            antiFireList.add(carPosition - i);
            int ident = res.getIdentifier(str, "id", getPackageName());
            ImageView test = (ImageView) findViewById(ident);
            test.setImageResource(R.drawable.fire);
            //Попадание
            if(enemyCoord == carPosition - i){
                Resources res2 = getResources();
                String str2 = "imageView" + (carPosition - i);
                int ident2 = res.getIdentifier(str2, "id", getPackageName());
                ImageView test2 = (ImageView) findViewById(ident2);
                test2.setImageResource(R.drawable.bang);
                isEnemy = false;
                score++;
                //Звук
                player = MediaPlayer.create(this, R.raw.bang);
                player.start();
            }
            //Попадание в босса
            if(bossCoord == carPosition - i){
                bossHealth--;
                player = MediaPlayer.create(this, R.raw.armor);
                player.start();
                if(bossHealth == 0){
                    isBoss = false;
                    bossCoord = 0;
                    score = score +5;
                }
            }
        }
        //Звук
        player = MediaPlayer.create(this, R.raw.fire);
        player.start();
    }

    //Очистка поля от пулемётной очереди
    void antiFire(){
        for(int x : antiFireList){
            Resources res = getResources();
            String str = "imageView" + x;
            int ident = res.getIdentifier(str, "id", getPackageName());
            ImageView test = (ImageView) findViewById(ident);
            test.setImageDrawable(null);
        }
    }

    //Скорость игры
    void gameSpeed(){
        if(score > 20){
            speed = 150;
        }
        else if(score > 10){
            speed = 200;
        }
        else if(score > 3){
            speed = 250;
        }
    }

    //Счёт игры
    void gameScore(){
        TextView test = (TextView)findViewById(R.id.scoreView);
        test.setText("SCORE:" + score);
        TextView test2 = (TextView)findViewById(R.id.healthView);
        test2.setText("HEALTH: " + health);
        TextView test3 = (TextView)findViewById(R.id.bossView);
        test3.setText("___");
        if(isBoss){
            test3.setText("BOSS: " + bossHealth);
        }
    }

    //Дополнительная жизнь
    void heart() {

        //Создание сердца
        if(!isHeart) {
            heartCoord = (rand.nextInt(5) + 2) * 100;
            while (heartCoord / 100 == enemyCoord / 100 || heartCoord == bossCoord || heartCoord / 100 == holeCoord / 100) {
                heartCoord = (rand.nextInt(5) + 2) * 100;
            }
            isHeart = true;
        }

        //Очистка поля
        if(heartCoord % 10 != 0 || heartCoord == carPosition + 1){
        Resources res = getResources();
        String str = "imageView" + (heartCoord - 1);
        int ident = res.getIdentifier(str, "id", getPackageName());
        ImageView test = (ImageView) findViewById(ident);
        test.setImageDrawable(null);
        }
        heartCoord = heartCoord + 1;
        //Отображение
        Resources res = getResources();
        String str = "imageView" + (heartCoord - 1);
        int ident = res.getIdentifier(str, "id", getPackageName());
        ImageView test = (ImageView) findViewById(ident);
        test.setImageResource(R.drawable.heart);

        //Получение жизни
        if(heartCoord == carPosition + 1){
            health++;
            needHelp = false;
            isHeart = false;
        }

        //Уход жизни за экран
        if (heartCoord == 210 || heartCoord == 310 || heartCoord == 410 || heartCoord == 510 || heartCoord == 610) {
            isHeart = false;
            needHelp = false;
        }
    }

    //Проигрыш
    void fail(){
        fail = true;
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.faildialog);
        dialog.show();
        //Отображение счёта
        TextView test = (TextView)dialog.findViewById(R.id.failView);
        test.setText("SCORE: " + score);
        //Кнопка Продолжить в диалоге Прогресса
        Button menu = (Button) dialog.findViewById(R.id.failButton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(Game.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //Звук
        player = MediaPlayer.create(this, R.raw.fail);
        player.start();
    }
}