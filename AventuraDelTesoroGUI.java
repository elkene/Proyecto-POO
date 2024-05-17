import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public AventuraDelTesoroGUI() {
        cargarJugadores();

        setTitle("Aventura del Tesoro");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        // Crear el menú principal
        panelMenu = new JPanel();
        panelMenu.setLayout(new GridBagLayout());
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
        JPanel panelMapa = new JPanel();
        panelMapa.setBackground(Color.WHITE);
        panelJuego.add(panelMapa, BorderLayout.CENTER);

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
        int tiempoInicial = 60; // Tiempo inicial en segundos
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

        // Escuchar eventos del teclado
        panelJuego.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        lblArriba.setForeground(Color.GREEN);
                        break;
                    case KeyEvent.VK_DOWN:
                        lblAbajo.setForeground(Color.GREEN);
                        break;
                    case KeyEvent.VK_LEFT:
                        lblIzquierda.setForeground(Color.GREEN);
                        break;
                    case KeyEvent.VK_RIGHT:
                        lblDerecha.setForeground(Color.GREEN);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        lblArriba.setForeground(Color.BLACK);
                        break;
                    case KeyEvent.VK_DOWN:
                        lblAbajo.setForeground(Color.BLACK);
                        break;
                    case KeyEvent.VK_LEFT:
                        lblIzquierda.setForeground(Color.BLACK);
                        break;
                    case KeyEvent.VK_RIGHT:
                        lblDerecha.setForeground(Color.BLACK);
                        break;
                }
            }
        });

        panelJuego.setFocusable(true);

        // Añadir paneles al CardLayout
        panelPrincipal.add(panelMenu, "Menu");
        panelPrincipal.add(panelJuego, "Juego");

        add(panelPrincipal);

        setFocusable(true);
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
            JLabel background = new JLabel(new ImageIcon("C:\\Users\\Kenne\\Documents\\POO\\Proyecto-POO-master\\fondoo.gif"));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AventuraDelTesoroGUI juego = new AventuraDelTesoroGUI();
            juego.setVisible(true);
        });
    }
}
