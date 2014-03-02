package edu.oswego.winahrad.balldemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BallDemo extends Game {
    SpriteBatch batch;
    BitmapFont font;

    int width = 480;
    int height = 320;

    boolean useDpad = false;
    boolean debugView = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        useDpad = !Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);

        // Use LibGDX's default Arial font.
        font = new BitmapFont();

        setScreen(new StartScreen(this));
   }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    @Override
    public void render() {
	super.render();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
