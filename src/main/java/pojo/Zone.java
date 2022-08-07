package pojo;

/**
 * Repésente une zone en cours de génération du dongeon
 * 
 * Code sous licence GPLv3 (http://www.gnu.org/licenses/gpl.html)
 *
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 *         GCS d- s+:+ a+ C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+
 *         G- e+++ h+ r- y-
 */
public class Zone {
	private int sx, ex, sy, ey;

	public Zone(int startx, int endx, int starty, int endy) {
		this.sx = startx;
		this.ex = endx;
		this.sy = starty;
		this.ey = endy;
	}

	public int h() {
		return ey - sy + 1;
	}

	public int w() {
		return ex - sx + 1;
	}

	public Zone[] splitHorizontal(int h) {
		return new Zone[] { new Zone(sx, ex, sy, h), new Zone(sx, ex, h, ey) };
	}

	public Zone[] splitVertical(int v) {
		return new Zone[] { new Zone(sx, v, sy, ey), new Zone(v, ex, sy, ey) };
	}

	public Zone reduce(int x, int y) {
		return new Zone(sx + x, ex - x, sy + y, ey - y);
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

	@Override
	public String toString() {
		return "[" + sx + "," + ex + "," + sy + "," + ey + "],(" + w() + "," + h() + ")";
	}

}