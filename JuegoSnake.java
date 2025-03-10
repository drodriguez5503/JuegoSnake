import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class JuegoSnake extends JPanel implements ActionListener, KeyListener {

    private static class Casilla{ //Creamos la clase Casilla para utilizar sistema de coordinadas x e y//

        int x;
        int y;

        Casilla(int x,int y){
            this.x=x;
            this.y=y;
        }
    }

    int anchoTablero;
    int alturaTablero;
    int tamCasilla = 25;

    Casilla cabezaSerpiente; //Creamos un objeto casilla para la cabeza de la serpiente
    ArrayList<Casilla> cuerpo; //Creamos arraylist para la longitud y las casillas incluidas en el cuerpo
    Casilla comida; //Creamos casilla para la comida
    ArrayList <Casilla> Nobstaculo; //Los obstáculos que salen según se come comida
    Casilla obstaculo;
    Random aleatorio;




    //Lógica del Juego: Las condiciones básicas del juego.
    Timer bucleJuego;
    int velocidadX; //El movimiento horizontal
    int velocidadY; //El movimiento vertical
    boolean gameOver = false; //Condición para finalizar el juego cuando devuelva valor true


    JuegoSnake(int anchoTablero, int alturaTablero){ //Método Constructor todos los objetos se declaran aquí
        this.anchoTablero = anchoTablero;
        this.alturaTablero = alturaTablero;
        setPreferredSize(new Dimension(this.anchoTablero,this.alturaTablero));//Establecemos dimensiones del tablero

        setBackground(Color.BLACK); //Cambiamos el color de fondo

        addKeyListener(this);
        setFocusable(true); //Hacemos que el programa pueda leer entradas de teclado

        cabezaSerpiente = new Casilla(10, 10); //Declaramos el objeto cabeza serpiente y donde empieza la serpiente
        cuerpo = new ArrayList<>(); //Creamos Arraylist para poder guardar las casillas del cuerpo de la serpiente

        Nobstaculo = new ArrayList<>();
        obstaculo = new Casilla(40,40);

        comida = new Casilla(15, 15); //Creamos una casilla para la comida
        aleatorio = new Random(); //Creamos el objeto que genera posiciones aleatorias
        colocarComida(); //colocamos la comida en una posición aleatoria con el método
        generarObstaculo();

        velocidadX = 0;
        velocidadY = 0; //Velocidades iniciales (Empiezan paradas)

        bucleJuego = new Timer(100, this); /*Creamos el objeto que implementa el actualizador del juego,
        se actualiza cada 100 milisegundos*/
        bucleJuego.start(); //Se inicia el juego
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        colorear(g);
    }

    public void colorear(Graphics g){ //Función para dibujar y colorear las casillas según se necesite
        //Serpiente
        g.setColor(Color.BLUE);
        g.fill3DRect(cabezaSerpiente.x* tamCasilla, cabezaSerpiente.y* tamCasilla,
        tamCasilla, tamCasilla,true); /*Color de la serpiente y coloreamos la posición de la
        cabeza de la serpiente del mismo tamaño que las casillas*/

        //Cuerpo
        for (Casilla parteCuerpo : cuerpo) {
            g.fill3DRect(parteCuerpo.x * tamCasilla, parteCuerpo.y * tamCasilla, tamCasilla, tamCasilla, true);
        }

        //Comida
        g.setColor(Color.GREEN);
        g.fill3DRect(comida.x*tamCasilla,comida.y*tamCasilla,tamCasilla,tamCasilla,true);
        //Coloreamos comida y añadimos bordes a la casilla con fill3DRect

        //Obstaculo
        for (Casilla obstaculo : Nobstaculo) {
            g.setColor(Color.RED);
            g.fill3DRect(obstaculo.x * tamCasilla, obstaculo.y * tamCasilla, tamCasilla, tamCasilla, true);
        }


        //Score
        if(!gameOver) {
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.GREEN);
            g.drawString("Puntuación: " + cuerpo.size(), tamCasilla - 16, tamCasilla);
        } else {
            g.setFont(new Font("Times New Roman", Font.PLAIN, 60));
            g.setColor(Color.red);
            g.drawString("Game over", 350, 450);
            g.setFont(new Font("Times New Roman", Font.PLAIN, 40));
            g.setColor(Color.GREEN);
            g.drawString("Puntuación: " + cuerpo.size(), 375, 500);

        }

         //Contamos la puntuación del usuario y mostramos mensaje de Game Over si muere
    }

    public void  generarObstaculo(){
        Casilla obstaculo = new Casilla(aleatorio.nextInt(anchoTablero / tamCasilla), aleatorio.nextInt(alturaTablero / tamCasilla));

        Nobstaculo.add(obstaculo);
    }

    public void colocarComida(){ //Método para colocar la comida en una coordenada x e y de forma aleatoria
        comida.x = aleatorio.nextInt(anchoTablero/tamCasilla);
        comida.y= aleatorio.nextInt(alturaTablero/tamCasilla);
    }


    public boolean colision(Casilla casilla1,Casilla casilla2){ //Verifica si dos casillas ocupan la misma posición
        return casilla1.x == casilla2.x && casilla1.y == casilla2.y;//Servirá para cuando nos choquemos o cuando comamos
    }



    public void move(){ //Todo lo que tiene que ver con el movimiento de la cabeza y su cuerpo

        //Comer comida
        if(colision(cabezaSerpiente,comida)){ //Si la cabeza de la serpiente colisiona con la comida
            cuerpo.add(new Casilla(comida.x, comida.y)); //Se agrega la casilla donde estaba al finar del arraylist
            colocarComida(); //Se llama al método colocar comida para que cree otra casilla con comida
            if(cuerpo.size()%3==0){
                generarObstaculo();
            }
        }

        //Cuerpo serpiente
        for(int i=cuerpo.size()-1;i>=0;i--){
            Casilla parteCuerpo = cuerpo.get(i);
            if (i == 0){ //Si es la primera vez que comemos lo añade de la cabeza
                parteCuerpo.x = cabezaSerpiente.x;
                parteCuerpo.y = cabezaSerpiente.y;
            } else { // Si no actualiza la posición de cada casilla a la casilla anterior
                Casilla parteCuerpoAnt = cuerpo.get(i-1);
                parteCuerpo.x = parteCuerpoAnt.x;
                parteCuerpo.y = parteCuerpoAnt.y;
            }

        }

        //Cabeza Serpiente
        cabezaSerpiente.x += velocidadX; //Se actualiza la posición de la cabeza para moverla
        cabezaSerpiente.y += velocidadY;

        for (Casilla parteCuerpo : cuerpo) { // Condiciones Game over
            if (colision(cabezaSerpiente, parteCuerpo)) { //Colisión con la cabeza
                gameOver = true;
            }
        }

        for (Casilla casilla : Nobstaculo) {
            if (colision(cabezaSerpiente, casilla)) {
                gameOver = true;
            }
        }

        if(cabezaSerpiente.x*tamCasilla < 0 || cabezaSerpiente.x*tamCasilla > anchoTablero ||
          cabezaSerpiente.y*tamCasilla > alturaTablero || cabezaSerpiente.y*tamCasilla < 0 ){
            //Colisión con cualquiera de las paredes
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) { //Bucle del juego
        move(); //Llama al metodo para moverse cuando empieza el contador
        repaint(); //Actualiza la pantalla
        if (gameOver) {
            bucleJuego.stop(); //Si hemos perdido se para el juego
        }
    }

    @Override
    public void keyPressed(KeyEvent e) { //Asignamos cada tecla de las flechas a una velocidad distinta, cada una siendo una dirección
        if (e.getKeyCode() == KeyEvent.VK_UP && velocidadY != 1){
            velocidadX = 0;
            velocidadY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocidadY != -1){
            velocidadX = 0;
            velocidadY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocidadX != 1){
            velocidadX = -1;
            velocidadY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocidadX != -1){
            velocidadX = 1;
            velocidadY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

}
