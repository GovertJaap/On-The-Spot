package com.example.android.onthespot;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Path;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Math.sin;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends Activity {
    //Declaration of all variables used by this activity.
    int newX, newY, typeChance, score1, score2, life, xPos, yPos, lastSpawn, xTouch, yTouch, timeAlive, gameTimer, randomSpawnTime, player;
    int levelNumber, circleSize, rectangleSize, hexagonSize, circleSpawnChance, rectangleSpawnChance, hexagonSpawnChance, maximumShapes, spawnSpeed, music;
    float size, shapeSize, density, rotation;
    float circleSpeed, rectangleSpeed, hexagonSpeed;
    String newType, type;
    String backgroundColor, circleColor, rectangleColor, hexagonColor, circleBorderColor, rectangleBorderColor, hexagonBorderColor;
    boolean justTouched, transition;
    static boolean backFromPause;
    String circleColor2, rectangleColor2, hexagonColor2;
    List<MyView.Shape> shapes, oldShapes;
    Random rand;
    Paint paint;
    static MainActivity mainActivity;
    Vibrator v;
    Bitmap livesBitmap, timeBitmap, pauseBitmap;
    FullMenu musicClass = new FullMenu();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialization of all variables used by this activity.
        size = shapeSize = density = rotation = 0f;
        newX = newY = typeChance = xPos = yPos = lastSpawn = xTouch = yTouch = timeAlive = randomSpawnTime = 0;
        player = 1;
        score1 = score2 = 0;
        gameTimer = 3600;
        life = 3;
        newType = type = "";
        circleColor2 = rectangleColor2 = hexagonColor2 = "";
        justTouched = false;
        shapes = new ArrayList<>();
        oldShapes = new ArrayList<>();
        rand = new Random();
        paint = new Paint();
        mainActivity = this;
        livesBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.heart);
        timeBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.stopwatch);
        pauseBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.pause);
        backFromPause = false;

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray jArray = obj.getJSONArray("main");
            JSONObject levelData = jArray.getJSONObject(getIntent().getExtras().getInt("level"));
            levelNumber = levelData.getInt("level");
            backgroundColor = levelData.getString("backgroundColor");
            circleColor = levelData.getString("circleColor");
            rectangleColor = levelData.getString("rectangleColor");
            hexagonColor = levelData.getString("hexagonColor");
            circleBorderColor = levelData.getString("circleBorderColor");
            rectangleBorderColor = levelData.getString("rectangleBorderColor");
            hexagonBorderColor = levelData.getString("hexagonBorderColor");
            maximumShapes = levelData.getInt("maximumShapes");
            spawnSpeed = levelData.getInt("spawnSpeed");
            circleSize = levelData.getInt("circleSize");
            rectangleSize = levelData.getInt("rectangleSize");
            hexagonSize = levelData.getInt("hexagonSize");
            circleSpeed = (float) levelData.getDouble("circleSpeed");
            rectangleSpeed = (float) levelData.getDouble("rectangleSpeed");
            hexagonSpeed = (float) levelData.getDouble("hexagonSpeed");
            circleSpawnChance = levelData.getInt("circleSpawnChance");
            rectangleSpawnChance = levelData.getInt("rectangleSpawnChance");
            hexagonSpawnChance = levelData.getInt("hexagonSpawnChance");
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        if (levelNumber == 13) {
            gameTimer = 3600;
            circleColor = "#DF0101";
            rectangleColor = "#DF0101";
            hexagonColor = "#DF0101";
            circleColor2 = "#0174DF";
            rectangleColor2 = "#0174DF";
            hexagonColor2 = "#0174DF";
        }

        super.onCreate(savedInstanceState);

        if (musicClass.musicOn == true) {
            musicClass.mpPlayer = musicClass.mpPlayer.create(this, musicSwitch(levelNumber));
            musicClass.mpPlayer.start();
        }

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(new MyView(this));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onStop() {
        if (transition != true && musicClass.mpPlayer != null && musicClass.musicOn == true) {
            musicClass.mpPlayer.release();
            musicClass.exit = true;
            musicClass.playing = false;
            musicClass.mpPlayer = musicClass.mpPlayer.create(this, R.raw.menu);
            finish();
        }

        super.onStop();
    }

    @Override
    protected void onResume() {

        transition = false;
        super.onResume();
    }


    public class MyView extends View {

        public MyView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onDraw(Canvas canvas) {
            KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (!myKM.inKeyguardRestrictedInputMode()) {

                super.onDraw(canvas);

                //Get the density of the screen if not done already
                //This cannot be done during OnCreate since the display is not initialized at that point.
                if (density == 0) {
                    density = getResources().getDisplayMetrics().density;
                    livesBitmap = Bitmap.createScaledBitmap(livesBitmap, (int) (78.8 * density), (int) (64.8 * density), true);
                    timeBitmap = Bitmap.createScaledBitmap(timeBitmap, (int) (54.8 * density), (int) (64.8 * density), true);
                    pauseBitmap = Bitmap.createScaledBitmap(pauseBitmap, (int) (64.8 * density), (int) (64.8 * density), true);
                }

                randomSpawnTime = rand.nextInt((int) (spawnSpeed * 1.3)) + (int) (spawnSpeed / 1.5);

                //This code makes sure that there won't spawn any more shapes than the maximum number allowed.
                //Also checks the variable lastSpawn so that shapes don't spawn too fast after each other.
                if (shapes.size() <= maximumShapes && lastSpawn > randomSpawnTime || gameTimer == 3590) {
                    //Get a new random x and y coordinate used to spawn the new shape.
                    //The limits for this depend on the density and pixel height/width of the screen.
                    if (levelNumber == 13) {
                        player = rand.nextInt(3) + 1;
                        if (player == 1 || gameTimer > 1800) {
                            newX = (int) (rand.nextInt(getWidth() - Math.round(120 * density)) + (60 * density));
                            newY = (int) (rand.nextInt(getHeight() - Math.round(440 * density)) + (380 * density));
                        }
                        else if (player == 2) {
                            newX = (int) (rand.nextInt(getWidth() - Math.round(120 * density)) + (60 * density));
                            newY = (int) (rand.nextInt(getHeight() - Math.round(440 * density)) + (60 * density));
                        }
                    }

                    else {
                        newX = (int) (rand.nextInt(getWidth() - Math.round(120 * density)) + (60 * density));
                        newY = (int) (rand.nextInt(getHeight() - Math.round(210 * density)) + (150 * density));
                    }

                    if (levelNumber == 13 && gameTimer < 1800) {
                        maximumShapes = 10;
                        spawnSpeed = 15;
                    }
                    rotation = rand.nextInt(360);

                    //Get a random number between 0 and 100 used for the chance calculation of what shape will spawn.
                    typeChance = rand.nextInt(100);

                    //Depending on the level there is a specific chance percentage for what shape will spawn.
                    if (typeChance < circleSpawnChance) {
                        //Change the starting size of the shape depending on whether it's a circle, rectangle or hexagon.
                        size = circleSize * density;
                        //Make sure the new shape doesn't overlap an existing shape.
                        checkCollision();
                        newType = "Circle";
                    } else if (typeChance < (circleSpawnChance + rectangleSpawnChance)) {
                        //Change the starting size of the shape depending on whether it's a circle, rectangle or hexagon.
                        size = rectangleSize * density;
                        //Make sure the new shape doesn't overlap an existing shape.
                        checkCollision();
                        newType = "Rectangle";
                    } else {
                        //Change the starting size of the shape depending on whether it's a circle, rectangle or hexagon.
                        size = hexagonSize * density;
                        //Make sure the new shape doesn't overlap an existing shape.
                        checkCollision();
                        newType = "Hexagon";
                    }

                    if (player == 1 || (levelNumber == 13 && gameTimer > 1800)) {
                        //Make a new shape according to the Shape class we've created, give it all the data used to determine it's characteristics.
                        Shape newShape = new Shape(newX, newY, size, newType, rotation, 0, 1);
                        //Add the shape to our list of existing shapes.
                        shapes.add(newShape);
                    }

                    if (gameTimer < 1800 && player == 2) {
                        Shape newShape = new Shape(newX, newY, size, newType, rotation, 0, 2);
                        shapes.add(newShape);
                    }

                    else if (levelNumber == 13 && gameTimer > 1800){
                        Shape newShape = new Shape(newX, getHeight() - newY, size, newType, rotation, 0, 2);
                        shapes.add(newShape);
                    }

                    //Reset the spawn timer.
                    lastSpawn = 0;
                }

                else {
                    //Increment the timer if no new shape has spawned this frame.
                    lastSpawn++;
                }

                /****** Code for drawing background ********/
                paint.setColor(Color.parseColor(backgroundColor));
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawPaint(paint);

                for (int i = 0; i < oldShapes.size(); i++) {
                    xPos = oldShapes.get(i).getXPos();
                    yPos = oldShapes.get(i).getYPos();
                    shapeSize = oldShapes.get(i).getShapeSize();
                    type = oldShapes.get(i).getType();
                    rotation = oldShapes.get(i).getRotation();
                    timeAlive = oldShapes.get(i).getTimeAlive();

                    switch (type) {
                        case "Circle":
                            drawOldCircle(canvas); //Call method to draw a circle.
                            if (timeAlive > 20) {
                                shapeSize = shapeSize - 34;
                                drawOldCircle(canvas);
                                shapeSize = shapeSize + 34;
                            }
                            if (timeAlive > 40) {
                                shapeSize = shapeSize - 68;
                                drawOldCircle(canvas);
                                shapeSize = shapeSize + 68;
                            }
                            break;
                        case "Rectangle":
                            drawOldRectangle(canvas); //Call method to draw a rectangle.
                            if (timeAlive > 20) {
                                shapeSize = shapeSize - 34;
                                drawOldRectangle(canvas);
                                shapeSize = shapeSize + 34;
                            }
                            if (timeAlive > 40) {
                                shapeSize = shapeSize - 68;
                                drawOldRectangle(canvas);
                                shapeSize = shapeSize + 68;
                            }
                            break;
                        case "Hexagon":
                            drawOldHexagon(canvas); //Call method to draw a hexagon.
                            if (timeAlive > 20) {
                                shapeSize = shapeSize - 34;
                                drawOldHexagon(canvas);
                                shapeSize = shapeSize + 34;
                            }
                            if (timeAlive > 40) {
                                shapeSize = shapeSize - 68;
                                drawOldHexagon(canvas);
                                shapeSize = shapeSize + 68;
                            }
                            break;
                        default:
                            break;
                    }

                    oldShapes.get(i).setShapeSize(shapeSize+1.7f);
                    oldShapes.get(i).setTimeAlive(timeAlive+1);

                    if (timeAlive > 120) {
                        oldShapes.remove(i);
                        i--;
                    }
                }

                //This for loop goes through all the shapes currently on the screen and draws them.
                //The code also checks whether a shape has been touched this frame and will delete it accordingly.
                for (int i = 0; i < shapes.size(); i++) {

                    //Get the coordinates, size and type of the current shape in the list.
                    xPos = shapes.get(i).getXPos();
                    yPos = shapes.get(i).getYPos();
                    shapeSize = shapes.get(i).getShapeSize();
                    type = shapes.get(i).getType();
                    rotation = shapes.get(i).getRotation();
                    player = shapes.get(i).getPlayer();

                    //Depending on the type of the shape, call the right method and change the size of the shape.
                    switch (type) {
                        case "Circle":
                            drawCircle(canvas); //Call method to draw a circle.
                            shapeSize = shapeSize - (circleSpeed * density);
                            break;
                        case "Rectangle":
                            rotation = rotation + 1.5f;
                            drawRectangle(canvas); //Call method to draw a rectangle.
                            shapeSize = shapeSize - (rectangleSpeed * density);
                            shapes.get(i).setRotation(rotation);
                            break;
                        case "Hexagon":
                            rotation = rotation + 3f;
                            drawHexagon(canvas); //Call method to draw a hexagon.
                            shapeSize = shapeSize - (hexagonSpeed * density);
                            shapes.get(i).setRotation(rotation);
                            break;
                        default:
                            break;
                    }

                    //Save the updated size in our object.
                    shapes.get(i).setShapeSize(shapeSize);

                    //If life reaches zero, the player will be game over
                    if (life <= 0) {
//                        shapes.removeAll(shapes);
                        if (musicClass.musicOn == true) {
                            musicClass.mpPlayer.release();
                            musicClass.playing = false;
                        }
                        transition = true;
                        Intent activity = new Intent(MainActivity.this, GameOver.class);
                        activity.putExtra("level", levelNumber);
                        activity.putExtra("score", score1);
                        startActivity(activity);
                        finish();
                    }

                    if (musicClass.playing == false && musicClass.mpPlayer != null && musicClass.musicOn == true && backFromPause == true) {
                        musicClass.mpPlayer.seekTo(musicClass.length);
                        musicClass.mpPlayer.start();
                        musicClass.playing = true;
                        backFromPause = false;
                    }

                    //If the user has not touched the shape, remove it from our list and decrement i by 1.
                    if (shapeSize <= 0f) {
                        if (levelNumber < 13) {
                            shapes.remove(i);
                            i--;
                            life--;
                            v.vibrate(200);
                        }

                        else {
                            if (gameTimer < 1800) {
                                shapes.remove(i);
                                i--;
                            }
                            else {
                                shapes.removeAll(shapes);
                                i = 0;
                            }
                        }
                    }

                    else {
                        //Check if there is a new touch this frame.
                        if (justTouched == true) {

                            //If this is the last shape in the list to be checked this frame,
                            //set justTouched on false so that there can be a new coordinate next frame.
                            if (shapes.size() - 1 == i) {
                                justTouched = false;
                            }

                            //Compare the coordinates of where the user touched the screen with the coordinates of the shape.
                            //If they overlap (meaning the user 'touched' the shape, remove the shape from the list and increase the score.
                            if (xTouch > (xPos - shapeSize - (5 * density)) &&
                                    xTouch < (xPos + shapeSize + (5 * density)) &&
                                    yTouch > (yPos - shapeSize - (5 * density)) &&
                                    yTouch < (yPos + shapeSize + (5 * density))) {
                                shapes.get(i).setShapeSize(0);
                                oldShapes.add(shapes.get(i));
                                if (levelNumber == 13) {
                                    if (gameTimer < 1800) {
                                        if (player == 2) {
                                            score2 = score2 + (int) (shapeSize * 5); //Depending on the size of the shape, give more or less points.
                                        }
                                        else {
                                            score1 = score1 + (int) (shapeSize * 5); //Depending on the size of the shape, give more or less points.
                                        }
                                        shapes.remove(i);
                                        i--;
                                    }
                                    else {
                                        if (player == 2) {
                                            score2 = score2 + (int) (shapeSize * 15); //Depending on the size of the shape, give more or less points.
                                        }
                                        else {
                                            score1 = score1 + (int) (shapeSize * 15); //Depending on the size of the shape, give more or less points.
                                        }
                                        shapes.removeAll(shapes);
                                        i = 0;
                                    }
                                }
                                else {
                                    score1 = score1 + (int) (shapeSize * 5); //Depending on the size of the shape, give more or less points.
                                    shapes.remove(i);
                                    i--;
                                }
                                justTouched = false;
                            }

                            if (xTouch > (getWidth() - (getWidth() / 4.8)) &&
                                    xTouch < (getWidth() - (getWidth() / 4.8) + (64.8 * density)) &&
                                    yTouch > (5 * density) &&
                                    yTouch < (69.8 * density)) {
                                if (musicClass.musicOn == true) {
                                    musicClass.mpPlayer.pause();
                                    musicClass.playing = false;
                                    musicClass.length = musicClass.mpPlayer.getCurrentPosition();
                                }

                                transition = true;
                                Intent activity = new Intent(MainActivity.this, Pause.class);
                                activity.putExtra("level", levelNumber);
                                startActivity(activity);
                            }
                        }
                    }
                }

                if (levelNumber == 13) {
                    paint.setColor(Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(5 * density);
                    canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);

                    paint.setStrokeWidth(0);
                    paint.setTextSize(20 * density);
                    canvas.drawText("Player 1: " + score1, (int) (getWidth() / 1.7f), (int) (getHeight() - 7 * density), paint);

                    canvas.rotate(180, 360, 640);
                    canvas.drawText("Player 2: " + score2, (int) (getWidth() - (getWidth() / 2.45f)), getHeight() - 7 * density, paint);
                    canvas.rotate(-180, 360, 640);
                }

                else {
                    paint.setColor(Color.parseColor("#F8ECE0"));
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(0, 0, getWidth(), (int) (density * 75), paint);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(5 * density);
                    paint.setColor(Color.DKGRAY);
                    canvas.drawRect(-5 * density, -5 * density, getWidth() + 5 * density, (int) (density * 77), paint);
                    canvas.drawBitmap(livesBitmap, (int) (getWidth() / 36), 5 * density, null);
                    canvas.drawBitmap(timeBitmap, (int) (getWidth() / 3.27f), 5 * density, null);
                    canvas.drawBitmap(pauseBitmap, (int) (getWidth() - (getWidth() / 4.8)), 5 * density, null);

                    paint.setColor(Color.WHITE);
                    paint.setTextSize(34 * density);
                    paint.setStrokeWidth(0);
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText("" + life, (int) (getWidth() / 36 + 39.4f * density), (int) (53.9f * density), paint);
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(26 * density);
                    canvas.drawText("" + gameTimer / 60, (int) (getWidth() / 3.27f + 27.4f * density), (int) (52.4f * density), paint);
                    canvas.drawText("Score: ", (int) (getWidth() / 1.57f), 31 * density, paint);
                    paint.setTextSize(30 * density);
                    canvas.drawText("" + score1, (int) (getWidth() / 1.60f), 62 * density, paint);
                }

                if (gameTimer <= 0) {
                    if (musicClass.musicOn == true) {
                        musicClass.mpPlayer.release();
                        musicClass.playing = false;
                    }

                    transition = true;
                    if (levelNumber == 13) {
                        Intent activity = new Intent(MainActivity.this, MultiplayerEnd.class);
                        activity.putExtra("level", levelNumber);
                        activity.putExtra("score1", score1);
                        activity.putExtra("score2", score2);
                        startActivity(activity);
                    }

                    else {
                        Intent activity = new Intent(MainActivity.this, LvlWon.class);
                        activity.putExtra("level", levelNumber);
                        activity.putExtra("score", score1);
                        startActivity(activity);
                    }

                    finish();
                }

                gameTimer--;
            }

            //This method makes sure that onDraw is called every frame.
            invalidate();

        }

        @Override
        public boolean onTouchEvent(MotionEvent e){
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                xTouch = (int) e.getX();
                yTouch = (int) e.getY();
                justTouched = true;
            }
            return true;
        }

        //Check collision is used when a new shape is spawned and 1 or more shapes already exist.
        //This code makes sure the new shape doesn't overlap an existing one.
        public void checkCollision() {
            //Make sure to check all the shapes on the screen for collisions.
            for (int i = 0; i < shapes.size(); i++) {
                //Get the coordinates and size of the current shape in the list.
                xPos = shapes.get(i).getXPos();
                yPos = shapes.get(i).getYPos();
                shapeSize = shapes.get(i).getShapeSize();

                //If the new random coordinates fall inside or near the existing one,
                //Create a new set of random coordinates and repeat the process until an empty location has been found.
                if (!(((newX + size) < (xPos - shapeSize - (5 * density))) ||
                        ((newX - size) > (xPos + shapeSize + (5 * density))) ||
                        ((newY + size) < (yPos - shapeSize - (5 * density))) ||
                        ((newY - size) > (yPos + shapeSize + (5 * density))))) {
                    i = -1; //Set i to -1 in order to reset the for loop and try again with the new coordinates.

                    if (levelNumber == 13) {
                        player = rand.nextInt(3) + 1;
                        if (player == 1 || gameTimer > 1800) {
                            newX = (int) (rand.nextInt(getWidth() - Math.round(120 * density)) + (60 * density));
                            newY = (int) (rand.nextInt(getHeight() - Math.round(440 * density)) + (380 * density));
                        }
                        else if (player == 2) {
                            newX = (int) (rand.nextInt(getWidth() - Math.round(120 * density)) + (60 * density));
                            newY = (int) (rand.nextInt(getHeight() - Math.round(440 * density)) + (60 * density));
                        }
                    }

                    else {
                        newX = (int) (rand.nextInt(getWidth() - Math.round(120 * density)) + (60 * density));
                        newY = (int) (rand.nextInt(getHeight() - Math.round(210 * density)) + (150 * density));
                    }
                }
            }
        }

        /****** Code for drawing a circle ********/
        public void drawCircle(Canvas canvas) {
            //Draw the shape itself.
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor(circleColor));
            if (player == 2) {
                paint.setColor(Color.parseColor(circleColor2));
            }
            canvas.drawCircle(xPos, yPos, shapeSize, paint);

            //Draw the border around it.
            paint.setColor(Color.parseColor(circleBorderColor));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5 * density);
            canvas.drawCircle(xPos, yPos, shapeSize, paint);
        }

        /****** Code for drawing a fading circle ********/
        public void drawOldCircle(Canvas canvas) {
            //Draw the border around it.
            paint.setColor(Color.parseColor(circleBorderColor));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3 * density);
            if (shapeSize * 0.8f < 100) {
                paint.setAlpha(100 - (int) (shapeSize * 0.8f));
            }
            else {
                paint.setAlpha(0);
            }
            canvas.drawCircle(xPos, yPos, shapeSize, paint);
            paint.setAlpha(100);
        }

        /****** Code for drawing a rectangle ********/
        public void drawRectangle(Canvas canvas) {
            //Draw the shape itself.
            canvas.rotate(rotation, xPos, yPos);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor(rectangleColor));
            if (player == 2) {
                paint.setColor(Color.parseColor(rectangleColor2));
            }
            canvas.drawRect(xPos - shapeSize, yPos - shapeSize, xPos + shapeSize, yPos + shapeSize, paint);

            //Draw the border around it.
            paint.setColor(Color.parseColor(rectangleBorderColor));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5 * density);
            canvas.drawRect(xPos - shapeSize, yPos - shapeSize, xPos + shapeSize, yPos + shapeSize, paint);
            canvas.rotate(-rotation, xPos, yPos);
        }

        /****** Code for drawing a fading rectangle ********/
        public void drawOldRectangle(Canvas canvas) {
            //Draw the border around it.
            canvas.rotate(rotation, xPos, yPos);
            paint.setColor(Color.parseColor(rectangleBorderColor));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3 * density);
            if (shapeSize * 0.8f < 100) {
                paint.setAlpha(100 - (int) (shapeSize * 0.8f));
            }
            else {
                paint.setAlpha(0);
            }
            canvas.drawRect(xPos - shapeSize, yPos - shapeSize, xPos + shapeSize, yPos + shapeSize, paint);
            canvas.rotate(-rotation, xPos, yPos);
            paint.setAlpha(100);
        }

        /****** Code for drawing a hexagon ********/
        public void drawHexagon(Canvas canvas) {
            //Created a path of lines to resemble the shape of a hexagon as there is no native method to do so.
            canvas.rotate(rotation, xPos, yPos);
            Path path = new Path();
            path.moveTo(xPos - (shapeSize / 2), yPos - ((float) sin(1.04719755) * shapeSize));
            path.lineTo(xPos + (shapeSize / 2), yPos - ((float) sin(1.04719755) * shapeSize));
            path.lineTo(xPos + shapeSize, yPos);
            path.lineTo(xPos + (shapeSize / 2), yPos + ((float) sin(1.04719755) * shapeSize));
            path.lineTo(xPos - (shapeSize / 2), yPos + ((float) sin(1.04719755) * shapeSize));
            path.lineTo(xPos - shapeSize, yPos);
            path.lineTo(xPos - (shapeSize / 2), yPos - ((float) sin(1.04719755) * shapeSize));
            path.close();

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor(hexagonColor));
            if (player == 2) {
                paint.setColor(Color.parseColor(hexagonColor2));
            }
            canvas.drawPath(path, paint);

            //Draw the border using this same path.
            paint.setColor(Color.parseColor(hexagonBorderColor));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5 * density);
            canvas.drawPath(path, paint);
            canvas.rotate(-rotation, xPos, yPos);
        }

        /****** Code for drawing a fading hexagon ********/
        public void drawOldHexagon(Canvas canvas) {
            //Created a path of lines to resemble the shape of a hexagon as there is no native method to do so.
            canvas.rotate(rotation, xPos, yPos);
            Path path = new Path();
            path.moveTo(xPos - (shapeSize / 2), yPos - ((float) sin(1.04719755) * shapeSize));
            path.lineTo(xPos + (shapeSize / 2), yPos - ((float) sin(1.04719755) * shapeSize));
            path.lineTo(xPos + shapeSize, yPos);
            path.lineTo(xPos + (shapeSize / 2), yPos + ((float) sin(1.04719755) * shapeSize));
            path.lineTo(xPos - (shapeSize / 2), yPos + ((float) sin(1.04719755) * shapeSize));
            path.lineTo(xPos - shapeSize, yPos);
            path.lineTo(xPos - (shapeSize / 2), yPos - ((float) sin(1.04719755) * shapeSize));
            path.close();

            //Draw the border using this same path.
            paint.setColor(Color.parseColor(hexagonBorderColor));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3 * density);
            if (shapeSize * 0.8f < 100) {
                paint.setAlpha(100 - (int) (shapeSize * 0.8f));
            } else {
                paint.setAlpha(0);
            }
            canvas.drawPath(path, paint);
            canvas.rotate(-rotation, xPos, yPos);
            paint.setAlpha(100);
        }

        //Class used for the 3 different shapes.
        public class Shape {
            private int xPos;
            private int yPos;
            private float shapeSize;
            private float rotation;
            private String type;
            private int timeAlive;
            private int player;


            public Shape(int xPos, int yPos, float shapeSize, String type, float rotation, int timeAlive) {
                this.xPos = xPos;
                this.yPos = yPos;
                this.shapeSize = shapeSize;
                this.rotation = rotation;
                this.type = type;
                this.timeAlive = timeAlive;
            }

            public Shape(int xPos, int yPos, float shapeSize, String type, float rotation, int timeAlive, int player) {
                this.xPos = xPos;
                this.yPos = yPos;
                this.shapeSize = shapeSize;
                this.rotation = rotation;
                this.type = type;
                this.timeAlive = timeAlive;
                this.player = player;
            }

            public int getXPos() {
                return xPos;
            }

            public int getYPos() {
                return yPos;
            }

            public float getShapeSize() {
                return shapeSize;
            }

            public String getType() {
                return type;
            }

            public void setXPos(int xPos) {
                this.xPos = xPos;
            }

            public void setYPos(int yPos) {
                this.yPos = yPos;
            }

            public void setShapeSize(float shapeSize) {
                this.shapeSize = shapeSize;
            }

            public void setType(String type) {
                this.type = type;
            }

            public float getRotation() {
                return rotation;
            }

            public void setRotation(float rotation) {
                this.rotation = rotation;
            }

            public int getTimeAlive() {
                return timeAlive;
            }

            public void setTimeAlive(int timeAlive) {
                this.timeAlive = timeAlive;
            }

            public int getPlayer() {
                return player;
            }

            public void setPlayer(int player) {
                this.player = player;
            }
        }
    }

    public static MainActivity getInstance() {
        return mainActivity;
    }

    //disables the default android backbutton
    @Override
    public void onBackPressed() {
        if (musicClass.musicOn == true) {
            musicClass.mpPlayer.pause();
            musicClass.playing = false;
            musicClass.length = musicClass.mpPlayer.getCurrentPosition();
        }

        transition = true;
        Intent activity = new Intent(MainActivity.this, Pause.class);
        activity.putExtra("level", levelNumber);
        startActivity(activity);
    }

    public String loadJSONFromAsset() throws IOException {
        String json;
        try {
            InputStream is = getAssets().open("leveldata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }

        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private int musicSwitch(int i) {
        switch (i) {
            case 1:  music = R.raw.kickshock;
                break;
            case 2:  music = R.raw.bitdungeonboss;
                break;
            case 3:  music = R.raw.kickshock;
                break;
            case 4:  music = R.raw.bitdungeonboss;
                break;
            case 5:  music = R.raw.kickshock;
                break;
            default: music = R.raw.bitdungeonboss;;
                break;
        }
        return music;
    }
}
