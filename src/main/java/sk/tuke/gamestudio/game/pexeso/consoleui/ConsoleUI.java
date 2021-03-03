package sk.tuke.gamestudio.game.pexeso.consoleui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.pexeso.core.Field;
import sk.tuke.gamestudio.game.pexeso.core.GameState;
import sk.tuke.gamestudio.game.pexeso.core.Tile;
import sk.tuke.gamestudio.service.*;

public final class ConsoleUI {
    private static final String GAME_NAME = "pexeso";
    private static final Pattern INPUT_PATTERN = Pattern.compile ( "([O])([A-I])([1-9])" );
    private Field field;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;


   /* private ScoreService scoreService = new ScoreServiceJDBC ();
    private CommentService commentService = new CommentServiceJDBC ();
    private RatingService ratingService = new RatingServiceJDBC ();*/

    private final BufferedReader br = new BufferedReader ( new InputStreamReader ( System.in ) );


    public ConsoleUI ( Field field ) {
        this.field = field;
    }

    public void play () throws IOException, CommentException {
        System.out.println ( "Welcome to Pexeso game!" );
        plays ();
        menu ();
    }


    public void plays () throws IOException, CommentException {
        do {
            this.printField ();
            this.processInput ();
        } while (this.field.getState () == GameState.PLAYING);
        this.printField ();
        if (this.field.getState () == GameState.SOLVED) {
            System.out.println ( "You won!!!" );
            endGame ();
            //mainMenu ();
            //*printNickName ();
            //  printScores ();
            // comment ();
            //printComments ();
        } else {
            System.out.println ( "You failed!!!" );
            //   printNickName ();
            //  printScores ();
            //  comment ();
            //   printComments ();
        }
    }


    public RatingService getRatingService () {
        return ratingService;
    }

    public void setRatingService ( RatingService ratingService ) {
        this.ratingService = ratingService;
    }

    public CommentService getCommentService () {
        return commentService;
    }

    public void setCommentService ( CommentService commentService ) {
        this.commentService = commentService;
    }

    public ScoreService getScoreService () {
        return scoreService;
    }

    public void setScoreService ( ScoreService scoreService ) {
        this.scoreService = scoreService;
    }

    private void printField () {
        this.printFieldHeader ();
        this.printFieldBody ();
    }

    private void printFieldHeader () {
        System.out.print ( " " );

        for (int column = 0; column < this.field.getColumnCount (); ++column) {
            System.out.print ( " " );
            System.out.print ( column + 1 );
        }

        System.out.println ();
    }

    private void printFieldBody () {
        for (int row = 0; row < this.field.getRowCount (); ++row) {
            System.out.print ( (char) (65 + row) );

            for (int column = 0; column < this.field.getColumnCount (); ++column) {
                System.out.print ( " " );
                this.printTile ( row , column );
            }

            System.out.println ();
        }

    }

    private void printTile ( int row , int column ) {
        Tile tile = this.field.getTile ( row , column );
        switch (tile.getState ()) {
            case CLOSED:
                System.out.print ( "-" );
                break;
            case OPEN:
            case FIXED:
                System.out.print ( tile.getValue () );
                break;
            default:
                throw new IllegalArgumentException ( "Unsupported tile state " + tile.getState () );
        }

    }

    protected void processInput () {
        while (true) {
            System.out.print ( "Enter input (e.g. MA0, OB3, X): " );
            String input = (new Scanner ( System.in )).nextLine ().trim ().toUpperCase ();
            if ("X".equals ( input )) {
                System.exit ( 0 );
            }

            Matcher matcher = INPUT_PATTERN.matcher ( input );
            if (matcher.matches ()) {
                try {
                    int row = matcher.group ( 2 ).charAt ( 0 ) - 65;
                    int column = Integer.parseInt ( matcher.group ( 3 ) ) - 1;
                    if (row >= 0 && row < this.field.getRowCount () && column >= 0 && column < this.field.getColumnCount () && "O".equals ( matcher.group ( 1 ) )) {
                        this.field.openTile ( row , column );
                        return;
                    }
                } catch (NumberFormatException var5) {
                }
            }
        }
    }

   /* private void printScores() {
        List<Score> scores = this.scoreService.getBestScores("pexeso");
        System.out.println ("------------------------");
        System.out.println ("Top Scores: " + Collections.max (scores).getPlayer () + " | " + Collections.max (scores).getPoints ());
        System.out.println ("------------------------");
        Iterator var2 = scores.iterator();

        while(var2.hasNext ()) {
            System.out.println ("------------------");
            Score s = (Score)var2.next ();
            System.out.println(s.getPlayer () + " | " + s.getPoints ());
            System.out.println ("------------------");
        }

    }
    *//*private void printNickName(){

        System.out.print("Enter your nickname: ");
        String playerName = (new Scanner(System.in)).nextLine().trim();
        if (playerName.length() < 4 || playerName.contains(" ")) {
            System.out.println(
                    "Nickname should have 4 character at least and cannot contain space characters!\n");
            printNickName ();
        } else {
            scoreService.addScore(new Score (  "pexeso", playerName, field.getScore (), new java.util.Date()));
        }
    }*//*

    private void printComments() throws CommentException {
        List<Comment> comments = this.commentService.getComments("pexeso");
        System.out.println (comments);
        System.out.println ("------------------------");
       // System.out.println ("Top Scores: " + Collections.max (scores).getPlayer () + " | " + Collections.max (scores).getPoints ());
       // System.out.println ("------------------------");
//        Iterator var2 = comments.iterator();
//
//        while(var2.hasNext ()) {
//            System.out.println ("------------------");
//
//            Comment s = (Comment) var2.next ();
//            System.out.println(s.getPlayer () + " | " + s.getComment ());
//            System.out.println ("------------------");
//        }
    }*/


    private void comment () throws IOException {
        System.out.print ( "Enter your nickname: " );
        String playerName = (new Scanner ( System.in )).nextLine ().trim ();
        System.out.println ( "Comment: " );
        String comment = br.readLine ();
        //submit comment goes here
        try {
            commentService
                    .addComment ( new Comment ( "pexeso" , playerName , comment , new java.util.Date () ) );
        } catch (CommentException e) {
            System.out.println ( "error caused by comment service" );
        }
    }


    private void endGame () throws IOException, CommentException {
        //DONE  Store scores process comes here
        //DONE  addScore method call comes here
        int finalScore = field.getScore ();

        System.out.println (
                "The game's end\nYour final score is: " + finalScore + "\nDo you want to submit? [Y/N]" );

        String usrOption = br.readLine ();
        if (usrOption.matches ( "[Yy]|([Yy]es)" )) {
            //plays();
            submit (finalScore);
            // wantToTryAgain();
        } else if (usrOption.matches ( "[Nn]|([Nn]o)" )) {
            // wantToTryAgain();
            menu ();
        } else {
            System.out.println ( "Wrong input, try again!" );
            endGame ();
        }

    }

   /* private void wantToTryAgain () throws IOException, CommentException {
        String usrOption;
        System.out.println ( "Wanna try again? [Y/N]" );
        usrOption = br.readLine ();
        if (usrOption.matches ( "[Yy]|([Yy]es)" )) {

            plays ();
        } else if (usrOption.matches ( "[Nn]|([Nn]o)" )) {
            menu ();
            return;
        } else {
            System.out.println ( "Wrong input, try again!" );
            endGame ();
            return;
        }
    }*/

    private void submit (int score) throws IOException, CommentException {
        System.out.println ( "Please enter your nickname: " );
        String playerName = br.readLine ();

//		String nickname = (usrInput.length() > 3) ? usrInput : null;

        if (playerName.length () < 4) {
            System.out.println ( "Nickname should have 4 character at least!\n" );
            endGame ();
        } else {
            System.out.println ( "inside" );

            scoreService
                    .addScore ( new Score ( GAME_NAME , playerName , score , new java.util.Date () ) );
            System.out.println ( "Success!" );
        }

    }

    public void menu () throws IOException, CommentException {

        System.out.println ( "YOU'RE IN THE MAIN MENU. PLS, SELECT OPTION\n\n" );
        System.out.println ( "(R)ate Pexeso\n" );
        System.out.println ( "(H)all Of Fame\n" );
        System.out.println ( "(C)omments\n" );

        //System.out.println("(S)ettings - set appropriate parameters for game\n");
        System.out.println ( "(Q)uit - quit the game\n" );
        // System.out.println("(S)ubmit - submit the game\n");
        System.out.println ();
        System.out.println ();

        try {
            System.out
                    .println ( String.format ( "Players like this game like: " + "%d" , ratingService
                            .getAverageRating ( GAME_NAME ) ) );
        } catch (RatingException e) {
            System.out.println ( "getting average score issue" );
        }

        String usrOption = br.readLine ();

        if (usrOption.matches ( "[Rr]|([Rr]ate)" )) {
            rate ();
        } else if (usrOption.matches ( "[Hh]|([Hh]allOfFame)" )) {
            hallOfFame ();
        } else if (usrOption.matches ( "[Qq]|([Qq]uit)" )) {
            quit ();
        } else if (usrOption.matches ( "[Cc]| ([Cc]omments)" )) {
            comments ();
        } else {
            System.out.println ( "Wrong input, try again!" );
        }

        menu ();

    }


    private void rate () throws IOException {
//		game.getRatingService()
        System.out.println ( "player nick: " );

        String playerName = br.readLine ();

        if (playerName.length () < 4 || playerName.contains ( " " )) {
            System.out.println (
                    "Nickname should have 4 character at least and cannot contain space characters!\n" );
            rate ();
        } else {

            System.out.println ( "Rating (1 - 5): " );

            int rating;
            try {
                rating = Integer.parseInt ( br.readLine () );
            } catch (NumberFormatException e) {
                System.out.println ( "string you've entered should consist only of digits!" );
                rate ();
                return;
            }

            if (rating > 5 || rating < 1) {
                System.out.println ( "Wrong input" );
                rate ();
                return;
            }
            try {
                ratingService
                        .setRating ( new Rating ( GAME_NAME , playerName , rating , new java.util.Date () ) );
                System.out.println ( "rated\n" );
            } catch (RatingException e) {
                System.out.println ( "error caused by rating service" );
            }
        }
    }

    private void hallOfFame () throws IOException, CommentException {
        int i = 0;
        for (Score score : getScoreService ().getBestScores ( GAME_NAME )) {
            System.out.println ( ++i + ": " + score.getPlayer () + " with " + score.getPoints () );
        }

        System.out.println ( "\n\nEnter anything to go back" );
        br.readLine ();
        menu ();
    }

    private void comments () throws IOException, CommentException {
        int i = 0;
        for (Comment comment : getCommentService ().getComments ( GAME_NAME )) {
            System.out.println ( ++i + ": " + comment.getPlayer () + " think: " + comment.getComment () );
        }

        System.out.println ( "\n\nEnter anything to go back" );
        br.readLine ();
        menu ();
    }

    private void quit () throws IOException, CommentException {

        System.out.println ( "Do you really want to quit? [Y/N]]" );
        String usrOption = br.readLine ();
        if (usrOption.matches ( "[Yy]|([Yy]es)" )) {
            System.out.println ( "Leave comment? [Y/N]" );
            usrOption = br.readLine ();
            if (usrOption.matches ( "[Yy]|([Yy]es)" )) {
                comment ();
            } else if (usrOption.matches ( "[Nn]|([Nn]o)" )) {

                System.out.println ( "See you later! Tell your friends about me!" );
                try {
                    Thread.sleep ( 1500 );
                    System.exit ( 0 );
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            }
        } else if (usrOption.matches ( "[Nn]|([Nn]o)" )) {
            menu ();
        } else {
            System.out.println ( "Wrong input, try again!" );
            quit ();
        }
    }

}



