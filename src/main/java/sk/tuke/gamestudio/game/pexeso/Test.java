package sk.tuke.gamestudio.game.pexeso;

import sk.tuke.gamestudio.game.pexeso.core.Field;
import sk.tuke.gamestudio.game.pexeso.core.Tile;

public class Test {

    public static void main ( String[] args ) {
        Field field = new Field ( 1 , 2 );
        field.openTile ( 0 , 0 );

        for (int row = 0; row < field.getRowCount (); ++row) {
            for (int column = 0; column < field.getColumnCount (); ++column) {
                Tile tile = field.getTile ( row , column );
                System.out.print ( " " );
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

            System.out.println ();
        }

    }
}
