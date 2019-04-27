import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import static javax.swing.JOptionPane.YES_NO_OPTION;

public class Main {
    JFrame frame;
    panel panel;
    snake snake;
    Random random;
    Food Food;
    Container cont;

    boolean isRainbowOn, gameOver;
    int POINT_RADIUS = 20;
    int FIELD_WIDTH = 30;
    int FIELD_HEIGHT = 20;

    JMenuItem menuItem1;


    public Main(){
        frame = new JFrame();
        panel = new panel();
        random = new Random();
        frame.setJMenuBar(createMenu());
        isRainbowOn = true;
        gameOver = false;
    }

    public JMenuBar createMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        JMenu menu1 = new JMenu("Exit");
        JMenuItem menuItem = new JMenuItem("Turn off the rainbow (Shift + R)");
        JMenuItem menuItem2 = new JMenuItem("Bye-bye");
        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(menuItem.getText().equals("Turn on the rainbow (Shift + R)")){
                    isRainbowOn = true;
                    menuItem.setText("Turn off the rainbow (Shift + R)");
                }
                else{
                    isRainbowOn = false;
                    menuItem.setText("Turn on the rainbow (Shift + R)");
                }
            }
        });

        menu.add(menuItem);
        menuItem1 = new JMenuItem("Make everything bigger (Shift + B)");
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(menuItem1.getText().equals("Make everything bigger (Shift + B)")){
                    POINT_RADIUS = 40;
                    FIELD_WIDTH = 15;
                    FIELD_HEIGHT = 10;
                    frame.setVisible(false);
                    frame.setBounds(30, 30, POINT_RADIUS * FIELD_WIDTH, POINT_RADIUS * FIELD_HEIGHT);
                    frame.setVisible(true);
                    menuItem1.setText("Make everything smaller (Shift + B)");
                }
                else{
                    POINT_RADIUS = 20;
                    FIELD_WIDTH = 30;
                    FIELD_HEIGHT = 20;
                    frame.setVisible(false);
                    frame.setBounds(30, 30, POINT_RADIUS * FIELD_WIDTH, POINT_RADIUS * FIELD_HEIGHT);
                    frame.setVisible(true);
                    menuItem1.setText("Make everything bigger (Shift + B)");
                }
            }
        });
        menu1.add(menuItem2);
        menu.add(menuItem1);
        menuBar.add(menu);
        menuBar.add(menu1);
        return menuBar;
    }

    void go(){
        while(true){
            frame.setBounds(30, 30, POINT_RADIUS * FIELD_WIDTH, POINT_RADIUS * FIELD_HEIGHT);

            frame.setFocusable(true);
            cont = frame.getContentPane();
            cont.add(panel);
            frame.setDefaultCloseOperation(3);
            frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() > 36 && e.getKeyCode() < 41){
                        if (Math.abs(snake.direction - e.getKeyCode()) != 2){
                            snake.direction = e.getKeyCode();
                        }
                    }
                    if(e.getKeyCode() == KeyEvent.VK_R && e.isShiftDown()){
                        isRainbowOn = !isRainbowOn;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_B && e.isShiftDown()){
                        if(menuItem1.getText().equals("Make everything bigger (Shift + B)")){
                            POINT_RADIUS = 40;
                            FIELD_WIDTH = 15;
                            FIELD_HEIGHT = 10;
                            frame.setVisible(false);
                            frame.setBounds(30, 30, POINT_RADIUS * FIELD_WIDTH, POINT_RADIUS * FIELD_HEIGHT);
                            frame.setVisible(true);
                            menuItem1.setText("Make everything smaller (Shift + B)");
                        }
                        else{
                            POINT_RADIUS = 20;
                            FIELD_WIDTH = 30;
                            FIELD_HEIGHT = 20;
                            frame.setVisible(false);
                            frame.setBounds(30, 30, POINT_RADIUS * FIELD_WIDTH, POINT_RADIUS * FIELD_HEIGHT);
                            frame.setVisible(true);
                            menuItem1.setText("Make everything bigger (Shift + B)");
                        }
                    }
                }
            });
            frame.setVisible(true);



            if(!gameOver){
                snake = new snake(6, 39, 5, 5);
                Food = new Food();
                Food.next();
            }

            while(!gameOver){
                FIELD_WIDTH = frame.getWidth() / POINT_RADIUS;
                FIELD_HEIGHT = frame.getHeight() / POINT_RADIUS;
                if(FIELD_WIDTH < 10){
                    FIELD_WIDTH += 10;
                }
                if(FIELD_HEIGHT < 5){
                    FIELD_HEIGHT += 10;
                }
                frame.setBounds(30, 30, POINT_RADIUS * FIELD_WIDTH, POINT_RADIUS * FIELD_HEIGHT);

                System.out.println(FIELD_WIDTH + " " + Food.getX());

                if(Food.getX() >= FIELD_WIDTH || Food.getY() >= FIELD_HEIGHT){
                    Food.next();
                }

                snake.move();
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                panel.repaint();
            }
            if(gameOver){
                frame.setVisible(false);
                UIManager.put("OptionPane.yesButtonText"   , "Exit"    );
                UIManager.put("OptionPane.noButtonText"    , "Try Again"   );
                int tryAgainOrExit = JOptionPane.showConfirmDialog(null, "Do you want to exit or try again?", "Exit?", YES_NO_OPTION);
                if(tryAgainOrExit == 0){
                    System.exit(0);
                }
                else{
                    gameOver = false;
                }

            }
        }
    }


    class snake{
        ArrayList<Point> snake = new ArrayList<Point>();
        int direction;

        public snake(int lenght, int direction, int x, int y){
            for (int i = 0; i < lenght; i++) {
                Point point = new Point(x- i, y);
                snake.add(point);
            }
            this.direction = direction;
        }

        boolean isInsideSnake(int x, int y) {
            for (Point point : snake) {
                if ((point.getX() == x) && (point.getY() == y)) {
                    return true;
                }
            }
            return false;
        }

        public void move(){
            int x = snake.get(0).getX();
            int y = snake.get(0).getY();
            if (direction == 37) { x--; }
            if (direction == 39) { x++; }
            if (direction == 38) { y--; }
            if (direction == 40) { y++; }
            if (x > FIELD_WIDTH - 1) { x = 0; }
            if (x < 0) { x = FIELD_WIDTH - 1; }
            if (y > FIELD_HEIGHT - 1) { y = 0; }
            if (y < 0) { y = FIELD_HEIGHT - 1; }
            for (Point point : snake){
                if ((point.getX() == x) && (point.getY() == y)){
                    gameOver = true;
                }
            }

            snake.add(0, new Point(x, y));

            if((snake.get(0).getX() == Food.getX() && snake.get(0).getY() == Food.getY())){
                Food.next();
            }
            else snake.remove(snake.size() - 1);


        }



        public void paint(Graphics g){
            for (Point point : snake){
                if (point.isEqual(snake.get(0))){
                    g.setColor(Color.orange);
                    point.paint(g);
                    g.setColor(Color.black);
                }
                else{
                    if(isRainbowOn){
                        int RED = random.nextInt(255);
                        int GREEN = random.nextInt(255);
                        int BLUE = random.nextInt(255);
                        g.setColor(new Color(RED, GREEN, BLUE));
                    }
                    point.paint(g);
                }
            }
        }
    }

    class Food extends Point{
        public Food(){
            super(-1, -1);
        }

        boolean doYouNeedNewColor = true;
        Color color = Color.GREEN;
        int BLUE, RED;

        void next() {
            int x, y;
            do {
                x = random.nextInt(FIELD_WIDTH);
                y = random.nextInt(FIELD_HEIGHT - 5);
            } while (snake.isInsideSnake(x, y));
            this.setXY(x, y);
            doYouNeedNewColor = true;
        }

        void paint(Graphics g){
            if(doYouNeedNewColor){
                boolean theRedderOne = random.nextBoolean();
                if(theRedderOne){
                    BLUE = 0;
                    RED = random.nextInt(170);
                }
                if(!theRedderOne){
                    RED = 0;
                    BLUE = random.nextInt(170);
                }

                color = new Color(RED, 255, BLUE);
                System.out.println("This is a new color! " + RED + " " + BLUE);
                doYouNeedNewColor = false;
            }
            g.setColor(color);
            g.fillOval(x * POINT_RADIUS, y * POINT_RADIUS, POINT_RADIUS, POINT_RADIUS);
        }
    }

    class Point{
        int x, y;
        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }
        int getX(){
            return x;
        }
        int getY(){
            return y;
        }

        void setXY(int x, int y){
            this.x = x;
            this.y = y;
        }

        void paint(Graphics g) {
            g.fillOval(x * POINT_RADIUS, y * POINT_RADIUS, POINT_RADIUS, POINT_RADIUS);
        }

        public boolean isEqual(Point point){
            return this.x == point.x && this.y == point.y;
        }


    }


    class panel extends JPanel{


        public panel(){

        }

        @Override
        public void paint(Graphics g){
            super.paint(g);
            if(!gameOver){
                snake.paint(g);
                Food.paint(g);
            }

        }



    }


    public static void main(String[] args) {
        Main main = new Main();
        main.go();
    }
}
