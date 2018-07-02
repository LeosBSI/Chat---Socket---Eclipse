import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClienteSocketSwing extends JFrame {

	private static final long serialVersionUID = -5261903818373181455L;
	private JTextArea taEditor = new JTextArea("Escreva aqui sua mensagem");
	private JTextArea taVisor = new JTextArea();
	private JList liUsuarios = new JList();
	private PrintWriter escritor;
	private BufferedReader leitor;
	private JScrollPane scrollTaVisor = new JScrollPane(taVisor);

	public ClienteSocketSwing() {
		setTitle("Chat Sockets - Redes e Sistemas");

		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		liUsuarios.setBackground(Color.cyan);
		taEditor.setBackground(Color.LIGHT_GRAY);

		taEditor.setPreferredSize(new Dimension(400, 40));
		// taVisor.setPreferredSize(new Dimension(350, 100));
		taVisor.setEditable(false);
		liUsuarios.setPreferredSize(new Dimension(100, 140));

		add(taEditor, BorderLayout.SOUTH);
		add(scrollTaVisor, BorderLayout.CENTER);
		add(new JScrollPane(liUsuarios), BorderLayout.WEST);

		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		String[] usuarios = new String[] { "ricardo", "leonardo", "andrey", "henrique" };
		preencherListaUsuarios(usuarios);
	}

	private void preencherListaUsuarios(String[] usuarios) {
		DefaultListModel modelo = new DefaultListModel();
		liUsuarios.setModel(modelo);
		for (String usuario : usuarios) {
			modelo.addElement(usuario);
		}

	}

	private void iniciarEscritor() {
		taEditor.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}


			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {


					if (taVisor.getText().isEmpty()) {
						return;
					}

					Object usuario = liUsuarios.getSelectedValue();
					if(usuario != null){
						taVisor.append("Eu:");
						taVisor.append(taEditor.getText());
						taVisor.append("\n");
						escritor.println(Comandos.MENSAGEM + usuario);
						escritor.println(taEditor.getText());
						taEditor.setText("");
						e.consume();
						
					}else{
						if (taVisor.getText().equalsIgnoreCase(Comandos.SAIR)) {
							System.exit(0);
						}
						JOptionPane.showMessageDialog(ClienteSocketSwing.this, "Selecione um usuário");
						return;					

					}

				}
			}
		});

	}

	public void iniciarChat() {
		try {
			final Socket cliente = new Socket("127.0.0.1", 9998);
			escritor = new PrintWriter(cliente.getOutputStream(), true);
			leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

			
		} catch (UnknownHostException e) {
			System.out.println("O endereço é inválido");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Conexão não foi possível");
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		final ClienteSocketSwing cliente = new ClienteSocketSwing();
		cliente.iniciarChat();
		cliente.iniciarEscritor();
		cliente.iniciarLeitor();
		
	}
	
	private void atulizarListaUsuarios() {
	escritor.println(Comandos.LISTA_USUARIOS);
	
		
	}

	private void iniciarLeitor() {
		try{
			while(true){
				String mensagem = leitor.readLine();
				if(mensagem == null || mensagem.isEmpty())
					continue;
				
				if(mensagem.equals(Comandos.LISTA_USUARIOS)){
					String[] usuarios = leitor.readLine().split(",");
					preencherListaUsuarios(usuarios);	
				}else if(mensagem.equals(Comandos.LOGIN)){
					String login = JOptionPane.showInputDialog("Insira seu login");
				    escritor.println(login);
				}else if(mensagem.equals(Comandos.LOGIN_NEGADO)){
					JOptionPane.showMessageDialog(ClienteSocketSwing.this, "O nome digitado é inválido");
				}else if(mensagem.equals(Comandos.LOGIN_ACEITO)){
					atulizarListaUsuarios();
				}
				else{
					taVisor.append(mensagem);
					taVisor.append("\n");
					taVisor.setCaretPosition(taVisor.getDocument().getLength());;
					
				}
					
				}
			
			
			}catch (IOException e) {
				System.out.println("Impossível ler a mensagem do servidor!");
				e.printStackTrace();
			}
	}
			
	
		

	private DefaultListModel getListaUsuarios() {
        return (DefaultListModel)liUsuarios.getModel();
	}

}
