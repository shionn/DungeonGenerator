package port.roguebasin.com;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

/**
 * port de
 * https://www.roguebasin.com/index.php?title=A_Simple_Dungeon_Generator_for_Python_2_or_3
 */
@Builder()
@RequiredArgsConstructor
@AllArgsConstructor
public class Generator {

	private final int width, height;
	private final int maxRooms, min_room_xy, max_room_xy;
	private final boolean rooms_overlap;
	private final int random_connections;
	private final int randomSpurs;
	private Tile[][] level;
	private List<Room> room_list = new ArrayList<>();
	private List<Corridor> corridor_list = new ArrayList<>();
	private Random random = new Random();

	public static void main(String[] args) {
		Generator.builder()
				.width(64)
				.height(64)
				.maxRooms(15)
				.min_room_xy(5)
				.max_room_xy(10)
				.rooms_overlap(false)
				.random_connections(1)
				.randomSpurs(3)
				.build()
				.genLevel();
	}

	private void genLevel() {
		this.level = new Tile[height][width];
		IntStream.range(0, height).forEach(i-> IntStream.range(0,width).forEach(j-> level[i][j] = Tile.stone));

		int max_iters = maxRooms * 5;
		IntStream.range(0, max_iters).forEach(a -> {
			if (this.room_list.size() < this.maxRooms) {
				Room tmp_room = this.gen_room();
				if (this.rooms_overlap || this.room_list.isEmpty()) {
					this.room_list.add(tmp_room);
				} else {
					List<Room> tmp_room_list = new ArrayList<>(this.room_list);
					if (!this.room_overlapping(tmp_room, tmp_room_list))
						this.room_list.add(tmp_room);
				}
			}
		});

		IntStream.range(0, this.room_list.size() - 1)
				.forEach(a -> this.join_rooms(this.room_list.get(a), this.room_list.get(a + 1)));

		// do the random joins
		IntStream.range(0, this.random_connections).forEach(a-> {
			Room room_1 = this.room_list.get(random.nextInt(0, this.room_list.size() - 1));
			Room room_2 = this.room_list.get(random.nextInt(0, this.room_list.size() - 1));
			this.join_rooms(room_1, room_2);
		});
//
//        # do the spurs
//        for a in range(this.random_spurs):
//            room_1 = [random.randint(2, this.width - 2), random.randint(
//                     2, this.height - 2), 1, 1]
//            room_2 = this.room_list[random.randint(0, len(this.room_list) - 1)]
//            this.join_rooms(room_1, room_2)
//
//        # fill the map
//        # paint rooms
//        for room_num, room in enumerate(this.room_list):
//            for b in range(room[2]):
//                for c in range(room[3]):
//                    this.level[room[1] + c][room[0] + b] = 'floor'
//
//        # paint corridors
//        for corridor in this.corridor_list:
//            x1, y1 = corridor[0]
//            x2, y2 = corridor[1]
//            for width in range(abs(x1 - x2) + 1):
//                for height in range(abs(y1 - y2) + 1):
//                    this.level[min(y1, y2) + height][
//                        min(x1, x2) + width] = 'floor'
//
//            if len(corridor) == 3:
//                x3, y3 = corridor[2]
//
//                for width in range(abs(x2 - x3) + 1):
//                    for height in range(abs(y2 - y3) + 1):
//                        this.level[min(y2, y3) + height][
//                            min(x2, x3) + width] = 'floor'
//
//        # paint the walls
//        for row in range(1, this.height - 1):
//            for col in range(1, this.width - 1):
//                if this.level[row][col] == 'floor':
//                    if this.level[row - 1][col - 1] == 'stone':
//                        this.level[row - 1][col - 1] = 'wall'
//
//                    if this.level[row - 1][col] == 'stone':
//                        this.level[row - 1][col] = 'wall'
//
//                    if this.level[row - 1][col + 1] == 'stone':
//                        this.level[row - 1][col + 1] = 'wall'
//
//                    if this.level[row][col - 1] == 'stone':
//                        this.level[row][col - 1] = 'wall'
//
//                    if this.level[row][col + 1] == 'stone':
//                        this.level[row][col + 1] = 'wall'
//
//                    if this.level[row + 1][col - 1] == 'stone':
//                        this.level[row + 1][col - 1] = 'wall'
//
//                    if this.level[row + 1][col] == 'stone':
//                        this.level[row + 1][col] = 'wall'
//
//                    if this.level[row + 1][col + 1] == 'stone':
//                        this.level[row + 1][col + 1] = 'wall'
	}

	private Object join_rooms(Room room_1, Room room_2) {
        # sort by the value of x
        sorted_room = [room_1, room_2]
        sorted_room.sort(key=lambda x_y: x_y[0])

        x1 = sorted_room[0][0]
        y1 = sorted_room[0][1]
        w1 = sorted_room[0][2]
        h1 = sorted_room[0][3]
        x1_2 = x1 + w1 - 1
        y1_2 = y1 + h1 - 1

        x2 = sorted_room[1][0]
        y2 = sorted_room[1][1]
        w2 = sorted_room[1][2]
        h2 = sorted_room[1][3]
        x2_2 = x2 + w2 - 1
        y2_2 = y2 + h2 - 1

        # overlapping on x
        if x1 < (x2 + w2) and x2 < (x1 + w1):
            jx1 = random.randint(x2, x1_2)
            jx2 = jx1
            tmp_y = [y1, y2, y1_2, y2_2]
            tmp_y.sort()
            jy1 = tmp_y[1] + 1
            jy2 = tmp_y[2] - 1

            corridors = self.corridor_between_points(jx1, jy1, jx2, jy2)
            self.corridor_list.append(corridors)

        # overlapping on y
        elif y1 < (y2 + h2) and y2 < (y1 + h1):
            if y2 > y1:
                jy1 = random.randint(y2, y1_2)
                jy2 = jy1
            else:
                jy1 = random.randint(y1, y2_2)
                jy2 = jy1
            tmp_x = [x1, x2, x1_2, x2_2]
            tmp_x.sort()
            jx1 = tmp_x[1] + 1
            jx2 = tmp_x[2] - 1

            corridors = self.corridor_between_points(jx1, jy1, jx2, jy2)
            self.corridor_list.append(corridors)

        # no overlap
        else:
            join = None
            if join_type is 'either':
                join = random.choice(['top', 'bottom'])
            else:
                join = join_type

            if join is 'top':
                if y2 > y1:
                    jx1 = x1_2 + 1
                    jy1 = random.randint(y1, y1_2)
                    jx2 = random.randint(x2, x2_2)
                    jy2 = y2 - 1
                    corridors = self.corridor_between_points(
                        jx1, jy1, jx2, jy2, 'bottom')
                    self.corridor_list.append(corridors)
                else:
                    jx1 = random.randint(x1, x1_2)
                    jy1 = y1 - 1
                    jx2 = x2 - 1
                    jy2 = random.randint(y2, y2_2)
                    corridors = self.corridor_between_points(
                        jx1, jy1, jx2, jy2, 'top')
                    self.corridor_list.append(corridors)

            elif join is 'bottom':
                if y2 > y1:
                    jx1 = random.randint(x1, x1_2)
                    jy1 = y1_2 + 1
                    jx2 = x2 - 1
                    jy2 = random.randint(y2, y2_2)
                    corridors = self.corridor_between_points(
                        jx1, jy1, jx2, jy2, 'top')
                    self.corridor_list.append(corridors)
                else:
                    jx1 = x1_2 + 1
                    jy1 = random.randint(y1, y1_2)
                    jx2 = random.randint(x2, x2_2)
                    jy2 = y2_2 + 1
                    corridors = self.corridor_between_points(
                        jx1, jy1, jx2, jy2, 'bottom')
                    self.corridor_list.append(corridors)		return null;
	}

	private boolean room_overlapping(Room room, List<Room> room_list) {
		for (Room current_room : room_list) {
			if (room.getX() < (current_room.getX() + current_room.getW())
					&& current_room.getX() < (room.getX() + room.getW())
					&& room.getY() < (current_room.getY() + current_room.getH())
					&& current_room.getY() < (room.getY() + room.getH()))
				return true;
		}
		return false;
	}

	private Room gen_room() {
		int w = random.nextInt(this.min_room_xy, this.max_room_xy);
		int h = random.nextInt(this.min_room_xy, this.max_room_xy);
		int x = random.nextInt(1, this.width - w - 1);
		int y = random.nextInt(1, this.height - h - 1);
		return Room.builder().w(w).h(h).x(x).y(y).build();
	}
}
