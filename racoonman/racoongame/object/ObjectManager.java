package racoonman.racoongame.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import racoonman.racoongame.core.tick.TickingObject;
import racoonman.racoongame.level.LevelTickContext;

public class ObjectManager implements TickingObject<LevelTickContext> {
	private Map<UUID, GameObject<?>> id2object;
	private Map<Class<GameObject<?>>, List<GameObject<?>>> objPerType;
	
	public ObjectManager() {
		this.id2object = Collections.synchronizedMap(new HashMap<>());
		this.objPerType = Collections.synchronizedMap(new HashMap<>());
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public UUID addObject(GameObject<?> obj) {
		UUID id = UUID.randomUUID();
		this.id2object.put(id, obj);
	
		Class<? extends GameObject> objClass = obj.getClass();
		if(this.objPerType.get(objClass) == null)
			this.objPerType.put((Class<GameObject<?>>) objClass, new ArrayList<>());
		this.objPerType.get(objClass).add(obj);

		return id;
	}

	@Override
	public void tick(LevelTickContext tickContext) {
		this.id2object.values().forEach(GameObject::tick);
	}
	
	public Map<Class<GameObject<?>>, List<GameObject<?>>> getObjectsPerType() {
		return this.objPerType;
	}
	
	public Collection<GameObject<?>> getAllObjects() {
		return this.id2object.values();
	}
}
