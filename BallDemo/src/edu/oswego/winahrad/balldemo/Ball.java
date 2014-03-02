package edu.oswego.winahrad.balldemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
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

    public Ball(EllipseMapObject mapObject, World world) {
        // http://opengameart.org/content/orbs-wo-drop-shadows
        texture = new Texture(Gdx.files.internal("data/GreenOrb.png"));
        sprite = new Sprite(texture);

        // dont know why scale needs to be 2...
        // maybe i need to tweak the texture size or something...
        sprite.setScale(2);

        sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(mapObject.getEllipse().x, mapObject.getEllipse().y);
        bodyDef.angularDamping = 0.1f;
        bodyDef.linearDamping = 0.1f;
        //bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(10f);

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
        sprite.setPosition(
            body.getPosition().x - (sprite.getWidth() / 2f),
            body.getPosition().y - (sprite.getHeight() / 2f));
        sprite.draw(batch);
    }

    public void dispose() {
        texture.dispose();
    }

    public Body getBody() {
        return body;
    }
}
