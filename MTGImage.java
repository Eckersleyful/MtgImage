/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtgimage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Ilmari
 */
public class MTGImage {

    /**
     * @param args the command line arguments
     */
    Dimension minimumDim;
    JFrame mainFrame;
    JButton generateButton;
    JLabel picLabel;
    int[][] realCardXY;
    
    public MTGImage(){
        defineFrame();
        runTime();
    }
    public static void main(String[] args) {
        // TODO code application logic here
        MTGImage startObject = new MTGImage();
        
    }
    public void defineFrame(){
        minimumDim = new Dimension(300,600);
        mainFrame = new JFrame("Card Holder");
        mainFrame.setSize(300, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setMinimumSize(minimumDim);
        mainFrame.setMaximumSize(minimumDim);
        mainFrame.setResizable(false);
        //generateButton = new JButton("Generate a random MTG-card");
        //mainFrame.add(generateButton);
        picLabel = new JLabel("",SwingConstants.CENTER);
      
        mainFrame.getContentPane().add(picLabel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
    public void runTime(){
        try{
            while(true){
              randomizeID();
              
              Thread.sleep(2000);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void randomizeID(){
        try{
            int maxID = 457295;
            int minID = 1;
            int random = (minID ) + (int)(Math.random()* ( maxID - minID + 1));
            String baseURL = "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid="+random+"&type=card";
            scrapeCard(random);
            System.out.println(baseURL);
            URL url = new URL(baseURL);
            BufferedImage img = ImageIO.read(url);
            double percentage = cardComparer(img);
            if(percentage>=10){
            picLabel.setIcon(new ImageIcon(img));
            }
            else 
                randomizeID();
                
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public double cardComparer(BufferedImage cardToCompare) throws InterruptedException{
        
        
        //Compare the randomized card with the default image of a card back
        
        BufferedImage compareCard = null;
        try{
        double differentPixel = 0;
        //resize the comparable image to the size of the cardback
        System.out.println();
        
        //BufferedImage exampleImage = ImageIO.read(getClass().getResource(("cardback.jpg")));
        BufferedImage exampleImage = ImageIO.read((Toolkit.getDefaultToolkit().getClass().getResource("/cardback.jpg")));
        if(exampleImage != null){
        //if the example image was loaded correctly, resize
        compareCard = resize(cardToCompare, exampleImage.getHeight(), exampleImage.getWidth());
        }
       
                
        int totalPixels = (exampleImage.getWidth() * exampleImage.getHeight())*3;
        
        for(int i = 0; i < exampleImage.getWidth(); i++){
            for(int a = 0; a< exampleImage.getHeight(); a++){
                Color backColor = new Color(exampleImage.getRGB(i,a));
                Color cardColor = new Color(compareCard.getRGB(i,a));
                
                differentPixel += Math.abs(backColor.getBlue() - cardColor.getBlue());
                differentPixel += Math.abs(backColor.getRed() - cardColor.getRed());
                differentPixel += Math.abs(backColor.getGreen() - cardColor.getGreen());
            }
        }
        
        double avg_pixels = differentPixel/totalPixels;
        double totalDifference = ((avg_pixels / 255) * 100);
        System.out.println("The images differ by " + totalDifference+ " percent.");
        
        return totalDifference;
                   
        }
        catch(IOException e){
            System.out.println(e.getClass() + e.getMessage());
        }
         return 0;
   }
    
    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
    private static ArrayList<String> scrapeCard(int id){
        ArrayList<String> cardInfo = new ArrayList();
        try{

        String url = "http://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid="+id;
        Document document = Jsoup.connect(url).followRedirects(true).timeout(6000).get();
        //cardInfo.add(document.body().select(".cardtextbox").get(0).text());
        //cardInfo.add(document.body().select(".value").get(0).text());
            System.out.println(document.body().select(".cardtextbox").get(0).text());
            System.out.println(document.body().select(".value").get(0).text());
        /*for(String g : cardInfo){
            System.out.println(g);
        }*/
        return cardInfo;
        }
        catch(Exception e){
            //System.out.println(e.getClass() + "\n"+ e.getMessage());
        }
        return null;
    }
}
