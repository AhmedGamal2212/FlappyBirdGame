/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.flappybird;
import javax.swing.JPanel;
import java.awt.Graphics;

/**
 *
 * @author gemmy
 */
public class BirdRenderer extends JPanel{
    private static final long serialVersionUID = 1L;
    
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        FlappyBird.flappyBird.repaint(g);
        
        
    }
}
