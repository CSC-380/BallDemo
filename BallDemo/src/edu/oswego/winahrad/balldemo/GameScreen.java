package edu.oswego.winahrad.balldemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    final BallDemo game;
    final OrthographicCamera camera;

    final Circle ball;

    World world = new World(new Vector2(0, 0), true);

    BodyDef ballBodyDef;
    CircleShape circle;
    FixtureDef ballFixtureDef;
    Fixture fixture;
    Body ballBody;
    int fps = 0;
    long startTime;

    TiledMap map;
    OrthogonalTiledMapRenderer mapRenderer;

    public GameScreen(final BallDemo game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.width, game.height);


        // FIXME: how can i have this just load for both a desktop and android?
        //        for desktop i need to path it to the android folder,
        //        do i just need copies for each?
        map = new TmxMapLoader().load("data/balldemo.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1);
        mapRenderer.setView(camera);

        MapLayer layer = map.getLayers().get(1);
        for (MapObject obj : layer.getObjects()) {
            // NOTE: when creating the map objects the polygons must have no more
            //       than 8 vertices and must not be concave. this is a limitation
            //       of the physics engine. so complex shapes need to be composed
            //       of multiple adjacent polygons.
            if (obj instanceof PolygonMapObject) {
                PolygonMapObject pobj = (PolygonMapObject)obj;
                Polygon polygon = pobj.getPolygon();

                BodyDef bdef = new BodyDef();
                bdef.position.set(polygon.getX(), polygon.getY());
                Body body = world.createBody(bdef);
                PolygonShape shape = new PolygonShape();
                shape.set(polygon.getVertices());
                body.createFixture(shape, 0.0f);
                // dispose after creating fixture
                shape.dispose();
            }
        }

        ball = new Circle((game.width/2), (game.height/2), 10);

        ballBodyDef = new BodyDef();
        ballBodyDef.type = BodyType.DynamicBody;
        ballBodyDef.position.set(game.width/2, game.height/2);

        ballBody = world.createBody(ballBodyDef);
        ballBody.setUserData(ball);

        circle = new CircleShape();
        circle.setRadius(10f);

        // Create a fixture definition to apply our shape to
        FixtureDef ballFixtureDef = new FixtureDef();
        ballFixtureDef.shape = circle;
        ballFixtureDef.density = .5f;
        ballFixtureDef.friction = 0.4f;
        ballFixtureDef.restitution = 0.6f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        fixture = ballBody.createFixture(ballFixtureDef);

        // dispose after creating fixture
        circle.dispose();

        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        // TODO: get the values for pitch and roll up on the screen.
        //       i need to be able to see them to see how they should affect things.
        //       can we make use of yaw as well?



        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        mapRenderer.render();

        ball.setX(ballBody.getPosition().x);
        ball.setY(ballBody.getPosition().y);

        game.batch.begin();
        game.font.draw(game.batch, "FPS: " + fps, 10, 15);
        if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
            game.font.draw(game.batch, "X: " + Gdx.input.getAccelerometerX(), 10, 30);
            game.font.draw(game.batch, "Y: " + Gdx.input.getAccelerometerY(), 10, 45);
            game.font.draw(game.batch, "Z: " + Gdx.input.getAccelerometerZ(), 10, 60);
        }
        game.batch.end();

        game.shapeRenderer.begin(ShapeType.Filled);
        game.shapeRenderer.setColor(0, 1, 0, 1);
        game.shapeRenderer.circle(ball.x, ball.y, ball.radius);
        game.shapeRenderer.end();

        // update every second
        if (TimeUtils.nanoTime() - startTime > 1000000000)  {
            fps = Gdx.graphics.getFramesPerSecond();
            startTime = TimeUtils.nanoTime();
        }

        if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
            ballBody.applyLinearImpulse(
                // accelerometer is reversed from screen coordinates
                Gdx.input.getAccelerometerY() * 10,
                Gdx.input.getAccelerometerX() * -10,
                ballBody.getPosition().x,
                ballBody.getPosition().y,
                false);
        }
        world.step(1/60f, 6, 2);
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
    }
}
