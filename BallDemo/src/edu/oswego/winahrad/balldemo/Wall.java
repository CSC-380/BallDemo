package edu.oswego.winahrad.balldemo;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Wall {
    Body body;

    public Wall(PolygonMapObject mapObject, World world) {
        Polygon polygon = mapObject.getPolygon();

        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.StaticBody;
        bdef.position.set(polygon.getX(), polygon.getY());

        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.set(polygon.getVertices());

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.friction = 0.5f;
        fdef.density = 5.0f;
        fdef.restitution = 0.0f;
        body.createFixture(fdef);

        // dispose after creating fixture
        shape.dispose();

        body.setUserData(this);
    }
}
