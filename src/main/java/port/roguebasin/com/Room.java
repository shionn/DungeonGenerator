package port.roguebasin.com;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder()
@AllArgsConstructor
@Getter()
public class Room {

	private int x, y, w, h;
}
