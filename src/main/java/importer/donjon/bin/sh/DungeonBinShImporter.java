package importer.donjon.bin.sh;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DungeonBinShImporter {

	public DungeonBinSh read() throws IOException {
		try (InputStream is = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream("The Lost Labyrinth of Sorrows 01.json")) {
			return new ObjectMapper().readValue(is, DungeonBinSh.class);
		}
	}

	public static void main(String[] args) throws IOException {
		new DungeonBinShImporter().read();
	}

}
