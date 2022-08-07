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
	WALL('#', true),
	EMPTY('.', false),
	DOORV('|', true),
	DOORH('-', true),
	CORRIDOR(' ', false),
	MONSTER('m', false),
	SAFE('¤', false);

	private char graphic;
	private boolean solid;

	private DungeonElt(char graphic, boolean solid) {
		this.graphic = graphic;
		this.solid = solid;
	}

	public char getGraphic() {
		return graphic;
	}

	public boolean isSolid() {
		return solid;
	}
}
