package com.risk.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 * Initiate the game view in java swings
 * @author sadgi
 *
 */
public class GameView {
	
	 private static JPanel gameActionJpanel = new JPanel(null);
	  private static JFrame gameJframe= null;

	  // Map Label
	  private static JLabel mapJlabel = new JLabel();
	  private static JScrollPane mapScrollPane = null;

	  // Reinforcement Label
	  private static JLabel reinforcementJlabel = new JLabel();
	  private static JLabel playersTurnJlabel = new JLabel("Default");
	  private static JLabel armyLeftJlabel = new JLabel("0");
	  private static JComboBox<String> addArmyToCountryJcomboBox = new JComboBox<>();
	  private static JButton addArmyButton = new JButton("Add Army To Country");

	  // Fortification Label
	  private static JLabel fortificationJlabel = new JLabel();
	  private static JComboBox<String> sourceCountry = new JComboBox<>();
	  private static JComboBox<String> destinationCountry = new JComboBox<>();
	  private static JComboBox<String> noOfArmyToMoveJcomboBox = new JComboBox<>();
	  private static JButton fortificationMoveButton = new JButton("Move Army");

	  // Save Game Button
	  private static JLabel saveButtonJlabel = new JLabel();
	  private static JButton saveButton = new JButton("Save");
	  	  
	  public void gameInitializer(){
			 gameJframe = new JFrame("Risk Game");
			    loadGameActionView();
			    gameJframe.add(gameActionJpanel);	
			    loadingReinforcementLabel();
			    loadingFortificationLabel();
			    loadingSaveGameButton();			    
			    gameJframe.setSize(1200, 700);
			    gameJframe.setVisible(true);
			    gameJframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 }
	  
	  public void loadGameActionView(){
		  gameActionJpanel.removeAll();
		  gameActionJpanel = new JPanel(null);
		  File imageFile = null;
		    imageFile = new File("assets/maps/"+"Africa" + ".bmp");
		    Image image;
		    ImageIcon icon = null;
		    try {
		      image = ImageIO.read(imageFile);
		      icon = new ImageIcon(image);
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		 /*
		  *NEED THIS INFO FROM CONTROLLER
		  *    for (int i = 0; i < Map.getCountryList().size(); i++) {
		        Country tempCountry = Map.getCountryList().get(i);
		        int[] coordinate = tempCountry.getCoordinate();
		        tempCountry.setPointInMapLabel(new JLabel("" + tempCountry.getSoilders()));
		        tempCountry.getPointInMapLabel().setFont(new Font("Courier", Font.BOLD, 20));
		        tempCountry.getPointInMapLabel().setForeground(tempCountry.getPlayer().getColor());
		        tempCountry.getPointInMapLabel().setBounds(coordinate[0], coordinate[1], 25, 25);
		        mapJlabel.add(tempCountry.getPointInMapLabel());
		      } */
		    mapJlabel = new JLabel(icon);
		    mapScrollPane = new JScrollPane(mapJlabel);
		    mapScrollPane.setBounds(10, 10, 700, 650);
		    mapScrollPane.setBorder(new TitledBorder(""));
		    gameActionJpanel.add(mapScrollPane);
		    
	  }
	  
	  private  void loadingReinforcementLabel(){
		    reinforcementJlabel.removeAll();
		    reinforcementJlabel = null;
		    reinforcementJlabel = new JLabel();
		    reinforcementJlabel.setBorder(BorderFactory.createTitledBorder(null, "Initialization Phase",
		        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
		        new Font("SansSerif", Font.PLAIN, 12), Color.BLUE));
		    reinforcementJlabel.setBounds(mapScrollPane.getX()+700,
		        mapScrollPane.getY(), 490,
		     180);
		 // Recreate every components in Label
		    playersTurnJlabel = new JLabel(
		        "Player A");
		    playersTurnJlabel.setBorder(new TitledBorder("Active Player"));
		    playersTurnJlabel.setBounds(5, 20, reinforcementJlabel.getWidth() / 2 - 8,
		        reinforcementJlabel.getHeight() / 3 - 15);
		    
		    // listOfCountriesPlayerOccupied(Map.playerWithTurn());
		  //  String[] countryNameList = Map.playerWithTurn().listOfCountriesPlayerConqueredInStringArray();
		    String[] countryNameList={"INDIA","PAKISTAN","NEPAL"};
		    addArmyToCountryJcomboBox = new JComboBox<>(countryNameList);
		    addArmyToCountryJcomboBox.setBorder(new TitledBorder("Add Army To Country"));
		    addArmyToCountryJcomboBox.setBounds(playersTurnJlabel.getX() + playersTurnJlabel.getWidth() + 3,
		        playersTurnJlabel.getY(), playersTurnJlabel.getWidth(), playersTurnJlabel.getHeight());
		    
		    armyLeftJlabel = new JLabel("ARMY NEEDS TO BE ASSIGNED=10");
		    armyLeftJlabel.setBorder(new TitledBorder("Armies Left"));
		    armyLeftJlabel.setBounds(playersTurnJlabel.getX(),
		    		playersTurnJlabel.getY() + playersTurnJlabel.getHeight() + 5, playersTurnJlabel.getWidth(),
		        playersTurnJlabel.getHeight());
		    addArmyButton.setBounds(addArmyToCountryJcomboBox.getX(), armyLeftJlabel.getY(),
		            armyLeftJlabel.getWidth(), armyLeftJlabel.getHeight());

		    reinforcementJlabel.add(playersTurnJlabel);
		    reinforcementJlabel.add(playersTurnJlabel);
		    reinforcementJlabel.add(armyLeftJlabel);
		    reinforcementJlabel.add(addArmyToCountryJcomboBox);
		    reinforcementJlabel.add(addArmyButton);
		    gameActionJpanel.add(reinforcementJlabel);
		  
	  }
	  
	  private static void loadingFortificationLabel() {
		  fortificationJlabel.removeAll();
		    fortificationJlabel = null;
		    fortificationJlabel = new JLabel();
		    fortificationJlabel.setBorder(
		        BorderFactory.createTitledBorder(null, "Fortification Phase", TitledBorder.DEFAULT_JUSTIFICATION,
		            TitledBorder.DEFAULT_POSITION, new Font("SansSerif", Font.PLAIN, 12), Color.BLUE));
		    fortificationJlabel.setBounds(reinforcementJlabel.getX(),
		    		reinforcementJlabel.getY() + 10 + reinforcementJlabel.getHeight(),
		    		reinforcementJlabel.getWidth(), 140);
		    // Recreate every components in Label
		    String conquerdCountries[]={"Country A","Country B","Country C","Country D"};
		    sourceCountry =
		        new JComboBox<>(conquerdCountries);
		    sourceCountry.setBorder(new TitledBorder("Source Country"));
		    sourceCountry.setBounds(20, 20, 252, 52);
	        String destinationCountries[]={"Country A","Country B","Country C","Country D"};
		    destinationCountry = new JComboBox<>(destinationCountries);
		    destinationCountry.setBorder(new TitledBorder("Destination Country"));
		    destinationCountry.setBounds(
		        sourceCountry.getX() + sourceCountry.getWidth() + 10,
		        sourceCountry.getY(), 200,
		        sourceCountry.getHeight());

		    noOfArmyToMoveJcomboBox.setBounds(sourceCountry.getX(),
		        sourceCountry.getHeight() + sourceCountry.getY() + 7,
		        sourceCountry.getWidth(), sourceCountry.getHeight());
		    noOfArmyToMoveJcomboBox.setBorder(new TitledBorder("Total number of army to move"));

		    fortificationMoveButton.setBounds(destinationCountry.getX(), noOfArmyToMoveJcomboBox.getY(),
		        destinationCountry.getWidth(), destinationCountry.getHeight());
		    

		    // Add all components in Label
		    fortificationJlabel.add(sourceCountry);
		    fortificationJlabel.add(destinationCountry);
		    fortificationJlabel.add(noOfArmyToMoveJcomboBox);
		    fortificationJlabel.add(fortificationMoveButton);
		    // Adding Label to Panel
		    gameActionJpanel.add(fortificationJlabel);  
		  
		  
	  }
	  
	  private static void loadingSaveGameButton() {
		    saveButtonJlabel.removeAll();
		    saveButtonJlabel = null;
		    saveButtonJlabel = new JLabel();
		    saveButtonJlabel
		        .setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION,
		            TitledBorder.DEFAULT_POSITION, new Font("SansSerif", Font.PLAIN, 12), Color.BLUE));
		    saveButtonJlabel.setBounds(fortificationJlabel.getX(),
		        fortificationJlabel.getY() + 10 + fortificationJlabel.getHeight(),
		        fortificationJlabel.getWidth(), fortificationJlabel.getHeight() - 13);

		    saveButton = new JButton("Save Game");
		    int buttonHeight = 25;
		    int buttonWidth = 100;
		    saveButton.setBounds(saveButtonJlabel.getWidth() / 2 - buttonWidth / 2,
		        saveButtonJlabel.getHeight() / 2 - buttonHeight / 2, buttonWidth, buttonHeight);
		    
		    saveButtonJlabel.add(saveButton);

		    gameActionJpanel.add(saveButtonJlabel);
		  }
	  public static void main(String[] args) {
			 GameView gameView=new GameView();
	    	 gameView.gameInitializer();
			
	}

	 

}
