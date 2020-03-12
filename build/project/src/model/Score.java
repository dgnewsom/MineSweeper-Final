package model;

import java.util.ArrayList;
import java.util.Comparator;

import application.MineSweeper;

/**
 * Score Class to represent a score item
 * 
 * @author Daniel Newsom
 * @version 3.0
 */
public class Score implements Comparable<Score>, Comparator<Score>{

	private String name;
	private int time;

	/**
	 * Constructor for the Score Class
	 * @param name String representing the scores name
	 * @param time int representing the score
	 */
	public Score(String name, int time) {
		this.name = name;
		this.time = time;
	}
	
	/**
	 * Override the compareTo method to compare by time
	 * then by name.
	 */
	@Override
	public int compareTo(Score other) {

		if(this.time == other.getTime()) {
			return this.name.compareTo(other.getName());
		}
		else {
			return this.time - other.getTime();		
		}
	}
	
	/**
	 * Override the compare method for sorting
	 */
	@Override
	public int compare(Score o1, Score o2) {
		return o1.compareTo(o2);
	}

	/**
	 * method to sort list of scores
	 * @param list list to sort
	 */
	public void sortList(ArrayList<Score> list) {
		list.sort(this);
	}
	
	/*
	 * Getters and setters
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * Override toString method
	 * @return String string representation of the object
	 */
	public String toString() {
				
		return String.format("%s %s", name, MineSweeper.getTimeAsString(time));
	}
}
