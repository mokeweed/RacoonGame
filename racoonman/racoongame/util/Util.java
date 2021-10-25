package racoonman.racoongame.util;

import java.util.function.Consumer;

public class Util {
	
	public static float[] unboxFloats(Float[] packaged) {
		return Util.make(new float[packaged.length], (floats) -> {
	    	for(int i = 0; i < packaged.length; i++) {
	    		floats[i] = packaged[i];
	    	}
	    });
	}
	
	public static <T> T make(T type, Consumer<T> action) {
		action.accept(type);
		return type;
	}
}
