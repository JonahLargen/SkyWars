package me.jonah.SG;

public class Leaderboard {
	
	String s;
	int i;
	
	public Leaderboard(String s, int i){
		this.s = s;
		this.i = i;
	}
	
	public int getWins(){
		return this.i;
	}
	public String getName(){
		return this.s;
	}

}
