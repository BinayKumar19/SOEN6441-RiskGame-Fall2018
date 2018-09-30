package com.risk.model;

import com.risk.helper.Color;

/**
 * Player Class
 * 
 * @author sadgi
 * @version 1.0.0
 * @since 27-September-2018
 */
public class Player {
	private int playerId;
	private String name;
	private Color color;
	private int NoOfArmies;

	/**
	 * This is a constructor of Player Class which sets playerId, name, and color.
	 * 
	 * @param playerId
	 * @param name
	 * @param color
	 */
	public Player(int playerId, String name, Color color) {
		super();
		this.playerId = playerId;
		this.name = name;
		this.color = color;
	}

	/**
	 * getters and setters of NoOfArmies
	 */
	public int getNoOfArmies() {
		return NoOfArmies;
	}

	public void setNoOfArmies(int noOfArmies) {
		NoOfArmies = noOfArmies;
	}

	/**
	 * getters and setters
	 */
	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
