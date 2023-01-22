package org.timecrafters.minibots.cyberarm.chiron.tasks.field_localizer;

import com.acmerobotics.roadrunner.geometry.Vector2d;

public class Obstacle {
    public final double x, y, radius;

    public Obstacle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    // TODO: Do the maths to get a normalized vector pointing AWAY from the obstacle's origin
    public Vector2d antiCollisionNormal(Vector2d robotVelocity) {
        return new Vector2d(0, 0);
    }
}
