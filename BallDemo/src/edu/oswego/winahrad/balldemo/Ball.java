package edu.oswego.winahrad.balldemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Ball {
    private final Texture texture;
    private final Sprite sprite;
    private final Body body;

    ScaleConverter scale;

    public Ball(EllipseMapObject mapObject, World world, ScaleConverter scale) {
        this.scale = scale;

        // http://opengameart.org/content/orbs-wo-drop-shadows
        Ellipse ellipse = mapObject.getEllipse();
        texture = new Texture(Gdx.files.internal("data/GreenOrb.png"));
        sprite = new Sprite(texture);

        // TODO: sprites come up 2x as large as they should be.
        //       there must be some sort of scaling going somewhere else
        sprite.setScale(0.5f);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(
                scale.pixelsToMeters(ellipse.x + (ellipse.width * 0.5f)),
                scale.pixelsToMeters(ellipse.y + (ellipse.height * 0.5f)));
        bodyDef.angularDamping = 0.1f;
        bodyDef.linearDamping = 0.1f;
        //bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(scale.pixelsToMeters(16f));

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.7f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        body.createFixture(fixtureDef);
        body.setAngularDamping(1);

        // dispose after creating fixture
        shape.dispose();
    }

    public void applyLinearImpulse(float x, float y) {
        body.applyLinearImpulse(
            x,
            y,
            body.getPosition().x,
            body.getPosition().y,
            true);
//        body.applyForceToCenter(x, y, false);
    }

    public void render(SpriteBatch batch) {
        sprite.setPosition(getX(), getY());
        sprite.draw(batch);
    }

    public float getX() {
        return scale.metersToPixels(body.getPosition().x) - (sprite.getWidth() * 0.5f);
    }

    public float getY() {
        return scale.metersToPixels(body.getPosition().y) - (sprite.getHeight() * 0.5f);
    }

    public void dispose() {
        texture.dispose();
    }

    public Body getBody() {
        return body;
    }
}
