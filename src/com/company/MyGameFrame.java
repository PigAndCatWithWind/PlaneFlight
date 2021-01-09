package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

public class MyGameFrame extends Frame {

    Image bg = GameUtil.getImage("images/bg.jpg");
    Image planeImg = GameUtil.getImage("images/plane.png");
    Plane plane = new Plane(planeImg,250,250);
    Shell[] shells = new Shell[10];
    Explode explode;
    Date startTime = new Date();
    Date endTime ;
    int period;
    @Override
    public void paint(Graphics g) {
        g.drawImage(bg,0,0,null);
        plane.drawSelf(g);
        for(int i=0;i<shells.length;i++){
            shells[i].draw(g);

            if (shells[i].getRect().intersects(plane.getRect())){
                plane.live = false;
                if (explode == null) {
                    explode = new Explode(plane.x, plane.y);
                    startTime = new Date();
                    period = (int)((endTime.getTime()-startTime.getTime())/1000);
                }
                explode.draw(g);
            }

            if (!plane.live){
                g.setColor(Color.white);
                g.setFont(new Font("",Font.BOLD,18));
                g.drawString("live time "+period+" s",(int)plane.x,(int)plane.y);
            }
        }
    }


    class KeyMonitor extends KeyAdapter{

        @Override
        public void keyPressed(KeyEvent e) {
            plane.addDirection(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            plane.minusDirection(e);
        }
    }


    class PaintThread extends Thread{
        @Override
        public void run() {
            while (true){
                repaint();

                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void launchFrame(){
        setTitle("flay");
        setVisible(true);
        setSize(Constant.GAME_WIDTH,Constant.GAME_HEIGH);
        setLocation(300,300);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        for (int i=0;i<shells.length;i++){
            shells[i] = new Shell();
        }
        new PaintThread().start();
        addKeyListener(new KeyMonitor());
    }

    public static void main(String[] args){
        MyGameFrame f = new MyGameFrame();
        f.launchFrame();
    }


    private Image offScreenImage = null;
    public void update(Graphics g){
        if(offScreenImage == null)
            offScreenImage = this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGH);

        Graphics gOff = offScreenImage.getGraphics();
        paint(gOff);
        g.drawImage(offScreenImage,0,0,null);
    }
}
