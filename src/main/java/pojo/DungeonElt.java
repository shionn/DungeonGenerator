package pojo;

/**
 * Elements composant {@link Dungeon}
 * 
 * Code sous licence GPLv3 (http://www.gnu.org/licenses/gpl.html)
 *
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 *         GCS d- s+:+ a+ C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+
 *         G- e+++ h+ r- y-
 */
public enum DungeonElt {
	WALL('#', true, false),
	EMPTY(' ', false, false),
	DOORV('|', true, true),
	DOORH('-', true, true),
	CORRIDOR(' ', false, false),
	MONSTER('m', false, false),
	SAFE('¤', false, false),
	KEY('k', false, true);

	private char graphic;
	private boolean solid;
	private boolean displayData;

	private DungeonElt(char graphic, boolean solid, boolean displayData) {
		this.graphic = graphic;
		this.solid = solid;
		this.displayData = displayData;
	}

	public char getGraphic() {
		return graphic;
	}

	public boolean isSolid() {
		return solid;
	}

	boolean isDisplayData() {
		return displayData;
	}
}
