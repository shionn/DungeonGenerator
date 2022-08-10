package generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import pojo.Dungeon;
import pojo.DungeonElt;
import pojo.Zone;

public class DungeonGenerator2 {
	private static final float KEY_RATE = .2f;
	private static final float SAFE_RATE = .1f;
	private static final float SPLIT_BIG_ROOM_RATIO = .5f;
	private static final float DOOR_RATIO = .5f;

	public static void main(String[] args) {
		new DungeonGenerator2().generate(new Random(0)).print();
	}

	private Queue<Character> keyToGenerate = new LinkedBlockingQueue<>();
	private List<Character> keyWithoutDoor = new ArrayList<>();

	public Dungeon generate(Random seed) {
		for (char ch : "abcdefghij".toCharArray()) {
			keyToGenerate.add(ch);
		}

		Dungeon dungeon = new Dungeon(32, 16);
		Zone zone = new Zone(0, dungeon.getWidth() - 1, 0, dungeon.getHeight() - 1);
		fill(dungeon, zone, DungeonElt.WALL);
		generate(dungeon, zone, seed);
		return dungeon;
	}

	private void generate(Dungeon dungeon, Zone zone, Random seed) {
		if (shouldSplit(zone, seed)) {
			if (zone.w() > zone.h()) {
				splitVertical(dungeon, zone, seed);
			} else {
				splitHorizontal(dungeon, zone, seed);
			}
		} else {
			createRoom(dungeon, zone, seed);
		}
	}

	/**
	 * Pour découper une zone en deux, on sélectionne une colonne aléatoirement
	 * respectant la possibilité de créer deux pièces. Avec les deux zones crées on
	 * part en récursion.
	 */
	private void splitVertical(Dungeon dungeon, Zone zone, Random seed) {
		int v = nextInt(seed, zone.sx() + 2, zone.ex() - 1);
		for (Zone child : zone.splitVertical(v)) {
			generate(dungeon, child, seed);
			zone.addKeys(child.getKeys());
		}
		addVerticalDoor(dungeon, zone, seed, v);
	}

	/**
	 * Pour ajouter une porte il faut prende aléatoirement une coordonnée sur le mur
	 * jusqu'à en avoir une ne donnant sur aucun mur des deux cotés
	 */
	private void addVerticalDoor(Dungeon dungeon, Zone zone, Random seed, int v) {
		int y = 0;
		do {
			y = nextInt(seed, zone.sy(), zone.ey());
		} while (dungeon.get(v + 1, y).isSolid() || dungeon.get(v - 1, y).isSolid());
		if (seed.nextFloat() < DOOR_RATIO) {
			char key = getPossibleLock(zone, seed);
			if (key != '\0' && seed.nextFloat() < .4f) {
				System.out.println("Add door " + key + " to : " + zone);
				dungeon.set(v, y, Character.toUpperCase(key));
				keyWithoutDoor.remove((Character) key);
			}
			dungeon.set(v, y, DungeonElt.DOORV);
		} else {
			dungeon.set(v, y, DungeonElt.CORRIDOR);
		}
	}


	private void splitHorizontal(Dungeon dungeon, Zone zone, Random seed) {
		int h = nextInt(seed, zone.sy() + 2, zone.ey() - 1);
		for (Zone child : zone.splitHorizontal(h)) {
			generate(dungeon, child, seed);
			zone.addKeys(child.getKeys());
		}
		addHorizontalDoor(dungeon, zone, seed, h);
	}

	private void addHorizontalDoor(Dungeon dungeon, Zone zone, Random seed, int h) {
		int x = 0;
		do {
			x = nextInt(seed, zone.sx(), zone.ex());
		} while (dungeon.get(x, h + 1).isSolid() || dungeon.get(x, h - 1).isSolid());
		if (seed.nextFloat() < DOOR_RATIO) {
			dungeon.set(x, h, DungeonElt.DOORH);
		} else {
			dungeon.set(x, h, DungeonElt.CORRIDOR);
		}
	}

	/**
	 * clef/porte
	 */
	private char getPossibleLock(Zone zone, Random seed) {
		List<Character> temp = new ArrayList<>(keyWithoutDoor);
		temp.removeAll(zone.getKeys());
		if (temp.isEmpty()) {
			return '\0';
		}
		return temp.get(nextInt(seed, 0, temp.size()));
	}

	private boolean shouldSplit(Zone zone, Random seed) {
		int s = Math.max(zone.w(), zone.h());
		if (s >= 7)
			return true;
		if (s <= 4)
			return false;
		return seed.nextFloat() < SPLIT_BIG_ROOM_RATIO;
	}

	private void createRoom(Dungeon dungeon, Zone zone, Random seed) {
		Zone room = zone.reduce(1, 1);
		if (Math.min(room.h(), room.w()) == 1) {
			fill(dungeon, room, DungeonElt.CORRIDOR);
		} else {
			fill(dungeon, room, DungeonElt.EMPTY);
		}

//        if (seed.nextFloat() < .8f) {
//            dungeon.set(rndRoomX(room, seed), rndRoomY(room, seed), DungeonElt.MONSTER);
//        }
		if (seed.nextFloat() < SAFE_RATE) {
			dungeon.set(rndRoomX(room, seed), rndRoomY(room, seed), DungeonElt.SAFE);
		}
		if (seed.nextFloat() < KEY_RATE && !keyToGenerate.isEmpty()) {
			int x = rndRoomX(room, seed);
			int y = rndRoomY(room, seed);
			char key = keyToGenerate.poll();
			dungeon.set(x, y, DungeonElt.KEY);
			dungeon.set(x, y, key);
			zone.addKey(key);
			keyWithoutDoor.add(key);
			System.out.println("add key " + key + " to " + zone);
		}
	}

	private void fill(Dungeon dungeon, Zone zone, DungeonElt elt) {
		for (int y = zone.sy(); y <= zone.ey(); y++) {
			for (int x = zone.sx(); x <= zone.ex(); x++) {
				dungeon.set(x, y, elt);
			}
		}
	}

	/**
	 * Renvoie une valeur aléatoire entre min et max
	 */
	private int nextInt(Random seed, int min, int max) {
		return seed.nextInt(max - min) + min;
	}

	private int rndRoomX(Zone room, Random seed) {
		return nextInt(seed, room.sx(), room.ex() + 1);
	}

	private int rndRoomY(Zone room, Random seed) {
		return nextInt(seed, room.sy(), room.ey() + 1);
	}

}
