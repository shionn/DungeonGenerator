package mine.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TreeDungeon implements Iterable<TreeDungeon> {

//	private int w, h;
	private TreeDungeon[] childs;
	private int sx;
	private int sy;
	private int ex;
	private int ey;
	private int doorX;
	private int doorY;
	private char doorLock;

//	public TreeDungeon(int w, int h) {
//		this.w = w;
//		this.h = h;
//	}
//
//	public TreeDungeon(int w, int h, TreeDungeon... childs) {
//		this.w = w;
//		this.h = h;
//		this.childs = childs;
//	}

	public TreeDungeon(int sx, int sy, int ex, int ey) {
		this.sx = sx;
		this.sy = sy;
		this.ex = ex;
		this.ey = ey;
	}

	public boolean isRoom() {
		return childs == null;
	}

	public int sx() {
		return sx;
	}

	public int ex() {
		return ex;
	}

	public int sy() {
		return sy;
	}

	public int ey() {
		return ey;
	}

	public int h() {
		return ey - sy + 1;
	}

	public int w() {
		return ex - sx + 1;
	}

	public TreeDungeon child(int i) {
		return childs[i];
	}

	public void print() {
		print(0);
	}

	private void print(int tab) {
		for (int i = 0; i < tab; i++) {
			System.out.print("  ");
		}
		System.out.println(sx + "," + sy + ">" + ex + "," + ey + " (" + w() + "," + h() + ")");
		if (childs != null) {
			for (TreeDungeon c : childs) {
				c.print(tab + 2);
			}
		}
	}

	public void childs(TreeDungeon... childs) {
		this.childs = childs;
	}

	public boolean solid(int x, int y) {
		if (sx == x || ex == x || sy == y || ey == y)
			return true;
		if (childs == null)
			return false;
		return child(0).solid(x, y) || child(1).solid(x, y);
	}

	public void door(int x, int y) {
		this.doorX = x;
		this.doorY = y;
	}

	public void door(char lock) {
		this.doorLock = lock;
	}

	public int doorX() {
		return doorX;
	}

	public int doorY() {
		return doorY;
	}

	public char doorLock() {
		return doorLock;
	}
	@Override
	public Iterator<TreeDungeon> iterator() {
		return allAsList().iterator();
	}

	private List<TreeDungeon> allAsList() {
		List<TreeDungeon> all = new ArrayList<TreeDungeon>();
		all.add(this);
		if (childs != null) {
			Arrays.stream(childs).forEach(child -> all.addAll(child.allAsList()));
		}
		return all;
	}


}
