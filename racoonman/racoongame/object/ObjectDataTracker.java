package racoonman.racoongame.object;

import java.util.HashMap;
import java.util.Map;

public class ObjectDataTracker {
	private Map<String, DataHolder<?>> holders;
	
	public ObjectDataTracker() {
		this.holders = new HashMap<>();
	}

	public <V> V getDataValue(String name, Class<V> type) {
		DataHolder<?> holder = this.holders.get(name);
		if(holder == null)
			throw new IllegalArgumentException("Unregistered data holder [" + name + "]");
		return holder.getValue(type);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> void setDataValue(String name, T value) {
		DataHolder holder = this.holders.get(value);
		
		if(holder == null) {
			this.holders.put(name, new DataHolder<T>(value, (Class<T>) value.getClass()));
			return;
		}
		holder.setValue(value);
	}
	
	public static class DataHolder<T> {
		private T value;
		private Class<T> type;
		
		public DataHolder(T value, Class<T> type) {
			this.value = value;
			this.type = type;
		}
		
		@SuppressWarnings("unchecked")
		protected <V> V getValue(Class<V> type) {
			if(this.type == type) {
				return (V)this.value;
			} else throw new RuntimeException("Got modifier as incorrect type, requested type [" + type.getSimpleName() + "], should be [" + this.type.getSimpleName() + "]");
		}
		
		protected void setValue(T value) {
			this.value = value;
		}
	}
}
