package edu.oswego.winahrad.balldemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PushBumper {
    Body body;
    ScaleConverter scale;

    public PushBumper(PolygonMapObject mapObject, World world, ScaleConverter scale) {
        this.scale = scale;

        Polygon polygon = mapObject.getPolygon();

        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.StaticBody;
        bdef.position.set(polygon.getX(), polygon.getY());

        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.set(polygon.getVertices());

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.friction = 0.0f;
        fdef.density = 2.0f;
        fdef.restitution = 1.0f;
        body.createFixture(fdef);

        // dispose after creating fixture
        shape.dispose();

        body.setUserData(this);
    }

    public void handleCollision(Contact contact, boolean isA) {
        Gdx.app.log("push bumper", "PUSH!!!!");
        Body target;
        if (isA) {
            target = contact.getFixtureB().getBody();
        }
        else {
            target = contact.getFixtureA().getBody();
        }
        Object type = target.getUserData();
        if (type instanceof Ball) {
            // TODO: what i want is to be able to instantly accelerate the object
            //       up to near maximum speed. the math for this could probably
            //       be better. if we use the center of this object and the
            //       outside point of contact to create a direction vector. we
            //       could then use that to more accurately apply the force.
            //       not sure about the math to determine how much force to apply.
            float force = 1000000.0f;
            target.applyLinearImpulse(
                force * target.getLinearVelocity().x,
                force * target.getLinearVelocity().y,
                // FIXME: what point do i need to use to not create a lot of spin?
                target.getLocalCenter().x,
                target.getLocalCenter().y,
                //target.getPosition().x,
                //target.getPosition().y,
                true);
        }
    }
}
