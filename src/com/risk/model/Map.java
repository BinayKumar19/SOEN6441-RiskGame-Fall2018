package com.risk.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.risk.helper.IOHelper;

/**
 * Map Class
 * 
 * @author sadgi
 * @version 1.0.0
 * @since 27-September-2018
 */
public class Map {
	private String mapName;
	private String mapPath = "assets/maps/";
	private ArrayList<Continent> continentsList = new ArrayList<>();

	/**
	 * This is a constructor of Map Class which sets mapId and mapName.
	 * 
	 * @param mapName
	 */
	public Map() {
		super();
	}

	/**
	 * getters and setters
	 */
	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	private HashMap<String, Integer> controlValuesByContinents = new HashMap<String, Integer>();
	private HashMap<String, ArrayList<String>> territories = new HashMap<String, ArrayList<String>>();

	public void readMap() {

		try {
			boolean captureContinents = false;
			boolean captureCountries = false;
			File file = new File(mapPath + mapName);
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String readLine;
			int continentID = 0;
			int countryID = 0;

			while ((readLine = bufferedReader.readLine()) != null) {
				if (readLine.length() == 0)
					continue;
				else if ((readLine.trim()).equals("[Continents]")) {
					captureContinents = true;
					continue;
				} else if ((readLine.trim()).equals("[Territories]")) {
					captureContinents = false;
					captureCountries = true;
					continue;
				}

				if (captureContinents) {
					String[] parsedControlValuesByContinentsArray = readLine.split("=");
					Continent continent = new Continent(continentID++, parsedControlValuesByContinentsArray[0],
							Integer.parseInt(parsedControlValuesByContinentsArray[1]));
					continentsList.add(continent);
				} else if (captureCountries) {
					String[] parsedTerritoriesArray = readLine.split(",");
					String continentName = parsedTerritoriesArray[3];
					Country country = new Country(countryID++, parsedTerritoriesArray[0]);
					country.setxCoordiate(Integer.parseInt(parsedTerritoriesArray[1]));
					country.setyCoordiate(Integer.parseInt(parsedTerritoriesArray[2]));

					for (int i = 0; i < continentsList.size(); i++) {
						if (continentsList.get(i).getContName().equals(continentName)) {
							continentsList.get(i).addCountry(country);
							country.setContId(continentsList.get(i).getContId());
							break;
						}
					}
				}
			}
			bufferedReader.close();
		} catch (Exception e) {
			IOHelper.printException(e);
		}

	}

	public void addControlValues(String country, int controlValue) {
		controlValuesByContinents.put(country, controlValue);
	}

	public void addContinent(Continent continent) {
		continentsList.add(continent);
	}

	public void addTeritorries(String teritorry, ArrayList<String> list) {
		territories.put(teritorry, list);
	}

	public Boolean isMapValid() {
		IOHelper.print("Validate map");

		return true;
	}

	public void saveMap() {
		StringBuffer content = new StringBuffer();
		content.append("[Continents]\r\n");
		for (Continent induvidualContinentObject : this.continentsList) {
			content.append(
					induvidualContinentObject.getContName() + "=" + induvidualContinentObject.getControlValue() + "\r\n");
		}
		content.append("\n[Territories]\r\n");
		for (Continent induvidualContinentObject : this.continentsList) {
			for (Country induvidualCountry : induvidualContinentObject.getCountryList()) {
				content.append(induvidualCountry.getCountryName() + "," + induvidualCountry.getxCoordiate() + ","
						+ induvidualCountry.getyCoordiate() + "," + induvidualContinentObject.getContName());
				for (String neighbouringCountry : induvidualCountry.getNeighboursString()) {
					content.append("," + neighbouringCountry);
				}
				content.append("\r\n");
			}
		}

		System.out.print(content);
		final Path path = Paths.get(this.mapPath + this.mapName+ ".map");
		BufferedWriter writer = null;
		try {
			writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
			writer.write(new String(content));
			writer.close();
			System.out.println("\nFile Saved");
		} catch (Exception e) {
			IOHelper.printException(e);
		}   
	}
}
