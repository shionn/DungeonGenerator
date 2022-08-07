
package generator;

import java.util.Random;

import pojo.Dungeon;
import pojo.DungeonElt;
import pojo.Zone;

/**
 * genere un dongon basé sur l'algo de split recursif
 * 
 * Code sous licence GPLv3 (http://www.gnu.org/licenses/gpl.html)
 *
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 *         GCS d- s+:+ a+ C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+ G- e+++ h+ r- y-
 */
public class DungeonGenerator {

    private static final int MIN_ROOM_SIZE = 3;
    private static final int MIN = 12;
    private static final int MAX = 64;

    public static void main(String[] args) {
        Dungeon dungeon = new DungeonGenerator().generate(new Random(1958));
        dungeon.print();
    }

    public Dungeon generate(Random seed) {
        Dungeon dungeon = new Dungeon(nextInt(seed, MIN, MAX), nextInt(seed, MIN, MAX));
        Zone zone = new Zone(0, dungeon.getWidth() - 1, 0, dungeon.getHeight() - 1);
        fill(dungeon, zone, DungeonElt.WALL);
        generate(dungeon, zone, seed);
        return dungeon;
    }

    /**
     * Si il est possible de couper la zone en deux, on le fait sinon on en crée une pièce
     */
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
     * Pour créer une pièce il suffit d'enlever les murs à l’intérieur
     */
    private void createRoom(Dungeon dungeon, Zone zone, Random seed) {
        Zone room = zone.reduce(1, 1);
        fill(dungeon, room, DungeonElt.EMPTY);
        if (seed.nextFloat() < .8f) {
            dungeon.set(rndRoomX(room, seed), rndRoomY(room, seed), DungeonElt.MONSTER);
        }
        if (seed.nextFloat() < .1f) {
            dungeon.set(rndRoomX(room, seed), rndRoomY(room, seed), DungeonElt.SAFE);
        }
    }

    private int rndRoomX(Zone room, Random seed) {
        return nextInt(seed, room.sx(), room.ex() + 1);
    }

    private int rndRoomY(Zone room, Random seed) {
        return nextInt(seed, room.sy(), room.ey() + 1);
    }

    /**
     * Pour découper une zone en deux, on sélectionne une colonne aléatoirement respectant la
     * possibilité de créer deux pièces. Avec les deux zones crées on part en récursion.
     */
    private void splitVertical(Dungeon dungeon, Zone zone, Random seed) {
        int v = nextInt(seed, zone.sx() + MIN_ROOM_SIZE + 1, zone.ex() - MIN_ROOM_SIZE);
        for (Zone child : zone.splitVertical(v)) {
            generate(dungeon, child, seed);
        }
        addVerticalDoor(dungeon, zone, seed, v);
    }

    /**
     * Pour ajouter une porte il faut prende aléatoirement une coordonnée sur le mur jusqu'à en
     * avoir une ne donnant sur aucun mur des deux cotés
     */
    private void addVerticalDoor(Dungeon dungeon, Zone zone, Random seed, int v) {
        int y = 0;
        do {
            y = nextInt(seed, zone.sy(), zone.ey());
        } while (dungeon.get(v + 1, y).isSolid() || dungeon.get(v - 1, y).isSolid());
        dungeon.set(v, y, DungeonElt.DOORV);
    }

    private void splitHorizontal(Dungeon dungeon, Zone zone, Random seed) {
        int h = nextInt(seed, zone.sy() + MIN_ROOM_SIZE + 1, zone.ey() - MIN_ROOM_SIZE);
        for (Zone child : zone.splitHorizontal(h)) {
            generate(dungeon, child, seed);
        }
        addHorizontalDoor(dungeon, zone, seed, h);
    }

    private void addHorizontalDoor(Dungeon dungeon, Zone zone, Random seed, int h) {
        int x = 0;
        do {
            x = nextInt(seed, zone.sx(), zone.ex());
        } while (dungeon.get(x, h + 1).isSolid() || dungeon.get(x, h - 1).isSolid());
        dungeon.set(x, h, DungeonElt.DOORH);
    }

    /**
     * La zone peu être coupé en deux si elle peu contenir deux pièces de taille minimales en
     * vertical ou en horizontal
     */
    private boolean shouldSplit(Zone zone, Random seed) {
        return zone.w() > MIN_ROOM_SIZE * 2 + 1 || zone.h() > MIN_ROOM_SIZE * 2 + 1;
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

}
