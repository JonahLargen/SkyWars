package me.jonah.SG;

import java.util.ArrayList;

public class T {
	
	String name;
	int wins;
	
	public T(String name, int wins){
		this.name = name;
		this.wins = wins;
	}
	
	public static ArrayList<T> getHighest(ArrayList<T> array){
		ArrayList<T> r = new ArrayList<T>();
		
		int most1 = -1;
		T t1 = new T("069a79f4-44e9-4726-a5be-fca90e38aaf5",-1);
		for (T t: array){
			if (t.wins > most1){
				most1 = t.wins;
				t1 = t;
			}
		}
		array.remove(t1);
		r.add(t1);
		
		int most2 = -1;
		T t2 = new T("069a79f4-44e9-4726-a5be-fca90e38aaf5",-1);
		for (T t: array){
			if (t.wins > most2){
				most2 = t.wins;
				t2 = t;
			}
		}
		array.remove(t2);
		r.add(t2);
		
		int most3 = -1;
		T t3 = new T("069a79f4-44e9-4726-a5be-fca90e38aaf5",-1);
		for (T t: array){
			if (t.wins > most3){
				most3 = t.wins;
				t3 = t;
			}
		}
		array.remove(t3);
		r.add(t3);
		
		int most4 = -1;
		T t4 = new T("069a79f4-44e9-4726-a5be-fca90e38aaf5",-1);
		for (T t: array){
			if (t.wins > most4){
				most4 = t.wins;
				t4 = t;
			}
		}
		array.remove(t4);
		r.add(t4);
		
		int most5 = -1;
		T t5 = new T("069a79f4-44e9-4726-a5be-fca90e38aaf5",-1);
		for (T t: array){
			if (t.wins > most5){
				most5 = t.wins;
				t5 = t;
			}
		}
		array.remove(t5);
		r.add(t5);
		
		int most6 = -1;
		T t6 = new T("069a79f4-44e9-4726-a5be-fca90e38aaf5",-1);
		for (T t: array){
			if (t.wins > most6){
				most6 = t.wins;
				t6 = t;
			}
		}
		array.remove(t6);
		r.add(t6);
		
		int most7 = -1;
		T t7 = new T("069a79f4-44e9-4726-a5be-fca90e38aaf5",-1);
		for (T t: array){
			if (t.wins > most7){
				most7 = t.wins;
				t7 = t;
			}
		}
		array.remove(t7);
		r.add(t7);
		
		int most8 = -1;
		T t8 = new T("069a79f4-44e9-4726-a5be-fca90e38aaf5",-1);
		for (T t: array){
			if (t.wins > most8){
				most8 = t.wins;
				t8 = t;
			}
		}
		array.remove(t8);
		r.add(t8);
		
		int most9 = -1;
		T t9 = new T("069a79f4-44e9-4726-a5be-fca90e38aaf5",-1);
		for (T t: array){
			if (t.wins > most9){
				most9 = t.wins;
				t9 = t;
			}
		}
		array.remove(t9);
		r.add(t9);
		
		int most10 = -1;
		T t10 = new T("069a79f4-44e9-4726-a5be-fca90e38aaf5",-1);
		for (T t: array){
			if (t.wins > most10){
				most10 = t.wins;
				t10 = t;
			}
		}
		array.remove(t10);
		r.add(t10);
		
		return r;
		
		
	}

}
