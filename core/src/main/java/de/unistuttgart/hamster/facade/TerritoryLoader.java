package de.unistuttgart.hamster.facade;

import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Direction;
import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Location;
import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Size;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class TerritoryLoader {
    private final TerritoryBuilder territoryBuilder;
    private Size loadedTerritoryDimensions;

    private TerritoryLoader(final TerritoryBuilder territoryBuilder) {
        super();
        this.territoryBuilder = territoryBuilder;
    }

    public static TerritoryLoader initializeFor(final HamsterGame game) {
        final var builder = new TerritoryBuilder(game);
        return new TerritoryLoader(builder);
    }

    /**
     * Loads the territory from the provided resource file.
     * @param territoryFile the path to a territory encoded in a territory string
     */
    public void loadFromResourceFile(final String territoryFile) throws IOException {
        final List<String> list = readLinesFromTerritoryResourceFile(territoryFile);
        interpretLoadedTerritoryLines(list);
    }

    /**
     * Loads the territory from the provided InputStream
     * @param inputStream the InputStream which is used to get the territory
     *                    lines
     */
    public void loadFromInputStream(final InputStream inputStream) throws IOException {
        final List<String> list = readLinesFromTerritoryInputStream(inputStream);
        interpretLoadedTerritoryLines(list);
    }

    private void interpretLoadedTerritoryLines(final List<String> list) {
        final String[] lines = list.toArray(new String[]{});
        setSizeFromStrings(lines);
        final String[] territoryDefinition = Arrays.copyOfRange(lines,2,lines.length);
        buildTiles(territoryDefinition);
    }

    private void setSizeFromStrings(final String[] lines) {
        this.loadedTerritoryDimensions = new Size(Integer.parseInt(lines[0]), Integer.parseInt(lines[1]));
        this.territoryBuilder.initTerritory(this.loadedTerritoryDimensions.getColumnCount(), this.loadedTerritoryDimensions.getRowCount());
    }

    private void buildTiles(final String[] lines) {
        final LinkedList<Location> grainLocations = new LinkedList<Location>();
        Optional<Location> defaultHamsterLocation = Optional.empty();
        Optional<Direction> defaultHamsterDirection = Optional.empty();

        for (int row = 0; row < this.loadedTerritoryDimensions.getRowCount(); row++) {
            for (int column = 0; column < this.loadedTerritoryDimensions.getColumnCount(); column++) {
                final Location currentLocation = new Location(column, row);
                final char tileCode = lines[row].charAt(column);
                switch (tileCode) {
                    case ' ':
                        break;
                    case '#':
                        createWallAt(currentLocation);
                        break;
                    case '*':
                        grainLocations.add(currentLocation);
                        break;
                    case '^':
                        grainLocations.add(currentLocation);
                        defaultHamsterLocation = Optional.of(currentLocation);
                        defaultHamsterDirection = Optional.of(Direction.NORTH);
                        break;
                    case '>':
                        grainLocations.add(currentLocation);
                        defaultHamsterLocation = Optional.of(currentLocation);
                        defaultHamsterDirection = Optional.of(Direction.EAST);
                        break;
                    case 'v':
                        grainLocations.add(currentLocation);
                        defaultHamsterLocation = Optional.of(currentLocation);
                        defaultHamsterDirection = Optional.of(Direction.SOUTH);
                        break;
                    case '<':
                        grainLocations.add(currentLocation);
                        defaultHamsterLocation = Optional.of(currentLocation);
                        defaultHamsterDirection = Optional.of(Direction.WEST);
                        break;
                    default:
                        throw new RuntimeException("Territory error.");
                }
            }
        }
        final int initialGrainCount = Integer.parseInt(lines[this.loadedTerritoryDimensions.getRowCount() + grainLocations.size()]);
        territoryBuilder.initDefaultLadybug(defaultHamsterLocation.get().getColumn(), defaultHamsterLocation.get().getRow(), defaultHamsterDirection.get());
        placeGrain(lines, grainLocations);
    }

    private List<String> readLinesFromTerritoryResourceFile(final String territoryFileName) throws IOException {
        final InputStream in = getClass().getClassLoader().getResourceAsStream(territoryFileName);
        if (in == null) {
            throw new IOException("Unable to load the territory from the filename: " + territoryFileName);
        }
        final List<String> result = readLinesFromTerritoryInputStream(in);
        in.close();
        return result;
    }

    private List<String> readLinesFromTerritoryInputStream(final InputStream inputStream) throws IOException {
        checkNotNull(inputStream);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final List<String> list = new ArrayList<String>();

        try (final Scanner input = new Scanner(reader))
        {
            while (input.hasNextLine()) {
                list.add(input.nextLine());
            }
        }

        return list;
    }

    private void checkNotNull(final Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
    }

    private void placeGrain(final String[] lines, final LinkedList<Location> grainLocations) {
        for (int i = 0; i < grainLocations.size(); i++) {
            final Location location = grainLocations.get(i);
            final int count = Integer.parseInt(lines[this.loadedTerritoryDimensions.getRowCount() + i]);
            territoryBuilder.addMushroomToTile(location);
        }
    }

    private void createWallAt(final Location currentLocation) {
        this.territoryBuilder.addWallToTile(currentLocation);
    }
}
