package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;


//GameView class is responsible for everything printed to screen during game. Including player and enemy movement.

public class GameView extends View {

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);
    }

    public static boolean isDead = false; // Is character dead or alive

    // Dimensions of the canvas
    private int canvasWidth;
    private int canvasHeight;

    //Bird animation, position and speed variables
    private Bitmap Bird[] = new Bitmap[2]; // Two images are used for animation, held in an array
    private int birdX;
    private int birdY;
    private int birdSpeed;

    // Blue Ball
    private int blueX;
    private int blueY;
    private int blueSpeed = 15;
    private Paint bluePaint = new Paint();

    // Black Ball
    private  int blackX;
    private  int blackY;
    private  double blackSpeed = 20;
    private  Paint blackPaint = new Paint();
    private Bitmap mine;


    //Background Image
    public static Bitmap bg;
    private static int bgSpeed = 30;
    private static int bgX;
    public static Canvas canvas;



    //Background Image 2
    public static Bitmap bg2;
    private int bgX2;

    // Score text
    private Paint paintScore = new Paint();
    private int Score;

    // Level text
    private  Paint paintLevel = new Paint();
    private int Level;


    //TextView score = (TextView) findViewById(R.id.score);

    //Life
    private Bitmap life[] = new Bitmap[2];
    private int lifeCount;

    // Status Update
    private boolean touchFlag = false;

    /** GameView Function assigns images to bitmap variables, and sets life and level count
     * @param context **/
    public GameView(Context context) {
        super(context);

        // Assigns correct images to Bitmap variables
        Bird[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bird1);
        Bird[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bird2);
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.background2);
        bg2 = BitmapFactory.decodeResource(getResources(), R.drawable.background2);




        bluePaint.setColor(Color.BLUE);
        bluePaint.setAntiAlias(false);

        blackPaint.setColor(Color.BLACK);
        blackPaint.setAntiAlias(false);


        // Create game text
        paintScore.setColor(Color.BLACK);
        paintScore.setTextSize(32);
        paintScore.setTypeface(Typeface.DEFAULT_BOLD);
        paintScore.setAntiAlias(true);


        paintLevel.setColor(Color.DKGRAY);
        paintLevel.setTextSize(32);
        paintLevel.setTypeface(Typeface.DEFAULT_BOLD);
        paintLevel.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_g);

        //Starting variables
        birdY = 500; // initial Y position
        Score = 0; // Initial score
        lifeCount = 3; // Starting lives
        Level = 1;



    }

    /** onDraw Function draws bitmaps to canvas, creates scrolling background, and handles movement,
     * speed, score value, and level number
     * @param canvas **/
    @Override
    protected  void onDraw(Canvas canvas)
    {
        canvasHeight = getHeight(); // Used to determine dimensions of phone screen
        canvasWidth = getWidth();




        // MAKING THE SCROLLING BACKGROUND

        // Canvas gets printed with continually decreasing x coordinates
        bgX -= 10;
        canvas.drawBitmap(bg,bgX,500,null);


        if (bgX < -1075) // if background Bitmap is off of screen
        {
            // print second Bitmap at same speed, in the same starting position
            bgX2 -=10;
            canvas.drawBitmap(bg2,bgX2,500,null);

            if(bgX2 < -1075){ // if second background Bitmap is off of the screen

                bgX = 0; // re-set x coordinates

                // Start printing first Bitmap again
                bgX -= 10;
                canvas.drawBitmap(bg,bgX,500,null);
            }

        }



        //Score
        canvas.drawText("Score: " + Score, 20, 60, paintScore);

        //Level
        if(Level < 5) { // if max level has not been reached, then print level number
            canvas.drawText("Level " + Level, (canvasWidth / 2) - 200, 60, paintLevel);
        } else {canvas.drawText("MAX LEVEL", (canvasWidth / 2) - 200, 60, paintLevel);} // if max level has been reached, then print "MAX LEVEL"

        //Update level variable and enemy ball speed appropriate to level
        if (Score > 49 && Score < 99) {Level = 2; blackSpeed = blackSpeed + 0.2;}
        if (Score > 99 && Score < 149) {Level = 3; blackSpeed = blackSpeed + 0.1;}
        if (Score > 149 && Score < 199) {Level = 4; blackSpeed = blackSpeed + 0.1;}
        if (Score > 199) {Level = 5; blackSpeed = blackSpeed + 0.1;} // Max level


        //Life
        for(int i = 0; i < 3; i++)
        {
            int x = (int) (560 + life[0].getWidth() * 1.5 * i);
            int y = 30;

            if(i < lifeCount)
            {
                canvas.drawBitmap(life[0], x,y,null);
            } else {
                canvas.drawBitmap(life[1], x,y,null);

            }

        }

        if (lifeCount == 0) {isDead = true;}


        // Sets max and min height variables for bird sprite
        int minBirdY = Bird[0].getHeight() + 350;
        int maxBirdY = (canvasHeight - Bird[0].getHeight() * 3) - 450;

        // Bird
        birdY += birdSpeed;
        if(birdY < minBirdY) birdY = minBirdY; // Prevents bird from going outside of bounds
        if(birdY > maxBirdY) birdY = maxBirdY;
        birdSpeed +=2;


        if(touchFlag)
        {
            //Flap wings
            canvas.drawBitmap(Bird[1],birdX,birdY,null);
            touchFlag = false;

        } else {
            canvas.drawBitmap(Bird[0], birdX, birdY, null);
        }

        // Blue Ball
        blueX -= blueSpeed;
        if(hitCheck(blueX,blueY))
        {
            Score += 10;
            blueX -= 100;
        }
        if (blueX < 0)
        {
            blueX += canvasWidth + 20;
            blueY = (int) Math.floor(Math.random() * (maxBirdY - minBirdY) + minBirdY);
        }
        canvas.drawCircle(blueX,blueY,10,bluePaint);

        // Black Ball
        blackX -= blackSpeed;
        if(hitCheck(blackX,blackY))
        {
            blackX = -100;
            lifeCount--;
            if(lifeCount == 0)
            {
                // Ends game
                Log.v("MESSAGE", "YOU DIED");

            }

        }
        if(blackX < 0)
        {
            blackX = canvasWidth +200;
            blackY = (int) Math.floor(Math.random() * (maxBirdY - minBirdY) + minBirdY);
        }

        
        canvas.drawCircle(blackX,blackY,15,blackPaint);



    }

    /** Checks to see if bird has collided with a blue or black ball
     * @param x
     * @param y **/
    public boolean hitCheck(int x, int y) // check if bird has collided with another object
    {
        if(birdX < x && x < (birdX + Bird[0].getWidth()) &&
                birdY < y && y < (birdY + Bird[0].getHeight())) {
            return true;
        }

        return  false;
    }

    /** Sets touch flag variable to true, when user touches screen. Allows us to code birds movement,
     * based on user interaction
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            touchFlag = true;
            birdSpeed = -20;
        }
        return true;
    }


}
