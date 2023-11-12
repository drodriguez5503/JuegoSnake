import javax.swing.*;
public class App{
    public static void main(String[] args) throws Exception{
        int anchoTablero = 900;
        int alturaTablero = anchoTablero; //Creamos las medidas del tablero

        JFrame pantalla = new JFrame("Snake"); //Creamos un objeto pantalla para poder jugar
        pantalla.setVisible(true);  //Hace visible la pantalla
        pantalla.setSize(anchoTablero,alturaTablero); //Declaramos que el tamaño de la pantalla
        pantalla.setLocationRelativeTo(null); // Colocamos la pantalla en el centro del monitor del ordenador
        pantalla.setResizable(false); //Hacemos que el usuario no pueda cambiar el tamaño de la pantalla
        pantalla.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); /*Declaramos que la operación de cierre es cuando
        el usuario cierre la ventana */

        JuegoSnake juego = new JuegoSnake(anchoTablero,alturaTablero); /*Creamos un objeto juego dentro
        de la clase JuegoSnake*/
        pantalla.add(juego);
        pantalla.pack();
        juego.requestFocus();
    }
}