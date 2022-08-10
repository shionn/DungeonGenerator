package tree;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import pojo.Dungeon;
import pojo.DungeonElt;

public class TreeDungeonGenerator {

	private static final float LOCK_RATE = .1f;
	private static final float SPLIT_ROOM_RATE = .6f;

	public static void main(String[] args) {
		Random seed = new Random(0);
		TreeDungeon tree = new TreeDungeonGenerator().generate(32, 16, seed);
		new TreeDungeonGenerator().fill(tree, seed);
		tree.print();
		Dungeon dungeon = new TreeDungeonGenerator().toDungeon(tree);
		dungeon.print();
	}


	private Dungeon toDungeon(TreeDungeon tree) {
		Dungeon dungeon = new Dungeon(tree.w(), tree.h());
		fillToDungeon(dungeon, tree);
		return dungeon;
	}

	private void fillToDungeon(Dungeon dungeon, TreeDungeon tree) {
		if (tree.isRoom()) {
			for (int y = tree.sy() + 1; y < tree.ey(); y++) {
				for (int x = tree.sx() + 1; x < tree.ex(); x++) {
					dungeon.set(x, y, DungeonElt.EMPTY);
				}
			}
		} else {
			fillToDungeon(dungeon, tree.child(0));
			fillToDungeon(dungeon, tree.child(1));
			if (tree.w() > tree.h()) {
				dungeon.set(tree.doorX(), tree.doorY(), DungeonElt.DOORV);
				dungeon.set(tree.doorX(), tree.doorY(), tree.doorLock());
			} else {
				dungeon.set(tree.doorX(), tree.doorY(), DungeonElt.DOORH);
				dungeon.set(tree.doorX(), tree.doorY(), tree.doorLock());
			}
		}
	}

	/**
	 * construction des murs et pieces
	 */
	private TreeDungeon generate(int w, int h, Random seed) {
		TreeDungeon dungeon = new TreeDungeon(0, 0, w - 1, h - 1);
		generate(dungeon, seed);
		return dungeon;
	}

	private void generate(TreeDungeon dungeon, Random seed) {
		if (shouldSplit(dungeon, seed)) {
			if (dungeon.w() > dungeon.h()) {
				splitVertical(dungeon, seed);
			} else {
				splitHorizontal(dungeon, seed);
			}
		}
	}

	private void splitVertical(TreeDungeon dungeon, Random seed) {
		int v = nextInt(seed, dungeon.sx() + 2, dungeon.ex() - 1);
		TreeDungeon left = new TreeDungeon(dungeon.sx(), dungeon.sy(), v, dungeon.ey());
		TreeDungeon right = new TreeDungeon(v, dungeon.sy(), dungeon.ex(), dungeon.ey());
		dungeon.childs(left, right);
		generate(left, seed);
		generate(right, seed);
		int y = 0;
		do {
			y = nextInt(seed, dungeon.sy() + 1, dungeon.ey());
		} while (dungeon.solid(v + 1, y) || dungeon.solid(v - 1, y));
		dungeon.door(v, y);
	}

	private void splitHorizontal(TreeDungeon dungeon, Random seed) {
		int v = nextInt(seed, dungeon.sy() + 2, dungeon.ey() - 1);
		TreeDungeon top = new TreeDungeon(dungeon.sx(), dungeon.sy(), dungeon.ex(), v);
		TreeDungeon down = new TreeDungeon(dungeon.sx(), v, dungeon.ex(), dungeon.ey());
		dungeon.childs(top, down);
		generate(top, seed);
		generate(down, seed);
		int x = 0;
		do {
			x = nextInt(seed, dungeon.sx() + 1, dungeon.ex());
		} while (dungeon.solid(x, v + 1) || dungeon.solid(x, v - 1));
		dungeon.door(x, v);
	}

	private boolean shouldSplit(TreeDungeon dungeon, Random seed) {
		int s = Math.max(dungeon.w(), dungeon.h());
		if (dungeon.h() == 3 || dungeon.w() == 3)
			return false;
		if (s > 7)
			return true;
		if (s <= 4)
			return false;
		return seed.nextFloat() < SPLIT_ROOM_RATE;
	}

	/**
	 * ajout, clef, monstre etc...
	 */
	private void fill(TreeDungeon tree, Random seed) {
		Queue<Character> todoDoors = new LinkedBlockingQueue<>();
		for (char ch : "ABCDEFGHIJ".toCharArray()) {
			todoDoors.add(ch);
		}

		for (TreeDungeon t : tree) {
			if (!t.isRoom() && seed.nextFloat() < LOCK_RATE) {
				t.door(todoDoors.poll());
			}
		}
	}

	private int nextInt(Random seed, int min, int max) {
		return seed.nextInt(max - min) + min;
	}

}
