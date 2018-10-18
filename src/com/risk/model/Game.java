package com.risk.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Random;
import java.util.stream.Collectors;

import java.util.List;
import com.risk.helper.EnumColor;
import com.risk.helper.IOHelper;
import com.risk.helper.InitialPlayerSetup;
import com.risk.helper.PhaseEnum;

/**
 * Game Class
 * 
 * @author Binay
 * @version 1.0.0
 * @since 30-September-2018
 */
public class Game extends Observable {

	// Country and the army assigned to it
	private HashMap<Player, ArrayList<Country>> playerCountry = new HashMap<>();
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private int currentPlayerId;
	private PhaseEnum gamePhase;
	private Map map;
	private final int MINIMUM_REINFORCEMENT_PlAYERS = 3;

	/**
	 * This is a constructor of Game class which will initialize the Map
	 * 
	 * @param map
	 */
	public Game(Map map) {
		super();
		this.map = map;
		this.setGamePhase(gamePhase.Startup);
	}

	public int getCurrentPlayerId() {
		return currentPlayerId;
	}

	private void setNextPlayerTurn() {
		if (currentPlayerId == playerList.size() - 1)
			currentPlayerId = 0;
		else
			currentPlayerId++;
		System.out.println("current player ID:" + currentPlayerId);
	}

	public Player getCurrentPlayer() {

		Player currentPlayer = playerList.get(currentPlayerId);
		return currentPlayer;
	}

	/**
	 * This function will randomly assign Countries to all players and assign one
	 * army to each country for a player
	 * 
	 */
	public void startUpPhase() {

		// Assign countries based on number of players to all players
		for (int i = 0; i < playerList.size(); i++) {
			playerList.get(i).setNoOfUnassignedArmies(InitialPlayerSetup.getInitialArmyCount(playerList.size()));
		}

		int countriesCount = map.getCountryList().size();
		int playerIndex = 0, playerCount = playerList.size();
		ArrayList<Integer> tempList = new ArrayList<>();
		// Here creating the list with indexes
		for (int i = 0; i < countriesCount; i++) {
			tempList.add(i);
		}

		// Shuffling the list for randomness
		Collections.shuffle(tempList, new Random());

		// assigning the shuffled countries from tempList to the players one by
		// one
		for (int i = 0; i < countriesCount; i++) {
			if (playerIndex == playerCount)
				playerIndex = 0;

			Country newCountry = map.getCountryList().get(tempList.get(i));

			assignCountryToPlayer(playerList.get(playerIndex), newCountry);
			increasePlayerArmyInCountry(playerList.get(playerIndex), newCountry);
			playerIndex++;
		}
		notifyObserverslocal(this);
	}

	/**
	 * Assigns the newCountry to selected player
	 * 
	 * @param player
	 * @param newCountry
	 */
	public void assignCountryToPlayer(Player player, Country newCountry) {
		if (playerCountry.containsKey(player)) {
			playerCountry.get(player).add(newCountry);
		} else {
			ArrayList<Country> assignedCountries = new ArrayList<>();
			assignedCountries.add(newCountry);
			playerCountry.put(player, assignedCountries);
		}
		newCountry.setCountryColor(player.getColor());
		newCountry.setPlayerId(player.getPlayerId());
	}

	/**
	 * Increase one army for selected selected player in selected country
	 * 
	 * @param player
	 * @param country
	 */
	private void increasePlayerArmyInCountry(Player player, Country country) {
		player.decreaseUnassignedArmyCount();
		country.increaseArmyCount();
	}

	private void decreasePlayerArmyInCountry(Player player, Country country) {
		player.increaseUnassignedArmyCount();
		country.decreaseArmyCount();
	}

	public void addArmyToCountry(int countryId) {
		if (this.gamePhase == PhaseEnum.Attack || this.gamePhase == PhaseEnum.Fortification) {
			IOHelper.print("Cannot assign army from player to country. Not valid phase");
			return;
		}

		// Player player = playerList.stream().filter(p -> currentPlayerId ==
		// p.getPlayerId()).findAny().orElse(null);
		Player player = this.getCurrentPlayer();

		if (player == null) {
			IOHelper.print("Player id " + currentPlayerId + " does not exist");
			setNextPlayerTurn();
			return;
		}

		if (player.getNoOfUnassignedArmies() == 0) {
			IOHelper.print("Player " + player.getName() + " doesn't have unassigned armies");
			setNextPlayerTurn();
			return;
		}

		Country country = playerCountry.get(player).stream().filter(c -> c.getCountryId() == countryId).findAny()
				.orElse(null);
		if (country == null) {
			IOHelper.print("Country id " + countryId + " does not exist");
			return;
		}

		increasePlayerArmyInCountry(player, country);
		setNextPlayerTurn();
		if (isStartUpPhaseOver())
			setGamePhase(gamePhase.Reinforcement);

		notifyObserverslocal(this);
		// return true;
	}

	private Boolean isStartUpPhaseOver() {
		long unassignedArmiesCount = playerList.stream().filter(p -> p.getNoOfUnassignedArmies() > 0).count();

		if (unassignedArmiesCount == 0)
			return true;
		else
			return false;
	}

	public void reinforcementPhaseSetup() {

		// count number of countries owned by player
		Player player = playerList.stream().filter(p -> currentPlayerId == p.getPlayerId()).findAny().orElse(null);

		// get reinforcement country count based on countries owned by player
		int countriesCount = (int) Math.floor(playerCountry.get(player).stream().count() / 3);

		// Check if player owns any of the continent
		if (playerCountry.containsKey(player)) {
			ArrayList<Country> assignedCountries = playerCountry.get(player);
			List<Integer> assignedCountryIds = assignedCountries.stream().map(c -> c.getCountryId())
					.collect(Collectors.toList());

			ArrayList<Continent> continents = map.getContinentList();
			for (Continent continent : continents) {
				List<Integer> continentCountryIds = continent.getCountryList().stream().map(c -> c.getCountryId())
						.collect(Collectors.toList());
				boolean hasPlayerAllCountries = assignedCountryIds.containsAll(continentCountryIds);
				if (hasPlayerAllCountries)
					countriesCount += continent.getControlValue();
			}
		}

		countriesCount = countriesCount < MINIMUM_REINFORCEMENT_PlAYERS ? MINIMUM_REINFORCEMENT_PlAYERS
				: countriesCount;
		System.out.println("countriesCount:" + countriesCount);
		player.setNoOfUnassignedArmies(countriesCount);
		notifyObserverslocal(this);
	}

	public void reinforcementPhase(String countryName) {
		// Player player = playerList.stream().filter(p -> currentPlayerId ==
		// p.getPlayerId()).findAny().orElse(null);
		Player player = getCurrentPlayer();
		Country country = playerCountry.get(player).stream().filter(c -> c.getCountryName().equals(countryName))
				.findAny().orElse(null);

		if (country == null)
			System.out.println(
					"reinforcementPhase country is null:countryName : player: " + countryName + " " + player.getName());
		else
			increasePlayerArmyInCountry(player, country);
		setGamePhase(gamePhase.Fortification); // Has to be changed to "attack" in the next build
		notifyObserverslocal(this);

	}

	public void attackPhase() {
		setGamePhase(gamePhase.Fortification);
	}

	public void fortificationPhase(String sourceCountryName, String destinationCountryName, int noOfArmies) {

		Player player = getCurrentPlayer();
		Country sourceCountry = playerCountry.get(player).stream()
				.filter(c -> c.getCountryName().equals(sourceCountryName)).findAny().orElse(null);
		Country destinationCountry = playerCountry.get(player).stream()
				.filter(c -> c.getCountryName().equals(destinationCountryName)).findAny().orElse(null);

		decreasePlayerArmyInCountry(player, sourceCountry);
		increasePlayerArmyInCountry(player, destinationCountry);
		this.setNextPlayerTurn();
		setGamePhase(gamePhase.Reinforcement);
		notifyObserverslocal(this);

	}

	/**
	 * This function will add the player to the game(playerList)
	 * 
	 * @param player
	 */
	public void addPlayer(Player player) {
		this.playerList.add(player.getPlayerId(), player);
	}

	public ArrayList<Country> getPlayerCountries() {
		Player currentPlayer = playerList.get(currentPlayerId);
		return playerCountry.get(currentPlayer);
	}

	public ArrayList<Country> getNeighbouringCountriesForFortification(int countryId) {
		Country country = map.getCountryList().stream().filter(c -> c.getCountryId() == countryId).findAny()
				.orElse(null);
		Player currentPlayer = playerList.get(currentPlayerId);

		if (country == null || currentPlayer == null) {
			IOHelper.print("Country id or player id is not valid");
			return null;
		}

		ArrayList<Country> neighbhbouringCountries = new ArrayList<>();

		for (Country pCounty : playerCountry.get(currentPlayer)) {
			Country matchedCountry = country.getNeighbours().stream().filter(c -> c.equals(pCounty)).findAny()
					.orElse(null);
			if (matchedCountry != null) {
				neighbhbouringCountries.add(matchedCountry);
			}
		}
		return neighbhbouringCountries;
	}

	public boolean fortifyCountry(int fromCountryId, int toCountryId, int fortifyArmiesCount) {
		Player currentPlayer = playerList.get(currentPlayerId);

		Country fromCountry = playerCountry.get(currentPlayer).stream().filter(c -> c.getCountryId() == fromCountryId)
				.findAny().orElse(null);
		if (fromCountry == null) {
			IOHelper.print("From country doesn't belong to current player");
			return false;
		}

		Country toCountry = playerCountry.get(currentPlayer).stream().filter(c -> c.getCountryId() == toCountryId)
				.findAny().orElse(null);
		if (toCountry == null) {
			IOHelper.print("From country doesn't belong to current player");
			return false;
		}

		System.out.println(
				"Fortify country " + toCountry.getCountryName() + " from country " + fromCountry.getCountryName()
						+ " with " + fortifyArmiesCount + " armies for player " + getCurrentPlayer().getName());
		// fromCountry.decreaseArmyCount(fortifyArmiesCount);
		toCountry.increaseArmyCount(fortifyArmiesCount);

		return true;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public PhaseEnum getGamePhase() {
		return gamePhase;
	}

	private void setGamePhase(PhaseEnum gamePhase) {
		this.gamePhase = gamePhase;
	}

	private void notifyObserverslocal(Game game) {
		setChanged();
		notifyObservers(this);
	}

	public ArrayList<Player> getAllPlayers() {
		return playerList;
	}
}
