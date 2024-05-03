import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AventuraDelTesoroGUI extends JFrame {

    private List<String> jugadores = new ArrayList<>();
    private String nombreJugadorSeleccionado;

    public AventuraDelTesoroGUI() {
        setTitle("Aventura del Tesoro");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear un JLabel para el GIF de fondo
        JLabel background = new JLabel(new ImageIcon("C:\\POO\\Proyecto de POO\\fondoo.gif"));
        setContentPane(background);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Estilo personalizado para los botones
        Font botonFont=new Font("Arial",Font.PLAIN,20); // Cambiar el tamaño de la fuente de los botones
        Color colorBoton = new Color(255, 255, 255); // Color blanco para el fondo de los botones
        Color colorTexto = new Color(0, 0, 0); // Color negro para el texto de los botones

        // Botón para iniciar el juego
        JButton btnIniciar = new JButton("Iniciar juego");
        btnIniciar.setFont(botonFont); // Aplicar la fuente al botón
        btnIniciar.setBackground(colorBoton); // Color de fondo del botón
        btnIniciar.setForeground(colorTexto); // Color del texto del botón
        btnIniciar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde del botón
        btnIniciar.setPreferredSize(new Dimension(200, 50)); // Tamaño del botón
        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jugadores.isEmpty()) {
                    JOptionPane.showMessageDialog(AventuraDelTesoroGUI.this, "No hay jugadores creados. Crea un nuevo jugador.");
                } else {
                    SeleccionJugadorFrame seleccionJugadorFrame = new SeleccionJugadorFrame();
                    seleccionJugadorFrame.setVisible(true);
                    dispose(); // Cierra la ventana actual
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Espacio entre los botones
        add(btnIniciar, gbc);

        // Botón para ver el scoreboard
        JButton btnScoreboard = new JButton("Ver scoreboard");
        btnScoreboard.setFont(botonFont); // Aplicar la fuente al botón
        btnScoreboard.setBackground(colorBoton); // Color de fondo del botón
        btnScoreboard.setForeground(colorTexto); // Color del texto del botón
        btnScoreboard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde del botón
        btnScoreboard.setPreferredSize(new Dimension(200, 50)); // Tamaño del botón
        btnScoreboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(AventuraDelTesoroGUI.this, "Mostrando scoreboard...");
            }
        });
        gbc.gridy = 1;
        add(btnScoreboard, gbc);

        // Botón para crear nuevo jugador
        JButton btnNuevoJugador = new JButton("Crear nuevo jugador");
        btnNuevoJugador.setFont(botonFont); // Aplicar la fuente al botón
        btnNuevoJugador.setBackground(colorBoton); // Color de fondo del botón
        btnNuevoJugador.setForeground(colorTexto); // Color del texto del botón
        btnNuevoJugador.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde del botón
        btnNuevoJugador.setPreferredSize(new Dimension(200, 50)); // Tamaño del botón
        btnNuevoJugador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreJugador = JOptionPane.showInputDialog(AventuraDelTesoroGUI.this, "Introduce tu nombre:");
                if (nombreJugador != null && !nombreJugador.isEmpty()) {
                    jugadores.add(nombreJugador);
                    JOptionPane.showMessageDialog(AventuraDelTesoroGUI.this, "Jugador creado: " + nombreJugador);
                } else {
                    JOptionPane.showMessageDialog(AventuraDelTesoroGUI.this, "Nombre de jugador inválido. Inténtalo de nuevo.");
                }
            }
        });
        gbc.gridy = 2;
        add(btnNuevoJugador, gbc);

        // Botón para salir del juego
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(botonFont); // Aplicar la fuente al botón
        btnSalir.setBackground(colorBoton); // Color de fondo del botón
        btnSalir.setForeground(colorTexto); // Color del texto del botón
        btnSalir.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Borde del botón
        btnSalir.setPreferredSize(new Dimension(200, 50)); // Tamaño del botón
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        gbc.gridy = 3;
        add(btnSalir, gbc);

        setFocusable(true);
    }

    public class SeleccionJugadorFrame extends JFrame {
        public SeleccionJugadorFrame() {
            setTitle("Seleccionar Jugador");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Crear un JLabel para el GIF de fondo
            JLabel background = new JLabel(new ImageIcon("C:\\POO\\Proyecto de POO\\fondoo.gif"));
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
