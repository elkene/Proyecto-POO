import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AventuraDelTesoroGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel panelPrincipal;
    private JPanel panelMenu;
    private JPanel panelJuego;
    private JLabel lblTemporizador;
    private Timer timer;

    private JLabel lblArriba, lblAbajo, lblIzquierda, lblDerecha;

    public AventuraDelTesoroGUI() {
        setTitle("Aventura del Tesoro");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear el CardLayout y el panel principal
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        // Crear el menú principal
        panelMenu = new JPanel();
        panelMenu.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton btnEntrar = new JButton("Entrar");
        JButton btnAjustes = new JButton("Ajustes");
        JButton btnCerrar = new JButton("Cerrar");

        btnEntrar.addActionListener(e -> entrarJuego());
        btnAjustes.addActionListener(e -> mostrarAjustes());
        btnCerrar.addActionListener(e -> System.exit(0));

        panelMenu.add(btnEntrar, gbc);
        gbc.gridy++;
        panelMenu.add(btnAjustes, gbc);
        gbc.gridy++;
        panelMenu.add(btnCerrar, gbc);

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

        // Mostrar el menú principal al iniciar
        cardLayout.show(panelPrincipal, "Menu");
    }

    private void entrarJuego() {
        // Mostrar el panel del juego y comenzar el temporizador
        cardLayout.show(panelPrincipal, "Juego");
        timer.start();
        panelJuego.requestFocusInWindow();
    }

    private void mostrarAjustes() {
        JOptionPane.showMessageDialog(this, "Aquí puedes configurar los ajustes del juego.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AventuraDelTesoroGUI juego = new AventuraDelTesoroGUI();
            juego.setVisible(true);
        });
    }
}
