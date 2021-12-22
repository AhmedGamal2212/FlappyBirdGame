/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.flappybird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.BUTTON1;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;
/**
 *
 * @author gemmy
 */
public class FlappyBird implements ActionListener, KeyListener, MouseListener{
    
    
    public static void main(String[] args) throws Exception{
        flappyBird = new FlappyBird();
    }
    public static FlappyBird flappyBird;
    public BirdRenderer birdRenderer;
    public Rectangle bird;
    public int ticks, yMotion, score;
    public ArrayList<Rectangle> obstacleList;
    public Random random;
    public boolean gameOver, gameStarted;
    public final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 800;
    
    public FlappyBird(){
        JFrame jFrame = new JFrame();
        Timer timer = new Timer(20, this);
        
        birdRenderer = new BirdRenderer();
        random = new Random();
        jFrame.add(birdRenderer);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        jFrame.addKeyListener(this);
        jFrame.addMouseListener(this);
        jFrame.setResizable(false);
        jFrame.setTitle("Flappy Bird Game");
        jFrame.setVisible(true);
        
        bird = new Rectangle(SCREEN_WIDTH / 2 - 10, SCREEN_HEIGHT / 2 - 10);
        obstacleList = new ArrayList<>();
        
        for(int i = 0; i < 4; i++){
            addObstacle(true);
        }
        
        timer.start();
        
    }
    
    public void jump(){
        if(gameOver){
            bird = new Rectangle(SCREEN_WIDTH / 2 - 10, SCREEN_HEIGHT / 2 - 10, 20, 20);
            obstacleList.clear();
            yMotion = 0; score = 0;
            
            for(int i = 0; i < 4; i++){
                addObstacle(true);
            }
            gameOver = false;
        }
        if(!gameStarted){
            gameStarted = true;
        }else if(!gameOver){
            if(yMotion > 0){
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }
    
    public void addObstacle(boolean gameStarted){
        int space = 300, width = 100, height = 50 + random.nextInt(300);
        if(gameStarted){
            obstacleList.add(new Rectangle(SCREEN_WIDTH + width + obstacleList.size() * 300, SCREEN_HEIGHT - height - 120, width, height));
            obstacleList.add(new Rectangle(SCREEN_WIDTH + width + (obstacleList.size() - 1) * 300, 0, width, SCREEN_HEIGHT - height - space));
        }else{
            obstacleList.add(new Rectangle(obstacleList.get(obstacleList.size() - 1).x + 600, SCREEN_HEIGHT - height - 120, width, height));
            obstacleList.add(new Rectangle(obstacleList.get(obstacleList.size() - 1).x, 0, width, SCREEN_HEIGHT - height - space));
        }
    }
    
    public void repaint(Graphics g){
        g.setColor(Color.black.darker().darker());
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g.setColor(Color.white);
        g.fillRect(0, SCREEN_HEIGHT - 120, SCREEN_WIDTH, 120);
        
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, SCREEN_HEIGHT - 120, SCREEN_WIDTH, 120);
        
        g.setColor(Color.white);
        g.fillRect(bird.x, bird.y, bird.height, bird.width);
        
        
        for(Rectangle obstacle : obstacleList){
            paintObstacle(g, obstacle);
            
        }
        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 80));
        
        if(!gameStarted){
            g.drawString("Click to Begin", 75, SCREEN_HEIGHT / 2 - 50);
        }
        
        if(gameOver){
            g.drawString("Game Over", 100, SCREEN_HEIGHT / 2 - 50);
        }
        
        if(!gameOver && gameStarted){
            g.drawString(String.valueOf(score), SCREEN_WIDTH / 2 - 25, 100);
        }
        
        
    }
    
    
    public void paintObstacle(Graphics g, Rectangle obstacle){
        g.setColor(Color.white.darker().darker());
        
        g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        int speed = 10;
        ticks++;
        if(gameStarted){
            for(int i = 0; i < obstacleList.size(); i++){
                Rectangle obstacle = obstacleList.get(i);
                obstacle.x -= speed;
            }
            if(ticks % 2 == 0 && yMotion < 15){
                yMotion += 2;
            }
            
            for(int i = 0; i < obstacleList.size(); i++){
                Rectangle obstacle = obstacleList.get(i);
                if(obstacle.x + obstacle.width < 0){
                    obstacleList.remove(obstacle);
                    if(obstacle.y == 0){
                        addObstacle(false);
                    }
                }
            }
            bird.y += yMotion;
            
            for(Rectangle obstacle : obstacleList){
                if(obstacle.y == 0 && bird.x + bird.width / 2 > obstacle.x + obstacle.width / 2 - 10 && bird.x + bird.width / 2 < obstacle.x + obstacle.width / 2 + 10){
                    score++;
                }
                if(obstacle.intersects(bird)){
                    gameOver = true;
                    if(bird.x <= obstacle.x){
                        bird.x = obstacle.x - bird.width;
                        
                    }else{
                        if(obstacle.y != 0){
                            bird.y = obstacle.y - bird.height;
                        }else if(bird.y < obstacle.height){
                            bird.y = obstacle.height;
                        }
                    }
                }
            }
            if(bird.y > SCREEN_HEIGHT - 120 || bird.y < 0){
                gameOver = true;
            }
            if(bird.y + yMotion >= SCREEN_HEIGHT - 120){
                bird.y = SCREEN_HEIGHT - 120 - bird.height;
            }
        }
        birdRenderer.repaint();
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if(ke.getKeyCode() == KeyEvent.VK_SPACE){
            jump();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
            jump();
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
    
}
