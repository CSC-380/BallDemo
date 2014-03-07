package edu.oswego.winahrad.balldemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Wall {
    Body body;
    ScaleConverter scale;

    public Wall(PolygonMapObject mapObject, World world, ScaleConverter scale) {
        this.scale = scale;

        Polygon polygon = mapObject.getPolygon();
        polygon.scale(scale.getScale());

        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.StaticBody;
        bdef.position.set(polygon.getX(), polygon.getY());

        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.set(polygon.getTransformedVertices());

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

    public Wall(RectangleMapObject mapObject, World world, ScaleConverter scale) {
        this.scale = scale;

        Rectangle rectangle = mapObject.getRectangle();

        Gdx.app.log("rectangle wall", rectangle.x + " " + rectangle.y);
        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.StaticBody;
        //bdef.position.set(rectangle.x, rectangle.y);
        //Gdx.app.log("rectangle wall", "set position");

        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        Vector2 center = new Vector2(
                scale.pixelsToMeters(rectangle.x + (rectangle.width * 0.5f)),
                scale.pixelsToMeters(rectangle.y + (rectangle.height * 0.5f)));
        shape.setAsBox(
                scale.pixelsToMeters(rectangle.width * 0.5f),
                scale.pixelsToMeters(rectangle.height * 0.5f),
                center,
                0.0f);

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
