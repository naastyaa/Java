package sk.tuke.gamestudio.game.pexeso;

import sk.tuke.gamestudio.game.pexeso.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.pexeso.core.Field;
import sk.tuke.gamestudio.service.CommentException;

import java.io.IOException;

public class Main {
    public static void main ( String[] args ) throws IOException, CommentException {
        Field field = new Field ( 1 , 4 );
        ConsoleUI ui = new ConsoleUI ( field );
        ui.play ();
    }
}
