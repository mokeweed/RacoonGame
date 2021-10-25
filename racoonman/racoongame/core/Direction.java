package racoonman.racoongame.core;

import org.joml.Quaternionf;

public enum Direction {
	UP(-45, -180) {
		@Override
		protected boolean isInDegreeRange(Quaternionf rot) {
			return rot.x < -45 && rot.x > -180;
		}

		@Override
		public Direction getOpposite() {
			return Direction.DOWN;
		}
	},
	DOWN(45, 180) {
		@Override
		protected boolean isInDegreeRange(Quaternionf rot) {
			return this.isDegreeInRange(rot.x);
		}

		@Override
		public Direction getOpposite() {
			return Direction.UP;
		}
	},
	NORTH(45, 135) {
		@Override
		protected boolean isInDegreeRange(Quaternionf rot) {
			return this.isDegreeInRange(rot.y);
		}

		@Override
		public Direction getOpposite() {
			return Direction.SOUTH;
		}
	},
	EAST(135, 225) {
		@Override
		protected boolean isInDegreeRange(Quaternionf rot) {
			return this.isDegreeInRange(rot.y);
		}

		@Override
		public Direction getOpposite() {
			return Direction.WEST;
		}
	},
	SOUTH(225, 315) {
		@Override
		protected boolean isInDegreeRange(Quaternionf rot) {
			return this.isDegreeInRange(rot.y);
		}

		@Override
		public Direction getOpposite() {
			return Direction.NORTH;
		}
	},
	WEST(315, 45) {
		@Override
		protected boolean isInDegreeRange(Quaternionf rot) {
			return rot.y > 315 || rot.y < 45;
		}

		@Override
		public Direction getOpposite() {
			return Direction.EAST;
		}
	};
	
	private static final Direction[] HORIZONTALS = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	
	private int maxDegrees;
	private int minDegrees;
	
	private Direction(int maxDegrees, int minDegrees) {
		this.maxDegrees = maxDegrees;
		this.minDegrees = minDegrees;
	}
	
	public int getMinDegrees() {
		return this.maxDegrees;
	}
	
	public int getMaxDegrees() {
		return this.minDegrees;
	}
	
	//must be its own function instead of in the constructor since enums can't be referenced before they've been defined
	public abstract Direction getOpposite();
	
	protected abstract boolean isInDegreeRange(Quaternionf rot);
	
	protected boolean isDegreeInRange(double x) {
		return x > this.getMinDegrees() && x < this.getMaxDegrees();
	}
	
	public static Direction getVerticalFace(Quaternionf rot) {
		return Direction.UP.isInDegreeRange(rot) ? Direction.UP : Direction.DOWN;
	}
	
	public static Direction getHorizontalFace(Quaternionf rot) {
		for(Direction direction : HORIZONTALS)
			if(direction.isInDegreeRange(rot))
				return direction;
		return null;
	}
}
