package org.timecrafters.minibots.cyberarm.chiron.tasks.field_localizer;

import org.timecrafters.minibots.cyberarm.chiron.Robot;

import java.util.ArrayList;

public class Field {
    private final ArrayList<Obstacle> obstacles = new ArrayList<>();
    private final Robot robot;

    public Field(Robot robot) {
        this.robot = robot;

        populateField();
    }

    private void populateField() {
        // ORIGIN is field CENTER point with RED alliance on the RIGHT (i.e. Audience overhead perspective)

        // Ground Junctions
        double posX = -48;
        double posY = -48;

        for (int y = 0; y < 3; y++) {
            posY += (y * 48); // Ground Junctions are 48 inches apart

            for (int x = 0; x < 3; x++) {
                posX += (x * 48);

                addGroundJunction(posX, posY);
            }
        }

        // Pole Junctions
        posX = -48;
        posY = -48;

        for (int y = 0; y < 5; y++) {
            posY += (y * 24); // Pole Junctions are 24 inches apart

            for (int x = 0; x < 5; x++) {
                posX += (x * 24);

                // FIXME: Don't add phantom poles where ground junctions are placed
                addPole(posX, posY);
            }
        }
    }

    private void addPole(double x, double y) {
        obstacles.add(
                new Obstacle(x, y, robot.getConfiguration().variable("Field", "Obstacles", "pole_radius_in_inches").value())
        );
    }

    private void addGroundJunction(double x, double y) {
        obstacles.add(
            new Obstacle(x, y, robot.getConfiguration().variable("Field", "Obstacles", "ground_junction_radius_in_inches").value())
        );
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }
}
