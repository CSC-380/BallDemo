package edu.oswego.winahrad.balldemo;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    final BallDemo game;
    final OrthographicCamera camera;

    Ball ball;

    World world = new World(new Vector2(0, 0), true);
    ContactListener contactListener;

    int fps = 0;
    long startTime;

    TiledMap map;
    OrthogonalTiledMapRenderer mapRenderer;

    float tiltX;
    float tiltY;

    ScaleConverter scale = new ScaleConverter(1f/64f);

    Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    final DecimalFormat decimalFormatter = new DecimalFormat("######.##");

    public GameScreen(final BallDemo game) {
        // TODO: apparently we can scale the graphics and the world to make things
        //       appear to be moving around much faster. this could possibly make
        //       the game feel a bit more responsive.

        this.game = game;

        debugRenderer.setDrawVelocities(true);
        debugRenderer.setDrawContacts(true);
        debugRenderer.setDrawJoints(true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.width, game.height);

        map = new TmxMapLoader().load("data/largekdub.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1);
        mapRenderer.setView(camera);

        MapLayer layer = map.getLayers().get("collision");
        for (MapObject obj : layer.getObjects()) {
            // NOTE: when creating the map objects the polygons must have no more
            //       than 8 vertices and must not be concave. this is a limitation
            //       of the physics engine. so complex shapes need to be composed
            //       of multiple adjacent polygons.
            if (obj.getName().equals("wall")) {
                if (obj instanceof PolygonMapObject) {
                    Gdx.app.log("populating map", "adding wall");
                    new Wall((PolygonMapObject)obj, world, scale);
                }
                else if (obj instanceof RectangleMapObject) {
                    Gdx.app.log("populating map", "adding wall");
                    new Wall((RectangleMapObject)obj, world, scale);
                }
            }
            else if (obj.getName().equals("bumper")) {
                if (obj instanceof PolygonMapObject) {
                    Gdx.app.log("populating map", "adding bumper");
                    new PushBumper((PolygonMapObject)obj, world, scale);
                }
            }
            else if (obj.getName().equals("ball spawn point")) {
                if (obj instanceof EllipseMapObject) {
                    Gdx.app.log("populating map", "adding ball");
                    ball = new Ball((EllipseMapObject)obj, world, scale);
                }
            }
        }

        if (game.useDpad) {
            Gdx.input.setInputProcessor(new InputProcessor() {
                @Override
                public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public boolean touchDragged(int screenX, int screenY, int pointer) {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public boolean scrolled(int amount) {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public boolean mouseMoved(int screenX, int screenY) {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public boolean keyUp(int keycode) {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public boolean keyTyped(char character) {
                    // TODO Auto-generated method stub
                    return false;
                }

                @Override
                public boolean keyDown(int keycode) {
                    switch (keycode) {
                    case Keys.LEFT:
                        tiltX -= 0.001f;
                        break;
                    case Keys.RIGHT:
                        tiltX += 0.001f;
                        break;
                    case Keys.UP:
                        tiltY += 0.001f;
                        break;
                    case Keys.DOWN:
                        tiltY -= 0.001f;
                        break;
                    case Keys.CENTER:
                        tiltX = 0f;
                        tiltY = 0f;
                        break;

                    default:
                        break;
                    }
                    Gdx.app.log("keydown", tiltX + " " + tiltY);
                    return false;
                }
            });
        }
        contactListener = new GameContactListener();
        world.setContactListener(contactListener);
        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        camera.position.set(
                scale.metersToPixels(ball.getBody().getPosition().x),
                scale.metersToPixels(ball.getBody().getPosition().y),
                camera.position.z);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        if (game.debugView) {
            debugRenderer.render(world, camera.combined.scl(1f / scale.getScale()));
        }
        else {
            mapRenderer.setView(camera);
            mapRenderer.render();
        }

        game.batch.begin();
        if (game.debugView) {
            renderTextInCameraView("FPS: " + fps, 10, 15);
            renderTextInCameraView("X: " + decimalFormatter.format(tiltX), 10, 30);
            renderTextInCameraView("Y: " + decimalFormatter.format(tiltY), 10, 45);
            renderTextInCameraView("BVel X: " + decimalFormatter.format(ball.getBody().getLinearVelocity().x), 10, 60);
            renderTextInCameraView("BVel Y: " + decimalFormatter.format(ball.getBody().getLinearVelocity().y), 10, 75);
            renderTextInCameraView("BVel A: " + decimalFormatter.format(ball.getBody().getAngularVelocity()), 10, 90);
        }
        else {
            ball.render(game.batch);
        }
        game.batch.end();


        // update every second
        if (TimeUtils.nanoTime() - startTime > 1000000000)  {
            fps = Gdx.graphics.getFramesPerSecond();
            startTime = TimeUtils.nanoTime();
        }

        if (!game.useDpad) {
            if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
                // accelerometer is reversed from screen coordinates, we are in landscape mode
                tiltX = Gdx.input.getAccelerometerY() * 0.001f;
                tiltY = Gdx.input.getAccelerometerX() * -0.001f;
            }
        }

        ball.applyLinearImpulse(tiltX, tiltY);

        //world.step(1/60f, 6, 2);
        world.step(1/45f, 10, 8);
    }

    private void renderTextInCameraView(String text, float x, float y) {
        game.font.draw(game.batch, text,
            camera.position.x - (camera.viewportWidth / 2f) + x,
            camera.position.y - (camera.viewportHeight / 2f) + y
        );
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
