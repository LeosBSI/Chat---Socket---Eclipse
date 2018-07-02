import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSocket {

	public static void main(String[] args) {

		ServerSocket servidor = null;
		try {
			System.out.println("Iniciando o servidor!");
			servidor = new ServerSocket(9998);
			System.out.println("Servidor iniciado!");

			while (true) {
				Socket cliente = servidor.accept();
				new GerenciadorDeClientes(cliente);
			}
		} catch (IOException e) {
			System.err.println("A porta está indisponível");
			try {
				if (servidor != null)
					servidor.close();
			} catch (IOException e1) {
			}
			e.printStackTrace();
		}
	}

}
