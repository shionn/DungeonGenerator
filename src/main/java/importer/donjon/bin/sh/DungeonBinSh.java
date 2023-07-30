package importer.donjon.bin.sh;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DungeonBinSh {
	CellBit cellBit;
	long cells[][];
	Map<String, CorridorFeatures> corridorFeatures;
	Details details;
	List<Egres> egress;
	List<Room> rooms;
	Map<String, String> settings;
	List<Stair> stairs;
	Map<String, String> wanderingMonsters;

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class CellBit {
		long aperture, arch;
		long block;
		long corridor;
		long door;
		long label, locked;
		long nothing;
		long perimeter, portcullis;
		long room, roomId;
		long secret, stairDown, stairUp;
		long trapped;

	}

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class CorridorFeatures {
		String detail, key, summary;
		Mark[] marks;
	}

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Mark {
		int col, row;
	}

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Details {
		String floor, illumination, special, temperature, walls;
	}

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Egres {
		int col, depth, row;
		String dir, type;
	}

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Room {
		int area, col, east, height, id, north, row, south, west, width;
		String shape, size;
		RoomContents contents;
		Map<String, RoomDoor[]> doors;
	}

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class RoomContents {
		RoomContentsDetail detail;
		String inhabited, summary;
	}

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class RoomContentsDetail {
		List<String> monster;
		List<String> hiddenTreasure;
		List<String> trap;
		String roomFeatures;
	}

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class RoomDoor {
		int col, row, outId;
		String desc, type, trap;
	}

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Stair {
		int col, row;
		String dir, key;
	}

}
