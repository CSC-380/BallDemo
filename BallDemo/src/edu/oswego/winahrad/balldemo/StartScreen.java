package edu.oswego.winahrad.balldemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class StartScreen implements Screen {

    final BallDemo game;
    //final OrthographicCamera camera;
    private final Stage stage;
    private final Skin skin;

    public StartScreen(final BallDemo game) {
        this.game = game;
        //camera = new OrthographicCamera();
        //camera.setToOrtho(false, game.width, game.height);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        skin.add("default", new BitmapFont());

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
        CheckBoxStyle checkBoxStyle = new CheckBoxStyle();
        checkBoxStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        checkBoxStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        checkBoxStyle.checked = skin.newDrawable("white", Color.BLUE);
        checkBoxStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        checkBoxStyle.font = skin.getFont("default");
        skin.add("default", checkBoxStyle);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Button play = new TextButton("Click to Play!", textButtonStyle);
        table.add(play);

        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        table.row();

        final CheckBox useDpad = new CheckBox("Use DPad: " + (game.useDpad ? "X" : " ") , checkBoxStyle);
        table.add(useDpad);

        useDpad.setChecked(game.useDpad);
        useDpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.useDpad = useDpad.isChecked();
                useDpad.setText("Use DPad: " + (game.useDpad ? "X" : " "));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        /*
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Touch Screen to Start", game.width / 2, game.height / 2);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
        */
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(width, height, true);
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
        stage.dispose();
        skin.dispose();
    }
}
