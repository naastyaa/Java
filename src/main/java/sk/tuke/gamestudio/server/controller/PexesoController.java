package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.pexeso.core.Field;
import sk.tuke.gamestudio.game.pexeso.core.GameState;
import sk.tuke.gamestudio.game.pexeso.core.Tile;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/pexeso")
public class PexesoController {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserController userController;

    private Field field;


    @RequestMapping
    public String pexeso ( String row , String column , Model model ) throws RatingException {
        if (field == null)
            newGame ();
        try {
            if (field.getState () == GameState.PLAYING) {
                field.openTile ( Integer.parseInt ( row ) , Integer.parseInt ( column ) );
                if (userController.isLogged () && field.getState () == GameState.SOLVED) {
                    scoreService.addScore ( new Score ( "pexeso" , userController.getLoggedUser () , field.getScore () , new Date () ) );
                }
            }

        } catch (NumberFormatException e) {
            //Jaro: Zle poslane nic sa nedeje
            e.printStackTrace ();
        }
        prepareModel ( model );
        return "pexeso";
    }

    @RequestMapping("/new")
    public String newGame ( Model model ) throws RatingException {
        newGame ();
        prepareModel ( model );
        return "pexeso";
    }

    @RequestMapping("/comment")
    public String addComment(String comment, Model model) throws RatingException {
        commentService.addComment(new Comment("pexeso", userController.getLoggedUser () ,comment,  new Date()));
        prepareModel ( model );
        return "pexeso";
    }


   @RequestMapping("/rating")
    public String addRating(String rating, Model model) throws RatingException {
        ratingService.setRating(new Rating("pexeso", userController.getLoggedUser (), Integer.parseInt(rating), new Date()));
        prepareModel ( model );
        return "pexeso";
    }

    public boolean getGameWon(){
        if(field.getState()==GameState.SOLVED) return true;
        else return false;
    }
    public GameState getGameState () {
        return field.getState ();
    }

    //Tento pristup sice nie je idealny, ale pre zaciatok je najjednoduchsi
    public String getHtmlField () {
        StringBuilder sb = new StringBuilder ();
        sb.append ( "<table class='field'>\n" );

        for (int row = 0; row < field.getRowCount (); row++) {
            sb.append ( "<tr>\n" );
            for (int column = 0; column < field.getColumnCount (); ++column) {
                Tile tile = field.getTile ( row , column );
                sb.append ( "<td>\n" );

                if (field.equals ( this.field ))
                    sb.append ( "<a href='" +
                            String.format ( "/pexeso?row=%s&column=%s" , row , column )
                            + "'>\n" );
                sb.append ( "<img src='/images/pexeso/" + getImageName ( tile ) + ".png'>" );
                if (field.equals ( this.field ))
                    sb.append ( "</a>\n" );
                sb.append ( "</td>\n" );
            }
            sb.append ( "</tr>\n" );
        }
        sb.append ( "</table>\n" );

     /*   double avg = 0;

        try {
            avg = ratingService.getAverageRating ( "pexeso" );
        } catch (RatingException e) {
            e.printStackTrace ();
        } catch (NullPointerException e) {
            e.printStackTrace ();
        }
        sb.append ( "Priemerna rating hry je: " + String.valueOf ( avg ) );*/

        return sb.toString ();

    }


    private String getImageName ( Tile tile ) {
        String result;
        switch (tile.getState ()) {
            case CLOSED:
                result = "zak";
                break;
            case OPEN:
            case FIXED:
                int letter = tile.getValue ();
                letter -= 'a';
                result = "open" + letter;
                break;
            default:
                throw new IllegalArgumentException ( "State is not supported " + tile.getState () );
        }
        return result;
    }

    private void prepareModel ( Model model ) throws RatingException {
        model.addAttribute ( "scores" , scoreService.getBestScores ( "pexeso" ) );
        model.addAttribute ( "comments" , commentService.getComments ( "pexeso" ) );
        model.addAttribute("rating", ratingService.getAverageRating("pexeso"));
    }

    private void newGame () {

        field = new Field ( 2 , 4 );
    }
}
