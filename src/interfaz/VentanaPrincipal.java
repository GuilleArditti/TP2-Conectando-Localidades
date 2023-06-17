package interfaz;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import entidad.Arista;
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
	private JPanel panelInfo;
	private JTextField campoCostoPorKM;
	private JTextField campoPorcentajeDeAumento;
	private JTextField campoTasaInterProvincial;
	private JButton botonCarga;
	private JButton botonIngresarCostos;
	private JTextField inputLatitud;
	private JTextField inputLongitud;
	private JTextField inputNombre;
	private JButton botonGenerarConexiones;
	private JComboBox<String> listaDeProvincias;
	private JList<String> listaNombreyProvincia;
	private JTextArea solucion;
	
	private LogicaAGM logica;

	
//	private List<Coordinate> conjuntoSolucion;
//	private JScrollPane panelDeControlDeslizable;


	public VentanaPrincipal() {
		initialize();
	}

	private void initialize() {		
		logica = new LogicaAGM();
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
		
		//en duda para panel scrolleable

//		panelDeControlDeslizable= new JScrollPane();
//		panelDeControlDeslizable.setFont(new Font("Unispace", Font.BOLD, 11));
//		panelDeControlDeslizable.setBounds(501, 0, 583, 961);
//		panelDeControlDeslizable.setBackground(new Color(128, 128, 128));
//		panelDeControlDeslizable.setLayout(null);
//		panelDeControlDeslizable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		frame.getContentPane().add(panelDeControlDeslizable);
		
		panelDeControl = new JPanel();
		panelDeControl.setFont(new Font("Unispace", Font.BOLD, 11));
		panelDeControl.setBackground(new Color(128, 128, 128));
		panelDeControl.setBounds(501, 0, 583, 961);
		panelDeControl.setLayout(null);
		frame.getContentPane().add(panelDeControl);

		generarTitulo();
		mostrarCostos();
		generarPanelDeCarga();
		generarListaNombreYProvincia();
		generarBotonConexion();
		generarPanelInfo();
	}
	


	private void generarTitulo() {
		JTextField titulo = new JTextField();
		titulo.setBorder(new LineBorder(new Color(171, 173, 179)));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		titulo.setBackground(new Color(0, 128, 255));
		titulo.setFont(new Font("Unispace", Font.BOLD, 27));
		titulo.setText(" Conectando Localidades");
		titulo.setBounds(21, 11, 531, 67);
		titulo.setEditable(false);
		panelDeControl.add(titulo);
//		panelDeControlDeslizable.add(titulo);
	}

	private void mostrarCostos() {
		JLabel costos = new JLabel("Costos");
		costos.setBorder(new LineBorder(new Color(0, 0, 0)));
		costos.setHorizontalAlignment(SwingConstants.CENTER);
		costos.setOpaque(true);
		costos.setForeground(new Color(0, 0, 0));
		costos.setBackground(new Color(128, 128, 255));
		costos.setFont(new Font("Unispace", Font.BOLD, 17));
		costos.setBounds(21, 97, 531, 23);
		panelDeControl.add(costos);

		JLabel etiquetaCostoXKM = new JLabel("Costo x KM ($)");
		etiquetaCostoXKM.setBorder(new LineBorder(new Color(0, 0, 0)));
		etiquetaCostoXKM.setOpaque(true);
		etiquetaCostoXKM.setFont(new Font("Unispace", Font.BOLD, 12));
		etiquetaCostoXKM.setHorizontalAlignment(SwingConstants.CENTER);
		etiquetaCostoXKM.setBounds(21, 115, 119, 28);
		panelDeControl.add(etiquetaCostoXKM);

		JLabel etiquetaPorcentajeDeAumento = new JLabel("% Aumento Costo x KM (>300KM) ");
		etiquetaPorcentajeDeAumento.setBorder(new LineBorder(new Color(0, 0, 0)));
		etiquetaPorcentajeDeAumento.setOpaque(true);
		etiquetaPorcentajeDeAumento.setFont(new Font("Unispace", Font.BOLD, 12));
		etiquetaPorcentajeDeAumento.setHorizontalAlignment(SwingConstants.CENTER);
		etiquetaPorcentajeDeAumento.setBounds(138, 115, 235, 28);
		etiquetaPorcentajeDeAumento.setToolTipText("Porcentaje de aumento del Costo x KM si la conexion es mayor a 300 KM");
		panelDeControl.add(etiquetaPorcentajeDeAumento);

		JLabel etiquetaTasaProvincial = new JLabel("Tasa InterProvincial ($)");
		etiquetaTasaProvincial.setBorder(new LineBorder(new Color(0, 0, 0)));
		etiquetaTasaProvincial.setOpaque(true);
		etiquetaTasaProvincial.setFont(new Font("Unispace", Font.BOLD, 12));
		etiquetaTasaProvincial.setHorizontalAlignment(SwingConstants.CENTER);
		etiquetaTasaProvincial.setBounds(372, 115, 180, 28);
		panelDeControl.add(etiquetaTasaProvincial);

		botonIngresarCostos = new JButton("Ingresar costos");
		botonIngresarCostos.setFont(new Font("Unispace", Font.BOLD, 12));
		botonIngresarCostos.setBounds(385, 182, 167, 23);
		botonIngresarCostos.addActionListener(this);
		panelDeControl.add(botonIngresarCostos);
//		panelDeControlDeslizable.add(costos);
	}

	private void generarPanelDeCarga() {

		panelDeCarga = new JPanel();
		panelDeCarga.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelDeCarga.setBackground(new Color(64, 128, 128));
		panelDeCarga.setBounds(21, 216, 530, 194);
		panelDeCarga.setLayout(null);
		panelDeControl.add(panelDeCarga);
//		panelDeControlDeslizable.add(panelDeCarga);
		
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
	

	private void generarInputs() {
		
		campoCostoPorKM = new JTextField();
		campoCostoPorKM.requestFocus();
		campoCostoPorKM.setHorizontalAlignment(SwingConstants.CENTER);
		campoCostoPorKM.setBorder(new LineBorder(new Color(171, 173, 179)));
		campoCostoPorKM.setBounds(21, 143, 119, 20);
		panelDeControl.add(campoCostoPorKM);
		campoCostoPorKM.setColumns(10);
		
		campoPorcentajeDeAumento = new JTextField();
		campoPorcentajeDeAumento.setHorizontalAlignment(SwingConstants.CENTER);
		campoPorcentajeDeAumento.setBorder(new LineBorder(new Color(171, 173, 179)));
		campoPorcentajeDeAumento.setColumns(10);
		campoPorcentajeDeAumento.setBounds(138, 143, 235, 20);
		panelDeControl.add(campoPorcentajeDeAumento);
		
		campoTasaInterProvincial = new JTextField();
		campoTasaInterProvincial.setHorizontalAlignment(SwingConstants.CENTER);
		campoTasaInterProvincial.setBorder(new LineBorder(new Color(171, 173, 179)));
		campoTasaInterProvincial.setColumns(10);
		campoTasaInterProvincial.setBounds(372, 143, 180, 20);
		panelDeControl.add(campoTasaInterProvincial);

		JLabel _longitud = new JLabel("Longitud");
		_longitud.setFont(new Font("Unispace", Font.BOLD, 15));
		_longitud.setBounds(225, 111, 78, 19);
		panelDeCarga.add(_longitud);

		inputLongitud = new JTextField();
		inputLongitud.setFont(new Font("Unispace", Font.BOLD, 11));
		inputLongitud.setBounds(325, 112, 109, 20);
		inputLongitud.setColumns(10);
		panelDeCarga.add(inputLongitud);
		

		JLabel _latitud = new JLabel("Latitud");
		_latitud.setBounds(10, 111, 78, 19);
		panelDeCarga.add(_latitud);
		_latitud.setFont(new Font("Unispace", Font.BOLD, 15));

		inputLatitud = new JTextField();
		inputLatitud.setFont(new Font("Unispace", Font.BOLD, 11));
		inputLatitud.setBounds(82, 110, 98, 20);
		inputLatitud.setColumns(10);
		panelDeCarga.add(inputLatitud);
		

		JLabel _nombre = new JLabel("Nombre");
		_nombre.setFont(new Font("Unispace", Font.BOLD, 15));
		_nombre.setBounds(10, 64, 78, 14);
		panelDeCarga.add(_nombre);

		inputNombre = new JTextField();
		inputNombre.setFont(new Font("Unispace", Font.BOLD, 11));
		inputNombre.setBounds(82, 63, 98, 20);
		inputNombre.setColumns(10);
		panelDeCarga.add(inputNombre);
		

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
		botonCarga.setEnabled(false);
		botonCarga.setBounds(10, 163, 121, 19);
		botonCarga.addActionListener(this);
		panelDeCarga.add(botonCarga);
		botonCarga.setFont(new Font("Unispace", Font.BOLD, 13));
	}

	private void generarBotonConexion() {

		botonGenerarConexiones = new JButton("Generar Conexion");
		botonGenerarConexiones.setEnabled(false);
		botonGenerarConexiones.setFont(new Font("Unispace", Font.BOLD, 13));
		botonGenerarConexiones.setBounds(21, 617, 206, 23);
		botonGenerarConexiones.addActionListener(this);
		panelDeControl.add(botonGenerarConexiones);
//		panelDeControlDeslizable.add(botonGenerarConexiones);
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
		panelDeControl.add(listaDeslizable);
//		panelDeControlDeslizable.add(listaDeslizable);
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
		panelDeControl.add(panelInfo);
//		panelDeControlDeslizable.add(panelInfo);
		

		JLabel _localidadesIngresadas = new JLabel("Localidades ingresadas:");
		_localidadesIngresadas.setFont(new Font("Unispace", Font.BOLD, 15));
		_localidadesIngresadas.setBounds(21, 421, 408, 23);
		panelDeControl.add(_localidadesIngresadas);
//		panelDeControlDeslizable.add(_localidadesIngresadas);

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
		panelDeControl.add(_costoConexion);
//		panelDeControlDeslizable.add(_costoConexion);

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
	
	private void agregarLocalidad(String nombre, String provincia, double latitud, double longitud) {
			try {
				logica.agregarUbicacion(nombre, provincia, latitud, longitud);
				MapMarker marcador = new MapMarkerDot(new Coordinate(latitud, longitud));
				marcador.getStyle().setBackColor(Color.red);
				marcador.getStyle().setColor(Color.RED);
				mapa.addMapMarker(marcador);
				agregarNombreYProvinciaEnLista(nombre, provincia);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						e.getMessage(), "Advertencia",
						JOptionPane.OK_OPTION);
			}
				
	}	

	private void mostrarSolucion() {
		solucion.setText("");
		solucion.append("Conexiones Telefonicas a construir : (En tramos) \n\n");
		solucion.append(logica.darSolucionAGM() + "\n\n");
		solucion.append("Solucion basada en el Algoritmo de Prim!");
		
	}

	private boolean verificarInputsLocalidad() {
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
	
	private boolean verificarInputsCostos() {
		if ((campoCostoPorKM.getText().equals("0") ||
			campoPorcentajeDeAumento.getText().equals("0") ||
			campoTasaInterProvincial.getText().equals("0")) ||
			campoCostoPorKM.getText().isEmpty() ||
			campoPorcentajeDeAumento.getText().isEmpty() ||
			campoTasaInterProvincial.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Campos vacÌos o en 0. Definir los costos es obligatorio!", "Advertencia",
					JOptionPane.WARNING_MESSAGE);
			return false;
		} 
		if (campoCostoPorKM.getText().matches("[a-zA-Z·ÈÌÛ˙¡…Õ”⁄Ò— ]+")
			|| campoPorcentajeDeAumento.getText().matches("[a-zA-Z·ÈÌÛ˙¡…Õ”⁄Ò— ]+")
			||campoTasaInterProvincial.getText().matches("[a-zA-Z·ÈÌÛ˙¡…Õ”⁄Ò— ]+")) {
			JOptionPane.showMessageDialog(null,
						"Solo se admiten numeros para los costos!",
						"Advertencia", JOptionPane.WARNING_MESSAGE);
				return false;
		}
		if(Double.parseDouble(campoCostoPorKM.getText())<0 ||
			Double.parseDouble(campoPorcentajeDeAumento.getText())<0 ||
			Double.parseDouble(campoTasaInterProvincial.getText())<0) {
			JOptionPane.showMessageDialog(null,
					"Solo se admiten numeros positivos para los costos!",
					"Advertencia", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
		}
	

	private void limpiarCampos() {
		inputLatitud.setText(null);
		inputLongitud.setText(null);
		inputNombre.setText(null);
	}
	
	private void dibujarConexiones() {
	    List<Ubicacion> ubicaciones = logica.getUbicaciones();
	    
	    Ubicacion ubiOrigen;
	    Ubicacion ubiDestino;
	    
	    Coordinate coord1;
	    Coordinate coord2;
	    
	    for (Arista arista : logica.getAristasAGM()) {
	      ubiOrigen = ubicaciones.get(arista.getOrigen());
	      ubiDestino = ubicaciones.get(arista.getDestino());
	      
	      coord1 = new Coordinate(ubiOrigen.getLatitud(), ubiOrigen.getLongitud());
	      coord2 = new Coordinate(ubiDestino.getLatitud(), ubiDestino.getLongitud());
	      
	      MapPolygon conexion = new MapPolygonImpl(coord1, coord2, coord2);
	      mapa.addMapPolygon(conexion);
	    }
	  }
	
	//Metodo experimental a corregir (grafica ciclos)
	//pensado para que la interfaz no maneje aristas ni ubicaciones, solo las coordenadas a representar pasadas desde la logica.
	
//	private void representarConexiones() {
//		conjuntoSolucion=logica.solucion();
//		for(int i=0;i<conjuntoSolucion.size()-1;i++) {
//			 MapPolygon conexion = new MapPolygonImpl(conjuntoSolucion.get(i),
//					 								conjuntoSolucion.get(i+1),
//					 								conjuntoSolucion.get(i+1));
//		     mapa.addMapPolygon(conexion);
//		}
//	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == botonCarga) {
			if (verificarInputsLocalidad()) {
				agregarLocalidad(inputNombre.getText(), listaDeProvincias.getSelectedItem().toString(),
						Double.parseDouble(inputLatitud.getText()), Double.parseDouble(inputLongitud.getText()));
				limpiarCampos();
				botonGenerarConexiones.setEnabled(true);
			}
		}
		if (e.getSource() == botonGenerarConexiones) {
			if (logica.getUbicaciones().size() >= 2) {
				mapa.removeAllMapPolygons();
				mostrarSolucion();
				dibujarConexiones();
				//representarConexiones();
				botonGenerarConexiones.setEnabled(false);
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
		
		if(e.getSource()== botonIngresarCostos) {
			if(verificarInputsCostos()) {
				logica.definirCostos(Integer.valueOf(campoCostoPorKM.getText()),
									Integer.valueOf(campoPorcentajeDeAumento.getText()),
									Integer.valueOf(campoTasaInterProvincial.getText()));
				JOptionPane.showMessageDialog(null,
						"Costos ingresados exitosamente!", "Exito", JOptionPane.DEFAULT_OPTION);
				botonIngresarCostos.setText("Actualizar costos");
				botonCarga.setEnabled(true);
				botonGenerarConexiones.setEnabled(true);
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