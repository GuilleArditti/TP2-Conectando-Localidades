package interfaz;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

import entidad.Ubicacion;
import logica.Logica;

public class VentanaPrincipal implements ActionListener{

	private JFrame frame;
	private JMenuBar barraMenu;
	private JMenu opciones;
	private JMenuItem reiniciar;
	private JMenuItem salir;
	private JMapViewer mapa;
	private Dimension tamañoPaneles;
	private JPanel panelPrincipal;
	private JPanel panelMapa;
	private JPanel panelDeControl;
	private JPanel panelDeCarga;
	private JPanel panelInfo;
	private JTextField campoCostoPorKM;
	private JTextField campoPorcentajeDeAumento;
	private JTextField campoTasaInterProvincial;
	private JTextField inputLatitud;
	private JTextField inputLongitud;
	private JTextField inputNombre;
	private JTextArea cuadroInformativo;
	private JButton botonGenerarConexiones;
	private JButton botonCarga;
	private JButton botonIngresarCostos;
	private JButton botonCargarConocida;
	private JButton botonEliminar;
	private JComboBox<String> listaDeProvincias;
	private JList<Ubicacion> listaDeUbicacionesIngresadas;
	private JList<Ubicacion> historialDeUbicaciones;
	private Logica logica;
	private ArrayList<ArrayList<Coordinate>> conjuntoSolucion;

	public VentanaPrincipal() {
		initialize();
	}

	private void initialize() {
		logica = new Logica();
		generarFrame();
		crearMenu();
		generarPanelMapa();
		generarPanelDeControl();
	}
	
	private void generarFrame() {
		frame = new JFrame();
		frame.setTitle("Conectando Localidades");
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(500, 20, 1200, 1020);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tamañoPaneles = new Dimension(600, 600);
		panelPrincipal = new JPanel(new GridLayout(1, 2));
		frame.getContentPane().add(panelPrincipal);
		frame.setVisible(true);
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

	private void generarPanelMapa() {
		generarMapa();
		panelMapa = new JPanel();
		panelMapa.setForeground(new Color(128, 128, 128));
		panelMapa.setBackground(new Color(128, 128, 128));
		panelMapa.setPreferredSize(tamañoPaneles);
		panelMapa.setMaximumSize(new Dimension(750,750));
		panelPrincipal.add(panelMapa);
		panelMapa.add(mapa);
	}

	private void generarMapa() {
		mapa = new JMapViewer();
		mapa.setBorder(null);
		mapa.setAlignmentX(Component.LEFT_ALIGNMENT);
		mapa.setAlignmentY(Component.TOP_ALIGNMENT);
		mapa.setZoomControlsVisible(false);
		mapa.setPreferredSize(new Dimension(500, 1000));
		mapa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		// Nos posicionamos sobre Argentina
		Coordinate coordenada = new Coordinate(-39.716, -63.616);
		mapa.setDisplayPosition(coordenada, 5);
		mapa.setBounds(0, 0, 500, 1000);
	}

	private void generarPanelDeControl() {
		panelDeControl = new JPanel();
		panelDeControl.setFont(new Font("Unispace", Font.BOLD, 11));
		panelDeControl.setBackground(new Color(128, 128, 128));
		panelDeControl.setPreferredSize(tamañoPaneles);
		panelDeControl.setMaximumSize(new Dimension(750,750));
		panelDeControl.setLayout(null);
		panelPrincipal.add(panelDeControl);

		generarTitulo();
		mostrarCostos();
		generarPanelDeCarga();
		generarListaDeUbicacionesIngresadas();
		generarListaDeUbicacionesConocidas();
		generarBotones();
		generarPanelInfo();
		
	}

	private void generarTitulo() {
		JTextField titulo = new JTextField();
		titulo.setBorder(new LineBorder(new Color(171, 173, 179)));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		titulo.setBackground(new Color(0, 128, 255));
		titulo.setFont(new Font("Unispace", Font.BOLD, 27));
		titulo.setText(" Conectando Localidades");
		titulo.setBounds(21, 0, 531, 44);
		titulo.setEditable(false);
		panelDeControl.add(titulo);
	}

	private void mostrarCostos() {
		JLabel costos = new JLabel("Costos");
		costos.setBorder(new LineBorder(new Color(0, 0, 0)));
		costos.setHorizontalAlignment(SwingConstants.CENTER);
		costos.setOpaque(true);
		costos.setForeground(new Color(0, 0, 0));
		costos.setBackground(new Color(128, 128, 255));
		costos.setFont(new Font("Unispace", Font.BOLD, 17));
		costos.setBounds(21, 50, 531, 23);
		panelDeControl.add(costos);

		JLabel etiquetaCostoXKM = new JLabel("Costo x KM ($)");
		etiquetaCostoXKM.setBorder(new LineBorder(new Color(0, 0, 0)));
		etiquetaCostoXKM.setOpaque(true);
		etiquetaCostoXKM.setFont(new Font("Unispace", Font.BOLD, 12));
		etiquetaCostoXKM.setHorizontalAlignment(SwingConstants.CENTER);
		etiquetaCostoXKM.setBounds(21, 70, 119, 23);
		panelDeControl.add(etiquetaCostoXKM);

		JLabel etiquetaPorcentajeDeAumento = new JLabel("% Aumento Costo x KM (>300KM) ");
		etiquetaPorcentajeDeAumento.setBorder(new LineBorder(new Color(0, 0, 0)));
		etiquetaPorcentajeDeAumento.setOpaque(true);
		etiquetaPorcentajeDeAumento.setFont(new Font("Unispace", Font.BOLD, 12));
		etiquetaPorcentajeDeAumento.setHorizontalAlignment(SwingConstants.CENTER);
		etiquetaPorcentajeDeAumento.setBounds(138, 70, 235, 23);
		etiquetaPorcentajeDeAumento
				.setToolTipText("Porcentaje de aumento del Costo x KM si la conexion es mayor a 300 KM");
		panelDeControl.add(etiquetaPorcentajeDeAumento);

		JLabel etiquetaTasaProvincial = new JLabel("Tasa InterProvincial ($)");
		etiquetaTasaProvincial.setBorder(new LineBorder(new Color(0, 0, 0)));
		etiquetaTasaProvincial.setOpaque(true);
		etiquetaTasaProvincial.setFont(new Font("Unispace", Font.BOLD, 12));
		etiquetaTasaProvincial.setHorizontalAlignment(SwingConstants.CENTER);
		etiquetaTasaProvincial.setBounds(372, 70, 180, 23);
		panelDeControl.add(etiquetaTasaProvincial);

		botonIngresarCostos = new JButton("Ingresar costos");
		botonIngresarCostos.setFont(new Font("Unispace", Font.BOLD, 12));
		botonIngresarCostos.setBounds(385, 123, 167, 23);
		botonIngresarCostos.addActionListener(this);
		panelDeControl.add(botonIngresarCostos);
	}

	private void generarPanelDeCarga() {
		panelDeCarga = new JPanel();
		panelDeCarga.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelDeCarga.setBackground(new Color(64, 128, 128));
		panelDeCarga.setBounds(22, 157, 530, 127);
		panelDeCarga.setLayout(null);
		panelDeControl.add(panelDeCarga);
		

		generarComboBox();
		generarInputs();
		generarBotonCarga();
	}

	private void generarComboBox() {
		String[] provincias = { "Buenos Aires", "Catamarca", "Chaco", "Chubut", "Córdoba", "Corrientes", "Entre Ríos",
				"Formosa", "Jujuy", "La Pampa", "La Rioja", "Mendoza", "Misiones", "Neuquén", "Río Negro", "Salta",
				"San Juan", "San Luis", "Santa Cruz", "Santa Fe", "Santiago del Estero", "Tierra del Fuego",
				"Tucumán" };

		listaDeProvincias = new JComboBox<String>();
		for (int i = 0; i < provincias.length; i++) {
			listaDeProvincias.addItem(provincias[i]);
		}
		listaDeProvincias.setFont(new Font("Unispace", Font.BOLD, 11));
		listaDeProvincias.setBounds(325, 33, 171, 22);
		panelDeCarga.add(listaDeProvincias);
	}

	private void generarInputs() {
		campoCostoPorKM = new JTextField();
		campoCostoPorKM.requestFocus();
		campoCostoPorKM.setHorizontalAlignment(SwingConstants.CENTER);
		campoCostoPorKM.setBorder(new LineBorder(new Color(171, 173, 179)));
		campoCostoPorKM.setBounds(21, 93, 119, 20);
		panelDeControl.add(campoCostoPorKM);
		campoCostoPorKM.setColumns(10);

		campoPorcentajeDeAumento = new JTextField();
		campoPorcentajeDeAumento.setHorizontalAlignment(SwingConstants.CENTER);
		campoPorcentajeDeAumento.setBorder(new LineBorder(new Color(171, 173, 179)));
		campoPorcentajeDeAumento.setColumns(10);
		campoPorcentajeDeAumento.setBounds(138, 93, 235, 20);
		panelDeControl.add(campoPorcentajeDeAumento);

		campoTasaInterProvincial = new JTextField();
		campoTasaInterProvincial.setHorizontalAlignment(SwingConstants.CENTER);
		campoTasaInterProvincial.setBorder(new LineBorder(new Color(171, 173, 179)));
		campoTasaInterProvincial.setColumns(10);
		campoTasaInterProvincial.setBounds(372, 93, 180, 20);
		panelDeControl.add(campoTasaInterProvincial);

		JLabel _longitud = new JLabel("Longitud");
		_longitud.setFont(new Font("Unispace", Font.BOLD, 15));
		_longitud.setBounds(222, 60, 78, 19);
		panelDeCarga.add(_longitud);

		inputLongitud = new JTextField();
		inputLongitud.setFont(new Font("Unispace", Font.BOLD, 11));
		inputLongitud.setBounds(325, 61, 109, 20);
		inputLongitud.setColumns(10);
		panelDeCarga.add(inputLongitud);

		JLabel _latitud = new JLabel("Latitud");
		_latitud.setBounds(10, 65, 78, 19);
		panelDeCarga.add(_latitud);
		_latitud.setFont(new Font("Unispace", Font.BOLD, 15));

		inputLatitud = new JTextField();
		inputLatitud.setFont(new Font("Unispace", Font.BOLD, 11));
		inputLatitud.setBounds(82, 61, 98, 20);
		inputLatitud.setColumns(10);
		panelDeCarga.add(inputLatitud);

		JLabel _nombre = new JLabel("Nombre");
		_nombre.setFont(new Font("Unispace", Font.BOLD, 15));
		_nombre.setBounds(10, 35, 78, 14);
		panelDeCarga.add(_nombre);

		inputNombre = new JTextField();
		inputNombre.setFont(new Font("Unispace", Font.BOLD, 11));
		inputNombre.setBounds(82, 34, 98, 20);
		inputNombre.setColumns(10);
		panelDeCarga.add(inputNombre);

		JLabel _provincia = new JLabel("Provincia");
		_provincia.setFont(new Font("Unispace", Font.BOLD, 15));
		_provincia.setBounds(222, 34, 81, 16);
		panelDeCarga.add(_provincia);

		JLabel _ingresarLocalidad = new JLabel("Ingresar Localidad");
		_ingresarLocalidad.setFont(new Font("Unispace", Font.BOLD, 16));
		_ingresarLocalidad.setBounds(10, 0, 231, 22);
		panelDeCarga.add(_ingresarLocalidad);
	}

	private void generarBotonCarga() {
		botonCarga = new JButton("Cargar");
		botonCarga.setEnabled(false);
		botonCarga.setBounds(10, 97, 121, 19);
		botonCarga.addActionListener(this);
		panelDeCarga.add(botonCarga);
		botonCarga.setFont(new Font("Unispace", Font.BOLD, 13));
	}

	private void generarBotones() {
		
		botonCargarConocida = new JButton("Cargar");
		botonCargarConocida.setFont(new Font("Unispace", Font.BOLD, 13));
		botonCargarConocida.setEnabled(false);
		botonCargarConocida.addActionListener(this);
		botonCargarConocida.setBounds(494, 463, 93, 23);
		panelDeControl.add(botonCargarConocida);
		
		botonEliminar = new JButton("Eliminar");
		botonEliminar.setFont(new Font("Unispace", Font.BOLD, 13));
		botonEliminar.setEnabled(false);
		botonEliminar.addActionListener(this);
		botonEliminar.setBounds(21, 464, 119, 23);
		panelDeControl.add(botonEliminar);
		
		botonGenerarConexiones = new JButton("Generar Conexion");
		botonGenerarConexiones.setEnabled(false);
		botonGenerarConexiones.setFont(new Font("Unispace", Font.BOLD, 13));
		botonGenerarConexiones.setBounds(207, 495, 206, 23);
		botonGenerarConexiones.addActionListener(this);
		botonGenerarConexiones.setToolTipText("Genera las conexiones en el mapa en base a las localidades ingresadas");
		panelDeControl.add(botonGenerarConexiones);
	}
	private void generarListaDeUbicacionesIngresadas() {
		JLabel _localidadesIngresadas = new JLabel("Localidades ingresadas:");
		_localidadesIngresadas.setFont(new Font("Unispace", Font.BOLD, 15));
		_localidadesIngresadas.setBounds(21, 295, 278, 23);
		panelDeControl.add(_localidadesIngresadas);
		
		listaDeUbicacionesIngresadas = new JList<Ubicacion>();
		listaDeUbicacionesIngresadas.setBorder(new LineBorder(new Color(0, 0, 0)));
		listaDeUbicacionesIngresadas.setBackground(new Color(192, 192, 192));
		listaDeUbicacionesIngresadas.setFont(new Font("Unispace", Font.BOLD, 11));
		modelarListaDeUbicacionesIngresadas();
		JScrollPane listaDeslizable = new JScrollPane(listaDeUbicacionesIngresadas);
		listaDeslizable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		listaDeslizable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listaDeslizable.setBounds(21, 321, 278, 137);
		panelDeControl.add(listaDeslizable);
	}
	
	private void generarListaDeUbicacionesConocidas() {
		JLabel _localidadesConocidas = new JLabel("Localidades conocidas:");
		_localidadesConocidas.setFont(new Font("Unispace", Font.BOLD, 15));
		_localidadesConocidas.setBounds(309, 295, 278, 23);
		panelDeControl.add(_localidadesConocidas);
		
		historialDeUbicaciones = new JList<Ubicacion>();
		historialDeUbicaciones.setBorder(new LineBorder(new Color(0, 0, 0)));
		historialDeUbicaciones.setBackground(new Color(192, 192, 192));
		historialDeUbicaciones.setFont(new Font("Unispace", Font.BOLD, 11));
		modelarHistorialDeUbicaciones();
		JScrollPane listaConocidasDeslizable = new JScrollPane(historialDeUbicaciones);
		listaConocidasDeslizable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		listaConocidasDeslizable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listaConocidasDeslizable.setBounds(309, 321, 278, 137);
		cargarHistorialDeUbicaciones();
		panelDeControl.add(listaConocidasDeslizable);
	}
	
	private DefaultListModel<Ubicacion> modelarHistorialDeUbicaciones() {
		DefaultListModel<Ubicacion> modelo = new DefaultListModel<>();
		historialDeUbicaciones.setModel(modelo);
		return modelo;
		
	}

	private DefaultListModel<Ubicacion> cargarHistorialDeUbicaciones() {
		ArrayList<Ubicacion> localidadesConocidas= logica.getHistorialDeUbicaciones();
		DefaultListModel<Ubicacion> modelo = (DefaultListModel<Ubicacion>) historialDeUbicaciones.getModel();
		for(Ubicacion ubicacion: localidadesConocidas) {
				modelo.addElement(ubicacion);
		}
		return modelo;
	}

	private DefaultListModel<Ubicacion> modelarListaDeUbicacionesIngresadas() {
		DefaultListModel<Ubicacion> modelo = new DefaultListModel<>();
		listaDeUbicacionesIngresadas.setModel(modelo);
		return modelo;
	}

	private DefaultListModel<Ubicacion> mostrarUbicacionIngresada(Ubicacion ubicacion) {
		DefaultListModel<Ubicacion> modelo = (DefaultListModel<Ubicacion>) listaDeUbicacionesIngresadas.getModel();
		modelo.addElement(ubicacion);
		return modelo;
	}
	
	private DefaultListModel<Ubicacion> eliminarUbicacionDeLaLista(Ubicacion ubicacion){
		DefaultListModel<Ubicacion> modelo = (DefaultListModel<Ubicacion>) listaDeUbicacionesIngresadas.getModel();
		modelo.removeElement(ubicacion);
		return modelo;
	}

	private void generarPanelInfo() {
		panelInfo = new JPanel();
		panelInfo.setBackground(new Color(0, 128, 192));
		panelInfo.setBounds(23, 552, 529, 180);
		panelInfo.setLayout(null);
		panelDeControl.add(panelInfo);

		JLabel _costoConexion = new JLabel("Costos de la conexión:");
		_costoConexion.setBounds(21, 529, 206, 22);
		_costoConexion.setForeground(new Color(0, 0, 0));
		_costoConexion.setFont(new Font("Unispace", Font.BOLD, 15));
		panelDeControl.add(_costoConexion);

		cuadroInformativo = new JTextArea();
		cuadroInformativo.setEditable(false);
		cuadroInformativo.setForeground(new Color(255, 255, 255));
		cuadroInformativo.setFont(new Font("Unispace", Font.BOLD, 13));
		cuadroInformativo.setBackground(new Color(0, 128, 192));
		JScrollPane Deslizable = new JScrollPane(cuadroInformativo);
		Deslizable.setBounds(31, 552, 514, 169);
		panelDeControl.add(Deslizable);
		Deslizable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		Deslizable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				
	}

//	private void agregarUbicacion(String nombre, String provincia, double latitud, double longitud) {
//		Ubicacion ubicacion= new Ubicacion(nombre, provincia, latitud, longitud);
//		try {
//			logica.agregarUbicacion(nombre, provincia, latitud, longitud);
//			MapMarker marcador = new MapMarkerDot(new Coordinate(latitud, longitud));
//			marcador.getStyle().setBackColor(Color.red);
//			marcador.getStyle().setColor(Color.RED);
//			mapa.addMapMarker(marcador);
//			mostrarUbicacionIngresada(nombre + ", " + provincia);
//		} catch (Exception e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), "Advertencia", JOptionPane.OK_OPTION);
//		}
//
//	}
	
	private void agregarUbicacion(String nombre, String provincia, double latitud, double longitud) {
		Ubicacion ubicacion= new Ubicacion(nombre, provincia, latitud, longitud);
		try {
			logica.agregarUbicacion(ubicacion);
			MapMarker marcador = new MapMarkerDot(new Coordinate(latitud, longitud));
			marcador.getStyle().setBackColor(Color.red);
			marcador.getStyle().setColor(Color.RED);
			mapa.addMapMarker(marcador);
			mostrarUbicacionIngresada(ubicacion);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Advertencia", JOptionPane.OK_OPTION);
		}

	}
	
	private void agregarUbicacionConocida(Ubicacion ubicacion) {
		
		try {
			logica.ingresarUbicacionConocida(ubicacion);
			mostrarUbicacionIngresada(ubicacion);
			MapMarker marcador = new MapMarkerDot(new Coordinate(ubicacion.getLatitud(), ubicacion.getLongitud()));
			marcador.getStyle().setBackColor(Color.red);
			marcador.getStyle().setColor(Color.RED);
			mapa.addMapMarker(marcador);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					e.getMessage(), "Mensaje",
					JOptionPane.OK_OPTION);
		}
	}
	
	private void eliminarUbicacion(Ubicacion ubicacion) {
		logica.eliminarUbicacion(ubicacion);
		eliminarUbicacionDeLaLista(ubicacion);
		for(MapMarker marcador: mapa.getMapMarkerList()) {
			if(ubicacion.getLatitud()==marcador.getLat() && ubicacion.getLongitud()==marcador.getLon()) {
				mapa.removeMapMarker(marcador);
				mapa.removeAllMapPolygons();
				break;
				
			}
			
		}
		
	}
	
	private boolean verificarInputsLocalidad() {
		if (inputLatitud.getText().isEmpty() || inputLongitud.getText().isEmpty() || inputNombre.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Hay campos vacios. Llenar los campos es obligatorio!", "Advertencia",
					JOptionPane.WARNING_MESSAGE);
			return false;
		} else {
			if (!inputLatitud.getText().matches("[0-9.-]+") || !inputLongitud.getText().matches("[0-9.-]+")
					|| !inputNombre.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
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

	private boolean verificarInputsCostos() {
		if ((campoCostoPorKM.getText().equals("0") || campoCostoPorKM.getText().isEmpty()
				|| campoPorcentajeDeAumento.getText().isEmpty() || campoTasaInterProvincial.getText().isEmpty())) {
			JOptionPane.showMessageDialog(null, "Campos vacíos o costo por KM en 0. Definir los costos es obligatorio!",
					"Advertencia", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
//		if (campoCostoPorKM.getText().getClass().equals(String.class)
//			|| campoPorcentajeDeAumento.getText().getClass().equals(String.class)
//			|| campoTasaInterProvincial.getText().getClass().equals(String.class)) {
//			JOptionPane.showMessageDialog(null, "Solo se admiten numeros para los costos!", "Advertencia",
//					JOptionPane.WARNING_MESSAGE);
//			return false;
//		}
		
		if (Double.parseDouble(campoCostoPorKM.getText()) < 0
				|| Double.parseDouble(campoPorcentajeDeAumento.getText()) < 0
				|| Double.parseDouble(campoTasaInterProvincial.getText()) < 0) {
			JOptionPane.showMessageDialog(null, "Solo se admiten numeros positivos para los costos!", "Advertencia",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private void limpiarCampos() {
		inputLatitud.setText(null);
		inputLongitud.setText(null);
		inputNombre.setText(null);
	}

	private void darSolucion() {
		logica.generarSolucion();
		cuadroInformativo.setText("");
		cuadroInformativo.append("Conexiones Telefonicas a construir : (En tramos) \n\n");
		cuadroInformativo.append(logica.generarStringResultado() + "\n\n");
		cuadroInformativo.append("Solucion basada en el Algoritmo de Prim!");
		dibujarConexiones();
	}

	
	private void dibujarConexiones() {
		mapa.removeAllMapPolygons();
		conjuntoSolucion=logica.coordenadasSolucion();
		for(int i=0;i<conjuntoSolucion.size();i++) {
			for(int j=0;j<conjuntoSolucion.get(i).size()-1;j++) {
				 MapPolygon conexion = new MapPolygonImpl(conjuntoSolucion.get(i).get(j),
							conjuntoSolucion.get(i).get(j+1),
							conjuntoSolucion.get(i).get(j+1));
				 mapa.addMapPolygon(conexion);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == botonIngresarCostos) {
			if (verificarInputsCostos()) {
				logica.definirCostos(Integer.valueOf(campoCostoPorKM.getText()),
						Integer.valueOf(campoPorcentajeDeAumento.getText()),
						Integer.valueOf(campoTasaInterProvincial.getText()));
				JOptionPane.showMessageDialog(null, "Costos ingresados exitosamente!", "Exito",
						JOptionPane.INFORMATION_MESSAGE);
				botonIngresarCostos.setText("Actualizar costos");
				botonCarga.setEnabled(true);
				botonCargarConocida.setEnabled(true);
				botonGenerarConexiones.setEnabled(true);
			}
		}
		
		if (e.getSource() == botonCarga) {
			if (verificarInputsLocalidad()) {
				agregarUbicacion(inputNombre.getText(), listaDeProvincias.getSelectedItem().toString(),
						Double.parseDouble(inputLatitud.getText()), Double.parseDouble(inputLongitud.getText()));
				limpiarCampos();
				botonEliminar.setEnabled(true);
				botonGenerarConexiones.setEnabled(true);
			}
		}
		
		if(e.getSource()==botonCargarConocida) {
					if(!historialDeUbicaciones.isSelectionEmpty()) {
						agregarUbicacionConocida(historialDeUbicaciones.getSelectedValue());
						botonEliminar.setEnabled(true);
						botonGenerarConexiones.setEnabled(true);
					}
					else {
						JOptionPane.showMessageDialog(null,
								"Debe seleccionar una ubicacion conocida de la lista para ingresarla", "Mensaje",
								JOptionPane.OK_OPTION);
					}
		}
		
		if (e.getSource() == botonGenerarConexiones) {
				try {
					darSolucion();
					botonGenerarConexiones.setEnabled(false);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,
							e1.getMessage(), "Mensaje",
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
		
		if(e.getSource()==botonEliminar) {
			if(!listaDeUbicacionesIngresadas.isSelectionEmpty()) {
			eliminarUbicacion(listaDeUbicacionesIngresadas.getSelectedValue());
			botonGenerarConexiones.setEnabled(true);
			}
			else {
				JOptionPane.showMessageDialog(null,
						"Debe seleccionar una ubicacion de la lista para eliminarla", "Mensaje",
						JOptionPane.OK_OPTION);
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