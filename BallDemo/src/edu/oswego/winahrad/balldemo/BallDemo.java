package edu.oswego.winahrad.balldemo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BallDemo extends Game {
    SpriteBatch batch;
    BitmapFont font;
    ShapeRenderer shapeRenderer;

    int width = 480;
    int height = 320;

    @Override
    public void create() {
        batch = new SpriteBatch();
        // Use LibGDX's default Arial font.
        font = new BitmapFont();
        setScreen(new StartScreen(this));

        shapeRenderer = new ShapeRenderer();
   }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
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
