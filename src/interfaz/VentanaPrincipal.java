package interfaz;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

import entidad.Planificador;
import entidad.Ubicacion;
import logica.LogicaAGM;

public class VentanaPrincipal implements ActionListener {

	private JFrame frame;
	private JMenuBar barraMenu;
	private JMenu opciones;
	private JMenuItem reiniciar;
	private JMenuItem salir;
	private JMapViewer mapa;
	private JPanel panelMapa;
	private JPanel panelDeControl;
	private JPanel panelDeCarga;
	private JPanel panelTabla;
	private JPanel panelInfo;
	private JButton botonCarga;
	private JButton botonGenerarConexiones;
	private JTable tablaDeCostos;
	private JComboBox<String> listaDeProvincias;
	private JList<String> listaNombreyProvincia;
	private List<Coordinate> coordenadas;
	private JTextField inputLatitud;
	private JTextField inputLongitud;
	private JTextField inputNombre;
	private JTextArea solucion;

	private Planificador planificador;
	private LogicaAGM logica;
	
	private List<Coordinate> conjuntoSolucion;
	private JScrollPane panelDeControlDeslizable;

	public VentanaPrincipal() {
		initialize();
	}
	
	private void agregarLocalidad(String nombre, String provincia, double latitud, double longitud) {
		Ubicacion ubicacion = new Ubicacion(nombre, provincia, latitud, longitud);
		Coordinate coordenada = new Coordinate(latitud, longitud);
		
		if (!coordenadas.contains(coordenada)) {
			coordenadas.add(coordenada);
			logica.agregarUbicacion(ubicacion);
			MapMarker marcador = new MapMarkerDot(new Coordinate(latitud, longitud));
			marcador.getStyle().setBackColor(Color.red);
			marcador.getStyle().setColor(Color.RED);
			mapa.addMapMarker(marcador);
			agregarNombreYProvinciaEnLista(nombre, provincia);
		} else {
			JOptionPane.showMessageDialog(null,
					nombre + "(" + provincia + ") ya fue agregada. No se admiten repetidos.", "Advertencia",
					JOptionPane.OK_OPTION);
		}
	}

	private void initialize() {

		planificador = new Planificador();
		logica = new LogicaAGM();
		coordenadas = new ArrayList<>();

		generarFrame();
		crearMenu();
		generarPanelDeControl();
		generarPanelMapa();

	}

	private void generarFrame() {
		frame = new JFrame();
		frame.setTitle("Conectando Localidades");
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(500, 20, 1100, 1020);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
	}

	private void crearMenu() {
		barraMenu = new JMenuBar();
		opciones = new JMenu("Opciones");
		frame.setJMenuBar(barraMenu);
		frame.setVisible(true);

		barraMenu.add(opciones);

		reiniciar = new JMenuItem("Reiniciar");
		reiniciar.addActionListener(this);

		salir = new JMenuItem("Salir");
		salir.addActionListener(this);

		opciones.add(reiniciar);
		opciones.add(salir);

	}
	
	private void generarPanelDeControl() {

		panelDeControlDeslizable= new JScrollPane();
		panelDeControlDeslizable.setFont(new Font("Unispace", Font.BOLD, 11));
		panelDeControlDeslizable.setBounds(501, 0, 583, 961);
		panelDeControlDeslizable.setBackground(new Color(128, 128, 128));
		panelDeControlDeslizable.setLayout(null);
		panelDeControlDeslizable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(panelDeControlDeslizable);
		
//		panelDeControl = new JPanel();
//		panelDeControl.setFont(new Font("Unispace", Font.BOLD, 11));
//		panelDeControl.setBackground(new Color(128, 128, 128));
//		panelDeControl.setBounds(501, 0, 583, 961);
//		panelDeControl.setLayout(null);
//		frame.getContentPane().add(panelDeControl);

		generarTitulo();
		generarTablaCostos();
		generarPanelDeCarga();
		generarListaNombreYProvincia();
		generarBotonConexion();
		generarPanelInfo();
	}

	private void generarPanelMapa() {
		generarMapa();
		panelMapa = new JPanel();
		panelMapa.setForeground(new Color(128, 128, 128));
		panelMapa.setBackground(new Color(128, 128, 128));
		panelMapa.setBounds(0, 0, 499, 1000);
		panelMapa.add(mapa);
		frame.getContentPane().add(panelMapa);
	}

	private void generarTitulo() {
		JTextField titulo = new JTextField();
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		titulo.setBackground(new Color(0, 128, 255));
		titulo.setFont(new Font("Unispace", Font.BOLD, 27));
		titulo.setText(" Conectando Localidades");
		titulo.setBounds(21, 11, 531, 67);
		titulo.setEditable(false);
//		panelDeControl.add(titulo);
		panelDeControlDeslizable.add(titulo);
	}

	@SuppressWarnings("serial")
	private void generarTablaCostos() {

		panelTabla = new JPanel();
		panelTabla.setBackground(new Color(0, 0, 0));
		panelTabla.setBounds(21, 111, 531, 59);
		panelTabla.setLayout(null);
		panelDeControlDeslizable.add(panelTabla);
		

		tablaDeCostos = new JTable() {
			public boolean isCellEditable(int row, int col) {
				return false;
			}

		};
		tablaDeCostos.setFont(new Font("Unispace", Font.BOLD, 11));
		tablaDeCostos.setRowSelectionAllowed(false);
		tablaDeCostos.setBorder(new LineBorder(new Color(0, 0, 0)));
		tablaDeCostos.setModel(new DefaultTableModel(
				new String[][] { { String.valueOf(planificador.getCostoPorKilometro()) + "$ ",
						"+" + String.valueOf(planificador.getPorcentajeDeAumento()) + "%",
						"+" + String.valueOf(planificador.getCostoDistintaProvincia() + "$") }, },
				new String[] { "Costo KM", "Costo KM si conexiÛn >300 KM", "Tasa ConexiÛn InterProvincial" }));

		JScrollPane scrollPane = new JScrollPane(tablaDeCostos);
		scrollPane.setBackground(new Color(255, 255, 255));
		scrollPane.setFont(new Font("Unispace", Font.BOLD, 11));
		scrollPane.setBounds(0, 0, 531, 59);
		panelTabla.add(scrollPane);

		JLabel costos = new JLabel("Costos");
		costos.setHorizontalAlignment(SwingConstants.CENTER);
		costos.setOpaque(true);
		costos.setForeground(new Color(0, 0, 0));
		costos.setBackground(new Color(128, 128, 255));
		costos.setFont(new Font("Unispace", Font.BOLD, 17));
		costos.setBounds(21, 89, 531, 23);
//		panelDeControl.add(costos);
		panelDeControlDeslizable.add(costos);
	}

	private void generarPanelDeCarga() {

		panelDeCarga = new JPanel();
		panelDeCarga.setBackground(new Color(64, 128, 128));
		panelDeCarga.setBounds(21, 207, 530, 191);
		panelDeCarga.setLayout(null);
//		panelDeControl.add(panelDeCarga);
		panelDeControlDeslizable.add(panelDeCarga);
		
		generarComboBox();
		generarInputs();
		generarBotonCarga();
	}

	private void generarComboBox() {
		String[] provincias = { "Buenos Aires", "Catamarca", "Chaco", "Chubut", "CÛrdoba", "Corrientes", "Entre RÌos",
				"Formosa", "Jujuy", "La Pampa", "La Rioja", "Mendoza", "Misiones", "NeuquÈn", "RÌo Negro", "Salta",
				"San Juan", "San Luis", "Santa Cruz", "Santa Fe", "Santiago del Estero", "Tierra del Fuego",
				"Tucum·n" };

		listaDeProvincias = new JComboBox<String>();
		for (int i = 0; i < provincias.length; i++) {
			listaDeProvincias.addItem(provincias[i]);
		}
		listaDeProvincias.setFont(new Font("Unispace", Font.BOLD, 11));
		listaDeProvincias.setBounds(325, 62, 171, 22);
		panelDeCarga.add(listaDeProvincias);
	}

	private void generarMapa() {

		mapa = new JMapViewer();
		mapa.setBorder(null);
		mapa.setAlignmentX(Component.LEFT_ALIGNMENT);
		mapa.setAlignmentY(Component.TOP_ALIGNMENT);
		mapa.setZoomControlsVisible(false);
		mapa.setPreferredSize(new Dimension(500, 950));
		mapa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		// Nos posicionamos sobre Argentina
		Coordinate coordenada = new Coordinate(-39.716, -63.616);
		mapa.setDisplayPosition(coordenada, 5);
		mapa.setBounds(0, 0, 500, 1000);
	}

	private void generarInputs() {

		JLabel _longitud = new JLabel("Longitud");
		_longitud.setFont(new Font("Unispace", Font.BOLD, 15));
		_longitud.setBounds(225, 111, 78, 19);
		panelDeCarga.add(_longitud);

		inputLongitud = new JTextField();
		inputLongitud.setFont(new Font("Unispace", Font.BOLD, 11));
		inputLongitud.setBounds(325, 112, 109, 20);
		panelDeCarga.add(inputLongitud);
		inputLongitud.setColumns(10);

		JLabel _latitud = new JLabel("Latitud");
		_latitud.setBounds(10, 111, 78, 19);
		panelDeCarga.add(_latitud);
		_latitud.setFont(new Font("Unispace", Font.BOLD, 15));

		inputLatitud = new JTextField();
		inputLatitud.setFont(new Font("Unispace", Font.BOLD, 11));
		inputLatitud.setBounds(82, 110, 98, 20);
		panelDeCarga.add(inputLatitud);
		inputLatitud.setColumns(10);

		JLabel _nombre = new JLabel("Nombre");
		_nombre.setFont(new Font("Unispace", Font.BOLD, 15));
		_nombre.setBounds(10, 64, 78, 14);
		panelDeCarga.add(_nombre);

		inputNombre = new JTextField();
		inputNombre.setFont(new Font("Unispace", Font.BOLD, 11));
		inputNombre.setBounds(82, 63, 98, 20);
		panelDeCarga.add(inputNombre);
		inputNombre.setColumns(10);

		JLabel _provincia = new JLabel("Provincia");
		_provincia.setFont(new Font("Unispace", Font.BOLD, 15));
		_provincia.setBounds(225, 63, 81, 16);
		panelDeCarga.add(_provincia);

		JLabel _ingresarLocalidad = new JLabel("Ingresar Localidad");
		_ingresarLocalidad.setFont(new Font("Unispace", Font.BOLD, 18));
		_ingresarLocalidad.setBounds(10, 11, 231, 24);
		panelDeCarga.add(_ingresarLocalidad);

	}

	private void generarBotonCarga() {
		botonCarga = new JButton("Cargar");
		botonCarga.setBounds(10, 163, 121, 19);
		botonCarga.addActionListener(this);
		panelDeCarga.add(botonCarga);
		botonCarga.setFont(new Font("Unispace", Font.BOLD, 13));
	}

	private void generarBotonConexion() {

		botonGenerarConexiones = new JButton("Generar Conexion");
		botonGenerarConexiones.setFont(new Font("Unispace", Font.BOLD, 13));
		botonGenerarConexiones.setBounds(21, 617, 206, 23);
		botonGenerarConexiones.addActionListener(this);
//		panelDeControl.add(botonGenerarConexiones);
		panelDeControlDeslizable.add(botonGenerarConexiones);
	}

	private void generarListaNombreYProvincia() {

		listaNombreyProvincia = new JList<String>();
		listaNombreyProvincia.setBorder(new LineBorder(new Color(0, 0, 0)));
		listaNombreyProvincia.setBackground(new Color(192, 192, 192));
		listaNombreyProvincia.setFont(new Font("Unispace", Font.BOLD, 11));
		modelarListaNombreYProvincia();
		JScrollPane listaDeslizable = new JScrollPane(listaNombreyProvincia);
		listaDeslizable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listaDeslizable.setBounds(21, 447, 531, 159);
//		panelDeControl.add(listaNombreyProvincia);
		panelDeControlDeslizable.add(listaDeslizable);
	}

	private DefaultListModel<String> modelarListaNombreYProvincia() {
		DefaultListModel<String> modelo = new DefaultListModel<>();
		listaNombreyProvincia.setModel(modelo);
		return modelo;
	}

	private DefaultListModel<String> agregarNombreYProvinciaEnLista(String nombre, String provincia) {
		DefaultListModel<String> modelo = (DefaultListModel<String>) listaNombreyProvincia.getModel();
		modelo.addElement(nombre + " - " + provincia);
		return modelo;
	}

	private void generarPanelInfo() {
		panelInfo = new JPanel();
		panelInfo.setBackground(new Color(0, 128, 192));
		panelInfo.setBounds(23, 687, 529, 215);
		panelInfo.setLayout(null);
//		panelDeControl.add(panelInfo);
		panelDeControlDeslizable.add(panelInfo);
		

		JLabel _localidadesIngresadas = new JLabel("Localidades ingresadas:");
		_localidadesIngresadas.setFont(new Font("Unispace", Font.BOLD, 15));
		_localidadesIngresadas.setBounds(21, 421, 408, 23);
//		panelDeControl.add(_localidadesIngresadas);
		panelDeControlDeslizable.add(_localidadesIngresadas);

		solucion = new JTextArea();
		solucion.setForeground(new Color(255, 255, 255));
		solucion.setFont(new Font("Unispace", Font.BOLD, 13));
		solucion.setBackground(new Color(0, 128, 192));
		solucion.setEditable(false);
		JScrollPane Deslizable = new JScrollPane(solucion);
		Deslizable.setBounds(10, 11, 509, 193);
		Deslizable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		Deslizable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panelInfo.add(Deslizable);

		JLabel _costoConexion = new JLabel("Costos de la conexiÛn:");
		_costoConexion.setBounds(21, 663, 206, 22);
		_costoConexion.setForeground(new Color(0, 0, 0));
		_costoConexion.setFont(new Font("Unispace", Font.BOLD, 15));
//		panelDeControl.add(_costoConexion);
		panelDeControlDeslizable.add(_costoConexion);

	}

	private void darSolucion() {
		solucion.append("Conexiones Telefonicas a construir : (En tramos) \n");
		solucion.append(logica.darSolucionAGM() + "\n");
		solucion.append("Solucion basada en el Algoritmo de Prim!");
	}

	private boolean verificarInputs() {
		if (inputLatitud.getText().isEmpty() || inputLongitud.getText().isEmpty() || inputNombre.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Hay campos vacios. Llenar los campos es obligatorio!", "Advertencia",
					JOptionPane.WARNING_MESSAGE);
			return false;
		} else {
			if (!inputLatitud.getText().matches("[0-9.-]+") || !inputLongitud.getText().matches("[0-9.-]+")
					|| !inputNombre.getText().matches("[a-zA-Z·ÈÌÛ˙¡…Õ”⁄Ò— ]+")) {
				JOptionPane.showMessageDialog(null,
						"Revise las entradas, hay ingresos invalidos \n"
								+ "Nombre: No admite numeros, ni caracteres especiales. \n"
								+ "Latitud: No admite letras. \n" + "Longitud: No admite letras. ",
						"Advertencia", JOptionPane.WARNING_MESSAGE);
				return false;
			}
			return true;
		}
	}

	private void limpiarCampos() {
		inputLatitud.setText(null);
		inputLongitud.setText(null);
		inputNombre.setText(null);
	}
	
	
	
	private void generarConexiones() {
		
		for(int i=0;i<conjuntoSolucion.size()-1;i++) {
			MapPolygon conexion = new MapPolygonImpl(conjuntoSolucion.get(i),conjuntoSolucion.get(i),conjuntoSolucion.get(i+1));
			mapa.addMapPolygon(conexion);
			
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == botonCarga) {
			if (verificarInputs()) {
				agregarLocalidad(inputNombre.getText(), listaDeProvincias.getSelectedItem().toString(),
						Double.parseDouble(inputLatitud.getText()), Double.parseDouble(inputLongitud.getText()));
				limpiarCampos();
			}
		}
		if (e.getSource() == botonGenerarConexiones) {
			if (logica.getUbicaciones().size() >= 2) {
				botonCarga.setEnabled(false);
				botonGenerarConexiones.setEnabled(false);
//				MapPolygon conexion = new MapPolygonImpl(coordenadas);
//				mapa.addMapPolygon(conexion);
				
				darSolucion();
				conjuntoSolucion=logica.solucion();
				generarConexiones();
				
			} else {
				JOptionPane.showMessageDialog(null,
						"Para generar una conexion debe haber al menos 2 localidades ingresadas!", "Mensaje",
						JOptionPane.OK_OPTION);
			}
		}
		if (e.getSource() == salir) {
			frame.dispose();
		}
		if (e.getSource() == reiniciar) {
			int respuesta = JOptionPane.showConfirmDialog(null,
					"Esta seguro que desea reiniciar? Perdera todo lo ingresado hasta ahora.", "Reiniciar", 0);
			if (respuesta == 0) {
				frame.dispose();
				@SuppressWarnings("unused")
				VentanaPrincipal nuevo = new VentanaPrincipal();
			}
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal window = new VentanaPrincipal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}