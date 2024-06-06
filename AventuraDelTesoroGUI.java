import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;


public class AventuraDelTesoroGUI extends JFrame {
    private List<String> jugadores = new ArrayList<>();
    private String nombreJugadorSeleccionado;
    private String archivoJugadores = "jugadores.txt";
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    private JPanel panelMenu;
    private JPanel panelJuego;
    private JLabel lblTemporizador;
    private Timer timer;
    private JLabel lblArriba, lblAbajo, lblIzquierda, lblDerecha;
    private Laberinto laberinto;
    private int playerX, playerY;
    private final int cellSize = 40; // Tamaño de cada celda en el laberinto
    private final int numRows = 10;  // Número de filas en el laberinto
    private final int numCols = 10;  // Número de columnas en el laberinto
    private boolean[] movement = new boolean[4]; // Up, Down, Left, Right

    public AventuraDelTesoroGUI() {
        cargarJugadores();
    
        setTitle("Aventura del Tesoro");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
    
        // Crear el menú principal con GIF de fondo
        panelMenu = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon fondo = new ImageIcon("fondo3.gif");
                g.drawImage(fondo.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
    
        // Estilo personalizado para los botones
        Font botonFont = new Font("Arial", Font.PLAIN, 20); // Cambiar el tamaño de la fuente de los botones
        Color colorBoton = new Color(255, 255, 255); // Color blanco para el fondo de los botones
        Color colorTexto = new Color(0, 0, 0); // Color negro para el texto de los botones
    
        // Botón para iniciar el juego
        JButton btnIniciar = new JButton("Iniciar juego");
        btnIniciar.setFont(botonFont);
        btnIniciar.setBackground(colorBoton);
        btnIniciar.setForeground(colorTexto);
        btnIniciar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnIniciar.setPreferredSize(new Dimension(200, 50));
        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jugadores.isEmpty()) {
                    JOptionPane.showMessageDialog(AventuraDelTesoroGUI.this, "No hay jugadores creados. Crea un nuevo jugador.");
                } else {
                    SeleccionJugadorFrame seleccionJugadorFrame = new SeleccionJugadorFrame();
                    seleccionJugadorFrame.setVisible(true);
                }
            }
        });
        panelMenu.add(btnIniciar, gbc);
    
        // Botón para ver el scoreboard
        JButton btnScoreboard = new JButton("Ver scoreboard");
        btnScoreboard.setFont(botonFont);
        btnScoreboard.setBackground(colorBoton);
        btnScoreboard.setForeground(colorTexto);
        btnScoreboard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnScoreboard.setPreferredSize(new Dimension(200, 50));
        btnScoreboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(AventuraDelTesoroGUI.this, "Mostrando scoreboard...");
            }
        });
        gbc.gridy = 1;
        panelMenu.add(btnScoreboard, gbc);
    
        // Botón para crear nuevo jugador
        JButton btnNuevoJugador = new JButton("Crear nuevo jugador");
        btnNuevoJugador.setFont(botonFont);
        btnNuevoJugador.setBackground(colorBoton);
        btnNuevoJugador.setForeground(colorTexto);
        btnNuevoJugador.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnNuevoJugador.setPreferredSize(new Dimension(200, 50));
        btnNuevoJugador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreJugador = JOptionPane.showInputDialog(AventuraDelTesoroGUI.this, "Introduce tu nombre:");
                if (nombreJugador != null && !nombreJugador.isEmpty()) {
                    jugadores.add(nombreJugador);
                    JOptionPane.showMessageDialog(AventuraDelTesoroGUI.this, "Jugador creado: " + nombreJugador);
                    guardarJugadores();
                } else {
                    JOptionPane.showMessageDialog(AventuraDelTesoroGUI.this, "Nombre de jugador inválido. Inténtalo de nuevo.");
                }
            }
        });
        gbc.gridy = 2;
        panelMenu.add(btnNuevoJugador, gbc);
    
        // Botón para salir del juego
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(botonFont);
        btnSalir.setBackground(colorBoton);
        btnSalir.setForeground(colorTexto);
        btnSalir.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnSalir.setPreferredSize(new Dimension(200, 50));
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarJugadores();
                System.exit(0);
            }
        });
        gbc.gridy = 3;
        panelMenu.add(btnSalir, gbc);
    
        // Crear el panel del juego
        panelJuego = new JPanel(new BorderLayout());
    
        // Panel para mostrar el mapa
        laberinto = new Laberinto(numRows, numCols, cellSize);
        panelJuego.add(laberinto, BorderLayout.CENTER); // Centrar el laberinto en el panel
    
        // Temporizador
        lblTemporizador = new JLabel("Tiempo restante: 60 segundos");
        panelJuego.add(lblTemporizador, BorderLayout.NORTH);
    
        // Indicadores de movimiento
        lblArriba = new JLabel("↑");
        lblAbajo = new JLabel("↓");
        lblIzquierda = new JLabel("←");
        lblDerecha = new JLabel("→");
    
        Font flechaFont = new Font("Arial", Font.PLAIN, 40); // Cambiar el tamaño de la fuente
        lblArriba.setFont(flechaFont);
        lblAbajo.setFont(flechaFont);
        lblIzquierda.setFont(flechaFont);
        lblDerecha.setFont(flechaFont);
    
        // Centrar las etiquetas de flechas horizontalmente
        lblArriba.setHorizontalAlignment(SwingConstants.CENTER);
        lblAbajo.setHorizontalAlignment(SwingConstants.CENTER);
        lblIzquierda.setHorizontalAlignment(SwingConstants.CENTER);
        lblDerecha.setHorizontalAlignment(SwingConstants.CENTER);
    
        JPanel panelIndicadores = new JPanel();
        panelIndicadores.setLayout(new GridLayout(3, 3));
        panelIndicadores.add(new JPanel()); // Celda vacía
        panelIndicadores.add(lblArriba);
        panelIndicadores.add(new JPanel()); // Celda vacía
        panelIndicadores.add(lblIzquierda);
        panelIndicadores.add(new JPanel()); // Celda vacía
        panelIndicadores.add(lblDerecha);
        panelIndicadores.add(new JPanel()); // Celda vacía
        panelIndicadores.add(lblAbajo);
        panelIndicadores.add(new JPanel()); // Celda vacía
    
        panelJuego.add(panelIndicadores, BorderLayout.SOUTH);
    
        // Configuración del temporizador
        int tiempoInicial = 300; // Tiempo inicial en segundos (5 minutos)
        timer = new Timer(1000, new ActionListener() {
        int tiempoRestante = tiempoInicial;

        @Override
        public void actionPerformed(ActionEvent e) {
            tiempoRestante--;
            if (tiempoRestante >= 0) {
                lblTemporizador.setText("Tiempo restante: " + tiempoRestante + " segundos");
            } else {
                ((Timer) e.getSource()).stop(); // Detener el temporizador
                JOptionPane.showMessageDialog(AventuraDelTesoroGUI.this, "¡Tiempo agotado!");
            }
        }
});
    
        // Escuchar eventos del teclado para movimiento continuo
        panelJuego.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        movement[0] = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        movement[1] = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        movement[2] = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        movement[3] = true;
                        break;
                }
            }
    
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        movement[0] = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        movement[1] = false;
                        break;
                    case KeyEvent.VK_LEFT:
                        movement[2] = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        movement[3] = false;
                        break;
                }
            }
        });
    
        // Temporizador para mover al jugador
        Timer movimientoTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moverJugador();
            }
        });
        movimientoTimer.start();
    
        panelJuego.setFocusable(true);
    
        // Añadir paneles al CardLayout
        panelPrincipal.add(panelMenu, "Menu");
        panelPrincipal.add(panelJuego, "Juego");
    
        add(panelPrincipal);
    
        setFocusable(true);
    
        // Inicializar posición del jugador
        playerX = 0;
        playerY = 0;
    }
    
    
    private void cargarJugadores() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoJugadores))) {
            String jugador;
            while ((jugador = br.readLine()) != null) {
                jugadores.add(jugador);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar jugadores: " + e.getMessage());
        }
    }

    private void guardarJugadores() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoJugadores))) {
            for (String jugador : jugadores) {
                bw.write(jugador);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar jugadores: " + e.getMessage());
        }
    }

    public class SeleccionJugadorFrame extends JFrame {
        public SeleccionJugadorFrame() {
            setTitle("Seleccionar Jugador");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Crear un JLabel para el GIF de fondo
            JLabel background = new JLabel(new ImageIcon("fondoo.gif"));
            setContentPane(background);
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            // Lista de jugadores
            JComboBox<String> jugadorComboBox = new JComboBox<>();
            for (String jugador : jugadores) {
                jugadorComboBox.addItem(jugador);
            }
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(jugadorComboBox, gbc);

            // Botón para seleccionar jugador
            JButton btnSeleccionar = new JButton("Seleccionar");
            btnSeleccionar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nombreJugadorSeleccionado = (String) jugadorComboBox.getSelectedItem();
                    JOptionPane.showMessageDialog(SeleccionJugadorFrame.this, "Seleccionaste a " + nombreJugadorSeleccionado);
                    iniciarJuego();
                }
            });
            gbc.gridy = 1;
            add(btnSeleccionar, gbc);

            // Botón para iniciar el juego
            JButton btnIniciarJuego = new JButton("Iniciar juego");
            btnIniciarJuego.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    iniciarJuego();
                }
            });
            gbc.gridy = 2;
            add(btnIniciarJuego, gbc);

            setFocusable(true);
        }

        private void iniciarJuego() {
            // Aquí puedes agregar la lógica para iniciar el juego con el jugador seleccionado
            JOptionPane.showMessageDialog(SeleccionJugadorFrame.this, "Iniciando juego para " + nombreJugadorSeleccionado);
            cardLayout.show(panelPrincipal, "Juego");
            timer.start();
            panelJuego.requestFocusInWindow();
            dispose(); // Cierra la ventana de selección de jugador
        }
    }

    // Clase para generar y pintar el laberinto
    class Laberinto extends JPanel {
        private int[][] grid;
        private int cellSize;
        private int rows, cols;
        private int initialRows, initialCols;
        private Point start, end;
        private int rowIncrement = 2; // Incremento de filas por nivel
        private int colIncrement = 2; // Incremento de columnas por nivel
    
        public Laberinto(int initialRows, int initialCols, int cellSize) {
            this.initialRows = initialRows;
            this.initialCols = initialCols;
            this.cellSize = cellSize;
            this.rows = initialRows;
            this.cols = initialCols;
            this.grid = new int[rows][cols];
            generarLaberinto();
        }
        
        private void generarLaberinto() {
            // Inicializar el laberinto con paredes
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    grid[i][j] = 1;
                }
            }
        
            // Crear el laberinto usando DFS
            Stack<Point> stack = new Stack<>();
            start = new Point(0, 0);
            stack.push(start);
        
            Random random = new Random();
        
            while (!stack.isEmpty()) {
                Point current = stack.pop();
                int x = current.x;
                int y = current.y;
        
                // Marcar el punto actual como camino
                grid[x][y] = 0;
        
                // Crear una lista de vecinos no visitados
                List<Point> neighbors = new ArrayList<>();
                if (x > 1 && grid[x - 2][y] == 1) neighbors.add(new Point(x - 2, y));
                if (x < rows - 2 && grid[x + 2][y] == 1) neighbors.add(new Point(x + 2, y));
                if (y > 1 && grid[x][y - 2] == 1) neighbors.add(new Point(x, y - 2));
                if (y < cols - 2 && grid[x][y + 2] == 1) neighbors.add(new Point(x, y + 2));
        
                // Mezclar los vecinos y agregar los caminos
                while (!neighbors.isEmpty()) {
                    int index = random.nextInt(neighbors.size());
                    Point neighbor = neighbors.remove(index);
                    int nx = neighbor.x;
                    int ny = neighbor.y;
        
                    if (grid[nx][ny] == 1) {
                        // Crear un camino entre el punto actual y el vecino
                        grid[nx][ny] = 0;
                        grid[(x + nx) / 2][(y + ny) / 2] = 0;
                        stack.push(current);
                        stack.push(neighbor);
                    }
                }
            }
        
            // Definir la posición final una celda arriba de la esquina inferior derecha
            end = new Point(rows - 2, cols - 1);
        
            // Asegurar que la posición final sea un camino
            grid[end.x][end.y] = 0;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (grid[i][j] == 1) {
                        g.setColor(Color.BLUE);
                        g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                }
            }
            // Dibujar el jugador
            g.setColor(Color.RED);
            g.fillRect(playerX, playerY, cellSize, cellSize);
    
            // Dibujar la posición final
            g.setColor(Color.GREEN);
            g.fillRect(end.y * cellSize, end.x * cellSize, cellSize, cellSize);
        }
    
        public boolean isWalkable(int x, int y) {
            int gridX = x / cellSize;
            int gridY = y / cellSize;
            return gridX >= 0 && gridX < cols && gridY >= 0 && gridY < rows && grid[gridY][gridX] == 0;
        }
    
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(cols * cellSize, rows * cellSize);
        }
    
        public void expand() {
            // Incrementar el número de filas y columnas
            rows += rowIncrement;
            cols += colIncrement;
    
            // Crear un nuevo laberinto con el nuevo tamaño
            grid = new int[rows][cols];
            generarLaberinto();
    
            // Actualizar la interfaz gráfica
            revalidate();
            repaint();
        }
    }
    private void moverJugador() {
        int step = 5; // Tamaño del paso para el movimiento continuo
    
        // Calcular las nuevas coordenadas del jugador
        int newX = playerX;
        int newY = playerY;
        if (movement[0]) { // Arriba
            newY -= step;
        }
        if (movement[1]) { // Abajo
            newY += step;
        }
        if (movement[2]) { // Izquierda
            newX -= step;
        }
        if (movement[3]) { // Derecha
            newX += step;
        }
    
        // Verificar si el nuevo destino del jugador estaría dentro de una pared del laberinto
        boolean collision = false;
        if (!laberinto.isWalkable(newX, newY) || !laberinto.isWalkable(newX + cellSize - 1, newY) ||
            !laberinto.isWalkable(newX, newY + cellSize - 1) || !laberinto.isWalkable(newX + cellSize - 1, newY + cellSize - 1)) {
            collision = true;
        }
    
        // Solo actualizar las coordenadas del jugador si no hay colisión
        if (!collision) {
            playerX = newX;
            playerY = newY;
        }
    
        laberinto.repaint();
    
        // Verificar si el jugador ha llegado al final
        if (playerX / cellSize == laberinto.end.y && playerY / cellSize == laberinto.end.x) {
            JOptionPane.showMessageDialog(AventuraDelTesoroGUI.this, "¡Felicidades! Has encontrado el tesoro.");
            
            // Generar un nuevo laberinto (mapa)
            panelJuego.remove(laberinto); // Remove the old maze from the panel
            laberinto = new Laberinto(numRows, numCols, cellSize); // Create a new maze
            panelJuego.add(laberinto, BorderLayout.CENTER); // Add the new maze to the panel
    
            // Transportar al jugador al nuevo mapa (posición inicial)
            playerX = 0;
            playerY = 0;
    
            // Actualizar la interfaz gráfica
            panelJuego.revalidate(); // Refresh the panel
            panelJuego.repaint(); // Repaint the panel
        }
    }
    
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AventuraDelTesoroGUI juego = new AventuraDelTesoroGUI();
            juego.setVisible(true);
        });
    }
}
