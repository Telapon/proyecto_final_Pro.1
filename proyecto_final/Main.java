// Raul Daniel Sanchez Sanchez | 2024-1755

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

// Clase abstracta para definir atributos comunes
abstract class AbstractUsuario {
    private String username;
    private String password;

    public AbstractUsuario(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() { 
        return username; 
    }
    
    public String getPassword() { 
        return password; 
    }
    
    // Método que se implementará en la subclase (Polimorfismo)
    public abstract void mostrarInfo();
}

// Clase Usuario que hereda de AbstractUsuario (Herencia) y utiliza encapsulamiento
class Usuario extends AbstractUsuario {
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;

    public Usuario(String username, String nombre, String apellido, String telefono, String email, String password) {
        super(username, password);
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
    }
    
    public String getNombre() { 
        return nombre; 
    }
    public String getApellido() { 
        return apellido; 
    }
    public String getTelefono() { 
        return telefono; 
    }
    public String getEmail() { 
        return email; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    public void setApellido(String apellido) { 
        this.apellido = apellido; 
    }
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    @Override
    public void mostrarInfo() {
        System.out.println("Usuario: " + getUsername() + ", Nombre: " + nombre + " " + apellido +
                           ", Teléfono: " + telefono + ", Email: " + email);
    }
}

// Interfaz que define operaciones para actualizar y eliminar usuarios
interface UserOperations {
    void actualizarUsuario(Usuario user);
    void eliminarUsuario(String username);
}

// Clase UserManager que administra la lista de usuarios y usa el patrón Singleton
class UserManager implements UserOperations {
    private ArrayList<Usuario> usuarios;
    
    // Variable para almacenar la única instancia de UserManager
    private static UserManager instancia = null;
    
    // Constructor privado para evitar instanciación desde fuera
    private UserManager() {
        usuarios = new ArrayList<>();
    }
    
    // Método estático para obtener la instancia única (Singleton)
    public static UserManager getInstance() {
        if (instancia == null) {
            instancia = new UserManager();
        }
        return instancia;
    }
    
    // Método para registrar un nuevo usuario (verifica que no exista previamente)
    public boolean registrarUsuario(Usuario usuario) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(usuario.getUsername())) {
                return false; // El usuario ya existe
            }
        }
        usuarios.add(usuario);
        return true;
    }
    
    // Método para validar el login
    public Usuario validarLogin(String username, String password) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
    
    public ArrayList<Usuario> obtenerUsuarios() {
        return usuarios;
    }
    
    // Actualizar los datos de un usuario
    @Override
    public void actualizarUsuario(Usuario user) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getUsername().equals(user.getUsername())) {
                usuarios.set(i, user);
                break;
            }
        }
    }
    
    // Eliminar un usuario por su nombre de usuario
    @Override
    public void eliminarUsuario(String username) {
        usuarios.removeIf(u -> u.getUsername().equals(username));
    }
}

// Nueva clase Producto usando encapsulamiento
class Producto {
    private String nombre;
    private String marca;
    private String categoria;
    private double precio;
    private int cantidad;

    public Producto(String nombre, String marca, String categoria, double precio, int cantidad) {
        this.nombre = nombre;
        this.marca = marca;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getMarca() { return marca; }
    public String getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
    public int getCantidad() { return cantidad; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}

// Gestor de productos usando Singleton
class ProductManager {
    private ArrayList<Producto> productos;
    private static ProductManager instancia = null;

    private ProductManager() {
        productos = new ArrayList<>();
    }

    public static ProductManager getInstance() {
        if (instancia == null) {
            instancia = new ProductManager();
        }
        return instancia;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void actualizarProducto(int index, Producto producto) {
        productos.set(index, producto);
    }

    public void eliminarProducto(int index) {
        productos.remove(index);
    }

    public ArrayList<Producto> obtenerProductos() {
        return productos;
    }
}

// Modelo para la tabla de productos
class ProductTableModel extends AbstractTableModel {
    private List<Producto> productos;
    private String[] columnNames = {"Nombre", "Marca", "Categoría", "Precio", "Cantidad"};

    public ProductTableModel(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public int getRowCount() { return productos.size(); }

    @Override
    public int getColumnCount() { return columnNames.length; }

    @Override
    public Object getValueAt(int row, int col) {
        Producto p = productos.get(row);
        switch(col) {
            case 0: return p.getNombre();
            case 1: return p.getMarca();
            case 2: return p.getCategoria();
            case 3: return p.getPrecio();
            case 4: return p.getCantidad();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public void actualizarDatos(List<Producto> productos) {
        this.productos = productos;
        fireTableDataChanged();
    }
}

// Ventana de gestión de productos
class ProductFrame extends JFrame {
    private ProductManager productManager;
    private JTable table;
    private ProductTableModel tableModel;
    private JButton btnNuevo, btnVolver;

    public ProductFrame(JFrame parentFrame) {
        productManager = ProductManager.getInstance();
        setTitle("Gestión de Productos");
        setSize(800, 400);
        setLocationRelativeTo(null);
        initComponents(parentFrame);
        setVisible(true);
    }

    private void initComponents(JFrame parentFrame) {
        tableModel = new ProductTableModel(productManager.obtenerProductos());
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editarProducto();
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        btnNuevo = new JButton("Nuevo Producto");
        btnNuevo.addActionListener(e -> new ProductFormFrame(this, -1));
        btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            dispose();
            parentFrame.setVisible(true);
        });

        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnVolver);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void editarProducto() {
        int row = table.getSelectedRow();
        if (row != -1) {
            new ProductFormFrame(this, row);
        }
    }

    public void actualizarTabla() {
        tableModel.actualizarDatos(productManager.obtenerProductos());
    }
}

// Formulario para productos
class ProductFormFrame extends JFrame {
    private JTextField tfNombre, tfMarca, tfCategoria, tfPrecio, tfCantidad;
    private JButton btnGuardar, btnEliminar;
    private ProductManager productManager;
    private int editIndex;
    private ProductFrame parentFrame;

    public ProductFormFrame(ProductFrame parent, int index) {
        this.parentFrame = parent;
        this.editIndex = index;
        this.productManager = ProductManager.getInstance();
        
        setTitle(index == -1 ? "Nuevo Producto" : "Editar Producto");
        setSize(400, 300);
        setLocationRelativeTo(null);
        initComponents();
        if (index != -1) {
            cargarDatosProducto();
        }
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        panel.add(new JLabel("Nombre:"));
        tfNombre = new JTextField();
        panel.add(tfNombre);

        panel.add(new JLabel("Marca:"));
        tfMarca = new JTextField();
        panel.add(tfMarca);

        panel.add(new JLabel("Categoría:"));
        tfCategoria = new JTextField();
        panel.add(tfCategoria);

        panel.add(new JLabel("Precio:"));
        tfPrecio = new JTextField();
        panel.add(tfPrecio);

        panel.add(new JLabel("Cantidad:"));
        tfCantidad = new JTextField();
        panel.add(tfCantidad);

        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarProducto());
        panel.add(btnGuardar);

        if (editIndex != -1) {
            btnEliminar = new JButton("Eliminar");
            btnEliminar.addActionListener(e -> eliminarProducto());
            panel.add(btnEliminar);
        }

        add(panel);
    }

    private void cargarDatosProducto() {
        Producto p = productManager.obtenerProductos().get(editIndex);
        tfNombre.setText(p.getNombre());
        tfMarca.setText(p.getMarca());
        tfCategoria.setText(p.getCategoria());
        tfPrecio.setText(String.valueOf(p.getPrecio()));
        tfCantidad.setText(String.valueOf(p.getCantidad()));
    }

    private void guardarProducto() {
        try {
            String nombre = tfNombre.getText().trim();
            String marca = tfMarca.getText().trim();
            String categoria = tfCategoria.getText().trim();
            double precio = Double.parseDouble(tfPrecio.getText().trim());
            int cantidad = Integer.parseInt(tfCantidad.getText().trim());

            if (nombre.isEmpty() || marca.isEmpty() || categoria.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
                return;
            }

            Producto producto = new Producto(nombre, marca, categoria, precio, cantidad);
            
            if (editIndex == -1) {
                productManager.agregarProducto(producto);
            } else {
                productManager.actualizarProducto(editIndex, producto);
            }
            
            parentFrame.actualizarTabla();
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio y cantidad deben ser números válidos");
        }
    }

    private void eliminarProducto() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar este producto?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            productManager.eliminarProducto(editIndex);
            parentFrame.actualizarTabla();
            dispose();
        }
    }
}

// Ventana para actualizar usuario
class UpdateUserFrame extends JFrame {
    private JTextField tfNombre, tfApellido, tfTelefono, tfEmail;
    private JButton btnGuardar;
    private UserManager userManager;
    private Usuario usuario;
    private UserTableModel tableModel;

    public UpdateUserFrame(UserManager userManager, Usuario usuario, UserTableModel tableModel) {
        this.userManager = userManager;
        this.usuario = usuario;
        this.tableModel = tableModel;
        setTitle("Actualizar Usuario: " + usuario.getUsername());
        setSize(400, 250);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        
        panel.add(new JLabel("Nombre:"));
        tfNombre = new JTextField(usuario.getNombre());
        panel.add(tfNombre);

        panel.add(new JLabel("Apellido:"));
        tfApellido = new JTextField(usuario.getApellido());
        panel.add(tfApellido);

        panel.add(new JLabel("Teléfono:"));
        tfTelefono = new JTextField(usuario.getTelefono());
        panel.add(tfTelefono);

        panel.add(new JLabel("Correo:"));
        tfEmail = new JTextField(usuario.getEmail());
        panel.add(tfEmail);

        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> actualizarUsuario());
        panel.add(btnGuardar);

        add(panel);
    }

    private void actualizarUsuario() {
        String nombre = tfNombre.getText().trim();
        String apellido = tfApellido.getText().trim();
        String telefono = tfTelefono.getText().trim();
        String email = tfEmail.getText().trim();

        if(nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        
        userManager.actualizarUsuario(usuario);
        tableModel.fireTableDataChanged();
        JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente");
        dispose();
    }
}

// Ventana principal que muestra el listado de usuarios y permite actualizar/eliminar
class MainFrame extends JFrame {
    private UserManager userManager;
    private JFrame loginFrame;

    public MainFrame(UserManager userManager, JFrame loginFrame) {
        this.userManager = userManager;
        this.loginFrame = loginFrame;
        setTitle("Menú Principal");
        setSize(400, 150);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 5, 5));
        
        JButton btnGestionUsuarios = new JButton("Gestión de Usuarios");
        btnGestionUsuarios.addActionListener(e -> mostrarGestionUsuarios());
        
        JButton btnGestionProductos = new JButton("Gestión de Productos");
        btnGestionProductos.addActionListener(e -> mostrarGestionProductos());
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        
        panelBotones.add(btnGestionUsuarios);
        panelBotones.add(btnGestionProductos);
        panelBotones.add(btnCerrarSesion);
        
        // Add some padding around the buttons
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(panelBotones, BorderLayout.CENTER);
        
        add(mainPanel);
    }

    private void cerrarSesion() {
        dispose();
        loginFrame.setVisible(true);
    }

    private void mostrarGestionUsuarios() {
        new UserManagementFrame(this, userManager);
        setVisible(false);
    }

    private void mostrarGestionProductos() {
        new ProductFrame(this);
        setVisible(false);
    }
}

// Nueva clase para separar la gestión de usuarios
class UserManagementFrame extends JFrame {
    private UserManager userManager;
    private JTable table;
    private UserTableModel tableModel;
    private JButton btnActualizar, btnEliminar, btnVolver;
    private JFrame parentFrame;

    public UserManagementFrame(JFrame parentFrame, UserManager userManager) {
        this.parentFrame = parentFrame;
        this.userManager = userManager;
        setTitle("Gestión de Usuarios");
        setSize(600, 400);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        tableModel = new UserTableModel(userManager.obtenerUsuarios());
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panelBotones = new JPanel();
        
        btnActualizar = new JButton("Actualizar Usuario");
        btnActualizar.addActionListener(e -> actualizarUsuario());
        
        btnEliminar = new JButton("Eliminar Usuario");
        btnEliminar.addActionListener(e -> eliminarUsuario());
        
        btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            dispose();
            parentFrame.setVisible(true);
        });

        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVolver);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void actualizarUsuario() {
        int selectedRow = table.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para actualizar");
            return;
        }
        String username = (String) tableModel.getValueAt(selectedRow, 0);
        Usuario usuario = null;
        for(Usuario u : userManager.obtenerUsuarios()) {
            if(u.getUsername().equals(username)) {
                usuario = u;
                break;
            }
        }
        if(usuario != null) {
            new UpdateUserFrame(userManager, usuario, tableModel);
        }
    }

    private void eliminarUsuario() {
        int selectedRow = table.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar");
            return;
        }
        String username = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Eliminar usuario " + username + "?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            userManager.eliminarUsuario(username);
            tableModel.setUsuarios(userManager.obtenerUsuarios());
            tableModel.fireTableDataChanged();
        }
    }
}

// Modelo de tabla para mostrar la lista de usuarios
class UserTableModel extends AbstractTableModel {
    private List<Usuario> usuarios;
    private String[] columnNames = {"Usuario", "Nombre Completo", "Teléfono", "Correo"};
    
    public UserTableModel(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    @Override
    public int getRowCount() {
        return usuarios.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Usuario u = usuarios.get(rowIndex);
        switch(columnIndex) {
            case 0: return u.getUsername();
            case 1: return u.getNombre() + " " + u.getApellido();
            case 2: return u.getTelefono();
            case 3: return u.getEmail();
            default: return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

// Clase principal que inicia el programa
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

// Ventana de Login
class LoginFrame extends JFrame {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton btnLogin, btnRegister;
    private UserManager userManager;

    public LoginFrame() {
        // Se obtiene la instancia única de UserManager
        userManager = UserManager.getInstance();
        setTitle("Inicio de Sesión");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }
    
    // Inicialización de componentes gráficos
    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Usuario:"));
        tfUsername = new JTextField();
        panel.add(tfUsername);
        
        panel.add(new JLabel("Contraseña:"));
        pfPassword = new JPasswordField();
        panel.add(pfPassword);
        
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.addActionListener(e -> login());
        panel.add(btnLogin);
        
        btnRegister = new JButton("Registrarse");
        // Abre la ventana de registro al presionar el botón
        btnRegister.addActionListener(e -> new RegistrationFrame(userManager, this));
        panel.add(btnRegister);
        
        add(panel, BorderLayout.CENTER);
    }
    
    // Lógica para iniciar sesión
    private void login() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar su usuario y contraseña, si no está registrado debe registrarse");
            return;
        }
        
        Usuario user = userManager.validarLogin(username, password);
        if (user != null) {
            // Si el login es correcto, se abre la ventana principal y se oculta el login
            new MainFrame(userManager, this);
            setVisible(false);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
        }
    }
    
    // Limpia los campos de texto
    public void clearFields() {
        tfUsername.setText("");
        pfPassword.setText("");
    }
    
    // Muestra la ventana de login
    public void mostrar() {
        setVisible(true);
    }
}

// Ventana de Registro
class RegistrationFrame extends JFrame {
    private JTextField tfUsername, tfNombre, tfApellido, tfTelefono, tfEmail;
    private JPasswordField pfPassword, pfConfirmPassword;
    private JButton btnSubmit;
    private UserManager userManager;
    private JFrame loginFrame;
    
    public RegistrationFrame(UserManager userManager, JFrame loginFrame) {
        this.userManager = userManager;
        this.loginFrame = loginFrame;
        setTitle("Registro");
        setSize(300, 200);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }
    
    // Inicialización de componentes del registro
    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.add(new JLabel("Nombre de Usuario:"));
        tfUsername = new JTextField();
        panel.add(tfUsername);
        
        panel.add(new JLabel("Nombre:"));
        tfNombre = new JTextField();
        panel.add(tfNombre);
        
        panel.add(new JLabel("Apellido:"));
        tfApellido = new JTextField();
        panel.add(tfApellido);
        
        panel.add(new JLabel("Teléfono:"));
        tfTelefono = new JTextField();
        panel.add(tfTelefono);
        
        panel.add(new JLabel("Correo:"));
        tfEmail = new JTextField();
        panel.add(tfEmail);
        
        panel.add(new JLabel("Contraseña:"));
        pfPassword = new JPasswordField();
        panel.add(pfPassword);
        
        panel.add(new JLabel("Confirmar Contraseña:"));
        pfConfirmPassword = new JPasswordField();
        panel.add(pfConfirmPassword);
        
        btnSubmit = new JButton("Registrar");
        btnSubmit.addActionListener(e -> registrar());
        
        add(panel, BorderLayout.CENTER);
        add(btnSubmit, BorderLayout.SOUTH);
    }
    
    // Lógica de registro
    private void registrar() {
        String username = tfUsername.getText().trim();
        String nombre = tfNombre.getText().trim();
        String apellido = tfApellido.getText().trim();
        String telefono = tfTelefono.getText().trim();
        String email = tfEmail.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();
        String confirmPassword = new String(pfConfirmPassword.getPassword()).trim();
        
        // Validaciones de cada campo
        if(username.isEmpty()){
            JOptionPane.showMessageDialog(this, "Falta el nombre de usuario");
            return;
        }
        if(nombre.isEmpty()){
            JOptionPane.showMessageDialog(this, "Falta el nombre");
            return;
        }
        if(apellido.isEmpty()){
            JOptionPane.showMessageDialog(this, "Falta el apellido");
            return;
        }
        if(telefono.isEmpty()){
            JOptionPane.showMessageDialog(this, "Falta el teléfono");
            return;
        }
        if(email.isEmpty()){
            JOptionPane.showMessageDialog(this, "Falta el correo");
            return;
        }
        if(password.isEmpty()){
            JOptionPane.showMessageDialog(this, "Falta la contraseña");
            return;
        }
        if(confirmPassword.isEmpty()){
            JOptionPane.showMessageDialog(this, "Falta confirmar la contraseña");
            return;
        }
        if(!password.equals(confirmPassword)){
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
            return;
        }
        
        Usuario nuevoUsuario = new Usuario(username, nombre, apellido, telefono, email, password);
        if(userManager.registrarUsuario(nuevoUsuario)){
            JOptionPane.showMessageDialog(this, "Registro exitoso");
            dispose(); // Cierra la ventana de registro
        } else {
            JOptionPane.showMessageDialog(this, "El usuario ya existe");
        }
    }
}
