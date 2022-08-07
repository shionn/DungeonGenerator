package pojo;

/**
 * Pojo d'un donjon
 * 
 * Code sous licence GPLv3 (http://www.gnu.org/licenses/gpl.html)
 *
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 *         GCS d- s+:+ a+ C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+ G- e+++ h+ r- y-
 */
public class Dungeon {

    private DungeonElt[][] maps;

    public Dungeon(int w, int h) {
        this.maps = new DungeonElt[w][h];
    }

    public int getHeight() {
        return maps[0].length;
    }

    public int getWidth() {
        return maps.length;
    }

    public void set(int x, int y, DungeonElt elt) {
        maps[x][y] = elt;
    }

    public DungeonElt get(int x, int y) {
        return maps[x][y];
    }

    public void print() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                System.out.print(maps[x][y].getGraphic());
            }
            System.out.println();
        }
    }

}
