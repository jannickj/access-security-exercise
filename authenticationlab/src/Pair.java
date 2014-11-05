import java.io.Serializable;


public class Pair<T1, T2>  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9053605616688989012L;
	
	public Pair(T1 val1, T2 val2) {
		super();
		this.val1 = val1;
		this.val2 = val2;
	}

	private T1 val1;
	private T2 val2;
	public T1 getVal1() {
		return val1;
	}

	public T2 getVal2() {
		return val2;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pair<?, ?>)
		{
			Pair<?,?> oPair = (Pair<?, ?>) obj;
			return this.val1.equals(oPair.val1) && this.val2.equals(oPair.val2);
		}
		else return false;
	}

}
