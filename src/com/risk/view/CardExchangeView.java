package com.risk.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;
import com.risk.helper.CardEnum;
import com.risk.helper.IOHelper;
import com.risk.model.Game;

/**
 * This class is used to display the card exchange view.
 * 
 * @author sadgi
 * @version 1.0.0
 * @since 01-November-2018
 */
public class CardExchangeView implements Observer {
	private static JFrame cardFrame = null;
	private static JPanel cardPanel;
	private static JLabel cardExchangeLabel;
	private static JLabel playersTurnJlabel;
	private static JList<String> palyerOwnedCard;
	private static JLabel totalNewArmies;
	private static JButton exchangeButton;
	private static JButton exitButton;

	/**
	 * This method is used to initialize the Card Exchange View.
	 * 
	 * @param game, Game
	 */
	public void exchangeInitializerView(Game game) {
		exchangeButton=new JButton("Exchange Cards");
		exitButton=new JButton("No Exchange And Exit");
		cardFrame = new JFrame("Card Exchange View");
		cardPanel = new JPanel(null);
		cardFrame.setSize(800, 600);

		cardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cardExchangeLabel = new JLabel();
		cardExchangeLabel
				.setBorder(BorderFactory.createTitledBorder(null, "Exchange Card", TitledBorder.DEFAULT_JUSTIFICATION,
						TitledBorder.DEFAULT_POSITION, new Font("SansSerif", Font.PLAIN, 12), Color.BLACK));
		cardExchangeLabel.setBounds(100, 100, 600, 400);
		playersTurnJlabel = new JLabel(game.getCurrentPlayer().getName());
		Font font = new Font("Courier", Font.BOLD, 24);
		playersTurnJlabel.setFont(font);
		playersTurnJlabel.setForeground(Color.RED);
		playersTurnJlabel.setBorder(new TitledBorder("Active Player"));
		playersTurnJlabel.setBounds(30, 45, 250, 70);
		ArrayList<CardEnum> typeOfCards = game.getCurrentPlayer().getCards();
		String cards[] = new String[typeOfCards.size()];
		for (int i = 0; i < typeOfCards.size(); i++) {
			cards[i] = typeOfCards.get(i).toString();
		}
		palyerOwnedCard = new JList<>(cards);
		palyerOwnedCard.setBorder(new TitledBorder("Cards Owned"));
		palyerOwnedCard.setBounds(310, 45, 250, 70);
		totalNewArmies = new JLabel("" + game.getCurrentPlayer().getNoOfTradedArmies());
		totalNewArmies.setBorder(new TitledBorder("New  Armies Assigned"));
		totalNewArmies.setBounds(180, 150, 250, 70);
		exchangeButton.setBounds(120, 255, 160, 40);
		exchangeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (palyerOwnedCard.getSelectedValuesList() != null &&  palyerOwnedCard.getSelectedValuesList().size() > 0) {
					ArrayList<String> selectedCards = (ArrayList<String>) palyerOwnedCard.getSelectedValuesList();

					game.tradeCards(selectedCards);
					palyerOwnedCard.removeAll();
				}
			}
		});
		exitButton.setBounds(310, 255, 160, 40);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				boolean checkCount = game.getCurrentPlayer().isAssigningReinforcementArmiesAllowed();
				if (checkCount) {
					cardFrame.dispose();
					game.reinforcementPhaseSetup();
				} else {
					IOHelper.print("Cannot Exit Without Exchange");
				}

			}

		});
		cardExchangeLabel.add(totalNewArmies);
		cardExchangeLabel.add(palyerOwnedCard);
		cardExchangeLabel.add(playersTurnJlabel);
		cardExchangeLabel.add(exchangeButton);
		cardExchangeLabel.add(exitButton);
		cardPanel.add(cardExchangeLabel);
		cardFrame.add(cardPanel);
		cardFrame.setVisible(true);

	}

	public void updateCardView(Game game) {

		if (game.getCurrentPlayer() != null && totalNewArmies != null) {
			totalNewArmies.setText("" + game.getCurrentPlayer().getNoOfTradedArmies());
			ArrayList<CardEnum> typeOfCards = game.getCurrentPlayer().getCards();
			String cards[] = new String[typeOfCards.size()];
			for (int i = 0; i < typeOfCards.size(); i++) {
				cards[i] = typeOfCards.get(i).toString();
			}
			cardExchangeLabel.remove(palyerOwnedCard);
			palyerOwnedCard = null;
			palyerOwnedCard = new JList<>(cards);
			palyerOwnedCard.setBorder(new TitledBorder("Cards Owned"));
			palyerOwnedCard.setBounds(310, 45, 250, 70);
			cardExchangeLabel.add(palyerOwnedCard);	
			cardFrame.revalidate();
			cardFrame.repaint();

		
		}

	}

	/**
	 * Update method called by the observable object to perform card exchange.
	 * @param obj, Observable 
	 * @param arg, Object
	 */
	@Override
	public void update(Observable obj, Object arg) {
		Game game = ((Game) obj);
		updateCardView(game);
	}

}
